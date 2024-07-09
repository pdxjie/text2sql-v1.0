<template>
  <div class="monaco-editor-box" :style="{ 'height': height }">
    <div class="monaco-editor" ref="monacoEditor"></div>
  </div>
</template>

<script>
import * as monaco from 'monaco-editor/esm/vs/editor/edcore.main'
import sqlKeywords from 'core-js/internals/array-iteration'
export default {
  name: 'MonacoEditor',
  props: {
    editorOptions: {
      type: Object,
      default: () => {
        return {
          selectOnLineNumbers: true,
          roundedSelection: false,
          readOnly: false, // 只读
          writeOnly: false,
          cursorStyle: 'line', // 光标样式
          automaticLayout: true, // 自动布局
          glyphMargin: true, // 字形边缘
          useTabStops: false,
          fontSize: 10, // 字体大小
          autoIndent: true, // 自动布局
          quickSuggestionsDelay: 500 // 代码提示延时
        }
      }
    },
    code: {
      type: String,
      default: () => {
        return ''
      }
    },
    language: {
      type: String,
      default: () => {
        return 'sql'
      }
    },
    readOnly: {
      type: Boolean,
      default: () => {
        return false
      }
    },
    height: {
      type: String,
      default: () => {
        return 400
      }
    },
    theme: {
      type: String,
      default: () => {
        return 'vs-dark'
      }
    },
    tableData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    databaseData: {
      type: Object,
      default: () => {
        return {
        }
      }
    }
  },
  data () {
    return {
      editor: null, // 文本编辑器
      isSave: true, // 文件改动状态，是否保存
      codeVal: null, // 保存后的文本
      suggestion: [] // 自动补全提示
    }
  },
  mounted () {
    this.initMonacoEditor()
    this.initEditor()
  },
  watch: {
    code (nVal) {
      console.log(this.isNeedFormatCode)
      if (this.editor) {
        if (nVal !== this.editor.getValue()) {
          this.editor.setValue(nVal)
          this.editor.getAction(['editor.action.formatDocument']).run()
          this.editor.setValue(this.editor.getValue())
        }
      }
    }
  },
  computed: {
  },
  methods: {
    // 关键字
    getSQLSuggest () {
      return sqlKeywords.map((key) => ({
        label: key,
        kind: monaco.languages.CompletionItemKind.Keyword,
        insertText: key,
        detail: 'keyword'
      }))
    },
    // 表名
    getTableSuggest (dbName) {
      const tableNames = this.databaseData[dbName]
      if (!tableNames) {
        return []
      }
      return tableNames.map((name) => ({
        label: name,
        kind: monaco.languages.CompletionItemKind.Constant,
        insertText: name,
        detail: dbName
      }))
    },
    // 字段名
    getParamSuggest (tableName) {
      const params = this.tableData[tableName]
      if (!params) {
        return []
      }
      return params.map((name) => ({
        label: name,
        kind: monaco.languages.CompletionItemKind.Constant,
        insertText: name,
        detail: 'param'
      }))
    },
    // 数据库名
    getDBSuggest () {
      return Object.keys(this.databaseData).map((key) => ({
        label: key,
        kind: monaco.languages.CompletionItemKind.Enum,
        insertText: key,
        detail: 'database'
      }))
    },
    initEditor () {
      // 自动补全提示
      this.suggestion = monaco.languages.registerCompletionItemProvider('sql', {
        // 触发条件，也可以不写，不写的话只要输入满足配置的label就会提示；仅支持单字符
        triggerCharacters: ['.', ' '],
        provideCompletionItems: (model, position) => {
          let suggestions = []
          const { lineNumber, column } = position
          const textBeforePointer = model.getValueInRange({
            startLineNumber: lineNumber,
            startColumn: 0,
            endLineNumber: lineNumber,
            endColumn: column
          })
          const tokens = textBeforePointer.toLocaleLowerCase().trim().split(/\s+/)
          const lastToken = tokens[tokens.length - 1] // 获取最后一段非空字符串
          if (lastToken.endsWith('.')) {
            // 提示该数据库下的表名
            const tokenNoDot = lastToken.slice(0, lastToken.length - 1)
            if (Object.keys(this.databaseData).includes(tokenNoDot)) {
              suggestions = [...this.getTableSuggest(tokenNoDot)]
            }
          } else if (lastToken === '.') {
            suggestions = []
          } else if (textBeforePointer.endsWith(' ')) {
            if (textBeforePointer.endsWith('select * from ')) {
              // select * from 提示指定数据库的表名
              suggestions = this.getTableSuggest(this.database)
            } else if (lastToken === 'where') {
              const lastToken2 = tokens[tokens.length - 2]
              const lastToken3 = tokens[tokens.length - 3]
              const lastToken4 = tokens[tokens.length - 4]
              const lastToken5 = tokens[tokens.length - 5]
              if (lastToken5 + lastToken4 + lastToken3 === 'select*from') {
                // select * from tableName where 提示指定表的字段名
                suggestions = [...this.getParamSuggest(lastToken2)]
              } else {
                suggestions = []
              }
            } else {
              suggestions = []
            }
          } else {
            // 提示数据库名和关键词
            suggestions = [...this.getDBSuggest(), ...this.getSQLSuggest()]
          }
          return {
            suggestions
          }
        }
      })
    },
    initMonacoEditor () {
      const that = this
      this.editor = monaco.editor.create(that.$refs.monacoEditor, {
        value: that.codeVal || that.code, // 编辑器初始显示的内容
        language: that.language, // 支持的语言
        theme: that.theme, // 编辑器主题样式
        selectOnLineNumbers: true, // 显示行号
        editorOptions: that.editorOptions,
        colorDecorators: true, // 颜色装饰器
        minimap: {
          enabled: false // 不要小地图
        },
        foldingStrategy: 'indentation', // 代码可分小段折叠
        fontSize: 16,
        'semanticHighlighting.enabled': 'configuredByTheme',
        trimAutoWhitespace: true,
        autoIndent: true,
        quickSuggestionsDelay: 200,
        autoClosingBrackets: 'always', // 是否自动添加结束括号(包括中括号) "always" | "languageDefined" | "beforeWhitespace" | "never"
        autoClosingDelete: 'always', // 是否自动删除结束括号(包括中括号) "always" | "never" | "auto"
        readOnly: that.readOnly // 是否只读
      })
      that.editor.onDidChangeModelContent(function (event) {
        // 编辑器内容 change 事件
        that.codeVal = that.editor.getValue()
      })
    }
  }
}
</script>

<style scoped lang='less'>
.monaco-editor {
  height: 100% !important;
}
</style>

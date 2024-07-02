<template>
  <div class="monaco-editor-box" :style="{ 'height': height }">
    <div class="monaco-editor" ref="monacoEditor"></div>
  </div>
</template>

<script>
import * as monaco from 'monaco-editor/esm/vs/editor/edcore.main'
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
    }
  },
  data () {
    return {
      editor: null, // 文本编辑器
      isSave: true, // 文件改动状态，是否保存
      codeVal: null // 保存后的文本
    }
  },
  mounted () {
    this.initMonacoEditor()
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
  methods: {
    initMonacoEditor () {
      const that = this
      this.editor = monaco.editor.create(that.$refs.monacoEditor, {
        value: that.codeVal || that.code, // 编辑器初始显示的内容
        language: that.language, // 支持的语言
        theme: 'vs-dark', // 编辑器主题样式
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

<template>
  <div class="w-full" style="padding: 10px">
    <a-row :gutter="20">
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
        <a-card title="Schema" style="border-radius: 8px;height: calc(100vh - 210px);overflow-y: auto;position: relative;" :loading="treeLoading">
          <div slot="extra" style="display: flex;gap: 10px" v-if="isLogin">
            <a-button type="primary" ghost icon="import" @click="importSchema">导入表</a-button>
            <a-button type="primary" ghost icon="plus" @click="createDatabase">新建数据库</a-button>
          </div>
          <a-row :gutter="20" style="position:relative;">
            <a-col
              :xs="7"
              :sm="7"
              :md="7"
              :lg="7"
              :xl="7"
              style="position:sticky;top: 65px!important;">
              <div class="h-full" style="border-right: 1px solid #eee;position:sticky;top: 0;">
                <a-tree
                  ref="treeRef"
                  v-if="treeNodes.length > 0"
                  :default-selected-keys="[selectedSubNode]"
                  :defaultExpandedKeys="[expandNode]"
                  v-click-outside="hideOperate"
                  :tree-data="treeNodes"
                  :expandedKeys="expandNodes"
                  :blockNode="true"
                  @select="onSelect"
                  @expand="onCheck">
                  <template slot="custom" slot-scope="item">
                    <div class="display-flex align-items justify-between" @mouseenter="showOperate(item)">
                      <span>
                        <a-icon :type="item.leaf ? 'table' : 'database'"></a-icon>
                        <span style="margin-left: 5px">{{ item.title }}</span>
                        <span v-if="!item.leaf"> ({{ item.children ? item.children.length : 0 }})</span>
                      </span>
                      <div class="display-flex align-items" style="gap: 10px" v-if="item.key === currentKey">
                        <a-tooltip placement="top">
                          <template slot="title">
                            <span>添加数据库表</span>
                          </template>
                          <a-icon type="plus" v-if="!item.leaf && item.type === 'NORMAL'" @click="creatSchemaInfo(item)"></a-icon>
                        </a-tooltip>
                        <a-badge status="processing" v-if="item.leaf" />
                        <a-dropdown :overlayStyle="{ width: '120px', 'border-radius': '4px' }">
                          <a class="ant-dropdown-link" @click="e => e.preventDefault()">
                            <a-icon type="ellipsis"></a-icon>
                          </a>
                          <a-menu slot="overlay">
                            <a-menu-item @click="generateData(item)" v-if="item.leaf" disabled>
                              <span>
                                <a-icon type="api"></a-icon>
                                <span>测试数据</span>
                              </span>
                            </a-menu-item>
                            <a-menu-item @click="moveDatabaseAndSchema(item)" :disabled="item.type === 'SYSTEM' || (item.children && item.children.length === 0)">
                              <span>
                                <a-icon type="pull-request"></a-icon>
                                <span v-if="item.leaf">迁移 Schema</span>
                                <span v-if="!item.leaf">迁移数据库</span>
                              </span>
                            </a-menu-item>
                            <a-menu-item @click="importSchemaSQL(item)" disabled>
                              <span>
                                <a-icon type="import"></a-icon>
                                <span>导出 SQL</span>
                              </span>
                            </a-menu-item>
                            <a-menu-divider />
                            <a-menu-item style="color: red" @click="removeSchema(item)" :disabled="item.type === 'SYSTEM'">
                              <span>
                                <a-icon type="delete"></a-icon>
                                <span>删除</span>
                              </span>
                            </a-menu-item>
                          </a-menu>
                        </a-dropdown>
                      </div>
                    </div>
                  </template>
                </a-tree>
              </div>
            </a-col>
            <a-col
              :xs="17"
              :sm="17"
              :md="17"
              :lg="17"
              :xl="17"
              v-if="schemaDetail"
              style="position:relative;">
              <a-spin tip="Loading..." :spinning="schemaInfoLoading">
                <div class="display-flex align-items margin-b-10" style="gap: 15px">
                  <div>
                    <span><span style="color: red;margin-right: 5px">*</span>数据库名: </span>
                    <a-input v-model="schemaDetail.databaseName" placeholder="请输入数据库名"/>
                  </div>
                  <div>
                    <span><span style="color: red;margin-right: 5px">*</span>数据表名: </span>
                    <a-input v-model="schemaDetail.schemaName" placeholder="请输入数据库表名"/>
                  </div>
                  <div>
                    <span><span style="color: red;margin-right: 5px">*</span>注释: </span>
                    <a-input v-model="schemaDetail.remark" placeholder="请输入数据库表注释"/>
                  </div>
                </div>
                <a-collapse v-model="activeKey">
                  <a-collapse-panel :key="item.id" v-for="(item) in schemaDetail.fields">
                    <span slot="header" style="font-size: 13px;color: #676879">
                      字段名: <a-input style="width: 150px;" v-model="item.fieldName" placeholder="请输入字段名"/>
                    </span>
                    <div class="filed-content" style="font-size: 13px;color: #676879">
                      <div class="display-flex align-items" style="gap: 10px;">
                        <div class="display-flex flex-column field-type">
                          <span><span style="color: red;margin-right: 5px">*</span>字段类型: </span>
                          <a-select v-model="item.fieldType" @change="handleChangeFieldType" placeholder="请选择字段类型">
                            <a-select-option :value="type.code" v-for="type in fieldTypes" :key="type.code">
                              {{ type.type }}
                            </a-select-option>
                          </a-select>
                        </div>
                        <div>
                          <span>默认值: </span>
                          <a-input v-model="item.fieldDefault"/>
                        </div>
                        <div>
                          <span>字段注释: </span>
                          <a-input v-model="item.fieldComment"/>
                        </div>
                      </div>
                      <div class="display-flex align-items" style="gap: 10px;margin-top: 10px">
                        <a-checkbox :checked="item.primaryKey" @change="changePrimaryValue(item, $event)">
                          主键
                        </a-checkbox>
                        <a-checkbox :checked="item.null" @change="changeIsNullValue(item, $event)">
                          非空
                        </a-checkbox>
                      </div>
                    </div>
                    <a-icon slot="extra" style="color: red" type="delete" @click.stop="handleRemoveField" />
                  </a-collapse-panel>
                </a-collapse>
                <a-card style="border: transparent!important;position: sticky;bottom: 0px" class="bottom-operate">
                  <div class="display-flex justify-between" style="gap: 10px">
                    <a-button block icon="plus" :disabled="schemaDetail.system" @click="addField">新增字段</a-button>
                    <a-button block icon="save" :disabled="schemaDetail.system" @click="saveSchema">保存表</a-button>
                  </div>
                </a-card>
              </a-spin>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12">
        <a-card title="Generate SQL" style="border-radius: 8px;height: calc(100vh - 210px);overflow-y: auto">
          <a-button type="primary" ghost slot="extra" icon="solution" @click="executeRecords">执行记录</a-button>
          <a-button type="primary" slot="extra" icon="code" @click="randomExample" style="margin-left: 10px">模范示例</a-button>
          <!-- 选择 Schema -->
          <div class="schema-body">
            <div class="display-flex align-items margin-b-10" style="gap: 10px;">
              <a-switch size="small" v-model="schema" @change="onChange" />
              <span style="color: #676879;font-size: 13px">{{ description }}</span>
            </div>
            <a-form :form="schemaForm" v-if="!schema">
              <a-form-item>
                <a-textarea
                  placeholder="请输入您所需要的数据库表结构, 例如：create table User( id, username, password, phone, brith)"
                  :auto-size="{ minRows: 5, maxRows: 5 }"
                  v-decorator="['schemas', { rules: [{ required: true, message: '数据库表内容不能为空！' }] }]"
                  style="border-radius: 8px;"/>
              </a-form-item>
            </a-form>
            <div v-else class="schema-select">
              <a-select v-model="currentDatabaseId" style="width: 15%;border-radius: 10px;" @change="handleChange">
                <a-select-option :value="data.key" v-for="data in databases" :key="data.key">
                  {{ data.title }}
                </a-select-option>
              </a-select>
              <a-select
                v-model="selectedSchemaIds"
                style="width: 80%;border-radius: 10px;margin-left: 15px"
                mode="multiple"
                :maxTagCount="4"
                placeholder="请选择需求所涉及到的表结构"
                @change="handleChangeSchemas">
                <a-select-option :value="schema.key" v-for="schema in schemas" :key="schema.key">
                  {{ schema.title }}
                </a-select-option>
              </a-select>
            </div>
          </div>
          <!-- 输入需求 -->
          <a-form :form="form">
            <a-form-item label="写下想要实现的需求">
              <a-textarea
                placeholder="写下您想要通过 SQL 实现的需求"
                :auto-size="{ minRows: 5, maxRows: 5 }"
                v-decorator="['demand', { rules: [{ required: true, message: '需求内容不能为空！' }] }]"
                style="border-radius: 8px;"/>
            </a-form-item>
          </a-form>
          <div class="w-full display-flex align-items justify-between" style="gap: 6px">
            <a-badge count="限时免费" style="width: 30%">
              <a-button class="ai-btn" style="width: 100%" :icon="analysising ? 'loading' : 'thunderbolt'" :disabled="analysising" @click="startAnalysis">开始分析</a-button>
            </a-badge>
            <a-select v-model="currentType" style="width: 30%;border-radius: 10px;" @change="handleChangeSqlType">
              <a-select-option :value="item.id" v-for="item in sqlTypes" :key="item.id">
                {{ item.value }}
              </a-select-option>
            </a-select>
            <a-button type="primary" style="width: 30%;border-radius: 10px;height: 40px" ghost icon="deployment-unit" @click="toWorkFlow">SQL 工作流</a-button>
          </div>
          <a-divider>最终结果</a-divider>
          <div class="content">
            <MonacoEditor :read-only="true" ref="referenceAnswer" height="300px" :code="code" language="sql"/>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <a-modal v-model="importSchemaSqlVisible" @ok="handleImportSchema" :confirmLoading="importLoading" :bodyStyle="{ 'padding-top': '5px!important' }">
      <template slot="title">
        <span class="display-flex align-items" style="gap: 6px">
          <span>导入表 SQL</span>
          <a-popover placement="right">
            <template slot="content">
              <img src="~@/assets/rule.png" style="width: 100%;height: 300px" alt="rule">
            </template>
            <template slot="title">
              <span style="font-size: 12px">导入格式规范</span>
            </template>
            <a-icon type="info-circle" />
          </a-popover>
        </span>
      </template>
      <span style="font-size: 13px;color: #ccc;margin-bottom: 5px;display: inline-block">请严格按照规格将数据库表导入到平台中，否则会出现 <span style="color: red">导入失败</span> ！</span>
      <div class="w-full">
        <a-select :default-value="importDatabaseId" placeholder="请选择您想导入数据库" class="database-selected" style="width: 100%" @change="handleChangeImportDatabase">
          <a-select-option :value="'0'">
            新建数据库(默认)
          </a-select-option>
          <a-select-option :value="data.key" v-for="data in databases" :key="data.key" :disabled="data.type === 'SYSTEM'">
            {{ data.title }}
          </a-select-option>
        </a-select>
      </div>
      <a-textarea v-model="importSchemaContent" placeholder="每个表结构之间使用分号隔开" :auto-size="{ minRows: 10, maxRows: 10 }"/>
    </a-modal>
    <a-modal v-model="createDatabaseVisible" title="创建数据库" @ok="handleCreateDatabase">
      <a-form :form="createDatabaseForm">
        <a-form-item label="数据库名称">
          <a-input
            v-decorator="['databaseName', { rules: [{ required: true, message: '请输入数据库名称！' }] }]"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal v-model="moveDatabaseAndSchemaVisible" title="迁移数据库" @ok="handleMove" @cancle="handleCancelMove">
      <a-form :form="moveForm">
        <a-form-item label="数据库名称">
          <a-select
            placeholder="请选择要迁移到的目标数据库"
            style="width: 100%"
            v-decorator="['targetDatabaseId', { rules: [{ required: true, message: '请选择要迁移到的目标数据库！' }] }]">
            <a-select-option :value="data.key" v-for="data in databases" :key="data.key" :disabled="data.type === 'SYSTEM'">
              {{ data.title }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
import { exampleRules } from '@/constants/example'
import ClickOutside from 'vue-click-outside'
import { sqlTypes } from '@/constants/types'
import { databaseTreeNodes, deleteDatabase, insertDatabase, streamApi } from '@/api/database'
import { autoImportSchema, deleteSchemaInfo, saveSchemaInfo, schemaDetailApi, moveDatabaseAndSchema } from '@/api/schema'
import { fieldTypes } from '@/constants/fieldTypes'
import { v4 as uuidv4 } from 'uuid'
import { eventSource } from '@/utils/eventSource'
import MonacoEditor from '@/components/MonacoEditor/index.vue'
export default {
  name: 'Dashboard',
  components: {
    MonacoEditor
  },
  directives: {
    ClickOutside
  },
  data () {
    return {
      form: this.$form.createForm(this, { name: 'coordinated' }), // 表单
      schema: false, // 是否选择表结构
      description: '输入您所需要的数据库表',
      schemaForm: this.$form.createForm(this, { name: 'schema' }),
      code: '-- 使用 AI 轻松生成优化的 SQL 查询，提高您的 SQL 技能并节省时间',
      sqlTypes,
      currentType: '1',
      activeKey: ['1'],
      importSchemaSqlVisible: false, // 是否显示导入表SQL弹窗
      treeNodes: [],
      expandNode: '', // 当前展开的节点
      selectedSubNode: '', // 当前选择的子节点
      currentKey: '', // 当前操作的节点
      expandNodes: [], // 当前展开的节点
      fieldTypes, // 字段类型
      schemaDetail: null, // 当前表的字段列表
      createDatabaseVisible: false, // 创建数据库表弹窗
      createDatabaseForm: this.$form.createForm(this, { name: 'createDatabase' }),
      databases: [], // 数据库列表
      schemas: [], // 当前数据库表列表
      currentDatabaseId: '', // 当前数据库 ID
      selectedSchemaIds: [], // 选中的数据库表 ID 集合
      analysising: false, // 是否正在分析中
      exampleRules, // 示例规则
      importSchemaContent: '', // 导入表 SQL 内容
      importLoading: false, // 导入表 SQL 是否正在加载
      schemaInfoLoading: false, // 获取表结构信息是否正在加载
      treeLoading: false, // 获取数据库树节点是否正在加载
      importDatabaseId: '0', // 导入选择的数据库 ID
      moveDatabaseAndSchemaVo: {
        databaseId: '', // 源数据库 ID
        targetDatabaseId: '', // 目标数据库 ID
        schemaId: '' // 迁移的数据库表 ID
      }, // 迁移请求体
      moveDatabaseAndSchemaVisible: false, // 迁移数据库表弹窗
      moveForm: this.$form.createForm(this, { name: 'moveForm' })
    }
  },
  created () {
    this.getDatabaseTreeNodes()
    const showNotification = localStorage.getItem('FUNCTIONAL_ITERATION')
    if (!showNotification || JSON.parse(showNotification)) {
      this.openNotification()
    }
  },
  computed: {
    isLogin () {
      return this.$store.getters.token
    }
  },
  methods: {
    async getDatabaseTreeNodes (insertOrUpdate = false) {
      this.treeLoading = true
      const { data } = await databaseTreeNodes()
      data.treeNodes.forEach(node => {
        this.$set(node, 'scopedSlots', {
          title: 'custom'
        })
        node.children && node.children.forEach(child => {
          this.$set(child, 'scopedSlots', {
            title: 'custom'
          })
        })
      })
      this.treeNodes = data.treeNodes
      this.treeLoading = false
      // 处理数据
      this.databases = data.treeNodes
      this.schemas = this.databases[0].children
      this.currentDatabaseId = this.databases[0].key
      this.selectedSchemaIds.push(this.schemas[0].key)
      if (!insertOrUpdate) {
        // 获取第一个节点
        const firstNode = data.treeNodes[0]
        // 获取第一个节点的第一个子节点
        const firstSubNode = firstNode.children[0]
        // 重置展开的节点
        this.expandNodes = []
        this.expandNodes.push(firstNode.key)
        this.selectedSubNode = firstSubNode.key
        this.expandNode = firstNode.key
        await this.getSchemaDetailInfo()
      }
    },
    showOperate (item) {
      this.currentKey = item.key
    },
    hideOperate () {
      this.currentKey = ''
    },
    removeSchema (item) {
      const itemId = item.key
      const that = this
      let tips = '温馨提示: 数据库一旦删除之后，其下的数据库表结构也将一同删除，并将无法恢复!'
      if (item.leaf) {
        tips = '温馨提示: 数据库表一旦删除之后，其下的表字段也将一同删除，并将无法恢复!'
      }
      this.$confirm({
        title: '确定要删除该数据库表?',
        content: tips,
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        async onOk () {
          if (!item.leaf) {
            const vo = {
              databaseId: itemId,
              force: true
            }
            const data = await deleteDatabase(vo)
            if (data.code === 200) {
              await that.getDatabaseTreeNodes()
              await that.$message.success('删除成功')
            }
          } else {
            const data = await deleteSchemaInfo(itemId)
            if (data.code === 200) {
              await that.getDatabaseTreeNodes()
              await that.$message.success('删除成功')
            }
          }
        },
        onCancel () {
          that.$message.info('已取消删除')
        }
      })
    },
    // 添加数据库表结构
    creatSchemaInfo (item) {
      this.expandNodes = [item.key]
      this.expandNode = item.key
      const uuid = uuidv4()
      const schemaDetail = {
        databaseId: item.key,
        databaseName: item.title,
        schemaName: '',
        remark: '',
        schemaId: '',
        fields: [
          {
            id: uuid,
            fieldName: '',
            fieldType: 1,
            fieldDefault: '',
            fieldComment: '',
            primaryKey: false,
            null: false
          }
        ]
      }
      this.activeKey = []
      this.activeKey.push(uuid)
      this.$set(this, 'schemaDetail', schemaDetail)
    },
    addField () {
      const fields = this.schemaDetail.fields
      if (fields.length > 0) {
        const uuid = uuidv4()
        const newField = {
          id: uuid,
          fieldName: '',
          fieldType: 1,
          fieldDefault: '',
          fieldComment: '',
          primaryKey: false,
          null: false
        }
        fields.push(newField)
        this.activeKey.push(uuid)
      }
    },
    async saveSchema () {
      // 校验参数
      if (!this.schemaDetail.databaseName || !this.schemaDetail.schemaName) {
        await this.$message.error('请输入数据库表名称')
        return
      }
      let flag = false
      if (this.schemaDetail) {
        this.schemaDetail.fields.forEach(field => {
          if (!field.fieldName || !field.fieldType || !field.fieldComment) {
            flag = true
          }
        })
        if (flag) {
          this.$message.error('请补充字段名称、字段类型、字段注释')
          return
        }
      }
      const { data } = await saveSchemaInfo(this.schemaDetail)
      // 重置展开的节点
      this.expandNodes = []
      this.expandNodes.push(data.databaseId)
      this.selectedSubNode = data.schemaId
      this.expandNode = data.databaseId
      await this.getDatabaseTreeNodes(true)
      this.$refs.treeRef.$forceUpdate()
    },
    importSchemaSQL (item) {},
    // 确定导入
    async handleImportSchema () {
      if (!this.importSchemaContent) {
        this.$message.error('请输入建表 SQL 内容')
        return
      }
      if (this.countSemilons(this.importSchemaContent) > 5) {
        this.$message.error('一次性最多导入 5 个数据库表')
        return
      }
      this.importLoading = true
      if (this.importDatabaseId === '0') {
        this.importDatabaseId = ''
      }
      const vo = {
        databaseId: this.importDatabaseId,
        importContent: this.importSchemaContent,
        moreImport: this.countSemilons(this.importSchemaContent) > 1
      }
      const data = await autoImportSchema(vo)
      if (data.code === 200) {
        this.$message.success('导入成功')
        await this.getDatabaseTreeNodes()
      } else {
        this.$message.error(data.message)
      }
      this.importLoading = false
      this.importSchemaContent = ''
      this.importDatabaseId = ''
      this.importSchemaSqlVisible = false
    },
    // 导入建表 SQL
    importSchema () {
      this.importSchemaSqlVisible = true
    },
    // 创建数据库
    createDatabase () {
      this.createDatabaseVisible = true
    },
    handleChangeFieldType (value) {},
    onChange (value) {
      this.schema = value
      if (value) {
        this.description = '选择您所需要的数据库表'
      } else {
        this.description = '输入您所需要的数据库表'
      }
      this.$nextTick(() => {
        this.form.setFieldsValue({
          demand: ''
        })
      })
    },
    handleChange (value) {
      this.currentDatabaseId = value
      const currentNode = this.treeNodes.find(node => node.key === value)
      if (currentNode) {
        this.schemas = currentNode.children
        if (this.schemas.length > 0) {
          const schemaId = this.schemas[0].key
          this.selectedSchemaIds = [schemaId]
        } else {
          this.selectedSchemaIds = []
        }
      }
    },
    // 分号数量
    countSemilons (content) {
      // 使用正则表达式匹配所有中文分号和英文分号
      const matches = content.match(/;|；/g)
      // 如果没有匹配项，则返回0，否则返回匹配项的数量
      return matches ? matches.length : 0
    },
    handleChangeSchemas (values) {
      this.selectedSchemaIds = values
    },
    handleChangeSqlType (value) {
      this.currentType = value
    },
    // 开始分析
    async startAnalysis () {
      if (!this.isLogin) {
        this.$message.warning('请先登录')
        this.$router.push({
          name: 'login'
        })
        return
      }
      this.code = ''
      // 判断是根据什么方式来进行分析
      // 为 true 时，按照系统数据库表结构分析
      if (this.schema) {
        if (this.currentDatabaseId.trim() === '') {
          this.$message.warning('请选择数据库个人数据库!')
          return
        }
        if (this.selectedSchemaIds.length === 0) {
          this.$message.warning('请选择数据库表结构!')
          return
        }
        this.form.validateFields((err, values) => {
          if (!err) {
            this.analysising = true
            const vo = {
              databaseId: this.currentDatabaseId.trim(),
              schemaIds: this.selectedSchemaIds,
              sqlDriver: this.currentType,
              demand: values.demand,
              type: 'SYSTEM'
            }
            const messageFun = (message) => {
              const data = JSON.parse(message.data)
              if (data.finish_reason && data.finish_reason === 'stop') {
                this.$message.success('分析结束！')
                this.analysising = false
                return
              }
              let content = data.delta.content
              content = content.replace('```', '')
              this.code = this.code + content
            }
            eventSource(streamApi.generateSQL, vo, messageFun, 'POST', vo).then(res => {
            })
          }
        })
      } else {
        const schemaData = await this.schemaForm.validateFields()
        const schemas = schemaData.schemas
        const demandData = await this.form.validateFields()
        const demand = demandData.demand
        if (schemas && demand) {
          this.analysising = true
          const vo = {
            schemas: schemas,
            sqlDriver: this.currentType,
            demand: demand,
            type: 'CUSTOM'
          }
          const messageFun = (message) => {
            const data = JSON.parse(message.data)
            if (data.finish_reason && data.finish_reason === 'stop') {
              this.$message.success('分析结束！')
              this.analysising = false
              return
            }
            let content = data.delta.content
            content = content.replace('```', '')
            this.code = this.code + content
          }
          eventSource(streamApi.generateSQL, vo, messageFun, 'POST', vo).then(res => {
          })
        }
      }
    },
    // 随机示例
    randomExample () {
      if (this.schema) {
        this.schema = false
      }
      // 1 - 4 随机数
      const random = Math.floor(Math.random() * 4) + 1
      const example = this.exampleRules[random]
      this.$nextTick(() => {
        this.schemaForm.setFieldsValue({
          schemas: example.content
        })
      })
    },
    handleCreateDatabase () {
      this.createDatabaseForm.validateFields(async (err, values) => {
        if (!err) {
          if (this.containsChinese(values.databaseName)) {
            this.$message.warning('数据库名称不能包含中文, 请重新数据库名称!')
          }
          const vo = {
            databaseName: values.databaseName
          }
          const data = await insertDatabase(vo)
          if (data.code === 200 && data.message) {
            this.$message.success('创建数据库成功!')
            this.getDatabaseTreeNodes()
            this.createDatabaseVisible = false
          } else {
            this.$message.warn(data.message)
          }
        }
      })
    },
    // 是否包含中文
    containsChinese (content) {
      const regex = /[\u4e00-\u9fa5]/
      return regex.test(content)
    },
    generateData (item) {},
    onSelect (selectedKeys, { node }) {
      const currentNode = selectedKeys[0]
      if (!node.isLeaf2() && currentNode) {
        if (this.expandNodes.length !== 0) {
          const existNode = this.expandNodes[0]
          if (existNode === currentNode) {
            this.expandNodes = []
            return
          } else {
            this.expandNodes = []
            this.expandNodes.push(currentNode)
          }
        }
        this.expandNodes = selectedKeys
      } else if (currentNode) {
        const [node] = this.treeNodes.filter(node => currentNode === node.key)
        // 判断当前节点是否是父节点, 如果是父节点，则不需要获取数据
        if (!node) {
          this.selectedSubNode = currentNode
          this.expandNode = this.expandNodes[0]
          this.getSchemaDetailInfo()
        } else {
          // 并将当前节点设置为展开节点，子节点置为空
          this.expandNode = currentNode
          this.selectedSubNode = ''
        }
      }
    },
    async getSchemaDetailInfo () {
      if (this.expandNode === '' || this.selectedSubNode === '') {
        return
      }
      this.schemaInfoLoading = true
      const vo = {
        dataId: this.expandNode,
        schemaId: this.selectedSubNode
      }
      const { data } = await schemaDetailApi(vo)
      if (data) {
        this.schemaDetail = data.schemaDetail
        if (this.schemaDetail.fields.length !== 0) {
          const activeKey = []
          this.schemaDetail.fields.forEach(field => {
            activeKey.push(field.id)
          })
          this.activeKey = activeKey
        }
      }
      this.schemaInfoLoading = false
    },
    onCheck (checkedKeys, info) {
      if (this.expandNodes.length !== 0) {
        const existNode = this.expandNodes[0]
        let newNode = checkedKeys[0]
        if (!newNode) {
          newNode = checkedKeys[1]
        }
        if (existNode === newNode) {
          this.expandNodes = []
        } else {
          this.expandNodes = []
          this.expandNodes.push(newNode)
        }
      } else {
        this.expandNodes = []
        let newNode = checkedKeys[0]
        if (!newNode) {
          newNode = checkedKeys[1]
        }
        this.expandNodes.push(checkedKeys[0])
      }
    },
    handleClick () {},
    changePrimaryValue (item, event) {
      const checked = event.target.checked
      this.$set(item, 'primaryKey', checked)
    },
    changeIsNullValue (item, event) {
      const checked = event.target.checked
      this.$set(item, 'null', checked)
    },
    // 选择导入的数据库
    handleChangeImportDatabase (value) {
      this.importDatabaseId = value
    },
    // 迁移数据库
    moveDatabaseAndSchema (item) {
      if (item.leaf) {
        this.moveDatabaseAndSchemaVo.databaseId = this.expandNode
        this.moveDatabaseAndSchemaVo.schemaId = item.key
      } else {
        this.moveDatabaseAndSchemaVo.databaseId = item.key
      }
      this.moveDatabaseAndSchemaVisible = true
    },
    handleMove () {
      this.moveForm.validateFields(async (err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          const targetDatabaseId = values.targetDatabaseId
          this.moveDatabaseAndSchemaVo.targetDatabaseId = targetDatabaseId
          const data = await moveDatabaseAndSchema(this.moveDatabaseAndSchemaVo)
          if (data.code === 200) {
            this.$message.success('迁移成功！')
            this.getDatabaseTreeNodes()
            this.handleCancelMove()
          } else {
            this.$message.error(data.message)
            this.handleCancelMove()
          }
        }
      })
    },
    // 取消迁移
    handleCancelMove () {
      this.moveDatabaseAndSchemaVo = {
        databaseId: '',
        schemaId: '',
        targetDatabaseId: ''
      }
      this.moveForm.resetFields()
      this.moveDatabaseAndSchemaVisible = false
    },
    executeRecords () {
      this.$message.warning('新功能正在开发中，敬请期待！')
    },
    openNotification () {
      this.$notification['success']({
        message: '迭代 v1.0.1 提示',
        duration: null,
        description:
          (<span>🐞 修复开启选择数据库表模式时不选择数据库时生成 SQL 页面卡死问题。<br/>🚀 新增导入 SQL 时可自由选择导入的数据库功能。<br/>🚀 新增数据库整体迁移或单个数据库表迁移功能。</span>),
        onClose: () => {
          localStorage.setItem('FUNCTIONAL_ITERATION', false)
        }
      })
    },
    handleRemoveField () {
      this.$message.warning('新功能正在开发中，敬请期待！')
    },
    // 跳转到工作流
    toWorkFlow () {
      // 只有登录之后才能跳转到工作流
      if (!this.isLogin) {
        this.$message.warning('请先登录')
        return
      }
      if (!this.schema) {
        this.$message.warning('请切换工作模式')
        return
      }
      this.$router.push({
        name: 'Workflow'
      })
    }
  }
}
</script>
<style lang="less" scoped>
  .schema-select {
    ::v-deep .ant-select-selection--multiple {
      border-radius: 8px!important;
      height: 30px;
    }
    ::v-deep .ant-select-selection--single {
      border-radius: 8px!important;
      height: 30px;
    }
    ::v-deep .ant-select-selection__rendered {
      line-height: 30px!important;
    }
  }
  ::v-deep .ant-select-selection--single {
    border-radius: 8px!important;
    height: 40px;
  }
  ::v-deep .ant-select-selection__rendered {
    line-height: 38px!important;
  }
  .content {
    border: 1px solid #eee;
    border-radius: 8px;
    height: 100px;
    ::v-deep .CodeMirror {
      height: 150px;
    }
  }
  .ai-btn {
    background: linear-gradient(to right, rgb(127, 0, 255), rgb(225, 0, 255));
    color: #fff;
    height: 40px;
  }
  ::v-deep .ant-card-head {
    position: sticky;
    top: 0px;
    z-index: 99;
    opacity: 1;
    background: #ffff;
  }
  .bottom-operate {
    ::v-deep .ant-card-body {
      padding: 24px 0px!important;
    }
  }
  .field-type {
    width: 33%;
    ::v-deep .ant-select-selection--single {
      height: 33px!important;
      line-height: 33px!important;
      border-radius: unset!important;
    }
    ::v-deep .ant-select-selection__rendered {
      line-height: 30px!important;
    }
  }
  .database-selected {
    margin-bottom: 10px;
    ::v-deep .ant-select-selection--single {
      height: 30px!important;
      line-height: 30px!important;
      border-radius: unset!important;
    }
    ::v-deep .ant-select-selection__rendered {
      line-height: 30px!important;
    }
  }
</style>

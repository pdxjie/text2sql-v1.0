<template>
  <div class="demo-split">
    <Split v-model="split3" min="230px">
      <div slot="right" class="demo-split-pane no-padding">
        <Split v-model="split4" mode="vertical">
          <div slot="top" class="demo-split-pane">
            <MonacoEditor
              :read-only="false"
              ref="referenceAnswer"
              height="380px"
              :code="resultCode"
              language="sql"
              theme="vs"/>
          </div>
          <div slot="bottom" class="demo-split-pane">
            <a-card style="border: unset"></a-card>
          </div>
        </Split>
      </div>
      <div slot="left" class="demo-left-split-pane">
        <a-directory-tree multiple default-expand-all>
          <a-tree-node key="0-0" title="parent 0">
            <a-tree-node key="0-0-0" title="leaf 0-0">
              <a-tree-node key="0-0-0" title="leaf 0-0" is-leaf />
            </a-tree-node>
            <a-tree-node key="0-0-1" title="leaf 0-1" is-leaf />
          </a-tree-node>
          <a-tree-node key="0-1" title="parent 1">
            <a-tree-node key="0-1-0" title="leaf 1-0" is-leaf />
            <a-tree-node key="0-1-1" title="leaf 1-1" is-leaf />
          </a-tree-node>
        </a-directory-tree>
        <a-popover
          placement="right"
          v-click-outside="disappearSqlTypePopover"
          :visible="sqlTypeVisible"
          trigger="click"
          :overlayStyle="{ width: '120px' }"
          :getPopupContainer=" triggerNode => {
            return triggerNode.parentNode
          }">
          <template slot="content">
            <div
              style="height: 30px;line-height: 30px;gap: 10px;cursor: pointer;padding: 10px"
              class="display-flex align-items active-sql"
              @click="selectSqlType(sql)"
              v-for="sql in sqlTypes"
              :key="sql.type">
              <img :src="sql.icon" alt="mysql" style="height: 20px;">
              <span>{{ sql.name }}</span>
            </div>
          </template>
          <a-tooltip placement="topLeft">
            <template slot="title">
              <span>新建连接</span>
            </template>
            <a-button
              type="primary"
              shape="circle"
              icon="api"
              ghost
              @click="openSqlTypeSelect"
              style="position: absolute;bottom: 10px;left: 10px;"/>
          </a-tooltip>
        </a-popover>
      </div>
    </Split>
    <!-- 连接弹窗 -->
    <a-modal v-model="connectionVisible" :width="700">
      <template slot="title">
        <div
          v-if="currentType"
          style="height: 30px;line-height: 30px;gap: 10px;"
          class="display-flex align-items">
          <img :src="currentType.icon" alt="mysql" style="height: 20px;">
          <span>{{ currentType.name }}</span>
        </div>
      </template>
      <template slot="footer">
        <div class="operate-btn">
          <a-button key="back" @click="connectMock" :loading="connectMocking">测试连接</a-button>
          <div>
            <a-button key="back" @click="cancelConnect">
              取消
            </a-button>
            <a-button key="submit" type="primary" :loading="connecting" @click="confirmConnect">
              保存
            </a-button>
          </div>
        </div>
      </template>
      <div>
        <a-form :form="connectForm" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-form-item label="连接名">
            <a-input
              placeholder="【例】本地数据库"
              v-decorator="['connNick', { initialValue: '@localhost', rules: [{ required: true, message: '请输入连接名!' }] }]"
            />
          </a-form-item>
          <a-form-item label="主机">
            <a-input
              placeholder="【例】102.183.2.12"
              v-decorator="['connHost', { rules: [{ required: true, message: '主机地址不能为空!' }] }]"
            />
          </a-form-item>
          <a-form-item label="端口号">
            <a-input
              placeholder="【例】3306"
              v-decorator="['connPort', { initialValue: '3306', rules: [{ required: true, message: '端口号不能为空!' }] }]"
            />
          </a-form-item>
          <a-form-item label="用户名">
            <a-input
              placeholder="【例】root"
              v-decorator="['connUser', { initialValue: 'root', rules: [{ required: true, message: '用户名不能为空!' }] }]"
            />
          </a-form-item>
          <a-form-item label="密码">
            <a-input-password
              placeholder="【例】123456"
              v-decorator="['connPass', { rules: [{ required: true, message: '连接密码不能为空!' }] }]"
            />
            <a-checkbox v-model="savePass" @change="changeSavePass">保存密码</a-checkbox>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script>
import ClickOutside from 'vue-click-outside'
import { dbConnectionApi } from '@/api/connect'
import MonacoEditor from '@/components/MonacoEditor/index.vue'
export default {
  name: 'NewTheme',
  components: { MonacoEditor },
  directives: {
    ClickOutside
  },
  data () {
    return {
      split3: 0.15, // 左侧宽度
      split4: 0.5, // 右侧宽度
      sqlTypes: [
        {
          name: 'MySQL',
          type: 1,
          icon: require('@/assets/sqltype/mysql.svg')
        },
        {
          name: 'Oracle',
          type: 4,
          icon: require('@/assets/sqltype/oracle.svg')
        },
        {
          name: 'PostgreSQL',
          type: 2,
          icon: require('@/assets/sqltype/PostgreSQL.svg')
        },
        {
          name: 'SqlServer',
          type: 3,
          icon: require('@/assets/sqltype/sqlserver.svg')
        }
      ], // 连接类型
      currentType: undefined, // 当前选择的连接类型
      sqlTypeVisible: false, // 连接类型弹窗是否显示
      connectionVisible: false, // 连接弹窗是否显示
      connecting: false, // 连接中
      connectForm: this.$form.createForm(this), // 连接表单
      savePass: false, // 是否保存密码
      connectMocking: false, // 连接测试中
      resultCode: '' // 查询结果
    }
  },
  methods: {
    openSqlTypeSelect () {
      this.sqlTypeVisible = true
    },
    selectSqlType (sql) {
      this.currentType = sql
      this.sqlTypeVisible = false
      this.connectionVisible = true
    },
    disappearSqlTypePopover () {
      this.sqlTypeVisible = false
    },
    cancelConnect () {
      this.connectionVisible = false
    },
    confirmConnect () {
      this.connectForm.validateFields(async (err, values) => {
        if (!err) {
          const params = {
            ...values,
            savePass: this.savePass,
            connSource: this.currentType.type,
            type: 'CHECK'
          }
          this.connecting = true
          const data = await dbConnectionApi(params)
          if (data.code === 200) {
            this.connecting = false
            this.connectionVisible = false
            this.$message.success('配置成功!')
          }
        }
      })
    },
    connectMock () {
      this.connectForm.validateFields(async (err, values) => {
        if (!err) {
          const params = {
            ...values,
            connSource: this.currentType.type,
            type: 'CHECK'
          }
          this.connectMocking = true
          const data = await dbConnectionApi(params)
          console.log(data, 'res...')
          if (data.code === 200) {
            this.connectMocking = false
            this.$message.success('连接成功')
          }
        }
      })
    },
    changeSavePass (e) {
      this.savePass = e.target.checked
    }
  }
}
</script>

<style lang="less" scoped>
 .demo-split {
    height: calc(100vh - 200px);
    border: 1px solid #dcdee2;
}
.demo-split-pane {
    padding: 10px;
}
.demo-left-split-pane {
    height: calc(100vh - 200px);
    padding: 10px;
    position: relative;
}
.demo-split-pane.no-padding{
    height: calc(100vh - 200px);
    padding: 0;
}
.demo-split {
    height: calc(100vh - 200px);
    border: 1px solid #dcdee2;
}
.demo-split-pane {
    padding: 10px;
}
.demo-split-pane.no-padding {
    height: calc(100vh - 200px);
    padding: 0;
}
::v-deep .ant-popover-content {
    width: 250px!important;
}

.active-sql:hover {
    background-color: #e6f7ff;
    color: #1890ff;
}
.operate-btn {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>

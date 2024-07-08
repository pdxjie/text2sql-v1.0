<template>
  <div class="demo-split">
    <Split v-model="split3" min="230px">
      <div slot="right" class="demo-split-pane no-padding">
        <Split v-model="split4" mode="vertical">
          <div slot="top" class="demo-split-pane">
            Top Pane
          </div>
          <div slot="bottom" class="demo-split-pane">
            Bottom Pane
          </div>
        </Split>
      </div>
      <div slot="left" class="demo-left-split-pane">
        <a-directory-tree multiple default-expand-all @select="onSelect" @expand="onExpand">
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
          trigger="click"
          :overlayStyle="{ width: '120px' }"
          :getPopupContainer=" triggerNode => {
            return triggerNode.parentNode
          }">
          <template slot="content">
            <div
              style="height: 30px;line-height: 30px;gap: 10px;cursor: pointer;padding: 10px"
              class="display-flex align-items active-sql"
              v-for="sql in sqlTypes"
              :key="sql.type">
              <img :src="sql.icon" alt="mysql" style="height: 20px;">
              <span>{{ sql.name }}</span>
            </div>
          </template>
          <a-tooltip placement="right">
            <template slot="title">
              <span>新建连接</span>
            </template>
            <a-button type="primary" shape="circle" icon="api" ghost style="position: absolute;bottom: 10px;left: 10px;"/>
          </a-tooltip>
        </a-popover>
      </div>
    </Split>
  </div>
</template>

<script>
export default {
  name: 'NewTheme',
  data () {
    return {
      split3: 0.15,
      split4: 0.5,
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
      ],
      currentType: undefined // 当前选择的连接类型
    }
  },
  methods: {
    // 切换类型
    hightlightItem (sql) {
      this.currentType = sql.type
    },
    unHightlightItem () {
      this.currentType = undefined
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
</style>

<template>
  <div>
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
</template>

<script>
import { databaseTreeNodes } from '@/api/database'
import { equalsIgnoreCase } from '@/utils/common'

export default {
  name: 'SqlWorkEdit',
  data () {
    return {
      currentDatabaseId: '', // 当前选择的数据库
      databases: [], // 数据库列表
      schemas: [], // 表结构列表
      selectedSchemaIds: [] // 选择的表结构列表
    }
  },
  props: {
    // 接收父组件传递的数据库 ID
    databaseId: {
      type: String,
      default: ''
    },
    // 接收父组件传递的表结构 ID 列表
    schemaIds: {
      type: Array,
      default: () => []
    }
  },
  created () {
    // 初始化数据
    this.initData()
  },
  methods: {
    // 初始化数据
    async initData () {
      // 获取数据库列表
      const { data } = await databaseTreeNodes()
      // 处理数据
      this.databases = data.treeNodes
      // 设置默认数据库和表结构
      if (this.databaseId) {
        this.currentDatabaseId = this.databaseId
        // 根据数据库 ID 获取表结构列表
        this.selectedSchemaIds = this.schemaIds
      } else {
        // 默认选择第一个数据库
        this.currentDatabaseId = this.databases[0].key
        this.schemas = this.databases[0].children
      }
    },
    // 选择数据库
    handleChange (value) {
      // 选择数据库
      this.currentDatabaseId = value
      // 根据数据库 ID 获取表结构列表
      const database = this.databases.find(item => equalsIgnoreCase(item.key, value))
      // 设置表结构列表
      if (database) {
        this.schemas = database.children
      }
    },
    // 选择表结构
    handleChangeSchemas (value) {
      this.selectedSchemaIds = value
    }
  }
}
</script>

<style scoped lang="less">

</style>

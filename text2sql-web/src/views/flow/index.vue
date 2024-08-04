<template>
  <div id="workflow-container">
  </div>
</template>

<script>
import { Graph } from '@antv/x6'
import '@antv/x6-vue-shape'

export default {
  name: 'Flow',
  data () {
    return {
      graph: null
    }
  },
  mounted () {
    this.initGraph()
  },
  methods: {
    initGraph () {
      this.graph = new Graph({
        container: document.getElementById('workflow-container'),
        width: '100%',
        height: 'calc(100vh - 200px)',
        grid: true
      })
    }
  }
}
</script>

<style scoped lang="less">
#workflow-container {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 10px;
}
.x6-graph {
  height: calc(100vh - 200px);
}
</style>

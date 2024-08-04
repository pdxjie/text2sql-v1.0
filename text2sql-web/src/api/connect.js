/**
 * 数据库连接
 */
import { axios } from '@/utils/request'
import { requestType } from '@/constants/RequestTypes'

/**
 * 数据库连接测试
 */
export const dbConnectionApi = (params) => {
  return axios.request({
    url: '/pdx/conn/check',
    method: requestType.POST,
    data: params
  })
}

/**
 * 获取所有数据源列表
 */
export const customDBs = () => {
  return axios.request({
    url: '/pdx/conn/databases',
    method: requestType.GET
  })
}

/**
 * 连接数据源
 */
export const realConnection = (connId) => {
  return axios.request({
    url: '/pdx/conn/' + connId,
    method: requestType.GET
  })
}

/**
 * 获取所有自定义连接列表
 */
export const customGroups = () => {
  return axios.request({
    url: '/pdx/conn/custom/group',
    method: requestType.GET
  })
}

/**
 * 关闭连接
 */
export const closeConn = () => {
  return axios.request({
    url: '/pdx/conn/close',
    method: requestType.GET
  })
}

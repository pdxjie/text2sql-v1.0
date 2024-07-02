/**
 * 用户相关接口
 */
import { axios } from '@/utils/request'
import { requestType } from '@/constants/RequestTypes'

// 流式接口
export const streamApi = {
  generateSQL: '/database/generateSQLStream' // 生成 SQL
}

export const generateSQLApi = (data) => {
  return axios({
    url: '/database/generateSQLStream',
    method: requestType.POST,
    data
  })
}

export const databaseTreeNodes = () => {
  return axios({
    url: '/database/tree',
    method: requestType.GET
  })
}

export const insertDatabase = (data) => {
  return axios({
    url: '/database/insert',
    method: requestType.POST,
    data
  })
}

export const deleteDatabase = (data) => {
  return axios({
    url: '/database/delete',
    method: requestType.POST,
    data
  })
}

export const allDatabases = () => {
  return axios.request({
    url: '/database/all',
    method: requestType.GET
  })
}

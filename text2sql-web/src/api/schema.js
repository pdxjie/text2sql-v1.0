/**
 * 用户相关接口
 */
import { axios } from '@/utils/request'
import { requestType } from '@/constants/RequestTypes'

export const schemaDetailApi = (vo) => {
  return axios.request({
    url: '/pdx/schema/detail',
    method: requestType.POST,
    data: vo
  })
}

export const saveSchemaInfo = (data) => {
  return axios.request({
    url: '/pdx/schema/insert',
    method: requestType.POST,
    data
  })
}

export const deleteSchemaInfo = (schemaId) => {
  return axios.request({
    url: '/pdx/schema/delete/' + schemaId,
    method: requestType.DELETE
  })
}

export const autoImportSchema = (data) => {
  return axios.request({
    url: '/pdx/schema/auto/import',
    method: requestType.POST,
    data
  })
}

export const moveDatabaseAndSchema = (data) => {
  return axios.request({
    url: '/pdx/schema/move',
    method: requestType.POST,
    data
  })
}

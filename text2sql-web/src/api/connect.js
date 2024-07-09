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

/**
 * 用户相关接口
 */
import { axios } from '@/utils/request'
import { requestType } from '@/constants/RequestTypes'

/**
 * 获取用户信息
 * @param userId
 * @returns {*}
 */
export const getUserInfo = (userId) => {
  return axios({
    url: '/pdx/user/' + userId,
    method: requestType.GET
  })
}

/**
 * 更新用户信息
 * @param vo
 * @returns {*}
 */
export const updateInfo = (vo) => {
  return axios({
    url: '/pdx/user/updateInfo',
    method: requestType.PUT,
    data: vo
  })
}

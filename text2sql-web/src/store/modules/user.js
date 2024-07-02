import Vue from 'vue'
import { login, getInfo, logout, register } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { welcome } from '@/utils/util'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    UPDATE_AI_FONT: (state, aiFont) => {
      state.info.userInfo.aiFont = aiFont
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          const result = response.data
          Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', result.token)
          getInfo().then(response => {
            const result = response.data
            if (result.roles) {
              commit('SET_ROLES', result.roles)
              commit('SET_INFO', result)
            } else {
              reject(new Error('getInfo: roles must be a non-null array !'))
            }
            commit('SET_NAME', { name: result.userInfo.nickName, welcome: welcome() })
            commit('SET_AVATAR', result.userInfo.avatar)

            resolve(response)
          }).catch(error => {
            reject(error)
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 注册
    Register ({ commit }, vo) {
      return new Promise((resolve, reject) => {
        register(vo).then(res => {
          const result = res.data
          Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', result.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const result = response.data
          if (result.roles) {
            commit('SET_ROLES', result.roles)
            commit('SET_INFO', result)
          } else {
            reject(new Error('getInfo: roles must be a non-null array !'))
          }
          commit('SET_NAME', { name: result.userInfo.nickName, welcome: welcome() })
          commit('SET_AVATAR', result.userInfo.avatar)

          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          Vue.ls.remove(ACCESS_TOKEN)
        })
      })
    },
    // 更新用户 AI 字数
    UpdateAiFont ({ commit }, aiFont) {
      commit('UPDATE_AI_FONT', aiFont)
    }
  }
}

export default user

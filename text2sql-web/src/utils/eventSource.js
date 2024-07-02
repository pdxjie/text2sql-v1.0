import { fetchEventSource } from '@microsoft/fetch-event-source'
import store from '@/store'
// const controller = new AbortController()
// const { signal } = controller
const baseUrl = process.env.VUE_APP_API_BASE_URL

export const eventSource = (url, params, _onmessage, method, body) => {
  // 默认请求方式
  method = method || 'GET'
  // body 序列化
  if (body) {
    body = JSON.stringify(body)
  }
  let retry = 0
  const maxRetry = 3
  // 错误类型
  class RetriableError extends Error {}
  class FatalError extends Error {}
  // eslint-disable-next-line promise/param-names
  return new Promise((resove, reject) => {
    // let resultParams = ''
    // 发起请求
    fetchEventSource(baseUrl + url, {
      method: method,
      headers: {
        'Content-Type': 'application/json', // 数据格式
        'token': store.getters.token // token
      },
      body: body, // 数据
      openWhenHidden: true, // 页面隐藏时继续连接
      onopen (response) {
        // 返回数据类型
        const contentType = response && response.headers ? response.headers.get('content-type') : null
        // 是否为 text/event-stream 类型
        const contentTypeError = !contentType || (contentType.toLowerCase() !== 'text/event-stream' && contentType.toLowerCase() !== 'text/event-stream;charset=utf-8')
        if (response.ok && !contentTypeError) {
          // 没有错误
        } else if (response.status >= 400 && response.status < 500 && response.status !== 429) {
          // 服务器错误，不需要重试
          throw new FatalError()
        } else {
          // 其他错误，需要重试
          throw new RetriableError()
        }
      },
      onmessage (message) {
        // 数据处理
        _onmessage(message)
      },
      onerror (error) {
        console.log('error', error)
        // 错误处理
        if (error instanceof RetriableError) {
          retry++
         if (retry > maxRetry) {
             reject(error)
              // 重试
              throw error
            }
        } else {
          reject(error)
          throw error
        }
      }
    })
  })
}

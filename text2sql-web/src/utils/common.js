// 忽略大小写的字符串比较函数
export const equalsIgnoreCase = (str1, str2) => {
  // 如果两个字符串都为空，则返回 false
  if (str1 === null || str2 === null) {
    return false
  }
  // 如果两个字符串长度不同，则返回 false
  return str1.toUpperCase() === str2.toUpperCase()
}

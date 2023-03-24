import request from '@/utils/request'

// 查询小说语句句法依存分析列表
export function listLineDependencyParsing(query) {
  return request({
    url: '/novel/lineDependencyParsing/list',
    method: 'get',
    params: query
  })
}

// 查询小说语句句法依存分析详细
export function getLineDependencyParsing(lineDpId) {
  return request({
    url: '/novel/lineDependencyParsing/' + lineDpId,
    method: 'get'
  })
}

// 新增小说语句句法依存分析
export function addLineDependencyParsing(data) {
  return request({
    url: '/novel/lineDependencyParsing',
    method: 'post',
    data: data
  })
}

// 修改小说语句句法依存分析
export function updateLineDependencyParsing(data) {
  return request({
    url: '/novel/lineDependencyParsing',
    method: 'put',
    data: data
  })
}

// 删除小说语句句法依存分析
export function delLineDependencyParsing(lineDpId) {
  return request({
    url: '/novel/lineDependencyParsing/' + lineDpId,
    method: 'delete'
  })
}

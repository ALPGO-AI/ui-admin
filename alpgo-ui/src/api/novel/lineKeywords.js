import request from '@/utils/request'

// 查询小说语句关键词列表
export function listLineKeywords(query) {
  return request({
    url: '/novel/lineKeywords/list',
    method: 'get',
    params: query
  })
}

// 查询小说语句关键词详细
export function getLineKeywords(lineKwId) {
  return request({
    url: '/novel/lineKeywords/' + lineKwId,
    method: 'get'
  })
}

// 新增小说语句关键词
export function addLineKeywords(data) {
  return request({
    url: '/novel/lineKeywords',
    method: 'post',
    data: data
  })
}

// 修改小说语句关键词
export function updateLineKeywords(data) {
  return request({
    url: '/novel/lineKeywords',
    method: 'put',
    data: data
  })
}

// 删除小说语句关键词
export function delLineKeywords(lineKwId) {
  return request({
    url: '/novel/lineKeywords/' + lineKwId,
    method: 'delete'
  })
}

import request from '@/utils/request'

// 查询小说语句归档列表
export function listLine(query) {
  return request({
    url: '/novel/line/list',
    method: 'get',
    params: query
  })
}

// 查询小说语句归档详细
export function getLine(lineId) {
  return request({
    url: '/novel/line/' + lineId,
    method: 'get'
  })
}

// 新增小说语句归档
export function addLine(data) {
  return request({
    url: '/novel/line',
    method: 'post',
    data: data
  })
}

// 修改小说语句归档
export function updateLine(data) {
  return request({
    url: '/novel/line',
    method: 'put',
    data: data
  })
}

// 删除小说语句归档
export function delLine(lineId) {
  return request({
    url: '/novel/line/' + lineId,
    method: 'delete'
  })
}

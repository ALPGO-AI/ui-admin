import request from '@/utils/request'

// 查询标签列表
export function listTag(query) {
  return request({
    url: '/tag/tag/list',
    method: 'get',
    params: query
  })
}

// 查询标签详细
export function getTag(tagId) {
  return request({
    url: '/tag/tag/' + tagId,
    method: 'get'
  })
}

// 新增标签
export function addTag(data) {
  return request({
    url: '/tag/tag',
    method: 'post',
    data: data
  })
}

// 修改标签
export function updateTag(data) {
  return request({
    url: '/tag/tag',
    method: 'put',
    data: data
  })
}

// 删除标签
export function delTag(tagId) {
  return request({
    url: '/tag/tag/' + tagId,
    method: 'delete'
  })
}

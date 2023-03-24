import request from '@/utils/request'

// 查询小说词性归档列表
export function listWordArchived(query) {
  return request({
    url: '/novel/wordArchived/list',
    method: 'get',
    params: query
  })
}

// 查询小说词性归档详细
export function getWordArchived(wordId) {
  return request({
    url: '/novel/wordArchived/' + wordId,
    method: 'get'
  })
}

// 新增小说词性归档
export function addWordArchived(data) {
  return request({
    url: '/novel/wordArchived',
    method: 'post',
    data: data
  })
}

// 修改小说词性归档
export function updateWordArchived(data) {
  return request({
    url: '/novel/wordArchived',
    method: 'put',
    data: data
  })
}

// 删除小说词性归档
export function delWordArchived(wordId) {
  return request({
    url: '/novel/wordArchived/' + wordId,
    method: 'delete'
  })
}

import request from '@/utils/request'

// 查询小说词频列表
export function listWord(query) {
  return request({
    url: '/novel/word/list',
    method: 'get',
    params: query
  })
}

// 查询小说词频详细
export function getWord(wordId) {
  return request({
    url: '/novel/word/' + wordId,
    method: 'get'
  })
}

// 新增小说词频
export function addWord(data) {
  return request({
    url: '/novel/word',
    method: 'post',
    data: data
  })
}

// 修改小说词频
export function updateWord(data) {
  return request({
    url: '/novel/word',
    method: 'put',
    data: data
  })
}

// 删除小说词频
export function delWord(wordId) {
  return request({
    url: '/novel/word/' + wordId,
    method: 'delete'
  })
}

// 上传文本并解析
export function uploadAnalyze(data) {
  return request({
    url: '/novel/word/analyze',
    method: 'post',
    data: data
  })
}

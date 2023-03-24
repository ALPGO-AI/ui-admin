import request from '@/utils/request'

// 查询知轩藏书列表
export function listBook(query) {
  return request({
    url: '/data/book/list',
    method: 'get',
    params: query
  })
}

// 查询知轩藏书详细
export function getBook(bookId) {
  return request({
    url: '/data/book/' + bookId,
    method: 'get'
  })
}

// 新增知轩藏书
export function addBook(data) {
  return request({
    url: '/data/book',
    method: 'post',
    data: data
  })
}

// 同步知轩藏书
export function syncBook() {
  return request({
    url: '/data/book/sync/',
    method: 'post'
  })
}
export function syncBookById(bookId) {
  return request({
    url: '/data/book/sync/' + bookId,
    method: 'post'
  })
}
export function syncStatus() {
  return request({
    url: '/data/book/sync/status',
    method: 'get'
  })
}
export function syncBookVote() {
  return request({
    url: '/data/book/syncVote/',
    method: 'post'
  })
}
export function syncBookVoteById(bookId) {
  return request({
    url: '/data/book/syncVote/' + bookId,
    method: 'post'
  })
}

// 修改知轩藏书
export function updateBook(data) {
  return request({
    url: '/data/book',
    method: 'put',
    data: data
  })
}

// 删除知轩藏书
export function delBook(bookId) {
  return request({
    url: '/data/book/' + bookId,
    method: 'delete'
  })
}

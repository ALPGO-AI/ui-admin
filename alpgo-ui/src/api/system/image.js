import request from '@/utils/request'

// 查询imageUrlMap
export function mapImage(ids) {
  return request({
    url: '/system/image/map',
    method: 'post',
    data: ids
  })
}

// 查询image列表
export function listImage(query) {
  return request({
    url: '/system/image/list',
    method: 'get',
    params: query
  })
}

// 查询image详细
export function getImage(imageId) {
  return request({
    url: '/system/image/' + imageId,
    method: 'get'
  })
}

// 新增image
export function addImage(data) {
  return request({
    url: '/system/image',
    method: 'post',
    data: data
  })
}

// 修改image
export function updateImage(data) {
  return request({
    url: '/system/image',
    method: 'put',
    data: data
  })
}

// 删除image
export function delImage(imageId) {
  return request({
    url: '/system/image/' + imageId,
    method: 'delete'
  })
}

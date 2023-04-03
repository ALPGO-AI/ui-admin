import request from '@/utils/request'

// 查询environments列表
export function listEnvironment(query) {
  return request({
    url: '/system/environment/list',
    method: 'get',
    params: query
  })
}

export function listWebUIModelOptions(refresh = false) {
  return request({
    url: '/system/environment/webuiModelOptions',
    method: 'post',
    data: {refresh: refresh}
  })
}
// 查询environments详细
export function getEnvironment(environmentId) {
  return request({
    url: '/system/environment/' + environmentId,
    method: 'get'
  })
}

// 新增environments
export function addEnvironment(data) {
  return request({
    url: '/system/environment',
    method: 'post',
    data: data
  })
}

// 修改environments
export function updateEnvironment(data) {
  return request({
    url: '/system/environment',
    method: 'put',
    data: data
  })
}

// 删除environments
export function delEnvironment(environmentId) {
  return request({
    url: '/system/environment/' + environmentId,
    method: 'delete'
  })
}

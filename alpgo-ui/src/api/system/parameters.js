import request from '@/utils/request'

// 查询environment_parameters列表
export function listParameters(query) {
  return request({
    url: '/system/parameters/list',
    method: 'get',
    params: query
  })
}

// 查询environment_parameters详细
export function getParameters(parameterId) {
  return request({
    url: '/system/parameters/' + parameterId,
    method: 'get'
  })
}

// 新增environment_parameters
export function addParameters(data) {
  return request({
    url: '/system/parameters',
    method: 'post',
    data: data
  })
}

// 修改environment_parameters
export function updateParameters(data) {
  return request({
    url: '/system/parameters',
    method: 'put',
    data: data
  })
}

// 删除environment_parameters
export function delParameters(parameterId) {
  return request({
    url: '/system/parameters/' + parameterId,
    method: 'delete'
  })
}

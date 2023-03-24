import request from '@/utils/request'

// 查询stable_diffusion_pattern列表
export function listPattern(query) {
  return request({
    url: '/sdtool/pattern/list',
    method: 'get',
    params: query
  })
}

// 查询stable_diffusion_pattern详细
export function getPattern(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId,
    method: 'get'
  })
}

// 新增stable_diffusion_pattern
export function addPattern(data) {
  return request({
    url: '/sdtool/pattern',
    method: 'post',
    data: data
  })
}

// 修改stable_diffusion_pattern
export function updatePattern(data) {
  return request({
    url: '/sdtool/pattern',
    method: 'put',
    data: data
  })
}

// 删除stable_diffusion_pattern
export function delPattern(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId,
    method: 'delete'
  })
}
export function generateByPattern(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId + `/generate`,
    method: 'post'
  })
}

export function generateSketchBySampleImg(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId + `/generateSketchBySampleImg`,
    method: 'post'
  })
}

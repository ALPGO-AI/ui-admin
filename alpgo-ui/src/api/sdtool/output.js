import request from '@/utils/request'

// 查询stable_diffusion_output列表
export function listOutput(query) {
  return request({
    url: '/sdtool/output/list',
    method: 'get',
    params: query
  })
}

// 查询stable_diffusion_output详细
export function getOutput(outputId) {
  return request({
    url: '/sdtool/output/' + outputId,
    method: 'get'
  })
}

// 新增stable_diffusion_output
export function addOutput(data) {
  return request({
    url: '/sdtool/output',
    method: 'post',
    data: data
  })
}

// 修改stable_diffusion_output
export function updateOutput(data) {
  return request({
    url: '/sdtool/output',
    method: 'put',
    data: data
  })
}

// 删除stable_diffusion_output
export function delOutput(outputId) {
  return request({
    url: '/sdtool/output/' + outputId,
    method: 'delete'
  })
}
export function generateByPattern(outputId) {
  return request({
    url: '/sdtool/output/' + outputId + `/generate`,
    method: 'post'
  })
}

export function generateSketchBySampleImg(outputId) {
  return request({
    url: '/sdtool/output/' + outputId + `/generateSketchBySampleImg`,
    method: 'post'
  })
}

import request from '@/utils/request'

// 查询Stable Diffusion 输出图片列表
export function listOutput(query) {
  return request({
    url: '/sdtool/output/list',
    method: 'get',
    params: query
  })
}

// 查询Stable Diffusion 输出图片详细
export function getOutput(outputId) {
  return request({
    url: '/sdtool/output/' + outputId,
    method: 'get'
  })
}

// 新增Stable Diffusion 输出图片
export function addOutput(data) {
  return request({
    url: '/sdtool/output',
    method: 'post',
    data: data
  })
}

// 修改Stable Diffusion 输出图片
export function updateOutput(data) {
  return request({
    url: '/sdtool/output',
    method: 'put',
    data: data
  })
}

// 删除Stable Diffusion 输出图片
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

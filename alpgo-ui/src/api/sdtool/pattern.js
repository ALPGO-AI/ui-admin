import request from '@/utils/request'

// 查询Stable Diffusion 风格模板列表
export function listPattern(query) {
  return request({
    url: '/sdtool/pattern/list',
    method: 'get',
    params: query
  })
}

// 查询Stable Diffusion 风格模板详细
export function getPattern(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId,
    method: 'get'
  })
}

// 新增Stable Diffusion 风格模板
export function addPattern(data) {
  return request({
    url: '/sdtool/pattern',
    method: 'post',
    data: data
  })
}

// 修改Stable Diffusion 风格模板
export function updatePattern(data) {
  return request({
    url: '/sdtool/pattern',
    method: 'put',
    data: data
  })
}

// 删除Stable Diffusion 风格模板
export function delPattern(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId,
    method: 'delete'
  })
}
export async function generateByPattern(patternId, data = []) {
  return request({
    url: '/sdtool/pattern/' + patternId + `/generate`,
    method: 'post',
    data: data
  })
}

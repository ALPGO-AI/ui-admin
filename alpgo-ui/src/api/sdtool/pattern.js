import request from '@/utils/request'
import sdapiRequest from '@/utils/sdapiRequest'
import cache from '@/plugins/cache'

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
const getToken = async () => {
  const headerParams = cache.local.getJSON("headerParams")
  const username = headerParams.stable_diffusion_webui_username
  const password = headerParams.stable_diffusion_webui_password
  const url = '/login'
  let data = new FormData();
  data.append('username', username)
  data.append('password', password)
  await sdapiRequest({
    url,
    method: 'post',
    data: data
  })
  const tokenResult = await sdapiRequest({
    url: '/token',
    method: 'get'
  })
  const token = tokenResult.token
  return {
    // Cookie: `access-token=${token}`
  }
}
const generateData = (params) => {
  return {
    "enable_hr": false,
    "denoising_strength": 0,
    "firstphase_width": 0,
    "firstphase_height": 0,
    "hr_scale": 2,
    "hr_upscaler": "string",
    "hr_second_pass_steps": 0,
    "hr_resize_x": 0,
    "hr_resize_y": 0,
    "prompt": params.positivePrompt,
    "styles": [
      "string"
    ],
    "seed": -1,
    "subseed": -1,
    "subseed_strength": 0,
    "seed_resize_from_h": -1,
    "seed_resize_from_w": -1,
    "sampler_name": params.parameters && params.parameters.sampler || 'Euler a',
    "batch_size": 1,
    "n_iter": 1,
    "steps": params.parameters && params.parameters.steps || 20,
    "cfg_scale": params.parameters && params.parameters.CFG || 7,
    "width": 512,
    "height": 512,
    "restore_faces": false,
    "tiling": false,
    "negative_prompt": params.negativePrompt,
    "eta": 0,
    "s_churn": 0,
    "s_tmax": 0,
    "s_tmin": 0,
    "s_noise": 1,
    "override_settings": {},
    "override_settings_restore_afterwards": true,
    "script_args": [],
    "sampler_index": params.parameters && params.parameters.sampler || 'Euler a',
    "script_name": null
  }
}
export async function generateByPattern(params) {
  const data = generateData(params)
  const url = '/sdapi/v1/txt2img'
  const headers = await getToken()
  return sdapiRequest({
    url,
    method: 'post',
    data: data,
    headers
  })
}

export function generateSketchBySampleImg(patternId) {
  return request({
    url: '/sdtool/pattern/' + patternId + `/generateSketchBySampleImg`,
    method: 'post'
  })
}

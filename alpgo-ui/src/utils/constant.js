// TODO: use db and api fetch models
export const controlNetModels = [
    {
      label: 'none',
      value: 'None'
    },
    {
      label: 'control_sd15_canny [fef5e48e]',
      value: 'control_sd15_canny [fef5e48e]'
    },
    {
      label: 'control_sd15_openpose [fef5e48e]',
      value: 'control_sd15_openpose [fef5e48e]'
    },
    {
      label: 'control_sd15_scribble [fef5e48e]',
      value: 'control_sd15_scribble [fef5e48e]'
    }
  ]
export const controlNetProcessor = [
    {
      value: 'none',
      label: 'None'
    },
    {
      value: 'canny',
      label: 'Canny 边缘检测（Canny edge detection）'
    },
    {
      value: 'depth',
      label: 'MiDaS 深度信息估算（MiDaS depth estimation）'
    },
    {
      value: 'depth_leres',
      label: 'LeReS 深度信息估算（LeReS depth estimation）'
    },
    {
      value: 'hed',
      label: 'HED 边缘检测（soft HED edge detection, 保留细节）'
    },
    {
      value: 'mlsd',
      label: 'M-LSD 线条检测（M-LSD line detection）'
    },
    {
      value: 'normal_map',
      label: '法线贴图（Normal map）'
    },
    {
      value: 'openpose',
      label: 'OpenPose 姿态检测（OpenPose pose detection）'
    },
    {
      value: 'openpose_hand',
      label: 'OpenPose 姿态及手部检测（Openpose hand）'
    },
    {
      value: 'clip_vision',
      label: 'clip_vision'
    },
    {
      value: 'color',
      label: 'color'
    },
    {
      value: 'pidinet',
      label: 'PiDiNet 边缘检测（像素差分网络，可尝试配合HED模型）'
    },
    {
      value: 'scribble',
      label: '涂鸦（scribble）'
    },
    {
      value: 'fake_scribble',
      label: '伪涂鸦（fake_scribble）'
    },
    {
      value: 'segmentation',
      label: 'ADE20k 语义分割（Semantic segmentation）'
    },
    {
      value: 'binary',
      label: 'binary'
    }
  ]

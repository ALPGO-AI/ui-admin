import { Loading } from 'element-ui'

let loadingInstance;

export default {
  checkIsRunning() {
    return false // TODO: 优化逻辑
  },
  // 打开遮罩层
  start(time, text) {
    loadingInstance = Loading.service({
      lock: true,
      text: `大概需要${time}秒钟，正在${text ? text : '处理中'}，请稍后...`,
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    });
  },
  // 关闭遮罩层
  success() {
    if (loadingInstance) {
      loadingInstance.close();
    }
  },
  // 关闭遮罩层
  failed() {
    if (loadingInstance) {
      loadingInstance.close();
    }
  }
}

import axios from 'axios'
import { getToken } from '@/utils/auth'
import cache from '@/plugins/cache'

const baseURL = process.env.VUE_APP_BASE_API

const state = {
  taskList: []
}

const mutations = {
  SET_TASK_LIST: (state, taskList) => {
    state.taskList = taskList
  },
}

const actions = {
  updateList({ dispatch, commit, state }) {
    return new Promise(resolve => {
      const headerParams = cache.local.getJSON("headerParams") || {};
      var url = baseURL + "/common/envTaskList";
      const self = this;
      axios({
        method: 'get',
        url: url,
        headers: {
          'Authorization': 'Bearer ' + getToken(),
          activewebuienvs: headerParams.activewebuienvs
        }
      }).then(async (res) => {
        const taskMap = res.data;
        const taskList = [];
        Object.keys(taskMap).forEach(key => {
            taskList.push({
                env: key.split(":")[2],
                list: taskMap[key],
                count: taskMap[key].length
            });
        })
        commit('SET_TASK_LIST', taskList)
        resolve(taskList);
      })
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

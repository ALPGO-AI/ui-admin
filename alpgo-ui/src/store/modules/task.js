import axios from 'axios'
import { getToken } from '@/utils/auth'
import cache from '@/plugins/cache'
import { listEnvironment } from "@/api/system/environment";
import { listWebUIModelOptions } from "@/api/system/environment";

const baseURL = process.env.VUE_APP_BASE_API

const state = {
  taskList: [],
  selectedWebUIList: [],
  modelVersionMapOptions: {},
  taskBoardVisible: false,
}

const mutations = {
  SET_TASK_LIST: (state, taskList) => {
    state.taskList = taskList
  },
  SET_ENV_LIST: (state, envList) => {
    const selectedWebUIList = []
    const headerParams = cache.local.getJSON("headerParams") || {};
    const selectIds = headerParams.activewebuienvs;
    envList.forEach(env => {
      if (selectIds.indexOf(env.environmentId) > -1) {
        selectedWebUIList.push(env);
      }
    })
    state.selectedWebUIList = selectedWebUIList;
  },
  SET_WEBUI_MODEL_OPTIONS: (state, modelVersionMapOptions) => {
    state.modelVersionMapOptions = modelVersionMapOptions;
  },
  SET_SHOW_TASK_BOARD: (state, taskBoardVisible) => {
    state.taskBoardVisible = taskBoardVisible;
  },
}

const actions = {
  showTaskBoard({ dispatch, commit, state }) {
    commit('SET_SHOW_TASK_BOARD', true);
  },
  fetchEnvs({ dispatch, commit, state }, refresh) {
    listEnvironment({
      pageNum: 1,
      pageSize: 100,
      name: null,
      type: null,
      description: null,
      accessLevel: null,
    }).then(response => {
      commit('SET_ENV_LIST', response.rows);
      listWebUIModelOptions(refresh).then(response => {
        commit('SET_WEBUI_MODEL_OPTIONS', response.data);
      });
    });
  },
  removeEnvTasks({ dispatch, commit }, envKey) {
    return new Promise(resolve => {
      const headerParams = cache.local.getJSON("headerParams") || {};
      var url = baseURL + `/common/removeTask`;
      const self = this;
      axios({
        method: 'post',
        url: url,
        data: envKey,
        headers: {
          'Authorization': 'Bearer ' + getToken(),
          activewebuienvs: headerParams.activewebuienvs
        }
      }).then(async (res) => {
        resolve(dispatch('task/updateList'));
      })
    })
  },
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
                envId: key.split(":")[0],
                envKey: key,
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

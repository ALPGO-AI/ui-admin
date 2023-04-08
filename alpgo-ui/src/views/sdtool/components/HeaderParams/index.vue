<template>
  <div class="header-params flex" style="flex-wrap: wrap; padding-bottom: 20px;">
    <el-form :model="headerParams" ref="form" style="width: 330px;">
      <el-form-item v-for="(item, index) in form" :key="index" :label="item.label">
        <el-input @input="saveToLocal" v-if="item.type === 'input'" v-model="headerParams[item.key]"></el-input>
        <el-input @input="saveToLocal" v-if="item.type === 'password'" type="password" v-model="headerParams[item.key]"></el-input>
        <el-switch @change="saveToLocal" v-if="item.type === 'switch'" v-model="headerParams[item.key]"
          active-color="#13ce66"
          inactive-color="#ff4949"/>
        <el-select @change="saveToLocal" v-model="headerParams[item.key]" multiple :placeholder="`请选择${item.label}`">
          <el-option
            v-for="item in optionsMap[item.options]"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-collapse v-model="activeName" accordion style="flex: 1;">
      <el-collapse-item title="执行任务面板" name="task">
        <el-row :gutter="20">
          <el-col style="padding-bottom: 20px;" v-for="webui in taskList" :key="webui.envId" :title="webuiMap[webui.envId] && webuiMap[webui.envId].label" :xs="24" :sm="12" :md="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>{{webui.env}}</span>
                <el-button style="float: right; padding: 3px 0" @click="removeEnvTasks(webui.envKey)" type="text">清空任务</el-button>
              </div>
              <div v-for="o in webui.list" :key="o.uuid" class="text item">
                {{'任务名称：' + o.taskName }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-collapse-item>

    </el-collapse>
  </div>
</template>

<script>
import { listEnvironment } from "@/api/system/environment";
import { mapGetters } from "vuex";

export default {
  name: "HeaderParams",
  components: {
  },
  data() {
    return {
      activeName: '',
      headerParams: {},
      form: [
        { label: '启用的WebUI环境', key: 'activewebuienvs', type: 'multiselect', options: 'stable_diffusion_webui'},
        { label: '启用的腾讯云COS', key: 'activetencentcos', type: 'multiselect', options: 'tencent_cos'}
      ],
      optionsMap: {},
      webuiMap: {}
    };
  },
  created() {
    this.$store.dispatch('task/fetchEnvs');
  },
  mounted() {
    this.headerParams = this.$cache.local.getJSON("headerParams") || {};
    listEnvironment({
        pageNum: 1,
        pageSize: 100,
        name: null,
        type: null,
        description: null,
        accessLevel: null,
      }).then(response => {
      const environmentList = response.rows;
      const optionsMap = {};
      for (let index = 0; index < environmentList.length; index++) {
        const env = environmentList[index];
        const type = env.type;
        optionsMap[type] = optionsMap[type] || [];
        optionsMap[type].push({
          label: env.name,
          value: env.environmentId
        });
        this.optionsMap = optionsMap;
        const webuiMap = {};
        const list = optionsMap['stable_diffusion_webui'];
        for (let index = 0; index < list.length; index++) {
          const element = list[index];
          webuiMap[element.value] = element;
        }
        this.webuiMap = webuiMap;
      }
    });
  },
  methods: {
    removeEnvTasks(env) {
      this.$store.dispatch("task/removeEnvTasks", env);
    },
    saveToLocal () {
      this.$store.dispatch('task/fetchEnvs');
      this.$cache.local.setJSON("headerParams", this.headerParams);
    }
  },
  computed: {
    ...mapGetters(["taskList"]),
    taskBoardVisible() {
      return this.$store.state.task.taskBoardVisible;
    }
  }
};
</script>

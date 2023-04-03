<template>
  <div class="header-params">
    <el-form label-position="left" :model="headerParams" ref="form" label-width="320px">
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
  </div>
</template>

<script>
import { listEnvironment } from "@/api/system/environment";

export default {
  name: "HeaderParams",
  components: {
  },
  data() {
    return {
      headerParams: {},
      form: [
        { label: '启用的WebUI环境', key: 'activewebuienvs', type: 'multiselect', options: 'stable_diffusion_webui'},
        { label: '启用的腾讯云COS', key: 'activetencentcos', type: 'multiselect', options: 'tencent_cos'}
      ],
      optionsMap: {}
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
      }
    });
  },
  methods: {
    saveToLocal () {
      this.$store.dispatch('task/fetchEnvs');
      this.$cache.local.setJSON("headerParams", this.headerParams);
    }
  },
  computed: {
  }
};
</script>

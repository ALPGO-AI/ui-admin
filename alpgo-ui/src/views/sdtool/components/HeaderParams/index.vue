<template>
  <div class="header-params">
    <el-form label-position="left" :model="headerParams" ref="form" label-width="320px">
      <el-form-item v-for="(item, index) in form" :key="index" :label="item.label">
        <el-input @input="saveToLocal" v-if="item.type === 'input'" v-model="headerParams[item.key]"></el-input>
        <el-input @input="saveToLocal" v-if="item.type === 'password'" type="password" v-model="headerParams[item.key]"></el-input>
        <el-switch @change="saveToLocal" v-if="item.type === 'switch'" v-model="headerParams[item.key]"
          active-color="#13ce66"
          inactive-color="#ff4949"/>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "HeaderParams",
  components: {
  },
  data() {
    return {
      headerParams: {},
      form: [
        { label: 'webui地址', key: 'stablediffusionwebuidomain', type: 'input' },
        { label: 'webui用户名', key: 'stablediffusionwebuiusername', type: 'input' },
        { label: 'webui用户密码', key: 'stablediffusionwebuipassword', type: 'password' },
        { label: 'webui生成按钮对应fn_index', key: 'stablediffusionwebuifnindexforgenerate', type: 'input' },
        { label: 'webui启用controlnet对应fn_index', key: 'stablediffusionwebuifnindexforcontrolnet', type: 'input' },
        { label: 'webui是否已安装lora插件', key: 'stablediffusionwebuiisloraplugininstalled', type: 'switch' },
      ]
    };
  },
  created() {
  },
  mounted() {
    this.headerParams = this.$cache.local.getJSON("headerParams") || {};
  },
  methods: {
    saveToLocal () {
      this.$cache.local.setJSON("headerParams", this.headerParams);
    }
  },
  computed: {
  }
};
</script>

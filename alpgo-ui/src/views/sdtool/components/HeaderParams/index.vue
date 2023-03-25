<template>
  <div class="header-params">
    <el-form label-position="left" :model="headerParams" ref="form" label-width="320px">
      <el-form-item v-for="(item, index) in form" :key="index" :label="item.label">
        <el-input @input="saveToLocal" v-if="item.type === 'input'" v-model="headerParams[item.key]"></el-input>
        <el-input @input="saveToLocal" v-if="item.type === 'password'" type="password" v-model="headerParams[item.key]"></el-input>
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
        { label: 'webui地址', key: 'stable_diffusion_webui_domain', type: 'input' },
        { label: 'webui用户名', key: 'stable_diffusion_webui_username', type: 'input' },
        { label: 'webui密码', key: 'stable_diffusion_webui_password', type: 'password' }
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

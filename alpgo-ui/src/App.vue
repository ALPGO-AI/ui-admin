<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default  {
  name:  'App',
  data() {
    return {
      url: process.env.VUE_APP_WS_API,
      message: "",
      text_content: "",
      ws: null,
    };
  },
  mounted() {
    this.join();
    this.$store.dispatch('task/updateList');
  },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  },
  methods: {
    join() {
      const wsuri = this.url;
      this.ws = new WebSocket(wsuri);
      const self = this;
      this.ws.onopen = function (event) {
        self.text_content = self.text_content + "已经打开连接!" + "\n";
        // self.notify("已经打开连接!");
      };
      this.ws.onmessage = function (event) {
        self.text_content = event.data + "\n";
        self.notify(event.data);
      };
      this.ws.onclose = function (event) {
        self.text_content = self.text_content + "已经关闭连接!" + "\n";
        // self.notify("已经关闭连接!");
      };
    },
    notify (msg) {
      this.$store.dispatch('task/updateList');
      if (msg.startsWith("连接成功:")) {
        const wsId = msg.split(':')[1];
        msg = msg.split(':')[0];
        this.$cache.local.set('wsId', wsId);
      }
      if (msg === 'TASK_LIST_UPDATED') {
        return;
      }
      this.$notify({
        title: '成功',
        message: msg,
        type: 'success'
      })
    },
    exit() {
      if (this.ws) {
        this.ws.close();
        this.ws = null;
      }
    },
    send() {
      if (this.ws) {
        this.ws.send(this.message);
      } else {
        // alert("未连接到服务器");
      }
    },
  }
}
</script>

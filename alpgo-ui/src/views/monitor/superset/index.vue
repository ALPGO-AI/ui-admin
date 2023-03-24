<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "Superset",
  components: { iFrame },
  data() {
    return {
      url: process.env.VUE_APP_SUPERSET_URL
    };
  },
};
</script>

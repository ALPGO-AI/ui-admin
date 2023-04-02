<template>
  <div class="gallery">
    <el-image 
      fit="cover"
      style="width: 256px; height: 256px" 
      v-for="url in images" :key="url" :src="url" lazy></el-image>
  </div>
</template>

<script>
export default {
  name: "Gallery",
  components: {},
  props: {
    images: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      index: null,
    };
  },
  methods: {
    showImage(image) {
      this.selectedImage = image;
    },
    hideImage() {
      this.selectedImage = null;
    },
  },
};
</script>

<style scoped>
.gallery {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
}
</style>

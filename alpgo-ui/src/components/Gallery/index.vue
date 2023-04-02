<template>
  <div class="gallery">
    <image-preview v-for="url in images" :key="url" :src="url" :width="256" :height="256" />
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

<template>
  <div class="gallery">
    <el-row type="flex" :gutter="20" style="flex-wrap: wrap;">
      <el-col style="padding-bottom: 20px;" v-for="(image, index) in images" :key="index" :xs="24" :sm="12" :md="6">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <el-image
            :src="`${image}?imageMogr2/thumbnail/!50p`"
            fit="cover"
            style="width: 100%"
            :preview-src-list="images"
            :initial-index="index"
          >
            <div slot="error" class="image-slot">
              <el-link type="primary" :href="image" target="_blank">图片地址</el-link>
            </div>
          </el-image>
        </el-card>
      </el-col>
    </el-row>
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

<style lang="scss" scoped>
.gallery {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
}

.el-image {
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #ccc;
  ::v-deep .el-image__inner {
    transition: all 0.3s;
    cursor: pointer;
    &:hover {
      transform: scale(1.2);
    }
  }
  ::v-deep .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: #909399;
    font-size: 30px;
  }
}
</style>

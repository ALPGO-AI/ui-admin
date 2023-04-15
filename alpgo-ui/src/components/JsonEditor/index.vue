<template>
    <div>
        <vue-json-editor @input="handleChange"
            v-model="inputVal" :showBtns="false" :mode="'code'" lang="zh" :expandedOnStart="true"
            @json-change="onJsonChange" @json-save="onJsonSave" />
    </div>
</template>
  
<script>
// 导入
import vueJsonEditor from 'vue-json-editor'
// 局部注册组件
export default {
    name: "JsonEditor",
    components: { vueJsonEditor },
    model: {
        //修改v-model映射关系
        value: "content",
        event: "change"
    },
    props: {
        content: {
            type: Object,
            default: () => { }
        }
    },
    mounted() {
    },
    data() {
        return {
            inputVal: undefined
        };
    },
    created() {
        this.inputVal = this.content;
    },
    methods: {
        handleChange() {
            this.$emit("change", this.inputVal);
        },
        // json编辑器内容变化
        onJsonChange(value) {
            this.$emit("change", value);
        },
        // json编辑器内容保存
        onJsonSave(value) {
            this.$emit("change", value);
        },
    },
};
</script>
  
<style scoped></style>
  
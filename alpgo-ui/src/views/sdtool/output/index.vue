<template>
  <div class="app-container">
    <header-params></header-params>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="使用的种子" prop="seed">
        <el-input
          v-model="queryParams.seed"
          placeholder="请输入使用的种子"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="参考输出图片id" prop="referenceOuputId">
        <el-input
          v-model="queryParams.referenceOuputId"
          placeholder="请输入参考输出图片id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="patternId" prop="patternId">
        <el-input
          v-model="queryParams.patternId"
          placeholder="请输入patternId"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
	    <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="outputList"
      row-key="outputId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="id" align="center" prop="outputId" width="55"/>
      <el-table-column label="输出图片缩略图" align="center" prop="outputImageUrl" width="150">
        <template slot-scope="scope">
          <image-preview :src="scope.row.outputImageUrl" :width="128" :height="128"/>
        </template>
      </el-table-column>
      <el-table-column label="输出图片地址" align="center" prop="outputImageUrl" width="150">
        <template slot-scope="scope">
          <el-link type="primary" :href="scope.row.outputImageUrl" target="_blank">图片地址</el-link>
        </template>
      </el-table-column>
      <!-- <el-table-column label="参考原图地址" align="center" prop="referenceImageUrl" /> -->
      <el-table-column label="使用的种子(默认-1表示随机)" align="center" prop="seed" />
      <el-table-column label="类型" align="center" prop="type" width="150"/>
      <!-- <el-table-column label="参考输出图片id" align="center" prop="referenceOuputId" /> -->
      <!-- <el-table-column label="patternId" align="center" prop="patternId" /> -->
      <el-table-column label="模板风格" align="center" prop="patternStyle" width="150"/>
      <el-table-column label="直出参数" align="left" prop="straightParameter" >
        <template slot-scope="scope">
          <json-viewer
            :value="scope.row.straightParameter && formatter(scope.row.straightParameter) || {}"
            :expand-depth="0"
            copyable
            boxed
          ></json-viewer>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-cloud"
            @click="handleGenerate(scope.row)"
            v-hasPermi="['sdtool:output:edit']"
          >使用相同模板再次生成</el-button>
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-cloud"
            @click="handleOpenControlNetSetting(scope.row)"
            v-hasPermi="['sdtool:output:edit']"
          >以此样图参数进行生成</el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sdtool:output:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sdtool:output:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改Stable Diffusion 输出图片对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="输出图片地址" prop="outputImageUrl">
          <el-input v-model="form.outputImageUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="参考原图地址" prop="referenceImageUrl">
          <el-input v-model="form.referenceImageUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="使用的种子" prop="seed">
          <el-input v-model="form.seed" placeholder="请输入使用的种子" />
        </el-form-item>
        <el-form-item label="直出参数" prop="straightParameter">
          <el-input v-model="form.straightParameter" placeholder="请输入直出参数" />
        </el-form-item>
        <el-form-item label="patternId" prop="patternId">
          <el-input v-model="form.patternId" placeholder="请输入patternId" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- Control Net -->
    <el-dialog :title="'图片生成设置'" :visible.sync="openControlNet" width="800px" append-to-body>
      <el-form ref="formControlNet" :model="formControlNet" :rules="rules" label-width="180px">
        <el-form-item label="使用的ControlNet预处理器（模式图或草稿选择无即可）">
          <el-select v-model="formControlNet.module" placeholder="请选择">
            <el-option
              v-for="item in controlNetProcessor"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用的ControlNet模型">
          <el-radio-group v-model="formControlNet.model">
            <el-radio v-for="dict in controlNetModels" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="预处理图片" prop="outputImageUrl">
          <image-upload v-model="formControlNet.inputImage"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleGenerateByImg(formControlNet.row)">确 定</el-button>
        <el-button @click="cancelOpenControlNet">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutput, getOutput, delOutput, addOutput, updateOutput, generateByPattern, generateByImg } from "@/api/sdtool/output";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import HeaderParams from "@/views/sdtool/components/HeaderParams/index.vue";
import { formatImgArrToSrc } from "@/utils";
import { controlNetModels, controlNetProcessor } from "@/utils/constant";

export default {
  name: "Output",
  components: {
    Treeselect,
    HeaderParams
  },
  data() {
    return {
      controlNetModels,
      controlNetProcessor,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // Stable Diffusion 输出图片表格数据
      outputList: [],
      // Stable Diffusion 输出图片树选项
      outputOptions: [],
      // 弹出层标题
      title: "",
      // 总条数
      total: 0,
      // 是否显示弹出层
      open: false,
      openControlNet: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        outputImageUrl: null,
        referenceImageUrl: null,
        seed: null,
        type: null,
        referenceOuputId: null,
        patternId: null,
      },
      // 表单参数
      form: {},
      formControlNet: {},
      // 表单校验
      rules: {
        referenceOuputId: [
          { required: true, message: "参考输出图片id不能为空", trigger: "blur" }
        ],
        patternId: [
          { required: true, message: "patternId不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    formatter (paramString) {
      let result = {};
      try {
        result = JSON.parse(paramString);
      } catch (error) {
        result = {};
      }
      return result;
    },
    handleOpenControlNetSetting (row) {
      this.resetControlNet()
      this.openControlNet = true;
      this.formControlNet.row = row;
    },
    handleGenerateByImg (row) {
      if (!this.$cache.local.checkHadInputHeaderParams()) {
        this.$message({
          type: 'info',
          message: '请先输入webui地址'
        })
        return
      }
      if (this.$progress.checkIsRunning()) {
        this.$message({
          type: 'info',
          message: '请等待上一次操作完成'
        });
        return
      }
      const formControlNet = this.formControlNet
      if (!formControlNet.inputImage) {
        this.$message({
          type: 'info',
          message: '请先选择预处理图片'
        })
        return
      }
      const outputId = row.outputId
      const patternstyle = row.patternStyle
      this.$modal.confirm('是否确认以风格为"' + patternstyle + '"的数据项进行AI出图？').then(() => {
        this.$message({
          type: 'success',
          message: '调用成功，处理中，大概需要60秒，请勿跳转页面'
        });
        this.$progress.start(60)
        return generateByImg(outputId, formControlNet).then(data => {
          this.$progress.success()
          this.$message({
            type: 'info',
            message: '等待图片上传到COS'
          });
          setTimeout(() => {
            this.getList()
            this.cancelOpenControlNet()
          }, 3000)
        });
      }).catch(() => {
        this.$progress.failed()
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    handleGenerate(row) {
      if (!this.$cache.local.checkHadInputHeaderParams()) {
        this.$message({
          type: 'info',
          message: '请先输入webui地址'
        })
        return
      }
      if (this.$progress.checkIsRunning()) {
        this.$message({
          type: 'info',
          message: '请等待上一次操作完成'
        });
        return
      }
      const outputId = row.outputId
      const patternstyle = row.patternStyle
      this.$modal.confirm('是否确认以风格为"' + patternstyle + '"的数据项进行AI出图？').then(() => {
        this.$message({
          type: 'success',
          message: '调用成功，处理中，大概需要30秒，请勿跳转页面'
        });
        this.$progress.start(30)
        return generateByPattern(outputId).then(data => {
          this.$progress.success()
          this.$message({
            type: 'info',
            message: '等待图片上传到COS'
          });
          setTimeout(() => {
            this.getList()
          }, 3000)
        });
      }).catch(() => {
        this.$progress.failed()
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    /** 查询Stable Diffusion 输出图片列表 */
    getList() {
      this.loading = true;
      listOutput(this.queryParams).then(response => {
        const rows = response.rows;
        this.outputList = rows.map(row => {
          return {
            ...row,
            outputImageUrl: formatImgArrToSrc(JSON.parse(row.outputImageUrl)),
            referenceImageUrl: formatImgArrToSrc(JSON.parse(row.referenceImageUrl)),
          }
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelOpenControlNet () {
      this.openControlNet = false;
      this.resetControlNet();
    },
    // 表单重置
    reset() {
      this.form = {
        outputId: null,
        outputImageUrl: null,
        referenceImageUrl: null,
        seed: null,
        type: null,
        straightParameter: null,
        referenceOuputId: null,
        patternId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    // 表单重置
    resetControlNet() {
      this.formControlNet = {
        inputImage: null,
        model: null,
        module: null
      };
      this.resetForm("formControlNet");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != null && row.outputId) {
        this.form.referenceOuputId = row.outputId;
      } else {
        this.form.referenceOuputId = 0;
      }
      this.open = true;
      this.title = "添加Stable Diffusion 输出图片";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      if (row != null) {
        this.form.referenceOuputId = row.outputId;
      }
      getOutput(row.outputId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改Stable Diffusion 输出图片";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.outputId != null) {
            updateOutput(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOutput(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除Stable Diffusion 输出图片编号为"' + row.outputId + '"的数据项？').then(function() {
        return delOutput(row.outputId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  },
  computed: {
  }
};
</script>

<template>
  <div class="app-container">
    <header-params></header-params>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模型" prop="model">
        <el-select v-model="queryParams.model" placeholder="请选择模型" clearable size="small">
          <el-option v-for="dict in dict.type.stable_diffusion_model" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="正向提示" prop="positivePrompt">
        <el-input v-model="queryParams.positivePrompt" placeholder="请输入正向提示" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="负向提示" prop="negativePrompt">
        <el-input v-model="queryParams.negativePrompt" placeholder="请输入负向提示" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="预设模板" prop="presetTemplate">
        <el-input v-model="queryParams.presetTemplate" placeholder="请输入预设模板" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="pattern风格" prop="patternStyle">
        <el-input v-model="queryParams.patternStyle" placeholder="请输入pattern风格" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['sdtool:pattern:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['sdtool:pattern:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['sdtool:pattern:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['sdtool:pattern:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="patternList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="patternId" width="55" />
      <el-table-column label="模型" align="center" prop="model">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.stable_diffusion_model" :value="scope.row.model" />
        </template>
      </el-table-column>
      <el-table-column label="正向提示" align="center" prop="positivePrompt" />
      <el-table-column label="负向提示" align="center" prop="negativePrompt" />
      <el-table-column label="样例图片" align="center" prop="sampleImage" width="150">
        <template slot-scope="scope">
          <image-preview :src="scope.row.sampleImage || ''" :width="128" :height="128" />
        </template>
      </el-table-column>
      <el-table-column label="预设模板" align="center" prop="presetTemplate" />
      <el-table-column label="pattern风格" align="center" prop="patternStyle" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-cloud" @click="handleGenerate(scope.row)"
            v-hasPermi="['sdtool:pattern:edit']">以此模板生成图片</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleDuplicate(scope.row)"
            v-hasPermi="['sdtool:pattern:edit']">复制</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['sdtool:pattern:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['sdtool:pattern:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改Stable Diffusion 风格模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模型">
          <el-radio-group v-model="form.model">
            <el-radio v-for="dict in dict.type.stable_diffusion_model" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="正向提示" prop="positivePrompt">
          <el-input 
            type="textarea"
            :rows="5"
            v-model="form.positivePrompt"
            placeholder="请输入正向提示" />
        </el-form-item>
        <el-form-item
            type="textarea"
            :rows="5"
            label="负向提示"
            prop="negativePrompt">
          <el-input v-model="form.negativePrompt" placeholder="请输入负向提示" />
        </el-form-item>
        <el-form-item label="预设模板" prop="presetTemplate">
          <el-input v-model="form.presetTemplate" placeholder="请输入预设模板" />
        </el-form-item>
        <el-form-item label="pattern风格" prop="patternStyle">
          <el-input v-model="form.patternStyle" placeholder="请输入pattern风格" />
        </el-form-item>
        <el-form-item label="CFG">
          <el-input-number v-model="form.parameters.CFG" :precision="1" :step="0.1" :max="20"></el-input-number>
        </el-form-item>
        <el-form-item label="Sampler">
          <el-radio-group v-model="form.parameters.sampler">
            <el-radio v-for="dict in dict.type.stable_diffusion_sampler" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Steps">
          <el-input-number v-model="form.parameters.steps" :precision="0" :step="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="开启高清修复">
          <el-switch v-model="form.parameters.enable_hr" 
            active-color="#13ce66"
            inactive-color="#ff4949"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPattern, getPattern, delPattern, addPattern, updatePattern, generateByPattern, generateByImg } from "@/api/sdtool/pattern";
import HeaderParams from "@/views/sdtool/components/HeaderParams/index.vue";
import { formatImgArrToSrc } from "@/utils"

const initParams = {
  CFG: 7,
  steps: 20,
  sampler: "Euler a",
  enable_hr: false
};

export default {
  name: "SDPattern",
  dicts: ['stable_diffusion_model', 'stable_diffusion_sampler'],
  components: {
    HeaderParams
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // Stable Diffusion 风格模板表格数据
      patternList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        model: null,
        positivePrompt: null,
        negativePrompt: null,
        sampleImage: null,
        presetTemplate: null,
        patternStyle: null,
      },
      // 表单参数
      form: {
        parameters: initParams
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询Stable Diffusion 风格模板列表 */
    getList() {
      this.loading = true;
      listPattern(this.queryParams).then(response => {
        const rows = response.rows || [];
        this.patternList = rows.map(row => {
          return {
            ...row,
            sampleImage: formatImgArrToSrc(JSON.parse(row.sampleImage))
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
    // 表单重置
    reset() {
      this.form = {
        patternId: null,
        model: "0",
        positivePrompt: null,
        negativePrompt: null,
        sampleImage: null,
        presetTemplate: null,
        patternStyle: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        parameters: initParams
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.patternId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加Stable Diffusion 风格模板";
    },
    handleDuplicate(row) {
      this.reset();
      const patternId = row.patternId || this.ids
      getPattern(patternId).then(response => {
        this.form = {
          ...response.data,
          parameters: JSON.parse(response.data.parametersJson) || {}
        };
        this.form.patternId = null;
        this.open = true;
        this.title = "复制Stable Diffusion 风格模板";
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
      const params = {
          ...row,
          parameters: JSON.parse(row.parametersJson) || {}
      }
      const patternId = row.patternId
      const patternStyle = row.patternStyle
      this.$modal.confirm('是否确认以"' + patternStyle + '"风格的数据项进行AI出图？').then(() => {
        const enable_hr = params.parameters.enable_hr
        const seconds = enable_hr ? 60 : 10
        this.$message({
          type: 'success',
          message: `调用成功，处理中，大概需要${seconds}秒，请勿跳转页面`
        });
        this.$progress.start(seconds)
        return generateByPattern(patternId).then(response => {
          this.$progress.success()
          this.$message({
            type: 'success',
            message: '处理完成，等待图片上传到COS'
          });
          setTimeout(() => {
            const data = response.data
            row.sampleImage = formatImgArrToSrc(data.patternImages)
          }, 3000)
        });
      }).catch((e) => {
        console.log(e)
        this.$progress.failed()
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const patternId = row.patternId || this.ids
      getPattern(patternId).then(response => {
        this.form = {
          ...response.data,
          parameters: JSON.parse(response.data.parametersJson) || {}
        };
        this.open = true;
        this.title = "修改Stable Diffusion 风格模板";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.parametersJson = JSON.stringify(this.form.parameters)
          if (this.form.patternId != null) {
            updatePattern(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPattern(this.form).then(response => {
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
      const patternIds = row.patternId || this.ids;
      this.$modal.confirm('是否确认删除Stable Diffusion 风格模板编号为"' + patternIds + '"的数据项？').then(function () {
        return delPattern(patternIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sdtool/pattern/export', {
        ...this.queryParams
      }, `pattern_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

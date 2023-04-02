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
      <el-table-column label="模型" align="center" prop="model" width="125">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.stable_diffusion_model" :value="scope.row.model" />
        </template>
      </el-table-column>

      <el-table-column label="正向提示" align="center" prop="positivePrompt" >
        <template slot-scope="scope">
          <el-popover
            placement="top-start"
            title="正向提示"
            width="300"
            trigger="hover"
            :content="scope.row.positivePrompt">
            <el-link @click="copyText(scope.row.positivePrompt)" slot="reference" type="info">{{ formatPrompt(scope.row.positivePrompt) }}</el-link>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="负向提示" align="center" prop="negativePrompt" >
        <template slot-scope="scope">
          <el-popover
            placement="top-start"
            title="负向提示"
            width="300"
            trigger="hover"
            :content="scope.row.negativePrompt">
            <el-link @click="copyText(scope.row.negativePrompt)" slot="reference" type="info">{{ formatPrompt(scope.row.negativePrompt) }}</el-link>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="样例图片" align="center" prop="sampleImagePreviewUrl" width="150">
        <template slot-scope="scope">
          <image-preview :src="scope.row.sampleImagePreviewUrl || ''" :width="128" :height="128" />
        </template>
      </el-table-column>
      <el-table-column label="预设模板" align="center" prop="presetTemplate" width="85">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.stable_diffusion_preset_template" :value="scope.row.presetTemplate"/>
        </template>
      </el-table-column>
      <el-table-column label="图生图初始图片" align="center" width="150">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.presetTemplate === 'img2img'" :src="scope.row.parameters.init_images" :width="128" :height="128" />
        </template>
      </el-table-column>
      <el-table-column label="ControlNet预处理" align="center" width="150">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.parameters.controlnet.enable" :src="scope.row.parameters.controlnet.inputImage" :width="128" :height="128" />
        </template>
      </el-table-column>
      <el-table-column label="pattern风格" align="center" prop="patternStyle" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
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
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
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
            :rows="5"
            label="负向提示"
            prop="negativePrompt">
          <el-input type="textarea" v-model="form.negativePrompt" placeholder="请输入负向提示" />
        </el-form-item>
        <el-form-item label="预设模板" prop="presetTemplate">
          <el-radio-group v-model="form.presetTemplate">
            <el-radio v-for="dict in dict.type.stable_diffusion_preset_template" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="form.presetTemplate === 'img2img'">
          <el-form-item label="图生图初始图片">
            <image-upload :limit="1" v-model="form.parameters.init_images"/>
          </el-form-item>
        </el-form-item>
        <el-form-item label="pattern风格" prop="patternStyle">
          <el-input v-model="form.patternStyle" placeholder="请输入pattern风格" />
        </el-form-item>
        <el-form-item label="Seed">
          <el-input v-model="form.parameters.seed" placeholder="请输入种子" />
        </el-form-item>
        <el-form-item label="CFG">
          <el-input-number v-model="form.parameters.CFG" :precision="1" :step="0.1" :max="20"></el-input-number>
        </el-form-item>
        <el-form-item label="Sampler">
          <el-select v-model="form.parameters.sampler" placeholder="请选择">
            <el-option
              v-for="item in dict.type.stable_diffusion_sampler"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Steps">
          <el-input-number v-model="form.parameters.steps" :precision="0" :step="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="开启高清修复">
          <el-switch v-model="form.parameters.enable_hr"
            active-color="#13ce66"
            inactive-color="#ff4949"/>
        </el-form-item>
        <el-form-item label="重绘幅度">
          <el-input-number v-model="form.parameters.denoising_strength" :precision="2" :step="0.01" :max="1"></el-input-number>
        </el-form-item>
        <el-form-item label="width">
          <el-input-number v-model="form.parameters.width" :precision="0" :step="1" :max="2000"></el-input-number>
        </el-form-item>
        <el-form-item label="height">
          <el-input-number v-model="form.parameters.height" :precision="0" :step="1" :max="2000"></el-input-number>
        </el-form-item>
        <el-form-item label="开启ControlNet">
          <el-switch v-model="form.parameters.controlnet.enable"
            active-color="#13ce66"
            inactive-color="#ff4949"/>
        </el-form-item>
        <el-form-item v-show="form.parameters.controlnet.enable">
          <el-form-item label="ControlNet预处理器">
            <el-select v-model="form.parameters.controlnet.module" placeholder="请选择">
              <el-option
                v-for="item in controlNetProcessor"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="ControlNet模型">
            <el-select v-model="form.parameters.controlnet.model" placeholder="请选择">
              <el-option
                v-for="item in controlNetModels"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="反色模式">
            <el-switch v-model="form.parameters.controlnet.invertImage"
              active-color="#13ce66"
              inactive-color="#ff4949"/>
          </el-form-item>
          <el-form-item label="预处理图片">
            <image-upload :limit="1" v-model="form.parameters.controlnet.inputImage"/>
            <el-link @click="setSampleImageToControlNetInputImage(form)" type="primary">将最新样图作为预处理图片</el-link>
          </el-form-item>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <input id="copyNode" type="hidden">
  </div>
</template>

<script>
import { listPattern, getPattern, delPattern, addPattern, updatePattern, generateByPattern } from "@/api/sdtool/pattern";
import HeaderParams from "@/views/sdtool/components/HeaderParams/index.vue";
import { formatImgArrToSrc } from "@/utils"
import { controlNetModels, controlNetProcessor } from "@/utils/constant";
import { mapImage } from "@/api/system/image";
import ClipboardJS from 'clipboard'

const initParams = {
  CFG: 7,
  steps: 20,
  sampler: "Euler a",
  enable_hr: false,
  height: 512,
  width: 512,
  init_images: [],
  denoising_strength: 0.6,
  seed: '-1',
  controlnet: {
    enable: false,
    inputImage: null,
    model: null,
    module: null,
    invertImage: false
  }
};
const formatPrompt = (str) => {
  if (!str) {
    return "";
  }
  // 超过50个字符，截取前50个字符，后面加...
  if (str.length > 50) {
    return str.substring(0, 50) + "...";
  }
  return str;
}
const getParameters = (jsonString) => {
  return {
    ...initParams,
    ...(JSON.parse(jsonString) || {})
  }
}
export default {
  name: "SDPattern",
  dicts: ['stable_diffusion_model', 'stable_diffusion_sampler', 'stable_diffusion_preset_template'],
  components: {
    HeaderParams
  },
  data() {
    return {
      copyValue: "",
      formatPrompt,
      imageMap: {},
      formatImgArrToSrc,
      controlNetModels,
      controlNetProcessor,
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
  mounted() {
    const clipboard = new ClipboardJS('#copyNode', {
      text: trigger => {
        const codeStr = this.generateContent()
        this.$notify({
          title: '成功',
          message: '已复制到剪切板，可粘贴。',
          type: 'success'
        })
        return codeStr
      }
    })
    clipboard.on('error', e => {
      this.$message.error('复制失败')
    })
  },
  methods: {
    generateContent () {
      return this.copyValue
    },
    copyText (text) {
      this.copyValue = text
      document.getElementById('copyNode').click()
    },
    setSampleImageToControlNetInputImage (form) {
      const imageMap = this.imageMap;
      const { sampleImage } = form;
      if (sampleImage) {
        const imageIds = JSON.parse(sampleImage);
        const imageId = imageIds[0];
        this.form.parameters.controlnet ={
          ...form.parameters.controlnet,
          inputImage: imageMap[imageId]
        };
      }
    },
    /** 查询Stable Diffusion 风格模板列表 */
    getList() {
      this.loading = true;
      listPattern(this.queryParams).then(response => {
        const rows = response.rows || [];
        const patternList = [];
        const imageIds = [];
        for (let index = 0; index < rows.length; index++) {
          const row = rows[index];
          patternList.push({
            ...row,
            parameters: getParameters(row.parametersJson)
          })
          if (row.sampleImage) {
            JSON.parse(row.sampleImage).forEach(imageId => {
            if (imageIds.indexOf(imageId) === -1) {
              imageIds.push(imageId);
            }
          })
          }
          this.total = response.total;
        }
        mapImage(imageIds).then(response => {
          const imageMap = response;
          this.imageMap = imageMap;
          patternList.forEach(pattern => {
            pattern.sampleImagePreviewUrl = formatImgArrToSrc(JSON.parse(pattern.sampleImage), imageMap);
          })
          this.patternList = patternList;
          this.loading = false;
        });
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
          parameters: getParameters(row.parametersJson)
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
          message: '请先选择WebUI环境'
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
        const seconds = enable_hr ? 60 : 30
        this.$message({
          type: 'success',
          message: `调用成功，处理中，大概需要${seconds}秒`
        });
        this.$progress.start(seconds)
        return generateByPattern(patternId).then(response => {
          this.$progress.success()
          this.$message({
            type: 'info',
            message: '等待图片上传到COS'
          });
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
          parameters: getParameters(row.parametersJson)
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

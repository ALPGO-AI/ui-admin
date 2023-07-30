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
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-switch v-model="showGraph"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="图表显示"
            inactive-text="列表显示"/>
      </el-col>
    </el-row>

    <neo4j-view :doubleClickNode="doubleClickNode" ref="neo4jView" v-show="showGraph" :nodes="nodes" :relations="relations"></neo4j-view>

    <el-row  v-show="!showGraph" :gutter="20">
      <el-col style="padding-bottom: 20px;" v-for="pattern in patternList" :key="pattern.patternId" :xs="24" :sm="12" :md="6">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <image-preview :src="pattern.sampleImagePreviewUrl || ''" style="width: 100%"/>
          <div style="padding: 14px;">
            <div class="flex space-between" style="padding-bottom: 7px;">
              <span v-show="pattern.patternStyle">{{pattern.patternStyle}}</span>
              <el-tag size="mini">{{pattern.model}}</el-tag>
            </div>
            <el-row style="padding-bottom: 7px;">
              <dict-tag :options="dict.type.stable_diffusion_preset_template" :value="pattern.presetTemplate"/>
            </el-row>
            <el-row style="padding-bottom: 7px;">
              <el-link @click="copyText(pattern.positivePrompt)" type="info"><label>提示词:</label> {{ formatPrompt(pattern.positivePrompt) }}</el-link>
            </el-row>
            <el-row style="padding-bottom: 7px;">
              <el-link @click="copyText(pattern.negativePrompt)" type="info"><label>反向词:</label> {{ formatPrompt(pattern.negativePrompt) }}</el-link>
            </el-row>
            <div class="flex bottom space-between">
              <time class="time">{{ pattern.updateTime }}</time>
              <el-dropdown trigger="click" @command="(command) => handleDropdownMenuClicked(command, pattern)">
                <el-link class="el-dropdown-link">
                  操作按钮<i class="el-icon-arrow-down el-icon--right"></i>
                </el-link>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleGenerate"
                    v-hasPermi="['sdtool:pattern:edit']">以此模板生成图片</el-dropdown-item>
                  <el-dropdown-item command="handleDuplicate"
                    v-hasPermi="['sdtool:pattern:edit']">复制</el-dropdown-item>
                  <el-dropdown-item command="handleUpdate"
                    v-hasPermi="['sdtool:pattern:edit']">修改</el-dropdown-item>
                  <el-dropdown-item command="handleDelete"
                    v-hasPermi="['sdtool:pattern:remove']">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <pagination v-show="total > 0 && !showGraph" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改Stable Diffusion 风格模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="90%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="模型">
          <el-select v-model="form.model" placeholder="请选择">
            <el-option
              v-for="item in dict.type.stable_diffusion_model"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-button @click="fetchModelVersions('model')" icon="el-icon-refresh" size="mini" circle></el-button>
        </el-form-item>
        <el-form-item>
          <el-form-item v-for="env in selectedWebUIList" :key="env.environmentId" :label="`${env.name}模型`">
            <el-select v-model="form.parameters.modelVersionMap[env.environmentId]" placeholder="请选择">
              <el-option
                v-for="item in modelVersionMapOptions[env.environmentId]"
                :key="item"
                :label="item"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
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
        <el-form-item v-show="form.presetTemplate === 'customerRequest'">
          <el-form-item label="自定义请求">
            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddCustomerRequest">添加</el-button>
              </el-col>
            </el-row>
            <el-collapse v-model="activeCustomerRequestIndex" accordion>
              <el-collapse-item v-for="(requestItem, index) in form.parameters.customer_requests" :key="index" :name="index">
                <template slot="title">
                  <el-row type="flex" justify="space-between" :gutter="10" class="mb8" style="width: 100%;">
                    <el-col :span="8">
                      {{ `请求体 ${index + 1}  `}}
                    </el-col>
                    <el-col :span="8">
                      <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteCustomerRequest">删除</el-button>
                    </el-col>
                  </el-row>
                </template>
                <el-row :gutter="10" class="mb8">
                  <el-col :span="6.5">
                    <el-input type="text" v-model="requestItem.requestName" placeholder="请输入请求脚本名称" />
                  </el-col>
                </el-row>
                <json-editor :content="requestItem.requestBody" v-model="requestItem.requestBody"/>
              </el-collapse-item>
            </el-collapse>
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
            <el-button @click="fetchModelVersions('controlNetModel')" icon="el-icon-refresh" size="mini" circle></el-button>
            <el-form-item v-for="env in selectedWebUIList" :key="env.environmentId" :label="`${env.name}模型`">
              <el-select v-model="form.parameters.controlnet.model" placeholder="请选择">
                <el-option
                  v-for="item in controlNetModelVersionMapOptions[env.environmentId]"
                  :key="item"
                  :label="item"
                  :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form-item>
          <el-form-item label="反色模式">
            <el-switch v-model="form.parameters.controlnet.invertImage"
              active-color="#13ce66"
              inactive-color="#ff4949"/>
          </el-form-item>
          <el-form-item label="预处理图片">
            <image-upload :limit="1" v-model="form.parameters.controlnet.inputImage"/>
            <el-link @click="setSampleImageToControlNetInputImage(form)" type="primary">将最新样图作为预处理图片</el-link>

            <el-form-item label="生成艺术字图片：`fontArtImage`">
              <el-switch v-model="form.parameters.enableFontArtImage"
                active-color="#13ce66"
                inactive-color="#ff4949"/>
            </el-form-item>
            <el-form-item v-show="form.parameters.enableFontArtImage" label="艺术字体字号">
              <el-input-number v-model="form.parameters.fontArtSize" :precision="0" :step="1" :max="200"></el-input-number>
            </el-form-item>
            <el-form-item v-show="form.parameters.enableFontArtImage" label="艺术字体内容(英文逗号换行)">
              <el-input v-model="form.parameters.fontArtText"></el-input>
            </el-form-item>
            <el-link @click="setGenerateFontArtToControlNetInputImage(form)" type="primary">生成艺术字作为预处理图片</el-link>
          </el-form-item>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="生成图片" width="90%" :visible.sync="generateFormVisible">
      <el-form v-if="generateForm.parameters" ref="generateForm" :model="generateForm">
        <el-form-item label="出图数量">
          <el-input-number v-model="generateForm.parameters.batch_size" :precision="0" :step="1" :max="8" />
        </el-form-item>
        <el-form-item label="重复次数">
          <el-input-number v-model="generateForm.parameters.n_iter" :precision="0" :step="1" :max="20" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelGenerate()">取 消</el-button>
        <el-button type="primary" @click="confirmGenerate()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="生成图包" width="90%" :visible.sync="cardPackageFormVisible">
      <el-form ref="cardPackageForm" :model="cardPackageForm">
        <el-form-item label="图包名称">
          <el-input v-model="cardPackageForm.name"/>
        </el-form-item>
        <el-form-item label="图包类型">
          <el-input v-model="cardPackageForm.type" />
        </el-form-item>
        <el-form-item label="图包稀有度">
          <el-rate
            v-model="cardPackageForm.rarity"
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']">
          </el-rate>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelPackage()">取 消</el-button>
        <el-button type="primary" @click="confirmPackage()">确 定</el-button>
      </div>
    </el-dialog>
    <input id="copyNode" type="hidden">
  </div>
</template>

<script>
import { listPattern, getPattern, delPattern, addPattern, updatePattern, generateByPattern, getGraph, packageCard, generateFontArtAndReturnCosUrl } from "@/api/sdtool/pattern";
import HeaderParams from "@/views/sdtool/components/HeaderParams/index.vue";
import { formatImgArrToSrc } from "@/utils"
import { controlNetModels, controlNetProcessor } from "@/utils/constant";
import { mapImage } from "@/api/system/image";
import ClipboardJS from 'clipboard';
import { mapState } from "vuex";

const formatNodeMap = (nodes) => {
  const nodeMap = {};
  nodes.forEach((node) => {
    nodeMap[node.id] = node;
  });
  return nodeMap;
};
const formatPatternNodes = (nodes) => {
  return nodes
    .map((node) => {
    const n = node.n;
    const properties = n.properties;
    return {
      id: n.id,
      label: properties.patternStyle?.val,
      properties,
      shape: 'box',
      group: n.labels[0]
    };
  });
};
const formatOutputNodes = (nodes) => {
  return nodes
    .map((node) => {
    const n = node.n;
    const properties = n.properties;
    return {
      id: n.id,
      properties,
      shape: "image",
      group: n.labels[0],
      image: properties.outputImageUrls?.val && JSON.parse(properties.outputImageUrls.val)[0] + "?imageMogr2/thumbnail/!25p",
      imageSrc: properties.outputImageUrls?.val && JSON.parse(properties.outputImageUrls.val)[0],
    };
  });
};
const formatRelations = (relations) => {
  return relations.map((relation) => {
    const r = relation.r;
    if (!r) {
      return {};
    }
    const properties = r.properties;
    return {
      id: r.start + "_" + r.end,
      from: r.start,
      to: r.end,
      // label: r.type + r.start + r.end,
      properties,
    };
  });
};
const initParams = {
  CFG: 7,
  steps: 20,
  sampler: "Euler a",
  enable_hr: false,
  height: 512,
  width: 512,
  init_images: [],
  customer_requests: [],
  denoising_strength: 0.6,
  seed: '-1',
  batch_size: 1,
  n_iter: 1,
  modelVersionMap: {},
  controlnet: {
    enable: false,
    inputImage: null,
    model: null,
    module: null,
    invertImage: false
  },
  enableFontArtImage: false,
  fontArtText: '',
  fontArtSize: 72
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
const getEnds = (edges, nodeMap) => {
  return edges.map(edge => {
    return nodeMap[edge.split("_")[1]].properties.id.val;
  })
}
export default {
  name: "SDPattern",
  dicts: ['stable_diffusion_model', 'stable_diffusion_sampler', 'stable_diffusion_preset_template'],
  components: {
    HeaderParams
  },
  data() {
    return {
      activeCustomerRequestIndex: '',
      patterns: [],
      outputs: [],
      nodeMap: {},
      nodes: [],
      relations: [],
      showGraph: false,
      generateFormVisible: false,
      generateForm: {},
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
      },
      cardPackageFormVisible: false,
      cardPackageForm: {
        outputIds: [],
        name: '',
        type: '',
        rarity: 0,
      }
    };
  },
  created() {
    this.getList();
    this.getGraph();
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
    handleAddCustomerRequest() {
      let obj = {};
      obj.requestName = "";
      obj.requestBody = "";
      this.form.parameters.customer_requests.push(obj);
    },
    /** environment_parameters删除按钮操作 */
    handleDeleteCustomerRequest(index) {
      const removeElemet = (arr, index) => {
        arr.splice(index, 1);
      };
      removeElemet(this.form.parameters.customer_requests, index);
    },
    handleDropdownMenuClicked (method, row) {
      this[method](row);
    },
    cancelPackage () {
      this.cardPackageFormVisible = false;
      this.cardPackageForm = {};
      this.resetForm("cardPackage");
    },
    confirmPackage () {
      const cardPackageForm = this.cardPackageForm;
      if (cardPackageForm.rarity === 0) {
        this.$message.error('请选择稀有度');
        return;
      }
      this.cardPackageFormVisible = false;
      packageCard(cardPackageForm).then(res => {
        this.$message.success('打包成功');
        this.cardPackageForm = {};
        this.resetForm("cardPackage");
        this.getGraph();
      })
    },
    doubleClickNode (params) {
      const edges = params.edges;
      const nodeMap = this.nodeMap;
      if (params.nodes.length === 1) {
        const node = nodeMap[params.nodes[0]];
        if (node.shape === 'image') {
          if(node.imageSrc != null) {
            window.open(node.imageSrc, '_blank');
          }
        }
        if (node.shape === 'box') {
          if (edges.length > 0) {
            this.cardPackageFormVisible = true;
            this.cardPackageForm = {
              outputIds: getEnds(edges, nodeMap),
              name: node.label,
              type: node.properties.model.val,
            }
          } else {
            const patternId = node.properties.patternId.val;
            getPattern([patternId]).then(response => {
              const row = {
                ...response.data,
                parameters: getParameters(response.data.parametersJson)
              };
              this.handleGenerate(row);
            });
          }
        }
      }
    },
    fetchModelVersions (type) {
      this.$store.dispatch('task/fetchEnvs', {refresh: true, type});
    },
    cancelGenerate() {
      this.generateFormVisible = false;
      this.generateForm = {};
      this.resetForm("generateForm");
    },
    async confirmGenerate() {
      const row = this.generateForm;
      const patternId = row.patternId;
      const patternStyle = row.patternStyle;
      const totalCount = row.parameters.n_iter;
      const extraGenerateParams = row.parameters;
      const extraGenerateParamsList = [];

      for (let index = 0; index < totalCount; index++) {
        extraGenerateParamsList.push(extraGenerateParams);
      }

      this.$modal.confirm('是否确认以"' + patternStyle + '"风格的数据项进行AI出图？').then(() => {
        generateByPattern(patternId, extraGenerateParamsList).then((res) => {
          this.$message({
            type: 'success',
            message: `任务添加成功 total: ${totalCount}`
          });
          this.$store.dispatch('task/updateList');
        })
      }).catch((e) => {
        console.log(e)
        this.$progress.failed()
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
      this.cancelGenerate();
    },
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
        this.form.parameters.controlnet = {
          ...form.parameters.controlnet,
          inputImage: imageMap[imageId]
        };
      }
    },
    async setGenerateFontArtToControlNetInputImage (form) {
      const { fontArtSize, fontArtText, width, height } = form.parameters;
      const res = await generateFontArtAndReturnCosUrl({ fontArtSize, fontArtText, width, height })
      setTimeout(() => {
        this.form.parameters.controlnet = {
          ...form.parameters.controlnet,
          inputImage: res.msg
        };
      }, 1000)
    },
    /** 查询Stable Diffusion 风格模板列表 */
    getGraph() {
      getGraph().then(response => {
        const data = response.data;
        const patterns = formatPatternNodes(JSON.parse(data.patterns));
        const outputs = formatOutputNodes(JSON.parse(data.outputs));
        const relations = formatRelations(JSON.parse(data.relations));
        this.patterns = patterns;
        this.outputs = outputs;
        this.nodes = patterns.concat(outputs);
        this.nodeMap = formatNodeMap(this.nodes);
        this.relations = relations;
        this.$nextTick(() => {
          this.$refs['neo4jView'].redraw();
        })
      })
    },
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
      this.getGraph();
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

      if (!row.presetTemplate) {
        this.$message({
          type: 'info',
          message: '请先为该模板选择预设模板类型（如：文生图）'
        })
        return
      }
      const selectedWebUIList = this.selectedWebUIList;
      for (let index = 0; index < selectedWebUIList.length; index++) {
        const env = selectedWebUIList[index];
        const envId = env.environmentId;
        if (!row.parameters.modelVersionMap[envId]) {
          this.$message({
            type: 'info',
            message: `请先为该模板中的${env.name}环境选择模型`
          })
          return
        }
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
      this.resetGenerateForm(params);
    },
    resetGenerateForm(params) {
      this.generateForm = params;
      this.generateFormVisible = true;
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
  },
  computed: {
    ...mapState({
      selectedWebUIList: state => state.task.selectedWebUIList,
      modelVersionMapOptions: state => state.task.modelVersionMapOptions,
      controlNetModelVersionMapOptions: state => state.task.controlNetModelVersionMapOptions
    })
  }
};
</script>

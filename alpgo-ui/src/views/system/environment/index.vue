<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="环境名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入环境名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="环境类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择环境类型" clearable size="small">
          <el-option
            v-for="dict in dict.type.environment_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="环境访问权限" prop="accessLevel">
        <el-input
          v-model="queryParams.accessLevel"
          placeholder="请输入环境访问权限"
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
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:environment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:environment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:environment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:environment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="environmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="环境Id" align="center" prop="environmentId" />
      <el-table-column label="环境名称" align="center" prop="name" />
      <el-table-column label="环境类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.environment_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="环境描述" align="center" prop="description" />
      <el-table-column label="环境访问权限" align="center" prop="accessLevel" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:environment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:environment:remove']"
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

    <!-- 添加或修改environments对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="环境名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入环境名称" />
        </el-form-item>
        <el-form-item label="环境类型" prop="type">
          <el-select @change="changeFormType" v-model="form.type" placeholder="请选择环境类型">
            <el-option
              v-for="dict in dict.type.environment_type"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="环境描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <!-- <el-form-item label="环境访问权限" prop="accessLevel">
          <el-input v-model="form.accessLevel" placeholder="环境访问权限，请输入用户名，使用英文逗号分割" />
        </el-form-item> -->
        <el-divider content-position="center">环境参数信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button :disabled="!form.type" type="primary" icon="el-icon-plus" size="mini" @click="handleAddEnvironmentParameters">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button :disabled="!form.type" type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteEnvironmentParameters">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="environmentParametersList" :row-class-name="rowEnvironmentParametersIndex" @selection-change="handleEnvironmentParametersSelectionChange" ref="environmentParameters">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="参数名" prop="paramName">
            <template slot-scope="scope">
              <el-input v-model="scope.row.paramName" placeholder="请输入参数名" />
            </template>
          </el-table-column>
          <el-table-column label="参数值" prop="paramValue">
            <template slot-scope="scope">
              <el-switch v-if="scope.row.paramType === 'boolean'"
                v-model="scope.row.paramValue"
                active-value="true"
                inactive-value="false"
                active-color="#13ce66"
                inactive-color="#ff4949"/>
              <el-input v-if="scope.row.paramType === 'string'" v-model="scope.row.paramValue" placeholder="请输入参数值" />
              <el-input-number v-if="scope.row.paramType === 'integer'" v-model="scope.row.paramValue" placeholder="请输入参数值" />
              <el-input-number v-if="scope.row.paramType === 'double'" v-model="scope.row.paramValue" placeholder="请输入参数值" />
            </template>
          </el-table-column>
          <el-table-column label="参数类型" prop="paramType">
            <template slot-scope="scope">
              <el-select v-model="scope.row.paramType" placeholder="请选择环境类型">
                <el-option
                  v-for="dict in dict.type.environment_parameters_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="参数描述" prop="description">
            <template slot-scope="scope">
              <el-input v-model="scope.row.description" placeholder="请输入参数描述" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEnvironment, getEnvironment, delEnvironment, addEnvironment, updateEnvironment } from "@/api/system/environment";

export default {
  name: "Environment",
  dicts: ['environment_type', 'environment_parameters_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedEnvironmentParameters: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // environments表格数据
      environmentList: [],
      // environment_parameters表格数据
      environmentParametersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        description: null,
        accessLevel: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    addEnvParam(paramName, paramType, description) {
      this.environmentParametersList.push({
        paramName: paramName,
        paramType: paramType,
        description: description,
      });
    },
    changeFormType () {
      this.environmentParametersList = [];
      // 添加对应类型的环境参数到列表
      switch (this.form.type) {
        case 'tencent_cos':
          // 腾讯云COS
          this.addEnvParam('cosApiSecretId', 'string', '腾讯云COS密钥ID');
          this.addEnvParam('cosApiSecretKey', 'string', '腾讯云COS密钥KEY');
          this.addEnvParam('cosApiBucketName', 'string', '腾讯云COS存储桶名称');
          this.addEnvParam('cosApiRegion', 'string', '腾讯云COS存储桶所在地域');
          break;
        case 'stable_diffusion_webui':
          // stable diffusion webui
          this.addEnvParam('domain', 'string', 'WebUI 地址');
          this.addEnvParam('username', 'string', 'WebUI 用户名');
          this.addEnvParam('password', 'string', 'WebUI 用户密码');
          this.addEnvParam('txt2imgFnIndex', 'integer', 'WebUI 文生图生成按钮对应fn_index');
          this.addEnvParam('img2imgFnIndex', 'integer', 'WebUI 图生图生成按钮对应fn_index');
          this.addEnvParam('txt2imgControlNetFnIndex', 'integer', 'WebUI 文生图启用controlnet对应fn_index');
          this.addEnvParam('img2imgControlNetFnIndex', 'integer', 'WebUI 图生图启用controlnet对应fn_index');
          this.addEnvParam('isLoraPluginInstalled', 'boolean', 'WebUI 是否已安装lora插件');
          break;
      }
    },
    /** 查询environments列表 */
    getList() {
      this.loading = true;
      listEnvironment(this.queryParams).then(response => {
        this.environmentList = response.rows;
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
        environmentId: null,
        name: null,
        type: null,
        description: null,
        accessLevel: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      };
      this.environmentParametersList = [];
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
      this.ids = selection.map(item => item.environmentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加环境信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const environmentId = row.environmentId || this.ids
      getEnvironment(environmentId).then(response => {
        this.form = response.data;
        this.environmentParametersList = response.data.environmentParametersList;
        this.open = true;
        this.title = "修改环境信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.environmentParametersList = this.environmentParametersList;
          if (this.form.environmentId != null) {
            updateEnvironment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEnvironment(this.form).then(response => {
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
      const environmentIds = row.environmentId || this.ids;
      this.$modal.confirm('是否确认删除environments编号为"' + environmentIds + '"的数据项？').then(function() {
        return delEnvironment(environmentIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** environment_parameters序号 */
    rowEnvironmentParametersIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** environment_parameters添加按钮操作 */
    handleAddEnvironmentParameters() {
      let obj = {};
      obj.paramName = "";
      obj.paramValue = "";
      obj.paramType = "";
      obj.description = "";
      obj.accessLevel = "";
      this.environmentParametersList.push(obj);
    },
    /** environment_parameters删除按钮操作 */
    handleDeleteEnvironmentParameters() {
      if (this.checkedEnvironmentParameters.length == 0) {
        this.$modal.msgError("请先选择要删除的environment_parameters数据");
      } else {
        const environmentParametersList = this.environmentParametersList;
        const checkedEnvironmentParameters = this.checkedEnvironmentParameters;
        this.environmentParametersList = environmentParametersList.filter(function(item) {
          return checkedEnvironmentParameters.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleEnvironmentParametersSelectionChange(selection) {
      this.checkedEnvironmentParameters = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/environment/export', {
        ...this.queryParams
      }, `environment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

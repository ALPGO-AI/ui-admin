<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="${comment}" prop="environmentId">
        <el-input
          v-model="queryParams.environmentId"
          placeholder="请输入${comment}"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="paramName">
        <el-input
          v-model="queryParams.paramName"
          placeholder="请输入${comment}"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="paramType">
        <el-select v-model="queryParams.paramType" placeholder="请选择${comment}" clearable size="small">
          <el-option
            v-for="dict in dict.type.environment_param_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="${comment}" prop="accessLevel">
        <el-input
          v-model="queryParams.accessLevel"
          placeholder="请输入${comment}"
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
          v-hasPermi="['system:parameters:add']"
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
          v-hasPermi="['system:parameters:edit']"
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
          v-hasPermi="['system:parameters:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:parameters:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="parametersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="parameterId" />
      <el-table-column label="${comment}" align="center" prop="environmentId" />
      <el-table-column label="${comment}" align="center" prop="paramName" />
      <el-table-column label="${comment}" align="center" prop="paramValue" />
      <el-table-column label="${comment}" align="center" prop="paramType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.environment_param_type" :value="scope.row.paramType"/>
        </template>
      </el-table-column>
      <el-table-column label="${comment}" align="center" prop="description" />
      <el-table-column label="${comment}" align="center" prop="accessLevel" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:parameters:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:parameters:remove']"
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

    <!-- 添加或修改environment_parameters对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="environmentId">
          <el-input v-model="form.environmentId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="paramName">
          <el-input v-model="form.paramName" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="paramValue">
          <el-input v-model="form.paramValue" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="${comment}" prop="paramType">
          <el-select v-model="form.paramType" placeholder="请选择${comment}">
            <el-option
              v-for="dict in dict.type.environment_param_type"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="${comment}" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="${comment}" prop="accessLevel">
          <el-input v-model="form.accessLevel" placeholder="请输入${comment}" />
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
import { listParameters, getParameters, delParameters, addParameters, updateParameters } from "@/api/system/parameters";

export default {
  name: "Parameters",
  dicts: ['environment_param_type'],
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
      // environment_parameters表格数据
      parametersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        environmentId: null,
        paramName: null,
        paramValue: null,
        paramType: null,
        description: null,
        accessLevel: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        environmentId: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询environment_parameters列表 */
    getList() {
      this.loading = true;
      listParameters(this.queryParams).then(response => {
        this.parametersList = response.rows;
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
        parameterId: null,
        environmentId: null,
        paramName: null,
        paramValue: null,
        paramType: null,
        description: null,
        accessLevel: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
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
      this.ids = selection.map(item => item.parameterId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加environment_parameters";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const parameterId = row.parameterId || this.ids
      getParameters(parameterId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改environment_parameters";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.parameterId != null) {
            updateParameters(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addParameters(this.form).then(response => {
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
      const parameterIds = row.parameterId || this.ids;
      this.$modal.confirm('是否确认删除environment_parameters编号为"' + parameterIds + '"的数据项？').then(function() {
        return delParameters(parameterIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/parameters/export', {
        ...this.queryParams
      }, `parameters_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

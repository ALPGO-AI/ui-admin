<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="image uri" prop="uri">
        <el-input
          v-model="queryParams.uri"
          placeholder="请输入image uri"
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
          v-hasPermi="['system:image:add']"
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
          v-hasPermi="['system:image:edit']"
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
          v-hasPermi="['system:image:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:image:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="imageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="image id" align="center" prop="imageId" />
      <el-table-column label="image uri" align="center" prop="uri" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:image:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:image:remove']"
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

    <!-- 添加或修改image对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="image uri" prop="uri">
          <el-input v-model="form.uri" placeholder="请输入image uri" />
        </el-form-item>
        <el-divider content-position="center">image_provider信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddImageProvider">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteImageProvider">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="imageProviderList" :row-class-name="rowImageProviderIndex" @selection-change="handleImageProviderSelectionChange" ref="imageProvider">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="环境id" prop="envId">
            <template slot-scope="scope">
              <el-input v-model="scope.row.envId" placeholder="请输入环境id" />
            </template>
          </el-table-column>
          <el-table-column label="完整地址" prop="url">
            <template slot-scope="scope">
              <el-input v-model="scope.row.url" placeholder="请输入完整地址" />
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
import { listImage, getImage, delImage, addImage, updateImage } from "@/api/system/image";

export default {
  name: "Image",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedImageProvider: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // image表格数据
      imageList: [],
      // image_provider表格数据
      imageProviderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uri: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        uri: [
          { required: true, message: "image uri不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询image列表 */
    getList() {
      this.loading = true;
      listImage(this.queryParams).then(response => {
        this.imageList = response.rows;
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
        imageId: null,
        uri: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.imageProviderList = [];
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
      this.ids = selection.map(item => item.imageId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加image";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const imageId = row.imageId || this.ids
      getImage(imageId).then(response => {
        this.form = response.data;
        this.imageProviderList = response.data.imageProviderList;
        this.open = true;
        this.title = "修改image";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.imageProviderList = this.imageProviderList;
          if (this.form.imageId != null) {
            updateImage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addImage(this.form).then(response => {
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
      const imageIds = row.imageId || this.ids;
      this.$modal.confirm('是否确认删除image编号为"' + imageIds + '"的数据项？').then(function() {
        return delImage(imageIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** image_provider序号 */
    rowImageProviderIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** image_provider添加按钮操作 */
    handleAddImageProvider() {
      let obj = {};
      obj.envId = "";
      obj.url = "";
      this.imageProviderList.push(obj);
    },
    /** image_provider删除按钮操作 */
    handleDeleteImageProvider() {
      if (this.checkedImageProvider.length == 0) {
        this.$modal.msgError("请先选择要删除的image_provider数据");
      } else {
        const imageProviderList = this.imageProviderList;
        const checkedImageProvider = this.checkedImageProvider;
        this.imageProviderList = imageProviderList.filter(function(item) {
          return checkedImageProvider.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleImageProviderSelectionChange(selection) {
      this.checkedImageProvider = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/image/export', {
        ...this.queryParams
      }, `image_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

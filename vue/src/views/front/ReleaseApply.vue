<template>
  <div style="width: 60%; margin: 10px auto">
    <div class="card" style="padding: 20px; margin-bottom: 20px">
      <div style="font-size: 20px; text-align: center; margin-bottom: 30px">代取员申请解封</div>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="90px" style="padding: 20px 50px">
        <el-form-item prop="reason" label="申请理由">
          <el-input type="textarea" :rows="3" placeholder="请输入申请理由" v-model="data.form.reason"></el-input>
        </el-form-item>
        <el-form-item prop="img" label="佐证图片">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" style="padding: 20px 30px" @click="add">提 交</el-button>
        </div>
      </el-form>
    </div>

    <div class="card" style="padding: 20px">
      <div style="font-size: 20px; margin-bottom: 30px">申请记录</div>

      <div style="margin-bottom: 20px">
        <el-table stripe :data="data.tableData">
          <el-table-column prop="img" label="图片">
            <template #default="scope">
              <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="申请理由" show-overflow-tooltip></el-table-column>
          <el-table-column prop="time" label="申请时间"></el-table-column>
          <el-table-column prop="status" label="申请状态">
            <template v-slot="scope">
              <el-tag type="info" v-if="scope.row.status === '待审核'">待审核</el-tag>
              <el-tag type="success" v-if="scope.row.status === '通过'">通过</el-tag>
              <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reply" label="回复" show-overflow-tooltip></el-table-column>
        </el-table>
      </div>
      <div v-if="data.total">
        <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {Delete} from "@element-plus/icons-vue";

const data = reactive({
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    reason: [
      { required: true, message: '请输入申请理由', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.img = res.data
}

const add = () => {
  request.post('/releaseApply/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/releaseApply/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      courierName: data.courierName
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

</script>

<style scoped>

</style>
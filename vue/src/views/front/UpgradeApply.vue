<template>
  <div style="width: 50%; margin: 10px auto">
    <div class="card" style="padding: 20px">
      <div style="font-size: 20px; margin-bottom: 10px; text-align: center">请提交您的等级提升申请信息</div>
      <div style="text-align: center; margin-bottom: 20px">
        <el-tag type="success">{{  data.user.commissionName }}</el-tag>
      </div>

      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="90px" style="padding: 20px">
        <el-form-item prop="commissionId" label="申请等级">
          <el-select style="width: 100%" v-model="data.form.commissionId" placeholder="请选择申请的等级">
            <el-option v-for="item in data.commissionList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="reason" label="申请理由">
          <el-input type="textarea" :rows="3" v-model="data.form.reason" placeholder="请输入申请理由"></el-input>
        </el-form-item>
        <el-form-item prop="img" label="证明材料">
          <el-upload
              ref="uploadRef"
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleFileUpload"
              list-type="picture"
          >
            <el-button type="primary">上传申请图片</el-button>
          </el-upload>
        </el-form-item>
        <div style="text-align: center"><el-button style="padding: 20px 30px" type="primary" @click="add">提交申请</el-button></div>
      </el-form>
    </div>

    <div style="margin: 40px 0">
      <div style="margin-bottom: 40px">
        <div style="display: flex; align-items: center; margin-bottom: 20px">
          <div style="width: 5px; height: 24px; background-color: red; border-radius: 2px; margin-right: 15px"></div>
          <div style="font-size: 24px; font-weight: bold">评判标准</div>
        </div>
        <div style="font-size: 16px">系统会根据升级申请的证明材料和公司HR的评价进行审批，一定会保证评定的公平公正。</div>
      </div>

      <div style="margin-bottom: 40px">
        <div style="display: flex; align-items: center; margin-bottom: 20px">
          <div style="width: 5px; height: 24px; background-color: red; border-radius: 2px; margin-right: 15px"></div>
          <div style="font-size: 24px; font-weight: bold">升级福利</div>
        </div>
        <div style="font-size: 16px">不同的代取员等级的主要区别在于提现时所获取的金额比例，等级越高，比例越大</div>
      </div>

      <div style="margin-bottom: 40px">
        <div style="display: flex; align-items: center; margin-bottom: 20px">
          <div style="width: 5px; height: 24px; background-color: red; border-radius: 2px; margin-right: 15px"></div>
          <div style="font-size: 24px; font-weight: bold">真实承诺</div>
        </div>
        <div style="font-size: 16px">必须保证申请所提供的证明材料都是真实的，一旦发现证明材料是造假，取消升级并且进行封号处理。</div>
      </div>
    </div>

    <div style="margin-top: 50px">
      <div style="margin-bottom: 20px; font-size: 20px">我的历史申请记录</div>
      <el-table stripe :data="data.tableData">
        <el-table-column prop="commissionName" label="申请等级"></el-table-column>
        <el-table-column prop="reason" label="申请原因"></el-table-column>
        <el-table-column prop="img" label="证明照片">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="申请状态">
          <template v-slot="scope">
            <el-tag type="info" v-if="scope.row.status === '待审核'">待审核</el-tag>
            <el-tag type="success" v-if="scope.row.status === '通过'">通过</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="申请时间"></el-table-column>
        <el-table-column prop="replyContent" label="管理员回复"></el-table-column>
      </el-table>
      <div style="margin-top: 15px" v-if="data.total">
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
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  commissionList: [],
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  rules: {
    commissionId: [
      { required: true, message: '请选择申请等级', trigger: 'change' }
    ],
    reason: [
      { required: true, message: '请输入申请理由', trigger: 'blur' }
    ],
  }
})

const formRef = ref()
const uploadRef = ref()

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.img = res.data
}


const load = () => {
  request.get('/upgradeApply/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
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

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (!data.form.img) {
        ElMessage.warning('请上传申请图片')
        return
      }
      request.post('/upgradeApply/add', data.form).then(res => {
        formRef.value.resetFields()
        uploadRef.value.clearFiles()
        if (res.code === '200') {
          ElMessage.success('操作成功')
          data.form = {}
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

request.get('/courierCommission/selectAll').then(res => {
  data.commissionList = res.data
})

// const load = () => {
//   request.get('').then(res => {
//     data.obj = res.data
//   })
// }
// load()
</script>

<style scoped>

</style>
<template>
  <div class="main-content" style="width: 50%">
    <div class="card" style="padding: 20px">
      <div style="font-size: 20px; margin: 20px 0; text-align: center">
        提交代取员认证信息
        <div style="text-align: center; margin-top: 10px">
          <el-tag type="warning" v-if="data.form.status === '待审核'">待审核</el-tag>
          <el-tag type="success"  v-if="data.form.status === '通过'">已认证</el-tag>
          <el-tag type="danger"  v-if="data.form.status === '拒绝'">认证失败</el-tag>
        </div>
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="120px" style="padding: 20px">
        <el-form-item label="真实名称" prop="name">
          <el-input :disabled="data.form.status === '待审核' || data.form.status === '通过'" v-model="data.form.name" placeholder="真实名称"></el-input>
        </el-form-item>
        <el-form-item label="当前住址" prop="address">
          <el-input :disabled="data.form.status === '待审核' || data.form.status === '通过'" type="textarea" :rows="3" v-model="data.form.address" placeholder="当前住址"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input :disabled="data.form.status === '待审核' || data.form.status === '通过'" v-model="data.form.phone" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="cardNo">
          <el-input :disabled="data.form.status === '待审核' || data.form.status === '通过'" v-model="data.form.cardNo" placeholder="身份证号"></el-input>
        </el-form-item>
        <el-form-item label="身份证正面照片">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleCardFrontUpload"
              list-type="picture"
          >
            <el-button :disabled="data.form.status === '待审核' || data.form.status === '通过'" type="primary">上传身份证正面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="身份证背面照片">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleCardBackUpload"
              list-type="picture"
          >
            <el-button :disabled="data.form.status === '待审核' || data.form.status === '通过'" type="primary">上传身份证背面</el-button>
          </el-upload>
        </el-form-item>
        <div style="text-align: center">
          <el-button :disabled="data.form.status === '待审核' || data.form.status === '通过'" type="primary" style="padding: 20px 30px" @click="addIdentification">立即提交</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  form: {},
  rules: {
    name: [
      {  required: true, message: '请输入真实姓名', trigger: 'blur' }
    ],
    address: [
      {  required: true, message: '请输入真实的住址', trigger: 'blur' }
    ],
    phone: [
      {  required: true, message: '请输入手机号', trigger: 'blur' }
    ],
    cardNo: [
      {  required: true, message: '请输入身份证号码', trigger: 'blur' }
    ],
  }
})

const formRef = ref()

const baseUrl = import.meta.env.VITE_BASE_URL
const handleCardFrontUpload = (res) => {
  data.form.cardFront = res.data
}
const handleCardBackUpload = (res) => {
  data.form.cardBack = res.data
}

const addIdentification = () => {
  if (!data.form.cardFront || !data.form.cardBack) {
    ElMessage.error('请上传身份证图片')
    return
  }
  if (data.form.id) {  // 提交过的form再回查是有id的
    request.put('/identification/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('提交成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post('/identification/add', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('提交成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

}

const load = () => {
  request.get('/identification/selectAll', {
    params: {
      studentId: data.user.id
    }
  }).then(res => {
    data.form = res.data?.length > 0 ? res.data[0] : {}  // res.data 返回的是数组
  })
}
load()
</script>
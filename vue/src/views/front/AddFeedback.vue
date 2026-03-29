<template>
  <div style="width: 50%; margin: 10px auto">
    <div class="card" style="padding: 20px">
      <div style="font-size: 20px; margin-bottom: 10px; text-align: center">请提交您的反馈信息</div>

      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="90px" style="padding: 30px 60px">
        <el-form-item prop="title" label="反馈问题">
          <el-input v-model="data.form.title" placeholder="请输入反馈问题"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="反馈内容">
          <el-input type="textarea" :rows="3" v-model="data.form.content" placeholder="请输入反馈内容"></el-input>
        </el-form-item>
        <div style="text-align: center"><el-button @click="add" type="primary" style="padding: 20px 30px">提 交</el-button></div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  form: {},
  rules: {
    title: [
      { required: true, message: '请输入反馈问题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请输入反馈内容', trigger: 'blur' }
    ]
  }
})
const formRef = ref()

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/feedback/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('提交成功')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })

}
</script>

<style scoped>

</style>
<template>
  <div class="login-container">
    <div style="display: flex; width: 50%; justify-content: center">
      <div class="login-box">
        <div style="font-weight: bold; font-size: 22px; text-align: center; margin-bottom: 30px; color: #3f4585">欢迎使用校园快递代取平台</div>
        <el-form ref="formRef" :model="data.form" :rules="data.rules">
          <el-form-item prop="username">
            <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select size="large" v-model="data.form.role">
              <el-option value="STUDENT" label="学生"></el-option>
              <el-option value="COURIER" label="代取员"></el-option>
              <el-option value="ADMIN" label="管理员"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="large" type="primary" style="background-color: #3f4585; border-color: #3f4585; width: 100%" @click="login">登 录</el-button>
          </el-form-item>
          <div style="text-align: right">
            还没有账号？请 <a style="color: #3f4585" href="/register">注册</a>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: { role: 'STUDENT' },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // 表示表单校验通过
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          // 存储用户信息到浏览器的缓存
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          if (res.data.role === 'ADMIN') {
            router.push('/manager/home')
          } else {
            router.push('/front/home')
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
}
.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: white;
}
</style>
<template>
  <div>
    <div class="front-notice"><el-icon size="16" style="top: 3px"><Bell /></el-icon>公告：{{ data.top }}</div>
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/receiveOrders" v-if="data.user.role === 'COURIER' && data.user.status === '正常'">接单中心</el-menu-item>
          <el-menu-item index="/front/transportOrders" v-if="data.user.role === 'COURIER' && data.user.status === '正常'">派送中订单</el-menu-item>
          <el-menu-item index="/front/studentOrders">代取订单</el-menu-item>
          <el-menu-item index="/front/rank">排行榜</el-menu-item>
          <el-menu-item index="/front/information">校园资讯</el-menu-item>
          <el-menu-item index="/front/addFeedback">问题反馈</el-menu-item>
          <el-menu-item index="/front/person">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 60px">
            <div style="display: flex; align-items: center">
              <img style="width: 40px; height: 40px; border-radius: 50%;" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px;">{{ data.user.name }}</span><span v-if="data.user.role === 'COURIER'">（代取员）</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goPage('/front/courierOrders')" v-if="data.user.role === 'COURIER'">历史接单</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/myFeedback')">我的反馈</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/cashRecord')" v-if="data.user.role === 'COURIER'">提现记录</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/upgradeApply')" v-if="data.user.role === 'COURIER' && data.user.status === '正常'">提升等级</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/releaseApply')" v-if="data.user.role === 'COURIER' && data.user.status === '封禁'">申请解封</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/myIdentification')">代取员认证</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView style="min-height: 500px" @updateUser="updateUser" />
      <Footer />
    </div>
  </div>
</template>

<script setup>
  import router from "@/router/index.js";
  import { reactive } from "vue";
  import request from "@/utils/request.js";
  import Footer from "@/conponents/Footer.vue";

  const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
  })

  const goPage = (path) => {
    location.href = path
  }

  const logout = () => {
    localStorage.removeItem('xm-user')
    router.push('/login')
  }

  const updateUser = () => {
    data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
  }

  const loadNotice = () => {
    request.get('/notice/selectAll').then(res => {
      data.noticeData = res.data
      let i = 0
      if (data.noticeData && data.noticeData.length) {
        data.top = data.noticeData[0].content
        setInterval(() => {
          data.top = data.noticeData[i].content
          i++
          if (i === data.noticeData.length) {
            i = 0
          }
        }, 2500)
      }
    })
  }
  loadNotice()
</script>

<style scoped>
@import "@/assets/css/front.css";

.el-menu--horizontal .el-menu-item {
  color: #333 !important;
  background-color: white !important;
}
.el-menu-item:not(.is-active):hover {
  background-color: white !important;
  color: red !important;
  border-bottom: 2px solid red;
}
.el-menu--horizontal .is-active {
  border: none;
  background-color: red !important;
  color: white !important;
}
</style>
<template>
  <div style="width: 40%; margin: 5px auto; padding: 20px" class="card">
    <div style="text-align: right">
      <el-button type="primary" @click="addCashRecord" v-if="data.user.role === 'COURIER' && data.user.status === '正常'">代取员提现</el-button>
      <el-button type="warning" @click="addAmount">充值余额</el-button>
    </div>
    <el-form ref="user" :model="data.user" label-width="90px" style="padding: 30px">
      <div style="text-align: center; margin-bottom: 20px">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item prop="username" label="用户名">
        <el-input disabled v-model="data.user.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="amount" label="余额">
        {{ data.user.amount }}(元)
      </el-form-item>
      <el-form-item prop="cash" label="代取员钱包" v-if="data.user.role === 'COURIER'">
        {{ data.user.cash }}(元)
      </el-form-item>
      <el-form-item prop="cash" label="代取员评分" v-if="data.user.role === 'COURIER'">
        <el-rate v-if="data.user.rate" v-model="data.user.rate" allow-half  disabled show-score score-template="{value} 星" text-color="#ff9900" />
        <span v-else style="color: #888">暂无评分</span>
      </el-form-item>
      <el-form-item prop="status" label="代取员状态" v-if="data.user.role === 'COURIER'">
        <el-tag type="success" v-if="data.user.status === '正常'">正常</el-tag>
        <el-tag type="danger" v-if="data.user.status === '封禁'">封禁</el-tag>
        <el-button v-if="data.user.status === '封禁'" style="margin-left: 5px" plain type="primary" size="small" @click="router.push('/front/releaseApply')">申请解封</el-button>
      </el-form-item>
      <div style="text-align: center">
        <el-button style="padding: 20px 30px" type="primary" @click="update">保 存</el-button>
      </div>
    </el-form>


    <el-dialog title="充值余额" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="form" :model="data.user" label-width="70px" style="padding: 20px">
        <el-form-item prop="amount" label="充值金额">
          <el-input-number style="width: 200px" :min="1" :step="10" v-model="data.user.amount1" placeholder="请输入金额"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="update">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="提现申请信息" v-model="data.formVisible1" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="90px" style="padding: 20px">
        <el-form-item prop="img" label="支付收款码">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              :on-success="handleImgUpload"
              list-type="picture"
          >
            <el-button type="primary">上传收款码图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="申请人">
          {{ data.user.name }}
        </el-form-item>
        <el-form-item label="提现金额">
          <span style="color: red">{{ data.user.cash }} 元</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="saveCashRecord">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  formVisible1: false,
  form: {}
})

const addAmount = () => {
  data.user.amount1 = 100
  data.formVisible = true
}

const saveCashRecord = () => {
  if (data.user.cash === 0) {
    ElMessage.error('您当前钱包的金额不足，无法申请提现')
    return
  }
  if (!data.form.img) {
    ElMessage.error('请上传收款码图片')
    return
  }
  request.post('/cashRecord/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('提交申请成功')
      data.formVisible1 = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const addCashRecord = () => {
  data.form = {}
  data.formVisible1 = true
}

const handleFileUpload = (res) => {
  data.user.avatar = res.data
}

const handleImgUpload = (res) => {
  data.form.img = res.data
}

// 获取最新的账户余额
request.get('/student/selectById/' + data.user.id).then(res => {
  data.user.amount = res.data.amount
  data.user.cash = res.data.cash
  data.user.rate = res.data.rate
})

const emit = defineEmits(['updateUser'])
const update = () => {
  if (data.formVisible) {   // 充值弹窗打开的时候 再设置充值
    data.user.amount = data.user.amount + data.user.amount1
  }
  request.put('/student/update', data.user).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      localStorage.setItem('xm-user', JSON.stringify(data.user))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
<template>
  <div class="card" style="width: 50%; margin: 10px auto">
    <div style="font-size: 20px; margin: 20px 0; text-align: center; position: relative">
      发布代取订单
      <el-button plain type="primary" style="position: absolute; right: 20px" @click="viewAddress">地址簿</el-button>
    </div>
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="120px" style="padding: 20px">
      <el-form-item label="代取物品类型" prop="expressType">
        <el-select v-model="data.form.expressType" style="width: 100%" placeholder="请选择类型" @change="changePrice">
          <el-option :label="item.name + '：' + item.price + '元'" :value="item.id" v-for="item in data.typeList" :key="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="代取订单简介" prop="descr">
        <el-input type="textarea" :rows="3" v-model="data.form.descr" placeholder="代取订单简介"></el-input>
      </el-form-item>
      <el-form-item label="取件地址" prop="pickupAddress">
        <el-input v-model="data.form.pickupAddress" placeholder="取件地址"></el-input>
      </el-form-item>
      <el-form-item label="收件人名称" prop="customerName">
        <el-input v-model="data.form.customerName" placeholder="收件人名称"></el-input>
      </el-form-item>
      <el-form-item label="收件地址" prop="shippingAddress">
        <el-input v-model="data.form.shippingAddress" placeholder="收件地址"></el-input>
      </el-form-item>
      <el-form-item label="收件人手机号" prop="phone">
        <el-input v-model="data.form.phone" placeholder="收件人手机号"></el-input>
      </el-form-item>
      <el-form-item label="取件码截图" prop="pickupCode">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :headers="{ 'token': data.user.token }"
            :on-success="handleFileUpload"
            list-type="picture"
        >
          <el-button type="primary">上传取件码截图</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="小费" prop="tip">
        <el-input-number :min="0" v-model="data.form.tip" placeholder="小费" @change="changePrice"></el-input-number>
      </el-form-item>
      <el-form-item label="共需支付">
        <span style="font-size: 16px; color: red">￥ {{ data.form.price }}</span>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" style="padding: 20px 30px" @click="addOrder">发 布</el-button>
      </div>
    </el-form>

    <el-dialog title="地址簿（双击行选择地址）" v-model="data.viewVisible" width="40%" destroy-on-close>
      <div style="margin: 10px 0">
        <el-button type="primary" plain @click="handleAdd">新增地址</el-button>
      </div>
      <div class="card">
        <el-table stripe :data="data.tableData" @row-dblclick="dbclick" :row-style="{ 'cursor': 'pointer' }">
          <el-table-column prop="name" label="姓名"></el-table-column>
          <el-table-column prop="phone" label="手机号"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
          <el-table-column prop="asDefault" label="是否默认"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template v-slot="scope">
              <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
              <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 10px" v-if="data.total">
          <el-pagination size="small" @current-change="loadAddress" layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
        </div>
      </div>
    </el-dialog>

    <el-dialog title="地址簿信息" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="form" :model="data.addressForm" label-width="70px" style="padding: 20px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.addressForm.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="data.addressForm.phone" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input type="textarea" :rows="3" v-model="data.addressForm.address" placeholder="地址"></el-input>
        </el-form-item>
        <el-form-item label="是否默认" prop="asDefault">
          <el-radio-group v-model="data.addressForm.asDefault">
            <el-radio-button label="是" value="是"></el-radio-button>
            <el-radio-button label="否" value="否"></el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  form: { tip: 0, price: 0, expressType: parseInt(router.currentRoute.value.query.expressType || '0')  },
  typeList: [],
  viewVisible: false,
  formVisible: false,
  addressForm: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  rules: {
    expressType: [
      {  required: true, message: '请选择代取快递类型', trigger: 'change' }
    ],
    pickupAddress: [
      {  required: true, message: '请输入取件地址', trigger: 'blur' }
    ],
    customerName: [
      {  required: true, message: '请输入收件人名称', trigger: 'blur' }
    ],
    shippingAddress: [
      {  required: true, message: '请输入收件人地址', trigger: 'blur' }
    ],
    phone: [
      {  required: true, message: '请输入收件人联系方式', trigger: 'blur' }
    ],
  }
})

const baseUrl = import.meta.env.VITE_BASE_URL
const handleFileUpload = (res) => {
  data.form.pickupCode = res.data
}

const dbclick = (row) =>{
  data.form.customerName = row.name
  data.form.shippingAddress = row.address
  data.form.phone = row.phone
  data.viewVisible = false
}

// 页面加载后  直接选择默认的地址
request.get('/address/selectAll').then(res => {
  if (res.data?.length) {
    let defaultAddress = res.data.find(v => v.asDefault === '是')
    if (defaultAddress) {
      data.form.customerName = defaultAddress.name
      data.form.shippingAddress = defaultAddress.address
      data.form.phone = defaultAddress.phone
    }
  }
})

const loadAddress = () => {
  request.get('/address/selectPage', {
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

const viewAddress = () => {
  data.viewVisible = true
  loadAddress()
}

const handleAdd = () => {
  data.addressForm = { asDefault: '否' }
  data.formVisible = true
}
const handleEdit = (row) => {
  data.addressForm = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/address/add', data.addressForm).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      loadAddress()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/address/update', data.addressForm).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      loadAddress()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  data.addressForm.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/address/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const addOrder = () => {
  request.post('/orders/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('发布成功')
      router.push('/front/studentOrders')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const changePrice = () => {
  let typePrice = data.typeList.find(v => v.id === data.form.expressType).price  // 找到选择的类型  并获取对应的类型的金额
  data.form.price = typePrice + data.form.tip
}

request.get('/expressType/selectAll').then(res => {
  data.typeList = res.data
  changePrice()
})

</script>

<style scoped>

</style>
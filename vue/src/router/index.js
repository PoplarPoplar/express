import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager/home' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'expressType', meta: { name: '代取快递类型' }, component: () => import('@/views/manager/ExpressType.vue'), },
        { path: 'student', meta: { name: '学生信息' }, component: () => import('@/views/manager/Student.vue'), },
        { path: 'courierCommission', meta: { name: '代取员提成' }, component: () => import('@/views/manager/CourierCommission.vue'), },
        { path: 'identification', meta: { name: '代取员认证' }, component: () => import('@/views/manager/Identification.vue'), },
        { path: 'courier', meta: { name: '代取员信息' }, component: () => import('@/views/manager/Courier.vue'), },
        { path: 'orders', meta: { name: '代取订单信息' }, component: () => import('@/views/manager/Orders.vue'), },
        { path: 'address', meta: { name: '学生地址簿' }, component: () => import('@/views/manager/Address.vue'), },
        { path: 'information', meta: { name: '资讯信息' }, component: () => import('@/views/manager/Information.vue'), },
        { path: 'cashRecord', meta: { name: '提现申请记录' }, component: () => import('@/views/manager/CashRecord.vue'), },
        { path: 'upgradeApply', meta: { name: '代取员等级提升申请' }, component: () => import('@/views/manager/UpgradeApply.vue'), },
        { path: 'feedback', meta: { name: '反馈信息' }, component: () => import('@/views/manager/Feedback.vue'), },
        { path: 'releaseApply', meta: { name: '代取员解封申请' }, component: () => import('@/views/manager/ReleaseApply.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  },
        { path: 'myIdentification', component: () => import('@/views/front/MyIdentification.vue'),  },
        { path: 'addOrder', component: () => import('@/views/front/AddOrder.vue'),  },
        { path: 'studentOrders', component: () => import('@/views/front/StudentOrders.vue'),  },
        { path: 'receiveOrders', component: () => import('@/views/front/ReceiveOrders.vue'),  },
        { path: 'transportOrders', component: () => import('@/views/front/TransportOrders.vue'),  },
        { path: 'courierOrders', component: () => import('@/views/front/CourierOrders.vue'),  },
        { path: 'orderDetail', component: () => import('@/views/front/OrderDetail.vue'),  },
        { path: 'information', component: () => import('@/views/front/Information.vue'),  },
        { path: 'informationDetail', component: () => import('@/views/front/InformationDetail.vue'),  },
        { path: 'courierCommission', component: () => import('@/views/front/CourierCommission.vue'),  },
        { path: 'cashRecord', component: () => import('@/views/front/CashRecord.vue'),  },
        { path: 'upgradeApply', component: () => import('@/views/front/UpgradeApply.vue'),  },
        { path: 'rank', component: () => import('@/views/front/Rank.vue'),  },
        { path: 'myFeedback', component: () => import('@/views/front/MyFeedback.vue'),  },
        { path: 'addFeedback', component: () => import('@/views/front/AddFeedback.vue'),  },
        { path: 'password', component: () => import('@/views/front/Password.vue'),  },
        { path: 'releaseApply', component: () => import('@/views/front/ReleaseApply.vue'),  },
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

export default router

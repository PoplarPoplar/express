package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.anno.LogOperation;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.CourierCommission;
import com.example.entity.Orders;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 业务层方法
 */
@Service
public class OrdersService {

    private static final String ORDER_ID_CACHE_KEY = "orders:id:";
    private static final String ORDER_NO_CACHE_KEY = "orders:no:";
    private static final long ORDER_CACHE_TTL_MINUTES = 10L;

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private StudentService studentService;
    @Resource
    private CourierCommissionService courierCommissionService;
    @Resource
    private ObjectMapper objectMapper;
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @LogOperation
    public void add(Orders orders) {
        orders.setOrderNo(System.currentTimeMillis() + "");
        orders.setCreateTime(DateUtil.now());  // 当前的时间 字符串
        Account currentUser = TokenUtils.getCurrentUser();
        orders.setStudentId(currentUser.getId());
        orders.setStatus("待支付");
        orders.setRate(0D);
        ordersMapper.insert(orders);
        clearOrderCacheByOrderNo(orders.getOrderNo());
    }

    @Transactional
    @LogOperation
    public void updateById(Orders orders) {
        Orders oldOrder = orders.getId() == null ? null : ordersMapper.selectById(orders.getId());
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.STUDENT.name().equals(currentUser.getRole())) {
            String status = orders.getStatus();
            if ("待接单".equals(status)) {  // 表示用户要支付   扣除用户余额
                Integer amount = currentUser.getAmount();
                if (amount < orders.getPrice()) {  // 用户的账户余额小于订单的总价   扣除用户余额
                    throw new CustomException("500", "抱歉，账户余额不足，请及时充值");
                } else {
                    currentUser.setAmount(currentUser.getAmount() - orders.getPrice());  // 更新学生的账户余额
                    studentService.updateById((Student) currentUser);
                }
            } else if ("已签收".equals(status)) {  // 给代取员结算金额
                orders.setSignTime(DateUtil.now());
                Student courier = studentService.selectById(orders.getCourierId());  // 代取员

                Integer commissionId = courier.getCommissionId();
                CourierCommission courierCommission = courierCommissionService.selectById(commissionId);
                Double commission = courierCommission.getCommission();
                Integer price = orders.getPrice();
                // 本单代取员可获取的金额
                BigDecimal cash = BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(commission)).setScale(2, BigDecimal.ROUND_HALF_UP);
                courier.setCash(courier.getCash() + cash.doubleValue());
                studentService.updateById(courier);
            }
        } else if (RoleEnum.COURIER.name().equals(currentUser.getRole())) {
            String status = orders.getStatus();
            if ("运输中".equals(status)) {   // 接单按钮触发
                orders.setCourierId(currentUser.getId());
                orders.setAccessTime(DateUtil.now());
            } else if ("已送达".equals(status)) {   // 送达按钮触发
                orders.setDeliverTime(DateUtil.now());
            }
        }
        ordersMapper.updateById(orders);
        if (oldOrder != null) {
            clearOrderCacheById(oldOrder.getId());
            clearOrderCacheByOrderNo(oldOrder.getOrderNo());
        }
        clearOrderCacheById(orders.getId());
        clearOrderCacheByOrderNo(orders.getOrderNo());
    }

    @LogOperation
    public void deleteById(Integer id) {
        Orders oldOrder = ordersMapper.selectById(id);
        ordersMapper.deleteById(id);
        if (oldOrder != null) {
            clearOrderCacheByOrderNo(oldOrder.getOrderNo());
        }
        clearOrderCacheById(id);
    }

    @LogOperation
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            Orders oldOrder = ordersMapper.selectById(id);
            ordersMapper.deleteById(id);
            if (oldOrder != null) {
                clearOrderCacheByOrderNo(oldOrder.getOrderNo());
            }
            clearOrderCacheById(id);
        }
    }

    public Orders selectById(Integer id) {
        String cacheKey = ORDER_ID_CACHE_KEY + id;
        Orders orders = readOrderCache(cacheKey);
        if (orders == null) {
            orders = ordersMapper.selectById(id);
        }
        if (orders == null) {
            return null;
        }
        // 查询有代取员的订单的代取员评分
        if (orders.getCourierId() != null) {
            Integer courierId = orders.getCourierId();
            List<Orders> ordersList = this.selectAll(null);
            List<Double> rateList = ordersList.stream().filter(o -> o.getRate() != null)
                .filter(o -> o.getCourierId() != null && o.getCourierId().equals(courierId))
                    .map(Orders::getRate).filter(r -> r > 0).toList();
            if (!rateList.isEmpty()) {
                Double total = rateList.stream().reduce(Double::sum).orElse(0D);
                BigDecimal courierRate = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(rateList.size()), 2, BigDecimal.ROUND_HALF_UP);
                orders.setCourierRate(courierRate.doubleValue());
            }
        }

        writeOrderCache(cacheKey, orders);
        if (orders.getOrderNo() != null) {
            writeOrderCache(ORDER_NO_CACHE_KEY + orders.getOrderNo(), orders);
        }

        return orders;
    }

    public Orders selectByOrderNo(String orderNo) {
        String cacheKey = ORDER_NO_CACHE_KEY + orderNo;
        Orders orders = readOrderCache(cacheKey);
        if (orders == null) {
            orders = ordersMapper.selectByOrderNo(orderNo);
        }
        if (orders == null) {
            return null;
        }
        // 查询有代取员的订单的代取员评分
        if (orders.getCourierId() != null) {
            Integer courierId = orders.getCourierId();
            List<Orders> ordersList = this.selectAll(null);
            List<Double> rateList = ordersList.stream().filter(o -> o.getRate() != null)
                .filter(o -> o.getCourierId() != null && o.getCourierId().equals(courierId))
                    .map(Orders::getRate).filter(r -> r > 0).toList();
            if (!rateList.isEmpty()) {
                Double total = rateList.stream().filter(r -> r > 0).reduce(Double::sum).orElse(0D);
                BigDecimal courierRate = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(rateList.size()), 2, BigDecimal.ROUND_HALF_UP);
                orders.setCourierRate(courierRate.doubleValue());
            }
        }

        writeOrderCache(cacheKey, orders);
        if (orders.getId() != null) {
            writeOrderCache(ORDER_ID_CACHE_KEY + orders.getId(), orders);
        }
        return orders;
    }

    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    public PageInfo<Orders> selectCourierPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        // 查询当前登录学生自己发布的订单
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.STUDENT.name().equals(currentUser.getRole()) || RoleEnum.COURIER.name().equals(currentUser.getRole())) {
            orders.setStudentId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    private Orders readOrderCache(String key) {
        if (stringRedisTemplate == null) {
            return null;
        }
        try {
            String json = stringRedisTemplate.opsForValue().get(key);
            if (json == null || json.isBlank()) {
                return null;
            }
            return objectMapper.readValue(json, Orders.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void writeOrderCache(String key, Orders orders) {
        if (stringRedisTemplate == null || orders == null) {
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(orders);
            stringRedisTemplate.opsForValue().set(key, json, ORDER_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (JsonProcessingException ignored) {
        } catch (Exception ignored) {
        }
    }

    private void clearOrderCacheById(Integer id) {
        if (id == null || stringRedisTemplate == null) {
            return;
        }
        try {
            stringRedisTemplate.delete(ORDER_ID_CACHE_KEY + id);
        } catch (Exception ignored) {
        }
    }

    private void clearOrderCacheByOrderNo(String orderNo) {
        if (orderNo == null || orderNo.isBlank() || stringRedisTemplate == null) {
            return;
        }
        try {
            stringRedisTemplate.delete(ORDER_NO_CACHE_KEY + orderNo);
        } catch (Exception ignored) {
        }
    }

}

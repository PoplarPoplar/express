package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private StudentService studentService;
    @Resource
    private OrdersService ordersService;
    @Resource
    private CashRecordService cashRecordService;
    @Resource
    private CourierCommissionService courierCommissionService;
    @Resource
    private ExpressTypeService expressTypeService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello () {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if (RoleEnum.STUDENT.name().equals(account.getRole())) {
            loginAccount = studentService.login(account);
        } else if (RoleEnum.COURIER.name().equals(account.getRole())) {
            loginAccount = studentService.courierLogin(account);
        }
        return Result.success(loginAccount);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if (RoleEnum.COURIER.name().equals(account.getRole()) || RoleEnum.STUDENT.name().equals(account.getRole())) {
            studentService.updatePassword(account);
        }
        return Result.success();
    }

    @GetMapping("/count")
    public Result count() {
        List<Orders> ordersList = ordersService.selectAll(null);
        long orderNum = ordersList.stream().filter(o -> !o.getStatus().equals("待支付") && !o.getStatus().equals("已取消")).count();
        Integer orderTotal = ordersList.stream()
                .filter(o -> !o.getStatus().equals("待支付") && !o.getStatus().equals("已取消"))
                .map(Orders::getPrice).reduce(Integer::sum).orElse(0);
        Double cashTotal = cashRecordService.selectAll(null).stream().filter(c -> "通过".equals(c.getStatus()))
                .map(CashRecord::getMoney).reduce(Double::sum).orElse(0D);
        List<Student> studentList = studentService.selectAll(null);
        long studentCount = studentList.stream().filter(s -> s.getRole().equals(RoleEnum.STUDENT.name())).count();
        long courierCount = studentList.stream().filter(s -> s.getRole().equals(RoleEnum.COURIER.name())).count();

        HashMap<String, Object> map = new HashMap<>();
        map.put("orderNum", orderNum);
        map.put("orderTotal", orderTotal);
        map.put("cashTotal", cashTotal);
        map.put("studentCount", studentCount);
        map.put("courierCount", courierCount);
        return Result.success(map);
    }

    @GetMapping("/line")
    public Result line() {
        // 横轴
        Date end = new Date();
        DateTime start = DateUtil.offsetDay(end, -6);
        List<DateTime> dateTimeList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimeList.stream().map(DateUtil::formatDate).toList();  // 年月日格式的日期字符串集合
        List<Orders> ordersList = ordersService.selectAll(null);
        List<Integer> totalList = new ArrayList<>();
        for (String dateStr : dateStrList) {
            Integer sum = ordersList.stream()
                    .filter(o -> !o.getStatus().equals("待支付") && !o.getStatus().equals("已取消"))
                    .filter(o -> o.getCreateTime().contains(dateStr))
                    .map(Orders::getPrice).reduce(Integer::sum).orElse(0);
            totalList.add(sum);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("dateStrList", dateStrList);  // x轴
        map.put("totalList", totalList);  // y轴
        return Result.success(map);
    }

    @GetMapping("/pie")
    public Result pie() {
        List<Student> studentList = studentService.selectAll(null);
        List<Student> courierList = studentList.stream().filter(s -> s.getRole().equals(RoleEnum.COURIER.name())).toList();

        List<CourierCommission> courierCommissionList = courierCommissionService.selectAll(null);
        List<HashMap<String, Object>> resList = new ArrayList<>();
        for (CourierCommission courierCommission : courierCommissionList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", courierCommission.getName());
            map.put("value", courierList.stream().filter(courier -> courier.getCommissionId().equals(courierCommission.getId())).count());
            resList.add(map);
        }
        return Result.success(resList);
    }

    @GetMapping("/bar")
    public Result bar() {
        // 横轴
        List<ExpressType> expressTypeList = expressTypeService.selectAll(null);
        List<String> typeNameList = expressTypeList.stream().map(ExpressType::getName).toList();
        List<Orders> ordersList = ordersService.selectAll(null);
        List<Long> countList = new ArrayList<>();
        for (ExpressType type : expressTypeList) {
            Long count = ordersList.stream()
                    .filter(o -> !o.getStatus().equals("待支付") && !o.getStatus().equals("已取消"))
                    .filter(o -> o.getExpressType().equals(type.getId()))
                    .count();
            countList.add(count);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("typeNameList", typeNameList);  // x轴
        map.put("countList", countList);  // y轴
        return Result.success(map);
    }

}

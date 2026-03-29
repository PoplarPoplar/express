package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.CourierCommission;
import com.example.entity.Orders;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.OrdersMapper;
import com.example.mapper.StudentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    CourierCommissionService courierCommissionService;

    public void add(Student student) {
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());
        if (ObjectUtil.isNotNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }
        student.setRole(RoleEnum.STUDENT.name());
        student.setAmount(0);
        student.setCash(0D);
        studentMapper.insert(student);
    }

    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            studentMapper.deleteById(id);
        }
    }

    public Student selectById(Integer id) {
        Student student = studentMapper.selectById(id);
        // 查询有代取员评分
        List<Orders> ordersList = ordersMapper.selectAll(null);
        List<Double> rateList = ordersList.stream().filter(o -> o.getRate() != null)
                .filter(o -> o.getCourierId() != null && o.getCourierId().equals(id))
                .map(Orders::getRate).filter(r -> r > 0).toList();
        if (!rateList.isEmpty()) {
            Double total = rateList.stream().reduce(Double::sum).orElse(0D);
            BigDecimal courierRate = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(rateList.size()), 1, BigDecimal.ROUND_HALF_UP);
            student.setRate(courierRate.doubleValue());  // 代取员
        }
        return student;
    }

    // 查询代取员的评分榜单
    public List<Student> selectRateRank() {
        List<Student> studentList = this.selectAll(null);
        List<Student> courierList = studentList.stream().filter(s -> s.getRole().equals(RoleEnum.COURIER.name())).toList();
        List<Orders> ordersList = ordersMapper.selectAll(null);
        for (Student courier : courierList) {
            // 查询有代取员评分
            List<Double> rateList = ordersList.stream().filter(o -> o.getRate() != null)
                    .filter(o -> o.getCourierId() != null && o.getCourierId().equals(courier.getId()))
                    .map(Orders::getRate).filter(r -> r > 0).toList();
            if (!rateList.isEmpty()) {
                Double total = rateList.stream().reduce(Double::sum).orElse(0D);
                BigDecimal courierRate = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(rateList.size()), 1, BigDecimal.ROUND_HALF_UP);
                courier.setRate(courierRate.doubleValue());  // 代取员
            }
        }
        return courierList.stream().sorted(Comparator.comparing(Student::getRate, Comparator.reverseOrder()))
                .limit(10)
                .toList();
    }


    public List<Student> selectNumRank() {
        List<Student> studentList = this.selectAll(null);
        List<Student> courierList = studentList.stream().filter(s -> s.getRole().equals(RoleEnum.COURIER.name())).toList();
        for (Student courier : courierList) {
            Integer count = ordersMapper.selectCountByCourierId(courier.getId());
            courier.setOrderNum(count);
        }
        return courierList.stream().sorted(Comparator.comparing(Student::getOrderNum, Comparator.reverseOrder()))
                .limit(10)
                .toList();
    }

    public List<Student> selectAll(Student student) {
        return studentMapper.selectAll(student);
    }

    public PageInfo<Student> selectPage(Student student, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> list = studentMapper.selectAll(student);
        return PageInfo.of(list);
    }


    /**
     * 学生登录
     */
    public Student login(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbStudent.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 验证学生身份
        if(!dbStudent.getRole().equals(RoleEnum.STUDENT.name())) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(dbStudent.getId() + "-" + dbStudent.getRole(), dbStudent.getPassword());
        dbStudent.setToken(token);
        return dbStudent;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbStudent.setPassword(account.getNewPassword());
        studentMapper.updateById(dbStudent);
    }

    /**
     * 代取员登录
     */
    public Account courierLogin(Account account) {
        Student courier = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(courier)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!courier.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 验证代取员身份
        if(!courier.getRole().equals(RoleEnum.COURIER.name())) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 查询等级名称
        CourierCommission courierCommission = courierCommissionService.selectById(courier.getCommissionId());
        courier.setCommissionName(courierCommission.getName());
        // 生成token
        String token = TokenUtils.createToken(courier.getId() + "-" + courier.getRole(), courier.getPassword());
        courier.setToken(token);
        return courier;
    }

}

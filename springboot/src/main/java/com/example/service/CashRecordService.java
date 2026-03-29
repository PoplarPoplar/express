package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.CashRecord;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.CashRecordMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class CashRecordService {

    @Resource
    private CashRecordMapper cashRecordMapper;
    @Resource
    private StudentService studentService;

    public void add(CashRecord cashRecord) {
        Account currentUser = TokenUtils.getCurrentUser();
        CashRecord record = cashRecordMapper.selectByCourierId(currentUser.getId());
        if (record != null) {
            throw new CustomException("500", "您申请的提现正在审核中，请勿重复申请");
        }
        cashRecord.setApplyTime(DateUtil.now());
        cashRecord.setStatus("待审核");
        cashRecord.setCourierId(currentUser.getId());

        Student courier = studentService.selectById(currentUser.getId());
        cashRecord.setMoney(courier.getCash());
        cashRecordMapper.insert(cashRecord);
    }

    @Transactional
    public void updateById(CashRecord cashRecord) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.ADMIN.name().equals(currentUser.getRole()) && "通过".equals(cashRecord.getStatus())) {
            // 审批通过需要清除钱包
            Student courier = studentService.selectById(cashRecord.getCourierId());
            courier.setCash(0D);
            studentService.updateById(courier);
        }
        cashRecordMapper.updateById(cashRecord);
    }

    public void deleteById(Integer id) {
        cashRecordMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            cashRecordMapper.deleteById(id);
        }
    }

    public CashRecord selectById(Integer id) {
        return cashRecordMapper.selectById(id);
    }

    public List<CashRecord> selectAll(CashRecord cashRecord) {
        return cashRecordMapper.selectAll(cashRecord);
    }

    public PageInfo<CashRecord> selectPage(CashRecord cashRecord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CashRecord> list = cashRecordMapper.selectAll(cashRecord);
        return PageInfo.of(list);
    }

}

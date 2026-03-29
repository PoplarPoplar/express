package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.entity.UpgradeApply;
import com.example.exception.CustomException;
import com.example.mapper.UpgradeApplyMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class UpgradeApplyService {

    @Resource
    private UpgradeApplyMapper upgradeApplyMapper;
    @Resource
    private StudentService studentService;

    public void add(UpgradeApply upgradeApply) {
        upgradeApply.setTime(DateUtil.now());
        upgradeApply.setStatus("待审核");
        Account currentUser = TokenUtils.getCurrentUser();
        upgradeApply.setCourierId(currentUser.getId());

        UpgradeApply apply = upgradeApplyMapper.selectByCourierId(currentUser.getId());
        if (apply != null) {
            throw new CustomException("500", "您已提交申请，请等待管理员审核");
        }
        upgradeApplyMapper.insert(upgradeApply);
    }

    public void updateById(UpgradeApply upgradeApply) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.ADMIN.name().equals(currentUser.getRole()) && "通过".equals(upgradeApply.getStatus())) {
            // 给代取员提升对应的等级
            Student student = studentService.selectById(upgradeApply.getCourierId());
            student.setCommissionId(upgradeApply.getCommissionId());
            studentService.updateById(student);
        }
        upgradeApplyMapper.updateById(upgradeApply);
    }

    public void deleteById(Integer id) {
        upgradeApplyMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            upgradeApplyMapper.deleteById(id);
        }
    }

    public UpgradeApply selectById(Integer id) {
        return upgradeApplyMapper.selectById(id);
    }

    public List<UpgradeApply> selectAll(UpgradeApply upgradeApply) {
        return upgradeApplyMapper.selectAll(upgradeApply);
    }

    public PageInfo<UpgradeApply> selectPage(UpgradeApply upgradeApply, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.COURIER.name().equals(currentUser.getRole())) {
            upgradeApply.setCourierId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UpgradeApply> list = upgradeApplyMapper.selectAll(upgradeApply);
        return PageInfo.of(list);
    }

}

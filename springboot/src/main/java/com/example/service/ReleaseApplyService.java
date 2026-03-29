package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.ReleaseApply;
import com.example.entity.Student;
import com.example.entity.UpgradeApply;
import com.example.exception.CustomException;
import com.example.mapper.ReleaseApplyMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class ReleaseApplyService {

    @Resource
    private ReleaseApplyMapper releaseApplyMapper;
    @Resource
    private StudentService studentService;

    public void add(ReleaseApply releaseApply) {
        releaseApply.setTime(DateUtil.now());
        releaseApply.setStatus("待审核");
        Account currentUser = TokenUtils.getCurrentUser();
        releaseApply.setCourierId(currentUser.getId());

        ReleaseApply apply = releaseApplyMapper.selectByCourierId(currentUser.getId());
        if (apply != null) {
            throw new CustomException("500", "您已提交申请，请等待管理员审核");
        }
        releaseApplyMapper.insert(releaseApply);
    }

    public void updateById(ReleaseApply releaseApply) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.ADMIN.name().equals(currentUser.getRole()) && "通过".equals(releaseApply.getStatus())) {
            // 给代取员解封
            Student student = studentService.selectById(releaseApply.getCourierId());
            student.setStatus("正常");
            studentService.updateById(student);
        }
        releaseApplyMapper.updateById(releaseApply);
    }

    public void deleteById(Integer id) {
        releaseApplyMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            releaseApplyMapper.deleteById(id);
        }
    }

    public ReleaseApply selectById(Integer id) {
        return releaseApplyMapper.selectById(id);
    }

    public List<ReleaseApply> selectAll(ReleaseApply releaseApply) {
        return releaseApplyMapper.selectAll(releaseApply);
    }

    public PageInfo<ReleaseApply> selectPage(ReleaseApply releaseApply, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.COURIER.name().equals(currentUser.getRole())) {
            releaseApply.setCourierId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<ReleaseApply> list = releaseApplyMapper.selectAll(releaseApply);
        return PageInfo.of(list);
    }

}

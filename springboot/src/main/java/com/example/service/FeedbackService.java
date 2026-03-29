package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Feedback;
import com.example.mapper.FeedbackMapper;
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
public class FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    public void add(Feedback feedback) {
        feedback.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        feedback.setStudentId(currentUser.getId());
        feedback.setStatus("待回复");
        feedbackMapper.insert(feedback);
    }

    public void updateById(Feedback feedback) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.ADMIN.name().equals(currentUser.getRole())) {
            feedback.setStatus("已回复");
        }
        feedbackMapper.updateById(feedback);
    }

    public void deleteById(Integer id) {
        feedbackMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            feedbackMapper.deleteById(id);
        }
    }

    public Feedback selectById(Integer id) {
        return feedbackMapper.selectById(id);
    }

    public List<Feedback> selectAll(Feedback feedback) {
        return feedbackMapper.selectAll(feedback);
    }

    public PageInfo<Feedback> selectPage(Feedback feedback, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!RoleEnum.ADMIN.name().equals(currentUser.getRole())) {
            feedback.setStudentId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Feedback> list = feedbackMapper.selectAll(feedback);
        return PageInfo.of(list);
    }

}

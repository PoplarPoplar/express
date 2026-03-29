package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Information;
import com.example.mapper.InformationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class InformationService {

    @Resource
    private InformationMapper informationMapper;

    public void add(Information information) {
        information.setTime(DateUtil.now());
        information.setViewCount(0);
        informationMapper.insert(information);
    }

    public void updateById(Information information) {
        informationMapper.updateById(information);
    }

    public void deleteById(Integer id) {
        informationMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            informationMapper.deleteById(id);
        }
    }

    public Information selectById(Integer id) {
        return informationMapper.selectById(id);
    }

    public List<Information> selectAll(Information information) {
        return informationMapper.selectAll(information);
    }

    public PageInfo<Information> selectPage(Information information, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Information> list = informationMapper.selectAll(information);
        for (Information info : list) {
            info.setSimpleContent(HtmlUtil.cleanHtmlTag(info.getContent()));
        }
        return PageInfo.of(list);
    }

    public void updateViewCount(Integer id) {
        informationMapper.updateViewCount(id);
    }

}

package com.example.mapper;

import com.example.entity.Information;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface InformationMapper {

    int insert(Information information);

    void updateById(Information information);

    void deleteById(Integer id);

    @Select("select * from `information` where id = #{id}")
    Information selectById(Integer id);

    List<Information> selectAll(Information information);

    @Update("update information set view_count = view_count + 1 where id = #{id}")
    void updateViewCount(Integer id);
}

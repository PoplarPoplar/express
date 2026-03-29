package com.example.mapper;

import com.example.entity.Identification;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IdentificationMapper {

    int insert(Identification identification);

    void updateById(Identification identification);

    void deleteById(Integer id);

    @Select("select * from `identification` where id = #{id}")
    Identification selectById(Integer id);

    List<Identification> selectAll(Identification identification);

}

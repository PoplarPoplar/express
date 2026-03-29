package com.example.mapper;

import com.example.entity.ReleaseApply;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReleaseApplyMapper {

    int insert(ReleaseApply releaseApply);

    void updateById(ReleaseApply releaseApply);

    void deleteById(Integer id);

    @Select("select * from `release_apply` where id = #{id}")
    ReleaseApply selectById(Integer id);

    List<ReleaseApply> selectAll(ReleaseApply releaseApply);

    @Select("select * from `release_apply` where courier_id = #{courierId} and status = '待审核'")
    ReleaseApply selectByCourierId(Integer courierId);
}

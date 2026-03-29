package com.example.mapper;

import com.example.entity.UpgradeApply;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UpgradeApplyMapper {

    int insert(UpgradeApply upgradeApply);

    void updateById(UpgradeApply upgradeApply);

    void deleteById(Integer id);

    @Select("select * from `upgrade_apply` where id = #{id}")
    UpgradeApply selectById(Integer id);

    List<UpgradeApply> selectAll(UpgradeApply upgradeApply);

    @Select("select * from `upgrade_apply` where courier_id = #{courierId} and status = '待审核'")
    UpgradeApply selectByCourierId(Integer courierId);

}

package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.CourierCommission;
import com.example.entity.Identification;
import com.example.entity.Student;
import com.example.mapper.IdentificationMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class IdentificationService {

    @Resource
    private IdentificationMapper identificationMapper;
    @Resource
    private CourierCommissionService courierCommissionService;
    @Resource
    private StudentService studentService;

    @Transactional
    public void add(Identification identification) {
        Account currentUser = TokenUtils.getCurrentUser();  // 获取到当前的登录用户信息
        identification.setStudentId(currentUser.getId());
        identification.setStatus("待审核");

        List<CourierCommission> courierCommissions = courierCommissionService.selectAll(null);
        courierCommissions = courierCommissions.stream().sorted(Comparator.comparingDouble(CourierCommission::getCommission)).toList();
        CourierCommission lowCommission = courierCommissions.get(0);  // 最低等级的代取员
        identification.setCommissionId(lowCommission.getId());
        identificationMapper.insert(identification);

        // 更新代取员的等级
        Student courierStudent = (Student) currentUser;
        courierStudent.setCommissionId(lowCommission.getId());
        studentService.updateById(courierStudent);
    }

    @Transactional
    public void updateById(Identification identification) {
        Account currentUser = TokenUtils.getCurrentUser();  // 获取到当前的登录用户信息
        if (currentUser.getRole().equals(RoleEnum.ADMIN.name())) {
            if ("通过".equals(identification.getStatus())) {
                // 审核通过  需要修改学生的角色为代取员
                Student student = studentService.selectById(identification.getStudentId());
                student.setRole(RoleEnum.COURIER.name());
                student.setStatus("正常");
                studentService.updateById(student);
            }
        } else if (currentUser.getRole().equals(RoleEnum.STUDENT.name())) {
            // 再次提交  需要改状态为待审核
            identification.setStatus("待审核");
        }
        identificationMapper.updateById(identification);
    }

    public void deleteById(Integer id) {
        identificationMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            identificationMapper.deleteById(id);
        }
    }

    public Identification selectById(Integer id) {
        return identificationMapper.selectById(id);
    }

    public List<Identification> selectAll(Identification identification) {
        return identificationMapper.selectAll(identification);
    }

    public PageInfo<Identification> selectPage(Identification identification, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Identification> list = identificationMapper.selectAll(identification);
        return PageInfo.of(list);
    }

}

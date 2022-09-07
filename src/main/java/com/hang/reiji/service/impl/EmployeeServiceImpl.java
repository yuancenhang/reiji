package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.Employee;
import com.hang.reiji.mapper.EmployeeMapper;
import com.hang.reiji.service.EmployeeService;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public int Mysave(Employee employee) {

        employee.setPassword(UtilOne.toMD5("123456"));
        employee.setId(UtilOne.getUUID());
        System.out.println();
        employee.setCreateTime(LocalDateTime.now());
        employee.setStatus(1);
        employee.setUpdateTime(LocalDateTime.now());
        return employeeMapper.save(employee);
    }

    /*
    根据name查单条，登陆用的
    */
    @Override
    public Employee getByName(Employee employee) {
        String name = employee.getUsername();
        return employeeMapper.selectByName(name);
    }
}

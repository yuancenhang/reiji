package com.hang.reiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.reiji.domain.Employee;

public interface EmployeeMapper extends BaseMapper<Employee> {

    Employee selectByName(String name);

    int save(Employee employee);
}

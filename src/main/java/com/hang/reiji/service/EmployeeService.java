package com.hang.reiji.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.Employee;

public interface EmployeeService extends IService<Employee> {
    int Mysave(Employee employee);

    Employee getByName(Employee employee);
}

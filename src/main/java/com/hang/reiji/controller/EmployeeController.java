package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.reiji.domain.Employee;
import com.hang.reiji.domain.R;
import com.hang.reiji.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 登陆，employee里只有账号和密码，所以根据账号查
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        //拿密码，转MD5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        Employee employee1 = employeeService.getByName(employee);
        if (employee1 == null) return R.error("账号不存在");
        if (!employee1.getPassword().equals(password)) return R.error("密码错误");
        if (employee1.getStatus() == 0) return R.error("账号已禁用");
        request.getSession().setAttribute("employee",employee1.getId());
        return R.success(employee1);
    }

    /**
     * 退出登陆
     */
    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("已退出系统");
    }

    /**
     * 保存员工
     */
    @PostMapping
    public R<Integer> addEmployee(@RequestBody Employee employee,HttpServletRequest request){
        Long createUser = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(createUser);
        employee.setUpdateUser(createUser);
        int i = employeeService.Mysave(employee);
        return i == 1 ? R.success(1) : R.error("添加失败");
    }

    /**
     * 查询员工,name可能为空
     */
    @RequestMapping("/page")
    public R<IPage<Employee>> getListPage(int page, int pageSize, String name){
        IPage<Employee> iPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null,Employee::getName,name);
        iPage = employeeService.page(iPage,wrapper);
        if (iPage != null){
            return R.success(iPage);
        }
        return R.error("没有记录！！！");
    }

    /**
     * 修改员工的status，员工的禁用和启用
     */
    @PutMapping
    public R<String> updateEmployee(@RequestBody Employee employee){
        boolean ok = employeeService.updateById(employee);
        return ok ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 编辑员工时，获取数据回填
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return employee == null ? R.error("没有这个员工") : R.success(employee);
    }
}














package com.hang.reiji;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.reiji.domain.Employee;
import com.hang.reiji.mapper.EmployeeMapper;
import com.hang.reiji.utils.UtilOne;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;


@SpringBootTest
class ReijiApplicationTests {
/*
    @Autowired
    EmployeeMapper employeeMapper;*/

    /*@Test
    void t1(){
        IPage<Employee> iPage = new Page<>(1,10);
        iPage = employeeMapper.selectPage(iPage,null);
        System.out.println("当前页码：" + iPage.getCurrent());
        System.out.println("每页多少条：" + iPage.getSize());
        System.out.println("一共多少条：" + iPage.getTotal());
        System.out.println("一共多少页：" + iPage.getPages());
        System.out.println("数据：" + iPage.getRecords());
    }*/
    @Test
    void t1(){
        UtilOne.getUUID();
    }


}

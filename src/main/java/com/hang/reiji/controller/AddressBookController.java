package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hang.reiji.domain.AddressBook;
import com.hang.reiji.domain.R;
import com.hang.reiji.service.AddressBookService;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    /**
     * 获取地址列表，虽然没有参数，但是应该根据userId来获取
     * @return list
     */
    @GetMapping("/list")
    public R<List<AddressBook>> getList(){
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,UtilOne.getIdInThread());
        wrapper.orderByDesc(AddressBook::getIsDefault);
        wrapper.orderByDesc(AddressBook::getCreateTime);
        List<AddressBook> list = addressBookService.list(wrapper);
        return list != null ? R.success(list) : R.error("还没有任何地址哦！");
    }

    /**
     * 设置默认地址
     * @param addressBook 里面有id
     * @return String
     */
    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        addressBook.setIsDefault(0);
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,UtilOne.getIdInThread());
        wrapper.eq(AddressBook::getIsDefault,1);
        addressBookService.update(addressBook,wrapper);
        addressBook.setIsDefault(1);
        boolean ok = addressBookService.updateById(addressBook);
        return ok ? R.success("设置成功") : R.error("设置失败");
    }

    /**
     * 获取单个地址，修改地址用的
     * @param id id
     * @return Address
     */
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return addressBook != null ? R.success(addressBook) : R.error("没有这个地址");
    }

    /**
     * 保存地址
     * @param addressBook 应该是传来一个json对象
     * @return String
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(UtilOne.getIdInThread());
        boolean ok = addressBookService.save(addressBook);
        return ok ? R.success("保存成功") : R.error("保存失败");
    }

    /**
     * 更新地址
     * @param addressBook 应该是传来一个json对象
     * @return String
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getId,addressBook.getId());
        boolean ok = addressBookService.update(addressBook,wrapper);
        return ok ? R.success("更新成功") : R.error("更新失败");
    }

    /**
     * 删除地址，支持批量
     * @param ids 传来的ID数组
     * @return String
     */
    @DeleteMapping
    public R<String> delete(Long[] ids){
        List<Long> list = new ArrayList<>(Arrays.asList(ids));
        boolean ok = addressBookService.removeByIds(list);
        return ok ? R.success("删除成功") : R.error("删除失败");
    }

    /**
     * 下单时后获取默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,UtilOne.getIdInThread());
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        return addressBook != null ? R.success(addressBook) : R.error("没有默认地址噢");
    }
}














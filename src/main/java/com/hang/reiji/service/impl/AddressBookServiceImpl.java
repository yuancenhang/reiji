package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.AddressBook;
import com.hang.reiji.mapper.AddressBookMapper;
import com.hang.reiji.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}

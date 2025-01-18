package ocm.itheima.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.entity.AddressBook;
import ocm.itheima.reggie.mapper.AddressBookMapper;
import ocm.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
package ocm.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ocm.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}

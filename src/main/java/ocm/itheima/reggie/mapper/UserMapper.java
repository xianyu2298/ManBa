package ocm.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ocm.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}

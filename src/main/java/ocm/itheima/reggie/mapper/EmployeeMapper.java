package ocm.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ocm.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

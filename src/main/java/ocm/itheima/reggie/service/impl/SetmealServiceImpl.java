package ocm.itheima.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ocm.itheima.reggie.entity.Setmeal;
import ocm.itheima.reggie.mapper.SetmealMapper;
import ocm.itheima.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>implements SetmealService {
}

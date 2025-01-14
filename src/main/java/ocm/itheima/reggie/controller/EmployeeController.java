package ocm.itheima.reggie.controller;
/**
 * 后台登录功能
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import ocm.itheima.reggie.common.R;
import ocm.itheima.reggie.entity.Employee;
import ocm.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login (HttpServletRequest request, @RequestBody Employee employee){

        //1.password进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据USERNAME查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.判断
        if (emp==null){
            return R.error("登录失败");
        }
        //4.密码比对
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //5.查看用户状态是否可用
        if (emp.getStatus()==0){
            return  R.error("账号已禁用");
        }
        //6.登录成功 将员工ID存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的员工ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}

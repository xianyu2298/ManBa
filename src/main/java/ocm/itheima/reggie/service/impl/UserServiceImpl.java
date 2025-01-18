package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.common.CustomExpection;
import ocm.itheima.reggie.entity.User;
import ocm.itheima.reggie.mapper.UserMapper;
import ocm.itheima.reggie.service.IUserService;
import ocm.itheima.reggie.utils.MailUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Service

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    // 发送邮箱验证码
    @Override
    public Boolean sendMsg(User user, HttpSession session) throws MessagingException {
        // 1.获取前端传来的用户邮箱
        String email = user.getEmail();
        // 2.如果邮箱不为空才进行下一步操作
        if (!email.isEmpty()) {
            // 2.1 随机生成六位数验证码
            String code = MailUtils.getCode();
            // 2.2 发送验证码邮件
            MailUtils.sendMail(email, code);
            // 2.3 把获得的验证码存入session保存作用域，方便后面拿出来比对
            session.setAttribute(email, code);
            return true;
        }
        return false;
    }

    // 移动端用户登录登录
    @Override
    public User login(Map<String, String> map, HttpSession session) {
        // 获取前端传送来的用户邮箱
        String email = map.get("email");
        // 获取前端传送来的验证码
        String code = map.get("code");
        // 验证邮箱和验证码是否为空，如果为空则直接登录失败
        if (email.isEmpty() || code.isEmpty()) {
            throw new CustomExpection("邮箱或验证码不能为空");
        }

        // 如果邮箱和验证码不为空，前往调用数据层查询数据库有无该用户
        // 获取之前存在session保存作用域中的正确验证码
        String trueCode = (String) session.getAttribute(email);

        // 比对用户输入的验证码和真实验证码，错了直接登录失败
        if (!code.equals(trueCode)) {
            throw new CustomExpection("验证码错误");
        }

        // 验证码匹配，开始调用数据库查询
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email);
        User user = this.getOne(lqw);

        // 如果数据库中没有该用户，就是新用户，要添加新用户
        if (user == null) {
            // 添加新用户
            user = new User();
            user.setEmail(email);
            this.save(user);
        }
        // 最后把这个登录用户存到session保存作用域中，表示已登录，让拦截器放行
        session.setAttribute("user", user.getId());
        return user;
    }
}

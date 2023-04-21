package com.lin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.common.R;
import com.lin.entity.User;
import com.lin.service.UserService;
import com.lin.untils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 发送短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        // 获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            // 生成随机4为验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            // 调用阿里云提供的短信服务API完成短信发送，为了不浪费钱，注释了，功能不影响
            // SMSUtils.sendMessage("小沐沐吖","SMS_248910220",phone,code);

            // 需要将生成的验证码保存到session
            session.setAttribute(phone, code);
            return R.success("手机验证码短信发送成功！");
        }

        return R.error("短信发送失败");
    }
    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        // 获取手机号
        String phone = map.get("phone").toString();

        // 获取验证码
        String code = map.get("code").toString();

        // 从session中获取保存的验证码
        String codeInSession = (String) session.getAttribute(phone);

        // 进行验证码校验
        if (codeInSession != null && codeInSession.equals(code)){
            // 比对成功，登录成功
            // 判断手机号是否存在（新用户）
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                // 若不存在则添加用户数据
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录成功");
    }
    /**
     * 用户退出
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        // 清理Session中保存的当前用户的id
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
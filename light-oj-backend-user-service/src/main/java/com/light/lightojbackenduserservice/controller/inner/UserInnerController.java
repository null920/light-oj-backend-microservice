package com.light.lightojbackenduserservice.controller.inner;

import com.light.lightojbackendmodel.model.entity.User;
import com.light.lightojbackendserviceclient.service.UserFeignClient;
import com.light.lightojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户服务内部接口
 *
 * @author null&&
 * @Date 2024/7/5 19:38
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {
    @Resource
    UserService userService;


    /**
     * 根据 id 获取用户
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }


    /**
     * 根据 id 列表查询用户列表
     *
     * @param idList id列表
     * @return 用户列表
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}

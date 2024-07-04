package com.light.lightojbackendserviceclient.service;

import com.light.lightojbackendcommon.common.ErrorCode;
import com.light.lightojbackendcommon.exception.BusinessException;
import com.light.lightojbackendmodel.model.entity.User;
import com.light.lightojbackendmodel.model.enums.UserRoleEnum;
import com.light.lightojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.light.lightojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 */
@FeignClient(name = "light-oj-backend-user-service", path = "/api/user")
public interface UserFeignClient {

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 用户信息
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user 用户
     * @return 是否为管理员
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());

    }


    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户
     * @return 脱敏后用户信息
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 根据 id 获取用户
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);


    /**
     * 根据 id 列表查询用户列表
     *
     * @param idList id列表
     * @return 用户列表
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);
}

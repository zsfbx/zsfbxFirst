package cn.smbms.dao.user;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.util.List;

public interface UserMapper {

    /**
     * 增加用户信息
     * @param user
     * @return
     */
    int add( User user);
    /**
     * 通过userCode获取User
     * @param userCode
     * @return
     */
    User getLoginUser(String userCode);

    /**
     * 通过条件查询-userList
     * @param userName
     * @param userRole
     * @return
     */
    List<User> getUserList(@Param("userName") String userName,
                           @Param("userRole") Integer userRole,
                           @Param("from") Integer from,
                           @Param("pageSize") Integer pageSize);

    /**
     * 通过userId获取user
     * @param id
     * @return
     */
    User getUserById(String id);
    /**
     * 通过条件查询-用户表记录数
     *
     * @param userName
     * @param userRole
     * @return
     * @
     */
    int getUserCount(@Param("userName") String userName, @Param("userRole") Integer userRole);

    /**
     * 通过userId删除user
     * @param delId
     * @return
     */
    int deleteUserById(Integer delId);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     * @
     */
    int modify(User user);

    /**
     * 修改当前用户密码
     *
     * @param id
     * @param pwd
     * @return
     * @
     */
    int updatePwd(@Param("id") Integer id, @Param("pwd") String pwd);
}

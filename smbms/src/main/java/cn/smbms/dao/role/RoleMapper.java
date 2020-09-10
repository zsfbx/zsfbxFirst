package cn.smbms.dao.role;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> getRoleList(@Param("roleName") String roleName, @Param("roleCode") String roleCode);

    int add(Role role);

    Role getRoleById(Integer id);

    int modify(Role role);

    int deleteRoleById(Integer delId);
}

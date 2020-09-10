package cn.smbms.service.role;

import java.util.List;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;

public interface RoleService {
	
	public List<Role> getRoleList(String roleName,String roleCode);

	public boolean add(Role role);

	public boolean deleteRoleById(Integer delId);

	public Role getRoleById(String id);

	public boolean modify(Role role);
	
}

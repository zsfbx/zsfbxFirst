package cn.smbms.service.role;

import java.sql.Connection;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.role.RoleDao;
import cn.smbms.dao.role.RoleDaoImpl;
import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleMapper roleMapper;

	
	@Override
	public List<Role> getRoleList(String roleName,String roleCode) {
		List<Role> roleList = null;
		try {
			roleList = roleMapper.getRoleList(roleName,roleCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleList;
	}

	@Override
	public boolean add(Role role) {
		boolean flag = false;
		try {
			if(roleMapper.add(role) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean deleteRoleById(Integer delId) {
		boolean flag = false;
		try {
			if(roleMapper.deleteRoleById(delId) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public Role getRoleById(String id) {
		Role role = null;
		try{
			role = roleMapper.getRoleById(Integer.parseInt(id));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			role = null;
		}
		return role;
	}

	@Override
	public boolean modify(Role role) {
		boolean flag = false;
		try {

			if(roleMapper.modify(role) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

}

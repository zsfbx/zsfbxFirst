package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-09-03 20:37:41
 **/
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/getrolelist.html", method = {RequestMethod.GET, RequestMethod.POST})
    private String getRoleList(@RequestParam(value = "queryRoleName", defaultValue = "") String queryRoleName,
                               @RequestParam(value = "queryRoleCode", defaultValue = "") String queryRoleCode,Model model) {
        List<Role> roleList = roleService.getRoleList(queryRoleName,queryRoleCode);

        model.addAttribute("rolelist",roleList);
        model.addAttribute("queryRoleName", queryRoleName);
        model.addAttribute("queryRoleCode", queryRoleCode);
        return "rolelist";

    }
    @RequestMapping("/addrole.html")
    public String addUser(@ModelAttribute("role") Role role){
        return "roleadd";
    }
    @RequestMapping("/addrolesave.html")
    public String addUserSave(@ModelAttribute("role") @Valid Role role,BindingResult result, HttpSession session){

        if (result.hasErrors()){
            return "roleadd";
        }
        role.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        role.setCreationDate(new Date());
        boolean flag = false;
        flag = roleService.add(role);
        System.out.println("add flag -- > " + flag);
        if(flag){
            return "redirect:/role/getrolelist.html";
        }else{
            return "roleadd";
        }
    }

    @RequestMapping("/tomodify.html")
    public String getRoleById(@RequestParam(value = "roleid", defaultValue = "") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Role role = roleService.getRoleById(id);
            model.addAttribute("role", role);
            return "rolemodify";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/modifysave.html")
    public String modifySave(Role role, HttpServletRequest request){
        role.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        role.setModifyDate(new Date());
        if(roleService.modify(role)){
            return "redirect:/role/getrolelist.html";
        }else{
            return "rolemodify";
        }
    }

    @RequestMapping("/view")
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "") String id) {
//调用后台方法得到user对象
        if (!StringUtils.isNullOrEmpty(id)) {
            Role role = roleService.getRoleById(id);
            return JSON.toJSONString(role);
        } else {
            return "null";
        }
    }

    @RequestMapping("/delrole")
    @ResponseBody
    public String delprovider(@RequestParam(value = "roleid",defaultValue = "") String id) {
        Integer delId = 0;
        try {
            delId = Integer.parseInt(id);
        } catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (delId <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            if (roleService.deleteRoleById(delId)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        //把resultMap转换成json对象输出
        return JSON.toJSONString(resultMap);
    }
}

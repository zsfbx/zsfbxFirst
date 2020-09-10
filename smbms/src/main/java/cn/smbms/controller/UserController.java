package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-08-31 14:12:28
 **/
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/main.html")
    public String main(HttpSession session) {
//        if (session.getAttribute(Constants.USER_SESSION) == null) {
//            return "redirect:/login.html";
//        }
        return "frame";
    }

//    异常处理器
//    @ExceptionHandler(value = {RuntimeException.class})
//    public String handleException(RuntimeException e, HttpServletRequest request){
//        request.setAttribute("e",e);
//        return "login";
//    }

    @RequestMapping(value = "/userlist.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String userList(@RequestParam(value = "queryname", defaultValue = "") String queryUserName,
                           @RequestParam(value = "queryUserRole", defaultValue = "0") Integer queryUserRole,
                           @RequestParam(value = "pageIndex", defaultValue = "1") Integer currentPageNo,
                           Model model) {

        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //总数量（表）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }


        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        List<Role> roleList = roleService.getRoleList("","");
        model.addAttribute("userList", userList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    @RequestMapping("/adduser.html")
    public String addUser(@ModelAttribute("user") User user) {
        return "useradd";
    }

    @RequestMapping("/addusersave.html")
    public String addUserSave(@ModelAttribute("user") @Valid User user, BindingResult result,
                              HttpSession session, Model model,
                              @RequestParam("a_idPicPath") MultipartFile multipartFile,
                              @RequestParam("a_workPicPath") MultipartFile workFile) {

        String savePath = null;
        if (!multipartFile.isEmpty()) {
            String oldName = multipartFile.getOriginalFilename();
            String ext = FilenameUtils.getExtension(oldName);
            long size = multipartFile.getSize();
            if (size > 5000 * 1024) {
                model.addAttribute("uploadFileError", "上传照片不能超过500K");
                return "useradd";
            } else {
                String[] types = {"jpg", "jpeg", "png", "gif"};
                if (!Arrays.asList(types).contains(ext)) {
                    model.addAttribute("uploadFileError", "格式错误");
                    return "useradd";
                } else {

                    String targetPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Personal." + ext;
                    File saveFile = new File(targetPath, fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    try {
                        multipartFile.transferTo(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError", "上传失败，联系管理员");
                        return "useradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }

        String workPath = null;
        if (!workFile.isEmpty()) {
            String oldName = workFile.getOriginalFilename();
            String ext = FilenameUtils.getExtension(oldName);
            long size = workFile.getSize();
            if (size > 5000 * 1024) {
                model.addAttribute("uploadWorkFileError", "上传照片不能超过500K");
                return "useradd";
            } else {
                String[] types = {"jpg", "jpeg", "png", "gif"};
                if (!Arrays.asList(types).contains(ext)) {
                    model.addAttribute("uploadWorkFileError", "格式错误");
                    return "useradd";
                } else {

                    String targetWorkPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
                    String fileWorkName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Work." + ext;
                    File saveWorkFile = new File(targetWorkPath, fileWorkName);
                    if (!saveWorkFile.exists()) {
                        saveWorkFile.mkdirs();
                    }
                    try {
                        workFile.transferTo(saveWorkFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadWorkFileError", "上传失败，联系管理员");
                        return "useradd";
                    }
                    workPath = targetWorkPath + File.separator + fileWorkName;
                }
            }
        }


        if (result.hasErrors()) {
            return "useradd";
        }

        user.setCreationDate(new Date());
        user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setIdPicPath(savePath);
        user.setWorkPicPath(workPath);
        if (userService.add(user)) {
            return "redirect:/user/userlist.html";
        } else {
            return "useradd";
        }
    }

    @RequestMapping("/getrolelist")
    @ResponseBody
    private String getRoleList(Model model) {
        List<Role> roleList = roleService.getRoleList("","");
        if (roleList == null){
            model.addAttribute("uploadFileError", "获取用户角色列表error");
            return "useradd";
        }
        //把roleList转换成json对象输出
        return JSON.toJSONString(roleList);
    }
    @RequestMapping("/tomodify.html")
    public String getUserById(@RequestParam(value = "uid", defaultValue = "") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "usermodify";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/modifysave.html")
    public String modifySave(User user, HttpServletRequest request) {
        user.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if (userService.modify(user)) {
            return "redirect:/user/userlist.html";
        } else {
            return "usermodify";
        }
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
//调用后台方法得到user对象
        if (!StringUtils.isNullOrEmpty(id)) {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "userview";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }


    @RequestMapping("/ucexists.html")
    @ResponseBody
    public String ucexists(@RequestParam(value = "userCode", defaultValue = "") String userCode) {
        HashMap<String, String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            resultMap.put("userCode", "exist");
        } else {
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(value = "/view"/*,produces = {"application/json;charset=utf-8"}*/)
    @ResponseBody
    public String view(@RequestParam(value = "id", defaultValue = "") String id) {
//调用后台方法得到user对象
        if (!StringUtils.isNullOrEmpty(id)) {
            User user = userService.getUserById(id);
            return JSON.toJSONString(user);
        } else {
            return "null";
        }
    }

//    @RequestMapping(value = "/updatepwd.html",method = {RequestMethod.GET,RequestMethod.POST})
//    public String updatePwd(@RequestParam(value = "newpassword",defaultValue = "")String newpassword,
//                            HttpSession session,
//                            Model model){
//        Object o = session.getAttribute(Constants.USER_SESSION);
//
//        boolean flag = false;
//        if(o != null && !StringUtils.isNullOrEmpty(newpassword)){
//            flag = userService.updatePwd(((User)o).getId(),newpassword);
//            if(flag){
//                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
//                session.removeAttribute(Constants.USER_SESSION);//session注销
//            }else{
//                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
//            }
//        }else{
//            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
//        }
//        return "pwdmodify";
//    }

    @RequestMapping("/upPwd.html")
    public String upPwd() {
        return "pwdmodify";
    }

    @RequestMapping(value = "/oldWorld", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String oldWorld(@RequestParam(value = "oldpassword", defaultValue = "") String oldpassword,
                           HttpSession session) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        Map<String, String> resultMap = new HashMap<String, String>();

        if (null == o) {//session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) o).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return JSON.toJSONString(resultMap);
    }

    @RequestMapping(value = "/updatepwd.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String updatePwd(@RequestParam(value = "newpassword", defaultValue = "") String newpassword,
                            HttpSession session,Model model) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        boolean flag = false;
        if(o != null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
            }else{
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        }else{
            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        return "pwdmodify";
    }

    @RequestMapping("/deleuser.html")
    public String deleUser(@RequestParam(value = "uid", defaultValue = "") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "userlist";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/deleusersave")
@ResponseBody
    public String deleUsersave(@RequestParam(value = "uid", defaultValue = "") String id) {
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
            if (userService.deleteUserById(delId)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        //把resultMap转换成json对象输出
        return JSON.toJSONString(resultMap);
    }

}

package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-09-01 09:34:37
 **/
@Controller
@RequestMapping("/provider")
public class PeoviderController {
    @Autowired
    private ProviderService providerService;

    @RequestMapping(value = "/providerlist.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String providerList(@RequestParam(value = "queryProName", defaultValue = "") String queryProName,
                               @RequestParam(value = "queryProCode", defaultValue = "") String queryProCode,
                               Model model) {

        List<Provider> providerList = providerService.getProviderList(queryProName, queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "providerlist";
    }

    @RequestMapping("/addprovider.html")
    public String addUser(@ModelAttribute("provider") Provider provider) {
        return "provideradd";
    }

    @RequestMapping("/addprovidersave.html")
    public String addUserSave(@ModelAttribute("provider") @Valid Provider provider, BindingResult result, HttpSession session,Model model,
                              @RequestParam("a_idPicPath") MultipartFile multipartFile,
                              @RequestParam("a_workPicPath")MultipartFile workFile) {

        String savePath = null;
        if (!multipartFile.isEmpty()){
            String oldName = multipartFile.getOriginalFilename();
            String ext = FilenameUtils.getExtension(oldName);
            long size = multipartFile.getSize();
            if (size > 5000 * 1024){
                model.addAttribute("uploadFileError","上传照片不能超过500K");
                return "provideradd";
            }else {
                String[] types = {"jpg","jpeg","png","gif"};
                if (!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadFileError","格式错误");
                    return "provideradd";
                }else {

                    String targetPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Logo." + ext;
                    File saveFile = new File(targetPath, fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    try {
                        multipartFile.transferTo(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("uploadFileError", "上传失败，联系管理员");
                        return "provideradd";
                    }
                    savePath = targetPath + File.separator + fileName;
                }
            }
        }

//        String workPath = null;
//        if (!workFile.isEmpty()){
//            String oldName = workFile.getOriginalFilename();
//            String ext = FilenameUtils.getExtension(oldName);
//            long size = workFile.getSize();
//            if (size > 5000 * 1024){
//                model.addAttribute("uploadWorkFileError","上传照片不能超过500K");
//                return "useradd";
//            }else {
//                String[] types = {"jpg","jpeg","png","gif"};
//                if (!Arrays.asList(types).contains(ext)){
//                    model.addAttribute("uploadWorkFileError","格式错误");
//                    return "useradd";
//                }else {
//
//                    String targetWorkPath = session.getServletContext().getRealPath("statics" + File.separator + "upload");
//                    String fileWorkName = System.currentTimeMillis() + RandomUtils.nextInt(100000) + "_Work." + ext;
//                    File saveWorkFile = new File(targetWorkPath, fileWorkName);
//                    if (!saveWorkFile.exists()) {
//                        saveWorkFile.mkdirs();
//                    }
//                    try {
//                        workFile.transferTo(saveWorkFile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        model.addAttribute("uploadWorkFileError", "上传失败，联系管理员");
//                        return "useradd";
//                    }
//                    workPath = targetWorkPath + File.separator + fileWorkName;
//                }
//            }
//        }

        if (result.hasErrors()) {
            return "provideradd";
        }
        provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        provider.setIdPicPath(savePath);
        boolean flag = false;
        flag = providerService.add(provider);
        if(flag){
            return "redirect:/provider/providerlist.html";
        } else {
            return "provideradd";
        }
    }


    @RequestMapping("/tomodify.html")
    public String getProviderById(@RequestParam(value = "proid", defaultValue = "") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providermodify";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/modifysave.html")
    public String modifySave(Provider provider, HttpServletRequest request){
        provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        if(providerService.modify(provider)){
            return "redirect:/provider/providerlist.html";
        }else{
            return "providermodify";
        }
    }
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
//调用后台方法得到user对象
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providerview";
        } else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/view")
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "") String id) {
//调用后台方法得到user对象
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = providerService.getProviderById(id);
            return JSON.toJSONString(provider);
        } else {
            return "null";
        }
    }

    @RequestMapping("/delprovider")
    @ResponseBody
    public String delprovider(@RequestParam(value = "proid",defaultValue = "") String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        return JSON.toJSONString(resultMap);
    }
}

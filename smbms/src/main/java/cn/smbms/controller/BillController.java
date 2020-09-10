package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-09-01 10:02:17
 **/
@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private ProviderService providerService;
    @Autowired
    private BillService billService;

    @RequestMapping(value = "/billlist.html",method = {RequestMethod.GET,RequestMethod.POST})
    public String providerList(@RequestParam(value = "queryProductName",defaultValue = "")String queryProductName,
                               @RequestParam(value = "queryProviderId",defaultValue = "")String queryProviderId,
                               @RequestParam(value = "queryIsPayment",defaultValue = "")String queryIsPayment,
                               Model model){
        List<Provider> providerList = providerService.getProviderList("","");
        model.addAttribute("providerList", providerList);

        Bill bill = new Bill();
        if(StringUtils.isNullOrEmpty(queryIsPayment)){
            bill.setIsPayment(0);
        }else{
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        if(StringUtils.isNullOrEmpty(queryProviderId)){
            bill.setProviderId(0);
        }else{
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);
        List<Bill> billList = billService.getBillList(bill);
       model.addAttribute("billList", billList);
       model.addAttribute("queryProductName", queryProductName);
       model.addAttribute("queryProviderId", queryProviderId);
       model.addAttribute("queryIsPayment", queryIsPayment);
       return "billlist";
    }

    @RequestMapping("/addbill.html")
    public String addUser(@ModelAttribute("bill") Bill bill){
        return "billadd";
    }
    @RequestMapping("/addbillsave.html")
    public String addUserSave(@ModelAttribute("bill") @Valid Bill bill, BindingResult result, HttpSession session){

        if (result.hasErrors()){
            return "billadd";
        }
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        flag = billService.add(bill);
        System.out.println("add flag -- > " + flag);
        if(flag){
            return "redirect:/bill/billlist.html";
        }else{
            return "billadd";
        }
    }

    @RequestMapping("/tomodify.html")
    public String getBillById(@RequestParam(value = "billid",defaultValue = "")String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到bill对象
            Bill bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billmodify";
        }else {
            throw new RuntimeException("数据不存在");
        }
    }
    @RequestMapping("/modifysave.html")
    public String modifySave(Bill bill, HttpServletRequest request){

        bill.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        if(billService.modify(bill)){
            return "redirect:/bill/billlist.html";
        }else{
            return "billmodify";
        }
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
//调用后台方法得到user对象
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到bill对象
            Bill bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billview";
        }else {
            throw new RuntimeException("数据不存在");
        }
    }

    @RequestMapping("/view")
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "") String id) {
//调用后台方法得到user对象
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到bill对象
            Bill bill = billService.getBillById(id);
            return JSON.toJSONString(bill);
        }else {
            return "null";
        }
    }

    @RequestMapping("/delbill")
    @ResponseBody
    public String delbill(@RequestParam(value = "billid",defaultValue = "") String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            boolean flag = billService.deleteBillById(id);
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        return JSON.toJSONString(resultMap);
    }

    @RequestMapping("/getProviderlist")
    @ResponseBody
    public String getProviderlist(){
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("","");
        //把providerList转换成json对象输出
       return JSON.toJSONString(providerList);
    }
}

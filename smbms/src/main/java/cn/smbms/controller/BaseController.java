package cn.smbms.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-09-04 09:54:48
 **/
public class BaseController {
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
//        binder.registerCustomEditor(Date.class,new PropertyEditorSupport(){
//            @Override
//            public String getAsText() {
//                return new SimpleDateFormat("yyyy-MM-dd").format((Date) getValue());
//            }
//
//            @Override
//            public void setAsText(String text) throws IllegalArgumentException {
//                Date date = null;
//                try {
//                    date = new SimpleDateFormat("yyyy-MM-dd").parse(text);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                setValue(date);
//            }
//        });
    }
}

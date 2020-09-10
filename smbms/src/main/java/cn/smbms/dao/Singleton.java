package cn.smbms.dao;

/**
 * @program: mavensmbms
 * @description:
 * @author: lrh
 * @create: 2020-08-31 09:51:44
 **/
public class Singleton {
    private static Singleton singleton;
    private Singleton(){}
    public static Singleton getSingleton(){
        if (singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}

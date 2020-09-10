package cn.smbms.tools;

import cn.smbms.dao.BaseDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-08-31 10:11:34
 **/
public class ConfigManager {
    private static ConfigManager configManager;

    private static Properties properties;
    private ConfigManager(){

        properties =new Properties();
        String configFile = "database.properties";
        InputStream is= BaseDao.class.getClassLoader().getResourceAsStream(configFile);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class InitInstance{
        private static ConfigManager instance = new ConfigManager();
    }

    public static ConfigManager getInstance(){
        configManager = InitInstance.instance;
        return configManager;
    }

//    public static synchronized ConfigManager getInstance(){
//        if (configManager == null){
//            configManager = new ConfigManager();
//        }
//        return configManager;
//    }

    public String getValue(String key){
        return properties.getProperty(key);
    }
}

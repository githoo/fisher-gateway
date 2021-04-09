package com.fisher.base.gateway.core;

import com.fisher.base.gateway.util.XGBUtils;
import com.fisher.base.gateway.util.ConfigParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Created by fisher
 */
public class ProxyServer {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);


    private  static double limitQPS = 10;
    static {
        //初始化配置文件
        try {
            Properties properties_cs= ConfigParseUtil.parse(null, "conf.properties");
            String config_switch = null;
            config_switch = ConfigParseUtil.getProperty(properties_cs, "config_switch");
            if (null != config_switch && !"".equals(config_switch)){
                Properties properties= ConfigParseUtil.parse(null, "config-"+config_switch+".properties");
                ConfigParseUtil.printProperty(properties,150,"*");
                String limit_QPS = ConfigParseUtil.getProperty(properties,"limit_QPS");
                limitQPS = Double.valueOf(limit_QPS);
                String modelPath = ConfigParseUtil.getProperty(properties,"xgb_model_path");
                XGBUtils.init(""+modelPath);
            }else {
                logger.error("config_switch is not config of conf.properties !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  double getLimitQPS(){
        return limitQPS;
    }

    public static void main(String[] args) throws Exception {

    }
}

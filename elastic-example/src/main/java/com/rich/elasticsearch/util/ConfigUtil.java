/** 
 * Project Name:elastic-example 
 * File Name:ConfigUtil.java 
 * Package Name:com.rich.elasticsearch.util 
 * Date:2015年11月5日上午11:19:41 
 * 
*/  
  
package com.rich.elasticsearch.util;  

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * ClassName:ConfigUtil <br/> 
 * Function: Get configuration from property file. <br/> 
 * Date:     2015年11月5日 上午11:19:41 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class ConfigUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);
    
    private static Properties properties = new Properties();
    
    static {
        loadProperty();
    }
    
    private static void loadProperty(){
        String fileNmae = "/config.properties";
        try {
            properties.load(ConfigUtil.class.getResourceAsStream(fileNmae));
        } catch (IOException e) {
            LOGGER.error("Load config property fail, file name:{}", fileNmae, e);
        }
    }
    
    public static String getElasticHost(){
        checkInitailState();
        
        String host = null;
        try {
            host = (String) properties.get("elastic.host");
        } catch (Exception e) {
            LOGGER.error("Get elastic.host error", e);
        }
        
        return host;
    }
    
    public static String getElasticClusterName(){
        checkInitailState();
        
        String clusterName = null;
        try {
            clusterName = (String) properties.get("elastic.cluster.name");
        } catch (Exception e) {
            LOGGER.error("Get elastic.cluster.name error", e);
        }
        
        return clusterName;
    }
    
    public static Integer getElasticPort(){
        checkInitailState();
        
        Integer port = null;
        try {
            port = Integer.parseInt((String) properties.get("elastic.tcp.port"));
        } catch (Exception e) {
            LOGGER.error("Get elastic.tcp.port error", e);
        }
        
        return port;
    }
    
    private static void checkInitailState(){
        if(properties == null){
            throw new IllegalStateException("Config initial failed!");
        }
    }

}
  
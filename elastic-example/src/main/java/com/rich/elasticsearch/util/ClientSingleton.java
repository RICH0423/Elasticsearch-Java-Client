/** 
 * Project Name:elastic-example 
 * File Name:ClientSingleton.java 
 * Package Name:com.rich.elasticsearch.util 
 * Date:2015年11月5日上午11:12:13 
 * 
*/  
  
package com.rich.elasticsearch.util;  

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rich.elasticsearch.ClientApp;

/** 
 * ClassName:ClientSingleton <br/> 
 * Function: Get Elasticsearch client instance, which implement Singleton pattern. <br/> 
 * Date:     2015年11月5日 上午11:12:13 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class ClientSingleton {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApp.class);
    
    private static Client client = initialClient();
    
    private ClientSingleton(){}
    
    private static Client initialClient(){
        String clusterName = ConfigUtil.getElasticClusterName();
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName).build();
        
        String hostName = ConfigUtil.getElasticHost();
        int port = ConfigUtil.getElasticPort();
        Client client = new TransportClient(settings).addTransportAddress(
                new InetSocketTransportAddress(hostName, port));
        
        LOGGER.info("Initial Client success, cluster name:{}, host:{}, port:{}", 
                new Object[]{clusterName, hostName, port});
        return client;
    }
    
    /**
     * Get client instance.
     * @return Client
     */
    public static Client getClientInstance(){
        return client;
    }

}
  
package com.rich.elasticsearch;

import java.util.Date;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rich.elasticsearch.document.Employee;
import com.rich.elasticsearch.util.ClientSingleton;
import com.rich.elasticsearch.util.ElasticsearchUtil;

/**
 * Execute Elasticsearch entry point
 * 
 * @author rich
 *
 */
public class ClientApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args) {
        Client client = ClientSingleton.getClientInstance();
        
        Employee employee_rich = new Employee("RICH", 30, "Engineer", new Date());
        String id = testIndex(client, employee_rich);
        searchIndex(client, id);
        
        client.close();
    }
    
    private static void testUpdate(Client client, String id){
        ElasticsearchUtil.updateDocument(client, "test", "employee", id, "title", "PG");
        LOGGER.info("Update ID: {}, success" , id);
    }
    
    private static String testIndex(Client client, Employee employee) {
        String id = ElasticsearchUtil.indexDocument(client, "test", "employee", employee);
        LOGGER.info("Index ID: {}, success" , id);
        return id;
    }
    
    private static void searchIndex(Client client, String id){
        ElasticsearchUtil.searchDocument(client, "test", "employee", id);
    }
    
}

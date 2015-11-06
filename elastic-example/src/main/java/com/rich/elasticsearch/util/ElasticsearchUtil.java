/** 
 * Project Name:elastic-example 
 * File Name:ElasticsearchUtil.java 
 * Package Name:com.rich.elasticsearch 
 * Date:2015年11月4日下午4:28:52 
 * 
*/  
  
package com.rich.elasticsearch.util;  

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * ClassName:ElasticsearchUtil <br/> 
 * Date:     2015年11月4日 下午4:28:52 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class ElasticsearchUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchUtil.class);
    
    /**
     * Index document.
     * 
     * @param client
     * @param index
     * @param type
     * @param document
     * @return
     */
    public static String indexDocument(Client client, String index, String type, Object document) {
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = null;
        try {
            json = mapper.writeValueAsBytes(document);
        } catch (JsonProcessingException e) {
            LOGGER.error("Parse bean to JSON error!", e);
            return null;
        }
        
        IndexResponse response = null;
        try {
            response = client.prepareIndex(index, type).setSource(json).execute().actionGet();
        } catch (ElasticsearchException e) {
            LOGGER.error("Indexing document error!", e);
            return null;
        }
        
        String id = response != null ? response.getId() : "";
        LOGGER.info("Indexing document success, id: {}", id);
        
        return id;
    }
        
    /**
     * Index document.
     * 
     * @param client
     * @param index
     * @param type
     * @param document
     * @return
     */
    public static String indexDocument(Client client, String index, String type, 
            Map<String, Object> document) {
        IndexResponse response = null;
        try {
            response = client.prepareIndex(index, type).setSource(document).execute().actionGet();
        } catch (ElasticsearchException e) {
            LOGGER.error("Indexing document error!", e);
            return null;
        }
        
        String id = response != null ? response.getId() : "";
        LOGGER.info("Indexing document success, id: {}", id);
        
        return id;
    }
    
    /**
     * Search Document.
     * @param client
     * @param index
     * @param type
     * @param id
     * @return Map<String, Object>
     */
    public static Map<String, Object> searchDocument(Client client, String index,
            String type, String id) {
        GetResponse response = client.prepareGet(index, type, id)
                .setOperationThreaded(false)
                .execute()
                .actionGet();
        
        Map<String, Object> resource = response.getSource();
        LOGGER.info("resource result: {}", resource);
        
        return resource;
    }
    
    /**
     * Search Document and print result.
     * @param client
     * @param index
     * @param type
     * @param field
     * @param value
     */
    public static void searchDocument(Client client, String index, String type,
            String field, String value) {

        SearchResponse response = client.prepareSearch(index).setTypes(type)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.matchQuery(field, value)).setFrom(0)
                .setSize(60).setExplain(true).execute().actionGet();

        SearchHit[] results = response.getHits().getHits();

        LOGGER.info("Current results: " + results.length);
        for (SearchHit hit : results) {
            LOGGER.info("------------------------------");
            Map<String, Object> result = hit.getSource();
            LOGGER.info("Search result: {}", result);
        }
    }
    
    /**
     * Update Document, the method also support passing a partial document, 
     * which will be merged into the existing document (simple recursive merge, 
     * inner merging of objects, replacing core "keys/values" and arrays).
     * 
     * @param client
     * @param index
     * @param type
     * @param id
     * @param filed
     * @param value
     * @return
     */
    public static String updateDocument(Client client, String index, String type,
            String id, String filed, String value) {
        
        UpdateResponse updateResponse = null;
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                    .doc(XContentFactory.jsonBuilder().startObject()
                            .field(filed, value).endObject());
            updateResponse = client.update(updateRequest).get();
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOGGER.error("Update document error!", e);
        }
        
        return updateResponse.getId();
    }
    
    /**
     * Update Document use script.
     * 
     * @param client
     * @param index
     * @param type
     * @param id
     * @param filed
     * @param value
     */
    public static void updateDocumentByScript(Client client, String index, String type,
            String id, String filed, String value){
        
        String script = "ctx._source." + filed + "= \"" + value + "\"";
        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
        .script(script, ScriptType.INLINE).scriptLang("javascript");
        
        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.update(updateRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Update document error, script: {}", script, e);
        }
        
        updateResponse.getId();
    }
    
    /**
     * Update Document by id.
     * 
     * @param client
     * @param index
     * @param type
     * @param id
     * @param field
     * @param newValue
     */
    public static void updateDocument(Client client, String index, String type,
            String id, String field, String[] newValue) {

        String tags = "";
        for (String tag : newValue)
            tags += tag + ", ";

        tags = tags.substring(0, tags.length() - 2);

        Map<String, Object> updateObject = new HashMap<String, Object>();
        updateObject.put(field, tags);

        client.prepareUpdate(index, type, id)
                .setScript("ctx._source." + field + "+=" + field, ScriptType.INLINE)
                .setScriptParams(updateObject).execute().actionGet();
    }
    
    /**
     * Delete document by id.
     * 
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void deleteDocument(Client client, String index, String type, String id){
        DeleteResponse response = client.prepareDelete(index, type, id).setOperationThreaded(false)
                .execute().actionGet();
        
        LOGGER.info("Information on the deleted document:");
        LOGGER.info("Index: " + response.getIndex());
        LOGGER.info("Type: " + response.getType());
        LOGGER.info("Id: " + response.getId());
        LOGGER.info("Version: " + response.getVersion());
  }

}
  
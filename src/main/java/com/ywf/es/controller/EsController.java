package com.ywf.es.controller;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/esController")
@RestController
public class EsController {

    @Autowired
    private RestHighLevelClient client;

    @GetMapping("/autoThink")
    public List autoThink(@RequestParam String key) {

        // 构造查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name.pinyin", key);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0).size(10);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("medcl").source(searchSourceBuilder);
        List respList = new ArrayList();
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            for (SearchHit searchHit : searchHits.getHits()) {
                System.out.println(searchHit.getSourceAsString());
                respList.add(searchHit.getSourceAsMap());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return respList;
    }
}

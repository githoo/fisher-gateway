package com.fisher.base.gateway.service.impl;


import com.fisher.base.gateway.service.ProvideService;

import java.util.Map;

/**
 * Created by fisher
 */
public class DefaultServiceImpl implements ProvideService {

    @Override
    public String excute(Map<String,String> param, String postData) {

       String x = "{\"sku\": \"123\",\"code\": 1,\"version\": \"1.0.0\",\"latency\": 14,\"data\": [{\"tag\": \"高清0\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清1\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清2\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清3\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清4\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清5\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清6\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清7\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清8\",\"count\": 1,\"weight\": 0.9},{\"tag\": \"高清9\",\"count\": 1,\"weight\": 0.9}]}";
       return  x;
    }
}

package com.ityuan.es.service;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author yuanchaoxin
 * @Date 2021/6/27 15:24
 * @Version 1.0
 */
public interface ContentService {

    public Boolean parse(String keyword) throws Exception;

    public List<Map<String,Object>> pageSearch(String keyword, int pageNo, int pageSize) throws Exception;

    List<Map<String, Object>> pageSearchHighlight(String keyword, int pageNo, int pageSize) throws Exception;
}

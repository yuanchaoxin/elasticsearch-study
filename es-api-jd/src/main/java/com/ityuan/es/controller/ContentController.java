package com.ityuan.es.controller;

import com.ityuan.es.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ContentController
 * @Package com.ityuan.es.controller
 * @Author yuanchaoxin
 * @Date 2021/6/27
 * @Version 1.0
 * @Description
 */
@Controller
public class ContentController {

    @Resource
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    @ResponseBody
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception{
        return contentService.parse(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    @ResponseBody
    public List<Map<String, Object>> pageSearch(@PathVariable("keyword") String keyword,
                                                @PathVariable("pageNo")int pageNo,
                                                @PathVariable("pageSize")int pageSize,
                                                @RequestParam("isClick") boolean isClick) throws Exception {
        if (isClick) {
            return contentService.pageSearchHighlight(keyword, pageNo, pageSize);
        }
        return contentService.pageSearch(keyword, pageNo, pageSize);
    }
}

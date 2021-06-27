package com.ityuan.es.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName IndexController
 * @Package com.ityuan.es.controller
 * @Author yuanchaoxin
 * @Date 2021/6/27
 * @Version 1.0
 * @Description
 */
@Controller
public class IndexController {

    @RequestMapping({"","/index"})
    public String index() {
        return "index";
    }
}

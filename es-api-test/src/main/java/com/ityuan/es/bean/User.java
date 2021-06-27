package com.ityuan.es.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ClassName User
 * @Package com.ityuan.es.api.esdemo.bean
 * @Author yuanchaoxin
 * @Date 2021/6/22
 * @Version 1.0
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User {
    private String name;
    private Integer age;
}

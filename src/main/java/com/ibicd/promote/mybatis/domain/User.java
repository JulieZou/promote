package com.ibicd.promote.mybatis.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class User {

    private Integer id;
    private String userName;
    private String passWord;
    private String realName;
}

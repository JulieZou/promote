package com.ibicd.promote.mybatis.mapper;

import com.ibicd.promote.mybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User findById(int id);
}

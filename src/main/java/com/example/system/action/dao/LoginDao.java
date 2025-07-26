package com.example.system.action.dao;

import com.example.system.action.model.Role;
import com.example.system.action.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginDao {
    @Select("select * from userinfo where account =#{account} ")
    public User getUserByAccount(String account);

    @Select("select r.* from user_role ur left JOIN role r ON ur.role_id = r.id where ur.user_id =#{userId} ")
    public List<Role> getRoleByUserId(String userId);
}

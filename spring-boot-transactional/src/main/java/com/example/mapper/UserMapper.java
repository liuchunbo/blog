package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Mapper
 * 
 * @author CL
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

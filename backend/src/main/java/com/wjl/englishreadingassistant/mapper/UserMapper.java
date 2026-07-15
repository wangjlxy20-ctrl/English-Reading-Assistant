package com.wjl.englishreadingassistant.mapper;

import com.wjl.englishreadingassistant.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
        @Select("""
        select *
        from era_user
        where username = #{username}

    """)
        User findByUsername(@Param("username") String username);

        @Insert("""
        insert into era_user
        (username,password,email)
        values (
                #{username},
                #{password},
                #{email}
        )
        
        """)
        void insert(@Param("eraUser") String eraUser);
    }


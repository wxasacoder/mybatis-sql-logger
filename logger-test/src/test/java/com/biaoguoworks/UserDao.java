package com.biaoguoworks;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuxin
 * @date 2025/04/25 12:44:15
 */
public interface UserDao {


    String selectNameAndIds(@Param("name") String name, @Param("ids") List<Long> ids);

    Integer insert(@Param("user") User user);

    Integer insertBatch(@Param("users") List<User> users);

    void insertByProcedure(@Param("name") String name);

    Integer update(@Param("user") User user);

    Integer updateBatch(@Param("users") List<User> users);

    Integer batchUpdateUsers(@Param("users") List<User> users);

}

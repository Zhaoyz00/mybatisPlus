package com.example.plus;

import com.example.plus.bean.User;
import com.example.plus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
public class MyBatisPlusTest {

//    @Autowired
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        userMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("张三");
        user.setAge(23);
        user.setEmail("ZhangSan@qq.com");
        int res = userMapper.insert(user);
        System.out.println("res = " + res);
        System.out.println("id = " + user.getId());
    }

    @Test
    public void testDeleteById() {
        int res = userMapper.deleteById(6L);
        System.out.println("res = " + res);
    }

    @Test
    public void testDeleteByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 21);
        int res = userMapper.deleteByMap(map);
        System.out.println("res = " + res);
    }

    @Test
    public void testUpdateById() {
        User user = new User(6L, "admin", 22, null, null);
        int res = userMapper.updateById(user);
        System.out.println("res = " + res);
    }

    @Test
    public void testSelectById() {
        User user = userMapper.selectById(6L);
        System.out.println(user);
    }

    @Test
    public void testSelectBatchIds() {
        List<Long> list = Arrays.asList(4L, 5L);
        List<User> users = userMapper.selectBatchIds(list);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "admin");
        map.put("age", 22);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }


}

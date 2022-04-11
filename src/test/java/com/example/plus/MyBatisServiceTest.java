package com.example.plus;

import com.example.plus.bean.User;
import com.example.plus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MyBatisServiceTest {

    @Autowired
    private UserService userService;

    /**
     * SELECT COUNT( * ) FROM user
     */
    @Test
    public void testGetCount() {
        long count = userService.count();
        System.out.println("count = " + count);
    }

    @Test
    public void testSaveBatch() {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("ybc" + i);
            user.setAge(20 + i);
            users.add(user);
        }
        boolean b = userService.saveBatch(users);
        System.out.println(b);
    }
}

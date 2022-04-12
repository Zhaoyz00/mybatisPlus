package com.example.plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.plus.bean.Product;
import com.example.plus.bean.User;
import com.example.plus.enums.SexEnum;
import com.example.plus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
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
        User user = new User(6L, "admin", 22, null, null, 0);
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

    /**
     *
     */
    @Test
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "admin");
        map.put("age", 22);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    /**
     * QueryWrapper:组装查询条件
     * 查询用户名包含a，年龄在20到30之间，并且邮箱不为null的用户信息
     */
    @Test
    public void testSelectByWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "a")
                .between("age", 18, 22)
                .isNotNull("email");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * QueryWrapper:组装排序条件
     * 按年龄降序查询用户，如果年龄相同则按id升序排列
     */
    @Test
    public void testOrderByWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                .orderByAsc("id");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * QueryWrapper:组装删除条件
     * 删除email为空的用户
     */
    @Test
    public void testDeleteByWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int res = userMapper.delete(queryWrapper);
        System.out.println("受影响的行数: " + res);
    }

    /**
     * QueryWrapper:条件的优先级1
     * 将（年龄大于30并且用户名中包含有a）或邮箱为null的用户信息修改
     */
    @Test
    public void testConditionPriority1() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("age", 30)
                .like("name", "a")
                .or()
                .isNull("email");
        User user = new User();
        user.setAge(18);
        user.setEmail("abc@qq.com");
        int res = userMapper.update(user, queryWrapper);
        System.out.println("受影响的行数: " + res);
    }

    /**
     * QueryWrapper:条件的优先级1
     * 将用户名中包含有a并且（年龄大于30或邮箱为null）的用户信息修改
     */
    @Test
    public void testConditionPriority2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "a")
                .and(i -> i.gt("age", 30)
                        .or()
                        .isNull("email"));
        User user = new User();
        user.setAge(18);
        user.setEmail("abc@qq.com");
        int res = userMapper.update(user, queryWrapper);
        System.out.println("受影响的行数: " + res);
    }

    /**
     * QueryWrapper:组装select子句
     * 查询用户信息的name和age字段
     */
    @Test
    public void testColumns() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name", "age");
        userMapper.selectMaps(queryWrapper).forEach(System.out::println);
    }

    /**
     * QueryWrapper:实现子查询
     * 查询id小于等于3的用户信息
     */
    @Test
    public void testSubQuery() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // SELECT id,name,age,email,is_deleted FROM user WHERE is_deleted=0 AND (id <= (3))
        // queryWrapper.le("id", 3);
        // queryWrapper.leSql("id", "3");

        //SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE (id IN (select id from t_user where id <= 3))
        queryWrapper.inSql("id", "select id from user where id <= 3");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * UpdateWrapper
     * 将（年龄大于30或邮箱为null）并且用户名中包含有a的用户信息修改
     */
    @Test
    public void testUpdateWrapper() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
/*        updateWrapper.gt("age", 30)
                .or()
                .isNull("email")
                .and(i -> i.like("name", "a"));
        User user = new User();
        user.setAge(18);
        int res = userMapper.update(user, updateWrapper);*/
        updateWrapper
                .set("age", 18)
                .like("name", "a")
                .and(i -> i.gt("age", 30).or().isNull("email"));
        int res = userMapper.update(new User(), updateWrapper);
        System.out.println("受影响的行数: " + res);
    }

    /**
     * condition
     * 定义查询条件，有可能为null（用户未输入或未选择）
     */
    @Test
    public void testCondition() {
        // 查询条件
        String name = null;
        Integer ageBegin = 18;
        Integer ageEnd = 20;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotBlank(name), "name", "a")
                .ge(ageBegin != null, "age", ageBegin)
                .le(ageEnd != null, "age", ageEnd);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * LambdaQueryWrapper:防止写错字段名
     */
    @Test
    public void testLambdaQuery() {
        String name = null;
        Integer ageBegin = 18;
        Integer ageEnd = 20;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), User::getName, "a")
                .ge(ageBegin != null, User::getAge, ageBegin)
                .le(ageEnd != null, User::getAge, ageEnd);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    /**
     * 通用枚举
     */
    @Test
    public void testSexEnum() {
        User user = new User();
        user.setName("Enum");
        user.setAge(20);
        //设置性别信息为枚举项，会将@EnumValue注解所标识的属性值存储到数据库
        user.setSex(SexEnum.MALE);
        //INSERT INTO t_user ( username, age, sex ) VALUES ( ?, ?, ? )
        //Parameters: Enum(String), 20(Integer), 1(Integer)
        userMapper.insert(user);
    }

}

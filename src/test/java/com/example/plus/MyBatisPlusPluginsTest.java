package com.example.plus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.plus.bean.Product;
import com.example.plus.bean.User;
import com.example.plus.mapper.ProductMapper;
import com.example.plus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class MyBatisPlusPluginsTest {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ProductMapper productMapper;

    /**
     * PaginationInnerInterceptor(DbType.MYSQL):分页插件
     */
    @Test
    public void testPage() {
        Page<User> page = new Page<>(2, 3);
        userMapper.selectPage(page, null);
        List<User> list = page.getRecords();
        list.forEach(System.out::println);
    }

    @Test
    public void testConcurrentVersionUpdate() {
        //小李取数据
        Product p1 = productMapper.selectById(1L);
        //小王取数据
        Product p2 = productMapper.selectById(1L);
        //小李修改 + 50
        p1.setPrice(p1.getPrice() + 50);
        int result1 = productMapper.updateById(p1);
        System.out.println("*************小李修改的结果：" + result1);
        //小王修改 - 30
        p2.setPrice(p2.getPrice() - 30);
        int result2 = productMapper.updateById(p2);
        System.out.println("*************小王修改的结果：" + result2);
        if (result2 == 0) {
            //失败重试，重新获取version并更新
            p2 = productMapper.selectById(1L);
            p2.setPrice(p2.getPrice() - 30);
            result2 = productMapper.updateById(p2);
        }
        System.out.println("*************小王修改重试的结果：" + result2);
        //老板看价格
        Product p3 = productMapper.selectById(1L);
        System.out.println("*************老板看价格：" + p3.getPrice());
    }
}

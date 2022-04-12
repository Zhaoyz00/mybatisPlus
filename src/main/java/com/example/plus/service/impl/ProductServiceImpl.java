package com.example.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.plus.bean.Product;
import com.example.plus.mapper.ProductMapper;
import com.example.plus.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * @author hp-pc
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}

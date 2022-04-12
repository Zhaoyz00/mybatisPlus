package com.example.plus.bean;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.example.plus.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author hp-pc
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private int age;
    private String email;
    private SexEnum sex;
    @TableLogic
    private Integer isDeleted;
}

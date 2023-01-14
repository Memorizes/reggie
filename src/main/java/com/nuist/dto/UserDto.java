package com.nuist.dto;

import com.nuist.pojo.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String code;
}

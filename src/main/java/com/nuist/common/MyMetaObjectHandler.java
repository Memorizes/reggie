package com.nuist.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getValue();
        metaObject.setValue("createTime", time);
        metaObject.setValue("updateTime", time);
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime time = LocalDateTime.now();
        Long id = BaseContext.getValue();
        metaObject.setValue("updateTime", time);
        metaObject.setValue("updateUser", id);
    }
}

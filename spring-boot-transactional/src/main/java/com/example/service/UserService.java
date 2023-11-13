package com.example.service;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private PlatformTransactionManager transactionManager;

    // 在此方法中使用编程式的事物

    public int insert() {
        
        User user = new User();
        user.setId(1);
        int result = 0;

        // 开启事务（获取事务）
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {

            result = userMapper.insert(user);
            System.out.println("add 受影响的行数：" + result);

            // 提交事务
            transactionManager.commit(transactionStatus);

           
        } catch (Exception e) {
            // 回滚事务
            transactionManager.rollback(transactionStatus);
        }

        return result;
    }
}


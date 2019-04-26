package com.inext.dao;

import com.inext.entity.UserMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserMessageDao {

    void saveUserMsg(UserMessage message);

}

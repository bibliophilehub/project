package com.inext.service.impl;

import com.inext.dao.IUserMessageDao;
import com.inext.entity.UserMessage;
import com.inext.service.IUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMessageService implements IUserMessageService {
    @Autowired
    private IUserMessageDao userMessageDao;

    @Override
    public void saveUserMsg(UserMessage message) {
        this.userMessageDao.saveUserMsg(message);
    }

}

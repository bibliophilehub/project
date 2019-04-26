package com.inext.service.impl;

import com.inext.dao.ISupermarketInfoServiceDao;
import com.inext.entity.SupermarketInfo;
import com.inext.service.ISupermarketInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class SupermarketInfoServiceImpl implements ISupermarketInfoService {

    @Resource
    ISupermarketInfoServiceDao iSupermarketInfoServiceDao;

    @Override
    public List<Map> getShowList() {
        return iSupermarketInfoServiceDao.getShowList();
    }

    @Override
    public List<SupermarketInfo> getList(SupermarketInfo info) {
        return iSupermarketInfoServiceDao.select(info);
    }

    @Override
    public int doAdd(SupermarketInfo info) {
        return iSupermarketInfoServiceDao.insertSelective(info);
    }

    @Override
    public int doChg(SupermarketInfo info) {
        return iSupermarketInfoServiceDao.updateByPrimaryKeySelective(info);
    }

    @Override
    public int doDel(int id) {
        SupermarketInfo info =new SupermarketInfo();
        info.setId(id);
        return iSupermarketInfoServiceDao.delete(info);
    }

    @Override
    public int doclick(int id) {
        return iSupermarketInfoServiceDao.doclick(id);
    }

    @Override
    public SupermarketInfo getDataById(Integer id) {
        SupermarketInfo info =new SupermarketInfo();
        info.setId(id);
        return iSupermarketInfoServiceDao.selectOne(info);
    }
}

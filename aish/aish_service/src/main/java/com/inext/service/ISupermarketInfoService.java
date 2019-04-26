package com.inext.service;

import com.inext.entity.SupermarketInfo;

import java.util.List;
import java.util.Map;

public interface ISupermarketInfoService {
    
    List<Map> getShowList();
    List<SupermarketInfo> getList(SupermarketInfo info);
    int doAdd(SupermarketInfo info);
    int doChg(SupermarketInfo info);
    int doDel(int id);
    int doclick(int id);
    SupermarketInfo getDataById(Integer id);
}

package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.inext.dao.ISysCodeDao;
import com.inext.service.ISysCodeService;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysCodeService implements ISysCodeService {
    Logger logger = LoggerFactory.getLogger(SysCodeService.class);
    @Resource
    ISysCodeDao sysCodeDao;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public void getRbArea(JSONObject json, HttpServletRequest request) {
        json.put("code", "0");
        json.put("message", "查询成功！");
        Object result = redisUtil.get("provinceCity");
        if (result == null) {
                List<Map<String, Object>> provinceCityList = initialArea();
                result = JSONObject.toJSON(provinceCityList);
                redisUtil.set("provinceCity", result);

        }

        json.put("result", result);
    }

    public List<Map<String, Object>> initialArea() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("area_sequence", "1");
        List<Map<String, Object>> provinceList = sysCodeDao.selectArea(map);
        map.put("area_sequence", "2");
        List<Map<String, Object>> cityList = sysCodeDao.selectArea(map);
        map.put("area_sequence", "3");
        List<Map<String, Object>> areaList = sysCodeDao.selectArea(map);

        setChildArea(provinceList, cityList, areaList);
        return provinceList;
    }

    private void setChildArea(List<Map<String, Object>> provinceList, List<Map<String, Object>> cityList, List<Map<String, Object>> areaList) {
        for (int i = 0; i < provinceList.size(); i++) {
            Map<String, Object> parentArea = provinceList.get(i);
            parentArea.put("code", parentArea.get("id"));
            List<Map<String, Object>> ctList = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < cityList.size(); j++) {
                Map<String, Object> childArea = cityList.get(j);
                if (StringUtils.getString(parentArea.get("id")).equals(StringUtils.getString(childArea.get("parentId")))) {
                    childArea.put("code", childArea.get("id"));
                    ctList.add(childArea);
                    List<Map<String, Object>> childAreaList = new ArrayList<Map<String, Object>>();
                    for (int k = 0; k < areaList.size(); k++) {
                        Map<String, Object> area = areaList.get(k);
                        if (StringUtils.getString(childArea.get("id")).equals(StringUtils.getString(area.get("parentId")))) {
                            area.put("code", area.get("id"));
                            childAreaList.add(area);
                        }
                    }
                    childArea.put("childer", childAreaList);
                }
            }
            parentArea.put("childer", ctList);
        }
    }

    private void setChild(List<Map<String, Object>> list) {
        List<Map<String, Object>> preList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if (StringUtils.getInteger(map.get("parentId")) == 0) {
                preList.add(map);
            } else {
                childList.add(map);
            }
        }
        setResultChild(preList, childList);
        list = preList;
    }

    private void setResultChild(List<Map<String, Object>> preList, List<Map<String, Object>> childList) {
        List<Map<String, Object>> cList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < preList.size(); i++) {
            List<Map<String, Object>> pList = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = preList.get(i);
            for (int j = 0; j < childList.size(); j++) {
                Map<String, Object> oldMap = childList.get(j);
                if (StringUtils.getInteger(map.get("id")).equals(StringUtils.getInteger(oldMap.get("parentId")))) {
                    pList.add(oldMap);
                } else {
                    cList.add(oldMap);
                }
            }
            if (pList.size() > 0) {
                map.put("childer", pList);
            }
            if (cList.size() > 0) {
                setResultChild(pList, cList);
            }
        }

    }

}

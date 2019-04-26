package com.inext.service.impl;

import com.inext.dao.IBackConfigParamsDao;
import com.inext.entity.BackConfigParams;
import com.inext.service.IBackConfigParamsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackConfigParamsServiceImpl implements IBackConfigParamsService {
    Logger logger = Logger.getLogger(getClass());
    @Autowired
    IBackConfigParamsDao backConfigParamsDao;

    @Override
    public List<BackConfigParams> findParams(HashMap<String, Object> params) {

        return backConfigParamsDao.findParams(params);
    }

    @Override
    public List<BackConfigParams> findParamsBySyskeys(HashMap<String, Object> params) {
        return backConfigParamsDao.findParamsBySyskeys(params);
    }

    @Override
    public int updateValue(List<BackConfigParams> list, String type) {
        int result = backConfigParamsDao.updateValue(list);
        if (result > 0) {
        }
        return result;
    }

    /**
     * 获取kv信息的config
     *
     * @param sysType
     * @param sysKey
     * @return
     */
    @Override
    public Map<String, String> getBackConfig(String sysType, String sysKey) {
        HashMap<String, Object> paramsback = new HashMap<String, Object>();
        paramsback.put("sysType", sysType);
        paramsback.put("sysKey", sysKey);
        List<BackConfigParams> listback = backConfigParamsDao.findParams(paramsback);
        Map<String, String> returnMap = new HashMap<String, String>();
        for (BackConfigParams backConig : listback) {
            returnMap.put(backConig.getSysKey(), backConig.getSysValue());
        }
        return returnMap;
    }

    @Override
    public BackConfigParams getBackConfigById(Integer id) {
        return backConfigParamsDao.findById(id);
    }

    @Override
    public int updateBackConfigParams(BackConfigParams backConfigParams) {
        int updatedRows = 0;
        Integer id = backConfigParams.getId();
        BackConfigParams backConfigParamsTarget = backConfigParamsDao.findById(id);

        if ("FK_RS_SCORE".equals(backConfigParams.getSysKey())) {
            // -1为未开启人工审核
            String status = backConfigParams.getSysValueView();
            if ("-1".equals(status)) {
                backConfigParamsTarget.setSysValue("-1");
            } else {
                backConfigParamsTarget.setSysValue(backConfigParams.getSysValue());
            }
            backConfigParamsTarget.setSysName(backConfigParams.getSysName());
            updatedRows = backConfigParamsDao.updateByPrimaryKeySelective(backConfigParamsTarget);
            return updatedRows;
        }

        updatedRows = backConfigParamsDao.updateByPrimaryKeySelective(backConfigParams);
        return updatedRows;
    }

}

package com.inext.service.impl;

import com.inext.constants.Constants;
import com.inext.dao.IBackConfigParamsDao;
import com.inext.entity.BackConfigParams;
import com.inext.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库常量加载 Service
 *
 * @author zhangj
 * @date 2017/12/12 0012 15:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InitializingBeanServiceImpl implements InitializingBean {

    @Resource
    IBackConfigParamsDao iBackConfigParamsDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        initBackConfigParamsMap();
    }

    public void initBackConfigParamsMap() {
        List<String> lists = iBackConfigParamsDao.findSysType();
        Map<String, Map<String, String>> map = new HashMap<>();
        for (String str : lists) {
            try {
                List<BackConfigParams> list = iBackConfigParamsDao.findParamsBySysType(str);
                Map<String, String> map1 = new HashMap<>();
                for (BackConfigParams backConfigParams : list) {
                    if (StringUtils.isEmpty(backConfigParams.getSysValue())) {
                        map1.put(backConfigParams.getSysKey(), backConfigParams.getSysValueBig());
                    } else {
                        map1.put(backConfigParams.getSysKey(), backConfigParams.getSysValue());
                    }
                }
                map.put(str, map1);
            } catch (Exception e) {
                System.out.println(str + ":获取失败");
            }
        }
        Constants.BACK_CONFIG_PARAMS_MAP = map;
    }
}

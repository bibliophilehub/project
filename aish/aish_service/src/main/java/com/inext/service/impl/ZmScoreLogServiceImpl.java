package com.inext.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inext.dao.IZmScoreLogDao;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.entity.ZmScoreLog;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiGxbServiceResult;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IZmScoreLogService;
import com.inext.service.IZmService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import com.inext.utils.IdUtil;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.zhima.AESHelper;
import com.inext.view.result.ZhimaResult;
import com.squareup.okhttp.Request.Builder;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("zmScoreLogService")
public class ZmScoreLogServiceImpl implements IZmScoreLogService {

    Logger logger = LoggerFactory.getLogger(ZmScoreLogServiceImpl.class);
    @Resource
    private IZmScoreLogDao zmScoreLogDao;


    @Override
    public List<ZmScoreLog> getZmScoreLog(ZmScoreLog log) {
        return zmScoreLogDao.getZmScoreLog(log);
    }

    @Override
    public int getZmScoreLogCount(ZmScoreLog log) {
        return zmScoreLogDao.getZmScoreLogCount(log);
    }

    @Override
    public void updateZmScoreLog(ZmScoreLog log) {
        zmScoreLogDao.updateZmScoreLog(log);
    }

    @Override
    public void saveZmScoreLog(ZmScoreLog log) {
        zmScoreLogDao.saveZmScoreLog(log);
    }
}

package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IAdviseDao;
import com.inext.dao.IBorrowUserDao;
import com.inext.entity.BorrowUser;
import com.inext.entity.PlatfromAdvise;
import com.inext.exception.CheckException;
import com.inext.service.IAdviseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("adviseService")
public class AdviseServiceImpl implements IAdviseService {

    @Resource
    IAdviseDao iAdviseDao;
    @Resource
    IBorrowUserDao userDao;

    @Override
    public List<PlatfromAdvise> getList(Map<String, Object> param) {
        return iAdviseDao.getList(param);
    }


    @Override
    public PageInfo<PlatfromAdvise> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<PlatfromAdvise> list = this.iAdviseDao.getList(params);


        PageInfo<PlatfromAdvise> pageInfo = new PageInfo<PlatfromAdvise>(list);
        return pageInfo;
    }


    @Override
    public void savePlatfromAdvise(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkQueryBorrowUserInfo(bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            String adviseContent = request.getParameter("adviseContent");
            PlatfromAdvise pa = new PlatfromAdvise();
            // emoji表情重新进行编码已便于保存至数据库
            pa.setAdviseContent(adviseContent);
            pa.setUserPhone(buu.getUserPhone());
            iAdviseDao.savePlatfromAdvise(pa);
            json.put("code", "0");
            json.put("message", "提交成功\n您的意见我们已经收到");
            Map<String, Object> resultmap = new HashMap<String, Object>();
            json.put("result", JSONObject.toJSON(resultmap));
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }


    }

    private void checkQueryBorrowUserInfo(BorrowUser bu) throws CheckException {
        if (bu == null) {
            throw new CheckException("登录信息已超时，请重新登录！");
        }
    }
}

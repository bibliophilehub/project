package com.inext.service.impl;

import com.inext.dao.IRiskCreditUserDao;
import com.inext.entity.RiskCreditUser;
import com.inext.service.IRiskCreditUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskCreditUserServiceImpl implements IRiskCreditUserService {

    @Resource
    IRiskCreditUserDao iRiskCreditUserDao;

    @Override
    public int updateByPrimaryKeySelective(RiskCreditUser user) {
        return iRiskCreditUserDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public RiskCreditUser getEntityById(Integer serialId) {
        RiskCreditUser user=new RiskCreditUser(serialId);
        List<RiskCreditUser> list=iRiskCreditUserDao.select(user);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public RiskCreditUser getNewestByUserId(String userId) {
        return iRiskCreditUserDao.getNewestByUserId(userId);
    }

    @Override
    public RiskCreditUser getByAssetId(String assetId){
        return iRiskCreditUserDao.getByAssetId(assetId);
    }

    @Override
    public List<RiskCreditUser> getNewestByUserIds(List<String> userIds) {
        StringBuffer strIds = new StringBuffer();
        if(CollectionUtils.isEmpty(userIds)){
            return new ArrayList<>();
        }
        for( String userId : userIds ){
            strIds.append(userId).append(",");
        }
        strIds.setLength( strIds.length() - 1 );
        return iRiskCreditUserDao.getNewestByUserIds(strIds.toString());
    }
}

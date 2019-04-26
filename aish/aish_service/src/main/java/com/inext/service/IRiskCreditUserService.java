package com.inext.service;

import com.inext.entity.RiskCreditUser;

import java.util.List;

public interface IRiskCreditUserService {


    int updateByPrimaryKeySelective(RiskCreditUser user);

    RiskCreditUser getEntityById(Integer serialId);

    RiskCreditUser getNewestByUserId(String userId);

    RiskCreditUser getByAssetId(String assetId);

    List<RiskCreditUser> getNewestByUserIds(List<String> userIds);
}

package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.BannerInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IBannerInfoDao extends BaseDao<BannerInfo> {

    BannerInfo getBannerById(Integer id);

    List<BannerInfo> getBannerList(Map<String, Object> param);

    int saveBanner(BannerInfo bannerInfo);

    int updateBanner(BannerInfo bannerInfo);


}

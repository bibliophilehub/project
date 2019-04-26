package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.BannerInfo;

import java.util.List;
import java.util.Map;

public interface IBannerInfoService {

    BannerInfo getBannerById(Integer id);

    List<BannerInfo> getBannerList(Map<String, Object> param);

    int saveBanner(BannerInfo bannerInfo);

    int updateBanner(BannerInfo bannerInfo);

    PageInfo<BannerInfo> getPageList(Map<String, Object> params);

    List<AssetBorrowOrder> queryBorrowOrderList();
}

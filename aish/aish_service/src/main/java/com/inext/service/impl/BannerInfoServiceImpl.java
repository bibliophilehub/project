package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IAssetBorrowOrderDao;
import com.inext.dao.IBannerInfoDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.BannerInfo;
import com.inext.service.IBannerInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("bannerInfoService")
public class BannerInfoServiceImpl implements IBannerInfoService {

    @Resource
    IBannerInfoDao iBannerInfoDao;
    @Resource
    IAssetBorrowOrderDao assetBorrowOrderDao;

    @Override
    public BannerInfo getBannerById(Integer id) {
        return iBannerInfoDao.getBannerById(id);
    }

    @Override
    public List<BannerInfo> getBannerList(Map<String, Object> param) {
        return iBannerInfoDao.getBannerList(param);
    }

    @Override
    public int saveBanner(BannerInfo bannerInfo) {
        return iBannerInfoDao.saveBanner(bannerInfo);
    }

    @Override
    public int updateBanner(BannerInfo bannerInfo) {
        return iBannerInfoDao.updateBanner(bannerInfo);
    }

    @Override
    public PageInfo<BannerInfo> getPageList(Map<String, Object> params) {
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
        List<BannerInfo> list = this.iBannerInfoDao.getBannerList(params);
        PageInfo<BannerInfo> pageInfo = new PageInfo<BannerInfo>(list);
        return pageInfo;
    }

    @Override
    public List<AssetBorrowOrder> queryBorrowOrderList() {
        return assetBorrowOrderDao.queryBorrowOrderList();
    }

}

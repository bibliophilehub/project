package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IAssetRenewalOrderDao;
import com.inext.entity.AssetRenewalOrder;
import com.inext.service.IAssetRenewalOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 续期订单
 * 开发人员：wenkk 创建时间：2018-03-21 18:47
 */

@Service
@Transactional
public class AssetRenewalOrderServiceImpl implements IAssetRenewalOrderService {

    @Resource
    IAssetRenewalOrderDao iAssetRenewalOrderDao;

    @Override
    public PageInfo<AssetRenewalOrder> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage;
            int pageSize = Constants.INITIAL_PAGE_SIZE;

            if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = Constants.INITIAL_CURRENT_PAGE;
            } else {
                currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.containsKey(Constants.PAGE_SIZE)) {
                pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        PageInfo<AssetRenewalOrder> pageInfo = new PageInfo<AssetRenewalOrder>(this.iAssetRenewalOrderDao.getPageList(params));
        return pageInfo;
    }

    @Override
    public List<Map<String, String>> getList(Map<String, Object> params) {
        return this.iAssetRenewalOrderDao.getList(params);
    }


    @Override
    public int insertRenewalOrder(AssetRenewalOrder assetRenewalOrder) {

         iAssetRenewalOrderDao.insertUseGeneratedKeys(assetRenewalOrder);
       return assetRenewalOrder.getId();
    }

    /**
     * 查询续期次数
     * @param params
     * @return
     */
    @Override
    public int getCount(Map<String, Object> params) {
        return iAssetRenewalOrderDao.getCount(params);
    }


}

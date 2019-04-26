package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IAbnormalOrderDao;
import com.inext.dao.IAssetRepaymentOrderDao;
import com.inext.entity.AbnormalOrder;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.service.IAbonormalOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：wenkk 创建时间：2018-03-21 18:47
 */

@Service
@Transactional
public class AbonormalOrderServiceImpl extends BaseSerivceImpl<AssetRepaymentOrder> implements IAbonormalOrderService {

    @Resource
    IAssetRepaymentOrderDao iAssetRepaymentOrderDao;
    @Resource
    IAbnormalOrderDao ibnormalOrderDao;


    @Override
    public PageInfo<AssetRepaymentOrder> getPageList(Map<String, Object> params) {
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
        List<AssetRepaymentOrder> list = this.iAssetRepaymentOrderDao.getAbnormalPageList(params);
        for (AssetRepaymentOrder item : list) {
            AbnormalOrder abnormalOrder = getAbnormalOrder(item.getNoOrder());
            if (abnormalOrder != null) {
                item.setType(1);
            } else {
                item.setType(0);
            }
        }
        PageInfo<AssetRepaymentOrder> pageInfo = new PageInfo<AssetRepaymentOrder>(list);
        return pageInfo;
    }

    @Override
    public AbnormalOrder getAbnormalOrder(String noOrder) {
        AbnormalOrder abnormalOrder = ibnormalOrderDao.getAbnormalOrder(noOrder);
        return abnormalOrder;
    }


    @Override
    public void saveAbnormal(AbnormalOrder abnormalOrder) {
        ibnormalOrderDao.insert(abnormalOrder);
    }

    @Override
    public List<Map<String, String>> getAbnormalList(Map<String, Object> params) {
        List<Map<String, String>> list = iAssetRepaymentOrderDao.getAbnormalList(params);
        return list;
    }
}


package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.inext.configuration.BaseDao;
import com.inext.dao.AssetRepaymentOrderDaikouDao;
import com.inext.dto.AssetRepaymentOrderDaikouDto;
import com.inext.entity.AssetRepaymentOrderDaikou;
import com.inext.service.RepaymentOrderDaikouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RepaymentOrderDaikouServiceImpl extends BaseSerivceImpl<AssetRepaymentOrderDaikou> implements RepaymentOrderDaikouService
{

    @Resource
    AssetRepaymentOrderDaikouDao repaymentOrderDaikouDao;

    @Autowired
    @Override
    public void setBaseDao(BaseDao<AssetRepaymentOrderDaikou> repaymentOrderDaikouDao)
    {
        super.setBaseDao(repaymentOrderDaikouDao);
    }

    @Override
    public List<AssetRepaymentOrderDaikou> getQueryDKList(AssetRepaymentOrderDaikouDto dto)
    {
        int pageNum = dto == null || dto.getPageNum() == null ? 1 : dto.getPageNum();
        int pageSize = dto == null || dto.getPageSize() == null ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<AssetRepaymentOrderDaikou> list = repaymentOrderDaikouDao.getQueryDKList(dto);
        return list;
    }

    @Override
    public int countTotalRepaymentDKNum(AssetRepaymentOrderDaikouDto dto)
    {
        return repaymentOrderDaikouDao.countTotalRepaymentDKNum(dto);
    }

    @Override
    public int countDayRepaymentDKNum(AssetRepaymentOrderDaikouDto dto)
    {
        return repaymentOrderDaikouDao.countDayRepaymentDKNum(dto);
    }

    @Override
    public int calculationDKQueryNum(AssetRepaymentOrderDaikou dto)
    {
        return repaymentOrderDaikouDao.calculationDKQueryNum(dto);
    }
}

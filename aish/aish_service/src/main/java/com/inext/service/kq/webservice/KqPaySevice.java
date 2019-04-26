package com.inext.service.kq.webservice;

import com.inext.entity.AssetRepaymentOrderWithhold;


public interface KqPaySevice {

    void advDebitBatch();

    void pkiQuery(AssetRepaymentOrderWithhold withhold);

    void pkiBatchQuery();
}

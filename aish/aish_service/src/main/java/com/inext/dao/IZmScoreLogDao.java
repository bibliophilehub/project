package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.*;
import java.util.List;

public interface IZmScoreLogDao extends BaseDao<ZmScoreLog> {


    void saveZmScoreLog(ZmScoreLog log);

    void updateZmScoreLog(ZmScoreLog log);

    List<ZmScoreLog> getZmScoreLog(ZmScoreLog log);

    public int getZmScoreLogCount(ZmScoreLog log);

}

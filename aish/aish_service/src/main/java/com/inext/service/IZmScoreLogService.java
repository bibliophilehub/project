package com.inext.service;

import java.util.List;

import com.inext.entity.ZmScoreLog;

public interface IZmScoreLogService {

    public List<ZmScoreLog> getZmScoreLog(ZmScoreLog log);

    public int getZmScoreLogCount(ZmScoreLog log);

    public void updateZmScoreLog( ZmScoreLog log);

    public void saveZmScoreLog( ZmScoreLog log);



}

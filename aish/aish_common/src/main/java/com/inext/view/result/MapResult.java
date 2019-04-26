package com.inext.view.result;


import com.google.common.collect.Lists;
import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * @author zhangj
 */
@ApiModel(value = "数组数据")
public class MapResult extends BaseResult {

    @ApiModelProperty(value = "数据")
    private Map Map;

    public MapResult() {
    }

    public MapResult(Map map) {
        Map = map;
    }

    public java.util.Map getMap() {
        return Map;
    }

    public void setMap(java.util.Map map) {
        Map = map;
    }
}

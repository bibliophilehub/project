package com.inext.view.result;


import com.google.common.collect.Lists;
import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author lisige
 */
@ApiModel(value = "数组数据")
public class ListResult<T extends BaseResult> extends BaseResult {

    @ApiModelProperty(value = "数据")
    private List<T> list = Lists.newArrayList();

    public ListResult() {
    }

    public ListResult(List<T> list) {
        this.list = list;
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

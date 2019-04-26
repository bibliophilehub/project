package com.inext.view.params;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
public class PagingParams {

    @ApiModelProperty(value = "每页数据条数", required = true, example = "10")
    private Integer pageSize = 0;

    @ApiModelProperty(value = "当前第N页", required = true, example = "1")
    private Integer pageNum = 0;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

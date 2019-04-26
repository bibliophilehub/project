package com.inext.view.result;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author lisige
 */
@ApiModel(value = "分页数据携带类")
public class PagingResult<T extends BaseResult> extends BaseResult {

    @ApiModelProperty(value = "当前页数据条数", example = "10")
    private Integer pageSize = 0;

    @ApiModelProperty(value = "当前第N页", example = "1")
    private Integer pageNum = 0;

    @ApiModelProperty(value = "总页数", example = "13")
    private Integer totalNum = 0;

    @ApiModelProperty(value = "数据")
    private List<T> list = Lists.newArrayList();

    public PagingResult() {
    }

    public PagingResult(Integer pageSize, Integer pageNum, Integer totalNum, List<T> list) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalNum = totalNum;
        this.list = list;
    }

    public PagingResult(PageInfo pageInfo) {
        this.pageSize = pageInfo.getPageSize();
        this.pageNum = pageInfo.getPageNum();
        this.totalNum = Integer.parseInt(pageInfo.getTotal() + "");
        this.list = pageInfo.getList();
    }

    public PagingResult(PageInfo pageInfo, List<T> list) {
        this.pageSize = pageInfo.getPageSize();
        this.pageNum = pageInfo.getPageNum();
        this.totalNum = Integer.parseInt(pageInfo.getTotal() + "");
        this.list = list;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

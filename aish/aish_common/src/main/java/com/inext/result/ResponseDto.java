package com.inext.result;

import com.google.gson.annotations.Expose;
import com.inext.enumerate.AppStatus;
import com.inext.enumerate.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * import java.util.HashMap;
 * import java.util.List;
 * import java.util.Map;
 * <p>
 * import com.google.gson.annotations.Expose;
 * <p>
 * <p>
 * /**
 * 定义返回json数据实体
 */
@ApiModel
public class ResponseDto {

    /**
     * 集合key
     */
    private final String LIST_KEY = "list";

    /**
     * 分页key
     */
    private final String PAGE_KEY = "page";

    /**
     * 返回类型
     */
    private Map<String, Object> params = new HashMap<String, Object>();

    /**
     * 返回CODE
     */
    @Expose
    @ApiModelProperty(value = "状态码,表示该次返回值是否成功", required = true, dataType = "String", position = 0, example = "200")
    private String code;

    /**
     * 返回提示信息
     */
    @Expose
    @ApiModelProperty(value = "请求操作的结果描述", required = true, dataType = "String", position = 1, example = "操作成功")
    private String message = "成功";

    /**
     * 返回结果信息
     */
    @Expose
    @ApiModelProperty(value = "返回的对象", required = true, dataType = "String", position = 2, example = "{'name':'2'}")
    private Object result;

    public ResponseDto() {

    }

    public ResponseDto(ServiceResult sr) {
        this.code = Status.SUCCESS.getName().equals(sr.getCode()) ? AppStatus.SUCCESS.getCode() : sr.getCode();
        this.message = sr.getMsg();
        this.result = sr.getData();
    }

    public ResponseDto(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    /**
     * 添加list集合
     *
     * @param list
     */
    public void addLists(List<Object> list) {
        params.put(LIST_KEY, list);

        this.setResult(params);
    }

    /**
     * 添加page信息
     *
     * @param currentPage
     * @param pageSize
     * @param totalPage
     */
    public void addPage(String currentPage, String pageSize, String totalPage) {
        Map<String, String> page = new HashMap<String, String>();
        page.put("currentPage", currentPage);
        page.put("pageSize", pageSize);
        page.put("totalPage", totalPage);

        params.put(PAGE_KEY, page);

        this.setResult(params);
    }

    /**
     * 添加其他信息
     */
    public void addKeyValue(String key, Object value) {
        params.put(key, value);

        this.setResult(params);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


}

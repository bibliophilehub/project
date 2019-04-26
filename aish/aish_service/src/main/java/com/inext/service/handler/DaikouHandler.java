
package com.inext.service.handler;

import com.inext.result.ApiServiceResult;
import com.inext.service.handler.invocation.DKParam;
import com.inext.service.handler.items.BasicPayItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DaikouHandler implements BasePaymentHandler
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DaikouHandler.class);

    private Map<String, BasicPayItem> items = new HashMap<String, BasicPayItem>();

    /**
     * @param items
     */
    @Autowired
    public void setMessageItem(List<BasicPayItem> items)
    {
        for (BasicPayItem item : items)
        {
            String messageTopic = item.getMessageTopic();
            this.items.put(messageTopic.toString(), item);
        }
    }

    @Override
    public ApiServiceResult<Object> execute(DKParam request) throws Exception
    {

        String topic = request.getTopic();

        //Get the message processing item according topic

        BasicPayItem item = items.get(topic);

        if (item == null)
        {
            LOGGER.info("根据topic" + topic + "找不到对象");

        }

        return item.excute(request);
    }

    /**
     * 
     * TODO 增加功能描述
     * @author wxy
     * @date 2018年7月12日
     * @param request
     * @return ApiServiceResult code 为：
     * @throws Exception
     */
    @Override
    public ApiServiceResult<Object> query(DKParam request) throws Exception
    {
        String topic = request.getTopic();

        //Get the message processing item according topic

        BasicPayItem item = items.get(topic);

        if (item == null)
        {
            LOGGER.info("根据topic" + topic + "找不到对象");

        }

        return item.query(request);
    }
}

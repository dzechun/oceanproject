package com.fantechs.provider.wms.in.config;

import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInAsnOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/8/16
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Resource
    private WmsInAsnOrderMapper wmsInAsnOrderMapper;
    @Resource
    private RedisUtil redisUtil;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    private final static String  MAYIKT_REDIS_KEY="pallet_id";
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        if(expiredKey.startsWith(MAYIKT_REDIS_KEY)){
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.add(Calendar.DATE, -1);
            Date lastDate = ca.getTime(); //结果
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String last = sf.format(lastDate);
            Example example = new Example(WmsInAsnOrder.class);
            example.createCriteria().andEqualTo("remark",last);
            WmsInAsnOrder wmsInAsnOrder = wmsInAsnOrderMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(wmsInAsnOrder)){
                redisUtil.set("lastAsnId",wmsInAsnOrder);
            }
        }
    }
}

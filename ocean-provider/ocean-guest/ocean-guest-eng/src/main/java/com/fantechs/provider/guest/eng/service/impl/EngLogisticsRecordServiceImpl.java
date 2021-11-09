package com.fantechs.provider.guest.eng.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.eng.EngLogisticsRecord;
import com.fantechs.common.base.general.entity.eng.EngLogisticsRecordMessage;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.config.RabbitConfig;
import com.fantechs.provider.guest.eng.mapper.EngLogisticsRecordMapper;
import com.fantechs.provider.guest.eng.service.EngLogisticsRecordService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/03.
 */
@Service
public class EngLogisticsRecordServiceImpl extends BaseService<EngLogisticsRecord> implements EngLogisticsRecordService {

    @Resource
    private EngLogisticsRecordMapper engLogisticsRecordMapper;
    @Resource
    private AmqpTemplate rabbitTemplate;
    @Resource
    private RabbitAdmin rabbitAdmin;

    @Override
    public int getUnReadCount() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(EngLogisticsRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("receiveUserId",user.getUserId())
                .andEqualTo("readStatus",0);
        return engLogisticsRecordMapper.selectCountByExample(example);
    }

    @Override
    public List<EngLogisticsRecord> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        map.put("receiveUserId",user.getUserId());
        return engLogisticsRecordMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<EngLogisticsRecord> list) {
        int i = 0;
        for (EngLogisticsRecord engLogisticsRecord : list){
            i += save(engLogisticsRecord);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EngLogisticsRecord record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setMessageContent(record.getMessage() == null ? null : jointMessageContent(record.getMessage()));
        record.setReadStatus(StringUtils.isEmpty(record.getReadStatus()) ? 0 : record.getReadStatus());
        record.setStatus(StringUtils.isEmpty(record.getStatus()) ? 1 : record.getStatus());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());

        int i = engLogisticsRecordMapper.insertSelective(record);

        //发消息
        sendMessage(record.getReceiveUserId());

        return i;
    }

    public void sendMessage(Long receiveUserId){
        String queueName = "QUEUE_M" + receiveUserId;
        try {
            Queue queue = new Queue(queueName, true);
            TopicExchange topicExchange = new TopicExchange(RabbitConfig.TOPIC_EXCHANGE_MESSAGE);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(queueName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AMQP.Queue.DeclareOk declareOk = rabbitAdmin.getRabbitTemplate().execute(
                new ChannelCallback<AMQP.Queue.DeclareOk>() {
                    @Override
                    public AMQP.Queue.DeclareOk doInRabbit(Channel channel)
                            throws Exception {
                        return channel.queueDeclarePassive(queueName);
                    }
                });
        if (declareOk.getMessageCount() == 0) {
            String json = JSONObject.toJSONString(getUnReadCount());
            byte[] bytes = json.getBytes();
            this.rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE_MESSAGE, queueName, bytes);
        }
    }

    //拼接消息内容
    public String jointMessageContent(EngLogisticsRecordMessage engLogisticsRecordMessage){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("材料编码：").append(engLogisticsRecordMessage.getMaterialCode())
                .append("，材料名称：").append(engLogisticsRecordMessage.getMaterialName())
                .append("，合同号：").append(engLogisticsRecordMessage.getContractCode())
                .append("，装置号：").append(engLogisticsRecordMessage.getDeviceCode())
                .append("，主项号：").append(engLogisticsRecordMessage.getDominantTermCode())
                .append("，位号：").append(engLogisticsRecordMessage.getLocationNum())
                .append("，单位：").append(engLogisticsRecordMessage.getMainUnit())
                .append("，规格：").append(engLogisticsRecordMessage.getMaterialDesc())
                .append("，相关单号：").append(engLogisticsRecordMessage.getRelatedOrderCode())
                .append("，时间：").append(engLogisticsRecordMessage.getChangeTime())
                .append("，数量：").append(engLogisticsRecordMessage.getQty())
                .append("，操作人：").append(engLogisticsRecordMessage.getOperateUser());
        return stringBuilder.toString();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EngLogisticsRecord entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        return engLogisticsRecordMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EngLogisticsRecord> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

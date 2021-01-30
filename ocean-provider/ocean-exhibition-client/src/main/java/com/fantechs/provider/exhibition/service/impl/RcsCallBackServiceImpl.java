package com.fantechs.provider.exhibition.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.agv.dto.MaterialAndPositionCodeEnum;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.general.dto.mes.pm.SmtStockDetDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtStockDet;
import com.fantechs.common.base.general.entity.mes.pm.SmtStockDet;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.exhibition.config.RabbitConfig;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import com.fantechs.provider.exhibition.service.RcsCallBackService;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RcsCallBackServiceImpl implements RcsCallBackService {

    private static final Logger log = LoggerFactory.getLogger(RcsCallBackServiceImpl.class);

    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private PMFeignApi pmFeignApi;

    @Autowired
    private AgvFeignApi agvFeignApi;

    @Autowired
    private ExhibitionClientService exhibitionClientService;

    @Override
    public String agvCallback(AgvCallBackDTO agvCallBackDTO) throws Exception {

        if (StringUtils.isEmpty(agvCallBackDTO.getTaskCode())) {

            throw new Exception("通知MES失败，agv任务单号为空！");
        }
        if (agvCallBackDTO.getRobotCode().equals("-1")) {
            throw new Exception("没有找到对应的AGV编号：" + agvCallBackDTO.getRobotCode());
        }
        if ("1".equals(agvCallBackDTO.getMethod())) {
            // 发送消息到客户端，控制伸缩臂进行伸缩取料
            MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
            mQResponseEntity.setCode(1010);
            Map<String,Object> map = new HashMap<>();
            mQResponseEntity.setData(map);
            fanoutSender.send(RabbitConfig.TOPIC_PROCESS_LIST_QUEUE, JSONObject.toJSONString(mQResponseEntity));
            log.info("发送消息到客户端，控制伸缩臂进行伸缩取料：" + JSONObject.toJSONString(mQResponseEntity));
        } else {
            SearchSmtStockDet searchSmtStockDet = new SearchSmtStockDet();
            searchSmtStockDet.setRemark(agvCallBackDTO.getTaskCode());
            List<SmtStockDetDto> smtStockDetDtoList = pmFeignApi.findSmtStockDetList(searchSmtStockDet).getData();
            if (StringUtils.isEmpty(smtStockDetDtoList)) {
                throw new Exception("没有找到agv任务单号：" + agvCallBackDTO.getTaskCode() + " 对应的配送物料");
            }
            SmtStockDet smtStockDet = smtStockDetDtoList.get(0);
            smtStockDet.setStatus((byte) 2);
            pmFeignApi.updateSmtStockDet(smtStockDet);

            // 查询剩下未配送的物料，进行配送
            String taskCode = exhibitionClientService.agvStockTask(smtStockDet.getStockId());

            if (StringUtils.isEmpty(taskCode)) {

                // 发送消息到客户端，物料配送完成
                MQResponseEntity mQResponseEntity = new MQResponseEntity<>();
                mQResponseEntity.setCode(1013);
                Map<String,Object> mapMQ = new HashMap<>();
                mQResponseEntity.setData(mapMQ);
                fanoutSender.send(RabbitConfig.TOPIC_PROCESS_LIST_QUEUE, JSONObject.toJSONString(mQResponseEntity));
                log.info("发送消息到客户端，物料配送完成：" + JSONObject.toJSONString(mQResponseEntity));

                // 配送小车成品完成
                String positionCode = "";
                for(MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()){
                    if("2249".equals(materialAndPositionCode.getMaterialCode())) {
                        positionCode = materialAndPositionCode.getEndPositionCode();
                    }
                }

                // agv回到暂驻区
                Map<String, Object> map = new HashMap<>();
                map.put("taskTyp", "c01");
                List<PositionCodePath> positionCodePathList = new LinkedList<>();
                // 目标位置条码
                PositionCodePath positionCodePath = new PositionCodePath();
                positionCodePath.setPositionCode(positionCode);
                positionCodePath.setType("00");
                positionCodePathList.add(positionCodePath);
                map.put("positionCodePath", positionCodePathList);
                String result = agvFeignApi.genAgvSchedulingTask(map).getData();
                RcsResponseDTO rcsResponseDTO = BeanUtils.convertJson(result, new TypeToken<RcsResponseDTO>(){}.getType());
                log.info("启动AGV配送任务：请求参数：" + JSONObject.toJSONString(map) + "， 返回结果：" + JSONObject.toJSONString(rcsResponseDTO));
            }
        }

        return "0";
    }
}

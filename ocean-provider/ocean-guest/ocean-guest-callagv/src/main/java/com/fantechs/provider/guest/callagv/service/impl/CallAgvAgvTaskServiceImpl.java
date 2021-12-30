package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvDistributionDto;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTaskBarcode;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvAgvTaskService;
import com.fantechs.provider.guest.callagv.service.RcsCallBackService;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CallAgvAgvTaskServiceImpl extends BaseService<CallAgvAgvTask> implements CallAgvAgvTaskService {

    @Resource
    private CallAgvAgvTaskMapper callAgvAgvTaskMapper;

    @Resource
    private RcsCallBackService rcsCallBackService;

    @Resource
    private TemVehicleFeignApi temVehicleFeignApi;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public int update(CallAgvAgvTask callAgvAgvTask) {
        // 手动结束任务，模拟RCS发送完整信号
        CallAgvAgvTask callAgvAgvTaskOld = callAgvAgvTaskMapper.selectByPrimaryKey(callAgvAgvTask.getAgvTaskId());
        TemVehicle temVehicle = temVehicleFeignApi.detail(callAgvAgvTask.getVehicleId()).getData();
        // 任务未开始取消，清除redis旧有的关联数据
        if (callAgvAgvTaskOld.getTaskStatus() == 1) {
            BaseStorageTaskPoint baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(callAgvAgvTaskOld.getStartStorageTaskPointId()).getData();
            if (StringUtils.isNotEmpty(redisUtil.get(baseStorageTaskPoint.getType()))) {
                try {
                    List<CallAgvDistributionDto> callAgvDistributionDtoList = BeanUtils.convertJson(redisUtil.get(baseStorageTaskPoint.getType()).toString(), new TypeToken<List<CallAgvDistributionDto>>() {}.getType());
                    for (CallAgvDistributionDto callAgvDistributionDto : callAgvDistributionDtoList) {
                        if (callAgvAgvTaskOld.getAgvTaskId() == callAgvDistributionDto.getAgvTaskId()) {
                            callAgvDistributionDtoList.remove(callAgvDistributionDto);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new BizErrorException(e.getMessage());
                }
            }
        }
        if (callAgvAgvTask.getTaskStatus() == 3 || callAgvAgvTask.getTaskStatus() == 5) {
            try {
                AgvCallBackDTO agvCallBackDTO = new AgvCallBackDTO();
                agvCallBackDTO.setTaskCode(callAgvAgvTask.getTaskCode());
                agvCallBackDTO.setPodCode(temVehicle.getVehicleCode());
                agvCallBackDTO.setMethod("3");
                rcsCallBackService.agvCallback(agvCallBackDTO);
                agvCallBackDTO.setMethod("4");
                rcsCallBackService.agvCallback(agvCallBackDTO);
                agvCallBackDTO.setMethod("2");
                rcsCallBackService.agvCallback(agvCallBackDTO);
            } catch (Exception e) {

            }
        }

        return callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
    }

    @Override
    public List<CallAgvAgvTaskDto> findList(Map<String, Object> map) {
        /*SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());*/
        if(StringUtils.isNotEmpty(map.get("ifToday"))&&map.get("ifToday").toString().equals("1")){
            String dateString = DateUtils.getDateString(new Date(), "yyyy-MM-dd");
            map.put("startTime",dateString);
            map.put("endTime",dateString);
        }
        List<CallAgvAgvTaskDto> list = callAgvAgvTaskMapper.findList(map);

        for (CallAgvAgvTaskDto callAgvAgvTaskDto : list) {
            List<CallAgvAgvTaskBarcode> barcodeList = callAgvAgvTaskDto.getBarcodeList();
            if (StringUtils.isNotEmpty(barcodeList)) {
                callAgvAgvTaskDto.setProductModel(barcodeList.get(0).getProductModel());
                BigDecimal totalQty = barcodeList.stream()
                        .filter(e -> barcodeList.get(0).getProductModel().equals(e.getProductModel()))
                        .map(CallAgvAgvTaskBarcode::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                callAgvAgvTaskDto.setTotalQty(totalQty);
            }
        }

        if (StringUtils.isNotEmpty(map.get("productModel"))) {
            String productModel = map.get("productModel").toString();
            list = list.stream().filter(e -> e.getProductModel() != null && e.getProductModel().contains(productModel)).collect(Collectors.toList());
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<CallAgvAgvTask> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行数", fail);
        return resultMap;
    }
}

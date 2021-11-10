package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleReBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleReBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvAgvTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CallAgvAgvTaskServiceImpl extends BaseService<CallAgvAgvTask> implements CallAgvAgvTaskService {

    @Resource
    private CallAgvAgvTaskMapper callAgvAgvTaskMapper;
    @Resource
    private CallAgvVehicleReBarcodeMapper callAgvVehicleReBarcodeMapper;

    @Override
    public List<CallAgvAgvTaskDto> findList(Map<String, Object> map) {
        /*SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());*/
        List<CallAgvAgvTaskDto> list = callAgvAgvTaskMapper.findList(map);

        for (CallAgvAgvTaskDto callAgvAgvTaskDto : list){
            Map<String, Object> barcodeMap = new HashMap<>();
            barcodeMap.put("vehicleId", callAgvAgvTaskDto.getVehicleId());
            List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList = callAgvVehicleReBarcodeMapper.findList(barcodeMap);
            if(StringUtils.isNotEmpty(callAgvVehicleReBarcodeDtoList)) {
                callAgvAgvTaskDto.setCallAgvVehicleReBarcodeDtoList(callAgvVehicleReBarcodeDtoList);
                callAgvAgvTaskDto.setProductModel(callAgvVehicleReBarcodeDtoList.get(0).getProductModel());
                BigDecimal totalQty = callAgvVehicleReBarcodeDtoList.stream()
                        .filter(e->callAgvVehicleReBarcodeDtoList.get(0).getProductModel().equals(e.getProductModel()))
                        .map(CallAgvVehicleReBarcodeDto::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                callAgvAgvTaskDto.setTotalQty(totalQty);
            }
        }

        if(StringUtils.isNotEmpty(map.get("productModel"))){
            String productModel = map.get("productModel").toString();
            list = list.stream().filter(e -> e.getProductModel().contains(productModel)).collect(Collectors.toList());
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

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

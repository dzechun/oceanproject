package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcHtReworkOrderMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcReworkOrderMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import com.fantechs.provider.mes.sfc.service.MesSfcReworkOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/06/15.
 */
@Service
public class MesSfcReworkOrderServiceImpl extends BaseService<MesSfcReworkOrder> implements MesSfcReworkOrderService {

    @Resource
    private MesSfcReworkOrderMapper mesSfcReworkOrderMapper;
    @Resource
    private MesSfcHtReworkOrderMapper mesSfcHtReworkOrderMapper;
    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;

    @Override
    public List<MesSfcReworkOrderDto> findList(Map<String, Object> map) {
        return mesSfcReworkOrderMapper.findList(map);
    }

    @Override
    public List<MesSfcHtReworkOrderDto> findHtList(Map<String, Object> map) {
        return mesSfcHtReworkOrderMapper.findList(map);
    }

    @Override
    public GenerateReworkOrderCodeDto generateReworkOrderCode(SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess) {
        GenerateReworkOrderCodeDto generateReworkOrderCodeDto = new GenerateReworkOrderCodeDto();

        // 生成返工单号
        MesSfcReworkOrderDto mesSfcReworkOrderDto = mesSfcReworkOrderMapper.getFirstTodayReworkOrder();
        String serialNum = mesSfcReworkOrderDto.getReworkOrderCode().substring(mesSfcReworkOrderDto.getReworkOrderCode().length() - 4);
        Integer num = Integer.valueOf(serialNum) + 1;
        String reworkOrderCode = "FG" + DateUtil.format(new Date(), "YYYYMMDD") + num;
        generateReworkOrderCodeDto.setReworkOrderCode(reworkOrderCode);

        // 获取所有工单部件清单并集
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        List<String> orderIds = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos){
            if(!orderIds.contains(dto.getWorkOrderId().toString())){
                orderIds.add(dto.getWorkOrderId().toString());
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderIds", orderIds);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
        return null;
    }
}

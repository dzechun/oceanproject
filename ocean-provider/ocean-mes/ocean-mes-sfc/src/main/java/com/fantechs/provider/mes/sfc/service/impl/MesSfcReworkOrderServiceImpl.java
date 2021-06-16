package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcHtReworkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.mapper.MesSfcHtReworkOrderMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcReworkOrderMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import com.fantechs.provider.mes.sfc.service.MesSfcReworkOrderService;
import org.springframework.beans.BeanUtils;
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

        // 所有符合条码
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        List<String> orderIds = new ArrayList<>();
        for (MesSfcBarcodeProcessDto dto : barcodeProcessDtos){
            if(!orderIds.contains(dto.getWorkOrderId().toString())){
                orderIds.add(dto.getWorkOrderId().toString());
            }
        }
        // 获取所有工单的部件清单并集
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderIds", orderIds);
        List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtos = mesSfcKeyPartRelevanceService.findList(map);
        generateReworkOrderCodeDto.setKeyPartRelevanceDtos(keyPartRelevanceDtos);
        return generateReworkOrderCodeDto;
    }

    @Override
    public int save(DoReworkOrderDto doReworkOrderDto) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        // 查找条码
        SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
        searchMesSfcBarcodeProcess.setMaterialId(doReworkOrderDto.getMaterialId());
        searchMesSfcBarcodeProcess.setCartonCode(doReworkOrderDto.getCartonCode());
        searchMesSfcBarcodeProcess.setWorkOrderId(doReworkOrderDto.getWorkOrderId());
        searchMesSfcBarcodeProcess.setBarCode(doReworkOrderDto.getBarCode());
        searchMesSfcBarcodeProcess.setPalletCode(doReworkOrderDto.getPalletCode());
        searchMesSfcBarcodeProcess.setColorBoxCode(doReworkOrderDto.getColorBoxCode());
        List<MesSfcBarcodeProcessDto> barcodeProcessDtos = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        // 保存返工单条码关系表

        // 保存返工单条码关系历史表

        // 重置条码过站表

        // 清除工单产出时间状态

        // 清除包箱

        // 清除彩盒

        // 清除栈板

        // 清除条码部件关系表


        MesSfcReworkOrder mesSfcReworkOrder = new MesSfcReworkOrder();
        mesSfcReworkOrder.setReworkOrderCode(doReworkOrderDto.getReworkOrderCode());
        mesSfcReworkOrder.setCreateTime(new Date());
        mesSfcReworkOrder.setReworkStatus((byte) 1);
        mesSfcReworkOrder.setReworkRouteId(doReworkOrderDto.getRouteId());
        mesSfcReworkOrder.setStatus((byte) 1);
        mesSfcReworkOrder.setReworkStartProcessId(doReworkOrderDto.getProcessId());
        mesSfcReworkOrder.setOrgId(user.getOrganizationId());
        mesSfcReworkOrder.setIsDelete((byte) 1);
        mesSfcReworkOrder.setCreateUserId(user.getUserId());

        // 保存返工单历史表
        MesSfcHtReworkOrder mesSfcHtReworkOrder = new MesSfcHtReworkOrder();
        BeanUtils.copyProperties(mesSfcReworkOrder, mesSfcHtReworkOrder);
        mesSfcHtReworkOrderMapper.insert(mesSfcHtReworkOrder);
        // 保存返工单
        return mesSfcReworkOrderMapper.insert(mesSfcReworkOrder);
    }
}

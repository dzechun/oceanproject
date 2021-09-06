package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintParam;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderPrintMapper;
import com.fantechs.provider.guest.eng.service.EngPackingOrderPrintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
@Service
public class EngPackingOrderPrintServiceImpl implements EngPackingOrderPrintService {
    @Resource
    private EngPackingOrderPrintMapper engPackingOrderPrintMapper;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Override
    public List<EngPackingOrderPrintDto> findList(Map<String, Object> map) {
        return engPackingOrderPrintMapper.findList(map);
    }

    @Override
    public int print(EngPackingOrderPrintParam engPackingOrderPrintParam) {
        if(StringUtils.isEmpty(engPackingOrderPrintParam.getPrintName())){
            throw new BizErrorException("请输入打印机名称");
        }
        PrintDto printDto = new PrintDto();
        printDto.setPrintName(engPackingOrderPrintParam.getPrintName());
        printDto.setLabelName(engPackingOrderPrintParam.getPrintMode());
        printDto.setLabelVersion("0.0.1");
        List<PrintModel> list = new ArrayList<>();
        for (Long id : engPackingOrderPrintParam.getIds()) {
            //获取标签信息
            PrintModel printModel = engPackingOrderPrintMapper.ViewPrint(ControllerUtil.dynamicCondition("type",engPackingOrderPrintParam.getType(),"id",id));
            if(StringUtils.isEmpty(printModel)){
                throw new BizErrorException("数据获取失败");
            }
            printModel.setSize(engPackingOrderPrintParam.getSize());
            list.add(printModel);
        }
        printDto.setPrintModelList(list);
        ResponseEntity responseEntity = sfcFeignApi.QUEUEprint(printDto,"1");
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return 1;
    }
}

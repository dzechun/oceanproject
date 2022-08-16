package com.fantechs.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.dto.PrintModelDto;
import com.fantechs.entity.WmsInnerInventoryModel;
import com.fantechs.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.service.WmsInnerInventoryUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Service
public class WmsInnerInventoryUreportServiceImpl implements WmsInnerInventoryUreportService {

    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private SFCFeignApi sfcFeignApi;

    @Override
    public List<WmsInnerInventoryModel> findList(Map<String, Object> map) {
        return wmsInnerInventoryMapper.findList(map);
    }

    @Override
    public int PrintMaterialCode(List<PrintModelDto> list) {
        for (PrintModelDto printModelDto : list) {
            if(StringUtils.isEmpty(printModelDto.getPrintName())){
                throw new BizErrorException("请输入打印机名称");
            }
            PrintDto printDto = new PrintDto();
            printDto.setPrintName(printModelDto.getPrintName());
            printDto.setLabelName(printModelDto.getPrintMode());
            printDto.setLabelVersion("0.0.1");
            List<PrintModel> printModels = new ArrayList<>();
            PrintModel printModel = new PrintModel();
            printModel.setId(Long.parseLong("1"));
            printModel.setOption1(printModelDto.getMaterialName());
            printModel.setOption2(printModelDto.getDeviceCode());
            printModel.setQrCode(printModelDto.getMaterialCode());
            printModel.setSize(printModelDto.getSize());
            printModels.add(printModel);
            printDto.setPrintModelList(printModels);
            ResponseEntity responseEntity = sfcFeignApi.print(printDto);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
        }
        return 1;
    }
}

package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintCarCodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcCartonPalletReprint;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.mapper.MesSfcCartonPalletReprintMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeReprintService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductCartonService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletService;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MesSfcBarcodeReprintServiceImpl implements MesSfcBarcodeReprintService {

    @Resource
    MesSfcCartonPalletReprintMapper mesSfcCartonPalletReprintMapper;
    @Resource
    MesSfcProductCartonService mesSfcProductCartonService;
    @Resource
    MesSfcProductPalletService mesSfcProductPalletService;
    @Resource
    MesSfcBarcodeProcessService mesSfcBarcodeProcessService;

    @Override
    public List<String> findCode(String keyword, String barocdeType) {
        List<String> barCodeList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("closeStatus", 1);
        if(StringUtils.isEmpty(keyword)){
            throw new BizErrorException(ErrorCodeEnum.PDA40012033);
        }
        if(barocdeType.equals("1")){
            map.put("cartonCode", keyword);
            List<MesSfcProductCartonDto> productCartonDtoList = mesSfcProductCartonService.findList(map);
            for (MesSfcProductCartonDto dto : productCartonDtoList) {
                barCodeList.add(dto.getCartonCode());
            }
        }else if(barocdeType.equals("2")){
            map.put("palletCode", keyword);
            List<MesSfcProductPalletDto> productPalletDtoList = mesSfcProductPalletService.findList(map);
            for (MesSfcProductPalletDto dto : productPalletDtoList) {
                barCodeList.add(dto.getPalletCode());
            }
        }
        if(barCodeList.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003, "该条码不存在");
        }
        return barCodeList;
    }

    @Override
    public int reprintBarcode(String barCode, byte barocdeType, String printName) throws Exception {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String packingQty = "";
        Long sourceBarcodeId = null;
        Long workOrderId = null;
        Map<String, Object> map = new HashMap<>();
        map.put("closeStatus", 1);
        if(barocdeType == 1){
            map.put("cartonCode", barCode);
            List<MesSfcProductCartonDto> productCartonDtoList = mesSfcProductCartonService.findList(map);
            if(productCartonDtoList == null || productCartonDtoList.size() <= 0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "该包箱不存在");
            }
            MesSfcProductCartonDto sfcProductCartonDto = productCartonDtoList.get(0);
            sourceBarcodeId = sfcProductCartonDto.getProductCartonId();
            workOrderId = sfcProductCartonDto.getWorkOrderId();

            List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(SearchMesSfcBarcodeProcess.builder()
                    .cartonCode(barCode)
                    .build());
            packingQty = mesSfcBarcodeProcessList.size() >= sfcProductCartonDto.getNowPackageSpecQty().intValue() ?
                            mesSfcBarcodeProcessList.size()+"" : mesSfcBarcodeProcessList.size()+"尾";
        }else if(barocdeType == 2){
            map.put("palletCode", barCode);
            List<MesSfcProductPalletDto> productPalletDtoList = mesSfcProductPalletService.findList(map);
            if(productPalletDtoList == null || productPalletDtoList.size() <= 0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "该栈板不存在");
            }
            MesSfcProductPalletDto sfcProductPalletDto = productPalletDtoList.get(0);
            sourceBarcodeId = sfcProductPalletDto.getProductPalletId();
            workOrderId = sfcProductPalletDto.getWorkOrderId();
        }

        // 调用打印机打印
        BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                .barcode(barCode)
                .labelTypeCode("09")
                .workOrderId(workOrderId)
                .printName(printName != null ? printName : "测试")
                .packingQty(packingQty)
                .build());

        // 生成补打记录
        MesSfcCartonPalletReprint data = new MesSfcCartonPalletReprint();
        data.setBarcode(barCode);
        data.setBarocdeType(barocdeType);
        data.setCreateTime(new Date());
        data.setIsDelete((byte) 1);
        data.setCreateUserId(user.getUserId());
        data.setSourceBarcodeId(sourceBarcodeId);
        data.setOrgId(user.getOrganizationId());
        return mesSfcCartonPalletReprintMapper.insert(data);
    }
}

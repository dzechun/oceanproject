package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterialPackage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import com.fantechs.provider.mes.sfc.service.MesSfcPalletWorkService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MesSfcPalletWorkServiceImpl implements MesSfcPalletWorkService {

    @Resource
    MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    @Resource
    MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;

    @Resource
    MesSfcProductPalletService mesSfcProductPalletService;

    @Resource
    MesSfcProductCartonService mesSfcProductCartonService;

    @Resource
    MesSfcProductCartonDetService mesSfcProductCartonDetService;

    @Resource
    MesSfcBarcodeProcessService mesSfcBarcodeProcessService;

    @Resource
    PMFeignApi pmFeignApi;

    @Resource
    BaseFeignApi baseFeignApi;

    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {

        String cartonCode = "";
        // 栈板作业需要绑定的所有产品条码
        List<MesSfcWorkOrderBarcode> mesSfcWorkOrderBarcodeList = new ArrayList<>();
        // 获取该条码对应的工单信息
        Long workOrderId = null;
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(requestPalletWorkScanDto.getBarcode());
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            // 不是产品箱码和客户箱码，判断是否为箱码
            Map<String, Object> map = new HashMap<>();
            map.put("cartonCode", requestPalletWorkScanDto.getBarcode());
            List<MesSfcProductCartonDto> mesSfcProductCartonDtoList = mesSfcProductCartonService.findList(map);
            if (mesSfcProductCartonDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012000);
            }
            workOrderId = mesSfcProductCartonDtoList.get(0).getWorkOrderId();
            // 获取箱号绑定产品条码
            cartonCode = requestPalletWorkScanDto.getBarcode();
            Map<String, Object> productCartonDetMap = new HashMap<>();
            productCartonDetMap.put("productCartonId", mesSfcProductCartonDtoList.get(0).getProductCartonId());
            List<MesSfcProductCartonDetDto> mesSfcProductCartonDetDtoList = mesSfcProductCartonDetService.findList(productCartonDetMap);
            for (MesSfcProductCartonDetDto mesSfcProductCartonDetDto : mesSfcProductCartonDetDtoList) {
                MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeService.selectByKey(mesSfcProductCartonDetDto.getWorkOrderBarcodeId());
                mesSfcWorkOrderBarcodeList.add(mesSfcWorkOrderBarcode);
            }
        } else {
            workOrderId = mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId();
            Long workOrderBarcodeId = null;
            // 产品条码
            if (mesSfcWorkOrderBarcodeDtoList.get(0).getBarcodeType() == 2) {
                workOrderBarcodeId = mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId();
                // 客户条码
            } else if (mesSfcWorkOrderBarcodeDtoList.get(0).getBarcodeType() == 3) {
                Map<String, Object> map = new HashMap<>();
                map.put("partBarcode", requestPalletWorkScanDto.getBarcode());
                List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
                if (mesSfcKeyPartRelevanceDtoList.isEmpty() || mesSfcKeyPartRelevanceDtoList.size() > 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
                }
                workOrderBarcodeId = mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId();
                Map<String, Object> productCartonDetMap = new HashMap<>();
                productCartonDetMap.put("workOrderBarcodeId", workOrderBarcodeId);
                List<MesSfcProductCartonDetDto> mesSfcProductCartonDetDtoList = mesSfcProductCartonDetService.findList(productCartonDetMap);
                // 产品条码没有关联箱号
                if (mesSfcProductCartonDetDtoList.isEmpty()) {
                    MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeService.selectByKey(workOrderBarcodeId);
                    mesSfcWorkOrderBarcodeList.add(mesSfcWorkOrderBarcode);
                    // 获取产品条码关联箱号绑定的所有产品条码
                } else {
                    productCartonDetMap.clear();
                    productCartonDetMap.put("productCartonId", mesSfcProductCartonDetDtoList.get(0).getProductCartonId());
                    mesSfcProductCartonDetDtoList = mesSfcProductCartonDetService.findList(productCartonDetMap);
                    for (MesSfcProductCartonDetDto mesSfcProductCartonDetDto : mesSfcProductCartonDetDtoList) {
                        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeService.selectByKey(mesSfcProductCartonDetDto.getWorkOrderBarcodeId());
                        mesSfcWorkOrderBarcodeList.add(mesSfcWorkOrderBarcode);
                    }
                }
            }
        }
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderId(workOrderId);
        List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if (mesPmWorkOrderDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
        }
        MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
        // 获取该工位所有未关闭的栈板
        Map<String, Object> productPalletMap = new HashMap<>();
        productPalletMap.put("stationId", requestPalletWorkScanDto.getStationId());
        productPalletMap.put("closeStatus", 0);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtoList = mesSfcProductPalletService.findList(productPalletMap);
        // 获取当前对应的栈板，判断是否需要新开一个栈板
        String palletCode = "";
        Boolean isPallet = true;
        for (MesSfcProductPalletDto mesSfcProductPalletDto : mesSfcProductPalletDtoList) {
            if (requestPalletWorkScanDto.getPalletType() == 0 && mesPmWorkOrderDto.getWorkOrderId() == mesSfcProductPalletDto.getWorkOrderId()) {
                palletCode = mesSfcProductPalletDto.getPalletCode();
                isPallet = false;
                break;
            }
            if (requestPalletWorkScanDto.getPalletType() == 1 && mesPmWorkOrderDto.getMaterialId() == mesSfcProductPalletDto.getMaterialId()) {
                palletCode = mesSfcProductPalletDto.getPalletCode();
                isPallet = false;
                break;
            }
        }
        // 查找条码生成规则配置，生成新的栈板码
        if (isPallet) {
            if (mesSfcProductPalletDtoList.size() >= requestPalletWorkScanDto.getMaxPalletNum()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前栈板可操作已达上限，该条码不属于正在操作的" + mesSfcProductPalletDtoList.size() + "个栈板，最大栈板操作数量" + requestPalletWorkScanDto.getMaxPalletNum());
            }

            SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
            searchBasePackageSpecification.setMaterialId(mesPmWorkOrderDto.getMaterialId());
            List<BasePackageSpecificationDto> basePackageSpecificationDtoList = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
            if (basePackageSpecificationDtoList.isEmpty() || basePackageSpecificationDtoList.get(0).getBaseMaterialPackages().isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该产品条码没有设置在该工序的包装规格");
            }
            // 获取该条码规则对应的最大条码数
            String maxCode = null;
            // 获取条码规则配置集合
            List<BaseBarcodeRuleSpec> barcodeRuleSpecList = new LinkedList<>();
            for (BaseMaterialPackage baseMaterialPackage : basePackageSpecificationDtoList.get(0).getBaseMaterialPackages()) {
                if (requestPalletWorkScanDto.getProcessId() == baseMaterialPackage.getProcessId()) {
                    boolean hasKey = redisUtil.hasKey(baseMaterialPackage.getBarcodeRuleId().toString());
                    if(hasKey){
                        // 从redis获取上次生成条码
                        maxCode = redisUtil.get(baseMaterialPackage.getBarcodeRuleId().toString()).toString();
                    }

                    SearchBaseBarcodeRuleSpec baseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
                    baseBarcodeRuleSpec.setBarcodeRuleId(baseMaterialPackage.getBarcodeRuleId());
                    barcodeRuleSpecList = baseFeignApi.findSpec(baseBarcodeRuleSpec).getData();
                    if (barcodeRuleSpecList.isEmpty()) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012023);
                    }
                    break;
                }
            }
            // 判断条码规则配置格式是否有[p],[L],[C]
            Map<String, Object> baseBarcodeRuleSpecMap = new HashMap<>();
            for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : barcodeRuleSpecList) {
                if ("[P]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[P]"))) {
                    baseBarcodeRuleSpecMap.put("[P]", mesPmWorkOrderDto.getMaterialCode());
                }
                if ("[L]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[L]"))) {
                    baseBarcodeRuleSpecMap.put("[L]", mesPmWorkOrderDto.getProCode());
                }
                if ("[C]".equals(baseBarcodeRuleSpec.getSpecification()) && StringUtils.isEmpty(baseBarcodeRuleSpecMap.get("[C]"))) {
                    SearchBaseMaterialSupplier searchBaseMaterialSupplier = new SearchBaseMaterialSupplier();
                    searchBaseMaterialSupplier.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                    List<BaseMaterialSupplierDto> baseMaterialSupplierDtoList = baseFeignApi.findBaseMaterialSupplierList(searchBaseMaterialSupplier).getData();
                    if (baseMaterialSupplierDtoList.isEmpty()) {
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到对应的客户料号");
                    }
                    baseBarcodeRuleSpecMap.put("[C]", baseMaterialSupplierDtoList.get(0).getMaterialSupplierCode());
                }
            }
            palletCode = baseFeignApi.newGenerateCode(barcodeRuleSpecList, maxCode, baseBarcodeRuleSpecMap, workOrderId.toString()).getData();
            if (StringUtils.isEmpty(palletCode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012008);
            }
        }
        // 记录过站信息
        for (MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode : mesSfcWorkOrderBarcodeList) {
            // 判断该产品条码是否有过站记录
            SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
            searchMesSfcBarcodeProcess.setBarCode(mesSfcWorkOrderBarcode.getBarcode());
            searchMesSfcBarcodeProcess.setPalletCode(palletCode);
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findBarcode(searchMesSfcBarcodeProcess);
            if (mesSfcBarcodeProcessList.isEmpty()) {

            }
        }

        return palletCode;
    }

    @Override
    public List<PalletWorkScanDto> palletWorkScan() {

        List<PalletWorkScanDto> palletWorkScanDtoList = new LinkedList<>();

        return palletWorkScanDtoList;
    }

    @Override
    public List<String> findPalletCarton(String palletCode) {

        List<String> cartonCodeList = new LinkedList<>();

        return cartonCodeList;
    }

    @Override
    public int submitNoFullPallet(List<String> palletCodeList) throws Exception {

        return palletCodeList.size();
    }

    @Override
    public Boolean updatePalletType(Long stationId) {
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcProductPallet.builder()
                .stationId(stationId)
                .build()));
        if(list.size()>0){
            return false;
        }
        return true;
    }
}

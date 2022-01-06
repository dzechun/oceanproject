package com.fantechs.provider.mes.sfc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.dto.wms.in.BarPODto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class MesSfcPalletWorkServiceImpl implements MesSfcPalletWorkService {

    @Resource
    MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    @Resource
    MesSfcKeyPartRelevanceService mesSfcKeyPartRelevanceService;

    @Resource
    MesSfcProductPalletService mesSfcProductPalletService;

    @Resource
    MesSfcProductPalletDetService mesSfcProductPalletDetService;

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
    @Resource
    InFeignApi inFeignApi;
    @Resource
    SecurityFeignApi securityFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //samePackageCode 同包装编码--扫描的箱号产品条码对应的或者扫描的产品条码对应的PO编码 2020-10-20
        String samePackageCode="";

        //samePackageCodePallet 同包装编码--栈板包装产品条码对应的PO编码 2020-10-20
        String samePackageCodePallet="";

        String cartonCode = "";
        // 栈板作业需要绑定的所有产品条码
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeList = new ArrayList<>();
        Long workOrderId = null;
        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(requestPalletWorkScanDto.getBarcode());
        if (requestPalletWorkScanDto.getBarcode().length() != 23){
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoCheckBarcode");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty()){
                Example example = new Example(MesSfcBarcodeProcess.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike("customerBarcode", requestPalletWorkScanDto.getBarcode() + "%");
                List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
                if (!mesSfcBarcodeProcesses.isEmpty()){
                    searchMesSfcWorkOrderBarcode.setBarcode(mesSfcBarcodeProcesses.get(0).getBarcode());
                }
            }
        }
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            // 不是产品箱码和客户箱码，判断是否为箱码
            Map<String, Object> map = new HashMap<>();
            map.put("cartonCode", requestPalletWorkScanDto.getBarcode());
            List<MesSfcProductCartonDto> mesSfcProductCartonDtoList = mesSfcProductCartonService.findList(map);
            if (mesSfcProductCartonDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012000);
            }
            // 判断对应的包箱是否已关闭
            if (mesSfcProductCartonDtoList.get(0).getCloseStatus() == 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该条码对应的包箱未关闭，请先进行包箱关闭");
            }

            workOrderId = mesSfcProductCartonDtoList.get(0).getWorkOrderId();
            // 获取箱号绑定产品条码
            cartonCode = requestPalletWorkScanDto.getBarcode();
            Map<String, Object> productCartonDetMap = new HashMap<>();
            productCartonDetMap.put("productCartonId", mesSfcProductCartonDtoList.get(0).getProductCartonId());
            List<MesSfcProductCartonDetDto> mesSfcProductCartonDetDtoList = mesSfcProductCartonDetService.findList(productCartonDetMap);
            for (MesSfcProductCartonDetDto mesSfcProductCartonDetDto : mesSfcProductCartonDetDtoList) {
                searchMesSfcWorkOrderBarcode.setBarcode(null);
                searchMesSfcWorkOrderBarcode.setWorkOrderBarcodeId(mesSfcProductCartonDetDto.getWorkOrderBarcodeId());
                List<MesSfcWorkOrderBarcodeDto> barcodeServiceList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                if (barcodeServiceList.isEmpty()){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该包箱关联产品条码不存在");
                }
                mesSfcWorkOrderBarcodeList.add(barcodeServiceList.get(0));
            }

            if(requestPalletWorkScanDto.getPalletType() == 0){
                // 判断是否同一工单
                List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtos = mesSfcWorkOrderBarcodeService.findByWorkOrderGroup(map);
                if(workOrderBarcodeDtos.size() > 1){
                    throw new BizErrorException(ErrorCodeEnum.PDA40012034);
                }
            }
            //扫描的是箱码 判断是否为同一PO 2021-10-20
            if(requestPalletWorkScanDto.getPalletType() == 2){
                Map<String, Object> currentmap = new HashMap<>();
                currentmap.put("workOrderBarcodeId", mesSfcProductCartonDetDtoList.get(0).getWorkOrderBarcodeId());
                List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(currentmap);
                if (processServiceList.get(0).getSamePackageCode() == null){
                    // PDA设置为同PO栈板，如果该条码没有PO号则按同料号走
                    requestPalletWorkScanDto.setPalletType((byte) 1);
                }else {
                    // 判断是否为同一PO
                    List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findByPOGroup(map);
                    if(mesSfcBarcodeProcessList.size() > 1){
                        throw new BizErrorException(ErrorCodeEnum.PDA40012034.getCode(),"该包箱条码不属于同个PO，不可扫码");
                    }else{
                        samePackageCode=mesSfcBarcodeProcessList.get(0).getSamePackageCode();
                    }
                }
            }
        } else {
            Long workOrderBarcodeId = null;
            BaseLabelCategory labelCategory = baseFeignApi.findLabelCategoryDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()).getData();
            if (labelCategory.getLabelCategoryCode().equals("01")) {
                // 产品条码
                workOrderBarcodeId = mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId();
                workOrderId = mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId();
            } else if (labelCategory.getLabelCategoryCode().equals("02")) {
                // 销售订单条码
                Map<String, Object> map = new HashMap<>();
                map.put("partBarcode", requestPalletWorkScanDto.getBarcode());
                List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
                if (mesSfcKeyPartRelevanceDtoList.isEmpty() || mesSfcKeyPartRelevanceDtoList.size() > 1) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
                }
                workOrderBarcodeId = mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId();
                workOrderId = mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("workOrderBarcodeId", workOrderBarcodeId);
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if(processServiceList == null && processServiceList.size() <= 0){
                throw new BizErrorException(ErrorCodeEnum.PDA40012000);
            }

            if (requestPalletWorkScanDto.getPalletType() == 2 && processServiceList.get(0).getSamePackageCode() == null){
                // PDA设置为同PO栈板，如果该条码没有PO号则按同料号走
                requestPalletWorkScanDto.setPalletType((byte) 1);
            }

            if(requestPalletWorkScanDto.getPalletType() == 0){

                if(StringUtils.isNotEmpty(processServiceList.get(0).getCartonCode())) {
                    map.clear();
                    map.put("cartonCode", processServiceList.get(0).getCartonCode());

                    // 判断是否同一工单
                    List<MesSfcWorkOrderBarcodeDto> workOrderBarcodeDtos = mesSfcWorkOrderBarcodeService.findByWorkOrderGroup(map);
                    if (workOrderBarcodeDtos.size() > 1) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012034);
                    }
                }
                else{
                    throw new BizErrorException(ErrorCodeEnum.PDA40012034.getCode(), "该产品条码未包箱，不可进行栈板扫码");
                }

            }
            //扫描的是箱码 判断是否为同一PO 2021-10-20
            if(requestPalletWorkScanDto.getPalletType() == 2){
                // 判断是否为同一PO
                if(StringUtils.isNotEmpty(processServiceList.get(0).getCartonCode())) {
                    map.clear();
                    map.put("cartonCode", processServiceList.get(0).getCartonCode());

                    List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findByPOGroup(map);
                    if (mesSfcBarcodeProcessList.size() > 1) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012034.getCode(), "该包箱条码不属于同个PO，不可扫码");
                    } else {
                        samePackageCode = mesSfcBarcodeProcessList.get(0).getSamePackageCode();
                    }
                }
                else{
                    throw new BizErrorException(ErrorCodeEnum.PDA40012034.getCode(), "该产品条码未包箱，不可进行栈板扫码");
                }
            }

            Map<String, Object> productCartonDetMap = new HashMap<>();
            productCartonDetMap.put("workOrderBarcodeId", workOrderBarcodeId);
            List<MesSfcProductCartonDetDto> mesSfcProductCartonDetDtoList = mesSfcProductCartonDetService.findList(productCartonDetMap);
            // 产品条码没有关联箱号
            if (mesSfcProductCartonDetDtoList.isEmpty()) {
                searchMesSfcWorkOrderBarcode.setBarcode(null);
                searchMesSfcWorkOrderBarcode.setWorkOrderBarcodeId(workOrderBarcodeId);
                List<MesSfcWorkOrderBarcodeDto> barcodeServiceList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                if (barcodeServiceList.isEmpty()){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该包箱关联产品条码不存在");
                }
                mesSfcWorkOrderBarcodeList.add(barcodeServiceList.get(0));
                // 获取产品条码关联箱号绑定的所有产品条码
            } else {
                // 判断对应的包箱是否已关闭
                MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(mesSfcProductCartonDetDtoList.get(0).getProductCartonId());
                if (mesSfcProductCarton.getCloseStatus() == 0) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该条码对应的包箱未关闭，请先进行包箱关闭");
                }
                productCartonDetMap.clear();
                productCartonDetMap.put("productCartonId", mesSfcProductCartonDetDtoList.get(0).getProductCartonId());
                mesSfcProductCartonDetDtoList = mesSfcProductCartonDetService.findList(productCartonDetMap);
                for (MesSfcProductCartonDetDto mesSfcProductCartonDetDto : mesSfcProductCartonDetDtoList) {
                    searchMesSfcWorkOrderBarcode.setBarcode(null);
                    searchMesSfcWorkOrderBarcode.setWorkOrderBarcodeId(mesSfcProductCartonDetDto.getWorkOrderBarcodeId());
                    List<MesSfcWorkOrderBarcodeDto> barcodeServiceList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
                    if (barcodeServiceList.isEmpty()){
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该包箱关联产品条码不存在");
                    }
                    mesSfcWorkOrderBarcodeList.add(barcodeServiceList.get(0));
                }
            }
        }

        // 获取该条码对应的工单信息
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
        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
        String palletCode = "";
        BigDecimal nowPackageSpecQty = BigDecimal.ZERO;
        Boolean isPallet = true;
        // 栈板已绑定的包装数量
        int palletCartons = 0;
        for (MesSfcProductPalletDto mesSfcProductPalletDto : mesSfcProductPalletDtoList) {
            if ((requestPalletWorkScanDto.getPalletType() == 0 && mesPmWorkOrderDto.getWorkOrderId().equals(mesSfcProductPalletDto.getWorkOrderId()))
            || (requestPalletWorkScanDto.getPalletType() == 1 && mesPmWorkOrderDto.getMaterialId().equals(mesSfcProductPalletDto.getMaterialId()))) {
                palletCode = mesSfcProductPalletDto.getPalletCode();
                nowPackageSpecQty = mesSfcProductPalletDto.getNowPackageSpecQty();
                isPallet = false;
                mesSfcProductPallet = mesSfcProductPalletDto;
                palletCartons = findPalletCarton(mesSfcProductPallet.getProductPalletId()).size();
                break;
            }

            //同一PO判断 2021-10-21
            if(requestPalletWorkScanDto.getPalletType() == 2){
                if(StringUtils.isNotEmpty(mesSfcProductPalletDto.getPalletCode()) && StringUtils.isNotEmpty(samePackageCode)){
                    Map<String, Object> PalletMap = new HashMap<>();
                    PalletMap.put("palletCode",mesSfcProductPalletDto.getPalletCode() );
                    List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessService.findByPalletPOGroup(PalletMap);
                    if(mesSfcBarcodeProcessList.size() > 0){
                        samePackageCodePallet= mesSfcBarcodeProcessList.get(0).getSamePackageCode();
                        if(StringUtils.isNotEmpty(samePackageCodePallet) && samePackageCodePallet.equals(samePackageCode)){
                            palletCode = mesSfcProductPalletDto.getPalletCode();
                            nowPackageSpecQty = mesSfcProductPalletDto.getNowPackageSpecQty();
                            isPallet = false;
                            mesSfcProductPallet = mesSfcProductPalletDto;
                            palletCartons = findPalletCarton(mesSfcProductPallet.getProductPalletId()).size();
                            break;
                        }
                    }
                }
            }
        }

        // 查找条码生成规则配置，生成新的栈板码
        if (isPallet) {
            if (mesSfcProductPalletDtoList.size() >= requestPalletWorkScanDto.getMaxPalletNum()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前栈板可操作已达上限，该条码不属于正在操作的" + mesSfcProductPalletDtoList.size() + "个栈板，最大栈板操作数量" + requestPalletWorkScanDto.getMaxPalletNum());
            }

            SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
            searchBasePackageSpecification.setMaterialId(mesPmWorkOrderDto.getMaterialId());
            searchBasePackageSpecification.setProcessId(requestPalletWorkScanDto.getProcessId());
            List<BasePackageSpecificationDto> basePackageSpecificationDtoList = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification).getData();
            if (basePackageSpecificationDtoList.isEmpty() || basePackageSpecificationDtoList.get(0).getBaseMaterialPackages().isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该产品条码没有设置在该工序的包装规格");
            }
            // 获取该条码规则对应的最大条码数
            String maxCode = null;
            String lastBarCode = null;
            String barcodeRule = "";
            // 获取条码规则配置集合
            List<BaseBarcodeRuleSpec> barcodeRuleSpecList = new LinkedList<>();
            for (BaseMaterialPackage baseMaterialPackage : basePackageSpecificationDtoList.get(0).getBaseMaterialPackages()) {
                if (requestPalletWorkScanDto.getProcessId().equals(baseMaterialPackage.getProcessId())) {
                    BasePackageSpecification basePackageSpecification = baseFeignApi.BasePackageSpecificationDetail(baseMaterialPackage.getPackageSpecificationId()).getData();
                    if (StringUtils.isEmpty(basePackageSpecification)) {
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该产品条码没有设置在该工序的包装规格");
                    }
                    nowPackageSpecQty = basePackageSpecification.getPackageSpecificationQuantity();
                    BaseBarcodeRule baseBarcodeRule = baseFeignApi.baseBarcodeRuleDetail(baseMaterialPackage.getBarcodeRuleId()).getData();
                    barcodeRule = baseBarcodeRule.getBarcodeRule();
                    boolean hasKey = redisUtil.hasKey(baseBarcodeRule.getBarcodeRule());
                    if(hasKey){
                        // 从redis获取上次生成条码
                        lastBarCode = redisUtil.get(baseBarcodeRule.getBarcodeRule()).toString();
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
            //获取最大流水号
            maxCode = baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
            palletCode = baseFeignApi.newGenerateCode(barcodeRuleSpecList, maxCode, baseBarcodeRuleSpecMap, workOrderId.toString()).getData();
            redisUtil.set(barcodeRule, palletCode);
            if (StringUtils.isEmpty(palletCode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012008);
            }
        }
        if (nowPackageSpecQty.compareTo(BigDecimal.ZERO) == 0) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "该工序包装规格设置有误，包装数量不能为0");
        }
        // 更新过站/过站记录信息
        for (MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode : mesSfcWorkOrderBarcodeList) {
            // 更新当前工序，工位，工段，下一工序
            UpdateProcessDto updateProcessDto = new UpdateProcessDto();
            updateProcessDto.setBarCode(mesSfcWorkOrderBarcode.getBarcode());
            updateProcessDto.setNowProcessId(requestPalletWorkScanDto.getProcessId());
            updateProcessDto.setProLineId(requestPalletWorkScanDto.getProLineId());
            updateProcessDto.setRouteId(mesPmWorkOrderDto.getRouteId());
            updateProcessDto.setOperatorUserId(user.getUserId());
            updateProcessDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
            updateProcessDto.setNowStationId(requestPalletWorkScanDto.getStationId());
            updateProcessDto.setPassCode(palletCode);
            updateProcessDto.setPassCodeType((byte) 2);
            BarcodeUtils.updateProcess(updateProcessDto);
        }

        // 当前工单已关闭栈板
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderId", workOrderId);
        map.put("closeStatus", 1);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtos = mesSfcProductPalletService.findList(map);
        int closePalletNum = mesSfcProductPalletDtos.size();
        // 新增产品栈板信息，产品栈板和产品条码明细表
        if (isPallet) {
            mesSfcProductPallet.setPalletCode(palletCode);
            mesSfcProductPallet.setNowPackageSpecQty(nowPackageSpecQty);
            mesSfcProductPallet.setWorkOrderId(workOrderId);
            mesSfcProductPallet.setMaterialId(mesPmWorkOrderDto.getMaterialId());
            mesSfcProductPallet.setStationId(requestPalletWorkScanDto.getStationId());
            mesSfcProductPallet.setCloseStatus((byte) 0);
            if (nowPackageSpecQty.compareTo(BigDecimal.ONE) == 0) {
                mesSfcProductPallet.setCloseStatus((byte) 1);
                mesSfcProductPallet.setClosePalletUserId(user.getUserId());
                mesSfcProductPallet.setClosePalletTime(new Date());
            }
            mesSfcProductPallet.setOrgId(user.getOrganizationId());
            mesSfcProductPallet.setCreateUserId(user.getUserId());
            mesSfcProductPallet.setCreateTime(new Date());
            mesSfcProductPallet.setMoveStatus((byte) 0);
            mesSfcProductPalletService.save(mesSfcProductPallet);
        } else if (nowPackageSpecQty.compareTo(BigDecimal.valueOf(palletCartons + 1)) == 0) {

            // 达到包装规格上限，关闭栈板
            mesSfcProductPallet.setCloseStatus((byte) 1);
            mesSfcProductPallet.setClosePalletUserId(user.getUserId());
            mesSfcProductPallet.setClosePalletTime(new Date());
            mesSfcProductPallet.setModifiedUserId(user.getUserId());
            mesSfcProductPallet.setModifiedTime(new Date());
            mesSfcProductPalletService.update(mesSfcProductPallet);
            closePalletNum += 1;
            // 是否打印栈板码
            if (requestPalletWorkScanDto.getPrintBarcode() == 1) {
                PrintCarCodeDto printCarCodeDto = new PrintCarCodeDto();
                printCarCodeDto.setWorkOrderId(workOrderId);
                printCarCodeDto.setLabelTypeCode("10");
                printCarCodeDto.setBarcode(palletCode);
                printCarCodeDto.setPrintName(requestPalletWorkScanDto.getPrintName() != null ? requestPalletWorkScanDto.getPrintName() : "测试");
                BarcodeUtils.printBarCode(printCarCodeDto);
            }


            if (mesPmWorkOrderDto.getOutputProcessId().equals(requestPalletWorkScanDto.getProcessId())){
                // 生成完工入库单
                List<Long> palletIds = new ArrayList<>();
                palletIds.add(mesSfcProductPallet.getProductPalletId());
                this.beforePalletAutoAsnOrder(palletIds, user.getOrganizationId(), mesSfcWorkOrderBarcodeList);
            }
        }

        for (MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode : mesSfcWorkOrderBarcodeList) {
            MesSfcProductPalletDet mesSfcProductPalletDet = new MesSfcProductPalletDet();
            mesSfcProductPalletDet.setProductPalletId(mesSfcProductPallet.getProductPalletId());
            mesSfcProductPalletDet.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
            mesSfcProductPalletDet.setOrgId(user.getOrganizationId());
            mesSfcProductPalletDet.setCreateUserId(user.getUserId());
            mesSfcProductPalletDet.setCreateTime(new Date());
            mesSfcProductPalletDetService.save(mesSfcProductPalletDet);
        }

        // 构建返回值
        PalletWorkScanDto palletWorkScanDto = new PalletWorkScanDto();
        palletWorkScanDto.setProductPalletId(mesSfcProductPallet.getProductPalletId());
        palletWorkScanDto.setPalletCode(palletCode);
        palletWorkScanDto.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
        palletWorkScanDto.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
        palletWorkScanDto.setMaterialDesc(mesPmWorkOrderDto.getMaterialDesc());
        palletWorkScanDto.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());
        palletWorkScanDto.setProductionQty(mesPmWorkOrderDto.getProductionQty());
        palletWorkScanDto.setClosePalletNum(BigDecimal.valueOf(closePalletNum));
        palletWorkScanDto.setNowPackageSpecQty(nowPackageSpecQty);
        palletWorkScanDto.setScanCartonNum(palletCartons + 1);

        // 未满箱
        if (nowPackageSpecQty.compareTo(BigDecimal.valueOf(palletCartons + 1)) != 0){
            /**
             * 万宝特性要求
             * 当程序配置项有数据时，则走此段逻辑
             * 栈板作业，增加根据产品条码、PO号判断当前产线是否有同样的产品，没有则关闭当前栈板
             */
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoAutoClosePallet");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty() && "1".equals(specItems.get(0).getParaValue())){
                // 判断同个工单的、已过包箱过站但是没有栈板过站的包箱、并且PO号一致的数据是否存在
                Map<String, Object> param = new HashMap<>();
                param.put("workOrderId", mesSfcProductPallet.getWorkOrderId());
//                param.put("samePackageCode", );

                //扫描的条码有PO号判断 2021-10-22
                if(StringUtils.isNotEmpty(samePackageCode) && requestPalletWorkScanDto.getPalletType() == 2){
                    param.put("samePackageCode", samePackageCode);
                }

                List<MesSfcBarcodeProcess> nextProcessIsPallet = mesSfcBarcodeProcessService.findNextProcessIsPallet(param);
                // TODO PO号判断
                //栈板作业的时候，判断当前条码有没有PO号，
                //如果有PO号 -> 则校验条码工单号、PO号在已过包箱数据里面还有没有，有就不做任何处理，没有的话就自动提交当前栈板
                //如果没有PO号 -> 则校验条码工单号在已过包箱数据里面还有没有，有就不做任何处理，没有的话就自动提交当前栈板

                if (nextProcessIsPallet.isEmpty()){
                    // 未满栈板自动提交
                    List<Long> palletIdList = new ArrayList<>();
                    palletIdList.add(mesSfcProductPallet.getProductPalletId());
                    this.submitNoFullPallet(palletIdList, requestPalletWorkScanDto.getPrintBarcode(), requestPalletWorkScanDto.getPrintName(), requestPalletWorkScanDto.getProcessId());
                }
            }
        }

        return palletWorkScanDto;
    }

    @Override
    public List<PalletWorkScanDto> palletWorkScan(Long stationId) {

        List<PalletWorkScanDto> palletWorkScanDtoList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("closeStatus", 0);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtoList = mesSfcProductPalletService.findList(map);
        for (MesSfcProductPalletDto mesSfcProductPalletDto : mesSfcProductPalletDtoList) {
            PalletWorkScanDto palletWorkScanDto = new PalletWorkScanDto();
            BeanUtils.autoFillEqFields(mesSfcProductPalletDto, palletWorkScanDto);
            map.clear();
            map.put("workOrderId", mesSfcProductPalletDto.getWorkOrderId());
            map.put("closeStatus", 1);
            List<MesSfcProductPalletDto> mesSfcProductPalletDtos = mesSfcProductPalletService.findList(map);
            palletWorkScanDto.setClosePalletNum(BigDecimal.valueOf(mesSfcProductPalletDtos.size()));
            int palletCartons = findPalletCarton(mesSfcProductPalletDto.getProductPalletId()).size();
            palletWorkScanDto.setScanCartonNum(palletCartons);
            // 栈板下的箱码
            List<String> cartonCodeList = this.findPalletCarton(mesSfcProductPalletDto.getProductPalletId());
            palletWorkScanDto.setCartonCodeList(cartonCodeList);
            palletWorkScanDtoList.add(palletWorkScanDto);
        }

        return palletWorkScanDtoList;
    }

    @Override
    public List<String> findPalletCarton(Long productPalletId) {

        List<String> cartonCodeList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productPalletId", productPalletId);
        map.put("groupBy", "cartonCode");
        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtoList = mesSfcProductPalletDetService.findList(map);
        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : mesSfcProductPalletDetDtoList) {
            cartonCodeList.add(mesSfcProductPalletDetDto.getCartonCode());
        }

        return cartonCodeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int submitNoFullPallet(List<Long> palletIdList, byte printBarcode, String printName, Long processId) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        log.error("============未满箱提交，palletIdList:"+ palletIdList.size());
        Example example = new Example(MesSfcProductPallet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("productPalletId", palletIdList);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductPallet> productPallets = mesSfcProductPalletService.selectByExample(example);
        log.error("============未满箱提交，productPallets:"+ productPallets.size());
        List<Long> palletIds = new ArrayList<>();
        for (MesSfcProductPallet item : productPallets){
            item.setCloseStatus((byte) 1);
            item.setModifiedUserId(user.getUserId());
            item.setModifiedTime(new Date());
            mesSfcProductPalletService.update(item);
            // 是否打印栈板码
            if (printBarcode == 1) {
                PrintCarCodeDto printCarCodeDto = new PrintCarCodeDto();
                printCarCodeDto.setWorkOrderId(item.getWorkOrderId());
                printCarCodeDto.setLabelTypeCode("10");
                printCarCodeDto.setBarcode(item.getPalletCode());
                printCarCodeDto.setPrintName(printName != null ? printName : "测试");
                BarcodeUtils.printBarCode(printCarCodeDto);
            }
            palletIds.add(item.getProductPalletId());
        }

        // 生成完工入库单
        log.error("============未满箱提交，palletIds:"+ palletIds.size());
        if (!palletIds.isEmpty() && palletIds.size() > 0){
            // 获取该条码对应的工单信息
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(productPallets.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (mesPmWorkOrderDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
            if (mesPmWorkOrderDto.getOutputProcessId().equals(processId)){
                this.beforePalletAutoAsnOrder(palletIds, user.getOrganizationId(), null);
            }
        }
        return productPallets.size();
    }

    @Override
    public Boolean updatePalletType(Long stationId) {
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcProductPallet.builder()
                .stationId(stationId)
                .closeStatus((byte) 0)
                .build()));
        if(list.size()>0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateNowPackageSpecQty(Long productPalletId, Double nowPackageSpecQty, Boolean print, String printName, Long processId) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        int palletCartons = findPalletCarton(productPalletId).size();
        if (nowPackageSpecQty < palletCartons) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "修改栈板包装规格数量不能小于栈板已绑定包箱数量");
        }
        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
        mesSfcProductPallet.setProductPalletId(productPalletId);
        mesSfcProductPallet.setNowPackageSpecQty(BigDecimal.valueOf(nowPackageSpecQty));
        if (nowPackageSpecQty == palletCartons) {
            mesSfcProductPallet.setCloseStatus((byte) 1);
            mesSfcProductPallet.setClosePalletUserId(user.getUserId());
            mesSfcProductPallet.setClosePalletTime(new Date());

            // 获取该条码对应的工单信息
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcProductPallet.getWorkOrderId());
            List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (mesPmWorkOrderDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
            if (mesPmWorkOrderDto.getOutputProcessId().equals(processId)){
                // 生成完工入库单
                List<Long> palletIds = new ArrayList<>();
                palletIds.add(productPalletId);
                this.beforePalletAutoAsnOrder(palletIds, user.getOrganizationId(), null);
            }
        }
        mesSfcProductPallet.setModifiedTime(new Date());
        mesSfcProductPallet.setModifiedUserId(user.getUserId());
        int update = mesSfcProductPalletService.update(mesSfcProductPallet);
        if (print && update > 0) {
            // 关箱后才能打印条码
            BarcodeUtils.printBarCode(PrintCarCodeDto.builder()
                    .barcode(mesSfcProductPallet.getPalletCode())
                    .labelTypeCode("09")
                    .workOrderId(mesSfcProductPallet.getWorkOrderId())
                    .printName(printName != null ? printName : "测试")
                    .build());
        }
        return update;
    }

    @Override
    public int updateMoveStatus(Long productPalletId) {
        MesSfcProductPallet sfcProductPallet = mesSfcProductPalletService.selectByKey(productPalletId);
        if(sfcProductPallet == null){
            throw new BizErrorException(ErrorCodeEnum.PDA40012030);
        }
        sfcProductPallet.setMoveStatus((byte) 1);
        return mesSfcProductPalletService.update(sfcProductPallet);
    }

    /**
     * 生成完工入库单
     * @param palletIds 栈板集合
     * @param orgId 组织
     * @param mesSfcWorkOrderBarcodeList 条码集合
     */
    private void beforePalletAutoAsnOrder(List<Long> palletIds, Long orgId, List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeList){
        /**
         * @date 20211217
         * @autor bgkun
         * 判断是否包箱工序是否工单的产出工序，若是，则调用完工入库
         * 增加程序配置项决定是否开启此处理
         */
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("autoWipCompletion");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        log.info("程序配置项autoWipCompletion值："+ specItems.get(0).getParaValue());
        if (!specItems.isEmpty() && specItems.size() > 0 && specItems.get(0).getParaValue().equals("1")){
            Map<String, Object> map = new HashMap<>();
            map.put("list", palletIds);
            map.put("orgId", orgId);
            List<PalletAutoAsnDto> autoAsnDtos = mesSfcProductPalletDetService.findListGroupByWorkOrder(map);
            for (PalletAutoAsnDto palletAutoAsnDto : autoAsnDtos){
                map.clear();
                map.put("productPalletId", palletAutoAsnDto.getProductPalletId());
                List<MesSfcWorkOrderBarcodeDto> barcodeDtos = mesSfcWorkOrderBarcodeService.findListByPalletDet(map);
                List<BarPODto> barPODtos = new ArrayList<>();
                barcodeDtos.forEach(item -> {
                    BarPODto barPODto = new BarPODto();
                    barPODto.setBarCode(item.getBarcode());
                    barPODto.setPOCode(item.getSamePackageCode());
                    barPODto.setCutsomerBarcode(item.getCutsomerBarcode());
                    barPODto.setSalesBarcode(item.getSalesBarcode());
                    barPODtos.add(barPODto);
                });
                if(mesSfcWorkOrderBarcodeList != null && mesSfcWorkOrderBarcodeList.size() > 0) {
                    mesSfcWorkOrderBarcodeList.forEach(item -> {
                        BarPODto barPODto = new BarPODto();
                        barPODto.setBarCode(item.getBarcode());
                        barPODto.setPOCode(item.getSamePackageCode());
                        barPODtos.add(barPODto);
                    });
                    palletAutoAsnDto.setActualQty(palletAutoAsnDto.getActualQty().add(new BigDecimal(mesSfcWorkOrderBarcodeList.size())));
                    palletAutoAsnDto.setPackingQty(palletAutoAsnDto.getActualQty());
                }
                palletAutoAsnDto.setBarCodeList(barPODtos);
                //完工入库
                SearchBaseMaterialOwner searchBaseMaterialOwner = new SearchBaseMaterialOwner();
                searchBaseMaterialOwner.setAsc((byte)1);
                ResponseEntity<List<BaseMaterialOwnerDto>> baseMaterialOwnerDtos = baseFeignApi.findList(searchBaseMaterialOwner);
                if(StringUtils.isNotEmpty(baseMaterialOwnerDtos.getData())){
                    palletAutoAsnDto.setMaterialOwnerId(baseMaterialOwnerDtos.getData().get(0).getMaterialOwnerId());
                    palletAutoAsnDto.setShipperName(baseMaterialOwnerDtos.getData().get(0).getMaterialOwnerName());
                }else{
                    throw new BizErrorException("未查询到货主信息");
                }

                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setMinSurplusCanPutSalver(0);
                searchBaseStorage.setStorageType((byte) 2);
                // 万宝项目-通过程序配置项判定物料匹配的仓库
                searchSysSpecItem.setSpecCode("wanbaoWarehouse");
                List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
                if(!itemList.isEmpty()){
                    String warehouseCode = null;
                    log.info("程序配置项wanbaoWarehouse值："+ itemList.get(0).getParaValue() + " ========== 条码集合数：" + barcodeDtos.size());
                    List<Map<String, String>> list = JSONArray.parseObject(itemList.get(0).getParaValue(), List.class);
                    for (Map<String, String> item : list){
                        if(barcodeDtos.get(0).getMaterialCode().contains(item.get("material"))){
                            warehouseCode = item.get("warehouse");
                            break;
                        }
                    }
                    searchBaseStorage.setWarehouseCode(warehouseCode);
                }
                ResponseEntity<List<BaseStorage>> baseStorages = baseFeignApi.findList(searchBaseStorage);
                if(StringUtils.isNotEmpty(baseStorages.getData())){
                    palletAutoAsnDto.setStorageId(baseStorages.getData().get(0).getStorageId());
                    palletAutoAsnDto.setWarehouseId(baseStorages.getData().get(0).getWarehouseId());
                }else{
                    throw new BizErrorException(ErrorCodeEnum.STO30012000);
                }
                palletAutoAsnDto.setProductionDate(new Date());

                //2021-07-30 增加包装单位 by Dylan
                SearchBasePackageSpecification searchBasePackageSpecification = new SearchBasePackageSpecification();
                searchBasePackageSpecification.setMaterialId(palletAutoAsnDto.getMaterialId());
                searchBasePackageSpecification.setOrgId(orgId);
                ResponseEntity<List<BasePackageSpecificationDto>> basePackgeSpecifications = baseFeignApi.findBasePackageSpecificationList(searchBasePackageSpecification);
                if(StringUtils.isNotEmpty(basePackgeSpecifications.getData())){
                    List<BaseMaterialPackageDto> baseMaterialPackageDtos = basePackgeSpecifications.getData().get(0).getBaseMaterialPackages();
                    if(StringUtils.isNotEmpty(baseMaterialPackageDtos)){
                        BaseMaterialPackageDto baseMaterialPackageDto = baseMaterialPackageDtos.get(0);
                        palletAutoAsnDto.setPackingUnitName(baseMaterialPackageDto.getPackingUnitName());
                    }
                }
                inFeignApi.palletAutoAsnOrder(palletAutoAsnDto);
            }
        }
    }

    public Boolean judgeSamePOPallet(Long stationId) {
        List<MesSfcProductPalletDto> list = mesSfcProductPalletService.findList(ControllerUtil.dynamicConditionByEntity(SearchMesSfcProductPallet.builder()
                .stationId(stationId)
                .closeStatus((byte) 0)
                .build()));
        if(list.size()>0){
            return false;
        }
        return true;
    }
}

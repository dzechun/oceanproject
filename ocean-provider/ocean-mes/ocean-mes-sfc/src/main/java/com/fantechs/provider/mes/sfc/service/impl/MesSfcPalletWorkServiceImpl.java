package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
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
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.dto.wms.in.BarPODto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.wanbao.WanbaoFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.mes.sfc.service.*;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MesSfcPalletWorkServiceImpl implements MesSfcPalletWorkService {

    // region Service 注入

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
    @Resource
    OMFeignApi omFeignApi;
    @Resource
    WanbaoFeignApi wanbaoFeignApi;
    @Resource
    InnerFeignApi innerFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;

    // endregion

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //samePackageCode 同包装编码--扫描的箱号产品条码对应的或者扫描的产品条码对应的PO编码 2020-10-20
        String samePackageCode = "";
        // 同销售订单栈板的销售订单编码 2022-01-20
        String salesOrderCode = "";
        //samePackageCodePallet 同包装编码--栈板包装产品条码对应的PO编码 2020-10-20
        String samePackageCodePallet="";
        Long workOrderBarcodeId = null;
        Long workOrderId = null;

        // 栈板作业需要绑定的所有产品条码
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeList = new ArrayList<>();
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
        String barcode = searchMesSfcWorkOrderBarcode.getBarcode();
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        List<MesSfcBarcodeProcessDto> processServiceList = new ArrayList<>();
        if (!mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
            BaseLabelCategory labelCategory = baseFeignApi.findLabelCategoryDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()).getData();
            if (labelCategory.getLabelCategoryCode().equals("01")) {
                // 产品条码
                workOrderBarcodeId = mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderBarcodeId();
                barcode = mesSfcWorkOrderBarcodeDtoList.get(0).getBarcode();
                workOrderId = mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId();
            } else if (labelCategory.getLabelCategoryCode().equals("02") || labelCategory.getLabelCategoryCode().equals("03")) {
                // 销售订单条码/客户条码
                Map<String, Object> map = new HashMap<>();
                map.put("partBarcode", requestPalletWorkScanDto.getBarcode());
                List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
                if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
                }
                workOrderBarcodeId = mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderBarcodeId();
                workOrderId = mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId();
                barcode = mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("workOrderBarcodeId", workOrderBarcodeId);
            processServiceList = mesSfcBarcodeProcessService.findList(map);
            if(processServiceList == null && processServiceList.size() <= 0){
                throw new BizErrorException(ErrorCodeEnum.PDA40012000);
            }

            if (requestPalletWorkScanDto.getPalletType() == 2 && processServiceList.get(0).getSamePackageCode() == null){
                // PDA设置为同PO栈板，如果该条码没有PO号则按同销售订单走
                requestPalletWorkScanDto.setPalletType((byte) 3);
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
            // 判断是否为同一PO 2021-10-20
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

            // 判断是否为同一销售订单 2022-01-20
            if(requestPalletWorkScanDto.getPalletType() == 3){
                SearchOmSalesOrderDto salesOrderDto = new SearchOmSalesOrderDto();
                salesOrderDto.setWorkOrderId(workOrderId);
                List<OmSalesOrderDto> salesOrderDtos = omFeignApi.findList(salesOrderDto).getData();
                if (salesOrderDtos.isEmpty() || salesOrderDtos.get(0).getSalesOrderCode() == null){
                    // PDA设置为同销售订单栈板，如果该条码没有销售订单编码则按同料号走
                    requestPalletWorkScanDto.setPalletType((byte) 1);
                }else {
                    salesOrderCode = salesOrderDtos.get(0).getSalesOrderCode();
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

        // 2022-03-08 判断是否质检完成之后走产线入库
        SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
        searchWmsInnerInventoryDet.setBarcode(barcode);
        List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
        if (!inventoryDetDtos.isEmpty()){
            return new PalletWorkScanDto();
        }

        // 获取该条码对应的工单信息
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderId(workOrderId);
        List<MesPmWorkOrderDto> mesPmWorkOrderDtoList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if (mesPmWorkOrderDtoList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "没有找到条码绑定的工单");
        }
        MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtoList.get(0);
        if (!mesPmWorkOrderDto.getProLineId().equals(requestPalletWorkScanDto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.PDA40012003.getCode(), "该产品条码产线跟该工位产线不匹配");
        }
        if (processServiceList.isEmpty() || processServiceList.size() <= 0){
            Map<String, Object> map = new HashMap<>();
            map.put("workOrderBarcodeId", workOrderBarcodeId);
            processServiceList = mesSfcBarcodeProcessService.findList(map);
        }
        MesSfcBarcodeProcessDto barcodeProcessDto = processServiceList.get(0);
        if (!barcodeProcessDto.getNextProcessId().equals(requestPalletWorkScanDto.getProcessId())){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "PDA配置工序与条码工序不匹配");
        }

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

            //同一销售订单判断 2022-01-20
            if(requestPalletWorkScanDto.getPalletType() == 3){
                if(StringUtils.isNotEmpty(mesSfcProductPalletDto.getPalletCode()) && StringUtils.isNotEmpty(salesOrderCode)){
                    Map<String, Object> PalletMap = new HashMap<>();
                    PalletMap.put("palletCode",mesSfcProductPalletDto.getPalletCode() );
                    List<MesSfcProductPalletBySalesOrderDto> salesCodeGroup = mesSfcProductPalletService.findBySalesCodeGroup(PalletMap);
                    if(salesCodeGroup.size() > 0){
                        String currentSalesOrderCode = salesCodeGroup.get(0).getSalesOrderCode();
                        if(StringUtils.isNotEmpty(currentSalesOrderCode) && salesOrderCode.equals(currentSalesOrderCode)){
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
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前栈板可操作已达上限，最大栈板操作数量：" + requestPalletWorkScanDto.getMaxPalletNum());
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

            // 2022-03-01 改为不自动提交栈板，有人工堆垛作业触发
            /*if (mesPmWorkOrderDto.getOutputProcessId().equals(requestPalletWorkScanDto.getProcessId())){
                // 生成完工入库单
                List<Long> palletIds = new ArrayList<>();
                palletIds.add(mesSfcProductPallet.getProductPalletId());
                this.beforePalletAutoAsnOrder(palletIds, user.getOrganizationId(), mesSfcWorkOrderBarcodeList);
            }*/
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

        /**
         * @date 2022-04-06
         * 调用人工堆垛方法，完成该流程形成数据闭环，并且A线要驱动分舵机
         */
        if (requestPalletWorkScanDto.getIsReadHead() != null && requestPalletWorkScanDto.getIsReadHead()){
            PalletWorkByManualOperationDto dto = new PalletWorkByManualOperationDto();
            BeanUtil.copyProperties(requestPalletWorkScanDto, dto);
            List<WanbaoBarcodeDto> list = new ArrayList<>();
            WanbaoBarcodeDto wanbaoBarcodeDto = new WanbaoBarcodeDto();
            wanbaoBarcodeDto.setBarcode(barcode);
            // 产品条码
            Map<String, Object> mapKeyPart = new HashMap<>();
            mapKeyPart.put("barcodeCode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(mapKeyPart);
            if (!mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList){
                    BaseLabelCategory keyPartLabelCategory = baseFeignApi.findLabelCategoryDetail(keyPartRelevanceDto.getLabelCategoryId()).getData();
                    if (keyPartLabelCategory.getLabelCategoryCode().equals("02")) {
                        // 销售订单条码
                        wanbaoBarcodeDto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                    }else if (keyPartLabelCategory.getLabelCategoryCode().equals("03")){
                        // 客户条码
                        wanbaoBarcodeDto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                    }
                }
            }
            list.add(wanbaoBarcodeDto);
            dto.setWanbaoBarcodeDtos(list);

            mapKeyPart.clear();
            mapKeyPart.put("usageStatus", 2);
            mapKeyPart.put("proName", dto.getStackingCode());
            List<WanbaoStackingDto> stackingList = mesSfcProductPalletService.findStackingList(mapKeyPart);
            if (stackingList.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "暂无空闲堆垛，请处理");
            }
            dto.setStackingCode(stackingList.get(0).getStackingCode());
            this.workByManualOperation(dto);

            palletWorkScanDto.setPalletCode(dto.getStackingCode());
        }else {
            palletWorkScanDto.setPalletCode(palletCode);
        }
        palletWorkScanDto.setProductPalletId(mesSfcProductPallet.getProductPalletId());
        palletWorkScanDto.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
        palletWorkScanDto.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
        palletWorkScanDto.setMaterialDesc(mesPmWorkOrderDto.getMaterialDesc());
        palletWorkScanDto.setWorkOrderQty(mesPmWorkOrderDto.getWorkOrderQty());
        palletWorkScanDto.setProductionQty(mesPmWorkOrderDto.getProductionQty());
        palletWorkScanDto.setClosePalletNum(BigDecimal.valueOf(closePalletNum));
        palletWorkScanDto.setNowPackageSpecQty(nowPackageSpecQty);
        palletWorkScanDto.setScanCartonNum(palletCartons + 1);
        return palletWorkScanDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int workByManualOperation(PalletWorkByManualOperationDto dto) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("stackingCode", dto.getStackingCode());
        List<WanbaoStackingDto> stackingList = mesSfcProductPalletService.findStackingList(map);
        if (stackingList.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该堆垛编码不存在");
        }
        try {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (dto.getWanbaoBarcodeDtos() == null || dto.getWanbaoBarcodeDtos().isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL99990500.getCode(), "条码集合为空，请扫码");
            }
            List<String> barcodeList = dto.getWanbaoBarcodeDtos().stream().map(WanbaoBarcodeDto::getBarcode).collect(Collectors.toList());
            SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
            searchMesSfcWorkOrderBarcode.setBarcodeList(barcodeList);
            List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
            if (mesSfcWorkOrderBarcodeDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在或已被删除");
            }

            // 校验同一配置作业
            map.clear();
            map.put("barcodeList", barcodeList);
            if (dto.getPalletType() == 2){
                String count = mesSfcBarcodeProcessService.countBarcodeListForPOGroup(map);
                if (count != null && Integer.parseInt(count) > 1){
                    dto.setPalletType((byte) 3);
                }
            }
            if (dto.getPalletType() == 3){
                String count = mesSfcBarcodeProcessService.countBarcodeListForSalesOrder(map);
                if (count != null && Integer.parseInt(count) > 1){
                    dto.setPalletType((byte) 1);
                }
            }
            if (dto.getPalletType() == 1){
                // 同料号
                String count = mesSfcBarcodeProcessService.countBarcodeListForMaterial(map);
                if (count != null && Integer.parseInt(count) > 1){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此批条码不属于同个物料，不可提交");
                }
            }

            // 保存条码堆垛关系
            List<WanbaoStackingDet> stackingDets = new ArrayList<>();
            for (WanbaoBarcodeDto wanbaoBarcodeDto : dto.getWanbaoBarcodeDtos()) {
                WanbaoStackingDet stackingDet = new WanbaoStackingDet();
                BeanUtil.copyProperties(wanbaoBarcodeDto, stackingDet);
                stackingDet.setStackingId(stackingList.get(0).getStackingId());
                for (MesSfcWorkOrderBarcodeDto workOrderBarcodeDto : mesSfcWorkOrderBarcodeDtoList){
                    if (workOrderBarcodeDto.getBarcode().equals(wanbaoBarcodeDto.getBarcode())){
                        wanbaoBarcodeDto.setWorkOrderId(workOrderBarcodeDto.getWorkOrderId());
                        break;
                    }
                }
                stackingDets.add(stackingDet);
            }
            wanbaoFeignApi.batchAdd(stackingDets);

            // 完工入库以及上架作业
            this.beforePalletAutoAsnOrder(stackingList.get(0).getStackingId(), user.getOrganizationId(), dto.getWanbaoBarcodeDtos(), barcodeList, mesSfcWorkOrderBarcodeDtoList);

            // A产线，发送堆垛数据至MQ
            if (stackingList.get(0).getProCode().equals("A") && (dto.getIsReadHead() == null || !dto.getIsReadHead())){
                WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
                wanbaoStackingMQDto.setCode(0);
                wanbaoStackingMQDto.setStackingCode(stackingList.get(0).getStackingCode());
                String stackingLine = stackingList.get(0).getStackingCode().substring(stackingList.get(0).getStackingCode().length() - 1);
                wanbaoStackingMQDto.setStackingLine(stackingLine);
                // 发送堆垛号至mq
                rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            }
        }catch (Exception e){
            if (!stackingList.isEmpty() && stackingList.get(0).getProCode().equals("A")){
                WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
                wanbaoStackingMQDto.setCode(500);
                // 发送堆垛号至mq
                rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            }
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
        return 1;
    }


    @Override
    public ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId) {
        String customerBarcode = null;
        if (barcode.length() != 23){
            // 判断是否三星客户条码
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoCheckBarcode");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty()){
                Example example = new Example(MesSfcBarcodeProcess.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike("customerBarcode", barcode + "%");
                List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = mesSfcBarcodeProcessService.selectByExample(example);
                if (!mesSfcBarcodeProcesses.isEmpty()){
                    customerBarcode = barcode;
                    barcode = mesSfcBarcodeProcesses.get(0).getBarcode();
                }
            }
        }
        ScanByManualOperationDto dto = new ScanByManualOperationDto();

        SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode = new SearchMesSfcWorkOrderBarcode();
        searchMesSfcWorkOrderBarcode.setBarcode(barcode);
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList = mesSfcWorkOrderBarcodeService.findList(searchMesSfcWorkOrderBarcode);
        if (mesSfcWorkOrderBarcodeDtoList.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码不存在系统或已被删除");
        }
        BaseLabelCategory labelCategory = baseFeignApi.findLabelCategoryDetail(mesSfcWorkOrderBarcodeDtoList.get(0).getLabelCategoryId()).getData();
        if (labelCategory.getLabelCategoryCode().equals("01")) {
            // 2022-03-08 判断是否质检完成之后走产线入库
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setBarcode(barcode);
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (!inventoryDetDtos.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此条码已入库，不可重复扫码，请检查是否品质重新入库");
            }
            // 产品条码
            Map<String, Object> map = new HashMap<>();
            map.put("barcodeCode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            map.clear();
            map.put("barcode", barcode);
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if (processServiceList.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在，清重新扫码");
            }
            if(!processServiceList.get(0).getProLineId().equals(proLineId)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "配置产线与条码产线不一致");
            }
            if (processServiceList.get(0).getNextProcessId() != 0L){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前条码未完成所有过站，请重新扫码");
            }
            if (!mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                if (customerBarcode != null){
                    dto.setCustomerBarcode(customerBarcode);
                }else {
                    for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList){
                        BaseLabelCategory keyPartLabelCategory = baseFeignApi.findLabelCategoryDetail(keyPartRelevanceDto.getLabelCategoryId()).getData();
                        if (keyPartLabelCategory.getLabelCategoryCode().equals("02")) {
                            // 销售订单条码
                            dto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                        }else if (keyPartLabelCategory.getLabelCategoryCode().equals("03")){
                            // 客户条码
                            dto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                        }
                    }
                }
                dto.setProName(mesSfcKeyPartRelevanceDtoList.get(0).getProName());
            }else {
                BaseProLine baseProLine = baseFeignApi.selectProLinesDetail(proLineId).getData();
                dto.setProName(baseProLine.getProName());
            }
            dto.setBarcode(barcode);
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcWorkOrderBarcodeDtoList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> orderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (orderDtos.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码所属离散任务不存在或已被删除，不可作业");
            }
            dto.setMaterialCode(orderDtos.get(0).getMaterialCode());
            dto.setMaterialDesc(orderDtos.get(0).getMaterialName());
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("partBarcode", barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            if (mesSfcKeyPartRelevanceDtoList.isEmpty()) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码未绑定产品条码");
            }
            if (labelCategory.getLabelCategoryCode().equals("02")) {
                // 销售订单条码
                dto.setSalesBarcode(barcode);
            }else if (labelCategory.getLabelCategoryCode().equals("03")){
                // 客户条码
                dto.setCustomerBarcode(barcode);
            }
            // 2022-03-08 判断是否质检完成之后走产线入库
            SearchWmsInnerInventoryDet searchWmsInnerInventoryDet = new SearchWmsInnerInventoryDet();
            searchWmsInnerInventoryDet.setBarcode(mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            List<WmsInnerInventoryDetDto> inventoryDetDtos = innerFeignApi.findList(searchWmsInnerInventoryDet).getData();
            if (!inventoryDetDtos.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此条码已入库，不可重复扫码，请检查是否品质重新入库");
            }
            map.clear();
            map.put("barcode", mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            List<MesSfcBarcodeProcessDto> processServiceList = mesSfcBarcodeProcessService.findList(map);
            if (processServiceList.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码不存在，清重新扫码");
            }
            if(!processServiceList.get(0).getProLineId().equals(proLineId)){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "配置产线与条码产线不一致");
            }
            if (processServiceList.get(0).getNextProcessId() != 0L){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "当前条码未完成所有过站，请重新扫码");
            }
            dto.setBarcode(mesSfcKeyPartRelevanceDtoList.get(0).getBarcodeCode());
            dto.setProName(mesSfcKeyPartRelevanceDtoList.get(0).getProName());
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(mesSfcKeyPartRelevanceDtoList.get(0).getWorkOrderId());
            List<MesPmWorkOrderDto> orderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
            if (orderDtos.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该条码所属离散任务不存在或已被删除，不可作业");
            }
            dto.setMaterialCode(orderDtos.get(0).getMaterialCode());
            dto.setMaterialDesc(orderDtos.get(0).getMaterialName());
            map.clear();
            map.put("barcodeCode", dto.getBarcode());
            mesSfcKeyPartRelevanceDtoList = mesSfcKeyPartRelevanceService.findList(map);
            for (MesSfcKeyPartRelevanceDto keyPartRelevanceDto : mesSfcKeyPartRelevanceDtoList){
                BaseLabelCategory keyPartLabelCategory = baseFeignApi.findLabelCategoryDetail(keyPartRelevanceDto.getLabelCategoryId()).getData();
                if (labelCategory.getLabelCategoryCode().equals("02") && keyPartLabelCategory.getLabelCategoryCode().equals("03")) {
                    // 客户条码
                    dto.setCustomerBarcode(keyPartRelevanceDto.getPartBarcode());
                }else if (labelCategory.getLabelCategoryCode().equals("03") && keyPartLabelCategory.getLabelCategoryCode().equals("02")){
                    // 销售订单条码
                    dto.setSalesBarcode(keyPartRelevanceDto.getPartBarcode());
                }
            }
        }
        return dto;
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
        Example example = new Example(MesSfcProductPallet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("productPalletId", palletIdList);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductPallet> productPallets = mesSfcProductPalletService.selectByExample(example);
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

            // 2022-03-01 改为不自动提交栈板，有人工堆垛作业触发
            /*// 获取该条码对应的工单信息
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
            }*/
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

    @Override
    public Boolean testStacking(String code) {
        try {
            if (code.equals("500")){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "报错啦！！！");
            }
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(0);
            wanbaoStackingMQDto.setStackingCode("391-1066920304213");
            wanbaoStackingMQDto.setStackingLine("1");
            // 发送堆垛号至mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            return true;
        }catch (Exception e){
            WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(500);
            // 发送堆垛号至mq
            rabbitProducer.sendStacking(JSON.toJSONString(wanbaoStackingMQDto));
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), e.getMessage());
        }
    }

    // region 私有方法

    /**
     * 生成完工入库单
     * @param stackingId 堆垛
     * @param orgId 组织
     * @param wanbaoBarcodeDtos 条码
     */
    private void beforePalletAutoAsnOrder(Long stackingId, Long orgId, List<WanbaoBarcodeDto> wanbaoBarcodeDtos, List<String> barcodeList, List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtoList){

        /**
         * 2022-03-12 bgkun 人工栈板作业提交完工入库以及后续操作
         */
        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("barcodeList", barcodeList);
        map.put("orgId", orgId);
        List<PalletAutoAsnDto> autoAsnDtos = mesSfcProductPalletDetService.findListGroupByWorkOrder(map);
        for (PalletAutoAsnDto palletAutoAsnDto : autoAsnDtos){
            long count = mesSfcWorkOrderBarcodeDtoList.stream().filter(item -> item.getWorkOrderId().equals(palletAutoAsnDto.getSourceOrderId())).count();
            palletAutoAsnDto.setPackingQty(BigDecimal.valueOf(count));
            palletAutoAsnDto.setActualQty(BigDecimal.valueOf(count));
            palletAutoAsnDto.setStackingId(stackingId);
            List<BarPODto> barPODtos = new ArrayList<>();
            wanbaoBarcodeDtos.forEach(item -> {
                if (item.getWorkOrderId().equals(palletAutoAsnDto.getSourceOrderId())){
                    BarPODto barPODto = new BarPODto();
                    barPODto.setBarCode(item.getBarcode());
                    barPODto.setPOCode(palletAutoAsnDto.getSamePackageCode());
                    barPODto.setCutsomerBarcode(item.getCustomerBarcode());
                    barPODto.setSalesBarcode(item.getSalesBarcode());
                    barPODtos.add(barPODto);
                }
            });
            log.info("========= wanbaoBarcodeDtos:" + JSON.toJSONString(wanbaoBarcodeDtos));
            log.info("========= barPODtos:" + JSON.toJSONString(barPODtos));
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

    // endregion
}

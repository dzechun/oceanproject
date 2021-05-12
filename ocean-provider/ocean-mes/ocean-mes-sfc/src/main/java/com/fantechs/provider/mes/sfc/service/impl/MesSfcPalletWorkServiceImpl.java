package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterialPackage;
import com.fantechs.common.base.general.entity.basic.BasePackageSpecification;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
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
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    MesSfcProductPalletDetService mesSfcProductPalletDetService;

    @Resource
    MesSfcProductCartonService mesSfcProductCartonService;

    @Resource
    MesSfcProductCartonDetService mesSfcProductCartonDetService;

    @Resource
    MesSfcBarcodeProcessService mesSfcBarcodeProcessService;

    @Resource
    MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;

    @Resource
    PMFeignApi pmFeignApi;

    @Resource
    BaseFeignApi baseFeignApi;

    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String cartonCode = "";
        // 栈板作业需要绑定的所有产品条码
        List<MesSfcWorkOrderBarcode> mesSfcWorkOrderBarcodeList = new ArrayList<>();
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
            if ((requestPalletWorkScanDto.getPalletType() == 0 && mesPmWorkOrderDto.getWorkOrderId() == mesSfcProductPalletDto.getWorkOrderId())
            || (requestPalletWorkScanDto.getPalletType() == 1 && mesPmWorkOrderDto.getMaterialId() == mesSfcProductPalletDto.getMaterialId())) {
                palletCode = mesSfcProductPalletDto.getPalletCode();
                nowPackageSpecQty = mesSfcProductPalletDto.getNowPackageSpecQty();
                isPallet = false;
                mesSfcProductPallet = mesSfcProductPalletDto;
                palletCartons = findPalletCarton(mesSfcProductPallet.getProductPalletId()).size();
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
            searchBasePackageSpecification.setProcessId(requestPalletWorkScanDto.getProcessId());
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
                    BasePackageSpecification basePackageSpecification = baseFeignApi.BasePackageSpecificationDetail(baseMaterialPackage.getPackageSpecificationId()).getData();
                    if (StringUtils.isEmpty(basePackageSpecification)) {
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "该产品条码没有设置在该工序的包装规格");
                    }
                    nowPackageSpecQty = basePackageSpecification.getPackageSpecificationQuantity();
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
            BarcodeUtils.updateProcess(updateProcessDto);

            // 获取条码对应的过站信息
            MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectOne(MesSfcBarcodeProcess.builder()
                    .barcode(mesSfcWorkOrderBarcode.getBarcode())
                    .build());
            mesSfcBarcodeProcess.setPalletCode(palletCode);
            mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);
            // 获取条码对应的过站记录信息
            MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord = mesSfcBarcodeProcessRecordService.selectOne(MesSfcBarcodeProcessRecord.builder()
                    .barcode(mesSfcWorkOrderBarcode.getBarcode())
                    .build());
            mesSfcBarcodeProcessRecord.setPalletCode(palletCode);
            mesSfcBarcodeProcessRecordService.update(mesSfcBarcodeProcessRecord);
        }

        // 当前工单已关闭栈板
        Map<String, Object> map = new HashMap<>();
        map.put("workOrderId", workOrderId);
        map.put("closeStatus", 1);
        List<MesSfcProductPalletDto> mesSfcProductPalletDtos = mesSfcProductPalletService.findList(map);
        int closePalletNum = mesSfcProductPalletDtos.size();

        // 新增产品栈板信息
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
        }
        // 是否打印栈板码
        if (requestPalletWorkScanDto.getPrintBarcode() == 1) {
            PrintCarCodeDto printCarCodeDto = new PrintCarCodeDto();
            printCarCodeDto.setWorkOrderId(workOrderId);
            printCarCodeDto.setLabelTypeCode("10");
            printCarCodeDto.setBarcode(palletCode);
            BarcodeUtils.printBarCode(printCarCodeDto);
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
            map.clear();
            map.put("workOrderId", mesSfcProductPalletDto.getWorkOrderId());
            List<MesSfcProductCartonDto> mesSfcProductCartonDtoList = mesSfcProductCartonService.findList(map);
            palletWorkScanDto.setScanCartonNum(mesSfcProductCartonDtoList.size());
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
    public int submitNoFullPallet(List<Long> palletIdList) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(MesSfcProductPalletDto.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("productPalletId", palletIdList);
        criteria.andEqualTo("closeStatus", 0);
        List<MesSfcProductPallet> productPallets = mesSfcProductPalletService.selectByExample(example);
        try {
            for (MesSfcProductPallet item : productPallets){
                item.setCloseStatus((byte) 1);
                item.setModifiedUserId(user.getUserId());
                item.setModifiedTime(new Date());
                mesSfcProductPalletService.update(item);
            }
        }catch (Exception e){
            throw new BizErrorException(ErrorCodeEnum.GL99990005, e.getMessage());
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
    public int updateNowPackageSpecQty(Long productPalletId, Integer nowPackageSpecQty) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int palletCartons = findPalletCarton(productPalletId).size();
        if (nowPackageSpecQty < palletCartons) {
            throw new BizErrorException(ErrorCodeEnum.GL99990500, "修改栈板包装规格数量不能小于栈板已绑定包箱数量");
        }
        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
        mesSfcProductPallet.setProductPalletId(productPalletId);
        mesSfcProductPallet.setNowPackageSpecQty(BigDecimal.valueOf(nowPackageSpecQty));
        if (nowPackageSpecQty == palletCartons) {
            mesSfcProductPallet.setCloseStatus((byte) 1);
            mesSfcProductPallet.setClosePalletUserId(user.getUserId());
            mesSfcProductPallet.setClosePalletTime(new Date());
        }
        mesSfcProductPallet.setModifiedTime(new Date());
        mesSfcProductPallet.setModifiedUserId(user.getUserId());
        mesSfcProductPalletService.update(mesSfcProductPallet);

        return 1;
    }
}

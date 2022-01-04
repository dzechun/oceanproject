package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCarton;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductCartonDet;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPallet;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCartonPalletInfoDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCheckBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombinePrintDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCartonDet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPalletDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerSplitAndCombineLog;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerSplitAndCombineLogMapper;
import com.fantechs.provider.wms.inner.service.PDAWmsInnerSplitAndCombineCartonPalletService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Service
public class PDAWmsInnerSplitAndCombineCartonPalletCartonPalletServiceImpl implements PDAWmsInnerSplitAndCombineCartonPalletService {

    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private WmsInnerSplitAndCombineLogMapper wmsInnerSplitAndCombineLogMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private RedisUtil redisUtil;


    @Override
    public PDAWmsInnerSplitAndCombineCartonPalletInfoDto getCartonPalletInfo(String barcode, Byte type) {
        //1、包箱 2、栈板
        PDAWmsInnerSplitAndCombineCartonPalletInfoDto cartonPalletInfoDto = new PDAWmsInnerSplitAndCombineCartonPalletInfoDto();
        Map<String,Object> map = new HashMap<>();
        if(type == 1){
            map.put("cartonCode",barcode);
        }else if(type == 2){
            map.put("palletCode",barcode);
        }
        List<WmsInnerInventoryDetDto> inventoryDetDtoList = wmsInnerInventoryDetMapper.findList(map);
        if(StringUtils.isEmpty(inventoryDetDtoList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该包箱/栈板信息");
        }

        List<WmsInnerInventoryDetDto> detDtos = new LinkedList<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtoList){
            if(type == (byte)1){
                if(StringUtils.isEmpty(wmsInnerInventoryDetDto.getColorBoxCode())
                        &&StringUtils.isEmpty(wmsInnerInventoryDetDto.getBarcode())){
                    cartonPalletInfoDto.setCartonPalletInventoryDetDto(wmsInnerInventoryDetDto);
                }else {
                    detDtos.add(wmsInnerInventoryDetDto);
                }
            }else if(type == (byte)2){
                if(StringUtils.isEmpty(wmsInnerInventoryDetDto.getCartonCode())
                        &&StringUtils.isEmpty(wmsInnerInventoryDetDto.getColorBoxCode())
                        &&StringUtils.isEmpty(wmsInnerInventoryDetDto.getBarcode())){
                    cartonPalletInfoDto.setCartonPalletInventoryDetDto(wmsInnerInventoryDetDto);
                }else {
                    detDtos.add(wmsInnerInventoryDetDto);
                }
            }
        }
        cartonPalletInfoDto.setNextLevelInventoryDetDtos(detDtos);

        return cartonPalletInfoDto;
    }

    @Override
    public WmsInnerInventoryDetDto checkBarcode(PDAWmsInnerSplitAndCombineCheckBarcodeDto pdaWmsInnerSplitAndCombineCheckBarcodeDto) {
        String cartonPalletCode = pdaWmsInnerSplitAndCombineCheckBarcodeDto.getCartonPalletCode();
        String barcode = pdaWmsInnerSplitAndCombineCheckBarcodeDto.getBarcode();
        Byte barcodeType = pdaWmsInnerSplitAndCombineCheckBarcodeDto.getBarcodeType();
        Byte storageType = pdaWmsInnerSplitAndCombineCheckBarcodeDto.getStorageType();
        Byte type = pdaWmsInnerSplitAndCombineCheckBarcodeDto.getType();

        Map<String,Object> map = new HashMap<>();
        if(type == (byte)1){
            map.put("cartonCode",cartonPalletCode);
        }else if(type == (byte)2){
            map.put("palletCode",cartonPalletCode);
        }
        List<WmsInnerInventoryDetDto> inventoryDetDtoList = wmsInnerInventoryDetMapper.findList(map);
        if(StringUtils.isEmpty(inventoryDetDtoList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该包箱/栈板信息");
        }
        List<WmsInnerInventoryDetDto> nextLevelInventoryDetDtos = new LinkedList<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtoList){
            if(type == (byte)1&&StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getBarcode())){
                nextLevelInventoryDetDtos.add(wmsInnerInventoryDetDto);
            }
            if(type == (byte)2&&StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getBarcode())){
                nextLevelInventoryDetDtos.add(wmsInnerInventoryDetDto);
            }
        }

        //所扫条码信息
        WmsInnerInventoryDetDto scanInventoryDetDto = null;
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : nextLevelInventoryDetDtos){
            if(type == (byte)2){
                if(barcode.equals(wmsInnerInventoryDetDto.getCartonCode())){
                    scanInventoryDetDto = wmsInnerInventoryDetDto;
                    break;
                }
            }
            if(barcode.equals(wmsInnerInventoryDetDto.getColorBoxCode())){
                scanInventoryDetDto = wmsInnerInventoryDetDto;
                break;
            }else if (barcode.equals(wmsInnerInventoryDetDto.getBarcode())){
                scanInventoryDetDto = wmsInnerInventoryDetDto;
                break;
            }
        }
        if(scanInventoryDetDto == null){
            throw new BizErrorException("该条码不属于该包箱/栈板");
        }

        if(scanInventoryDetDto.getStorageType()==(byte)3){
            throw new BizErrorException("不能扫描发货库位中的条码");
        }
        if(barcodeType!=null&&!barcodeType.equals(scanInventoryDetDto.getBarcodeType())){
            throw new BizErrorException("所扫条码类型需一致");
        }
        if(storageType!=null&&!storageType.equals(scanInventoryDetDto.getStorageType())){
            throw new BizErrorException("所扫条码需存放在相同类型的库位中");
        }

        return scanInventoryDetDto;
    }

    @Override
    public BaseStorage checkStorageCode(String storageCode){
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode(storageCode);
        List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
        if(StringUtils.isEmpty(baseStorages)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该库位信息");
        }
        BaseStorage baseStorage = baseStorages.get(0);
        if(baseStorage.getStorageType()!=(byte)1){
            throw new BizErrorException("该库位非存货库位");
        }

        return baseStorage;
    }


    public BaseBarcodeRule findBarcodeRule(Long materialId,Byte type){
        Long barcodeRuleId = null;

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(materialId);
        List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
        if(StringUtils.isEmpty(baseMaterials)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该物料信息");
        }
        BaseMaterial baseMaterial = baseMaterials.get(0);
        if(StringUtils.isEmpty(baseMaterial.getBarcodeRuleSetId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"该物料未绑定条码规则集合");
        }
        SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet = new SearchBaseBarcodeRuleSetDet();
        searchBaseBarcodeRuleSetDet.setBarcodeRuleSetId(baseMaterial.getBarcodeRuleSetId());
        List<BaseBarcodeRuleSetDetDto> barcodeRuleSetDetDtos = baseFeignApi.findBarcodeRuleSetDetList(searchBaseBarcodeRuleSetDet).getData();
        if(StringUtils.isEmpty(barcodeRuleSetDetDtos)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"条码规则集合未绑定条码规则");
        }


        for (BaseBarcodeRuleSetDetDto baseBarcodeRuleSetDetDto : barcodeRuleSetDetDtos){
            if(type == (byte)1) {//找箱码规则
                if ("09".equals(baseBarcodeRuleSetDetDto.getBarcodeRruleCategoryCode())) {
                    barcodeRuleId = baseBarcodeRuleSetDetDto.getBarcodeRuleId();
                    break;
                }
            }else if(type == (byte)2) {//找栈板码规则
                if ("10".equals(baseBarcodeRuleSetDetDto.getBarcodeRruleCategoryCode())) {
                    barcodeRuleId = baseBarcodeRuleSetDetDto.getBarcodeRuleId();
                    break;
                }
            }
        }
        if(StringUtils.isEmpty(barcodeRuleId)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"条码规则集合未绑定包箱条码规则");
        }
        BaseBarcodeRule barcodeRule = baseFeignApi.baseBarcodeRuleDetail(barcodeRuleId).getData();

        return barcodeRule;
    }


    public String generatorCartonPalletCode(BaseBarcodeRule barcodeRule,Long materialId){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(materialId);
        List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
        if(StringUtils.isEmpty(baseMaterials)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该物料信息");
        }
        BaseMaterial baseMaterial = baseMaterials.get(0);

        // 获取规则配置列表
        SearchBaseBarcodeRuleSpec baseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        baseBarcodeRuleSpec.setBarcodeRuleId(barcodeRule.getBarcodeRuleId());
        ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecResponseEntity = baseFeignApi.findSpec(baseBarcodeRuleSpec);
        if (barcodeRuleSpecResponseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012022);
        }
        List<BaseBarcodeRuleSpec> barcodeRuleSpecList = barcodeRuleSpecResponseEntity.getData();
        if (barcodeRuleSpecList.isEmpty()) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012023);
        }

        String lastBarCode = null;
        boolean hasKey = redisUtil.hasKey(barcodeRule.getBarcodeRule());
        if (hasKey) {
            // 从redis获取上次生成条码
            Object redisRuleData = redisUtil.get(barcodeRule.getBarcodeRule());
            lastBarCode = String.valueOf(redisRuleData);
        }
        //获取最大流水号
        String maxCode = baseFeignApi.generateMaxCode(barcodeRuleSpecList, lastBarCode).getData();
        //生成条码
        ResponseEntity<String> rs = baseFeignApi.generateCode(barcodeRuleSpecList, maxCode, baseMaterial.getMaterialCode(), materialId.toString());
        if (rs.getCode() != 0) {
            throw new BizErrorException(rs.getMessage());
        }
        // 更新redis最新包箱/栈板号
        redisUtil.set(barcodeRule.getBarcodeRule(), rs.getData());

        return rs.getData();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String print(PDAWmsInnerSplitAndCombinePrintDto pdaWmsInnerSplitAndCombinePrintDto){
        Long materialId = pdaWmsInnerSplitAndCombinePrintDto.getMaterialId();
        Long storageId = pdaWmsInnerSplitAndCombinePrintDto.getStorageId();
        List<Long> materialBarcodeIdList = pdaWmsInnerSplitAndCombinePrintDto.getMaterialBarcodeIdList();
        Byte type = pdaWmsInnerSplitAndCombinePrintDto.getType();
        List<String> cartonPalletCodes = pdaWmsInnerSplitAndCombinePrintDto.getCartonPalletCodes();
        BigDecimal packageSpecificationQuantity = pdaWmsInnerSplitAndCombinePrintDto.getPackageSpecificationQuantity();

        //查询包箱/栈板信息
        List<PDAWmsInnerSplitAndCombineCartonPalletInfoDto> cartonPalletInfoDtos = new LinkedList<>();
        for (String cartonPalletCode : cartonPalletCodes){
            PDAWmsInnerSplitAndCombineCartonPalletInfoDto cartonPalletInfo = getCartonPalletInfo(cartonPalletCode, type == (byte) 1 || type == (byte) 2 ? (byte) 1 : (byte) 2);
            cartonPalletInfoDtos.add(cartonPalletInfo);
        }

        String barcodes = null;
        //获取生成规则
        BaseBarcodeRule barcodeRule = null;
        if(type == (byte)1 || type == (byte)2) {
            barcodeRule = findBarcodeRule(materialId, (byte) 1);
        }else if(type == (byte)3 || type == (byte)4){
            barcodeRule = findBarcodeRule(materialId, (byte) 2);
        }

        if(type == (byte)1||type == (byte)3) {//分包箱（栈板）
            String sourceCartonPalletCode = generatorCartonPalletCode(barcodeRule, materialId);
            String newCartonPalletCode = generatorCartonPalletCode(barcodeRule, materialId);
            barcodes = sourceCartonPalletCode + "," + sourceCartonPalletCode;

            PDAWmsInnerSplitAndCombineCartonPalletInfoDto cartonPalletInfoDto = cartonPalletInfoDtos.get(0);
            List<WmsInnerInventoryDetDto> nextLevelInventoryDetDtos = cartonPalletInfoDto.getNextLevelInventoryDetDtos();
            WmsInnerInventoryDetDto cartonPalletInventoryDetDto = cartonPalletInfoDto.getCartonPalletInventoryDetDto();

            //源条码与扫描条码分组
            List<WmsInnerInventoryDetDto> sourceInventoryDetDtos = new LinkedList<>();
            List<WmsInnerInventoryDetDto> newInventoryDetDtos = new LinkedList<>();
            for (WmsInnerInventoryDetDto nextLevelInventoryDetDto : nextLevelInventoryDetDtos) {
                if (materialBarcodeIdList.contains(nextLevelInventoryDetDto.getMaterialBarcodeId())) {
                    newInventoryDetDtos.add(nextLevelInventoryDetDto);
                } else {
                    sourceInventoryDetDtos.add(nextLevelInventoryDetDto);
                }
            }

            //修改源包箱（栈板）信息
            BigDecimal sourceMaterialQty = BigDecimal.ZERO;
            StringBuilder sourceMaterialBarcodeId = new StringBuilder();
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : sourceInventoryDetDtos) {
                sourceMaterialBarcodeId.append(wmsInnerInventoryDetDto.getMaterialBarcodeId()).append(",");
                wmsInnerInventoryDetDto.setStorageId(storageId);
                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDetDto);
            }
            String sourceMaterialBarcodeIds = sourceMaterialBarcodeId.substring(0, sourceMaterialBarcodeId.length() - 1);
            List<WmsInnerMaterialBarcode> sourceMaterialBarcodes = wmsInnerMaterialBarcodeMapper.selectByIds(sourceMaterialBarcodeIds);
            for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : sourceMaterialBarcodes) {
                sourceMaterialQty = sourceMaterialQty.add(wmsInnerMaterialBarcode.getMaterialQty());
                if(type==(byte)1){
                    wmsInnerMaterialBarcode.setCartonCode(sourceCartonPalletCode);
                }else if(type==(byte)3){
                    wmsInnerMaterialBarcode.setPalletCode(sourceCartonPalletCode);
                }
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
            }

            cartonPalletInventoryDetDto.setStorageId(storageId);
            wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(cartonPalletInventoryDetDto);
            WmsInnerMaterialBarcode sourceMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(cartonPalletInventoryDetDto.getMaterialBarcodeId());
            if(type==(byte)1) {
                sourceMaterialBarcode.setCartonCode(sourceCartonPalletCode);
            }else if(type==(byte)3){
                sourceMaterialBarcode.setPalletCode(sourceCartonPalletCode);
            }
            sourceMaterialBarcode.setMaterialQty(sourceMaterialQty);
            wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(sourceMaterialBarcode);


            //修改新包箱（栈板）信息
            BigDecimal newMaterialQty = BigDecimal.ZERO;
            StringBuilder newMaterialBarcodeId = new StringBuilder();
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : newInventoryDetDtos) {
                newMaterialBarcodeId.append(wmsInnerInventoryDetDto.getMaterialBarcodeId()).append(",");
                wmsInnerInventoryDetDto.setStorageId(storageId);
                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDetDto);
            }
            String newMaterialBarcodeIds = newMaterialBarcodeId.substring(0, newMaterialBarcodeId.length() - 1);
            List<WmsInnerMaterialBarcode> newMaterialBarcodes = wmsInnerMaterialBarcodeMapper.selectByIds(newMaterialBarcodeIds);
            for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : newMaterialBarcodes) {
                newMaterialQty = newMaterialQty.add(wmsInnerMaterialBarcode.getMaterialQty());
                if(type==(byte)1) {
                    wmsInnerMaterialBarcode.setCartonCode(newCartonPalletCode);
                }else if(type==(byte)3){
                    wmsInnerMaterialBarcode.setPalletCode(newCartonPalletCode);
                }
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
            }

            WmsInnerMaterialBarcode newMaterialBarcode = new WmsInnerMaterialBarcode();
            BeanUtils.copyProperties(sourceMaterialBarcode, newMaterialBarcode);
            newMaterialBarcode.setMaterialBarcodeId(null);
            if(type==(byte)1) {
                newMaterialBarcode.setCartonCode(newCartonPalletCode);
            } else if(type==(byte)3){
                newMaterialBarcode.setPalletCode(newCartonPalletCode);
            }
            newMaterialBarcode.setMaterialQty(newMaterialQty);
            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(newMaterialBarcode);

            WmsInnerInventoryDetDto newCartonInventoryDetDto = new WmsInnerInventoryDetDto();
            BeanUtils.copyProperties(cartonPalletInventoryDetDto, newCartonInventoryDetDto);
            newCartonInventoryDetDto.setInventoryDetId(null);
            newCartonInventoryDetDto.setMaterialBarcodeId(newMaterialBarcode.getMaterialBarcodeId());
            newCartonInventoryDetDto.setStorageId(storageId);
            wmsInnerInventoryDetMapper.insertSelective(newCartonInventoryDetDto);

            //是否返写mes
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("IfWriteBackMES");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isNotEmpty(sysSpecItemList)&&Integer.parseInt(sysSpecItemList.get(0).getParaValue())==1){
                if(type == (byte)1) {
                    SearchMesSfcProductCarton searchMesSfcProductCarton = new SearchMesSfcProductCarton();
                    searchMesSfcProductCarton.setCartonCode(cartonPalletCodes.get(0));
                    List<MesSfcProductCartonDto> productCartonDtos = sfcFeignApi.findProductCartonList(searchMesSfcProductCarton).getData();
                    if(StringUtils.isNotEmpty(productCartonDtos)) {
                        MesSfcProductCartonDto mesSfcProductCartonDto = productCartonDtos.get(0);
                        SearchMesSfcProductCartonDet searchMesSfcProductCartonDet = new SearchMesSfcProductCartonDet();
                        searchMesSfcProductCartonDet.setProductCartonId(mesSfcProductCartonDto.getProductCartonId());
                        List<MesSfcProductCartonDetDto> productCartonDetDtos = sfcFeignApi.findList(searchMesSfcProductCartonDet).getData();

                        List<MesSfcProductCartonDet> cartonDets = new LinkedList<>();
                        for (MesSfcProductCartonDetDto mesSfcProductCartonDetDto : productCartonDetDtos){
                            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : newInventoryDetDtos){
                                if(mesSfcProductCartonDetDto.getBarcode().equals(wmsInnerInventoryDetDto.getBarcode())){
                                    cartonDets.add(mesSfcProductCartonDetDto);
                                }
                            }
                        }
                        MesSfcProductCarton mesSfcProductCarton = new MesSfcProductCarton();
                        BeanUtils.copyProperties(mesSfcProductCartonDto,mesSfcProductCarton);
                        mesSfcProductCarton.setProductCartonId(null);
                        mesSfcProductCarton.setCartonCode(newCartonPalletCode);
                        mesSfcProductCarton.setNowPackageSpecQty(new BigDecimal(newInventoryDetDtos.size()));
                        mesSfcProductCarton.setCartonDets(cartonDets);
                        sfcFeignApi.add(mesSfcProductCarton);

                        mesSfcProductCartonDto.setCartonCode(sourceCartonPalletCode);
                        mesSfcProductCartonDto.setNowPackageSpecQty(new BigDecimal(sourceInventoryDetDtos.size()));
                        sfcFeignApi.update(mesSfcProductCartonDto);
                    }
                }else if(type == (byte)3){
                    SearchMesSfcProductPallet searchMesSfcProductPallet = new SearchMesSfcProductPallet();
                    searchMesSfcProductPallet.setPalletCode(cartonPalletCodes.get(0));
                    List<MesSfcProductPalletDto> productPalletDtos = sfcFeignApi.findProductPalletList(searchMesSfcProductPallet).getData();
                    if(StringUtils.isNotEmpty(productPalletDtos)) {
                        MesSfcProductPalletDto mesSfcProductPalletDto = productPalletDtos.get(0);
                        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet = new SearchMesSfcProductPalletDet();
                        searchMesSfcProductPalletDet.setProductPalletId(mesSfcProductPalletDto.getProductPalletId());
                        List<MesSfcProductPalletDetDto> productPalletDetDtos = sfcFeignApi.findList(searchMesSfcProductPalletDet).getData();

                        List<MesSfcProductPalletDet> palletDets = new LinkedList<>();
                        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : productPalletDetDtos){
                            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : newInventoryDetDtos){
                                if(mesSfcProductPalletDetDto.getCartonCode().equals(wmsInnerInventoryDetDto.getCartonCode())){
                                    palletDets.add(mesSfcProductPalletDetDto);
                                }
                            }
                        }
                        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
                        BeanUtils.copyProperties(mesSfcProductPalletDto,mesSfcProductPallet);
                        mesSfcProductPallet.setProductPalletId(null);
                        mesSfcProductPallet.setPalletCode(newCartonPalletCode);
                        mesSfcProductPallet.setNowPackageSpecQty(new BigDecimal(newInventoryDetDtos.size()));
                        mesSfcProductPallet.setPalletDets(palletDets);
                        sfcFeignApi.add(mesSfcProductPallet);

                        mesSfcProductPalletDto.setPalletCode(sourceCartonPalletCode);
                        mesSfcProductPalletDto.setNowPackageSpecQty(new BigDecimal(sourceInventoryDetDtos.size()));
                        sfcFeignApi.update(mesSfcProductPalletDto);
                    }
                }

            }
        }else if(type == (byte)2||type == (byte)4){//合包箱（栈板）
            String newCartonPalletCode = generatorCartonPalletCode(barcodeRule, materialId);
            barcodes = newCartonPalletCode;

            List<WmsInnerInventoryDetDto> nextLevelInventoryDetDtos = new LinkedList<>();
            List<Long> toDeleteInventoryDetIdList = new LinkedList<>();
            List<Long> toDeleteMaterialBarcodeIdList = new LinkedList<>();
            for (PDAWmsInnerSplitAndCombineCartonPalletInfoDto cartonPalletInfoDto:cartonPalletInfoDtos){
                List<WmsInnerInventoryDetDto> InventoryDetDtos = cartonPalletInfoDto.getNextLevelInventoryDetDtos();
                nextLevelInventoryDetDtos.addAll(InventoryDetDtos);

                WmsInnerInventoryDetDto cartonPalletInventoryDetDto = cartonPalletInfoDto.getCartonPalletInventoryDetDto();
                toDeleteInventoryDetIdList.add(cartonPalletInventoryDetDto.getInventoryDetId());
                toDeleteMaterialBarcodeIdList.add(cartonPalletInventoryDetDto.getMaterialBarcodeId());
            }

            //下级条码信息修改
            BigDecimal totalMaterialQty = BigDecimal.ZERO;
            StringBuilder materialBarcodeId = new StringBuilder();
            for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : nextLevelInventoryDetDtos) {
                materialBarcodeId.append(wmsInnerInventoryDetDto.getMaterialBarcodeId()).append(",");
                wmsInnerInventoryDetDto.setStorageId(storageId);
                wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDetDto);
            }
            String materialBarcodeIds = materialBarcodeId.substring(0, materialBarcodeId.length() - 1);
            List<WmsInnerMaterialBarcode> materialBarcodes = wmsInnerMaterialBarcodeMapper.selectByIds(materialBarcodeIds);
            for (WmsInnerMaterialBarcode wmsInnerMaterialBarcode : materialBarcodes) {
                totalMaterialQty = totalMaterialQty.add(wmsInnerMaterialBarcode.getMaterialQty());
                if(type==(byte)2) {
                    wmsInnerMaterialBarcode.setCartonCode(newCartonPalletCode);
                }else if(type==(byte)4) {
                    wmsInnerMaterialBarcode.setPalletCode(newCartonPalletCode);
                }
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
            }

            //新增包箱（栈板）信息
            WmsInnerInventoryDetDto oldInventoryDetDto = cartonPalletInfoDtos.get(0).getCartonPalletInventoryDetDto();
            WmsInnerMaterialBarcode newMaterialBarcode = new WmsInnerMaterialBarcode();
            WmsInnerMaterialBarcode oldMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(oldInventoryDetDto.getMaterialBarcodeId());
            BeanUtils.copyProperties(oldMaterialBarcode, newMaterialBarcode);
            newMaterialBarcode.setMaterialBarcodeId(null);
            if(type==(byte)2) {
                newMaterialBarcode.setCartonCode(newCartonPalletCode);
            }else if(type==(byte)4) {
                newMaterialBarcode.setPalletCode(newCartonPalletCode);
            }
            newMaterialBarcode.setMaterialQty(totalMaterialQty);
            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(newMaterialBarcode);

            WmsInnerInventoryDetDto newInventoryDetDto = new WmsInnerInventoryDetDto();
            BeanUtils.copyProperties(oldInventoryDetDto, newInventoryDetDto);
            newInventoryDetDto.setInventoryDetId(null);
            newInventoryDetDto.setMaterialBarcodeId(newMaterialBarcode.getMaterialBarcodeId());
            newInventoryDetDto.setStorageId(storageId);
            wmsInnerInventoryDetMapper.insertSelective(newInventoryDetDto);

            //删除源包箱（栈板）信息
            Example inventoryDetExample = new Example(WmsInnerInventoryDet.class);
            inventoryDetExample.createCriteria().andIn("inventoryDetId",toDeleteInventoryDetIdList);
            wmsInnerInventoryDetMapper.deleteByExample(inventoryDetExample);
            Example materialBarcodeExample = new Example(WmsInnerMaterialBarcode.class);
            materialBarcodeExample.createCriteria().andIn("materialBarcodeId",toDeleteMaterialBarcodeIdList);
            wmsInnerMaterialBarcodeMapper.deleteByExample(materialBarcodeExample);

            //是否返写mes
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("IfWriteBackMES");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isNotEmpty(sysSpecItemList)&&Integer.parseInt(sysSpecItemList.get(0).getParaValue())==1){
                if(type == (byte)2) {
                    SearchMesSfcProductCarton searchMesSfcProductCarton1 = new SearchMesSfcProductCarton();
                    searchMesSfcProductCarton1.setCartonCode(cartonPalletCodes.get(0));
                    List<MesSfcProductCartonDto> productCartonDtos1 = sfcFeignApi.findProductCartonList(searchMesSfcProductCarton1).getData();
                    SearchMesSfcProductCarton searchMesSfcProductCarton2 = new SearchMesSfcProductCarton();
                    searchMesSfcProductCarton2.setCartonCode(cartonPalletCodes.get(1));
                    List<MesSfcProductCartonDto> productCartonDtos2 = sfcFeignApi.findProductCartonList(searchMesSfcProductCarton2).getData();
                    if(StringUtils.isNotEmpty(productCartonDtos1,productCartonDtos2)) {
                        SearchMesSfcProductCartonDet searchMesSfcProductCartonDet1 = new SearchMesSfcProductCartonDet();
                        searchMesSfcProductCartonDet1.setProductCartonId(productCartonDtos1.get(0).getProductCartonId());
                        List<MesSfcProductCartonDetDto> productCartonDetDtos1 = sfcFeignApi.findList(searchMesSfcProductCartonDet1).getData();
                        SearchMesSfcProductCartonDet searchMesSfcProductCartonDet2 = new SearchMesSfcProductCartonDet();
                        searchMesSfcProductCartonDet2.setProductCartonId(productCartonDtos2.get(0).getProductCartonId());
                        List<MesSfcProductCartonDetDto> productCartonDetDtos2 = sfcFeignApi.findList(searchMesSfcProductCartonDet2).getData();

                        //新增
                        List<MesSfcProductCartonDet> cartonDets = new LinkedList<>();
                        cartonDets.addAll(productCartonDetDtos1);
                        cartonDets.addAll(productCartonDetDtos2);

                        MesSfcProductCarton mesSfcProductCarton = new MesSfcProductCarton();
                        MesSfcProductCartonDto mesSfcProductCartonDto = productCartonDtos1.get(0);
                        BeanUtils.copyProperties(mesSfcProductCartonDto,mesSfcProductCarton);
                        mesSfcProductCarton.setProductCartonId(null);
                        mesSfcProductCarton.setCartonCode(newCartonPalletCode);
                        BigDecimal add = productCartonDtos1.get(0).getNowPackageSpecQty().add(productCartonDtos2.get(0).getNowPackageSpecQty());
                        mesSfcProductCarton.setNowPackageSpecQty(add);
                        mesSfcProductCarton.setCartonDets(cartonDets);
                        sfcFeignApi.add(mesSfcProductCartonDto);
                        //删除原包箱
                        sfcFeignApi.deleteProductCartons(productCartonDtos1.get(0).getProductCartonId()+","+productCartonDtos2.get(0).getProductCartonId());
                    }
                }else if(type == (byte)4){
                    SearchMesSfcProductPallet searchMesSfcProductPallet1 = new SearchMesSfcProductPallet();
                    searchMesSfcProductPallet1.setPalletCode(cartonPalletCodes.get(0));
                    List<MesSfcProductPalletDto> productPalletDtos1 = sfcFeignApi.findProductPalletList(searchMesSfcProductPallet1).getData();
                    SearchMesSfcProductPallet searchMesSfcProductPallet2 = new SearchMesSfcProductPallet();
                    searchMesSfcProductPallet2.setPalletCode(cartonPalletCodes.get(1));
                    List<MesSfcProductPalletDto> productPalletDtos2 = sfcFeignApi.findProductPalletList(searchMesSfcProductPallet2).getData();
                    if(StringUtils.isNotEmpty(productPalletDtos1,productPalletDtos2)) {
                        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet1 = new SearchMesSfcProductPalletDet();
                        searchMesSfcProductPalletDet1.setProductPalletId(productPalletDtos1.get(0).getProductPalletId());
                        List<MesSfcProductPalletDetDto> productPalletDetDtos1 = sfcFeignApi.findList(searchMesSfcProductPalletDet1).getData();
                        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet2 = new SearchMesSfcProductPalletDet();
                        searchMesSfcProductPalletDet2.setProductPalletId(productPalletDtos2.get(0).getProductPalletId());
                        List<MesSfcProductPalletDetDto> productPalletDetDtos2 = sfcFeignApi.findList(searchMesSfcProductPalletDet2).getData();

                        //新增
                        List<MesSfcProductPalletDet> palletDets = new LinkedList<>();
                        palletDets.addAll(productPalletDetDtos1);
                        palletDets.addAll(productPalletDetDtos2);

                        MesSfcProductPallet mesSfcProductPallet = new MesSfcProductPallet();
                        MesSfcProductPalletDto mesSfcProductPalletDto = productPalletDtos1.get(0);
                        BeanUtils.copyProperties(mesSfcProductPalletDto,mesSfcProductPallet);
                        mesSfcProductPallet.setProductPalletId(null);
                        mesSfcProductPallet.setPalletCode(newCartonPalletCode);
                        BigDecimal add = productPalletDtos1.get(0).getNowPackageSpecQty().add(productPalletDtos2.get(0).getNowPackageSpecQty());
                        mesSfcProductPallet.setNowPackageSpecQty(add);
                        mesSfcProductPallet.setPalletDets(palletDets);
                        sfcFeignApi.add(mesSfcProductPallet);
                        //删除原栈板
                        sfcFeignApi.deleteProductCartons(productPalletDtos1.get(0).getProductPalletId()+","+productPalletDtos2.get(0).getProductPalletId());
                    }
                }
            }
        }

        //打印
        SearchBaseLabel searchBaseLabel = new SearchBaseLabel();
        searchBaseLabel.setLabelCategoryName(type == (byte)1 || type == (byte)2 ? "包箱条码" : "栈板条码");
        List<BaseLabelDto> labelList = baseFeignApi.findLabelList(searchBaseLabel).getData();
        if (StringUtils.isEmpty(labelList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"标签模板不存在");
        }
        BaseLabelDto baseLabelDto = labelList.get(0);

        List<PrintModel> printModelList = new ArrayList<>();
        String[] split = barcodes.split(",");
        for (String s : split){
            PrintModel printModel = new PrintModel();
            printModel.setSize(1);
            printModel.setQrCode(s);
            printModelList.add(printModel);
        }
        PrintDto printDto = new PrintDto();
        printDto.setLabelName(baseLabelDto.getLabelName());
        printDto.setLabelVersion(baseLabelDto.getLabelVersion());
        //printDto.setPrintName(printName);
        printDto.setPrintModelList(printModelList);
        sfcFeignApi.print(printDto);

        //记录日志
        List<WmsInnerSplitAndCombineLog> logs = new LinkedList<>();
        for (PDAWmsInnerSplitAndCombineCartonPalletInfoDto cartonPalletInfoDto:cartonPalletInfoDtos) {
            WmsInnerInventoryDetDto cartonPalletInventoryDetDto = cartonPalletInfoDto.getCartonPalletInventoryDetDto();

            WmsInnerSplitAndCombineLog wmsInnerSplitAndCombineLog = new WmsInnerSplitAndCombineLog();
            wmsInnerSplitAndCombineLog.setOperatorType(type);
            wmsInnerSplitAndCombineLog.setDataType((byte)1);
            wmsInnerSplitAndCombineLog.setDataType((byte) 2);
            wmsInnerSplitAndCombineLog.setBarcode(cartonPalletInventoryDetDto.getBarcode());
            wmsInnerSplitAndCombineLog.setColorBoxCode(cartonPalletInventoryDetDto.getColorBoxCode());
            wmsInnerSplitAndCombineLog.setCartonCode(cartonPalletInventoryDetDto.getCartonCode());
            wmsInnerSplitAndCombineLog.setPalletCode(cartonPalletInventoryDetDto.getPalletCode());
            wmsInnerSplitAndCombineLog.setMaterialBarcodeId(cartonPalletInventoryDetDto.getMaterialBarcodeId());
            wmsInnerSplitAndCombineLog.setMaterialId(cartonPalletInventoryDetDto.getMaterialId());
            wmsInnerSplitAndCombineLog.setMaterialQty(cartonPalletInventoryDetDto.getMaterialQty());
            wmsInnerSplitAndCombineLog.setBarcodeRuleId(barcodeRule.getBarcodeRuleId());
            wmsInnerSplitAndCombineLog.setLabelId(baseLabelDto.getLabelId());
            logs.add(wmsInnerSplitAndCombineLog);
        }
        wmsInnerSplitAndCombineLogMapper.insertList(logs);

        return barcodes;
    }
}

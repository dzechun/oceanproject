package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCartonPalletInfoDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCheckBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombinePrintDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
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
    private BaseFeignApi baseFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
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
                if(wmsInnerInventoryDetDto.getColorBoxCode()==null&&wmsInnerInventoryDetDto.getBarcode()==null){
                    cartonPalletInfoDto.setCartonPalletInventoryDetDto(wmsInnerInventoryDetDto);
                }else {
                    detDtos.add(wmsInnerInventoryDetDto);
                }
            }else if(type == (byte)2){
                if(wmsInnerInventoryDetDto.getColorBoxCode()==null&&wmsInnerInventoryDetDto.getBarcode()==null&&wmsInnerInventoryDetDto.getCartonCode()==null){
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
        if(type == 1){
            map.put("cartonCode",cartonPalletCode);
        }else if(type == 2){
            map.put("palletCode",cartonPalletCode);
        }
        List<WmsInnerInventoryDetDto> inventoryDetDtoList = wmsInnerInventoryDetMapper.findList(map);
        List<WmsInnerInventoryDetDto> nextLevelInventoryDetDtos = new LinkedList<>();
        for (WmsInnerInventoryDetDto wmsInnerInventoryDetDto : inventoryDetDtoList){
            if(type == 1 && wmsInnerInventoryDetDto.getBarcodeType() != 3){
                nextLevelInventoryDetDtos.add(wmsInnerInventoryDetDto);
            }
            if(type == 2 && wmsInnerInventoryDetDto.getBarcodeType() != 4){
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

    public void printBarcode(String barcodes,Byte type){
        SearchBaseLabel searchBaseLabel = new SearchBaseLabel();
        searchBaseLabel.setLabelCategoryName(type == (byte)1 || type == (byte)2 ? "包箱条码" : "栈板条码");
        List<BaseLabelDto> labelList = baseFeignApi.findLabelList(searchBaseLabel).getData();
        if (StringUtils.isEmpty(labelList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"标签模板不存在");
        }

        List<PrintModel> printModelList = new ArrayList<>();
        String[] split = barcodes.split(",");
        for (String s : split){
            PrintModel printModel = new PrintModel();
            printModel.setSize(1);
            printModel.setQrCode(s);
            printModelList.add(printModel);
        }
        PrintDto printDto = new PrintDto();
        printDto.setLabelName(labelList.get(0).getLabelName());
        printDto.setLabelVersion(labelList.get(0).getLabelVersion());
        //printDto.setPrintName(printName);
        printDto.setPrintModelList(printModelList);
        sfcFeignApi.print(printDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String print(PDAWmsInnerSplitAndCombinePrintDto pdaWmsInnerSplitAndCombinePrintDto){
        Long materialId = pdaWmsInnerSplitAndCombinePrintDto.getMaterialId();
        Long storageId = pdaWmsInnerSplitAndCombinePrintDto.getStorageId();
        List<Long> materialBarcodeIdList = pdaWmsInnerSplitAndCombinePrintDto.getMaterialBarcodeIdList();
        Byte type = pdaWmsInnerSplitAndCombinePrintDto.getType();
        List<String> cartonPalletCodes = pdaWmsInnerSplitAndCombinePrintDto.getCartonPalletCodes();

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

        if(type == (byte)1) {//分包箱
            String sourceCartonCode = generatorCartonPalletCode(barcodeRule, materialId);
            String newCartonCode = generatorCartonPalletCode(barcodeRule, materialId);
            barcodes = sourceCartonCode + "," + newCartonCode;

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

            //修改源包箱信息
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
                sourceMaterialQty.add(wmsInnerMaterialBarcode.getMaterialQty());
                wmsInnerMaterialBarcode.setCartonCode(sourceCartonCode);
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
            }

            cartonPalletInventoryDetDto.setStorageId(storageId);
            wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(cartonPalletInventoryDetDto);
            WmsInnerMaterialBarcode sourceMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(cartonPalletInventoryDetDto.getMaterialBarcodeId());
            sourceMaterialBarcode.setCartonCode(sourceCartonCode);
            sourceMaterialBarcode.setMaterialQty(sourceMaterialQty);
            wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(sourceMaterialBarcode);


            //修改新包箱信息
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
                newMaterialQty.add(wmsInnerMaterialBarcode.getMaterialQty());
                wmsInnerMaterialBarcode.setCartonCode(newCartonCode);
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
            }

            WmsInnerMaterialBarcode newMaterialBarcode = new WmsInnerMaterialBarcode();
            BeanUtils.copyProperties(sourceMaterialBarcode, newMaterialBarcode);
            newMaterialBarcode.setMaterialBarcodeId(null);
            newMaterialBarcode.setCartonCode(newCartonCode);
            newMaterialBarcode.setMaterialQty(newMaterialQty);
            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(sourceMaterialBarcode);

            WmsInnerInventoryDetDto newCartonInventoryDetDto = new WmsInnerInventoryDetDto();
            BeanUtils.copyProperties(cartonPalletInventoryDetDto, newCartonInventoryDetDto);
            newCartonInventoryDetDto.setInventoryDetId(null);
            newCartonInventoryDetDto.setMaterialBarcodeId(newMaterialBarcode.getMaterialBarcodeId());
            newCartonInventoryDetDto.setStorageId(storageId);
            wmsInnerInventoryDetMapper.insertSelective(newCartonInventoryDetDto);
        }else if(type == (byte)2){//合包箱
            String newCartonCode = generatorCartonPalletCode(barcodeRule, materialId);
            barcodes = newCartonCode;

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
                totalMaterialQty.add(wmsInnerMaterialBarcode.getMaterialQty());
                wmsInnerMaterialBarcode.setCartonCode(newCartonCode);
                wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcode);
            }

            //新增箱码信息
            WmsInnerInventoryDetDto oldInventoryDetDto = cartonPalletInfoDtos.get(0).getCartonPalletInventoryDetDto();
            WmsInnerMaterialBarcode newMaterialBarcode = new WmsInnerMaterialBarcode();
            WmsInnerMaterialBarcode oldMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(oldInventoryDetDto.getMaterialBarcodeId());
            BeanUtils.copyProperties(oldMaterialBarcode, newMaterialBarcode);
            newMaterialBarcode.setMaterialBarcodeId(null);
            newMaterialBarcode.setCartonCode(newCartonCode);
            newMaterialBarcode.setMaterialQty(totalMaterialQty);
            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(newMaterialBarcode);

            WmsInnerInventoryDetDto newInventoryDetDto = new WmsInnerInventoryDetDto();
            BeanUtils.copyProperties(oldInventoryDetDto, newInventoryDetDto);
            newInventoryDetDto.setInventoryDetId(null);
            newInventoryDetDto.setMaterialBarcodeId(newMaterialBarcode.getMaterialBarcodeId());
            newInventoryDetDto.setStorageId(storageId);
            wmsInnerInventoryDetMapper.insertSelective(newInventoryDetDto);

            //删除源箱码信息
            Example inventoryDetExample = new Example(WmsInnerInventoryDet.class);
            inventoryDetExample.createCriteria().andIn("inventoryDetId",toDeleteInventoryDetIdList);
            wmsInnerInventoryDetMapper.deleteByExample(inventoryDetExample);
            Example materialBarcodeExample = new Example(WmsInnerMaterialBarcode.class);
            materialBarcodeExample.createCriteria().andIn("materialBarcodeId",toDeleteMaterialBarcodeIdList);
            wmsInnerMaterialBarcodeMapper.deleteByExample(materialBarcodeExample);
        }else if(type == (byte)3){//分栈板

        }else if(type == (byte)4){//合栈板

        }

        //打印
        printBarcode(barcodes,type);

        return barcodes;
    }
}

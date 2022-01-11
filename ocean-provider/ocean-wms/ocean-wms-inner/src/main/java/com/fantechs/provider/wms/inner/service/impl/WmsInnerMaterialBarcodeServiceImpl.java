package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerMaterialBarcodeImport;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReprint;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeReprintMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/07/03.
 */
@Service
public class WmsInnerMaterialBarcodeServiceImpl extends BaseService<WmsInnerMaterialBarcode> implements WmsInnerMaterialBarcodeService {

    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerMaterialBarcodeReOrderService wmsInnerMaterialBarcodeReOrderService;
    @Resource
    private WmsInnerMaterialBarcodeReprintMapper wmsInnerMaterialBarcodeReprintMapper;
    @Resource
    private WmsInnerHtMaterialBarcodeMapper wmsInnerHtMaterialBarcodeMapper;

    @Override
    public List<WmsInnerMaterialBarcodeDto> findList(Map<String,Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser sysUser = currentUser();
            map.put("orgId",sysUser.getOrganizationId());
            map.put("supplierId",sysUser.getSupplierId());
        }

        if (StringUtils.isNotEmpty(map.get("printOrderTypeCode")) && Integer.valueOf(map.get("printOrderTypeCode").toString()) == 1) {
            map.put("printOrderTypeCode","SRM-ASN");
        }else if (StringUtils.isNotEmpty(map.get("printOrderTypeCode")) && Integer.valueOf(map.get("printOrderTypeCode").toString()) == 2) {
            map.put("printOrderTypeCode","IN-SWK");
        }else if (StringUtils.isNotEmpty(map.get("printOrderTypeCode")) && Integer.valueOf(map.get("printOrderTypeCode").toString()) == 3) {
            map.put("printOrderTypeCode","QMS-MIIO");
        }else if (StringUtils.isNotEmpty(map.get("printOrderTypeCode")) && Integer.valueOf(map.get("printOrderTypeCode").toString()) == 4) {
            map.put("printOrderTypeCode","IN-IWK");
        }

        return wmsInnerMaterialBarcodeMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchUpdate(List<WmsInnerMaterialBarcodeDto> list) {
        int i = 0;
        //添加履历
        List<WmsInnerHtMaterialBarcode> htList = new ArrayList<>();
        for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto : list) {
            i += wmsInnerMaterialBarcodeMapper.updateByPrimaryKeySelective(wmsInnerMaterialBarcodeDto);

            WmsInnerHtMaterialBarcode wmsInnerHtMaterialBarcode = new WmsInnerHtMaterialBarcode();
            BeanUtil.copyProperties(wmsInnerMaterialBarcodeDto,wmsInnerHtMaterialBarcode);
            htList.add(wmsInnerHtMaterialBarcode);
        }
        if (StringUtils.isNotEmpty(htList)) {
            wmsInnerHtMaterialBarcodeMapper.insertList(htList);
        }

        return i;
    }

    @Override
    public List<WmsInnerMaterialBarcodeDto> batchAdd(List<WmsInnerMaterialBarcodeDto> list) {
        SysUser sysUser = currentUser();

        //履历集合
        List<WmsInnerHtMaterialBarcode> htList = new ArrayList<>();

        for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcode : list) {
            wmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
            wmsInnerMaterialBarcode.setCreateTime(new Date());
            wmsInnerMaterialBarcode.setCreateUserId(sysUser.getUserId());
            wmsInnerMaterialBarcode.setModifiedTime(new Date());
            wmsInnerMaterialBarcode.setModifiedUserId(sysUser.getUserId());
            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarcode);
            //添加履历
            WmsInnerHtMaterialBarcode wmsInnerHtMaterialBarcode = new WmsInnerHtMaterialBarcode();
            BeanUtil.copyProperties(wmsInnerMaterialBarcode,wmsInnerHtMaterialBarcode);
            htList.add(wmsInnerHtMaterialBarcode);
        }
        if (StringUtils.isNotEmpty(htList)) {
            wmsInnerHtMaterialBarcodeMapper.insertList(htList);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public List<WmsInnerMaterialBarcodeDto> add(List<WmsInnerMaterialBarcodeDto> barcodeDtoList,Integer type) {
        SysUser sysUser = currentUser();

        //根据选择生成的条码数量生成条码
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList = new ArrayList<>();

        //ASN条码明细集合
        List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrderList = new ArrayList<>();

        //履历集合
        List<WmsInnerHtMaterialBarcode> htList = new ArrayList<>();

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();

        for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto : barcodeDtoList) {
            if(StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getMaterialId())){
                throw new BizErrorException("绑定物料编码不能为空");
            }
            if(StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getPrintQty())){
                throw new BizErrorException("打印条码数量不能为空");
            }

            SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode = new SearchWmsInnerMaterialBarcode();
            //判断打印类型（1，ASN单 2，收货作业单 3，来料检验单 4，上架作业单）
            if (type == 1) {
                searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("SRM-ASN");
            }else if (type == 2) {
                searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("IN-SWK");
            }else if (type == 3) {
                searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("QMS-MIIO");
            }else if (type == 4) {
                searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("IN-IWK");
            }
            searchWmsInnerMaterialBarcode.setPrintOrderDetId(wmsInnerMaterialBarcodeDto.getPrintOrderDetId());
            searchWmsInnerMaterialBarcode.setMaterialId(wmsInnerMaterialBarcodeDto.getMaterialId());
            searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
            Integer totalMaterialQty = wmsInnerMaterialBarcodeMapper.getTotalMaterialQty(searchWmsInnerMaterialBarcode);
            wmsInnerMaterialBarcodeDto.setTotalMaterialQty(new BigDecimal(StringUtils.isNotEmpty(totalMaterialQty)?totalMaterialQty:0));

            if (StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getGenerateQty(),wmsInnerMaterialBarcodeDto.getOrderQty())) {
                throw new BizErrorException("数量不正确");
            } else if (wmsInnerMaterialBarcodeDto.getOrderQty().compareTo(wmsInnerMaterialBarcodeDto.getGenerateQty().add(StringUtils.isNotEmpty(wmsInnerMaterialBarcodeDto.getTotalMaterialQty())?wmsInnerMaterialBarcodeDto.getTotalMaterialQty():new BigDecimal(0))) == -1) {
                throw new BizErrorException("总打印数量超出订单数量");
            }


            SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();

            //取系统配置默认编码
            BaseBarcodeRuleDto baseBarCode = getBaseBarCode();

            searchBaseMaterial.setMaterialId(wmsInnerMaterialBarcodeDto.getMaterialId());
            List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();


            if (StringUtils.isEmpty(baseMaterialList) || StringUtils.isEmpty(baseMaterialList.get(0).getBarcodeRuleSetId())) {
                searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
            }else {
                searchBaseBarcodeRule.setBarcodeRuleSetId(baseMaterialList.get(0).getBarcodeRuleSetId());
                List<BaseBarcodeRuleDto> baseBarcodeRuleList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
                if (StringUtils.isEmpty(baseBarcodeRuleList)) {
                    searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
                }
                Long barcodeRuleId = null;
                for (BaseBarcodeRuleDto baseBarcodeRuleDto : baseBarcodeRuleList) {
                    if ("物料条码".equals(baseBarcodeRuleDto.getLabelCategoryName())) {
                        barcodeRuleId = baseBarcodeRuleDto.getBarcodeRuleId();
                        break;
                    }
                }

                if (StringUtils.isEmpty(barcodeRuleId)) {
                    searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
                }else {
                    searchBaseBarcodeRuleSpec.setBarcodeRuleId(barcodeRuleId);
                }

            }



            ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecList= baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
            if(barcodeRuleSpecList.getCode()!=0) {
                throw new BizErrorException(barcodeRuleSpecList.getMessage());
            }
            if(barcodeRuleSpecList.getData().size()<1) {
                throw new BizErrorException("请设置条码规则");
            }
            List<BaseBarcodeRuleSpec>  list = barcodeRuleSpecList.getData();

            Integer materialQty = StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getMaterialQty())?1:wmsInnerMaterialBarcodeDto.getMaterialQty().intValue();
            Integer generateQty = StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getGenerateQty())?0:wmsInnerMaterialBarcodeDto.getGenerateQty().intValue();
            for(int i=0;i < Math.ceil(generateQty.doubleValue()/materialQty.doubleValue());i++) {

                WmsInnerMaterialBarcodeDto  wmsInnerMaterialBarCode = new WmsInnerMaterialBarcodeDto();
                BeanUtils.autoFillEqFields(wmsInnerMaterialBarcodeDto,wmsInnerMaterialBarCode);
                String barCode = creatBarCode(list, wmsInnerMaterialBarcodeDto.getMaterialCode(), wmsInnerMaterialBarcodeDto.getMaterialId());

                Example example = new Example(WmsInnerMaterialBarcode.class);
                example.createCriteria().andEqualTo("barcode",barCode);
                List<WmsInnerMaterialBarcode> wmsInnerMaterialBarcodes = wmsInnerMaterialBarcodeMapper.selectByExample(example);
                if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodes)) {
                    throw new BizErrorException("条码重复产生");
                }

                if (generateQty%materialQty != 0 && i == Math.ceil(generateQty.doubleValue()/materialQty.doubleValue()) -1) {
                    wmsInnerMaterialBarCode.setMaterialQty(new BigDecimal(generateQty%materialQty));
                }
                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                //判断打印类型（1，ASN单 2，收货作业单 3，来料检验单 4，上架作业单）
                if (type == 1) {
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("SRM-ASN");
                    wmsInnerMaterialBarCode.setPrintOrderTypeCode("SRM-ASN");
                }else if (type == 2) {
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-SWK");
                    wmsInnerMaterialBarCode.setPrintOrderTypeCode("IN-SWK");

//                    wmsInnerMaterialBarCode.setSupplierId();
//                    wmsInnerMaterialBarCode.setSupplierName();
                }else if (type == 3) {
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("QMS-MIIO");
                    wmsInnerMaterialBarCode.setPrintOrderTypeCode("QMS-MIIO");
                }else if (type == 4) {
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-IWK");
                    wmsInnerMaterialBarCode.setPrintOrderTypeCode("IN-IWK");

//                    wmsInnerMaterialBarCode.setSupplierId();
//                    wmsInnerMaterialBarCode.setSupplierName();
                }
                wmsInnerMaterialBarCode.setBarcodeStatus((byte) 1);
                wmsInnerMaterialBarCode.setBarcode(barCode);
                wmsInnerMaterialBarCode.setBarcodeRuleId(list.get(0).getBarcodeRuleId());
                wmsInnerMaterialBarCode.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarCode.setCreateTime(new Date());
                wmsInnerMaterialBarCode.setCreateUserId(sysUser.getUserId());
                wmsInnerMaterialBarCode.setModifiedTime(new Date());
                wmsInnerMaterialBarCode.setModifiedUserId(sysUser.getUserId());
                wmsInnerMaterialBarCode.setCreateType((byte) 3);
                wmsInnerMaterialBarCode.setBarcodeType((byte) 1);
                wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarCode);

                //添加履历
                WmsInnerHtMaterialBarcode wmsInnerHtMaterialBarcode = new WmsInnerHtMaterialBarcode();
                BeanUtil.copyProperties(wmsInnerMaterialBarCode,wmsInnerHtMaterialBarcode);
                htList.add(wmsInnerHtMaterialBarcode);

                materialBarcodeList.add(wmsInnerMaterialBarCode);

                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 1);
                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerMaterialBarcodeDto.getPrintOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerMaterialBarcodeDto.getPrintOrderId());
                wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInnerMaterialBarcodeDto.getPrintOrderDetId());
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerMaterialBarCode.getMaterialBarcodeId());
                wmsInnerMaterialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);
            }
        }

        if (StringUtils.isNotEmpty(htList)) {
            wmsInnerHtMaterialBarcodeMapper.insertList(htList);
        }
        if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeReOrderList)) {
            wmsInnerMaterialBarcodeReOrderService.batchSave(wmsInnerMaterialBarcodeReOrderList);
        }

        return materialBarcodeList;
    }

    //生成条码
    private String creatBarCode(List<BaseBarcodeRuleSpec> list,String materialCode,Long materialId){
        String lastBarCode = null;
        boolean hasKey = redisUtil.hasKey(this.sub(list));
        if(hasKey){
            // 从redis获取上次生成条码
            Object redisRuleData = redisUtil.get(this.sub(list));
            lastBarCode = String.valueOf(redisRuleData);
        }
        //获取最大流水号
        String maxCode = baseFeignApi.generateMaxCode(list, lastBarCode).getData();
        //生成条码
        ResponseEntity<String> rs = baseFeignApi.generateCode(list,maxCode,materialCode,materialId.toString());
        if(rs.getCode()!=0){
            throw new BizErrorException(rs.getMessage());
        }

        // 更新redis最新条码
        redisUtil.set(sub(list), rs.getData());

        return rs.getData();
    }


    @Override
    public LabelRuteDto findLabelRute(Long barcodeRuleSetId,Long materialId) {
        SysUser sysUser = currentUser();
        SearchSysSpecItem lableItem = new SearchSysSpecItem();
        lableItem.setSpecCode("BaseLabel");
        ResponseEntity<List<SysSpecItem>> lableList = securityFeignApi.findSpecItemList(lableItem);
        if(StringUtils.isEmpty(lableList.getData())) {
            throw new BizErrorException("未设置默认标签");
        }
        LabelRuteDto labelRuteDto = wmsInnerMaterialBarcodeMapper.findRule(lableList.getData().get(0).getParaValue(),materialId,sysUser.getOrganizationId());
        if(StringUtils.isEmpty(labelRuteDto)) {
            throw new BizErrorException("标签卡为空");
        }

        if(barcodeRuleSetId != 0) {
            SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet = new SearchBaseBarcodeRuleSetDet();
            searchBaseBarcodeRuleSetDet.setBarcodeRuleSetId(barcodeRuleSetId);
            ResponseEntity<List<BaseBarcodeRuleSetDetDto>> barcodeRuleSetDetList = baseFeignApi.findBarcodeRuleSetDetList(searchBaseBarcodeRuleSetDet);
            if (StringUtils.isEmpty(barcodeRuleSetDetList.getData())) {
                throw new BizErrorException("未找到对应条码集合");
            }
            SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
            for (BaseBarcodeRuleSetDetDto dto : barcodeRuleSetDetList.getData()) {
                searchBaseBarcodeRule.setBarcodeRuleId(dto.getBarcodeRuleId());
                List<BaseBarcodeRuleDto> baseBarcodeRuleDtos = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
                if(StringUtils.isNotEmpty(baseBarcodeRuleDtos) && "来料打印".equals(baseBarcodeRuleDtos.get(0).getLabelCategoryName())) {
                    labelRuteDto.setBarcodeRuleId(baseBarcodeRuleDtos.get(0).getBarcodeRuleId());
                    labelRuteDto.setBarcodeRule(baseBarcodeRuleDtos.get(0).getBarcodeRule());
                }
            }
            if(StringUtils.isEmpty(labelRuteDto.getBarcodeRuleId())) {
                BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
                labelRuteDto.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
                labelRuteDto.setBarcodeRule(baseBarCode.getBarcodeRule());
            }
            /*if (barcodeRuleSetDetList.getData().size() > 1) throw new BizErrorException("规则集合配置错误，该规则集合只能配置一条规则");
            SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
            searchBaseBarcodeRule.setBarcodeRuleSetId(barcodeRuleSetId);
            ResponseEntity<List<BaseBarcodeRuleDto>>  barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule);
            if (StringUtils.isEmpty(barcodeRulList.getData())) throw new BizErrorException("未找到对应条码");
            labelRuteDto.setBarcodeRuleId(barcodeRulList.getData().get(0).getBarcodeRuleId());*/
        }else{
            //无id值则取默认生成规则

            BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
            labelRuteDto.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
            labelRuteDto.setBarcodeRule(baseBarCode.getBarcodeRule());
        }

        return labelRuteDto;
      //  return barcodeRulList.getData().get(0);
    }

    /**
     * 打印/补打条码
     * @param ids 条码唯一标识
     * @return 1
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(String ids, int printQty,String printName,String printType,int printMode) {
        String[] arrId = ids.split(",");

        if (StringUtils.isEmpty(printType)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未配置打印标签模板");
        }
        SearchBaseLabel searchBaseLabel = new SearchBaseLabel();
        searchBaseLabel.setLabelCode(printType);
        List<BaseLabelDto> labelList = baseFeignApi.findLabelList(searchBaseLabel).getData();
        if (StringUtils.isEmpty(labelList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"标签模板不存在");
        }
        List<WmsInnerMaterialBarcodeReprint> reprintList = new ArrayList<>();
        List<WmsInnerMaterialBarcode> barcodeList = new ArrayList<>();
        for (String s : arrId) {
            //查询模版信息
            WmsInnerMaterialBarcode wmsInnerMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(s);
            wmsInnerMaterialBarcode.setBarcodeStatus((byte) 2);
            barcodeList.add(wmsInnerMaterialBarcode);
            if (printMode == 1) {
                WmsInnerMaterialBarcodeReprint wmsInnerMaterialBarcodeReprint = new WmsInnerMaterialBarcodeReprint();
                wmsInnerMaterialBarcodeReprint.setCreateTime(new Date());
                wmsInnerMaterialBarcodeReprint.setCreateUserId(currentUser().getUserId());
                wmsInnerMaterialBarcodeReprint.setMaterialBarcodeId(Long.valueOf(s));
                wmsInnerMaterialBarcodeReprint.setModifiedTime(new Date());
                wmsInnerMaterialBarcodeReprint.setModifiedUserId(currentUser().getUserId());
                wmsInnerMaterialBarcodeReprint.setPrinterName(printName);
                reprintList.add(wmsInnerMaterialBarcodeReprint);
            }
            PrintModel printModel = wmsInnerMaterialBarcodeMapper.findPrintModel(wmsInnerMaterialBarcode.getMaterialBarcodeId(),labelList.get(0).getLabelCode());
            printModel.setSize(printQty);
            PrintDto printDto = new PrintDto();
            printDto.setLabelName(labelList.get(0).getLabelName());
            printDto.setLabelVersion(labelList.get(0).getLabelVersion());
            printDto.setPrintName(printName);
            List<PrintModel> printModelList = new ArrayList<>();
            printModelList.add(printModel);
            printDto.setPrintModelList(printModelList);
            sfcFeignApi.print(printDto);
        }
        if (StringUtils.isNotEmpty(reprintList)) {
            wmsInnerMaterialBarcodeReprintMapper.insertList(reprintList);
        }
        if (StringUtils.isNotEmpty(barcodeList)) {
            wmsInnerMaterialBarcodeMapper.batchUpdate(barcodeList);
        }

        return 1;
    }


    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    public String sub(List<BaseBarcodeRuleSpec> list){
        StringBuffer sb = new StringBuffer();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            StringBuffer s = new StringBuffer(baseBarcodeRuleSpec.getSpecification());
            String specification = "";
            for (Integer i = 0; i < baseBarcodeRuleSpec.getBarcodeLength() - 1; i++) {
                specification += baseBarcodeRuleSpec.getSpecification().substring(1, 2);
            }
            s.insert(1,specification);
            sb.append(s.toString());
        }
        return sb.toString();
    }

    //获取默认规则
    public BaseBarcodeRuleDto getBaseBarCode(){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("BaseBarCodeRule");
        List<SysSpecItem> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(StringUtils.isEmpty(specItemList)) {
            throw new BizErrorException("未设置默认编码规则，无法生成编码");
        }
        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
        searchBaseBarcodeRule.setBarcodeRuleCode(specItemList.get(0).getParaValue());
        ResponseEntity<List<BaseBarcodeRuleDto>> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule);
        if(StringUtils.isEmpty(barcodeRulList.getData())) {
            throw new BizErrorException("未查询到配置项配置的默认编码规则");
        }
        return barcodeRulList.getData().get(0);
    }

    @Override
    public Map<String, Object> importExcel(List<WmsInnerMaterialBarcodeImport> importList, List<WmsInnerMaterialBarcodeDto> list,Integer type) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
//        List<WmsInnerMaterialBarcodeDto> dataList = new ArrayList<>();
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode = new SearchWmsInnerMaterialBarcode();
        List<WmsInnerHtMaterialBarcode> htList = new ArrayList<>();
        Example example = new Example(WmsInnerMaterialBarcode.class);

        String printOrderTypeCode = "";
        //判断打印类型（1，ASN单 2，收货作业单 3，来料检验单 4，上架作业单）
        if (type == 1) {
            searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("SRM-ASN");
            printOrderTypeCode = "SRM-ASN";
        }else if (type == 2) {
            searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("IN-SWK");
            printOrderTypeCode = "IN-SWK";
        }else if (type == 3) {
            searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("QMS-MIIO");
            printOrderTypeCode = "QMS-MIIO";
        }else if (type == 4) {
            searchWmsInnerMaterialBarcode.setPrintOrderTypeCode("IN-IWK");
            printOrderTypeCode = "IN-IWK";
        }
        //ASN条码明细集合
        List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrderList = new ArrayList<>();

        Example barcodeExample = new Example(WmsInnerMaterialBarcode.class);

        for (WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto : list) {
            for (int i = 0; i < importList.size(); i++) {
                //导入的实体
                WmsInnerMaterialBarcodeImport wmsInnerMaterialBarcodeImport = importList.get(i);
                //获取导入条码的物料是否存在
                searchBaseMaterial.setMaterialCode(wmsInnerMaterialBarcodeImport.getMaterialCode());
                List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterialList)) {
                    fail.add(i + 1);
                    continue;
                }

                //判断条码是否存在
                example.createCriteria().orEqualTo("barcode",wmsInnerMaterialBarcodeImport.getBarcode());
                List<WmsInnerMaterialBarcode> wmsInnerMaterialBarcodes = wmsInnerMaterialBarcodeMapper.selectByExample(example);
                example.clear();
                if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodes)) {
                    fail.add(i + 1);
                    continue;
                }

                //判断条码归属那张单
                if (wmsInnerMaterialBarcodeDto.getPrintOrderCode().equals(wmsInnerMaterialBarcodeImport.getOrderCode()) &&
                    wmsInnerMaterialBarcodeDto.getMaterialId().equals(baseMaterialList.get(0).getMaterialId())) {

                    WmsInnerMaterialBarcodeDto addWmsInnerMaterialBarcode = new WmsInnerMaterialBarcodeDto();

                    BeanUtil.copyProperties(wmsInnerMaterialBarcodeDto,addWmsInnerMaterialBarcode);
                    BeanUtil.copyProperties(wmsInnerMaterialBarcodeImport,addWmsInnerMaterialBarcode);


                    searchWmsInnerMaterialBarcode.setPrintOrderDetId(wmsInnerMaterialBarcodeDto.getPrintOrderDetId());
                    searchWmsInnerMaterialBarcode.setMaterialId(wmsInnerMaterialBarcodeDto.getMaterialId());
                    searchWmsInnerMaterialBarcode.setOrgId(user.getOrganizationId());
                    Integer totalMaterialQty = wmsInnerMaterialBarcodeMapper.getTotalMaterialQty(searchWmsInnerMaterialBarcode);
                    wmsInnerMaterialBarcodeDto.setTotalMaterialQty(new BigDecimal(StringUtils.isNotEmpty(totalMaterialQty)?totalMaterialQty:0));
                    if (StringUtils.isEmpty(addWmsInnerMaterialBarcode.getMaterialQty())) {
                        fail.add(i + 1);
                        continue;
                    } else if (wmsInnerMaterialBarcodeDto.getOrderQty().compareTo(addWmsInnerMaterialBarcode.getMaterialQty().add(StringUtils.isNotEmpty(wmsInnerMaterialBarcodeDto.getTotalMaterialQty())?wmsInnerMaterialBarcodeDto.getTotalMaterialQty():new BigDecimal(0))) == -1) {
                        fail.add(i + 1);
                        continue;
                    }
                    //按照最大的条码维度确定导入条码类型
                    WmsInnerMaterialBarcodeDto parentBarcode = new WmsInnerMaterialBarcodeDto();
                    BeanUtil.copyProperties(wmsInnerMaterialBarcodeImport,parentBarcode);
                    parentBarcode.setCreateType((byte) 3);
                    parentBarcode.setPrintOrderTypeCode(printOrderTypeCode);
                    parentBarcode.setIfSysBarcode((byte) 1);
                    parentBarcode.setBarcodeStatus((byte) 1);

                    if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeImport.getPalletCode())) {
                        barcodeExample.createCriteria().andEqualTo("palletCode",wmsInnerMaterialBarcodeImport.getPalletCode())
                                .andEqualTo("barcodeType",4);
                        List<WmsInnerMaterialBarcode> parentMaterialBarcodeList = wmsInnerMaterialBarcodeMapper.selectByExample(barcodeExample);
                        barcodeExample.clear();
                        if (StringUtils.isNotEmpty(parentMaterialBarcodeList)) {
                            parentMaterialBarcodeList.get(0).setMaterialQty(parentMaterialBarcodeList.get(0).getMaterialQty().add(new BigDecimal(1)));
                        }else {
                            parentBarcode.setBarcodeType((byte) 4);
                            parentBarcode.setCartonCode(null);
                            parentBarcode.setBarcode(null);
                            parentBarcode.setColorBoxCode(null);
                            parentBarcode.setMaterialQty(new BigDecimal(1));
                            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(parentBarcode);

                            //添加导入条码履历与单据中间表数据
                            addHt(parentBarcode,printOrderTypeCode,wmsInnerMaterialBarcodeReOrderList,wmsInnerMaterialBarcodeDto);
                        }
                    }
                    if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeImport.getCartonCode())) {
                        barcodeExample.createCriteria().andEqualTo("cartonCode",wmsInnerMaterialBarcodeImport.getCartonCode())
                                .andEqualTo("barcodeType",3);
                        List<WmsInnerMaterialBarcode> parentMaterialBarcodeList = wmsInnerMaterialBarcodeMapper.selectByExample(barcodeExample);
                        barcodeExample.clear();
                        if (StringUtils.isNotEmpty(parentMaterialBarcodeList)) {
                            parentMaterialBarcodeList.get(0).setMaterialQty(parentMaterialBarcodeList.get(0).getMaterialQty().add(new BigDecimal(1)));
                        }else {
                            parentBarcode.setBarcodeType((byte) 3);
                            parentBarcode.setPalletCode(null);
                            parentBarcode.setBarcode(null);
                            parentBarcode.setColorBoxCode(null);
                            parentBarcode.setMaterialQty(new BigDecimal(1));
                            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(parentBarcode);

                            //添加导入条码履历与单据中间表数据
                            addHt(parentBarcode,printOrderTypeCode,wmsInnerMaterialBarcodeReOrderList,wmsInnerMaterialBarcodeDto);
                        }

                    }
                    if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeImport.getColorBoxCode())) {
                        barcodeExample.createCriteria().andEqualTo("colorBoxCode",wmsInnerMaterialBarcodeImport.getColorBoxCode())
                                .andEqualTo("barcodeType",2);
                        List<WmsInnerMaterialBarcode> parentMaterialBarcodeList = wmsInnerMaterialBarcodeMapper.selectByExample(barcodeExample);
                        barcodeExample.clear();
                        if (StringUtils.isNotEmpty(parentMaterialBarcodeList)) {
                            parentMaterialBarcodeList.get(0).setMaterialQty(parentMaterialBarcodeList.get(0).getMaterialQty().add(new BigDecimal(1)));
                        }else {
                            parentBarcode.setBarcodeType((byte) 2);
                            parentBarcode.setPalletCode(null);
                            parentBarcode.setBarcode(null);
                            parentBarcode.setColorBoxCode(null);
                            parentBarcode.setMaterialQty(new BigDecimal(1));
                            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(parentBarcode);

                            //添加导入条码履历与单据中间表数据
                            addHt(parentBarcode,printOrderTypeCode,wmsInnerMaterialBarcodeReOrderList,wmsInnerMaterialBarcodeDto);
                        }
                    }

                    if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeImport.getBarcode())) {
                        barcodeExample.createCriteria().andEqualTo("barcode",wmsInnerMaterialBarcodeImport.getBarcode())
                                .andEqualTo("barcodeType",1);
                        List<WmsInnerMaterialBarcode> parentMaterialBarcodeList = wmsInnerMaterialBarcodeMapper.selectByExample(barcodeExample);
                        barcodeExample.clear();
                        if (StringUtils.isNotEmpty(parentMaterialBarcodeList)) {
                            parentMaterialBarcodeList.get(0).setMaterialQty(parentMaterialBarcodeList.get(0).getMaterialQty().add(new BigDecimal(1)));
                        }else {
                            parentBarcode.setBarcodeType((byte) 1);
                            parentBarcode.setPalletCode(null);
                            parentBarcode.setBarcode(null);
                            parentBarcode.setColorBoxCode(null);
                            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(parentBarcode);

                            //添加导入条码履历与单据中间表数据
                            addHt(parentBarcode,printOrderTypeCode,wmsInnerMaterialBarcodeReOrderList,wmsInnerMaterialBarcodeDto);
                        }
                    }
                    success++;
                }else {
                    fail.add(i + 1);
                    continue;
                }
            }
        }
        if (StringUtils.isNotEmpty(htList)) {
            wmsInnerHtMaterialBarcodeMapper.insertList(htList);
        }
        if (StringUtils.isNotEmpty(wmsInnerMaterialBarcodeReOrderList)) {
            wmsInnerMaterialBarcodeReOrderService.batchSave(wmsInnerMaterialBarcodeReOrderList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    private void addHt(WmsInnerMaterialBarcodeDto parentBarcode,String printOrderTypeCode,
                       List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrderList,WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto){
        //添加履历
        WmsInnerHtMaterialBarcode wmsInnerHtMaterialBarcode = new WmsInnerHtMaterialBarcode();
        BeanUtil.copyProperties(parentBarcode,wmsInnerHtMaterialBarcode);
        WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();


        wmsInnerMaterialBarcodeReOrder.setOrderTypeCode(printOrderTypeCode);
        wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 1);
        wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerMaterialBarcodeDto.getPrintOrderCode());
        wmsInnerMaterialBarcodeReOrder.setOrderId(wmsInnerMaterialBarcodeDto.getPrintOrderId());
        wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInnerMaterialBarcodeDto.getPrintOrderDetId());
        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(parentBarcode.getMaterialBarcodeId());
        wmsInnerMaterialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<WmsInnerHtMaterialBarcode> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsInnerMaterialBarcode wmsInnerMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerMaterialBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerHtMaterialBarcode wmsInnerHtMaterialBarcode = new WmsInnerHtMaterialBarcode();
            org.springframework.beans.BeanUtils.copyProperties(wmsInnerMaterialBarcode,wmsInnerHtMaterialBarcode);
            htList.add(wmsInnerHtMaterialBarcode);
        }

        wmsInnerHtMaterialBarcodeMapper.insertList(htList);
        return wmsInnerMaterialBarcodeMapper.deleteByIds(ids);
    }


    @Override
    public List<WmsInnerMaterialBarcodeDto> findListByCode(List<String> codes) {
        List<WmsInnerMaterialBarcodeDto> list = new ArrayList<>();
        for (String code : codes) {
            //条码
            Map map = new HashMap();
            map.put("barcode",code);
            map.put("codeQueryMark",1);
            List<WmsInnerMaterialBarcodeDto> list1 = wmsInnerMaterialBarcodeMapper.findList(map);
            if(StringUtils.isNotEmpty(list1)){
                list.addAll(list1);
            }else{
                //彩盒码
                map = new HashMap();
                map.put("colorBoxCode",code);
                map.put("codeQueryMark",1);
                List<WmsInnerMaterialBarcodeDto> list2 = wmsInnerMaterialBarcodeMapper.findList(map);
                if(StringUtils.isNotEmpty(list2)){
                    list.addAll(list2);
                }else{
                    //箱码
                    map = new HashMap();
                    map.put("cartonCode",code);
                    map.put("codeQueryMark",1);
                    List<WmsInnerMaterialBarcodeDto> list3 = wmsInnerMaterialBarcodeMapper.findList(map);
                    if(StringUtils.isNotEmpty(list3)){
                        list.addAll(list3);
                    }else{
                        //栈板码
                        map = new HashMap();
                        map.put("palletCode",code);
                        map.put("codeQueryMark",1);
                        List<WmsInnerMaterialBarcodeDto> list4 = wmsInnerMaterialBarcodeMapper.findList(map);
                        if(StringUtils.isNotEmpty(list4)){
                            list.addAll(list4);
                        }else{
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                    }
                }
            }
        }
        return list;
    }
}

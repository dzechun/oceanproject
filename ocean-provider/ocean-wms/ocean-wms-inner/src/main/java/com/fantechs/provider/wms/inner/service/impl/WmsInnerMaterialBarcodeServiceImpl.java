package com.fantechs.provider.wms.inner.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPlanDeliveryOrderImport;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerMaterialBarcodeImport;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<WmsInnerMaterialBarcodeDto> findList(SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode) {
        if(StringUtils.isEmpty(searchWmsInnerMaterialBarcode.getOrgId())){
            SysUser sysUser = currentUser();
            searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
            searchWmsInnerMaterialBarcode.setSupplierId(sysUser.getSupplierId());
        }
        return wmsInnerMaterialBarcodeMapper.findList(searchWmsInnerMaterialBarcode);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchUpdate(List<WmsInnerMaterialBarcodeDto> list) {
        return wmsInnerMaterialBarcodeMapper.batchUpdate(list);
    }

    @Override
    public List<WmsInnerMaterialBarcodeDto> add(List<WmsInnerMaterialBarcodeDto> barcodeDtoList,Integer type) {
        SysUser sysUser = currentUser();

        //根据选择生成的条码数量生成条码
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList = new ArrayList<>();

        //ASN条码明细集合
        List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrderList = new ArrayList<>();


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
            BigDecimal totalMaterialQty = wmsInnerMaterialBarcodeMapper.getTotalMaterialQty(searchWmsInnerMaterialBarcode);
            wmsInnerMaterialBarcodeDto.setTotalMaterialQty(totalMaterialQty);

            if (StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getTotalMaterialQty(),wmsInnerMaterialBarcodeDto.getGenerateQty(),
                    wmsInnerMaterialBarcodeDto.getOrderQty())) {
                throw new BizErrorException("数量不正确");
            } else if (wmsInnerMaterialBarcodeDto.getOrderQty().compareTo(wmsInnerMaterialBarcodeDto.getTotalMaterialQty().add(wmsInnerMaterialBarcodeDto.getGenerateQty())) == -1) {
                throw new BizErrorException("打印数量操作订单数量");
            }


            SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();

            //取系统配置默认编码
            BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
            searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());

            ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecList= baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
            if(barcodeRuleSpecList.getCode()!=0) throw new BizErrorException(barcodeRuleSpecList.getMessage());
            if(barcodeRuleSpecList.getData().size()<1) throw new BizErrorException("请设置条码规则");
            List<BaseBarcodeRuleSpec>  list = barcodeRuleSpecList.getData();

            Integer materialQty = StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getMaterialQty())?1:wmsInnerMaterialBarcodeDto.getMaterialQty().intValue();
            Integer generateQty = StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getGenerateQty())?0:wmsInnerMaterialBarcodeDto.getGenerateQty().intValue();
            for(int i=0;i < Math.ceil(generateQty/materialQty);i++) {

                WmsInnerMaterialBarcodeDto  wmsInnerMaterialBarCode = new WmsInnerMaterialBarcodeDto();
                BeanUtils.autoFillEqFields(wmsInnerMaterialBarcodeDto,wmsInnerMaterialBarCode);
                String barCode = creatBarCode(list, wmsInnerMaterialBarcodeDto.getMaterialCode(), wmsInnerMaterialBarcodeDto.getMaterialId());
                if (generateQty%materialQty != 0 && i == Math.ceil(generateQty/materialQty) -1) {
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
                }else if (type == 3) {
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("QMS-MIIO");
                    wmsInnerMaterialBarCode.setPrintOrderTypeCode("QMS-MIIO");
                }else if (type == 4) {
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-IWK");
                    wmsInnerMaterialBarCode.setPrintOrderTypeCode("IN-IWK");
                }

                wmsInnerMaterialBarCode.setBarcode(barCode);
                wmsInnerMaterialBarCode.setBarcodeRuleId(list.get(0).getBarcodeRuleId());
                wmsInnerMaterialBarCode.setOrgId(sysUser.getOrganizationId());
                wmsInnerMaterialBarCode.setCreateTime(new Date());
                wmsInnerMaterialBarCode.setCreateUserId(sysUser.getUserId());
                wmsInnerMaterialBarCode.setModifiedTime(new Date());
                wmsInnerMaterialBarCode.setModifiedUserId(sysUser.getUserId());
                wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarCode);
                materialBarcodeList.add(wmsInnerMaterialBarCode);


                wmsInnerMaterialBarcodeReOrder.setOrderCode(wmsInnerMaterialBarcodeDto.getPrintOrderCode());
                wmsInnerMaterialBarcodeReOrder.setOrgId(wmsInnerMaterialBarcodeDto.getPrintOrderId());
                wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInnerMaterialBarcodeDto.getPrintOrderDetId());
                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInnerMaterialBarCode.getMaterialBarcodeId());
                wmsInnerMaterialBarcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);
            }
            wmsInnerMaterialBarcodeReOrderService.batchAdd(wmsInnerMaterialBarcodeReOrderList);
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
        if(StringUtils.isEmpty(lableList.getData())) throw new BizErrorException("未设置默认标签");
        LabelRuteDto labelRuteDto = wmsInnerMaterialBarcodeMapper.findRule(lableList.getData().get(0).getParaValue(),materialId,sysUser.getOrganizationId());
        if(StringUtils.isEmpty(labelRuteDto)) throw new BizErrorException("标签卡为空");

        if(barcodeRuleSetId != 0) {
            SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet = new SearchBaseBarcodeRuleSetDet();
            searchBaseBarcodeRuleSetDet.setBarcodeRuleSetId(barcodeRuleSetId);
            ResponseEntity<List<BaseBarcodeRuleSetDetDto>> barcodeRuleSetDetList = baseFeignApi.findBarcodeRuleSetDetList(searchBaseBarcodeRuleSetDet);
            if (StringUtils.isEmpty(barcodeRuleSetDetList.getData())) throw new BizErrorException("未找到对应条码集合");
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
    public int print(String ids, int printQty,String printName,int printType) {
        String[] arrId = ids.split(",");
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("printModel");
        List<SysSpecItem> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        if (StringUtils.isEmpty(specItemList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未配置打印标签模板");
        }

        JSONArray jsonArray = JSONArray.parseArray(specItemList.get(0).getParaValue());
        SearchBaseLabel searchBaseLabel = new SearchBaseLabel();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
            if (StringUtils.isNotEmpty(jsonObject.get("type")) && printType == Integer.valueOf(jsonObject.get("type").toString())) {
                searchBaseLabel.setLabelCode(StringUtils.isNotEmpty(jsonObject.get("modelName"))?jsonObject.get("modelName").toString():"");
            }
        }
        List<BaseLabelDto> labelList = baseFeignApi.findLabelList(searchBaseLabel).getData();
        if (StringUtils.isEmpty(labelList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"标签模板不存在");
        }

        for (String s : arrId) {
            //查询模版信息
            WmsInnerMaterialBarcode wmsInnerMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(s);

            PrintModel printModel = wmsInnerMaterialBarcodeMapper.findPrintModel(wmsInnerMaterialBarcode.getMaterialBarcodeId());
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
            sb.append(baseBarcodeRuleSpec.getSpecification());
        }
        return sb.toString();
    }

    //获取默认规则
    public BaseBarcodeRuleDto getBaseBarCode(){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("BaseBarCodeRule");
        ResponseEntity<List<SysSpecItem>> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        if(StringUtils.isEmpty(specItemList.getData())) throw new BizErrorException("未设置默认编码规则，无法生成编码");
        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
        searchBaseBarcodeRule.setBarcodeRuleCode(specItemList.getData().get(0).getParaValue());
        ResponseEntity<List<BaseBarcodeRuleDto>> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule);
        if(StringUtils.isEmpty(barcodeRulList.getData())) throw new BizErrorException("未查询到配置项配置的默认编码规则");
        return barcodeRulList.getData().get(0);
    }

    @Override
    public Map<String, Object> importExcel(List<WmsInnerMaterialBarcodeImport> importList, List<WmsInnerMaterialBarcodeDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<String> fail = new ArrayList<>();  //记录操作失败行数
        List<SrmPlanDeliveryOrderDet> detList = new ArrayList<>();


        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败的送货计划标识",fail);
        return resultMap;
    }
}

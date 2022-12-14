package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSetDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
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
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<WmsInnerMaterialBarcodeDto> findList(SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode) {
        if(StringUtils.isEmpty(searchWmsInnerMaterialBarcode.getOrgId())){
            SysUser sysUser = currentUser();
            searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
        }
        return wmsInnerMaterialBarcodeMapper.findList(searchWmsInnerMaterialBarcode);
    }

    @Override
    public List<WmsInnerMaterialBarcodeDto> add(WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getMaterialId())){
            throw new BizErrorException("??????????????????????????????");
        }
        if(StringUtils.isEmpty(wmsInnerMaterialBarcodeDto.getPrintQty())){
            throw new BizErrorException("??????????????????????????????");
        }

        SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();

        if(wmsInnerMaterialBarcodeDto.getBarcodeRuleSetId() !=0 ){
            //??????????????????????????????????????????????????????????????????????????????????????????
            SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet = new SearchBaseBarcodeRuleSetDet();
            searchBaseBarcodeRuleSetDet.setBarcodeRuleSetId(wmsInnerMaterialBarcodeDto.getBarcodeRuleSetId());
            ResponseEntity<List<BaseBarcodeRuleSetDetDto>> barcodeRuleSetDetList = baseFeignApi.findBarcodeRuleSetDetList(searchBaseBarcodeRuleSetDet);
            if(StringUtils.isEmpty(barcodeRuleSetDetList.getData())) throw new BizErrorException("???????????????????????????");

            SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
            for (BaseBarcodeRuleSetDetDto dto : barcodeRuleSetDetList.getData()) {
                searchBaseBarcodeRule.setBarcodeRuleId(dto.getBarcodeRuleId());

                List<BaseBarcodeRuleDto> baseBarcodeRuleDtos = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
                if(StringUtils.isNotEmpty(baseBarcodeRuleDtos) && "????????????".equals(baseBarcodeRuleDtos.get(0).getLabelCategoryName()))
                    searchBaseBarcodeRuleSpec.setBarcodeRuleId(barcodeRuleSetDetList.getData().get(0).getBarcodeRuleId());
            }
            if(StringUtils.isEmpty(searchBaseBarcodeRuleSpec.getBarcodeRuleId())) {
                BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
                searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
            }
            //if(barcodeRuleSetDetList.getData().size()>1) throw new BizErrorException("??????????????????????????????????????????????????????????????????");

        }else{
            //???????????????????????????
            BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
            searchBaseBarcodeRuleSpec.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
        }

        ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecList= baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
        if(barcodeRuleSpecList.getCode()!=0) throw new BizErrorException(barcodeRuleSpecList.getMessage());
        if(barcodeRuleSpecList.getData().size()<1) throw new BizErrorException("?????????????????????");
        List<BaseBarcodeRuleSpec>  list = barcodeRuleSpecList.getData();

        //?????????????????????????????????????????????
        List<WmsInnerMaterialBarcodeDto> materialBarcodeList = new ArrayList<>();
        for(int i=0;i< wmsInnerMaterialBarcodeDto.getBarCodeQty();i++) {
            WmsInnerMaterialBarcodeDto  wmsInnerMaterialBarCode = new WmsInnerMaterialBarcodeDto();
            BeanUtils.autoFillEqFields(wmsInnerMaterialBarcodeDto,wmsInnerMaterialBarCode);
            String barCode = creatBarCode(list, wmsInnerMaterialBarcodeDto.getMaterialCode(), wmsInnerMaterialBarcodeDto.getMaterialId());
            wmsInnerMaterialBarCode.setBarcode(barCode);
            wmsInnerMaterialBarCode.setBarcodeRuleId(list.get(0).getBarcodeRuleId());
            wmsInnerMaterialBarCode.setOrgId(sysUser.getOrganizationId());
            wmsInnerMaterialBarCode.setCreateTime(new Date());
            wmsInnerMaterialBarCode.setCreateUserId(sysUser.getUserId());
            wmsInnerMaterialBarCode.setModifiedTime(new Date());
            wmsInnerMaterialBarCode.setModifiedUserId(sysUser.getUserId());
            wmsInnerMaterialBarcodeMapper.insertUseGeneratedKeys(wmsInnerMaterialBarCode);
            materialBarcodeList.add(wmsInnerMaterialBarCode);
        }
        return materialBarcodeList;
    }

    //????????????
    private String creatBarCode(List<BaseBarcodeRuleSpec> list,String materialCode,Long materialId){
        String lastBarCode = null;
        boolean hasKey = redisUtil.hasKey(this.sub(list));
        if(hasKey){
            // ???redis????????????????????????
            Object redisRuleData = redisUtil.get(this.sub(list));
            lastBarCode = String.valueOf(redisRuleData);
        }
        //?????????????????????
        String maxCode = baseFeignApi.generateMaxCode(list, lastBarCode).getData();
        //????????????
        ResponseEntity<String> rs = baseFeignApi.generateCode(list,maxCode,materialCode,materialId.toString());
        if(rs.getCode()!=0){
            throw new BizErrorException(rs.getMessage());
        }

        // ??????redis????????????
        redisUtil.set(sub(list), rs.getData());

        return rs.getData();
    }


    @Override
    public LabelRuteDto findLabelRute(Long barcodeRuleSetId,Long materialId) {
        SysUser sysUser = currentUser();
        SearchSysSpecItem lableItem = new SearchSysSpecItem();
        lableItem.setSpecCode("BaseLabel");
        ResponseEntity<List<SysSpecItem>> lableList = securityFeignApi.findSpecItemList(lableItem);
        if(StringUtils.isEmpty(lableList.getData())) throw new BizErrorException("?????????????????????");
        LabelRuteDto labelRuteDto = wmsInnerMaterialBarcodeMapper.findRule(lableList.getData().get(0).getParaValue(),materialId,sysUser.getOrganizationId());
        if(StringUtils.isEmpty(labelRuteDto)) throw new BizErrorException("???????????????");

        if(barcodeRuleSetId != 0) {
            SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet = new SearchBaseBarcodeRuleSetDet();
            searchBaseBarcodeRuleSetDet.setBarcodeRuleSetId(barcodeRuleSetId);
            ResponseEntity<List<BaseBarcodeRuleSetDetDto>> barcodeRuleSetDetList = baseFeignApi.findBarcodeRuleSetDetList(searchBaseBarcodeRuleSetDet);
            if (StringUtils.isEmpty(barcodeRuleSetDetList.getData())) throw new BizErrorException("???????????????????????????");
            SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
            for (BaseBarcodeRuleSetDetDto dto : barcodeRuleSetDetList.getData()) {
                searchBaseBarcodeRule.setBarcodeRuleId(dto.getBarcodeRuleId());
                List<BaseBarcodeRuleDto> baseBarcodeRuleDtos = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
                if(StringUtils.isNotEmpty(baseBarcodeRuleDtos) && "????????????".equals(baseBarcodeRuleDtos.get(0).getLabelCategoryName())) {
                    labelRuteDto.setBarcodeRuleId(baseBarcodeRuleDtos.get(0).getBarcodeRuleId());
                    labelRuteDto.setBarcodeRule(baseBarcodeRuleDtos.get(0).getBarcodeRule());
                }
            }
            if(StringUtils.isEmpty(labelRuteDto.getBarcodeRuleId())) {
                BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
                labelRuteDto.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
                labelRuteDto.setBarcodeRule(baseBarCode.getBarcodeRule());
            }
            /*if (barcodeRuleSetDetList.getData().size() > 1) throw new BizErrorException("??????????????????????????????????????????????????????????????????");
            SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
            searchBaseBarcodeRule.setBarcodeRuleSetId(barcodeRuleSetId);
            ResponseEntity<List<BaseBarcodeRuleDto>>  barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule);
            if (StringUtils.isEmpty(barcodeRulList.getData())) throw new BizErrorException("?????????????????????");
            labelRuteDto.setBarcodeRuleId(barcodeRulList.getData().get(0).getBarcodeRuleId());*/
        }else{
            //???id???????????????????????????

            BaseBarcodeRuleDto baseBarCode = getBaseBarCode();
            labelRuteDto.setBarcodeRuleId(baseBarCode.getBarcodeRuleId());
            labelRuteDto.setBarcodeRule(baseBarCode.getBarcodeRule());
        }

        return labelRuteDto;
      //  return barcodeRulList.getData().get(0);
    }

    /**
     * ??????/????????????
     * @param ids ??????????????????
     * @return 1
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(String ids, int printQty) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            //??????????????????
            WmsInnerMaterialBarcode wmsInnerMaterialBarcode = wmsInnerMaterialBarcodeMapper.selectByPrimaryKey(s);
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialId(wmsInnerMaterialBarcode.getMaterialId());
            ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
            Long setId = (long)0;
            if(StringUtils.isNotEmpty(list.getData()) && StringUtils.isNotEmpty(list.getData().get(0).getBarcodeRuleSetId()))
                setId = list.getData().get(0).getBarcodeRuleSetId();

            LabelRuteDto labelRuteDto = this.findLabelRute(setId,wmsInnerMaterialBarcode.getMaterialId());
            if(StringUtils.isEmpty(labelRuteDto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"?????????????????????????????????");
            }
            PrintModel printModel = wmsInnerMaterialBarcodeMapper.findPrintModel(wmsInnerMaterialBarcode.getMaterialBarcodeId());
            printModel.setSize(printQty);
            PrintDto printDto = new PrintDto();
            printDto.setLabelName(labelRuteDto.getLabelName());
            printDto.setLabelVersion(labelRuteDto.getLabelVersion());
         //   printDto.setPrintName();
            List<PrintModel> printModelList = new ArrayList<>();
            printModelList.add(printModel);
            printDto.setPrintModelList(printModelList);
            sfcFeignApi.print(printDto);
        }
        return 1;
    }


    /**
     * ????????????????????????
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

    //??????????????????
    public BaseBarcodeRuleDto getBaseBarCode(){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("BaseBarCodeRule");
        ResponseEntity<List<SysSpecItem>> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        if(StringUtils.isEmpty(specItemList.getData())) throw new BizErrorException("????????????????????????????????????????????????");
        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
        searchBaseBarcodeRule.setBarcodeRuleCode(specItemList.getData().get(0).getParaValue());
        ResponseEntity<List<BaseBarcodeRuleDto>> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule);
        if(StringUtils.isEmpty(barcodeRulList.getData())) throw new BizErrorException("????????????????????????????????????????????????");
        return barcodeRulList.getData().get(0);
    }
}

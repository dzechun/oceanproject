package com.fantechs.provider.guest.eng.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummaryDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.guest.eng.mapper.*;
import com.fantechs.provider.guest.eng.service.EngPackingOrderSummaryDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class EngPackingOrderSummaryDetServiceImpl extends BaseService<EngPackingOrderSummaryDet> implements EngPackingOrderSummaryDetService {

    @Resource
    private EngPackingOrderSummaryDetMapper engPackingOrderSummaryDetMapper;
    @Resource
    private EngHtPackingOrderSummaryDetMapper engHtPackingOrderSummaryDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EngPackingOrderSummaryMapper engPackingOrderSummaryMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;
    @Resource
    private EngPurchaseReqOrderMapper engPurchaseReqOrderMapper;

    @Override
    public List<EngPackingOrderSummaryDetDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());
        List<BaseSupplierReUser> supplier = getSupplier(user.getUserId());
        if(StringUtils.isNotEmpty(supplier))
            map.put("supplierId",supplier.get(0).getSupplierId());
        return engPackingOrderSummaryDetMapper.findList(map);
    }

    public int save(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto) {
        if(StringUtils.isEmpty(engPackingOrderSummaryDetDto.getCartonCode()))
            throw new BizErrorException("包装箱号不能为空");
        SysUser user = getUser();
        engPackingOrderSummaryDetDto.setStatus((byte)1);
        engPackingOrderSummaryDetDto.setOrgId(user.getOrganizationId());

        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(engPackingOrderSummaryDetDto.getPackingOrderSummaryId());

        getMaterial(engPackingOrderSummaryDetDto,user,engPackingOrderSummary);

        if(StringUtils.isNotEmpty(engPackingOrderSummary)){
            engPackingOrderSummaryDetDto.setPackingOrderSummaryId(engPackingOrderSummary.getPackingOrderSummaryId());
        }else{
            throw new BizErrorException("添加失败，未查询到上级数据");
        }

        int i = engPackingOrderSummaryDetMapper.insertUseGeneratedKeys(engPackingOrderSummaryDetDto);

        EngHtPackingOrderSummaryDet engHtPackingOrderSummaryDet =new EngHtPackingOrderSummaryDet();
        BeanUtils.copyProperties(engPackingOrderSummaryDetDto, engHtPackingOrderSummaryDet);
        engHtPackingOrderSummaryDetMapper.insertSelective(engHtPackingOrderSummaryDet);
        return i;
    }

    @Override
    public int batchAdd(List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos) {
        List<EngPackingOrderSummaryDet> ins = new ArrayList<EngPackingOrderSummaryDet>();
        List<EngHtPackingOrderSummaryDet> engHtPackingOrderSummaryDets = new ArrayList<EngHtPackingOrderSummaryDet>();
        SysUser user = getUser();
        int result =1;
        for(EngPackingOrderSummaryDetDto det : engPackingOrderSummaryDetDtos) {

          //  EngPackingOrderSummary engPackingOrderSummary = getEngPackingOrderSummary(user.getOrganizationId(), det.getCartonCode());
            EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(det.getPackingOrderSummaryId());
            if(StringUtils.isNotEmpty(engPackingOrderSummary)) {
                getMaterial(det, user, engPackingOrderSummary);
            }else{
                throw new BizErrorException("添加失败，未查询到上级数据");
            }
            if(StringUtils.isNotEmpty(det.getPackingOrderSummaryDetId())){
                int i = engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(det);
                if (i<1) result=i;
                continue;
            }else{
                det.setCreateTime(new Date());
                det.setCreateUserId(user.getUserId());
                det.setModifiedTime(new Date());
                det.setModifiedUserId(user.getUserId());
                det.setStatus((byte)1);
                det.setOrgId(user.getOrganizationId());
                ins.add(det);
                EngHtPackingOrderSummaryDet engHtPackingOrderSummaryDet =new EngHtPackingOrderSummaryDet();
                BeanUtils.copyProperties(det, engHtPackingOrderSummaryDet);
                engHtPackingOrderSummaryDets.add(engHtPackingOrderSummaryDet);
            }

        }
        if(StringUtils.isNotEmpty(ins)) {
           int i = engPackingOrderSummaryDetMapper.insertList(ins);
            if (i<1) result=i;
        }
        //新增历史信息
        if(StringUtils.isNotEmpty(engHtPackingOrderSummaryDets))
            engHtPackingOrderSummaryDetMapper.insertList(engHtPackingOrderSummaryDets);

            return result;


    }

    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }

    public List<BaseSupplierReUser> getSupplier(Long userId){
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(userId);
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        return list.getData();
    }

    //获取物料id ，未查询到则根据编码或者规则生成
    public void getMaterial(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto,SysUser user,EngPackingOrderSummary engPackingOrderSummary) {
        ResponseEntity<BaseMaterial> baseMaterialResponseEntity = null;
        BaseMaterial baseMaterial = new BaseMaterial();
        if (StringUtils.isEmpty(engPackingOrderSummaryDetDto.getMaterialCode())) {

            //按照规则自动生成编码
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("Specialities");
            List<SysSpecItem> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isEmpty(specItemList )) throw new BizErrorException("导入失败，未查询到专业编码");
            List<Map<String, String>> itemList = JSONArray.parseObject(specItemList.get(0).getParaValue(), List.class);
            String header = "";
            // for(int i=0;i<paraValue.size();i++){
            for(Map map : itemList){
                if(engPackingOrderSummary.getProfessionCode().equals(map.get("name")))
                    header = map.get("code").toString();
            }
            SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
            searchBaseBarcodeRule.setBarcodeRuleCode("装箱清单条码规则");
            List<BaseBarcodeRuleDto> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
            if(StringUtils.isEmpty(barcodeRulList)) throw new BizErrorException("未配置《装箱清单条码规则》");
            SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
            searchBaseBarcodeRuleSpec.setBarcodeRuleId(barcodeRulList.get(0).getBarcodeRuleId());
            ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecList= baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
            if(barcodeRuleSpecList.getCode()!=0) throw new BizErrorException(barcodeRuleSpecList.getMessage());
            if(barcodeRuleSpecList.getData().size()<1) throw new BizErrorException("请设置条码规则");
            List<BaseBarcodeRuleSpec>  list = barcodeRuleSpecList.getData();
            String code = header + creatBarCode(list, "", (long) 0);
            logger.info("----------自动生成流水号code---------"+code);
            baseMaterial.setMaterialName(engPackingOrderSummaryDetDto.getMaterialName());
            baseMaterial.setMaterialCode(code);
            baseMaterial.setMaterialDesc(engPackingOrderSummaryDetDto.getSpec());
            baseMaterial.setRemark(engPackingOrderSummaryDetDto.getUnitName());
            baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
            engPackingOrderSummaryDetDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
        } else {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(engPackingOrderSummaryDetDto.getMaterialCode());
            searchBaseMaterial.setOrganizationId(user.getOrganizationId());
            ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(list.getData())) {
                engPackingOrderSummaryDetDto.setMaterialId(list.getData().get(0).getMaterialId());
            } else {
                baseMaterial.setMaterialName(engPackingOrderSummaryDetDto.getMaterialName());
                baseMaterial.setMaterialCode(engPackingOrderSummaryDetDto.getMaterialCode());
                baseMaterial.setMaterialDesc(engPackingOrderSummaryDetDto.getSpec());
                baseMaterial.setRemark(engPackingOrderSummaryDetDto.getUnitName());
                baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
                engPackingOrderSummaryDetDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
            }
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EngPackingOrderSummaryDetImport> engPackingOrderSummaryDetImports,Long packingOrderSummaryId) {
        SysUser user = getUser();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EngPackingOrderSummaryDetDto> list = new LinkedList<>();
        LinkedList<EngHtPackingOrderSummaryDet> htList = new LinkedList<>();
        Long engPackingOrderId = engPackingOrderSummaryMapper.selectByPrimaryKey(packingOrderSummaryId).getPackingOrderId();
        for (int i = 0; i < engPackingOrderSummaryDetImports.size(); i++) {
            EngPackingOrderSummaryDetImport engPackingOrderSummaryDetImport = engPackingOrderSummaryDetImports.get(i);

            //非空校验
            String cartonCode = engPackingOrderSummaryDetImport.getCartonCode();//
            String purchaseReqOrderCode = engPackingOrderSummaryDetImport.getPurchaseReqOrderCode();
            String contractCode = engPackingOrderSummaryDetImport.getContractCode();
            String materialName = engPackingOrderSummaryDetImport.getMaterialName();
            String unitName = engPackingOrderSummaryDetImport.getUnitName();//
            String dominantTermCode = engPackingOrderSummaryDetImport.getDominantTermCode();//
            if (StringUtils.isEmpty(
                    cartonCode,materialName,purchaseReqOrderCode,contractCode,unitName,dominantTermCode
            )){
                throw new BizErrorException("添加失败，箱号、请购单号、合同号、货物名称、单位、主项号不能为空");
               /* fail.add(i+2);
                continue;*/
            }
            EngPackingOrderSummary engPackingOrderSummary = getEngPackingOrderSummary(user.getOrganizationId(), cartonCode, engPackingOrderId);
            //判断各参数是否大于0
            BigDecimal qty = engPackingOrderSummaryDetImport.getQty();
            if(qty.compareTo(BigDecimal.ZERO)<0 ){
                throw new BizErrorException("添加失败，数量必须大于0"+"错误行数为:"+(i+2));
                /*fail.add(i+2);
                continue;*/
            }

            //装箱汇总明细的包装箱号需与装箱汇总的包装箱号一致
            if (!cartonCode.equals(engPackingOrderSummary.getCartonCode()) || !contractCode.equals(engPackingOrderSummary.getContractCode())
                    || !purchaseReqOrderCode.equals(engPackingOrderSummary.getPurchaseReqOrderCode())
            ){
                throw new BizErrorException("添加失败，包装箱号、合同号、请购单号必须和装箱汇总一致"+"错误行数为:"+(i+2));
                /*fail.add(i+2);
                continue;*/
            }
            EngPackingOrderSummaryDetDto dto = new EngPackingOrderSummaryDetDto();
            BeanUtils.copyProperties(engPackingOrderSummaryDetImport, dto);


            //材料编码、原材料编码二者不能都为空，必须有一个有值，而且存在于物料表中
            if (StringUtils.isEmpty(engPackingOrderSummaryDetImport.getMaterialCode()) && StringUtils.isEmpty(engPackingOrderSummaryDetImport.getRawMaterialCode())){
                /*fail.add(i+2);
                continue;*/
                throw new BizErrorException("添加失败，材料编码、原材料编码二者不能同时为空"+"错误行数为:"+(i+2));
            }else if (StringUtils.isEmpty(engPackingOrderSummaryDetImport.getMaterialCode()) && StringUtils.isNotEmpty(engPackingOrderSummaryDetImport.getRawMaterialCode())){
                if(!"管道".equals(engPackingOrderSummary.getProfessionName())){
                    /*fail.add(i+2);
                    continue;*/
                    throw new BizErrorException("添加失败，专业不等于管道时，原材料编码不能为空"+"错误行数为:"+(i+2));
                }
                continue;
            }

            //校验合同量单
            Example qtyExample = new Example(EngContractQtyOrder.class);
            Example.Criteria qtyCriteria = qtyExample.createCriteria();
            qtyCriteria.andEqualTo("contractCode",contractCode);
            qtyCriteria.andEqualTo("dominantTermCode",engPackingOrderSummaryDetImport.getDominantTermCode());
            if(StringUtils.isNotEmpty(engPackingOrderSummaryDetImport.getMaterialCode())){
                qtyCriteria.andEqualTo("materialCode",engPackingOrderSummaryDetImport.getMaterialCode());
            }else if(StringUtils.isEmpty(engPackingOrderSummaryDetImport.getMaterialCode()) && StringUtils.isNotEmpty(engPackingOrderSummaryDetImport.getRawMaterialCode())){
                qtyCriteria.andEqualTo("materialCode",engPackingOrderSummaryDetImport.getRawMaterialCode());
            }
            List<EngContractQtyOrder> engContractQtyOrders = engContractQtyOrderMapper.selectByExample(qtyExample);
            if(StringUtils.isEmpty(engContractQtyOrders)){
                throw new BizErrorException("添加失败，未查询到对应的合同量单"+"错误行数为:"+(i+2));
               /* fail.add(i+2);
                continue;*/
            }

            //校验请购单
            Example orderExample = new Example(EngPurchaseReqOrder.class);
            Example.Criteria orderCriteria = orderExample.createCriteria();
            orderCriteria.andEqualTo("purchaseReqOrderCode",engPackingOrderSummaryDetImport.getPurchaseReqOrderCode());
            orderCriteria.andEqualTo("option3",engContractQtyOrders.get(0).getOption3());
            List<EngPurchaseReqOrder> engPurchaseReqOrders = engPurchaseReqOrderMapper.selectByExample(orderExample);
            if(StringUtils.isEmpty(engPurchaseReqOrders)){
                throw new BizErrorException("添加失败，未查询到对应的请购单号"+"错误行数为:"+(i+2));
                /*fail.add(i+2);
                continue;*/
            }




            getMaterial(dto,user,engPackingOrderSummary);



            //判断集合中是否已经存在同样的数据---所有字段可重复，无法校验
/*            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : list) {
                    if (engPackingOrderSummaryDetDto.getMaterialName().equals(engPackingOrderSummaryDetImport.getMaterialName())){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+2);
                continue;
            }*/

            dto.setPackingOrderSummaryId(engPackingOrderSummary.getPackingOrderSummaryId());
            dto.setCreateTime(new Date());
            dto.setCreateUserId(user.getUserId());
            dto.setModifiedTime(new Date());
            dto.setModifiedUserId(user.getUserId());
            dto.setStatus((byte)1);
            dto.setOrgId(user.getOrganizationId());
            list.add(dto);
        }

        if (StringUtils.isNotEmpty(list)){
            success = engPackingOrderSummaryDetMapper.insertList(list);
        }

        for (EngPackingOrderSummaryDet engPackingOrderSummaryDet : list) {
            EngHtPackingOrderSummaryDet engHtPackingOrderSummaryDet = new EngHtPackingOrderSummaryDet();
            BeanUtils.copyProperties(engPackingOrderSummaryDet, engHtPackingOrderSummaryDet);
            htList.add(engHtPackingOrderSummaryDet);
        }
        if (StringUtils.isNotEmpty(htList)){
            engHtPackingOrderSummaryDetMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    public EngPackingOrderSummary getEngPackingOrderSummary(Long userId, String code,Long packingOrderId){
        Example example = new Example(EngPackingOrderSummary.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", userId);
        criteria.andEqualTo("cartonCode", code);
        criteria.andEqualTo("packingOrderId", packingOrderId);
        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectOneByExample(example);
        return engPackingOrderSummary;
    }

    //生成条码
    private String creatBarCode(List<BaseBarcodeRuleSpec> list, String materialCode, Long materialId){
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


    public String sub(List<BaseBarcodeRuleSpec> list){
        StringBuffer sb = new StringBuffer();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            sb.append(baseBarcodeRuleSpec.getSpecification());
        }
        return sb.toString();
    }


    public void check(EngPackingOrderSummaryDetDto dto, SysUser user,EngPackingOrderSummary engPackingOrderSummary){
        if (StringUtils.isEmpty(dto.getCartonCode()))
            throw new BizErrorException("添加失败，包装箱号不能为空");
        if (StringUtils.isEmpty(dto.getDominantTermCode()))
            throw new BizErrorException("添加失败，主项号不能为空");
        if (StringUtils.isEmpty(dto.getQty()))
            throw new BizErrorException("添加失败，数量不能为空");
        if (StringUtils.isEmpty(dto.getUnitName()))
            throw new BizErrorException("添加失败，单位名称不能为空");
        if (StringUtils.isEmpty(dto.getMaterialName()))
            throw new BizErrorException("添加失败，货物名称不能为空");

        //判断参数是否大于0
        BigDecimal netWeight = dto.getQty();
        if(netWeight.compareTo(BigDecimal.ZERO)<0)
            throw new BizErrorException("添加失败，长宽高等参数必须大于0");

        if (!dto.getCartonCode().equals(engPackingOrderSummary.getCartonCode()))
            throw new BizErrorException("添加失败，包装箱号不一致");

        //材料编码、原材料编码二者不能都为空，必须有一个有值，而且存在于物料表中
        if (StringUtils.isEmpty(dto.getMaterialCode()) && StringUtils.isEmpty(dto.getRawMaterialCode())){
            throw new BizErrorException("添加失败，材料编码、原材料编码二者不能都为空");
        }else{
            if("管道".equals(engPackingOrderSummary.getProfessionCode())&& StringUtils.isEmpty(dto.getMaterialCode())){
                throw new BizErrorException("添加失败，专业为管道时材料编码不能为空");
            }else if(!"管道".equals(engPackingOrderSummary.getProfessionCode())&& StringUtils.isEmpty(dto.getRawMaterialCode())){
                throw new BizErrorException("添加失败，专业为非管道时原材料编码不能为空");
            }
        }
    }

}


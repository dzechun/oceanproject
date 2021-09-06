package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummary;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngHtPackingOrderSummaryMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryMapper;
import com.fantechs.provider.guest.eng.service.EngPackingOrderSummaryService;
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
public class EngPackingOrderSummaryServiceImpl extends BaseService<EngPackingOrderSummary> implements EngPackingOrderSummaryService {

    @Resource
    private EngPackingOrderSummaryMapper engPackingOrderSummaryMapper;
    @Resource
    private EngHtPackingOrderSummaryMapper engHtPackingOrderSummaryMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EngPackingOrderMapper engPackingOrderMapper;
    @Resource
    private EngPackingOrderSummaryDetMapper engPackingOrderSummaryDetMapper;


    @Override
    public List<EngPackingOrderSummaryDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());

        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(user.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if(StringUtils.isNotEmpty(list.getData()))
            map.put("supplierId",list.getData().get(0).getSupplierId());

        return engPackingOrderSummaryMapper.findList(map);
    }

    public int save(EngPackingOrderSummaryDto engPackingOrderSummaryDto) {
        SysUser user = getUser();
        engPackingOrderSummaryDto.setCreateTime(new Date());
        engPackingOrderSummaryDto.setCreateUserId(user.getUserId());
        engPackingOrderSummaryDto.setModifiedTime(new Date());
        engPackingOrderSummaryDto.setModifiedUserId(user.getUserId());
        engPackingOrderSummaryDto.setStatus((byte)1);
        engPackingOrderSummaryDto.setOrgId(user.getOrganizationId());
        getMaterial(engPackingOrderSummaryDto,user);

        EngPackingOrder engPackingOrder = getEngPackingOrder(user.getOrganizationId(), null,engPackingOrderSummaryDto.getPackingOrderId());
        if(StringUtils.isNotEmpty(engPackingOrder)){
            engPackingOrderSummaryDto.setPackingOrderSummaryId(engPackingOrder.getPackingOrderId());
        }else{
            throw new BizErrorException("添加失败，未查询到上级数据");
        }

        int i = engPackingOrderSummaryMapper.insertUseGeneratedKeys(engPackingOrderSummaryDto);

        EngHtPackingOrderSummary engHtPackingOrderSummary =new EngHtPackingOrderSummary();
        BeanUtils.copyProperties(engPackingOrderSummaryDto, engHtPackingOrderSummary);
        engHtPackingOrderSummaryMapper.insertSelective(engHtPackingOrderSummary);
        return i;
    }


    @Override
    public int batchAdd(List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos) {
        List<EngPackingOrderSummary> ins = new ArrayList<EngPackingOrderSummary>();
        List<EngHtPackingOrderSummary> engHtPackingOrderSummarys = new ArrayList<EngHtPackingOrderSummary>();
        SysUser user = getUser();
        int fail =0;
        for(EngPackingOrderSummaryDto dto : engPackingOrderSummaryDtos) {

            EngPackingOrder engPackingOrder = getEngPackingOrder(user.getOrganizationId(), null,dto.getPackingOrderId());
            if(StringUtils.isNotEmpty(engPackingOrder)){
                dto.setPackingOrderId(engPackingOrder.getPackingOrderId());
            }else{
               // throw new BizErrorException("添加失败，未查询到上级数据");
                fail = fail+1;
                continue;
            }

            getMaterial(dto,user);

            if (StringUtils.isNotEmpty(dto.getPackingOrderSummaryId())) {
                engPackingOrderSummaryMapper.updateByPrimaryKeySelective(dto);
            }else{
                dto.setCreateTime(new Date());
                dto.setCreateUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setStatus((byte)1);
                dto.setOrgId(user.getOrganizationId());
                ins.add(dto);
                EngHtPackingOrderSummary engHtPackingOrderSummary =new EngHtPackingOrderSummary();
                BeanUtils.copyProperties(dto, engHtPackingOrderSummary);
                engHtPackingOrderSummarys.add(engHtPackingOrderSummary);
            }

        }
        if(StringUtils.isNotEmpty(ins)) {
            engPackingOrderSummaryMapper.insertList(ins);
        }
        //新增历史信息
        if(StringUtils.isNotEmpty(engHtPackingOrderSummarys))
            engHtPackingOrderSummaryMapper.insertList(engHtPackingOrderSummarys);

        if(fail >0){
            throw new BizErrorException("添加完成，添加失败条数："+fail);
        }else{
            return 1;
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = getUser();

        List<EngHtPackingOrderSummary> htList = new ArrayList<>();
        String[] split = ids.split(",");
        for (String id : split){
            EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(engPackingOrderSummary)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //新增履历信息
            EngHtPackingOrderSummary engHtPackingOrderSummary = new EngHtPackingOrderSummary();
            BeanUtils.copyProperties(engPackingOrderSummary, engHtPackingOrderSummary);
            htList.add(engHtPackingOrderSummary);

            Example example = new Example(EngPackingOrderSummaryDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packingOrderSummaryId",id);
            engPackingOrderSummaryDetMapper.deleteByExample(example);
        }

        engHtPackingOrderSummaryMapper.insertList(htList);

        return engPackingOrderSummaryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EngPackingOrderSummaryImport> engPackingOrderSummaryImports,Long packingOrderId) {
        SysUser user = getUser();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EngPackingOrderSummary> list = new LinkedList<>();
        LinkedList<EngHtPackingOrderSummary> htList = new LinkedList<>();
        for (int i = 0; i < engPackingOrderSummaryImports.size(); i++) {
            EngPackingOrderSummaryImport engPackingOrderSummaryImport = engPackingOrderSummaryImports.get(i);

            String cartonCode = engPackingOrderSummaryImport.getCartonCode();
            String purchaseReqOrderCode = engPackingOrderSummaryImport.getPurchaseReqOrderCode();
            String contractCode = engPackingOrderSummaryImport.getContractCode();
            String materialName = engPackingOrderSummaryImport.getMaterialName();
            if (StringUtils.isEmpty(
                    cartonCode,purchaseReqOrderCode,materialName,contractCode
            )){
                fail.add(i+2);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (EngPackingOrderSummary engPackingOrderSummary : list) {
                    if (engPackingOrderSummary.getCartonCode().equals(cartonCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+2);
                continue;
            }

            //判断各参数是否大于0
            BigDecimal netWeight = engPackingOrderSummaryImport.getNetWeight();
            BigDecimal grossWeight = engPackingOrderSummaryImport.getGrossWeight();
            BigDecimal length = engPackingOrderSummaryImport.getLength();
            BigDecimal width = engPackingOrderSummaryImport.getWidth();
            BigDecimal height = engPackingOrderSummaryImport.getHeight();
            BigDecimal volume = engPackingOrderSummaryImport.getVolume();
            if(netWeight.compareTo(BigDecimal.ZERO)<0 || grossWeight.compareTo(BigDecimal.ZERO)<0 || length.compareTo(BigDecimal.ZERO)<0
                    ||width.compareTo(BigDecimal.ZERO)<0 || height.compareTo(BigDecimal.ZERO)<0 || volume.compareTo(BigDecimal.ZERO)<0 ){
                fail.add(i+2);
                continue;
            }


            EngPackingOrderSummaryDto dto = new EngPackingOrderSummaryDto();
            BeanUtils.copyProperties(engPackingOrderSummaryImport, dto);
            getMaterial(dto,user);

/*            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierName(engPackingOrderSummaryImport.getSupplierName());
            ResponseEntity<List<BaseSupplier>> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier);
            if(StringUtils.isNotEmpty(baseSuppliers.getData())) {
                dto.setSupplierId(baseSuppliers.getData().get(0).getSupplierId());
            }else {
                fail.add(i+2);
                continue;
            }*/

            //装箱单id
            dto.setPackingOrderId(packingOrderId);

            //判断编码是否重复
            Example example = new Example(EngPackingOrderSummary.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("cartonCode",cartonCode);
            criteria.andEqualTo("packingOrderId",dto.getPackingOrderId());
            if (StringUtils.isNotEmpty(engPackingOrderSummaryMapper.selectOneByExample(example))){
                fail.add(i+2);
                continue;
            }

            if(StringUtils.isEmpty(dto.getCartonQty()))
                dto.setCartonQty(1);
            dto.setCreateTime(new Date());
            dto.setCreateUserId(user.getUserId());
            dto.setModifiedTime(new Date());
            dto.setModifiedUserId(user.getUserId());
            dto.setStatus((byte)1);
            dto.setOrgId(user.getOrganizationId());
            list.add(dto);
        }

        if (StringUtils.isNotEmpty(list)){
            success = engPackingOrderSummaryMapper.insertList(list);
        }

        for (EngPackingOrderSummary engPackingOrderSummary : list) {
            EngHtPackingOrderSummary engHtPackingOrderSummary = new EngHtPackingOrderSummary();
            BeanUtils.copyProperties(engPackingOrderSummary, engHtPackingOrderSummary);
            htList.add(engHtPackingOrderSummary);
        }
        if (StringUtils.isNotEmpty(htList)){
            engHtPackingOrderSummaryMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
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
    public void getMaterial(EngPackingOrderSummaryDto engPackingOrderSummaryDto, SysUser user) {
        ResponseEntity<BaseMaterial> baseMaterialResponseEntity = null;
        if (StringUtils.isEmpty(engPackingOrderSummaryDto.getMaterialCode())) {
            BaseMaterial baseMaterial = new BaseMaterial();
            baseMaterial.setMaterialName(engPackingOrderSummaryDto.getMaterialName());
            Long startTs = System.currentTimeMillis();
            baseMaterial.setMaterialCode(startTs.toString());
            baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
            //  return baseMaterialResponseEntity.getData();
            engPackingOrderSummaryDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
        } else {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(engPackingOrderSummaryDto.getMaterialCode());
            searchBaseMaterial.setOrganizationId(user.getOrganizationId());
            ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(list.getData())) {
                engPackingOrderSummaryDto.setMaterialId(list.getData().get(0).getMaterialId());
                //    return list.getData().get(0);
            } else {
                BaseMaterial baseMaterial = new BaseMaterial();
                baseMaterial.setMaterialName(engPackingOrderSummaryDto.getMaterialName());
                baseMaterial.setMaterialCode(engPackingOrderSummaryDto.getMaterialCode());
                baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
                //    return baseMaterialResponseEntity.getData();
                engPackingOrderSummaryDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
            }
        }
    }

    public EngPackingOrder getEngPackingOrder(Long userId, String code, Long id){
        Example example = new Example(EngPackingOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", userId);
        if(StringUtils.isNotEmpty(code))
            criteria.andEqualTo("packingOrderCode", code);
        if(StringUtils.isNotEmpty(id))
            criteria.andEqualTo("packingOrderId", id);
        EngPackingOrder engPackingOrder = engPackingOrderMapper.selectOneByExample(example);
        return engPackingOrder;
    }

}

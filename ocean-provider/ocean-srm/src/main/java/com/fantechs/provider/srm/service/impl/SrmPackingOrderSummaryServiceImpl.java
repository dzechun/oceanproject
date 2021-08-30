package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummary;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderSummaryMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderSummaryMapper;
import com.fantechs.provider.srm.service.SrmPackingOrderSummaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class SrmPackingOrderSummaryServiceImpl extends BaseService<SrmPackingOrderSummary> implements SrmPackingOrderSummaryService {

    @Resource
    private SrmPackingOrderSummaryMapper srmPackingOrderSummaryMapper;
    @Resource
    private SrmHtPackingOrderSummaryMapper srmHtPackingOrderSummaryMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SrmPackingOrderMapper srmPackingOrderMapper;


    @Override
    public List<SrmPackingOrderSummaryDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());

        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(user.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if(StringUtils.isNotEmpty(list.getData()))
            map.put("supplierId",list.getData().get(0).getSupplierId());

        return srmPackingOrderSummaryMapper.findList(map);
    }

    public int save(SrmPackingOrderSummaryDto srmPackingOrderSummaryDto) {
        SysUser user = getUser();
        srmPackingOrderSummaryDto.setCreateTime(new Date());
        srmPackingOrderSummaryDto.setCreateUserId(user.getUserId());
        srmPackingOrderSummaryDto.setModifiedTime(new Date());
        srmPackingOrderSummaryDto.setModifiedUserId(user.getUserId());
        srmPackingOrderSummaryDto.setStatus((byte)1);
        srmPackingOrderSummaryDto.setOrgId(user.getOrganizationId());

        getMaterial(srmPackingOrderSummaryDto,user);

        SrmPackingOrder srmPackingOrder = getSrmPackingOrder(user.getOrganizationId(), null,srmPackingOrderSummaryDto.getPackingOrderId());
        if(StringUtils.isNotEmpty(srmPackingOrder)){
            srmPackingOrderSummaryDto.setPackingOrderSummaryId(srmPackingOrder.getPackingOrderId());
        }else{
            throw new BizErrorException("添加失败，未查询到上级数据");
        }

        int i = srmPackingOrderSummaryMapper.insertUseGeneratedKeys(srmPackingOrderSummaryDto);

        SrmHtPackingOrderSummary srmHtPackingOrderSummary =new SrmHtPackingOrderSummary();
        BeanUtils.copyProperties(srmPackingOrderSummaryDto, srmHtPackingOrderSummary);
        srmHtPackingOrderSummaryMapper.insertSelective(srmHtPackingOrderSummary);
        return i;
    }


    @Override
    public int batchAdd(List<SrmPackingOrderSummaryDto> srmPackingOrderSummaryDtos) {
        List<SrmPackingOrderSummary> ins = new ArrayList<SrmPackingOrderSummary>();
        List<SrmHtPackingOrderSummary> srmHtPackingOrderSummarys = new ArrayList<SrmHtPackingOrderSummary>();
        SysUser user = getUser();
        int fail =0;
        for(SrmPackingOrderSummaryDto dto : srmPackingOrderSummaryDtos) {
          /*  Example example = new Example(SrmPackingOrderSummary.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", dto.getOrgId());
            criteria.andEqualTo("cartonCode", dto.getCartonCode());
            SrmPackingOrderSummary OldSrmPackingOrderSummary = srmPackingOrderSummaryMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(OldSrmPackingOrderSummary)){
                fail = fail+1;
                continue;
            }*/

            SrmPackingOrder srmPackingOrder = getSrmPackingOrder(user.getOrganizationId(), null,dto.getPackingOrderId());
            if(StringUtils.isNotEmpty(srmPackingOrder)){
                dto.setPackingOrderId(srmPackingOrder.getPackingOrderId());
            }else{
               // throw new BizErrorException("添加失败，未查询到上级数据");
                fail = fail+1;
                continue;
            }

            getMaterial(dto,user);

            if (StringUtils.isNotEmpty(dto.getPackingOrderSummaryId())) {
                srmPackingOrderSummaryMapper.updateByPrimaryKeySelective(dto);
            }else{
                dto.setCreateTime(new Date());
                dto.setCreateUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setStatus((byte)1);
                dto.setOrgId(user.getOrganizationId());
                ins.add(dto);
                SrmHtPackingOrderSummary srmHtPackingOrderSummary =new SrmHtPackingOrderSummary();
                BeanUtils.copyProperties(dto, srmHtPackingOrderSummary);
                srmHtPackingOrderSummarys.add(srmHtPackingOrderSummary);
            }

        }
        if(StringUtils.isNotEmpty(ins)) {
            srmPackingOrderSummaryMapper.insertList(ins);
        }
        //新增历史信息
        if(StringUtils.isNotEmpty(srmHtPackingOrderSummarys))
            srmHtPackingOrderSummaryMapper.insertList(srmHtPackingOrderSummarys);

        if(fail >0){
            throw new BizErrorException("添加完成，添加失败条数："+fail);
        }else{
            return 1;
        }


    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SrmPackingOrderSummaryImport> srmPackingOrderSummaryImports) {
        SysUser user = getUser();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SrmPackingOrderSummary> list = new LinkedList<>();
        LinkedList<SrmHtPackingOrderSummary> htList = new LinkedList<>();
        for (int i = 0; i < srmPackingOrderSummaryImports.size(); i++) {
            SrmPackingOrderSummaryImport srmPackingOrderSummaryImport = srmPackingOrderSummaryImports.get(i);

            String cartonCode = srmPackingOrderSummaryImport.getCartonCode();
            String supplierName = srmPackingOrderSummaryImport.getSupplierName();
            String packingOrderCode = srmPackingOrderSummaryImport.getPackingOrderCode();
            if (StringUtils.isEmpty(
                    cartonCode,supplierName,packingOrderCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (SrmPackingOrderSummary srmPackingOrderSummary : list) {
                    if (srmPackingOrderSummary.getCartonCode().equals(cartonCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            SrmPackingOrderSummaryDto dto = new SrmPackingOrderSummaryDto();
            BeanUtils.copyProperties(srmPackingOrderSummaryImport, dto);
            getMaterial(dto,user);

            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierName(srmPackingOrderSummaryImport.getSupplierName());
            ResponseEntity<List<BaseSupplier>> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier);
            if(StringUtils.isNotEmpty(baseSuppliers.getData())) {
                dto.setSupplierId(baseSuppliers.getData().get(0).getSupplierId());
            }else {
                fail.add(i+4);
                continue;
            }


            SrmPackingOrder srmPackingOrder = getSrmPackingOrder(user.getOrganizationId(), packingOrderCode,null);
            if(StringUtils.isNotEmpty(srmPackingOrder)){
                dto.setPackingOrderId(srmPackingOrder.getPackingOrderId());
            }else{
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SrmPackingOrderSummary.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("cartonCode",cartonCode);
            criteria.andEqualTo("packingOrderId",dto.getPackingOrderId());
            if (StringUtils.isNotEmpty(srmPackingOrderSummaryMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            dto.setCreateTime(new Date());
            dto.setCreateUserId(user.getUserId());
            dto.setModifiedTime(new Date());
            dto.setModifiedUserId(user.getUserId());
            dto.setStatus((byte)1);
            dto.setOrgId(user.getOrganizationId());
            list.add(dto);
        }

        if (StringUtils.isNotEmpty(list)){
            success = srmPackingOrderSummaryMapper.insertList(list);
        }

        for (SrmPackingOrderSummary srmPackingOrderSummary : list) {
            SrmHtPackingOrderSummary srmHtPackingOrderSummary = new SrmHtPackingOrderSummary();
            BeanUtils.copyProperties(srmPackingOrderSummary, srmHtPackingOrderSummary);
            htList.add(srmHtPackingOrderSummary);
        }
        if (StringUtils.isNotEmpty(htList)){
            srmHtPackingOrderSummaryMapper.insertList(htList);
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
    public void getMaterial(SrmPackingOrderSummaryDto srmPackingOrderSummaryDto, SysUser user) {
        ResponseEntity<BaseMaterial> baseMaterialResponseEntity = null;
        if (StringUtils.isEmpty(srmPackingOrderSummaryDto.getMaterialCode())) {
            BaseMaterial baseMaterial = new BaseMaterial();
            baseMaterial.setMaterialName(srmPackingOrderSummaryDto.getMaterialName());
            Long startTs = System.currentTimeMillis();
            baseMaterial.setMaterialCode(startTs.toString());
            baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
            //  return baseMaterialResponseEntity.getData();
            srmPackingOrderSummaryDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
        } else {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(srmPackingOrderSummaryDto.getMaterialCode());
            searchBaseMaterial.setOrganizationId(user.getOrganizationId());
            ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(list.getData())) {
                srmPackingOrderSummaryDto.setMaterialId(list.getData().get(0).getMaterialId());
                //    return list.getData().get(0);
            } else {
                BaseMaterial baseMaterial = new BaseMaterial();
                baseMaterial.setMaterialName(srmPackingOrderSummaryDto.getMaterialName());
                baseMaterial.setMaterialCode(srmPackingOrderSummaryDto.getMaterialCode());
                baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
                //    return baseMaterialResponseEntity.getData();
                srmPackingOrderSummaryDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
            }
        }
    }

    public SrmPackingOrder getSrmPackingOrder(Long userId, String code, Long id){
        Example example = new Example(SrmPackingOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", userId);
        if(StringUtils.isNotEmpty(code))
            criteria.andEqualTo("packingOrderCode", code);
        if(StringUtils.isNotEmpty(id))
            criteria.andEqualTo("packingOrderId", id);
        SrmPackingOrder srmPackingOrder = srmPackingOrderMapper.selectOneByExample(example);
        return srmPackingOrder;
    }

}

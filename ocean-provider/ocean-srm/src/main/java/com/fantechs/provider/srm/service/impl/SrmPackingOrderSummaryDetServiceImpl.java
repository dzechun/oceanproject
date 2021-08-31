package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummaryDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderSummaryDetMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderSummaryDetMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderSummaryMapper;
import com.fantechs.provider.srm.service.SrmPackingOrderSummaryDetService;
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
public class SrmPackingOrderSummaryDetServiceImpl extends BaseService<SrmPackingOrderSummaryDet> implements SrmPackingOrderSummaryDetService {

    @Resource
    private SrmPackingOrderSummaryDetMapper srmPackingOrderSummaryDetMapper;
    @Resource
    private SrmHtPackingOrderSummaryDetMapper srmHtPackingOrderSummaryDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SrmPackingOrderSummaryMapper srmPackingOrderSummaryMapper;


    @Override
    public List<SrmPackingOrderSummaryDetDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());
        List<BaseSupplierReUser> supplier = getSupplier(user.getUserId());
        if(StringUtils.isNotEmpty(supplier))
            map.put("supplierId",supplier.get(0).getSupplierId());
        return srmPackingOrderSummaryDetMapper.findList(map);
    }

    public int save(SrmPackingOrderSummaryDetDto srmPackingOrderSummaryDetDto) {
        if(StringUtils.isEmpty(srmPackingOrderSummaryDetDto.getCartonCode()))
            throw new BizErrorException("包装箱号不能为空");
        SysUser user = getUser();
        srmPackingOrderSummaryDetDto.setCreateTime(new Date());
        srmPackingOrderSummaryDetDto.setCreateUserId(user.getUserId());
        srmPackingOrderSummaryDetDto.setModifiedTime(new Date());
        srmPackingOrderSummaryDetDto.setModifiedUserId(user.getUserId());
        srmPackingOrderSummaryDetDto.setStatus((byte)1);
        srmPackingOrderSummaryDetDto.setOrgId(user.getOrganizationId());

        getMaterial(srmPackingOrderSummaryDetDto,user);

        SrmPackingOrderSummary srmPackingOrderSummary = getSrmPackingOrderSummary(user.getOrganizationId(), srmPackingOrderSummaryDetDto.getCartonCode());
        if(StringUtils.isNotEmpty(srmPackingOrderSummary)){
            srmPackingOrderSummaryDetDto.setPackingOrderSummaryId(srmPackingOrderSummary.getPackingOrderSummaryId());
        }else{
            throw new BizErrorException("添加失败，未查询到上级数据");
        }

        int i = srmPackingOrderSummaryDetMapper.insertUseGeneratedKeys(srmPackingOrderSummaryDetDto);

        SrmHtPackingOrderSummaryDet srmHtPackingOrderSummaryDet =new SrmHtPackingOrderSummaryDet();
        BeanUtils.copyProperties(srmPackingOrderSummaryDetDto, srmHtPackingOrderSummaryDet);
        srmHtPackingOrderSummaryDetMapper.insertSelective(srmHtPackingOrderSummaryDet);
        return i;
    }

    @Override
    public int batchAdd(List<SrmPackingOrderSummaryDetDto> srmPackingOrderSummaryDetDtos) {
        List<SrmPackingOrderSummaryDet> ins = new ArrayList<SrmPackingOrderSummaryDet>();
        List<SrmHtPackingOrderSummaryDet> srmHtPackingOrderSummaryDets = new ArrayList<SrmHtPackingOrderSummaryDet>();
        SysUser user = getUser();
        int fail =0;
        for(SrmPackingOrderSummaryDetDto det : srmPackingOrderSummaryDetDtos) {

            getMaterial(det,user);

            if(StringUtils.isNotEmpty(det.getPackingOrderSummaryDetId())){
                srmPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(det);
               continue;
            }else{
                det.setCreateTime(new Date());
                det.setCreateUserId(user.getUserId());
                det.setModifiedTime(new Date());
                det.setModifiedUserId(user.getUserId());
                det.setStatus((byte)1);
                det.setOrgId(user.getOrganizationId());
                ins.add(det);
                SrmHtPackingOrderSummaryDet srmHtPackingOrderSummaryDet =new SrmHtPackingOrderSummaryDet();
                BeanUtils.copyProperties(det, srmHtPackingOrderSummaryDet);
                srmHtPackingOrderSummaryDets.add(srmHtPackingOrderSummaryDet);
            }

        }
        if(StringUtils.isNotEmpty(ins)) {
            srmPackingOrderSummaryDetMapper.insertList(ins);
        }
        //新增历史信息
        if(StringUtils.isNotEmpty(srmHtPackingOrderSummaryDets))
            srmHtPackingOrderSummaryDetMapper.insertList(srmHtPackingOrderSummaryDets);

        if(fail >0){
            throw new BizErrorException("添加完成，添加失败条数："+fail);
        }else{
            return 1;
        }

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
    public void getMaterial(SrmPackingOrderSummaryDetDto srmPackingOrderSummaryDetDto,SysUser user) {
        ResponseEntity<BaseMaterial> baseMaterialResponseEntity = null;
        if (StringUtils.isEmpty(srmPackingOrderSummaryDetDto.getMaterialCode())) {
            BaseMaterial baseMaterial = new BaseMaterial();
            baseMaterial.setMaterialName(srmPackingOrderSummaryDetDto.getMaterialName());
            Long startTs = System.currentTimeMillis();
            baseMaterial.setMaterialCode(startTs.toString());
            baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
          //  return baseMaterialResponseEntity.getData();
            srmPackingOrderSummaryDetDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
        } else {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(srmPackingOrderSummaryDetDto.getMaterialCode());
            searchBaseMaterial.setOrganizationId(user.getOrganizationId());
            ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(list.getData())) {
                srmPackingOrderSummaryDetDto.setMaterialId(list.getData().get(0).getMaterialId());
            //    return list.getData().get(0);
            } else {
                BaseMaterial baseMaterial = new BaseMaterial();
                baseMaterial.setMaterialName(srmPackingOrderSummaryDetDto.getMaterialName());
                baseMaterial.setMaterialCode(srmPackingOrderSummaryDetDto.getMaterialCode());
                baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
            //    return baseMaterialResponseEntity.getData();
             srmPackingOrderSummaryDetDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
            }
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SrmPackingOrderSummaryDetImport> srmPackingOrderSummaryDetImports,Long packingOrderSummaryId) {
        SysUser user = getUser();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SrmPackingOrderSummaryDet> list = new LinkedList<>();
        LinkedList<SrmHtPackingOrderSummaryDet> htList = new LinkedList<>();
        for (int i = 0; i < srmPackingOrderSummaryDetImports.size(); i++) {
            SrmPackingOrderSummaryDetImport srmPackingOrderSummaryDetImport = srmPackingOrderSummaryDetImports.get(i);

            String cartonCode = srmPackingOrderSummaryDetImport.getCartonCode();
            String supplierName = srmPackingOrderSummaryDetImport.getSupplierName();
            if (StringUtils.isEmpty(
                    cartonCode,supplierName
            )){
                fail.add(i+2);
                continue;
            }

            SrmPackingOrderSummaryDetDto dto = new SrmPackingOrderSummaryDetDto();
            BeanUtils.copyProperties(srmPackingOrderSummaryDetImport, dto);
            getMaterial(dto,user);

            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierName(srmPackingOrderSummaryDetImport.getSupplierName());
            ResponseEntity<List<BaseSupplier>> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier);
            if(StringUtils.isNotEmpty(baseSuppliers.getData())) {
                dto.setSupplierId(baseSuppliers.getData().get(0).getSupplierId());
            }else {
                fail.add(i+2);
                continue;
            }

            dto.setPackingOrderSummaryId(packingOrderSummaryId);

            dto.setCreateTime(new Date());
            dto.setCreateUserId(user.getUserId());
            dto.setModifiedTime(new Date());
            dto.setModifiedUserId(user.getUserId());
            dto.setStatus((byte)1);
            dto.setOrgId(user.getOrganizationId());
            list.add(dto);
        }

        if (StringUtils.isNotEmpty(list)){
            success = srmPackingOrderSummaryDetMapper.insertList(list);
        }

        for (SrmPackingOrderSummaryDet srmPackingOrderSummaryDet : list) {
            SrmHtPackingOrderSummaryDet srmHtPackingOrderSummaryDet = new SrmHtPackingOrderSummaryDet();
            BeanUtils.copyProperties(srmPackingOrderSummaryDet, srmHtPackingOrderSummaryDet);
            htList.add(srmHtPackingOrderSummaryDet);
        }
        if (StringUtils.isNotEmpty(htList)){
            srmHtPackingOrderSummaryDetMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    public SrmPackingOrderSummary getSrmPackingOrderSummary(Long userId, String code){
        Example example = new Example(SrmPackingOrderSummary.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", userId);
        criteria.andEqualTo("cartonCode", code);
        SrmPackingOrderSummary srmPackingOrderSummary = srmPackingOrderSummaryMapper.selectOneByExample(example);
        return srmPackingOrderSummary;
    }
}

package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummaryDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngHtPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryMapper;
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
        engPackingOrderSummaryDetDto.setCreateTime(new Date());
        engPackingOrderSummaryDetDto.setCreateUserId(user.getUserId());
        engPackingOrderSummaryDetDto.setModifiedTime(new Date());
        engPackingOrderSummaryDetDto.setModifiedUserId(user.getUserId());
        engPackingOrderSummaryDetDto.setStatus((byte)1);
        engPackingOrderSummaryDetDto.setOrgId(user.getOrganizationId());

        getMaterial(engPackingOrderSummaryDetDto,user);

        EngPackingOrderSummary engPackingOrderSummary = getEngPackingOrderSummary(user.getOrganizationId(), engPackingOrderSummaryDetDto.getCartonCode());
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
        int fail =0;
        for(EngPackingOrderSummaryDetDto det : engPackingOrderSummaryDetDtos) {

            getMaterial(det,user);

            if(StringUtils.isNotEmpty(det.getPackingOrderSummaryDetId())){
                engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(det);
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
            engPackingOrderSummaryDetMapper.insertList(ins);
        }
        //新增历史信息
        if(StringUtils.isNotEmpty(engHtPackingOrderSummaryDets))
            engHtPackingOrderSummaryDetMapper.insertList(engHtPackingOrderSummaryDets);

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
    public void getMaterial(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto,SysUser user) {
        ResponseEntity<BaseMaterial> baseMaterialResponseEntity = null;
        if (StringUtils.isEmpty(engPackingOrderSummaryDetDto.getMaterialCode())) {
            BaseMaterial baseMaterial = new BaseMaterial();
            baseMaterial.setMaterialName(engPackingOrderSummaryDetDto.getMaterialName());
            Long startTs = System.currentTimeMillis();
            baseMaterial.setMaterialCode(startTs.toString());
            baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
          //  return baseMaterialResponseEntity.getData();
            engPackingOrderSummaryDetDto.setMaterialId(baseMaterialResponseEntity.getData().getMaterialId());
        } else {
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(engPackingOrderSummaryDetDto.getMaterialCode());
            searchBaseMaterial.setOrganizationId(user.getOrganizationId());
            ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(list.getData())) {
                engPackingOrderSummaryDetDto.setMaterialId(list.getData().get(0).getMaterialId());
            //    return list.getData().get(0);
            } else {
                BaseMaterial baseMaterial = new BaseMaterial();
                baseMaterial.setMaterialName(engPackingOrderSummaryDetDto.getMaterialName());
                baseMaterial.setMaterialCode(engPackingOrderSummaryDetDto.getMaterialCode());
                baseMaterialResponseEntity = baseFeignApi.saveByApi(baseMaterial);
            //    return baseMaterialResponseEntity.getData();
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

        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(packingOrderSummaryId);

        for (int i = 0; i < engPackingOrderSummaryDetImports.size(); i++) {
            EngPackingOrderSummaryDetImport engPackingOrderSummaryDetImport = engPackingOrderSummaryDetImports.get(i);

            String cartonCode = engPackingOrderSummaryDetImport.getCartonCode();
            String purchaseReqOrderCode = engPackingOrderSummaryDetImport.getPurchaseReqOrderCode();
            String contractCode = engPackingOrderSummaryDetImport.getContractCode();
            String deviceCode = engPackingOrderSummaryDetImport.getDeviceCode();
            String materialName = engPackingOrderSummaryDetImport.getMaterialName();
            String unitName = engPackingOrderSummaryDetImport.getUnitName();
            String dominantTermCode = engPackingOrderSummaryDetImport.getDominantTermCode();
            if (StringUtils.isEmpty(
                    cartonCode,deviceCode,materialName,purchaseReqOrderCode,contractCode,unitName,dominantTermCode
            )){
                fail.add(i+2);
                continue;
            }

            //判断各参数是否大于0
            BigDecimal qty = engPackingOrderSummaryDetImport.getQty();
            if(qty.compareTo(BigDecimal.ZERO)<0 ){
                fail.add(i+2);
                continue;
            }

            //装箱汇总明细的包装箱号需与装箱汇总的包装箱号一致
            if (!cartonCode.equals(engPackingOrderSummary.getCartonCode()) || !contractCode.equals(engPackingOrderSummary.getContractCode())
                 || !purchaseReqOrderCode.equals(engPackingOrderSummary.getPurchaseReqOrderCode())
            ){
                fail.add(i+2);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
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
            }

            EngPackingOrderSummaryDetDto dto = new EngPackingOrderSummaryDetDto();
            BeanUtils.copyProperties(engPackingOrderSummaryDetImport, dto);
            getMaterial(dto,user);

            /*SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierName(engPackingOrderSummaryDetImport.getSupplierName());
            ResponseEntity<List<BaseSupplier>> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier);
            if(StringUtils.isNotEmpty(baseSuppliers.getData())) {
                dto.setSupplierId(baseSuppliers.getData().get(0).getSupplierId());
            }else {
                fail.add(i+2);
                continue;
            }*/

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

    public EngPackingOrderSummary getEngPackingOrderSummary(Long userId, String code){
        Example example = new Example(EngPackingOrderSummary.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", userId);
        criteria.andEqualTo("cartonCode", code);
        EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectOneByExample(example);
        return engPackingOrderSummary;
    }
}
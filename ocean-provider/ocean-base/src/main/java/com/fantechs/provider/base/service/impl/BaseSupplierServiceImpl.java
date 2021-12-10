package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseSignature;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierAddress;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionExemptedList;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseSupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2020/09/27.
 */
@Service
public class BaseSupplierServiceImpl  extends BaseService<BaseSupplier> implements BaseSupplierService {

    @Resource
    private BaseSupplierMapper baseSupplierMapper;
    @Resource
    private BaseSignatureMapper baseSignatureMapper;
    @Resource
    private BaseSupplierAddressMapper baseSupplierAddressMapper;
    @Resource
    private BaseHtSupplierMapper baseHtSupplierMapper;
    @Resource
    private BaseSupplierReUserMapper baseSupplierReUserMapper;

    @Override
    public List<BaseSupplier> findInspectionSupplierList(SearchBaseInspectionExemptedList searchBaseInspectionExemptedList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        searchBaseInspectionExemptedList.setOrgId(user.getOrganizationId());
        List<BaseSupplier> baseSuppliers = baseSupplierMapper.findInspectionSupplierList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionExemptedList));
        return baseSuppliers;
    }

    @Override
    public List<BaseSupplier> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("organizationId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());

            /*Map<String, Object> mapReUser=new HashMap<>();
            mapReUser.put("userId",user.getUserId());
            mapReUser.put("orgId",user.getOrganizationId());
            List<BaseSupplierReUser> list = baseSupplierReUserMapper.findList(mapReUser);
            if(StringUtils.isNotEmpty(list))
                map.put("supplierId",list.get(0).getSupplierId());*/
            map.put("userId",user.getUserId());
        }

        return baseSupplierMapper.findList(map);
    }

    @Override
    public List<BaseHtSupplier> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtSupplierMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseSupplier record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseSupplier.class);
        example.createCriteria().andEqualTo("supplierCode",record.getSupplierCode())
                                .andEqualTo("supplierType",record.getSupplierType())
                                .andEqualTo("organizationId", currentUser.getOrganizationId());
        List<BaseSupplier> list = baseSupplierMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(list)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUser.getUserId());
        record.setModifiedUserId(currentUser.getUserId());
        record.setModifiedTime(new Date());
        record.setIsDelete((byte) 1);
        record.setOrganizationId(currentUser.getOrganizationId());
        int i = baseSupplierMapper.insertUseGeneratedKeys(record);

        //新增历史信息
        BaseHtSupplier baseHtSupplier = new BaseHtSupplier();
        BeanUtils.copyProperties(record, baseHtSupplier);
        baseHtSupplierMapper.insertSelective(baseHtSupplier);

        List<BaseAddressDto> address = record.getList();

        bindAddress(record.getSupplierId(),address);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseSupplier entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseSupplier.class);
        example.createCriteria().andEqualTo("supplierCode",entity.getSupplierCode())
                                .andEqualTo("supplierType",entity.getSupplierType())
                                .andEqualTo("organizationId", currentUser.getOrganizationId());
        BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(baseSupplier)&&!baseSupplier.getSupplierId().equals(entity.getSupplierId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        entity.setOrganizationId(currentUser.getOrganizationId());
        int i = baseSupplierMapper.updateByPrimaryKeySelective(entity);

        //新增历史信息
        BaseHtSupplier baseHtSupplier = new BaseHtSupplier();
        BeanUtils.copyProperties(entity, baseHtSupplier);
        baseHtSupplierMapper.insertSelective(baseHtSupplier);

        Example supplierAddressExample = new Example(BaseSupplierAddress.class);
        supplierAddressExample.createCriteria().andEqualTo("supplierId",entity.getSupplierId());
        baseSupplierAddressMapper.deleteByExample(supplierAddressExample);

        //获取当前供应商的地址集合对象
        List<BaseAddressDto> address = entity.getList();

        bindAddress(entity.getSupplierId(),address);

        return i;
    }

    //供应商绑定地址
    public void bindAddress(Long supplier,List<BaseAddressDto> list){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isNotEmpty(list)){
            List<BaseSupplierAddress> supplierAddresses = new ArrayList<>();
            //将新增的地址与供应商进行绑定
            for (BaseAddressDto baseAddressDto : list) {
                BaseSupplierAddress baseSupplierAddress = new BaseSupplierAddress();
                baseSupplierAddress.setIfDefault(baseAddressDto.getIfDefault());
                baseSupplierAddress.setSupplierId(supplier);
                baseSupplierAddress.setAddressId(baseAddressDto.getAddressId());
                baseSupplierAddress.setStatus((byte)1);
                baseSupplierAddress.setOrganizationId(currentUser.getOrganizationId());
                supplierAddresses.add(baseSupplierAddress);
            }
            if(StringUtils.isNotEmpty(supplierAddresses)){
                baseSupplierAddressMapper.insertList(supplierAddresses);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int i = 0;
        String[] idsArr = ids.split(",");
        for (String item:idsArr) {
            BaseSupplier baseSupplier = baseSupplierMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(baseSupplier)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被物料特征码引用
            Example example = new Example(BaseSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("supplierId",baseSupplier.getSupplierId());
            List<BaseSignature> baseSignatures = baseSignatureMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseSignatures)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //删除供应商同时删除供应商与用户绑定关系
            Example example1 = new Example(BaseSupplierReUser.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("supplierId",baseSupplier.getSupplierId());
            baseSupplierReUserMapper.deleteByExample(example1);
        }
        i = baseSupplierMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSupplierImport> baseSupplierImports, Byte supplierType) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseSupplier> list = new LinkedList<>();
        LinkedList<BaseSupplierImport> supplierImports = new LinkedList<>();

        for (int i = 0; i < baseSupplierImports.size(); i++) {
            BaseSupplierImport baseSupplierImport = baseSupplierImports.get(i);
            String supplierCode = baseSupplierImport.getSupplierCode();
            String supplierName = baseSupplierImport.getSupplierName();

            if (StringUtils.isEmpty(
                    supplierCode,supplierName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("supplierCode",supplierCode);
            criteria.andEqualTo("supplierType",supplierType);
            if (StringUtils.isNotEmpty(baseSupplierMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(supplierImports)){
                for (BaseSupplierImport supplierImport : supplierImports) {
                    if (supplierImport.getSupplierCode().equals(supplierCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }
            supplierImports.add(baseSupplierImport);
        }

        if (StringUtils.isNotEmpty(supplierImports)){

            for (BaseSupplierImport baseSupplierImport : supplierImports) {
                BaseSupplier baseSupplier = new BaseSupplier();
                BeanUtils.copyProperties(baseSupplierImport,baseSupplier);
                baseSupplier.setCreateTime(new Date());
                baseSupplier.setCreateUserId(currentUser.getUserId());
                baseSupplier.setModifiedTime(new Date());
                baseSupplier.setModifiedUserId(currentUser.getUserId());
                baseSupplier.setSupplierType(supplierType);
                baseSupplier.setOrganizationId(currentUser.getOrganizationId());
                baseSupplier.setStatus(StringUtils.isEmpty(baseSupplierImport.getStatus())?null:baseSupplierImport.getStatus().byteValue());
                list.add(baseSupplier);
            }
            success = baseSupplierMapper.insertList(list);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public List<BaseSupplier> findAll(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSupplierMapper.findList(map);
    }

    @Override
    public int saveByApi(BaseSupplier baseSupplier) {
        Example example = new Example(BaseSupplier.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("supplierCode",baseSupplier.getSupplierCode());
        criteria.andEqualTo("supplierType",baseSupplier.getSupplierType());
        criteria.andEqualTo("organizationId",baseSupplier.getOrganizationId());
        BaseSupplier supplier = baseSupplierMapper.selectOneByExample(example);
        int i= 0;
        if(StringUtils.isEmpty(supplier)) {
            baseSupplier.setCreateTime(new Date());
            baseSupplier.setCreateUserId((long) 1);
            baseSupplier.setModifiedUserId((long) 1);
            baseSupplier.setModifiedTime(new Date());
            if(StringUtils.isEmpty(baseSupplier.getSupplierType()))
                baseSupplier.setSupplierType((byte)1);
            baseSupplier.setIsDelete((byte) 1);
            i = baseSupplierMapper.insertSelective(baseSupplier);
        }else{
            baseSupplier.setSupplierId(supplier.getSupplierId());
            baseSupplier.setModifiedTime(new Date());
            baseSupplierMapper.updateByPrimaryKeySelective(baseSupplier);
        }
        return i;
    }
}

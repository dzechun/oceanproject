package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSupplierImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
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
    private BaseInspectionExemptedListMapper baseInspectionExemptedListMapper;
    @Resource
    private BaseMaterialSupplierMapper baseMaterialSupplierMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseOrganizationMapper baseOrganizationMapper;

    @Override
    public List<BaseSupplier> findInspectionSupplierList(SearchBaseInspectionExemptedList searchBaseInspectionExemptedList) {
        if(StringUtils.isEmpty(searchBaseInspectionExemptedList.getMaterialCode())){
            throw new BizErrorException("产品料号不能为空");
        }

        Example example = new Example(BaseMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode",searchBaseInspectionExemptedList.getMaterialCode());
        List<BaseMaterial> baseMaterials = baseMaterialMapper.selectByExample(example);

        List<BaseInspectionExemptedList> baseInspectionExemptedLists = baseInspectionExemptedListMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionExemptedList));
        ArrayList<Long> idList = new ArrayList();
        if(StringUtils.isNotEmpty(baseInspectionExemptedLists)){
            for (BaseInspectionExemptedList baseInspectionExemptedList : baseInspectionExemptedLists){
                if(baseInspectionExemptedList.getObjType()==1){
                    idList.add(baseInspectionExemptedList.getSupplierId());
                }else if(baseInspectionExemptedList.getObjType()==2){
                    idList.add(baseInspectionExemptedList.getCustomerId());
                }
            }
        }

        Example example1 = new Example(BaseMaterialSupplier.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("materialId",baseMaterials.get(0).getMaterialId());
        if(idList.size()>0){
            criteria1.andNotIn("supplierId",idList);
        }
        List<BaseMaterialSupplier> baseMaterialSuppliers = baseMaterialSupplierMapper.selectByExample(example1);

        if(StringUtils.isEmpty(baseMaterialSuppliers)){
            throw new BizErrorException("没有符合条件的客户");
        }

        ArrayList<Long> ids = new ArrayList();
        for (BaseMaterialSupplier baseMaterialSupplier: baseMaterialSuppliers){
            ids.add(baseMaterialSupplier.getSupplierId());
        }
        Example example2 = new Example(BaseSupplier.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andIn("supplierId",ids);
        List<BaseSupplier> baseSuppliers = baseSupplierMapper.selectByExample(example2);
        return baseSuppliers;
    }

    @Override
    public List<BaseSupplier> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("organizationId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        return baseSupplierMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseSupplier record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseSupplier.class);
        example.createCriteria().andEqualTo("supplierCode",record.getSupplierCode())
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

        List<BaseAddressDto> address = record.getList();

        bindAddress(record.getSupplierId(),address);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseSupplier entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseSupplier.class);
        example.createCriteria().andEqualTo("supplierCode",entity.getSupplierCode())
                                .andEqualTo("organizationId", currentUser.getOrganizationId());
        BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(baseSupplier)&&!baseSupplier.getSupplierId().equals(entity.getSupplierId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        entity.setOrganizationId(currentUser.getOrganizationId());
        int i = baseSupplierMapper.updateByPrimaryKeySelective(entity);

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
        if (StringUtils.isNotEmpty(list)){
            List<BaseSupplierAddress> supplierAddresses = new ArrayList<>();
            //将新增的地址与供应商进行绑定
            for (BaseAddressDto baseAddressDto : list) {
                BaseSupplierAddress baseSupplierAddress = new BaseSupplierAddress();
                baseSupplierAddress.setIfDefault(baseAddressDto.getIfDefault());
                baseSupplierAddress.setSupplierId(supplier);
                baseSupplierAddress.setAddressId(baseAddressDto.getAddressId());
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
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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
        }
        i = baseSupplierMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSupplierImport> baseSupplierImports, Byte supplierType) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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
                list.add(baseSupplier);
            }
            success = baseSupplierMapper.insertList(list);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int saveByApi(BaseSupplier baseSupplier) {
        Example example = new Example(BaseSupplier.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("supplierCode",baseSupplier.getSupplierCode());
        criteria.andEqualTo("organizationId",baseSupplier.getOrganizationId());
        baseSupplierMapper.deleteByExample(example);

        baseSupplier.setCreateTime(new Date());
        baseSupplier.setCreateUserId((long)1);
        baseSupplier.setModifiedUserId((long)1);
        baseSupplier.setModifiedTime(new Date());
        baseSupplier.setIsDelete((byte) 1);
        return baseSupplierMapper.insertSelective(baseSupplier);
    }
}

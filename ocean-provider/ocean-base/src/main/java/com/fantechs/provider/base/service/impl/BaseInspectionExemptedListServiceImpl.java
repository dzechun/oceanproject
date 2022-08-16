package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionExemptedListImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionExemptedListMapper;
import com.fantechs.provider.base.mapper.BaseInspectionExemptedListMapper;
import com.fantechs.provider.base.mapper.BaseMaterialMapper;
import com.fantechs.provider.base.mapper.BaseSupplierMapper;
import com.fantechs.provider.base.service.BaseInspectionExemptedListService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/05/21.
 */
@Service
public class BaseInspectionExemptedListServiceImpl extends BaseService<BaseInspectionExemptedList> implements BaseInspectionExemptedListService {

    @Resource
    private BaseInspectionExemptedListMapper baseInspectionExemptedListMapper;
    @Resource
    private BaseHtInspectionExemptedListMapper baseHtInspectionExemptedListMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseSupplierMapper baseSupplierMapper;

    @Override
    public List<BaseInspectionExemptedList> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseInspectionExemptedListMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionExemptedList baseInspectionExemptedList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        baseInspectionExemptedList.setCreateUserId(user.getUserId());
        baseInspectionExemptedList.setCreateTime(new Date());
        baseInspectionExemptedList.setModifiedUserId(user.getUserId());
        baseInspectionExemptedList.setModifiedTime(new Date());
        baseInspectionExemptedList.setStatus(StringUtils.isEmpty(baseInspectionExemptedList.getStatus())?1: baseInspectionExemptedList.getStatus());
        baseInspectionExemptedList.setOrgId(user.getOrganizationId());
        int i = baseInspectionExemptedListMapper.insertUseGeneratedKeys(baseInspectionExemptedList);

        BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
        BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
        baseHtInspectionExemptedListMapper.insertSelective(baseHtInspectionExemptedList);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionExemptedList baseInspectionExemptedList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        baseInspectionExemptedList.setModifiedTime(new Date());
        baseInspectionExemptedList.setModifiedUserId(user.getUserId());
        baseInspectionExemptedList.setOrgId(user.getOrganizationId());

        BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
        BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
        baseHtInspectionExemptedListMapper.insertSelective(baseHtInspectionExemptedList);

        return baseInspectionExemptedListMapper.updateByPrimaryKey(baseInspectionExemptedList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtInspectionExemptedList> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionExemptedList baseInspectionExemptedList = baseInspectionExemptedListMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionExemptedList)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
            BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
            list.add(baseHtInspectionExemptedList);
        }

        baseHtInspectionExemptedListMapper.insertList(list);

        return baseInspectionExemptedListMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseInspectionExemptedListImport> baseInspectionExemptedListImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseInspectionExemptedList> list = new LinkedList<>();
        LinkedList<BaseHtInspectionExemptedList> htList = new LinkedList<>();
        LinkedList<BaseInspectionExemptedListImport> inspectionExemptedListImports = new LinkedList<>();

        for (int i = 0; i < baseInspectionExemptedListImports.size(); i++) {
            BaseInspectionExemptedListImport baseInspectionExemptedListImport = baseInspectionExemptedListImports.get(i);
            Integer objType = baseInspectionExemptedListImport.getObjType();
            String materialCode = baseInspectionExemptedListImport.getMaterialCode();
            String customerCode = baseInspectionExemptedListImport.getCustomerCode();
            String supplierCode = baseInspectionExemptedListImport.getSupplierCode();

            if (StringUtils.isEmpty(
                    objType,materialCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId())
                     .andEqualTo("materialCode", materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseMaterial)) {
                fail.add(i + 4);
                continue;
            }
            baseInspectionExemptedListImport.setMaterialId(baseMaterial.getMaterialId());


            //判断客户信息是否存在
            Example example2 = new Example(BaseSupplier.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId", currentUser.getOrganizationId())
                    .andEqualTo("supplierCode", objType == 1 ? supplierCode : customerCode)
                    .andEqualTo("supplierType", objType);
            BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseSupplier)) {
                fail.add(i + 4);
                continue;
            }
            if(objType == 1){
                baseInspectionExemptedListImport.setSupplierId(baseSupplier.getSupplierId());
            }else if(objType == 2){
                baseInspectionExemptedListImport.setCustomerId(baseSupplier.getSupplierId());
            }

            inspectionExemptedListImports.add(baseInspectionExemptedListImport);
        }

        if (StringUtils.isNotEmpty(inspectionExemptedListImports)){

            for (BaseInspectionExemptedListImport baseInspectionExemptedListImport : inspectionExemptedListImports) {
                BaseInspectionExemptedList baseInspectionExemptedList = new BaseInspectionExemptedList();
                BeanUtils.copyProperties(baseInspectionExemptedListImport,baseInspectionExemptedList);
                baseInspectionExemptedList.setObjType(baseInspectionExemptedListImport.getObjType().byteValue());
                baseInspectionExemptedList.setCreateTime(new Date());
                baseInspectionExemptedList.setCreateUserId(currentUser.getUserId());
                baseInspectionExemptedList.setModifiedTime(new Date());
                baseInspectionExemptedList.setModifiedUserId(currentUser.getUserId());
                baseInspectionExemptedList.setOrgId(currentUser.getOrganizationId());
                baseInspectionExemptedList.setStatus((byte)1);
                list.add(baseInspectionExemptedList);
            }
            success = baseInspectionExemptedListMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseInspectionExemptedList baseInspectionExemptedList : list) {
                    BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
                    BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
                    htList.add(baseHtInspectionExemptedList);
                }
                baseHtInspectionExemptedListMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

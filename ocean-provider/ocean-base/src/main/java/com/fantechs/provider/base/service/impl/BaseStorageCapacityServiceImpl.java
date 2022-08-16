package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatformImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageCapacityImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BasePlatform;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlatform;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageCapacity;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtStorageCapacityMapper;
import com.fantechs.provider.base.mapper.BaseMaterialMapper;
import com.fantechs.provider.base.mapper.BaseStorageCapacityMapper;
import com.fantechs.provider.base.service.BaseStorageCapacityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/10/18.
 */
@Service
public class BaseStorageCapacityServiceImpl extends BaseService<BaseStorageCapacity> implements BaseStorageCapacityService {

    @Resource
    private BaseStorageCapacityMapper baseStorageCapacityMapper;
    @Resource
    private BaseHtStorageCapacityMapper baseHtStorageCapacityMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;

    @Override
    public List<BaseStorageCapacity> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseStorageCapacityMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStorageCapacity record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStorageCapacity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCodePrefix",record.getMaterialCodePrefix())
                .andEqualTo("orgId",user.getOrganizationId());
        BaseStorageCapacity baseStorageCapacity = baseStorageCapacityMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(baseStorageCapacity)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已存在该物料编码前缀的库容信息");
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        baseStorageCapacityMapper.insertUseGeneratedKeys(record);

        BaseHtStorageCapacity baseHtStorageCapacity = new BaseHtStorageCapacity();
        BeanUtils.copyProperties(record, baseHtStorageCapacity);
        int i = baseHtStorageCapacityMapper.insertSelective(baseHtStorageCapacity);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStorageCapacity entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStorageCapacity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCodePrefix",entity.getMaterialCodePrefix())
                .andEqualTo("orgId",user.getOrganizationId())
                .andNotEqualTo("storageCapacityId",entity.getStorageCapacityId());
        BaseStorageCapacity baseStorageCapacity = baseStorageCapacityMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(baseStorageCapacity)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已存在该物料编码前缀的库容信息");
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        BaseHtStorageCapacity baseHtStorageCapacity = new BaseHtStorageCapacity();
        BeanUtils.copyProperties(entity, baseHtStorageCapacity);
        baseHtStorageCapacityMapper.insertSelective(baseHtStorageCapacity);

        return baseStorageCapacityMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<BaseHtStorageCapacity> htList = new ArrayList<>();
        List<BaseStorageCapacity> baseStorageCapacities = baseStorageCapacityMapper.selectByIds(ids);
        for (BaseStorageCapacity baseStorageCapacity : baseStorageCapacities){
            BaseHtStorageCapacity baseHtStorageCapacity = new BaseHtStorageCapacity();
            BeanUtils.copyProperties(baseStorageCapacity, baseHtStorageCapacity);
            htList.add(baseHtStorageCapacity);
        }
        baseHtStorageCapacityMapper.insertList(htList);

        return baseStorageCapacityMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStorageCapacityImport> baseStorageCapacityImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseStorageCapacity> list = new LinkedList<>();
        LinkedList<BaseHtStorageCapacity> htList = new LinkedList<>();
        for (int i = 0; i < baseStorageCapacityImports.size(); i++) {
            BaseStorageCapacityImport baseStorageCapacityImport = baseStorageCapacityImports.get(i);

            String materialCodePrefix = baseStorageCapacityImport.getMaterialCodePrefix();
            if (StringUtils.isEmpty(
                    materialCodePrefix
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料是否存在
            /*Example example = new Example(BaseMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId())
                    .andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            baseStorageCapacityImport.setMaterialId(baseMaterial.getMaterialId());*/

            //判断是否重复
            Example example1 = new Example(BaseStorageCapacity.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId())
                    .andEqualTo("materialCodePrefix",baseStorageCapacityImport.getMaterialCodePrefix());
            if (StringUtils.isNotEmpty(baseStorageCapacityMapper.selectOneByExample(example1))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BaseStorageCapacity baseStorageCapacity : list) {
                    if (baseStorageCapacity.getMaterialCodePrefix().equals(materialCodePrefix)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            BaseStorageCapacity baseStorageCapacity = new BaseStorageCapacity();
            BeanUtils.copyProperties(baseStorageCapacityImport, baseStorageCapacity);
            baseStorageCapacity.setCreateTime(new Date());
            baseStorageCapacity.setCreateUserId(currentUser.getUserId());
            baseStorageCapacity.setModifiedTime(new Date());
            baseStorageCapacity.setModifiedUserId(currentUser.getUserId());
            baseStorageCapacity.setStatus((byte)1);
            baseStorageCapacity.setOrgId(currentUser.getOrganizationId());
            list.add(baseStorageCapacity);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseStorageCapacityMapper.insertList(list);
        }

        for (BaseStorageCapacity baseStorageCapacity : list) {
            BaseHtStorageCapacity baseHtStorageCapacity = new BaseHtStorageCapacity();
            BeanUtils.copyProperties(baseStorageCapacity, baseHtStorageCapacity);
            htList.add(baseHtStorageCapacity);
        }
        if (StringUtils.isNotEmpty(htList)){
            baseHtStorageCapacityMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    @Override
    public BigDecimal totalQty(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return baseStorageCapacityMapper.totalQty(map);
    }

    @Override
    public BigDecimal putJobQty(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return baseStorageCapacityMapper.putJobQty(map);
    }

    @Override
    public List<WmsInnerInventory> wmsList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return baseStorageCapacityMapper.wmsList(map);
    }
}

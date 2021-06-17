package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.BaseWarehousePersonnel;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehousePersonnel;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseWarehouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class BaseWarehouseServiceImpl extends BaseService<BaseWarehouse> implements BaseWarehouseService {

    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;

    @Resource
    private BaseHtWarehouseMapper baseHtWarehouseMapper;

    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;

    @Resource
    private BaseMaterialOwnerReWhMapper baseMaterialOwnerReWhMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWarehouse baseWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseCode", baseWarehouse.getWarehouseCode());
        List<BaseWarehouse> baseWarehouses = baseWarehouseMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseWarehouses)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseWarehouse.setCreateUserId(currentUser.getUserId());
        baseWarehouse.setCreateTime(new Date());
        baseWarehouse.setModifiedUserId(currentUser.getUserId());
        baseWarehouse.setModifiedTime(new Date());
        baseWarehouse.setOrganizationId(currentUser.getOrganizationId());
        baseWarehouseMapper.insertUseGeneratedKeys(baseWarehouse);

        //新增货主和仓库关系
        List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseWarehouse.getBaseMaterialOwnerReWhDtos();
        if(StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
            for (BaseMaterialOwnerReWhDto baseMaterialOwnerReWhDto : baseMaterialOwnerReWhDtos) {
                baseMaterialOwnerReWhDto.setWarehouseId(baseWarehouse.getWarehouseId());
                baseMaterialOwnerReWhDto.setOrgId(currentUser.getOrganizationId());
            }
            baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhDtos);
        }

        //新增仓库历史信息
        BaseHtWarehouse baseHtWarehouse = new BaseHtWarehouse();
        BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
        int i = baseHtWarehouseMapper.insertSelective(baseHtWarehouse);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<BaseHtWarehouse> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] warehouseIds = ids.split(",");
        for (String warehouseId : warehouseIds) {
            BaseWarehouse baseWarehouse = baseWarehouseMapper.selectByPrimaryKey(Long.parseLong(warehouseId));
            if(StringUtils.isEmpty(baseWarehouse)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被库区引用
            Example example = new Example(BaseWarehouseArea.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",warehouseId);
            List<BaseWarehouseArea> baseWarehouseAreas = baseWarehouseAreaMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseWarehouseAreas)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增仓库历史信息
            BaseHtWarehouse baseHtWarehouse =new BaseHtWarehouse();
            BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
            baseHtWarehouse.setModifiedUserId(currentUser.getUserId());
            baseHtWarehouse.setModifiedTime(new Date());
            list.add(baseHtWarehouse);

            //删除原有绑定关系
            Example example1 = new Example(BaseMaterialOwnerReWh.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("warehouseId", baseWarehouse.getWarehouseId());
            baseMaterialOwnerReWhMapper.deleteByExample(example1);
        }
        baseHtWarehouseMapper.insertList(list);

        return baseWarehouseMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWarehouse baseWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseCode", baseWarehouse.getWarehouseCode())
                .andNotEqualTo("warehouseId", baseWarehouse.getWarehouseId());
        BaseWarehouse warehouse = baseWarehouseMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(warehouse)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseWarehouse.setModifiedUserId(currentUser.getUserId());
        baseWarehouse.setModifiedTime(new Date());
        baseWarehouse.setOrganizationId(currentUser.getOrganizationId());
        int i= baseWarehouseMapper.updateByPrimaryKeySelective(baseWarehouse);

        //删除原有绑定关系
        Example example1 = new Example(BaseMaterialOwnerReWh.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("warehouseId", baseWarehouse.getWarehouseId());
        baseMaterialOwnerReWhMapper.deleteByExample(example1);

        //新增货主和仓库关系
        List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseWarehouse.getBaseMaterialOwnerReWhDtos();
        if(StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
            for (BaseMaterialOwnerReWhDto baseMaterialOwnerReWhDto : baseMaterialOwnerReWhDtos) {
                baseMaterialOwnerReWhDto.setWarehouseId(baseWarehouse.getWarehouseId());
                baseMaterialOwnerReWhDto.setOrgId(currentUser.getOrganizationId());
            }
            baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhDtos);
        }

        //新增仓库历史信息
        BaseHtWarehouse baseHtWarehouse =new BaseHtWarehouse();
        BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
        baseHtWarehouseMapper.insertSelective(baseHtWarehouse);
        return i;
    }


    @Override
    public List<BaseWarehouse> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        List<BaseWarehouse> baseWarehouses = baseWarehouseMapper.findList(map);
        if (StringUtils.isNotEmpty(baseWarehouses)){
            SearchBaseMaterialOwnerReWh searchBaseMaterialOwnerReWh = new SearchBaseMaterialOwnerReWh();

            for (BaseWarehouse baseWarehouse : baseWarehouses) {
                searchBaseMaterialOwnerReWh.setWarehouseId(baseWarehouse.getWarehouseId());
                List<BaseMaterialOwnerReWhDto> list = baseMaterialOwnerReWhMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialOwnerReWh));
                if (StringUtils.isNotEmpty(list)){
                    baseWarehouse.setBaseMaterialOwnerReWhDtos(list);
                }
            }
        }
        return baseWarehouses;
    }

    @Override
    public int batchUpdateByCode(List<BaseWarehouse> baseWarehouses) {
        if (StringUtils.isNotEmpty(baseWarehouses)){
            return baseWarehouseMapper.batchUpdateByCode(baseWarehouses);
        }else {
            return 0;
        }
    }

    @Override
    public int insertList(List<BaseWarehouse> baseWarehouses) {
        return baseWarehouseMapper.insertList(baseWarehouses);
    }

}

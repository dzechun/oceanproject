package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.history.BaseHtFactory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInventoryStatusMapper;
import com.fantechs.provider.base.mapper.BaseInventoryStatusMapper;
import com.fantechs.provider.base.service.BaseInventoryStatusService;
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
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseInventoryStatusServiceImpl extends BaseService<BaseInventoryStatus> implements BaseInventoryStatusService {

    @Resource
    private BaseInventoryStatusMapper baseInventoryStatusMapper;
    @Resource
    private BaseHtInventoryStatusMapper baseHtInventoryStatusMapper;

    @Override
    public List<BaseInventoryStatus> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseInventoryStatusMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseInventoryStatus baseInventoryStatus) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //相同的货主与仓库只能有一条默认的库存状态
        ifRepeat(baseInventoryStatus);

        baseInventoryStatus.setCreateUserId(user.getUserId());
        baseInventoryStatus.setCreateTime(new Date());
        baseInventoryStatus.setModifiedUserId(user.getUserId());
        baseInventoryStatus.setModifiedTime(new Date());
        baseInventoryStatus.setIfCanStoreIssue(StringUtils.isEmpty(baseInventoryStatus.getIfCanStoreIssue())?0: baseInventoryStatus.getIfCanStoreIssue());
        baseInventoryStatus.setIfDefaultStatus(StringUtils.isEmpty(baseInventoryStatus.getIfDefaultStatus())?0: baseInventoryStatus.getIfDefaultStatus());
        baseInventoryStatus.setStatus(StringUtils.isEmpty(baseInventoryStatus.getStatus())?1: baseInventoryStatus.getStatus());
        baseInventoryStatus.setOrgId(user.getOrganizationId());
        int i = baseInventoryStatusMapper.insertUseGeneratedKeys(baseInventoryStatus);

        BaseHtInventoryStatus baseHtInventoryStatus = new BaseHtInventoryStatus();
        BeanUtils.copyProperties(baseInventoryStatus, baseHtInventoryStatus);
        baseHtInventoryStatusMapper.insertSelective(baseHtInventoryStatus);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseInventoryStatus baseInventoryStatus) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //相同的货主与仓库只能有一条默认的库存状态
        ifRepeat(baseInventoryStatus);

        baseInventoryStatus.setModifiedTime(new Date());
        baseInventoryStatus.setModifiedUserId(user.getUserId());
        baseInventoryStatus.setOrgId(user.getOrganizationId());

        BaseHtInventoryStatus baseHtInventoryStatus = new BaseHtInventoryStatus();
        BeanUtils.copyProperties(baseInventoryStatus, baseHtInventoryStatus);

        baseHtInventoryStatusMapper.insertSelective(baseHtInventoryStatus);

        return baseInventoryStatusMapper.updateByPrimaryKeySelective(baseInventoryStatus);
    }


    public void ifRepeat(BaseInventoryStatus baseInventoryStatus){
        if(StringUtils.isNotEmpty(baseInventoryStatus.getIfDefaultStatus())&&baseInventoryStatus.getIfDefaultStatus()==1) {
            Example example = new Example(BaseInventoryStatus.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialOwnerId", baseInventoryStatus.getMaterialOwnerId())
                    .andEqualTo("warehouseId", baseInventoryStatus.getWarehouseId())
                    .andEqualTo("ifDefaultStatus", 1);

            if (StringUtils.isNotEmpty(baseInventoryStatus.getInventoryStatusId())) {
                criteria.andNotEqualTo("inventoryStatusId", baseInventoryStatus.getInventoryStatusId());
            }
            BaseInventoryStatus inventoryStatus = baseInventoryStatusMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(inventoryStatus)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "相同的货主与仓库只能有一条默认的库存状态");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtInventoryStatus> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInventoryStatus baseInventoryStatus = baseInventoryStatusMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInventoryStatus)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInventoryStatus baseHtInventoryStatus = new BaseHtInventoryStatus();
            BeanUtils.copyProperties(baseInventoryStatus, baseHtInventoryStatus);
            list.add(baseHtInventoryStatus);
        }

        baseHtInventoryStatusMapper.insertList(list);

        return baseInventoryStatusMapper.deleteByIds(ids);
    }
}

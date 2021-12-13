package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionWayMapper;
import com.fantechs.provider.base.mapper.BaseInspectionWayMapper;
import com.fantechs.provider.base.service.BaseInspectionWayService;
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
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseInspectionWayServiceImpl extends BaseService<BaseInspectionWay> implements BaseInspectionWayService {

    @Resource
    private BaseInspectionWayMapper baseInspectionWayMapper;
    @Resource
    private BaseHtInspectionWayMapper baseHtInspectionWayMapper;

    @Override
    public List<BaseInspectionWay> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseInspectionWayMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseInspectionWay baseInspectionWay) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseInspectionWay.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("inspectionWayCode", baseInspectionWay.getInspectionWayCode());
        BaseInspectionWay baseInspectionWay1 = baseInspectionWayMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionWay1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(baseInspectionWay.getInspectionType() == 1) {
            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("orgId", user.getOrganizationId())
                    .andEqualTo("inspectionType", baseInspectionWay.getInspectionType());
            BaseInspectionWay baseInspectionWay2 = baseInspectionWayMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseInspectionWay2)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"只允许一条来料检验的检验方式");
            }
        }

        baseInspectionWay.setCreateUserId(user.getUserId());
        baseInspectionWay.setCreateTime(new Date());
        baseInspectionWay.setModifiedUserId(user.getUserId());
        baseInspectionWay.setModifiedTime(new Date());
        baseInspectionWay.setStatus(StringUtils.isEmpty(baseInspectionWay.getStatus())?1: baseInspectionWay.getStatus());
        baseInspectionWay.setOrgId(user.getOrganizationId());
        int i = baseInspectionWayMapper.insertUseGeneratedKeys(baseInspectionWay);

        BaseHtInspectionWay baseHtInspectionWay = new BaseHtInspectionWay();
        BeanUtils.copyProperties(baseInspectionWay, baseHtInspectionWay);
        baseHtInspectionWayMapper.insertSelective(baseHtInspectionWay);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseInspectionWay baseInspectionWay) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseInspectionWay.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("inspectionWayCode", baseInspectionWay.getInspectionWayCode())
                .andNotEqualTo("inspectionWayId",baseInspectionWay.getInspectionWayId());
        BaseInspectionWay baseInspectionWay1 = baseInspectionWayMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionWay1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(baseInspectionWay.getInspectionType() == 1) {
            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("orgId", user.getOrganizationId())
                    .andEqualTo("inspectionType", baseInspectionWay.getInspectionType())
                    .andNotEqualTo("inspectionWayId",baseInspectionWay.getInspectionWayId());
            BaseInspectionWay baseInspectionWay2 = baseInspectionWayMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseInspectionWay2)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"只允许一条来料检验的检验方式");
            }
        }

        baseInspectionWay.setModifiedTime(new Date());
        baseInspectionWay.setModifiedUserId(user.getUserId());
        baseInspectionWay.setOrgId(user.getOrganizationId());

        BaseHtInspectionWay baseHtInspectionWay = new BaseHtInspectionWay();
        BeanUtils.copyProperties(baseInspectionWay, baseHtInspectionWay);
        baseHtInspectionWayMapper.insertSelective(baseHtInspectionWay);

        return baseInspectionWayMapper.updateByPrimaryKeySelective(baseInspectionWay);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtInspectionWay> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionWay baseInspectionWay = baseInspectionWayMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionWay)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInspectionWay baseHtInspectionWay = new BaseHtInspectionWay();
            BeanUtils.copyProperties(baseInspectionWay, baseHtInspectionWay);
            list.add(baseHtInspectionWay);
        }

        baseHtInspectionWayMapper.insertList(list);

        return baseInspectionWayMapper.deleteByIds(ids);
    }
}



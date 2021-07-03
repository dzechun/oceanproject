package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandard;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionStandardMapper;
import com.fantechs.provider.base.mapper.BaseInspectionStandardDetMapper;
import com.fantechs.provider.base.mapper.BaseInspectionStandardMapper;
import com.fantechs.provider.base.service.BaseInspectionStandardService;
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
public class BaseInspectionStandardServiceImpl extends BaseService<BaseInspectionStandard> implements BaseInspectionStandardService {

    @Resource
    private BaseInspectionStandardMapper baseInspectionStandardMapper;
    @Resource
    private BaseInspectionStandardDetMapper baseInspectionStandardDetMapper;
    @Resource
    private BaseHtInspectionStandardMapper baseHtInspectionStandardMapper;

    @Override
    public List<BaseInspectionStandard> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        List<BaseInspectionStandard> baseInspectionStandards = baseInspectionStandardMapper.findList(map);
        SearchBaseInspectionStandardDet searchBaseInspectionStandardDet = new SearchBaseInspectionStandardDet();

        for (BaseInspectionStandard baseInspectionStandard : baseInspectionStandards) {
            searchBaseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
            List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandardDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionStandardDet));
            if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
                baseInspectionStandard.setBaseInspectionStandardDets(baseInspectionStandardDets);
            }
        }

        return baseInspectionStandards;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionStandard baseInspectionStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseInspectionStandard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("inspectionStandardCode", baseInspectionStandard.getInspectionStandardCode());
        BaseInspectionStandard baseInspectionStandard1 = baseInspectionStandardMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionStandard1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增检验标准
        baseInspectionStandard.setCreateUserId(user.getUserId());
        baseInspectionStandard.setCreateTime(new Date());
        baseInspectionStandard.setModifiedUserId(user.getUserId());
        baseInspectionStandard.setModifiedTime(new Date());
        baseInspectionStandard.setStatus(StringUtils.isEmpty(baseInspectionStandard.getStatus())?1:baseInspectionStandard.getStatus());
        baseInspectionStandard.setOrgId(user.getOrganizationId());
        int i = baseInspectionStandardMapper.insertUseGeneratedKeys(baseInspectionStandard);

        //新增检验标准明细
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets) {
                if(baseInspectionStandardDet.getInspectionTag()==(byte)2) {
                    if(StringUtils.isNotEmpty(baseInspectionStandardDet.getSpecificationUpperLimit(),baseInspectionStandardDet.getSpecificationFloor())) {
                        if (baseInspectionStandardDet.getSpecificationUpperLimit().compareTo(baseInspectionStandardDet.getSpecificationFloor()) == -1
                                || baseInspectionStandardDet.getSpecificationUpperLimit().compareTo(baseInspectionStandardDet.getSpecificationFloor()) == 0) {
                            throw new BizErrorException("规格上限必须大于规格下限");
                        }
                    }
                }
                baseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
                baseInspectionStandardDet.setCreateUserId(user.getUserId());
                baseInspectionStandardDet.setCreateTime(new Date());
                baseInspectionStandardDet.setModifiedUserId(user.getUserId());
                baseInspectionStandardDet.setModifiedTime(new Date());
                baseInspectionStandardDet.setStatus(StringUtils.isEmpty(baseInspectionStandardDet.getStatus())?1:baseInspectionStandardDet.getStatus());
                baseInspectionStandardDet.setOrgId(user.getOrganizationId());
            }
            baseInspectionStandardDetMapper.insertList(baseInspectionStandardDets);
        }

        //履历
        BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
        BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
        baseHtInspectionStandardMapper.insert(baseHtInspectionStandard);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionStandard baseInspectionStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseInspectionStandard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("inspectionStandardCode", baseInspectionStandard.getInspectionStandardCode())
                .andNotEqualTo("inspectionStandardId",baseInspectionStandard.getInspectionStandardId());
        BaseInspectionStandard baseInspectionStandard1 = baseInspectionStandardMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionStandard1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改检验标准
        baseInspectionStandard.setModifiedUserId(user.getUserId());
        baseInspectionStandard.setModifiedTime(new Date());
        baseInspectionStandard.setOrgId(user.getOrganizationId());
        int i=baseInspectionStandardMapper.updateByPrimaryKeySelective(baseInspectionStandard);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<BaseInspectionStandardDet> baseInspectionStandardDetList = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDetList)) {
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDetList) {
                if (StringUtils.isNotEmpty(baseInspectionStandardDet.getInspectionStandardDetId())) {
                    baseInspectionStandardDetMapper.updateByPrimaryKeySelective(baseInspectionStandardDet);
                    idList.add(baseInspectionStandardDet.getInspectionStandardDetId());
                }
            }
        }

        //删除原有检验标准明细
        Example example1 = new Example(BaseInspectionStandardDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionStandardId", baseInspectionStandard.getInspectionStandardId());
        if(idList.size()>0){
            criteria1.andNotIn("inspectionStandardDetId",idList);
        }
        baseInspectionStandardDetMapper.deleteByExample(example1);

        //新增检验标准明细
        List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandard.getBaseInspectionStandardDets();
        if(StringUtils.isNotEmpty(baseInspectionStandardDets)){
            for (BaseInspectionStandardDet baseInspectionStandardDet : baseInspectionStandardDets) {
                if(idList.contains(baseInspectionStandardDet.getInspectionStandardDetId())){
                    continue;
                }
                baseInspectionStandardDet.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());
                baseInspectionStandardDet.setCreateUserId(user.getUserId());
                baseInspectionStandardDet.setCreateTime(new Date());
                baseInspectionStandardDet.setModifiedUserId(user.getUserId());
                baseInspectionStandardDet.setModifiedTime(new Date());
                baseInspectionStandardDet.setStatus(StringUtils.isEmpty(baseInspectionStandardDet.getStatus())?1:baseInspectionStandardDet.getStatus());
                baseInspectionStandardDet.setOrgId(user.getOrganizationId());
                baseInspectionStandardDetMapper.insert(baseInspectionStandardDet);
            }
        }

        //履历
        BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
        BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
        baseHtInspectionStandardMapper.insert(baseHtInspectionStandard);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtInspectionStandard> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionStandard baseInspectionStandard = baseInspectionStandardMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionStandard)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInspectionStandard baseHtInspectionStandard = new BaseHtInspectionStandard();
            BeanUtils.copyProperties(baseInspectionStandard, baseHtInspectionStandard);
            list.add(baseHtInspectionStandard);

            //删除相关检验标准明细
            Example example1 = new Example(BaseInspectionStandardDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionStandardId", baseInspectionStandard.getInspectionStandardId());
            baseInspectionStandardDetMapper.deleteByExample(example1);
        }

        //履历
        baseHtInspectionStandardMapper.insertList(list);

        return baseInspectionStandardMapper.deleteByIds(ids);
    }
}

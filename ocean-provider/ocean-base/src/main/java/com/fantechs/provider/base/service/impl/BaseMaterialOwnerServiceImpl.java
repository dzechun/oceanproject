package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwnerReWh;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtMaterialOwnerMapper;
import com.fantechs.provider.base.mapper.BaseMaterialOwnerMapper;
import com.fantechs.provider.base.mapper.BaseMaterialOwnerReWhMapper;
import com.fantechs.provider.base.service.BaseMaterialOwnerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/22.
 */
@Service
public class BaseMaterialOwnerServiceImpl extends BaseService<BaseMaterialOwner> implements BaseMaterialOwnerService {

    @Resource
    private BaseMaterialOwnerMapper baseMaterialOwnerMapper;

    @Resource
    private BaseHtMaterialOwnerMapper baseHtMaterialOwnerMapper;

    @Resource
    private BaseMaterialOwnerReWhMapper baseMaterialOwnerReWhMapper;

    @Override
    public List<BaseMaterialOwnerDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        List<BaseMaterialOwnerDto> baseMaterialOwnerDtos = baseMaterialOwnerMapper.findList(map);
        SearchBaseMaterialOwnerReWh searchBaseMaterialOwnerReWh = new SearchBaseMaterialOwnerReWh();

        for (BaseMaterialOwnerDto baseMaterialOwnerDto : baseMaterialOwnerDtos) {
            searchBaseMaterialOwnerReWh.setMaterialOwnerId(baseMaterialOwnerDto.getMaterialOwnerId());
            List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseMaterialOwnerReWhMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialOwnerReWh));
            if (StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
                baseMaterialOwnerDto.setBaseMaterialOwnerReWhDtos(baseMaterialOwnerReWhDtos);
            }
        }
        return baseMaterialOwnerDtos;
    }

    @Override
    public List<BaseMaterialOwnerDto> findAll() {
        Map<String, Object> map = new HashMap<>();
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return baseMaterialOwnerMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseMaterialOwner baseMaterialOwner) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseMaterialOwner.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("materialOwnerCode", baseMaterialOwner.getMaterialOwnerCode());
        BaseMaterialOwner baseMaterialOwner1 = baseMaterialOwnerMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseMaterialOwner1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseMaterialOwner.setCreateUserId(user.getUserId());
        baseMaterialOwner.setCreateTime(new Date());
        baseMaterialOwner.setModifiedUserId(user.getUserId());
        baseMaterialOwner.setModifiedTime(new Date());
        baseMaterialOwner.setStatus(StringUtils.isEmpty(baseMaterialOwner.getStatus())?1:baseMaterialOwner.getStatus());
        baseMaterialOwner.setOrgId(user.getOrganizationId());
        int i = baseMaterialOwnerMapper.insertUseGeneratedKeys(baseMaterialOwner);

        //新增货主和仓库关系
        List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseMaterialOwner.getBaseMaterialOwnerReWhDtos();
        if(StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
            for (BaseMaterialOwnerReWhDto baseMaterialOwnerReWhDto : baseMaterialOwnerReWhDtos) {
                baseMaterialOwnerReWhDto.setMaterialOwnerId(baseMaterialOwner.getMaterialOwnerId());
                baseMaterialOwnerReWhDto.setCreateUserId(user.getUserId());
                baseMaterialOwnerReWhDto.setCreateTime(new Date());
                baseMaterialOwnerReWhDto.setModifiedUserId(user.getUserId());
                baseMaterialOwnerReWhDto.setModifiedTime(new Date());
                baseMaterialOwnerReWhDto.setStatus(StringUtils.isEmpty(baseMaterialOwnerReWhDto.getStatus())?1:baseMaterialOwnerReWhDto.getStatus());
                baseMaterialOwnerReWhDto.setOrgId(user.getOrganizationId());
            }
            baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhDtos);
        }

        BaseHtMaterialOwner baseHtMaterialOwner = new BaseHtMaterialOwner();
        BeanUtils.copyProperties(baseMaterialOwner, baseHtMaterialOwner);
        baseHtMaterialOwnerMapper.insertSelective(baseHtMaterialOwner);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseMaterialOwner baseMaterialOwner) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseMaterialOwner.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("materialOwnerCode", baseMaterialOwner.getMaterialOwnerCode())
                .andNotEqualTo("materialOwnerId",baseMaterialOwner.getMaterialOwnerId());
        BaseMaterialOwner baseMaterialOwner1 = baseMaterialOwnerMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseMaterialOwner1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseMaterialOwner.setModifiedUserId(user.getUserId());
        baseMaterialOwner.setModifiedTime(new Date());
        baseMaterialOwner.setOrgId(user.getOrganizationId());
        int i=baseMaterialOwnerMapper.updateByPrimaryKeySelective(baseMaterialOwner);

        //删除原有绑定关系
        Example example1 = new Example(BaseMaterialOwnerReWh.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("materialOwnerId", baseMaterialOwner.getMaterialOwnerId());
        baseMaterialOwnerReWhMapper.deleteByExample(example1);

        //新增货主和仓库关系
        List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseMaterialOwner.getBaseMaterialOwnerReWhDtos();
        if(StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
            for (BaseMaterialOwnerReWhDto baseMaterialOwnerReWhDto : baseMaterialOwnerReWhDtos) {
                baseMaterialOwnerReWhDto.setMaterialOwnerId(baseMaterialOwner.getMaterialOwnerId());
                baseMaterialOwnerReWhDto.setCreateUserId(user.getUserId());
                baseMaterialOwnerReWhDto.setCreateTime(new Date());
                baseMaterialOwnerReWhDto.setModifiedUserId(user.getUserId());
                baseMaterialOwnerReWhDto.setModifiedTime(new Date());
                baseMaterialOwnerReWhDto.setStatus(StringUtils.isEmpty(baseMaterialOwnerReWhDto.getStatus())?1:baseMaterialOwnerReWhDto.getStatus());
                baseMaterialOwnerReWhDto.setOrgId(user.getOrganizationId());
            }
            baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhDtos);
        }

        BaseHtMaterialOwner baseHtMaterialOwner = new BaseHtMaterialOwner();
        BeanUtils.copyProperties(baseMaterialOwner, baseHtMaterialOwner);
        baseHtMaterialOwnerMapper.insertSelective(baseHtMaterialOwner);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtMaterialOwner> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseMaterialOwner baseMaterialOwner = baseMaterialOwnerMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseMaterialOwner)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtMaterialOwner baseHtMaterialOwner = new BaseHtMaterialOwner();
            BeanUtils.copyProperties(baseMaterialOwner, baseHtMaterialOwner);
            list.add(baseHtMaterialOwner);

            //删除货主与仓库关系
            Example example1 = new Example(BaseMaterialOwnerReWh.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialOwnerId", id);
            baseMaterialOwnerReWhMapper.deleteByExample(example1);
        }

        baseHtMaterialOwnerMapper.insertList(list);

        return baseMaterialOwnerMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseMaterialOwnerDto> baseMaterialOwnerDtos) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseMaterialOwner> list = new LinkedList<>();
        LinkedList<BaseHtMaterialOwner> htList = new LinkedList<>();
        for (int i = 0; i < baseMaterialOwnerDtos.size(); i++) {
            BaseMaterialOwnerDto baseMaterialOwnerDto = baseMaterialOwnerDtos.get(i);
            String materialOwnerCode = baseMaterialOwnerDto.getMaterialOwnerCode();
            String materialOwnerName = baseMaterialOwnerDto.getMaterialOwnerName();
            if (StringUtils.isEmpty(
                    materialOwnerCode,materialOwnerName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialOwnerCode",baseMaterialOwnerDto.getMaterialOwnerCode());
            if (StringUtils.isNotEmpty(baseMaterialOwnerMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            BaseMaterialOwner baseMaterialOwner = new BaseMaterialOwner();
            BeanUtils.copyProperties(baseMaterialOwnerDto,baseMaterialOwner);
            baseMaterialOwner.setCreateTime(new Date());
            baseMaterialOwner.setCreateUserId(user.getUserId());
            baseMaterialOwner.setModifiedTime(new Date());
            baseMaterialOwner.setModifiedUserId(user.getUserId());
            baseMaterialOwner.setStatus(StringUtils.isEmpty(baseMaterialOwner.getStatus())?1:baseMaterialOwner.getStatus());
            baseMaterialOwner.setOrgId(user.getOrganizationId());
            list.add(baseMaterialOwner);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseMaterialOwnerMapper.insertList(list);
        }
        for (BaseMaterialOwner baseMaterialOwner : list) {
            BaseHtMaterialOwner baseHtMaterialOwner = new BaseHtMaterialOwner();
            BeanUtils.copyProperties(baseMaterialOwner,baseHtMaterialOwner);
            htList.add(baseHtMaterialOwner);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtMaterialOwnerMapper.insertList(htList);
        }
        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

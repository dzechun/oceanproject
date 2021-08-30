package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialListDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaterial;
import com.fantechs.common.base.general.entity.eam.EamJigMaterialList;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigMaterialMapper;
import com.fantechs.provider.eam.mapper.EamJigMaterialListMapper;
import com.fantechs.provider.eam.mapper.EamJigMaterialMapper;
import com.fantechs.provider.eam.service.EamJigMaterialService;
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
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamJigMaterialServiceImpl extends BaseService<EamJigMaterial> implements EamJigMaterialService {

    @Resource
    private EamJigMaterialMapper eamJigMaterialMapper;
    @Resource
    private EamJigMaterialListMapper eamJigMaterialListMapper;
    @Resource
    private EamHtJigMaterialMapper eamHtJigMaterialMapper;

    @Override
    public List<EamJigMaterialDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }

        return eamJigMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigMaterialDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.ifRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamJigMaterialMapper.insertUseGeneratedKeys(record);

        //治具绑定产品明细
        List<EamJigMaterialListDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaterialListDto eamJigMaterialListDto : list){
                eamJigMaterialListDto.setJigMaterialId(record.getJigMaterialId());
                eamJigMaterialListDto.setCreateUserId(user.getUserId());
                eamJigMaterialListDto.setCreateTime(new Date());
                eamJigMaterialListDto.setModifiedUserId(user.getUserId());
                eamJigMaterialListDto.setModifiedTime(new Date());
                eamJigMaterialListDto.setStatus(StringUtils.isEmpty(eamJigMaterialListDto.getStatus())?1: eamJigMaterialListDto.getStatus());
                eamJigMaterialListDto.setOrgId(user.getOrganizationId());
            }
            eamJigMaterialListMapper.insertList(list);
        }

        EamHtJigMaterial eamHtJigMaterial = new EamHtJigMaterial();
        BeanUtils.copyProperties(record,eamHtJigMaterial);
        int i = eamHtJigMaterialMapper.insert(eamHtJigMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigMaterialDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.ifRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigMaterialMapper.updateByPrimaryKeySelective(entity);

        //删除原治具绑定产品明细
        Example example1 = new Example(EamJigMaterialList.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigMaterialId", entity.getJigMaterialId());
        eamJigMaterialListMapper.deleteByExample(example1);

        //治具绑定产品明细
        List<EamJigMaterialListDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaterialListDto eamJigMaterialListDto : list){
                eamJigMaterialListDto.setJigMaterialId(entity.getJigMaterialId());
                eamJigMaterialListDto.setCreateUserId(user.getUserId());
                eamJigMaterialListDto.setCreateTime(new Date());
                eamJigMaterialListDto.setModifiedUserId(user.getUserId());
                eamJigMaterialListDto.setModifiedTime(new Date());
                eamJigMaterialListDto.setStatus(StringUtils.isEmpty(eamJigMaterialListDto.getStatus())?1: eamJigMaterialListDto.getStatus());
                eamJigMaterialListDto.setOrgId(user.getOrganizationId());
            }
            eamJigMaterialListMapper.insertList(list);
        }

        EamHtJigMaterial eamHtJigMaterial = new EamHtJigMaterial();
        BeanUtils.copyProperties(entity,eamHtJigMaterial);
        int i = eamHtJigMaterialMapper.insert(eamHtJigMaterial);

        return i;
    }

    private void ifRepeat(EamJigMaterialDto eamJigMaterialDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        //判断是否重复
        criteria.andEqualTo("jigId",eamJigMaterialDto.getJigId());
        if (StringUtils.isNotEmpty(eamJigMaterialDto.getJigMaterialId())){
            criteria.andNotEqualTo("jigMaterialId",eamJigMaterialDto.getJigMaterialId());
        }
        EamJigMaterial eamJigMaterial = eamJigMaterialMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(eamJigMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigMaterial> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigMaterial eamJigMaterial = eamJigMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigMaterial eamHtJigMaterial = new EamHtJigMaterial();
            BeanUtils.copyProperties(eamJigMaterial,eamHtJigMaterial);
            htList.add(eamHtJigMaterial);

            //删除治具绑定产品明细
            Example example1 = new Example(EamJigMaterialList.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigMaterialId", id);
            eamJigMaterialListMapper.deleteByExample(example1);
        }

        eamHtJigMaterialMapper.insertList(htList);

        return eamJigMaterialMapper.deleteByIds(ids);
    }
}
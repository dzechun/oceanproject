package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigReMaterial;
import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigReMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigReMaterialMapper;
import com.fantechs.provider.eam.mapper.EamJigReMaterialMapper;
import com.fantechs.provider.eam.service.EamJigReMaterialService;
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
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigReMaterialServiceImpl extends BaseService<EamJigReMaterial> implements EamJigReMaterialService {

    @Resource
    private EamJigReMaterialMapper eamJigReMaterialMapper;
    @Resource
    private EamHtJigReMaterialMapper eamHtJigReMaterialMapper;

    @Override
    public List<EamJigReMaterialDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        List<EamJigReMaterialDto> list = eamJigReMaterialMapper.findList(map);

        for (EamJigReMaterialDto eamJigReMaterialDto:list){
            SearchEamJigReMaterial searchEamJigReMaterial = new SearchEamJigReMaterial();
            searchEamJigReMaterial.setJigId(eamJigReMaterialDto.getJigId());
            List<EamJigReMaterialDto> materials = eamJigReMaterialMapper.findMaterial(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));
            eamJigReMaterialDto.setEamJigReMaterialDtos(materials);
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigReMaterial record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigReMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigId", record.getJigId())
                .andEqualTo("materialId",record.getMaterialId());
        EamJigReMaterial eamJigReMaterial = eamJigReMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJigReMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamJigReMaterialMapper.insertUseGeneratedKeys(record);

        //履历
        EamHtJigReMaterial eamHtJigReMaterial = new EamHtJigReMaterial();
        BeanUtils.copyProperties(record, eamHtJigReMaterial);
        int i = eamHtJigReMaterialMapper.insert(eamHtJigReMaterial);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigReMaterial entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigReMaterial.class);
        /*Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigId", entity.getJigId())
                .andEqualTo("materialId",entity.getMaterialId());
        EamJigReMaterial eamJigReMaterial = eamJigReMaterialMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJigReMaterial)&&!entity.getJigReMaterialId().equals(eamJigReMaterial.getJigReMaterialId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/

        int i = 0;
        List<EamHtJigReMaterial> htList = new ArrayList<>();
        example.clear();
        example.createCriteria().andEqualTo("jigId", entity.getJigId());
        List<EamJigReMaterial> list = eamJigReMaterialMapper.selectByExample(example);
        for (EamJigReMaterial jigReMaterial : list){
            jigReMaterial.setStatus(entity.getStatus());
            jigReMaterial.setModifiedTime(new Date());
            jigReMaterial.setModifiedUserId(user.getUserId());
            i += eamJigReMaterialMapper.updateByPrimaryKeySelective(jigReMaterial);

            //履历
            EamHtJigReMaterial eamHtJigReMaterial = new EamHtJigReMaterial();
            BeanUtils.copyProperties(entity, eamHtJigReMaterial);
            htList.add(eamHtJigReMaterial);
        }

        eamHtJigReMaterialMapper.insertList(htList);

        return i;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAddOrUpdate(List<EamJigReMaterial> list) {
        int sum = 0;

        Example example = new Example(EamJigReMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigId", list.get(0).getJigId());
        eamJigReMaterialMapper.deleteByExample(example);

        for (EamJigReMaterial eamJigReMaterial : list){
            sum += this.save(eamJigReMaterial);
        }

        return sum;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigReMaterial> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        int i = 0;
        for (String id : idArry) {
            EamJigReMaterial eamJigReMaterial = eamJigReMaterialMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamJigReMaterial)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //删除该治具与产品的所有绑定关系
            Example example = new Example(EamJigReMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("jigId", eamJigReMaterial.getJigId());
            List<EamJigReMaterial> eamJigReMaterials = eamJigReMaterialMapper.selectByExample(example);
            for (EamJigReMaterial jigReMaterial:eamJigReMaterials){
                EamHtJigReMaterial eamHtJigReMaterial = new EamHtJigReMaterial();
                BeanUtils.copyProperties(jigReMaterial, eamHtJigReMaterial);
                list.add(eamHtJigReMaterial);
            }

            i += eamJigReMaterialMapper.deleteByExample(example);
        }

        eamHtJigReMaterialMapper.insertList(list);

        return i;
    }
}

package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamSparePartDto;
import com.fantechs.common.base.general.entity.eam.EamSparePart;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePart;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtSparePartMapper;
import com.fantechs.provider.eam.mapper.EamSparePartMapper;
import com.fantechs.provider.eam.service.EamSparePartService;
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
 * Created by leifengzhi on 2021/09/17.
 */
@Service
public class EamSparePartServiceImpl extends BaseService<EamSparePart> implements EamSparePartService {

    @Resource
    private EamSparePartMapper eamSparePartMapper;
    @Resource
    private EamHtSparePartMapper eamHtSparePartMapper;

    @Override
    public List<EamSparePartDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamSparePartMapper.findList(map);
    }

    @Override
    public List<EamHtSparePart> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamHtSparePartMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamSparePart record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamSparePart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sparePartCode", record.getSparePartCode());
        EamSparePart eamSparePart = eamSparePartMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamSparePart)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamSparePartMapper.insertUseGeneratedKeys(record);

        //履历
        EamHtSparePart eamHtSparePart = new EamHtSparePart();
        BeanUtils.copyProperties(record, eamHtSparePart);
        int i = eamHtSparePartMapper.insertSelective(eamHtSparePart);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamSparePart entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamSparePart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sparePartCode", entity.getSparePartCode())
                .andNotEqualTo("sparePartId",entity.getSparePartId());
        EamSparePart eamSparePart = eamSparePartMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamSparePart)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamSparePartMapper.updateByPrimaryKeySelective(entity);

        //履历
        EamHtSparePart eamHtSparePart = new EamHtSparePart();
        BeanUtils.copyProperties(entity, eamHtSparePart);
        int i = eamHtSparePartMapper.insertSelective(eamHtSparePart);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<EamHtSparePart> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamSparePart eamSparePart = eamSparePartMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamSparePart)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtSparePart eamHtSparePart = new EamHtSparePart();
            BeanUtils.copyProperties(eamSparePart, eamHtSparePart);
            list.add(eamHtSparePart);
        }
        //履历
        eamHtSparePartMapper.insertList(list);

        return eamSparePartMapper.deleteByIds(ids);
    }
}

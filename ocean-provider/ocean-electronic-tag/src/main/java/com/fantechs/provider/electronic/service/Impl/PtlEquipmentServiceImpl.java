package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlEquipment;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.PtlEquipmentMapper;
import com.fantechs.provider.electronic.service.PtlEquipmentService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/23.
 */
@Service
public class PtlEquipmentServiceImpl extends BaseService<PtlEquipment> implements PtlEquipmentService {

    @Resource
    private PtlEquipmentMapper ptlEquipmentMapper;

    @Override
    public int save(PtlEquipment ptlEquipment) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(PtlEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", ptlEquipment.getEquipmentCode());

        PtlEquipment equipment = ptlEquipmentMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(equipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        ptlEquipment.setCreateUserId(user.getUserId());
        ptlEquipment.setCreateTime(new Date());
        ptlEquipment.setModifiedUserId(user.getUserId());
        ptlEquipment.setModifiedTime(new Date());
        ptlEquipment.setStatus(StringUtils.isEmpty(ptlEquipment.getStatus())?1: ptlEquipment.getStatus());
        int i = ptlEquipmentMapper.insertUseGeneratedKeys(ptlEquipment);

        return i;
    }

    @Override
    public int update(PtlEquipment ptlEquipment) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(PtlEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", ptlEquipment.getEquipmentCode())
                .andNotEqualTo("equipmentId", ptlEquipment.getEquipmentId());

        PtlEquipment equipment = ptlEquipmentMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(equipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        ptlEquipment.setModifiedTime(new Date());
        ptlEquipment.setModifiedUserId(user.getUserId());

        return ptlEquipmentMapper.updateByPrimaryKeySelective(ptlEquipment);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            PtlEquipment ptlEquipment = ptlEquipmentMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(ptlEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return ptlEquipmentMapper.deleteByIds(ids);
    }

    @Override
    public List<PtlEquipmentDto> findList(Map<String, Object> map) {
        return ptlEquipmentMapper.findList(map);
    }
}

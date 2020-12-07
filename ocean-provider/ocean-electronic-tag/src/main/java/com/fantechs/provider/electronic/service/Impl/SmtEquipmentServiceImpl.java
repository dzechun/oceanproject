package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtEquipment;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.SmtEquipmentMapper;
import com.fantechs.provider.electronic.service.SmtEquipmentService;
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
public class SmtEquipmentServiceImpl extends BaseService<SmtEquipment> implements SmtEquipmentService {

    @Resource
    private SmtEquipmentMapper smtEquipmentMapper;

    @Override
    public int save(SmtEquipment smtEquipment) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode",smtEquipment.getEquipmentCode());

        SmtEquipment equipment = smtEquipmentMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(equipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtEquipment.setCreateUserId(user.getUserId());
        smtEquipment.setCreateTime(new Date());
        smtEquipment.setModifiedUserId(user.getUserId());
        smtEquipment.setModifiedTime(new Date());
        smtEquipment.setStatus(StringUtils.isEmpty(smtEquipment.getStatus())?1:smtEquipment.getStatus());
        int i = smtEquipmentMapper.insertUseGeneratedKeys(smtEquipment);

        return i;
    }

    @Override
    public int update(SmtEquipment smtEquipment) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode",smtEquipment.getEquipmentCode())
                .andNotEqualTo("equipmentId",smtEquipment.getEquipmentId());

        SmtEquipment equipment = smtEquipmentMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(equipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtEquipment.setModifiedTime(new Date());
        smtEquipment.setModifiedUserId(user.getUserId());

        return smtEquipmentMapper.updateByPrimaryKeySelective(smtEquipment);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            SmtEquipment smtEquipment = smtEquipmentMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtEquipmentMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtEquipmentDto> findList(Map<String, Object> map) {
        return smtEquipmentMapper.findList(map);
    }
}

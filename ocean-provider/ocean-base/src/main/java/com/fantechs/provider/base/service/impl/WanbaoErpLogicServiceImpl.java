package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.WanbaoErpLogicDto;
import com.fantechs.common.base.general.entity.basic.WanbaoErpLogic;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.base.mapper.WanbaoErpLogicMapper;
import com.fantechs.provider.base.service.WanbaoErpLogicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2022/03/03.
 */
@Service
public class WanbaoErpLogicServiceImpl extends BaseService<WanbaoErpLogic> implements WanbaoErpLogicService {

    @Resource
    private WanbaoErpLogicMapper wanbaoErpLogicMapper;

    @Override
    public List<WanbaoErpLogicDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wanbaoErpLogicMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WanbaoErpLogic record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        //查重
        Example example = new Example(WanbaoErpLogic.class);
        example.createCriteria().andEqualTo("logicCode",record.getLogicCode());
        if(wanbaoErpLogicMapper.selectCountByExample(example)>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setStatus((byte)1);
        record.setOrgId(sysUser.getOrganizationId());
        return super.save(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WanbaoErpLogic entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        //查重
        Example example = new Example(WanbaoErpLogic.class);
        example.createCriteria().andEqualTo("logicCode",entity.getLogicCode()).andNotEqualTo("logicId",entity.getLogicId());
        if(wanbaoErpLogicMapper.selectCountByExample(example)>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        return super.update(entity);
    }
}

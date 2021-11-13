package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlClientManageDto;
import com.fantechs.common.base.electronic.entity.PtlClientManage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.electronic.service.PtlClientManageService;
import com.fantechs.provider.electronic.mapper.PtlClientManageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/01.
 */
@Service
public class PtlClientManageServiceImpl extends BaseService<PtlClientManage> implements PtlClientManageService {

    @Resource
    private PtlClientManageMapper ptlClientManageMapper;

    @Override
    public int save(PtlClientManage ptlClientManage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        ptlClientManage.setOrgId(user.getOrganizationId());
        ptlClientManage.setCreateUserId(user.getUserId());
        ptlClientManage.setCreateTime(new Date());
        ptlClientManage.setModifiedUserId(user.getUserId());
        ptlClientManage.setModifiedTime(new Date());
        ptlClientManage.setSecretKey(UUIDUtils.getUUID());
        ptlClientManageMapper.insertUseGeneratedKeys(ptlClientManage);
        ptlClientManage.setQueueName("ablepick" + ptlClientManage.getClientId());
        return ptlClientManageMapper.updateByPrimaryKeySelective(ptlClientManage);
    }

    @Override
    public int update(PtlClientManage ptlClientManage) {
        if (ptlClientManage.getLoginTag()==0){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
        }
        return ptlClientManageMapper.updateByPrimaryKeySelective(ptlClientManage);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            PtlClientManage ptlClientManage = ptlClientManageMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(ptlClientManage)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }
        return ptlClientManageMapper.deleteByIds(ids);
    }

    @Override
    public List<PtlClientManageDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return ptlClientManageMapper.findList(map);
    }
}

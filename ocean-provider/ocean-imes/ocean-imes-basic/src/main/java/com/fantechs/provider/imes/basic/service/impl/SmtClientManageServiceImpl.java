package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagController;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.imes.basic.mapper.SmtClientManageMapper;
import com.fantechs.provider.imes.basic.service.SmtClientManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/01.
 */
@Service
public class SmtClientManageServiceImpl extends BaseService<SmtClientManage> implements SmtClientManageService {

    @Resource
    private SmtClientManageMapper smtClientManageMapper;

    @Override
    public int save(SmtClientManage smtClientManage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        smtClientManage.setCreateUserId(user.getUserId());
        smtClientManage.setCreateTime(new Date());
        smtClientManage.setModifiedUserId(user.getUserId());
        smtClientManage.setModifiedTime(new Date());
        smtClientManage.setSecretKey(UUIDUtils.getUUID());
        smtClientManageMapper.insertUseGeneratedKeys(smtClientManage);
        smtClientManage.setQueueName("ocean.ablepick" + smtClientManage.getId());
        return smtClientManageMapper.updateByPrimaryKeySelective(smtClientManage);
    }

    @Override
    public int update(SmtClientManage smtClientManage) {
        if (smtClientManage.getLoginTag()==0){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
        }
        return smtClientManageMapper.updateByPrimaryKeySelective(smtClientManage);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            SmtClientManage smtClientManage = smtClientManageMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtClientManage)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }
        return smtClientManageMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtClientManageDto> findList(Map<String, Object> map) {
        return smtClientManageMapper.findList(map);
    }
}

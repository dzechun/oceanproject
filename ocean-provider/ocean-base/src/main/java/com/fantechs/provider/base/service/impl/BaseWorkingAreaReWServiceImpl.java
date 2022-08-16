package com.fantechs.provider.base.service.impl;

import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.general.entity.basic.BaseWorkingAreaReW;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorker;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseWorkingAreaReWMapper;
import com.fantechs.provider.base.service.BaseWorkingAreaReWService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseWorkingAreaReWServiceImpl extends BaseService<BaseWorkingAreaReW> implements BaseWorkingAreaReWService {

    @Resource
    private BaseWorkingAreaReWMapper baseWorkingAreaReWMapper;

    @Override
    public int saveDto(BaseWorkingAreaReWDto baseWorkingAreaReWDto) {

        BaseWorkingAreaReW baseWorkingAreaReW = new BaseWorkingAreaReW();
        BeanUtils.autoFillEqFields(baseWorkingAreaReWDto, baseWorkingAreaReW);

        return this.save(baseWorkingAreaReW);
    }

    @Override
    public int save(BaseWorkingAreaReW baseWorkingAreaReW) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        baseWorkingAreaReW.setWorkingAreaReWId(null);

        baseWorkingAreaReW.setCreateTime(DateUtils.getDateTimeString(new DateTime()));
        baseWorkingAreaReW.setCreateUserId(currentUserInfo.getUserId());
        baseWorkingAreaReW.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        baseWorkingAreaReW.setModifiedUserId(currentUserInfo.getUserId());
        baseWorkingAreaReW.setOrgId(currentUserInfo.getOrganizationId());

        if(baseWorkingAreaReWMapper.insertUseGeneratedKeys(baseWorkingAreaReW) <= 0) {
            recordHistory(baseWorkingAreaReW, currentUserInfo, "新增");
            return 0;
        }
        return 1;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        for(String id : ids.split(",")) {
            BaseWorkingAreaReW baseWorkingAreaReW = baseWorkingAreaReWMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseWorkingAreaReW)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
            recordHistory(baseWorkingAreaReW, currentUserInfo, "删除");
        }
        return baseWorkingAreaReWMapper.deleteByIds(ids);
    }

    @Override
    public int updateDto(BaseWorkingAreaReWDto baseWorkingAreaReWDto) {
        BaseWorkingAreaReW baseWorkingAreaReW = new BaseWorkingAreaReW();
        BeanUtils.autoFillEqFields(baseWorkingAreaReWDto, baseWorkingAreaReW);
        return this.update(baseWorkingAreaReW);
    }

    @Override
    public int update(BaseWorkingAreaReW baseWorkingAreaReW) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        baseWorkingAreaReW.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        baseWorkingAreaReW.setModifiedUserId(currentUserInfo.getUserId());

        recordHistory(baseWorkingAreaReW, currentUserInfo, "修改");

        return baseWorkingAreaReWMapper.updateByPrimaryKeySelective(baseWorkingAreaReW);
    }

    @Override
    public List<BaseWorkingAreaReWDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseWorkingAreaReWMapper.findList(map);
    }


    private void recordHistory(BaseWorkingAreaReW baseWorkingAreaReW, SysUser currentUserInfo, String operation) {

    }
}

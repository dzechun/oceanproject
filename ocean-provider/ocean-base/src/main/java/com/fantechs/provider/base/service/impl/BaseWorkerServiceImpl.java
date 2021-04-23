package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseWorkerMapper;
import com.fantechs.provider.base.service.BaseWorkerService;
import org.omg.CORBA.Current;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@Service
public class BaseWorkerServiceImpl extends BaseService<BaseWorker> implements BaseWorkerService {

    @Resource
    private BaseWorkerMapper baseWorkerMapper;



    @Override
    public int save(BaseWorker baseWorker) {
        SysUser currentUserInfo = this.getCurrentUserInfo();

        if(StringUtils.isEmpty(baseWorker.getUserId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "添加：用户ID不能为空");
        }
        if(StringUtils.isEmpty(baseWorker.getWarehouseId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "添加：仓库ID不能为空");
        }

        baseWorker.setCreateTime(new Date());
        baseWorker.setCreateUserId(currentUserInfo.getUserId());
        baseWorker.setModifiedTime(new Date());
        baseWorker.setModifiedUserId(currentUserInfo.getUserId());

        return baseWorkerMapper.insertUseGeneratedKeys(baseWorker);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = this.getCurrentUserInfo();
        for(String id : ids.split(",")) {
            BaseWorker baseWorker = baseWorkerMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseWorker)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
        }

        return baseWorkerMapper.deleteByIds(ids);
    }

    @Override
    public int update(BaseWorker baseWorker) {
        SysUser currentUserInfo = this.getCurrentUserInfo();

        if(StringUtils.isEmpty(baseWorker.getWorkerId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "更新：工人ID不能为空");
        }

        baseWorker.setModifiedTime(new Date());
        baseWorker.setModifiedUserId(currentUserInfo.getUserId());
        return baseWorkerMapper.updateByPrimaryKeySelective(baseWorker);
    }

    @Override
    public List<BaseWorkerDto> findList(Map<String, Object> map) {
        return baseWorkerMapper.findList(map);
    }

    private SysUser getCurrentUserInfo() {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }else{
            return currentUserInfo;
        }
    }
}

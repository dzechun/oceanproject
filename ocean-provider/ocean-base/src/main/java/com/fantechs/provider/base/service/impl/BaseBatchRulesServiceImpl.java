package com.fantechs.provider.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBatchRulesDto;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBatchRules;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBatchRules;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBatchRulesMapper;
import com.fantechs.provider.base.mapper.BaseHtBatchRulesMapper;
import com.fantechs.provider.base.service.BaseBatchRulesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseBatchRulesServiceImpl extends BaseService<BaseBatchRules> implements BaseBatchRulesService {

    @Resource
    private BaseBatchRulesMapper baseBatchRulesMapper;
    @Resource
    private BaseHtBatchRulesMapper baseHtBatchRulesMapper;

    @Override
    public List<BaseBatchRulesDto> findList(SearchBaseBatchRules searchBaseBatchRules) {
        return baseBatchRulesMapper.findList(searchBaseBatchRules);
    }

    @Override
    public int save(BaseBatchRules record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getBatchRulesName())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"批次名称不能为空");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = baseBatchRulesMapper.insertUseGeneratedKeys(record);

        //添加履历
        BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
        BeanUtil.copyProperties(record,baseHtBatchRules);
        baseHtBatchRulesMapper.insert(baseHtBatchRules);
        return num;
    }

    @Override
    public int update(BaseBatchRules entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        int num = baseBatchRulesMapper.updateByPrimaryKeySelective(entity);

        //添加履历
        BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
        BeanUtil.copyProperties(entity,baseHtBatchRules);
        baseHtBatchRulesMapper.insert(baseHtBatchRules);

        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        List<BaseHtBatchRules> list = new ArrayList<>();
        for (String s : arrId) {
            BaseBatchRules baseBatchRules = baseBatchRulesMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(baseBatchRules)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
            BeanUtil.copyProperties(baseBatchRules,baseHtBatchRules);
            list.add(baseHtBatchRules);
        }
        baseHtBatchRulesMapper.insertList(list);
        return super.batchDelete(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}

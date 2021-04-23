package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAcRe;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAql;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseSamplePlanAcReMapper;
import com.fantechs.provider.base.mapper.BaseSamplePlanAqlMapper;
import com.fantechs.provider.base.service.BaseSamplePlanAqlService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseSamplePlanAqlServiceImpl extends BaseService<BaseSamplePlanAql> implements BaseSamplePlanAqlService {

    @Resource
    private BaseSamplePlanAqlMapper baseSamplePlanAqlMapper;

    @Resource
    private BaseSamplePlanAcReMapper baseSamplePlanAcReMapper;

    @Override
    public List<BaseSamplePlanAqlDto> findList(Map<String, Object> map) {
        return baseSamplePlanAqlMapper.findList(map);
    }

    @Override
    public int batchSave(List<BaseSamplePlanAql> list) {
        try{
            for (BaseSamplePlanAql baseSamplePlanAql : list) {
                this.save(baseSamplePlanAql);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int batchUpdate(List<BaseSamplePlanAql> list) {
        try{
            if (StringUtils.isNotEmpty(list)){
                Example example = new Example(BaseSamplePlanAql.class);
                example.createCriteria().andEqualTo("samplePlanId",list.get(0).getSamplePlanId());
                baseSamplePlanAqlMapper.deleteByExample(example);
            }
            for (BaseSamplePlanAql baseSamplePlanAql : list) {
                this.save(baseSamplePlanAql);
            }
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int save(BaseSamplePlanAql record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseSamplePlanAqlMapper.insertUseGeneratedKeys(record);

        if (StringUtils.isNotEmpty(record.getList())){
            List<BaseSamplePlanAcRe> list = record.getList();
            for (BaseSamplePlanAcRe baseSamplePlanAcRe : list) {
                baseSamplePlanAcRe.setSamplePlanAqlId(record.getSamplePlanAqlId());
            }
            baseSamplePlanAcReMapper.insertList(list);
        }

        return i;
    }

    @Override
    public int update(BaseSamplePlanAql entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());

        Example example = new Example(BaseSamplePlanAcRe.class);
        example.createCriteria().andEqualTo("samplePlanAqlId",entity.getSamplePlanAqlId());
        baseSamplePlanAcReMapper.deleteByExample(example);

        if (StringUtils.isNotEmpty(entity.getList())){
            baseSamplePlanAcReMapper.insertList(entity.getList());
        }


        return baseSamplePlanAqlMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseSamplePlanAcRe.class);

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseSamplePlanAql baseSamplePlanAql = baseSamplePlanAqlMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSamplePlanAql)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            example.createCriteria().andEqualTo("samplePlanAqlId",id);
            baseSamplePlanAcReMapper.deleteByExample(example);
        }

        return baseSamplePlanAqlMapper.deleteByIds(ids);
    }
}

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
import java.util.ArrayList;
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
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
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
            //原来有的AQL只更新
            ArrayList<Long> idList = new ArrayList<>();
            if (StringUtils.isNotEmpty(list)) {
                for (BaseSamplePlanAql baseSamplePlanAql : list) {
                    if (StringUtils.isNotEmpty(baseSamplePlanAql.getSamplePlanAqlId())) {
                        baseSamplePlanAqlMapper.updateByPrimaryKeySelective(baseSamplePlanAql);
                        idList.add(baseSamplePlanAql.getSamplePlanAqlId());
                    }
                }
            }

            //删除原有AQL
            Example example = new Example(BaseSamplePlanAql.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("samplePlanId", list.get(0).getSamplePlanId());
            if (idList.size() > 0) {
                criteria.andNotIn("samplePlanAqlId", idList);
            }
            baseSamplePlanAqlMapper.deleteByExample(example);

            //新增新AQL
            for (BaseSamplePlanAql baseSamplePlanAql : list) {
                if (idList.contains(baseSamplePlanAql.getSamplePlanAqlId())) {
                    //只更新的AQL修改其对应AC、RE数据
                    if (StringUtils.isNotEmpty(baseSamplePlanAql.getList())){
                        Example example1 = new Example(BaseSamplePlanAcRe.class);
                        example1.createCriteria().andEqualTo("samplePlanAqlId",baseSamplePlanAql.getSamplePlanAqlId());
                        baseSamplePlanAcReMapper.deleteByExample(example1);

                        List<BaseSamplePlanAcRe> baseSamplePlanAcReList = baseSamplePlanAql.getList();
                        for (BaseSamplePlanAcRe baseSamplePlanAcRe : baseSamplePlanAcReList) {
                            baseSamplePlanAcRe.setSamplePlanAqlId(baseSamplePlanAql.getSamplePlanAqlId());
                        }
                        baseSamplePlanAcReMapper.insertList(baseSamplePlanAcReList);
                    }
                    continue;
                }

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

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseSamplePlanAqlMapper.insertUseGeneratedKeys(record);

        if (StringUtils.isNotEmpty(record.getList())){

            Example example = new Example(BaseSamplePlanAcRe.class);
            example.createCriteria().andEqualTo("samplePlanAqlId",record.getSamplePlanAqlId());
            baseSamplePlanAcReMapper.deleteByExample(example);

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

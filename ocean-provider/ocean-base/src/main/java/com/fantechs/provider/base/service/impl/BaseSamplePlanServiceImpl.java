package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessCategoryImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseSamplePlanImport;
import com.fantechs.common.base.general.entity.basic.BaseProcessCategory;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlan;
import com.fantechs.common.base.general.entity.basic.BaseSampleStandard;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplePlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSamplePlanMapper;
import com.fantechs.provider.base.mapper.BaseSamplePlanMapper;
import com.fantechs.provider.base.mapper.BaseSampleStandardMapper;
import com.fantechs.provider.base.service.BaseSamplePlanAqlService;
import com.fantechs.provider.base.service.BaseSamplePlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseSamplePlanServiceImpl extends BaseService<BaseSamplePlan> implements BaseSamplePlanService {

    @Resource
    private BaseSamplePlanMapper baseSamplePlanMapper;

    @Resource
    private BaseHtSamplePlanMapper baseHtSamplePlanMapper;

    @Resource
    private BaseSamplePlanAqlService baseSamplePlanAqlService;

    @Resource
    private BaseSampleStandardMapper baseSampleStandardMapper;

    @Override
    public List<BaseSamplePlanDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSamplePlanMapper.findList(map);
    }

    @Override
    public int save(BaseSamplePlan record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseSamplePlanMapper.insertUseGeneratedKeys(record);

        BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
        BeanUtils.copyProperties(record, baseHtSamplePlan);
        baseHtSamplePlanMapper.insertSelective(baseHtSamplePlan);

        return i;
    }

    @Override
    public int update(BaseSamplePlan entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
        BeanUtils.copyProperties(entity, baseHtSamplePlan);
        baseHtSamplePlanMapper.insertSelective(baseHtSamplePlan);

        return baseSamplePlanMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtSamplePlan> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        HashMap<String, Object> map = new HashMap<>();

        for (String id : idsArr) {
            BaseSamplePlan baseSamplePlan = baseSamplePlanMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSamplePlan)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
            BeanUtils.copyProperties(baseSamplePlan, baseHtSamplePlan);
            list.add(baseHtSamplePlan);
            map.put("samplePlanId",id);
            List<BaseSamplePlanAqlDto> samplingPlanAqlList = baseSamplePlanAqlService.findList(map);
            if (StringUtils.isNotEmpty(samplingPlanAqlList)){
                String aqlIds = "";
                for (BaseSamplePlanAqlDto baseSamplingPlanAqlDto : samplingPlanAqlList) {
                    aqlIds+=baseSamplingPlanAqlDto.getSamplePlanAqlId()+",";
                }
                baseSamplePlanAqlService.batchDelete(aqlIds.substring(0, aqlIds.length() - 1));
            }
        }

        baseHtSamplePlanMapper.insertList(list);
        return baseSamplePlanMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseSamplePlan entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseSamplePlan.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("samplePlanCode",entity.getSamplePlanCode());
        if (StringUtils.isNotEmpty(entity.getSamplePlanId())){
            criteria.andNotEqualTo("samplePlanId",entity.getSamplePlanId());
        }
        BaseSamplePlan baseSamplePlan = baseSamplePlanMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSamplePlan)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSamplePlanImport> baseSamplePlanImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseSamplePlan> list = new LinkedList<>();
        LinkedList<BaseHtSamplePlan> htList = new LinkedList<>();
        for (int i = 0; i < baseSamplePlanImports.size(); i++) {
            BaseSamplePlanImport baseSamplePlanImport = baseSamplePlanImports.get(i);
            String samplePlanCode = baseSamplePlanImport.getSamplePlanCode();

            if (StringUtils.isEmpty(
                    samplePlanCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseSamplePlan.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("samplePlanCode",samplePlanCode);
            if (StringUtils.isNotEmpty(baseSamplePlanMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BaseSamplePlan baseSamplePlan : list) {
                    if (baseSamplePlan.getSamplePlanCode().equals(samplePlanCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            //抽样标准信息
            String sampleStandardName = baseSamplePlanImport.getSampleStandardName();
            if(StringUtils.isNotEmpty(sampleStandardName)){
                Example example1 = new Example(BaseSampleStandard.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
                criteria1.andEqualTo("sampleStandardName",sampleStandardName);
                BaseSampleStandard baseSampleStandard = baseSampleStandardMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseSampleStandard)){
                    fail.add(i+4);
                    continue;
                }
                baseSamplePlanImport.setSampleStandardId(baseSampleStandard.getSampleStandardId());
            }

            BaseSamplePlan baseSamplePlan = new BaseSamplePlan();
            BeanUtils.copyProperties(baseSamplePlanImport, baseSamplePlan);
            baseSamplePlan.setCreateTime(new Date());
            baseSamplePlan.setCreateUserId(currentUser.getUserId());
            baseSamplePlan.setModifiedTime(new Date());
            baseSamplePlan.setModifiedUserId(currentUser.getUserId());
            baseSamplePlan.setStatus(StringUtils.isEmpty(baseSamplePlanImport.getStatus())?1:baseSamplePlanImport.getStatus().byteValue());
            baseSamplePlan.setOrgId(currentUser.getOrganizationId());
            list.add(baseSamplePlan);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseSamplePlanMapper.insertList(list);
        }

        for (BaseSamplePlan baseSamplePlan : list) {
            BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
            BeanUtils.copyProperties(baseSamplePlan, baseHtSamplePlan);
            htList.add(baseHtSamplePlan);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtSamplePlanMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}

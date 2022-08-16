package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShiftImport;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkShiftMapper;
import com.fantechs.provider.base.mapper.BaseWorkShiftMapper;
import com.fantechs.provider.base.mapper.BaseWorkShiftTimeMapper;
import com.fantechs.provider.base.service.BaseWorkShiftService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseWorkShiftServiceImpl extends BaseService<BaseWorkShift> implements BaseWorkShiftService {

    @Resource
    private BaseWorkShiftMapper baseWorkShiftMapper;
    @Resource
    private BaseHtWorkShiftMapper baseHtWorkShiftMapper;
    @Resource
    private BaseWorkShiftTimeMapper baseWorkShiftTimeMapper;

    @Override
    public int save(BaseWorkShift baseWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWorkShift.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("workShiftCode",baseWorkShift.getWorkShiftCode());
        BaseWorkShift baseWorkShift1 = baseWorkShiftMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShift1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增班次
        baseWorkShift.setCreateTime(new Date());
        baseWorkShift.setCreateUserId(user.getUserId());
        baseWorkShift.setModifiedTime(new Date());
        baseWorkShift.setModifiedUserId(user.getUserId());
        baseWorkShift.setStatus(StringUtils.isEmpty(baseWorkShift.getStatus())?1:baseWorkShift.getStatus());
        baseWorkShift.setOrganizationId(user.getOrganizationId());
        int i = baseWorkShiftMapper.insertUseGeneratedKeys(baseWorkShift);

        //新增班次履历
        BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
        BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
        baseHtWorkShiftMapper.insertSelective(baseHtWorkShift);

        return i;
    }

    @Override
    public int update(BaseWorkShift baseWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWorkShift.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("workShiftCode",baseWorkShift.getWorkShiftCode())
                .andNotEqualTo("workShiftId",baseWorkShift.getWorkShiftId());
        BaseWorkShift baseWorkShift1 = baseWorkShiftMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShift1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }


        baseWorkShift.setModifiedUserId(user.getUserId());
        baseWorkShift.setModifiedTime(new Date());
        baseWorkShift.setOrganizationId(user.getOrganizationId());

        BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
        BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
        baseHtWorkShiftMapper.insertSelective(baseHtWorkShift);

        return baseWorkShiftMapper.updateByPrimaryKeySelective(baseWorkShift);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtWorkShift> baseHtWorkShifts = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseWorkShift baseWorkShift = baseWorkShiftMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseWorkShift)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
            BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
            baseHtWorkShifts.add(baseHtWorkShift);
        }

        baseHtWorkShiftMapper.insertList(baseHtWorkShifts);
        return baseWorkShiftMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseWorkShiftDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseWorkShiftMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWorkShiftImport> baseWorkShiftImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWorkShift> list = new LinkedList<>();
        LinkedList<BaseHtWorkShift> htList = new LinkedList<>();
        LinkedList<BaseWorkShiftTime> baseWorkShiftTimes = new LinkedList<>();

        //排除不合法的数据
        Iterator<BaseWorkShiftImport> iterator = baseWorkShiftImports.iterator();
        int i = 0;
        while (iterator.hasNext()){
            BaseWorkShiftImport baseWorkShiftImport = iterator.next();
            String workShiftCode = baseWorkShiftImport.getWorkShiftCode();
            String workShiftName = baseWorkShiftImport.getWorkShiftName();
            Date startTime = baseWorkShiftImport.getStartTime();
            Date endTime = baseWorkShiftImport.getEndTime();

            //产品编码必传
            if (StringUtils.isEmpty(
                    workShiftCode, workShiftName, startTime, endTime
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWorkShift.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("workShiftCode",workShiftCode);
            BaseWorkShift baseWorkShift = baseWorkShiftMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseWorkShift)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            Map<String, List<BaseWorkShiftImport>> map = baseWorkShiftImports.stream().collect(Collectors.groupingBy(BaseWorkShiftImport::getWorkShiftCode, HashMap::new, Collectors.toList()));

            Set<String> keySet = map.keySet();
            for (String code : keySet) {
                List<BaseWorkShiftImport> workShiftImports = map.get(code);
                if (StringUtils.isNotEmpty(workShiftImports)){
                    //新增班次信息
                    BaseWorkShiftImport baseWorkShiftImport1 = workShiftImports.get(0);
                    BaseWorkShift workShift = new BaseWorkShift();
                    BeanUtils.copyProperties(baseWorkShiftImport1,workShift);
                    workShift.setModifiedTime(new Date());
                    workShift.setModifiedUserId(currentUser.getUserId());
                    workShift.setCreateTime(new Date());
                    workShift.setCreateUserId(currentUser.getUserId());
                    workShift.setOrganizationId(currentUser.getOrganizationId());
                    workShift.setStatus((byte) 1);
                    baseWorkShiftMapper.insertUseGeneratedKeys(workShift);

                    //新增履历
                    BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
                    BeanUtils.copyProperties(workShift,baseHtWorkShift);
                    htList.add(baseHtWorkShift);

                    //新增班次时间关系
                    for (BaseWorkShiftImport workShiftImport : workShiftImports) {
                        BaseWorkShiftTime baseWorkShiftTime = new BaseWorkShiftTime();
                        BeanUtils.copyProperties(workShiftImport,baseWorkShiftTime);
                        baseWorkShiftTime.setCreateTime(new Date());
                        baseWorkShiftTime.setCreateUserId(currentUser.getUserId());
                        baseWorkShiftTime.setModifiedTime(new Date());
                        baseWorkShiftTime.setModifiedUserId(currentUser.getUserId());
                        baseWorkShiftTime.setStatus((byte) 1);
                        baseWorkShiftTime.setOrganizationId(currentUser.getOrganizationId());
                        baseWorkShiftTime.setWorkShiftId(workShift.getWorkShiftId());
                        baseWorkShiftTimes.add(baseWorkShiftTime);
                    }
                    if (StringUtils.isNotEmpty(baseWorkShiftTimes)){
                        success += baseWorkShiftTimeMapper.insertList(baseWorkShiftTimes);
                    }
                }
            }

            if (StringUtils.isNotEmpty(htList)){
                baseHtWorkShiftMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStaffProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProcessService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wcz on 2020/09/25.
 */
@Service
public class BaseProcessServiceImpl extends BaseService<BaseProcess> implements BaseProcessService {

    @Resource
    private BaseProcessMapper baseProcessMapper;
    @Resource
    private BaseHtProcessMapper baseHtProcessMapper;
    @Resource
    private BaseStationMapper baseStationMapper;
    @Resource
    private BaseRouteProcessMapper baseRouteProcessMapper;
    @Resource
    private BaseProcessCategoryMapper baseProcessCategoryMapper;
    @Resource
    private BaseWorkshopSectionMapper baseWorkshopSectionMapper;
    @Resource
    private BaseStaffProcessMapper baseStaffProcessMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProcess baseProcess) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCode", baseProcess.getProcessCode())
                .orEqualTo("processName", baseProcess.getProcessName());
        List<BaseProcess> baseProcesses = baseProcessMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseProcesses)) {
            throw new BizErrorException("工序名称或编码重复");
        }

        baseProcess.setCreateUserId(currentUser.getUserId());
        baseProcess.setCreateTime(new Date());
        baseProcess.setModifiedUserId(currentUser.getUserId());
        baseProcess.setModifiedTime(new Date());
        baseProcess.setOrganizationId(currentUser.getOrganizationId());
        baseProcessMapper.insertUseGeneratedKeys(baseProcess);

        //新增工序历史信息
        BaseHtProcess baseHtProcess = new BaseHtProcess();
        BeanUtils.copyProperties(baseProcess, baseHtProcess);
        int i = baseHtProcessMapper.insertSelective(baseHtProcess);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtProcess> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] processIds = ids.split(",");
        for (String processId : processIds) {
            BaseProcess baseProcess = baseProcessMapper.selectByPrimaryKey(Long.parseLong(processId));
            if (StringUtils.isEmpty(baseProcess)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被工位引用
            Example example = new Example(BaseStation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processId", baseProcess.getProcessId());
            List<BaseStation> baseStations = baseStationMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseStations)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //被工艺路线引用
            Example example1 = new Example(BaseRouteProcess.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processId", baseProcess.getProcessId());
            List<BaseRouteProcess> baseRouteProcesses = baseRouteProcessMapper.selectByExample(example1);
            if (StringUtils.isNotEmpty(baseRouteProcesses)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增工序历史信息
            BaseHtProcess baseHtProcess = new BaseHtProcess();
            BeanUtils.copyProperties(baseProcess, baseHtProcess);
            baseHtProcess.setModifiedUserId(currentUser.getUserId());
            baseHtProcess.setModifiedTime(new Date());
            list.add(baseHtProcess);
        }
        baseHtProcessMapper.insertList(list);

        return baseProcessMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProcess baseProcess) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCode", baseProcess.getProcessCode())
                .orEqualTo("processName", baseProcess.getProcessName());

        BaseProcess process = baseProcessMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(process) && !process.getProcessId().equals(baseProcess.getProcessId())) {
            throw new BizErrorException("工序名称或编码重复");
        }

        baseProcess.setModifiedUserId(currentUser.getUserId());
        baseProcess.setModifiedTime(new Date());
        baseProcess.setOrganizationId(currentUser.getOrganizationId());
        int i = baseProcessMapper.updateByPrimaryKeySelective(baseProcess);

        //新增工序历史信息
        BaseHtProcess baseHtProcess = new BaseHtProcess();
        BeanUtils.copyProperties(baseProcess, baseHtProcess);
        baseHtProcessMapper.insertSelective(baseHtProcess);
        return i;
    }

    @Override
    public List<BaseProcess> findList(SearchBaseProcess searchBaseProcess) {
        List<BaseProcess> baseProcesses = baseProcessMapper.findList(searchBaseProcess);

        for (BaseProcess baseProcess : baseProcesses) {
            SearchBaseStaffProcess searchBaseStaffProcess = new SearchBaseStaffProcess();
            searchBaseStaffProcess.setProcessId(baseProcess.getProcessId());
            searchBaseStaffProcess.setStaffId(searchBaseProcess.getStaffId());
            List<BaseStaffProcess> baseStaffProcesses = baseStaffProcessMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcess));
            if (StringUtils.isNotEmpty(baseStaffProcesses)) {
                BaseStaffProcess baseStaffProcess = baseStaffProcesses.get(0);
                baseProcess.setEffectiveStartTime(baseStaffProcess.getEffectiveStartTime());
                baseProcess.setEffectiveEndTime(baseStaffProcess.getEffectiveEndTime());
            }
        }
        return baseProcesses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProcessImport> baseProcessImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseProcess> list = new LinkedList<>();
        LinkedList<BaseHtProcess> htList = new LinkedList<>();
        LinkedList<BaseProcessImport> processImports = new LinkedList<>();
        for (int i = 0; i < baseProcessImports.size(); i++) {
            BaseProcessImport baseProcessImport = baseProcessImports.get(i);
            String processCode = baseProcessImport.getProcessCode();
            String processName = baseProcessImport.getProcessName();
            String processCategoryCode = baseProcessImport.getProcessCategoryCode();
            String sectionCode = baseProcessImport.getSectionCode();
            if (StringUtils.isEmpty(
                    processCode, processName, processCategoryCode, sectionCode
            )) {
                fail.add(i + 4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processCode", processCode)
                    .orEqualTo("processName",processName);
            List<BaseProcess> processes = baseProcessMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(processes)) {
                throw new BizErrorException("工序名称或编码重复");
            }

            //判断工序类别是否存在
            Example example1 = new Example(BaseProcessCategory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processCategoryCode", processCategoryCode);
            BaseProcessCategory baseProcessCategory = baseProcessCategoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseProcessCategory)) {
                fail.add(i + 4);
                continue;
            }

            //判断工段是否存在
            if (StringUtils.isNotEmpty(sectionCode)) {
                Example example2 = new Example(BaseWorkshopSection.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("sectionCode", sectionCode);
                BaseWorkshopSection baseWorkshopSection = baseWorkshopSectionMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseWorkshopSection)) {
                    fail.add(i + 4);
                    continue;
                }
                baseProcessImport.setSectionId(baseWorkshopSection.getSectionId());
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(processImports)){
                for (BaseProcessImport processImport : processImports) {
                    if (processImport.getProcessCode().equals(processCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i + 4);
                continue;
            }

            processImports.add(baseProcessImport);
        }

        if (StringUtils.isNotEmpty(processImports)){
            for (BaseProcessImport processImport : processImports) {
                BaseProcess baseProcess = new BaseProcess();
                BeanUtils.copyProperties(processImport, baseProcess);
                baseProcess.setCreateTime(new Date());
                baseProcess.setCreateUserId(currentUser.getUserId());
                baseProcess.setModifiedTime(new Date());
                baseProcess.setModifiedUserId(currentUser.getUserId());
                baseProcess.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseProcess);
            }

            success = baseProcessMapper.insertList(list);

            for (BaseProcess baseProcess : list) {
                BaseHtProcess baseHtProcess = new BaseHtProcess();
                BeanUtils.copyProperties(baseProcess, baseHtProcess);
                htList.add(baseHtProcess);
            }

            baseHtProcessMapper.insertList(htList);
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行数", fail);
        return resultMap;
    }
}

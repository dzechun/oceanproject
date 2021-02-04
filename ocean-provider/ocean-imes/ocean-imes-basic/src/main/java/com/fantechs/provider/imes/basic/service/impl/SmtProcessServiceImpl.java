package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtProcess;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStaffProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtProcessService;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * Created by wcz on 2020/09/25.
 */
@Service
public class SmtProcessServiceImpl extends BaseService<SmtProcess> implements SmtProcessService {

    @Resource
    private SmtProcessMapper smtProcessMapper;
    @Resource
    private SmtHtProcessMapper smtHtProcessMapper;
    @Resource
    private SmtStationMapper smtStationMapper;
    @Resource
    private SmtRouteProcessMapper smtRouteProcessMapper;
    @Resource
    private SmtProcessCategoryMapper smtProcessCategoryMapper;
    @Resource
    private SmtWorkshopSectionMapper smtWorkshopSectionMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProcess smtProcess) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCode", smtProcess.getProcessCode());
        List<SmtProcess> smtProcesses = smtProcessMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProcesses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcess.setCreateUserId(currentUser.getUserId());
        smtProcess.setCreateTime(new Date());
        smtProcess.setModifiedUserId(currentUser.getUserId());
        smtProcess.setModifiedTime(new Date());
        smtProcessMapper.insertUseGeneratedKeys(smtProcess);

        //新增工序历史信息
        SmtHtProcess smtHtProcess = new SmtHtProcess();
        BeanUtils.copyProperties(smtProcess, smtHtProcess);
        int i = smtHtProcessMapper.insertSelective(smtHtProcess);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<SmtHtProcess> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] processIds = ids.split(",");
        for (String processId : processIds) {
            SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(Long.parseLong(processId));
            if (StringUtils.isEmpty(smtProcess)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被工位引用
            Example example = new Example(SmtStation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processId", smtProcess.getProcessId());
            List<SmtStation> smtStations = smtStationMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtStations)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //被工艺路线引用
            Example example1 = new Example(SmtRouteProcess.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processId", smtProcess.getProcessId());
            List<SmtRouteProcess> smtRouteProcesses = smtRouteProcessMapper.selectByExample(example1);
            if (StringUtils.isNotEmpty(smtRouteProcesses)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增工序历史信息
            SmtHtProcess smtHtProcess = new SmtHtProcess();
            BeanUtils.copyProperties(smtProcess, smtHtProcess);
            smtHtProcess.setModifiedUserId(currentUser.getUserId());
            smtHtProcess.setModifiedTime(new Date());
            list.add(smtHtProcess);
        }
        smtHtProcessMapper.insertList(list);

        return smtProcessMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProcess smtProcess) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCode", smtProcess.getProcessCode());

        SmtProcess process = smtProcessMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(process) && !process.getProcessId().equals(smtProcess.getProcessId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcess.setModifiedUserId(currentUser.getUserId());
        smtProcess.setModifiedTime(new Date());
        int i = smtProcessMapper.updateByPrimaryKeySelective(smtProcess);

        //新增工序历史信息
        SmtHtProcess smtHtProcess = new SmtHtProcess();
        BeanUtils.copyProperties(smtProcess, smtHtProcess);
        smtHtProcessMapper.insertSelective(smtHtProcess);
        return i;
    }

    @Override
    public List<SmtProcess> findList(SearchSmtProcess searchSmtProcess) {
        List<SmtProcess> smtProcesses = smtProcessMapper.findList(searchSmtProcess);

        for (SmtProcess smtProcess : smtProcesses) {
            SearchBaseStaffProcess searchBaseStaffProcess = new SearchBaseStaffProcess();
            searchBaseStaffProcess.setProcessId(smtProcess.getProcessId());
            searchBaseStaffProcess.setStaffId(searchSmtProcess.getStaffId());
            List<BaseStaffProcess> baseStaffProcesses = baseFeignApi.findStaffProcessList(searchBaseStaffProcess).getData();
            if (StringUtils.isNotEmpty(baseStaffProcesses)) {
                BaseStaffProcess baseStaffProcess = baseStaffProcesses.get(0);
                smtProcess.setEffectiveStartTime(baseStaffProcess.getEffectiveStartTime());
                smtProcess.setEffectiveEndTime(baseStaffProcess.getEffectiveEndTime());
            }
        }
        return smtProcesses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtProcess> smtProcesses) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtProcess> list = new LinkedList<>();
        LinkedList<SmtHtProcess> htList = new LinkedList<>();
        for (int i = 0; i < smtProcesses.size(); i++) {
            SmtProcess smtProcess = smtProcesses.get(i);
            String processCode = smtProcess.getProcessCode();
            String processName = smtProcess.getProcessName();
            String processCategoryCode = smtProcess.getProcessCategoryCode();
            if (StringUtils.isEmpty(
                    processCode, processName, processCategoryCode
            )) {
                fail.add(i + 3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processCode", processCode);
            if (StringUtils.isNotEmpty(smtProcessMapper.selectOneByExample(example))) {
                fail.add(i + 3);
                continue;
            }

            //判断工序类别是否存在
            Example example1 = new Example(SmtProcessCategory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processCategoryCode", processCategoryCode);
            SmtProcessCategory smtProcessCategory = smtProcessCategoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtProcessCategory)) {
                fail.add(i + 3);
                continue;
            }

            //判断工段是否存在
            if (StringUtils.isNotEmpty(smtProcess.getSectionCode())) {
                Example example2 = new Example(SmtWorkshopSection.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("sectionCode", smtProcess.getSectionCode());
                SmtWorkshopSection smtWorkshopSection = smtWorkshopSectionMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(smtWorkshopSection)) {
                    fail.add(i + 3);
                    continue;
                }
                smtProcess.setProcessId(smtWorkshopSection.getSectionId());
            }

            smtProcess.setCreateTime(new Date());
            smtProcess.setCreateUserId(currentUser.getUserId());
            smtProcess.setModifiedTime(new Date());
            smtProcess.setModifiedUserId(currentUser.getUserId());
            smtProcess.setStatus(1);
            smtProcess.setProcessCategoryId(smtProcessCategory.getProcessCategoryId());
            list.add(smtProcess);
        }

        if (StringUtils.isNotEmpty(list)) {
            success = smtProcessMapper.insertList(list);
        }

        for (SmtProcess smtProcess : list) {
            SmtHtProcess smtHtProcess = new SmtHtProcess();
            BeanUtils.copyProperties(smtProcess, smtHtProcess);
            htList.add(smtHtProcess);
        }


        if (StringUtils.isNotEmpty(htList)) {
            smtHtProcessMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数", success);
        resutlMap.put("操作失败行数", fail);
        return resutlMap;
    }
}

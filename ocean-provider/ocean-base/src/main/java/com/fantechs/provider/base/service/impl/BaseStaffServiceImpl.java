package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStaffDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseStaffImport;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStaff;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseStaffProcessService;
import com.fantechs.provider.base.service.BaseStaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2021/01/16.
 */
@Service
public class BaseStaffServiceImpl extends BaseService<BaseStaff> implements BaseStaffService {

    @Resource
    private BaseStaffMapper baseStaffMapper;
    @Resource
    private BaseHtStaffMapper baseHtStaffMapper;
    @Resource
    private BaseStaffProcessService baseStaffProcessService;
    @Resource
    private BaseTeamMapper baseTeamMapper;
    @Resource
    private BaseStaffProcessMapper baseStaffProcessMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Override
    public List<BaseStaffDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseStaffMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStaff baseStaff) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //????????????????????????
        Example example = new Example(BaseStaff.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("staffCode", baseStaff.getStaffCode());
        BaseStaff baseStaff1 = baseStaffMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStaff1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //??????????????????
        baseStaff.setCreateTime(new Date());
        baseStaff.setCreateUserId(user.getUserId());
        baseStaff.setModifiedTime(new Date());
        baseStaff.setModifiedUserId(user.getUserId());
        baseStaff.setOrganizationId(user.getOrganizationId());
        int i = baseStaffMapper.insertUseGeneratedKeys(baseStaff);

        //?????????????????????????????????
        List<BaseStaffProcess> baseStaffProcessList = baseStaff.getBaseStaffProcessList();
        if (StringUtils.isNotEmpty(baseStaffProcessList)){
            for (BaseStaffProcess baseStaffProcess : baseStaffProcessList) {
                baseStaffProcess.setStaffId(baseStaff.getStaffId());
                baseStaffProcess.setOrganizationId(user.getOrganizationId());
            }
            baseStaffProcessService.batchSave(baseStaff.getBaseStaffProcessList());
        }

        //????????????????????????
        BaseHtStaff baseHtStaff = new BaseHtStaff();
        BeanUtils.copyProperties(baseStaff, baseHtStaff);
        baseHtStaffMapper.insertSelective(baseHtStaff);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStaff baseStaff) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //????????????????????????
        Example example = new Example(BaseStaff.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("staffCode", baseStaff.getStaffCode())
                .andNotEqualTo("staffId", baseStaff.getStaffId());
        BaseStaff baseStaff1 = baseStaffMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStaff1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //??????????????????
        baseStaff.setModifiedUserId(user.getUserId());
        baseStaff.setModifiedTime(new Date());
        baseStaff.setOrganizationId(user.getOrganizationId());
        int i = baseStaffMapper.updateByPrimaryKeySelective(baseStaff);

        //????????????????????????
        Example example1 = new Example(BaseStaffProcess.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("staffId",baseStaff.getStaffId());
        baseStaffProcessService.deleteByExample(example1);
        List<BaseStaffProcess> baseStaffProcessList = baseStaff.getBaseStaffProcessList();
        if (StringUtils.isNotEmpty(baseStaffProcessList)) {
            for (BaseStaffProcess baseStaffProcess : baseStaffProcessList) {
                baseStaffProcess.setStaffId(baseStaff.getStaffId());
                baseStaffProcess.setOrganizationId(user.getOrganizationId());
            }
            baseStaffProcessService.batchSave(baseStaff.getBaseStaffProcessList());
        }


        //????????????????????????
        BaseHtStaff baseHtStaff = new BaseHtStaff();
        BeanUtils.copyProperties(baseStaff, baseHtStaff);
        baseHtStaffMapper.insertSelective(baseHtStaff);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseStaff baseStaff = baseStaffMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseStaff)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //??????????????????????????????
            Example example = new Example(BaseStaffProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("staffId", id);
            baseStaffProcessService.deleteByExample(example);
        }
        return baseStaffMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStaffImport> baseStaffImports) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????

        List<BaseStaffImport> baseStaffImportList = new ArrayList<>();//????????????????????????

        for (int i = 0; i < baseStaffImports.size(); i++) {
            BaseStaffImport baseStaffImport = baseStaffImports.get(i);
            String teamCode = baseStaffImport.getTeamCode();
            String staffCode = baseStaffImport.getStaffCode();
            String processCode = baseStaffImport.getProcessCode();

            //??????????????????
            if (StringUtils.isEmpty(
                    staffCode,processCode
            )) {
                fail.add(i + 4);
                continue;
            }

            //??????????????????????????????
            Example example = new Example(BaseStaff.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("staffCode",staffCode);
            BaseStaff baseStaff = baseStaffMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseStaff)){
                fail.add(i + 4);
                continue;
            }

            //??????????????????????????????
            SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
            searchBaseProcess.setCodeQueryMark(1);
            searchBaseProcess.setProcessCode(baseStaffImport.getProcessCode());
            List<BaseProcess> baseProcesses = baseProcessMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcess));
            if (StringUtils.isEmpty(baseProcesses)){
                fail.add(i + 4);
                continue;
            }
            baseStaffImport.setProcessId(baseProcesses.get(0).getProcessId());

            if (StringUtils.isNotEmpty(teamCode)){
                //??????????????????????????????
                Example example2 = new Example(BaseTeam.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria2.andEqualTo("teamCode",teamCode);
                BaseTeam baseTeam = baseTeamMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseTeam)){
                    fail.add(i + 4);
                    continue;
                }
                baseStaffImport.setTeamId(baseTeam.getTeamId());
            }

            baseStaffImportList.add(baseStaffImport);
        }

        //???????????????????????????
        Map<String, List<BaseStaffImport>> map = baseStaffImportList.stream().collect(Collectors.groupingBy(BaseStaffImport::getStaffCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<BaseStaffImport> baseStaffImportList1 = map.get(code);

            //??????????????????
            BaseStaff baseStaff = new BaseStaff();
            BeanUtils.copyProperties(baseStaffImportList1.get(0), baseStaff);
            baseStaff.setCreateTime(new Date());
            baseStaff.setCreateUserId(currentUser.getUserId());
            baseStaff.setModifiedTime(new Date());
            baseStaff.setModifiedUserId(currentUser.getUserId());
            baseStaff.setStatus((byte) 1);
            baseStaff.setOrganizationId(currentUser.getOrganizationId());
            baseStaffMapper.insertUseGeneratedKeys(baseStaff);

            //??????????????????
            BaseHtStaff baseHtStaff = new BaseHtStaff();
            BeanUtils.copyProperties(baseStaff, baseHtStaff);
            baseHtStaffMapper.insertSelective(baseHtStaff);

            for (BaseStaffImport baseStaffImport : baseStaffImportList1) {
                //????????????????????????
                BaseStaffProcess baseStaffProcess = new BaseStaffProcess();
                BeanUtils.copyProperties(baseStaffImport,baseStaffProcess);
                baseStaffProcess.setCreateTime(new Date());
                baseStaffProcess.setCreateUserId(currentUser.getUserId());
                baseStaffProcess.setModifiedTime(new Date());
                baseStaffProcess.setModifiedUserId(currentUser.getUserId());
                baseStaffProcess.setStatus((byte) 1);
                baseStaffProcess.setStaffId(baseStaff.getStaffId());
                baseStaffProcess.setOrganizationId(currentUser.getOrganizationId());
                success += baseStaffProcessMapper.insertSelective(baseStaffProcess);
            }
        }

        resultMap.put("??????????????????", success);
        resultMap.put("???????????????", fail);
        return resultMap;
    }
}

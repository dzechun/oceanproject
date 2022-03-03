package com.fantechs.provider.base.service.impl;


import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkerImport;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.general.entity.basic.BaseWorkingAreaReW;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorker;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.BaseWarehouseMapper;
import com.fantechs.provider.base.mapper.BaseWorkerMapper;
import com.fantechs.provider.base.mapper.BaseWorkingAreaMapper;
import com.fantechs.provider.base.mapper.BaseWorkingAreaReWMapper;
import com.fantechs.provider.base.service.BaseHtWorkerService;
import com.fantechs.provider.base.service.BaseWorkerService;
import com.fantechs.provider.base.service.BaseWorkingAreaReWService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@Service
@Transactional
public class BaseWorkerServiceImpl extends BaseService<BaseWorker> implements BaseWorkerService {

    @Resource
    private BaseWorkerMapper baseWorkerMapper;
    @Resource
    private BaseHtWorkerService baseHtWorkerService;
    @Resource
    private BaseWorkingAreaReWService baseWorkingAreaReWService;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;
    @Resource
    private BaseWorkingAreaMapper baseWorkingAreaMapper;
    @Resource
    private BaseWorkingAreaReWMapper baseWorkingAreaReWMapper;

    @Override
    public int saveDto(BaseWorkerDto baseWorkerDto) {

        BaseWorker baseWorker = new BaseWorker();
        BeanUtils.autoFillEqFields(baseWorkerDto, baseWorker);
        this.save(baseWorker);

        List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList = baseWorkerDto.getBaseWorkingAreaReWDtoList();
        for(BaseWorkingAreaReWDto baseWorkingAreaReWDto : baseWorkingAreaReWDtoList) {
            baseWorkingAreaReWDto.setWorkerId(baseWorker.getWorkerId());
            baseWorkingAreaReWService.saveDto(baseWorkingAreaReWDto);
        }


        return 1;
    }

    @Override
    public int save(BaseWorker baseWorker) {
        SysUser currentUserInfo = this.getCurrentUserInfo();

        if(StringUtils.isEmpty(baseWorker.getUserId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "添加：用户ID不能为空");
        }
        if(StringUtils.isEmpty(baseWorker.getWarehouseId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "添加：仓库ID不能为空");
        }

        baseWorker.setWorkerId(null);
        if(StringUtils.isEmpty(baseWorker.getStatus())) {
            baseWorker.setStatus((byte)1);
        }

        baseWorker.setCreateTime(DateUtils.getDateTimeString(new DateTime()));
        baseWorker.setCreateUserId(currentUserInfo.getUserId());
        baseWorker.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        baseWorker.setModifiedUserId(currentUserInfo.getUserId());
        baseWorker.setOrgId(currentUserInfo.getOrganizationId());

        if(baseWorkerMapper.insertUseGeneratedKeys(baseWorker) <= 0) {

            return 0;
        }
        recordHistory(baseWorker, currentUserInfo, "新增");
        return 1;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = this.getCurrentUserInfo();
        for(String id : ids.split(",")) {
            BaseWorker baseWorker = baseWorkerMapper.selectByPrimaryKey(Long.parseLong(id));
            if(StringUtils.isEmpty(baseWorker)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            baseWorker.setModifiedUserId(currentUserInfo.getUserId());
            baseWorker.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));

            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("workerId", id);
            List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList = baseWorkingAreaReWService.findList(searchMap);
            StringBuffer deleteIds = new StringBuffer();
            for(BaseWorkingAreaReWDto baseWorkingAreaReWDto : baseWorkingAreaReWDtoList) {
                deleteIds.append(baseWorkingAreaReWDto.getWorkingAreaReWId());
                deleteIds.append(',');
            }
            if(deleteIds.length() > 0) {
                deleteIds.deleteCharAt(deleteIds.length() - 1);
                baseWorkingAreaReWService.batchDelete(deleteIds.toString());
            }

            recordHistory(baseWorker, currentUserInfo, "删除");
            if(StringUtils.isEmpty(baseWorker)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404);
            }
        }

        return baseWorkerMapper.deleteByIds(ids);
    }

    @Override
    public int updateDto(BaseWorkerDto baseWorkerDto) {
        BaseWorker baseWorker = new BaseWorker();
        BeanUtils.autoFillEqFields(baseWorkerDto, baseWorker);
        this.update(baseWorker);

        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("workerId", baseWorker.getWorkerId());
        List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList = baseWorkingAreaReWService.findList(searchMap);
        StringBuffer deleteIds = new StringBuffer();
        for(BaseWorkingAreaReWDto baseWorkingAreaReWDto : baseWorkingAreaReWDtoList) {
            deleteIds.append(baseWorkingAreaReWDto.getWorkingAreaReWId());
            deleteIds.append(',');
        }
        if(deleteIds.length() > 0) {
            deleteIds.deleteCharAt(deleteIds.length() - 1);
            baseWorkingAreaReWService.batchDelete(deleteIds.toString());
        }

        for(BaseWorkingAreaReWDto baseWorkingAreaReWDto : baseWorkerDto.getBaseWorkingAreaReWDtoList()) {
            baseWorkingAreaReWDto.setWorkerId(baseWorker.getWorkerId());
            baseWorkingAreaReWService.saveDto((baseWorkingAreaReWDto));
        }

//        List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList = baseWorkerDto.getBaseWorkingAreaReWDtoList();
//        for(BaseWorkingAreaReWDto baseWorkingAreaReWDto : baseWorkingAreaReWDtoList) {
//            baseWorkingAreaReWService.updateDto(baseWorkingAreaReWDto);
//        }


        return 1;
    }

    @Override
    public int update(BaseWorker baseWorker) {
        SysUser currentUserInfo = this.getCurrentUserInfo();

        if(StringUtils.isEmpty(baseWorker.getWorkerId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "更新：工人ID不能为空");
        }

        baseWorker.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        baseWorker.setModifiedUserId(currentUserInfo.getUserId());

        recordHistory(baseWorker, currentUserInfo, "修改");
        return baseWorkerMapper.updateByPrimaryKeySelective(baseWorker);
    }

    @Override
    public BaseWorkerDto selectDtoByKey(Long id) {
        BaseWorkerDto baseWorkerDto = baseWorkerMapper.selectDtoByKey(id);

        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("workerId", baseWorkerDto.getWorkerId());
        List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList = baseWorkingAreaReWService.findList(searchMap);
        baseWorkerDto.setBaseWorkingAreaReWDtoList(baseWorkingAreaReWDtoList);

        return baseWorkerDto;
    }

    @Override
    public List<BaseWorkerDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseWorkerDto> baseWorkerDtoList = baseWorkerMapper.findList(map);
        for(BaseWorkerDto baseWorkerDto : baseWorkerDtoList) {
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("workerId", baseWorkerDto.getWorkerId());
            List<BaseWorkingAreaReWDto> baseWorkingAreaReWDtoList = new ArrayList<>(baseWorkingAreaReWService.findList(searchMap));
            baseWorkerDto.setBaseWorkingAreaReWDtoList(baseWorkingAreaReWDtoList);
        }
        return baseWorkerDtoList;
    }

    private SysUser getCurrentUserInfo() {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        return currentUserInfo;
    }

    private void recordHistory(BaseWorker baseWorker, SysUser currentUserInfo, String operation) {
        if(StringUtils.isEmpty(baseWorker)) {
            return;
        }
        BaseHtWorker baseHtWorker = new BaseHtWorker();
        BeanUtils.autoFillEqFields(baseWorker, baseHtWorker);
        baseHtWorker.setOption1(operation);
        baseHtWorker.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        baseHtWorker.setModifiedUserId(currentUserInfo.getUserId());
        baseHtWorkerService.save(baseHtWorker);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWorkerImport> baseWorkerImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        List<BaseWorkerImport> baseWorkerImportList = new ArrayList<>();//保存合格工作人员数据

        for (int i = 0; i < baseWorkerImports.size(); i++) {
            BaseWorkerImport baseWorkerImport = baseWorkerImports.get(i);
            String userCode = baseWorkerImport.getUserCode();
            String workingAreaCode = baseWorkerImport.getWorkingAreaCode();


            //判断必传字段
            if (StringUtils.isEmpty(
                    userCode,workingAreaCode
            )) {
                fail.add(i + 4);
                continue;
            }


            //判断用户信息是否存在
            SearchSysUser searchSysUser = new SearchSysUser();
            searchSysUser.setUserCode(userCode);
            List<SysUser> sysUsers = securityFeignApi.selectUsers(searchSysUser).getData();
            if (StringUtils.isEmpty(sysUsers)){
                fail.add(i + 4);
                continue;
            }
            baseWorkerImport.setUserId(sysUsers.get(0).getUserId());


            if (StringUtils.isNotEmpty(baseWorkerImport.getWarehouseCode())){
                //判断仓库信息是否存在
                Example example2 = new Example(BaseWarehouse.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("organizationId", currentUser.getOrganizationId())
                         .andEqualTo("warehouseCode",baseWorkerImport.getWarehouseCode());
                BaseWarehouse baseWarehouse = baseWarehouseMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseWarehouse)){
                    fail.add(i + 4);
                    continue;
                }
                baseWorkerImport.setWarehouseId(baseWarehouse.getWarehouseId());
            }

            //判断工作区信息是否存在
            Example example1 = new Example(BaseWorkingArea.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId())
                    .andEqualTo("workingAreaCode",workingAreaCode);
            BaseWorkingArea baseWorkingArea = baseWorkingAreaMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseWorkingArea)){
                fail.add(i + 4);
                continue;
            }
            baseWorkerImport.setWorkingAreaId(baseWorkingArea.getWorkingAreaId());

            baseWorkerImportList.add(baseWorkerImport);
        }

        //对合格数据进行分组
        HashMap<String, List<BaseWorkerImport>> map = baseWorkerImportList.stream().collect(Collectors.groupingBy(BaseWorkerImport::getUserCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<BaseWorkerImport> baseWorkerImports1 = map.get(code);

            //新增工作人员信息
            BaseWorker baseWorker = new BaseWorker();
            org.springframework.beans.BeanUtils.copyProperties(baseWorkerImports1.get(0), baseWorker);
            baseWorker.setCreateTime(DateUtils.getDateTimeString(new DateTime()));
            baseWorker.setCreateUserId(currentUser.getUserId());
            baseWorker.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
            baseWorker.setModifiedUserId(currentUser.getUserId());
            baseWorker.setStatus((byte) 1);
            baseWorker.setOrgId(currentUser.getOrganizationId());
            baseWorkerMapper.insertUseGeneratedKeys(baseWorker);

            //新增工作人员履历
            BaseHtWorker baseHtWorker = new BaseHtWorker();
            org.springframework.beans.BeanUtils.copyProperties(baseWorker, baseHtWorker);
            baseHtWorkerService.save(baseHtWorker);

            for (BaseWorkerImport baseWorkerImport : baseWorkerImports1) {
                //新增工作区工作人员关系
                BaseWorkingAreaReW baseWorkingAreaReW = new BaseWorkingAreaReW();
                org.springframework.beans.BeanUtils.copyProperties(baseWorkerImport,baseWorkingAreaReW);
                baseWorkingAreaReW.setWorkerId(baseWorker.getWorkerId());
                baseWorkingAreaReW.setCreateTime(DateUtils.getDateTimeString(new DateTime()));
                baseWorkingAreaReW.setCreateUserId(currentUser.getUserId());
                baseWorkingAreaReW.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
                baseWorkingAreaReW.setModifiedUserId(currentUser.getUserId());
                baseWorkingAreaReW.setStatus((byte) 1);
                baseWorkingAreaReW.setOrgId(currentUser.getOrganizationId());
                success += baseWorkingAreaReWMapper.insertSelective(baseWorkingAreaReW);
            }
        }

        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

}

package com.fantechs.provider.ews.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.ews.EwsHtWarningUserInfoDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningUserInfoDto;
import com.fantechs.common.base.general.dto.ews.imports.EwsWarningUserInfoImport;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningUserInfo;
import com.fantechs.common.base.general.entity.ews.EwsWarningUserInfo;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.ews.mapper.EwsHtWarningUserInfoMapper;
import com.fantechs.provider.ews.mapper.EwsWarningUserInfoMapper;
import com.fantechs.provider.ews.service.EwsWarningUserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by mr.lei on 2021/12/27.
 */
@Service
public class EwsWarningUserInfoServiceImpl extends BaseService<EwsWarningUserInfo> implements EwsWarningUserInfoService {

    @Resource
    private EwsWarningUserInfoMapper ewsWarningUserInfoMapper;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private EwsHtWarningUserInfoMapper ewsHtWarningUserInfoMapper;

    @Override
    public List<EwsWarningUserInfoDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getUserId());
        return ewsWarningUserInfoMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class )
    public Map<String, Object> importExcel(List<EwsWarningUserInfoImport> ewsWarningUserInfoImports) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        Map<String,String > map = new HashMap<>();
        List<Map<String,String>> failMap = new ArrayList<>();  //记录操作失败行数
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        int i=0;
        for (EwsWarningUserInfoImport ewsWarningUserInfoImport : ewsWarningUserInfoImports) {
            if(StringUtils.isEmpty(ewsWarningUserInfoImport.getUserCode())){
                map.put("1","员工工号不能为空");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            SearchSysUser searchSysUser = new SearchSysUser();
            searchSysUser.setUserCode(ewsWarningUserInfoImport.getUserCode());
            ResponseEntity<List<SysUser>> responseEntity = securityFeignApi.selectUsers(searchSysUser);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if(responseEntity.getData().isEmpty()){
                map.put(ewsWarningUserInfoImport.getUserCode(),"未查询到员工信息");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            Example example = new Example(EwsWarningUserInfo.class);
            example.createCriteria().andEqualTo("userId",responseEntity.getData().get(0).getUserId());
            int count = ewsWarningUserInfoMapper.selectCountByExample(example);
            if(count>0){
                map.put(ewsWarningUserInfoImport.getUserCode(),"员工信息已存在");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            EwsWarningUserInfo ewsWarningUserInfo = new EwsWarningUserInfo();
            BeanUtils.copyProperties(ewsWarningUserInfoImport,ewsWarningUserInfo);
            ewsWarningUserInfo.setEMailAddress(ewsWarningUserInfoImport.getMailAddress());
            ewsWarningUserInfo.setUserId(responseEntity.getData().get(0).getUserId());
            ewsWarningUserInfo.setCreateUserId(sysUser.getUserId());
            ewsWarningUserInfo.setCreateTime(new Date());
            ewsWarningUserInfo.setModifiedUserId(sysUser.getUserId());
            ewsWarningUserInfo.setModifiedTime(new Date());
            ewsWarningUserInfo.setOrgId(sysUser.getOrganizationId());
            success+=ewsWarningUserInfoMapper.insertSelective(ewsWarningUserInfo);
        }
        //添加日志
        SysImportAndExportLog log = new SysImportAndExportLog();
        log.setSucceedCount(ewsWarningUserInfoImports.size() - fail.size());
        log.setFailCount(fail.size());
        log.setFailInfo(failMap.toString());
        log.setOperatorUserId(sysUser.getUserId());
        log.setTotalCount(ewsWarningUserInfoImports.size());
        log.setType((byte)1);
        securityFeignApi.add(log);

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public List<EwsHtWarningUserInfoDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return ewsHtWarningUserInfoMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EwsWarningUserInfo record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getUserId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"预警人员不能为空");
        }
        Example example = new Example(EwsWarningUserInfo.class);
        example.createCriteria().andEqualTo("userId",record.getUserId());
        int count = ewsWarningUserInfoMapper.selectCountByExample(example);
        if(count>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"员工信息已存在");
        }
        record.setCreateUserId(sysUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(sysUser.getOrganizationId());
        int num = ewsWarningUserInfoMapper.insertUseGeneratedKeys(record);

        EwsHtWarningUserInfo ewsHtWarningUserInfo = new EwsHtWarningUserInfoDto();
        BeanUtils.copyProperties(record,ewsHtWarningUserInfo);
        ewsHtWarningUserInfoMapper.insertSelective(ewsHtWarningUserInfo);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EwsWarningUserInfo entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(entity.getUserId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"预警人员不能为空");
        }
        Example example = new Example(EwsWarningUserInfo.class);
        example.createCriteria().andEqualTo("userId",entity.getUserId()).andNotEqualTo("warningUserInfoId",entity.getWarningUserInfoId());
        int count = ewsWarningUserInfoMapper.selectCountByExample(example);
        if(count>0){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"员工信息已存在");
        }
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());

        EwsHtWarningUserInfo ewsHtWarningUserInfo = new EwsHtWarningUserInfoDto();
        BeanUtils.copyProperties(entity,ewsHtWarningUserInfo);
        ewsHtWarningUserInfoMapper.insertSelective(ewsHtWarningUserInfo);

        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            EwsWarningUserInfo ewsWarningUserInfo = ewsWarningUserInfoMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(ewsWarningUserInfo)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
        }
        return super.batchDelete(ids);
    }
}

package com.fantechs.provider.base.service.impl;


import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.general.entity.basic.BaseWorkingAreaReW;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorker;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkerMapper;
import com.fantechs.provider.base.mapper.BaseWorkerMapper;
import com.fantechs.provider.base.service.BaseHtWorkerService;
import com.fantechs.provider.base.service.BaseWorkerService;
import com.fantechs.provider.base.service.BaseWorkingAreaReWService;
import javafx.beans.binding.ObjectExpression;
import org.omg.CORBA.Current;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }else{
            return currentUserInfo;
        }
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

}

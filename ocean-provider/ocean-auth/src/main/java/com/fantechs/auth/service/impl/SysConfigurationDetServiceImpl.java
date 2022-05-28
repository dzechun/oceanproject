package com.fantechs.auth.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.security.SysConfigurationDetDto;
import com.fantechs.common.base.general.entity.security.SysConfigurationDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.auth.mapper.SysConfigurationDetMapper;
import com.fantechs.auth.service.SysConfigurationDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/09.
 */
@Service
public class SysConfigurationDetServiceImpl extends BaseService<SysConfigurationDet> implements SysConfigurationDetService {

    @Resource
    private SysConfigurationDetMapper sysConfigurationDetMapper;

    @Override
    public List<SysConfigurationDetDto> findList(Map<String, Object> map) {
        return sysConfigurationDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SysConfigurationDet> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int batchUpdate(List<SysConfigurationDet> sysConfigurationDets) {
        return sysConfigurationDetMapper.batchUpdate(sysConfigurationDets);
    }
}

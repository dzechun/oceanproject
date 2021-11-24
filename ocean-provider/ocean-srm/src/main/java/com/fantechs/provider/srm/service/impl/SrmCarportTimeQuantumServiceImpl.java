package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmCarportTimeQuantumDto;
import com.fantechs.common.base.general.entity.srm.SrmCarportTimeQuantum;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmCarportTimeQuantumMapper;
import com.fantechs.provider.srm.service.SrmCarportTimeQuantumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/23.
 */
@Service
public class SrmCarportTimeQuantumServiceImpl extends BaseService<SrmCarportTimeQuantum> implements SrmCarportTimeQuantumService {

    @Resource
    private SrmCarportTimeQuantumMapper srmCarportTimeQuantumMapper;

    @Override
    public List<SrmCarportTimeQuantumDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return srmCarportTimeQuantumMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<SrmCarportTimeQuantum> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        int i = 1;
        if (StringUtils.isNotEmpty(list)){
            List<SrmCarportTimeQuantum> addList = new ArrayList<>();
            for (SrmCarportTimeQuantum srmCarportTimeQuantum : list) {
                srmCarportTimeQuantum.setCreateUserId(user.getUserId());
                srmCarportTimeQuantum.setCreateTime(new Date());
                srmCarportTimeQuantum.setModifiedUserId(user.getUserId());
                srmCarportTimeQuantum.setModifiedTime(new Date());
                srmCarportTimeQuantum.setStatus(StringUtils.isEmpty(srmCarportTimeQuantum.getStatus())?1: srmCarportTimeQuantum.getStatus());
                srmCarportTimeQuantum.setOrgId(user.getOrganizationId());
                addList.add(srmCarportTimeQuantum);
            }

            if(StringUtils.isNotEmpty(addList)) {
                i = srmCarportTimeQuantumMapper.insertList(addList);
            }
        }
        return i;
    }


}

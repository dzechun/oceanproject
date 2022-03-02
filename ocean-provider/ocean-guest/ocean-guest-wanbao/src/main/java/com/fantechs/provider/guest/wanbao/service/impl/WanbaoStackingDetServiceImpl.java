package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.wanbao.mapper.WanbaoStackingDetMapper;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2022/01/21.
 */
@Service
public class WanbaoStackingDetServiceImpl extends BaseService<WanbaoStackingDet> implements WanbaoStackingDetService {

    @Resource
    private WanbaoStackingDetMapper wanbaoStackingDetMapper;

    @Override
    public List<WanbaoStackingDetDto> findList(Map<String, Object> map) {
        return wanbaoStackingDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WanbaoStackingDet> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int batchAdd(List<WanbaoStackingDet> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        for (WanbaoStackingDet det : list){
            det.setOrgId(user.getOrganizationId());
            det.setCreateTime(new Date());
            det.setCreateUserId(user.getUserId());
            det.setModifiedTime(new Date());
            det.setModifiedUserId(user.getUserId());
            det.setIsDelete((byte) 1);
            det.setStatus((byte) 1);
        }
        return wanbaoStackingDetMapper.insertList(list);
    }
}

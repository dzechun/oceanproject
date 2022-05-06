package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.wanbao.mapper.WanbaoStackingDetMapper;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingDetService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
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
    @Resource
    private WanbaoStackingService wanbaoStackingService;

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
        if (list.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码堆垛数据不能为空");
        }
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
        WanbaoStacking stacking = wanbaoStackingService.selectByKey(list.get(0).getStackingId());
        stacking.setUsageStatus((byte) 2);
        wanbaoStackingService.update(stacking);
        return wanbaoStackingDetMapper.insertList(list);
    }

    /**
     * 查找空闲并且有条码的堆垛
     *
     * @param proLineId
     * @return
     */
    @Override
    public List<WanbaoAutoStackingDto> findStackingByAuto(Long proLineId) {
        return wanbaoStackingDetMapper.findStackingByAuto(proLineId);
    }
}

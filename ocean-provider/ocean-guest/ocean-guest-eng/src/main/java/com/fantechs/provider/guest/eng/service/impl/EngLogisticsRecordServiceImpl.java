package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eng.EngLogisticsRecord;
import com.fantechs.common.base.general.entity.eng.EngUserFollowContractQtyOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.mapper.EngLogisticsRecordMapper;
import com.fantechs.provider.guest.eng.service.EngLogisticsRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/03.
 */
@Service
public class EngLogisticsRecordServiceImpl extends BaseService<EngLogisticsRecord> implements EngLogisticsRecordService {

    @Resource
    private EngLogisticsRecordMapper engLogisticsRecordMapper;

    @Override
    public List<EngLogisticsRecord> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return engLogisticsRecordMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EngLogisticsRecord record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());

        return engLogisticsRecordMapper.insertSelective(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EngLogisticsRecord entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        return engLogisticsRecordMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EngLogisticsRecord> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

package com.fantechs.provider.guest.wanbao.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.wanbao.dto.WanbaoReadHeadDto;
import com.fantechs.provider.guest.wanbao.mapper.WanbaoReadHeadMapper;
import com.fantechs.provider.guest.wanbao.model.WanbaoReadHead;
import com.fantechs.provider.guest.wanbao.service.WanbaoReadHeadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class WanbaoReadHeadServiceImpl extends BaseService<WanbaoReadHead> implements WanbaoReadHeadService {

    @Resource
    private WanbaoReadHeadMapper wanbaoReadHeadMapper;

    @Override
    public List<WanbaoReadHeadDto> findList(Map<String, Object> map) {
        return wanbaoReadHeadMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WanbaoReadHead> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

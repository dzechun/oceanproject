package com.fantechs.provider.guest.leisai.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.leisai.LeisaiHtProductAndHalfOrderDto;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProductAndHalfOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.leisai.mapper.LeisaiHtProductAndHalfOrderMapper;
import com.fantechs.provider.guest.leisai.service.LeisaiHtProductAndHalfOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/27.
 */
@Service
public class LeisaiHtProductAndHalfOrderServiceImpl extends BaseService<LeisaiHtProductAndHalfOrder> implements LeisaiHtProductAndHalfOrderService {

    @Resource
    private LeisaiHtProductAndHalfOrderMapper leisaiHtProductAndHalfOrderMapper;

    @Override
    public List<LeisaiHtProductAndHalfOrderDto> findList(Map<String, Object> map) {
        return leisaiHtProductAndHalfOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<LeisaiHtProductAndHalfOrder> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

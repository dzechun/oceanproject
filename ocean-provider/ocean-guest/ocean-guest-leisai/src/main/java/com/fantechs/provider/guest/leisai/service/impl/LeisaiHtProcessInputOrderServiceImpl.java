package com.fantechs.provider.guest.leisai.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.leisai.LeisaiHtProcessInputOrderDto;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProcessInputOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.guest.leisai.mapper.LeisaiHtProcessInputOrderMapper;
import com.fantechs.provider.guest.leisai.service.LeisaiHtProcessInputOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */
@Service
public class LeisaiHtProcessInputOrderServiceImpl extends BaseService<LeisaiHtProcessInputOrder> implements LeisaiHtProcessInputOrderService {

    @Resource
    private LeisaiHtProcessInputOrderMapper leisaiHtProcessInputOrderMapper;

    @Override
    public List<LeisaiHtProcessInputOrderDto> findList(Map<String, Object> map) {
        return leisaiHtProcessInputOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<LeisaiHtProcessInputOrder> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

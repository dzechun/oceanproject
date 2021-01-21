package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStaff;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtStaffMapper;
import com.fantechs.provider.base.service.BaseHtStaffService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/16.
 */
@Service
public class BaseHtStaffServiceImpl extends BaseService<BaseHtStaff> implements BaseHtStaffService {

    @Resource
    private BaseHtStaffMapper baseHtStaffMapper;

    @Override
    public List<BaseHtStaff> findHtList(Map<String, Object> map) {
        return baseHtStaffMapper.findHtList(map);
    }
}

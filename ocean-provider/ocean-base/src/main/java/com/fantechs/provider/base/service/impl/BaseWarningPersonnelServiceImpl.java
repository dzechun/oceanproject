package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseWarningPersonnel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseWarningPersonnelMapper;
import com.fantechs.provider.base.service.BaseWarningPersonnelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/03.
 */
@Service
public class BaseWarningPersonnelServiceImpl extends BaseService<BaseWarningPersonnel> implements BaseWarningPersonnelService {

    @Resource
    private BaseWarningPersonnelMapper baseWarningPersonnelMapper;

    @Override
    public List<BaseWarningPersonnel> findList(Map<String, Object> map) {
        return baseWarningPersonnelMapper.findList(map);
    }
}

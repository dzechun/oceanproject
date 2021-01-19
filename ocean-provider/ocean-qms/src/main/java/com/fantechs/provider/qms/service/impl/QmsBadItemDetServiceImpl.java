package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.dto.qms.QmsBadItemDetDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItemDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsBadItemDetMapper;
import com.fantechs.provider.qms.service.QmsBadItemDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/18.
 */
@Service
public class QmsBadItemDetServiceImpl extends BaseService<QmsBadItemDet> implements QmsBadItemDetService {

    @Resource
    private QmsBadItemDetMapper qmsBadItemDetMapper;

    @Override
    public List<QmsBadItemDetDto> findList(Map<String, Object> map) {
        return qmsBadItemDetMapper.findList(map);
    }
}

package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDetDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItemDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsInspectionItemDetMapper;
import com.fantechs.provider.qms.service.QmsInspectionItemDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class QmsInspectionItemDetServiceImpl extends BaseService<QmsInspectionItemDet> implements QmsInspectionItemDetService {

    @Resource
    private QmsInspectionItemDetMapper qmsInspectionItemDetMapper;

    @Override
    public List<QmsInspectionItemDetDto> findList(Map<String, Object> map) {
        return qmsInspectionItemDetMapper.findList(map);
    }
}

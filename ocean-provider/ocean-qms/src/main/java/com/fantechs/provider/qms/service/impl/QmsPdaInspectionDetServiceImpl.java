package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDetDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspectionDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsPdaInspectionDetMapper;
import com.fantechs.provider.qms.service.QmsPdaInspectionDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class QmsPdaInspectionDetServiceImpl extends BaseService<QmsPdaInspectionDet> implements QmsPdaInspectionDetService {

    @Resource
    private QmsPdaInspectionDetMapper qmsPdaInspectionDetMapper;

    @Override
    public List<QmsPdaInspectionDetDto> findList(Map<String, Object> map) {
        return qmsPdaInspectionDetMapper.findList(map);
    }
}

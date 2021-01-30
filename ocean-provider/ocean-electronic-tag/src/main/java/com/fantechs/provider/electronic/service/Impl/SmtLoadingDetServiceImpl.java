package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.SmtLoadingDetMapper;
import com.fantechs.provider.electronic.service.SmtLoadingDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class SmtLoadingDetServiceImpl extends BaseService<SmtLoadingDet> implements SmtLoadingDetService {

    @Resource
    private SmtLoadingDetMapper smtLoadingDetMapper;

    @Override
    public List<SmtLoadingDetDto> findList(Map<String, Object> map) {
             return smtLoadingDetMapper.findList(map);
    }
}

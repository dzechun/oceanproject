package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.general.entity.srm.history.SrmHtCarportTimeQuantum;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.srm.mapper.SrmHtCarportTimeQuantumMapper;
import com.fantechs.provider.srm.service.SrmHtCarportTimeQuantumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/23.
 */
@Service
public class SrmHtCarportTimeQuantumServiceImpl extends BaseService<SrmHtCarportTimeQuantum> implements SrmHtCarportTimeQuantumService {

    @Resource
    private SrmHtCarportTimeQuantumMapper srmHtCarportTimeQuantumMapper;

    @Override
    public List<SrmHtCarportTimeQuantum> findList(Map<String, Object> map) {
        return srmHtCarportTimeQuantumMapper.findList(map);
    }

}

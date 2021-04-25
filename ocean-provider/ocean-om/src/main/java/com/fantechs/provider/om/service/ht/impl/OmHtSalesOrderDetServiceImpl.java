package com.fantechs.provider.om.service.ht.impl;


import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.OmHtSalesOrderDetMapper;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class OmHtSalesOrderDetServiceImpl extends BaseService<OmHtSalesOrderDet> implements OmHtSalesOrderDetService {

    @Resource
    private OmHtSalesOrderDetMapper omHtSalesOrderDetMapper;

    @Override
    public int save(OmHtSalesOrderDet omHtSalesOrderDet) {
        return omHtSalesOrderDetMapper.insertUseGeneratedKeys(omHtSalesOrderDet);
    }

    @Override
    public List<OmHtSalesOrderDetDto> findList(Map<String, Object> map) {
        return omHtSalesOrderDetMapper.findList(map);
    }
}

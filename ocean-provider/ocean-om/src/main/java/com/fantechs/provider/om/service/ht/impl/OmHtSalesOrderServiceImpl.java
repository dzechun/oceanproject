package com.fantechs.provider.om.service.ht.impl;


import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.OmHtSalesOrderMapper;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class OmHtSalesOrderServiceImpl extends BaseService<OmHtSalesOrder> implements OmHtSalesOrderService {

    @Resource
    private OmHtSalesOrderMapper omHtSalesOrderMapper;

    @Override
    public int save(OmHtSalesOrder omHtSalesOrder) {
        return omHtSalesOrderMapper.insertUseGeneratedKeys(omHtSalesOrder);
    }

    @Override
    public List<OmHtSalesOrderDto> findList(Map<String, Object> map) {
        return omHtSalesOrderMapper.findList(map);
    }
}

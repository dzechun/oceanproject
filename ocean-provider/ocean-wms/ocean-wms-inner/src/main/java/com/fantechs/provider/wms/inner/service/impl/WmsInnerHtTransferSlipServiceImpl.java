package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtTransferSlipMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtTransferSlipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/09.
 */
@Service
public class WmsInnerHtTransferSlipServiceImpl extends BaseService<WmsInnerHtTransferSlip> implements WmsInnerHtTransferSlipService {

    @Resource
    private WmsInnerHtTransferSlipMapper wmsInnerHtTransferSlipMapper;

    @Override
    public List<WmsInnerHtTransferSlip> findHtList(Map<String, Object> map) {
        return wmsInnerHtTransferSlipMapper.findHtList(map);
    }
}

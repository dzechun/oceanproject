package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.inner.mapper.WmsInnerDirectTransferOrderDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class WmsInnerDirectTransferOrderDetServiceImpl extends BaseService<WmsInnerDirectTransferOrderDet> implements WmsInnerDirectTransferOrderDetService {

    @Resource
    private WmsInnerDirectTransferOrderDetMapper wmsInnerDirectTransferOrderDetMapper;

    @Override
    public List<WmsInnerDirectTransferOrderDetDto> findList(Map<String, Object> map) {
        return wmsInnerDirectTransferOrderDetMapper.findList(map);
    }

}

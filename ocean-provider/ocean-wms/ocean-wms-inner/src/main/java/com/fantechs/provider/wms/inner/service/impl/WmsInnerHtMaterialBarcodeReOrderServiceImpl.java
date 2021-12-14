package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtMaterialBarcodeReOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtMaterialBarcodeReOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerHtMaterialBarcodeReOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/14.
 */
@Service
public class WmsInnerHtMaterialBarcodeReOrderServiceImpl extends BaseService<WmsInnerHtMaterialBarcodeReOrder> implements WmsInnerHtMaterialBarcodeReOrderService {

    @Resource
    private WmsInnerHtMaterialBarcodeReOrderMapper wmsInnerHtMaterialBarcodeReOrderMapper;

    @Override
    public List<WmsInnerHtMaterialBarcodeReOrder> findList(Map<String, Object> map) {
        return wmsInnerHtMaterialBarcodeReOrderMapper.findList(map);
    }
}

package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReprintDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReprint;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeReprintMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReprintService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/16.
 */
@Service
public class WmsInnerMaterialBarcodeReprintServiceImpl extends BaseService<WmsInnerMaterialBarcodeReprint> implements WmsInnerMaterialBarcodeReprintService {

    @Resource
    private WmsInnerMaterialBarcodeReprintMapper wmsInnerMaterialBarcodeReprintMapper;

    @Override
    public List<WmsInnerMaterialBarcodeReprintDto> findList(Map<String, Object> map) {
        return wmsInnerMaterialBarcodeReprintMapper.findList(map);
    }

}

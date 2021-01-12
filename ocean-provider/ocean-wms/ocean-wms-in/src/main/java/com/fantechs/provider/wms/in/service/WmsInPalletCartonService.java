package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.entity.wms.in.WmsInPalletCarton;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.IService;


/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface WmsInPalletCartonService extends IService<WmsInPalletCarton> {

    String checkPallet(String palletCode);
}

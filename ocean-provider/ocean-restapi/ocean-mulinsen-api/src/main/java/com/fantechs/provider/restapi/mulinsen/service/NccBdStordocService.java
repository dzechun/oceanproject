package com.fantechs.provider.restapi.mulinsen.service;

import com.fantechs.common.base.general.entity.mulinsen.NccBdStordoc;
import com.fantechs.common.base.support.IService;

public interface NccBdStordocService extends IService<NccBdStordoc> {

    int synchronizeNccBdStordoc() throws Exception;
}
package com.fantechs.provider.guest.meidi.service;

import com.fantechs.common.base.general.entity.leisai.LeisaiProcessInputOrder;
import com.fantechs.common.base.support.IService;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepation;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */

public interface MeterialPrepationService extends IService<LeisaiProcessInputOrder> {

    int send(MeterialPrepation meterialPrepation);

}

package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.entity.PtlLoadingDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface PtlLoadingDetService extends IService<PtlLoadingDet> {

    List<PtlLoadingDetDto> findList(Map<String, Object> map);

}

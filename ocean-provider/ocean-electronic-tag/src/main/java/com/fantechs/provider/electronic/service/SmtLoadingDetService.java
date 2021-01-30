package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface SmtLoadingDetService extends IService<SmtLoadingDet> {

    List<SmtLoadingDetDto> findList(Map<String, Object> map);

}

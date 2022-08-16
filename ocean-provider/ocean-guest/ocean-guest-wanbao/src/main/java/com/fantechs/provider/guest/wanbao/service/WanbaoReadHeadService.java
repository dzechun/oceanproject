package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.support.IService;
import com.fantechs.common.base.general.dto.wanbao.WanbaoReadHeadDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoReadHead;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface WanbaoReadHeadService extends IService<WanbaoReadHead> {
    List<WanbaoReadHeadDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WanbaoReadHeadDto> list);
}

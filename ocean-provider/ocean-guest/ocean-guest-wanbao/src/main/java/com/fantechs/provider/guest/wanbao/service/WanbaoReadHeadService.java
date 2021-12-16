package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.support.IService;
import com.fantechs.provider.guest.wanbao.dto.WanbaoReadHeadDto;
import com.fantechs.provider.guest.wanbao.model.WanbaoReadHead;

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

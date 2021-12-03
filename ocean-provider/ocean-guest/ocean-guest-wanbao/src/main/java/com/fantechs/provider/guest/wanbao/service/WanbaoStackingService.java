package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.support.IService;
import com.fantechs.provider.guest.wanbao.dto.WanbaoStackingDto;
import com.fantechs.provider.guest.wanbao.model.WanbaoStacking;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface WanbaoStackingService extends IService<WanbaoStacking> {
    List<WanbaoStackingDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WanbaoStackingDto> list);
}

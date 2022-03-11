package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.support.IService;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface WanbaoStackingService extends IService<WanbaoStacking> {
    List<WanbaoStackingDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WanbaoStackingDto> list);

    int updateAndClearBarcode(WanbaoStacking wanbaoStacking);
}

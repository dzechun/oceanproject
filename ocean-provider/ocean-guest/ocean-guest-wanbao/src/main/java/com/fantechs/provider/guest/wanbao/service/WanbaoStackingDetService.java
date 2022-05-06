package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2022/01/21.
 */

public interface WanbaoStackingDetService extends IService<WanbaoStackingDet> {
    List<WanbaoStackingDetDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WanbaoStackingDet> list);

    int batchAdd(List<WanbaoStackingDet> list);

    /**
     * 查找空闲并且有条码的堆垛
     * @param proLineId
     * @return
     */
    List<WanbaoAutoStackingDto> findStackingByAuto(Long proLineId);
}

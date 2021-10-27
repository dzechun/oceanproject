package com.fantechs.provider.guest.leisai.service;

import com.fantechs.common.base.general.dto.leisai.LeisaiHtProcessInputOrderDto;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProcessInputOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */

public interface LeisaiHtProcessInputOrderService extends IService<LeisaiHtProcessInputOrder> {
    List<LeisaiHtProcessInputOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<LeisaiHtProcessInputOrder> list);
}

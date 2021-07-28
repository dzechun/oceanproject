package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigStandingBook;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamHtJigStandingBookService extends IService<EamHtJigStandingBook> {
    List<EamHtJigStandingBook> findHtList(Map<String, Object> map);
}

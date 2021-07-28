package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.EamHtJigStandingBook;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/28.
 */

public interface EamHtJigStandingBookService extends IService<EamHtJigStandingBook> {
    List<EamHtJigStandingBook> findList(Map<String, Object> map);
}

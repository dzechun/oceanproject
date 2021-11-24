package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmHtCarportTimeQuantum;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/23.
 */

public interface SrmHtCarportTimeQuantumService extends IService<SrmHtCarportTimeQuantum> {
    List<SrmHtCarportTimeQuantum> findList(Map<String, Object> map);
}

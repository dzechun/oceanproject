package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmCarportTimeQuantumDto;
import com.fantechs.common.base.general.entity.srm.SrmCarportTimeQuantum;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/23.
 */

public interface SrmCarportTimeQuantumService extends IService<SrmCarportTimeQuantum> {
    List<SrmCarportTimeQuantumDto> findList(Map<String, Object> map);
}

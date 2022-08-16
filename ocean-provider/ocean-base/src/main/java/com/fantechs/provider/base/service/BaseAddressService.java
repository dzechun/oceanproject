package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseAddressImport;
import com.fantechs.common.base.general.entity.basic.BaseAddress;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/13.
 */

public interface BaseAddressService extends IService<BaseAddress> {

        List<BaseAddressDto> findList(Map<String, Object> map);

        Map<String, Object> importExcel(List<BaseAddressImport> baseAddressImports);
        }

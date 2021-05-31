package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlClientManageDto;
import com.fantechs.common.base.electronic.entity.PtlClientManage;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2020/12/01.
 */

public interface PtlClientManageService extends IService<PtlClientManage> {

    List<PtlClientManageDto> findList(Map<String, Object> map);
    }

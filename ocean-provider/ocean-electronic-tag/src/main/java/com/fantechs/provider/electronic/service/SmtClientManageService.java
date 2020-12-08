package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtClientManageDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2020/12/01.
 */

public interface SmtClientManageService extends IService<SmtClientManage> {

    List<SmtClientManageDto> findList(Map<String, Object> map);
    }

package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.support.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2020/12/01.
 */

public interface SmtClientManageService extends IService<SmtClientManage> {

    List<SmtClientManageDto> findList(Map<String, Object> map);
    }

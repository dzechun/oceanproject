package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.entity.basic.SmtClientManage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LoginService {

    List<SmtEquipmentDto> login(SmtClientManage smtClientManage, HttpServletRequest request);
}

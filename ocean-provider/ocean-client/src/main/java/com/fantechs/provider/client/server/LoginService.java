package com.fantechs.provider.client.server;

import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlClientManage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LoginService {

    List<PtlEquipmentDto> login(PtlClientManage ptlClientManage, HttpServletRequest request);
}

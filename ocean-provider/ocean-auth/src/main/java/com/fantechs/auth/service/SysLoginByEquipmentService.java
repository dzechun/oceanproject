package com.fantechs.auth.service;

import com.fantechs.common.base.response.ResponseEntity;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */

public interface SysLoginByEquipmentService {
    ResponseEntity eamLogin(String username, String password,Long orgId,String mac ,String type);
}

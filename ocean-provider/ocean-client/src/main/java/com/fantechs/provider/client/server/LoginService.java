package com.fantechs.provider.client.server;

import com.fantechs.common.base.entity.basic.SmtClientManage;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    SmtClientManage login(SmtClientManage smtClientManage, HttpServletRequest request);
}

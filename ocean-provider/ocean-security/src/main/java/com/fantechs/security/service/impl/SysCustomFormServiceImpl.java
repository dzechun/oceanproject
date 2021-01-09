package com.fantechs.security.service.impl;

import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.service.SysCustomFormService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormServiceImpl  extends BaseService<SysCustomForm> implements SysCustomFormService {

         @Resource
         private SysCustomFormMapper sysCustomFormMapper;
}

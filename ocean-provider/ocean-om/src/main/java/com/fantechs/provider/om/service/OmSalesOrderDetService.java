package com.fantechs.provider.om.service;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/19.
 */

public interface OmSalesOrderDetService extends IService<OmSalesOrderDet> {
    List<OmSalesOrderDetDto> findList(Map<String, Object> map);

    int saveDto(OmSalesOrderDetDto omSalesOrderDetDto, String customerOrderCode, Integer lineNumber, SysUser currentUserInfo);

    int updateDto(OmSalesOrderDetDto omSalesOrderDetDto, SysUser currentUserInfo);
}

package com.fantechs.provider.om.service.impl.sales;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.sales.OmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.sales.OmSalesOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.sales.OmSalesOrderMapper;
import com.fantechs.provider.om.service.sales.OmSalesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/19.
 */
@Service
public class OmSalesOrderServiceImpl extends BaseService<OmSalesOrder> implements OmSalesOrderService {

    @Resource
    private OmSalesOrderMapper omSalesOrderMapper;

    @Override
    public int save(OmSalesOrder omSalesOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException((ErrorCodeEnum.UAC10011039));
        }

        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(new Date());

        recordHistory(omSalesOrder, "新增");

        if(omSalesOrderMapper.insert(omSalesOrder)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for(String id : idArray) {
            OmSalesOrder omSalesOrder = omSalesOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(omSalesOrder, "删除");
        }
        if(omSalesOrderMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int update(OmSalesOrder omSalesOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty((currentUserInfo))) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(new Date());

        recordHistory(omSalesOrder, "更新");

        if(omSalesOrderMapper.updateByPrimaryKey(omSalesOrder)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<OmSalesOrderDto> findList(Map<String, Object> map) {
        return omSalesOrderMapper.findList(map);
    }

    @Override
    public List<OmSalesOrderDto> findHtList(Map<String, Object> map) {
        return omSalesOrderMapper.findHtList(map);
    }

    private void recordHistory(OmSalesOrder omSalesOrder, String operation) {

    }
}

package com.fantechs.provider.om.service.impl.sales;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.sales.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.sales.OmSalesOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.sales.OmSalesOrderDetMapper;
import com.fantechs.provider.om.service.sales.OmSalesOrderDetService;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
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
public class OmSalesOrderDetServiceImpl extends BaseService<OmSalesOrderDet> implements OmSalesOrderDetService {

    @Resource
    private OmSalesOrderDetMapper omSalesOrderDetMapper;

    @Override
    public int save(OmSalesOrderDet omSalesOrderDet) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        omSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedTime(new Date());

        recordHistory(omSalesOrderDet, "新增");

        if(omSalesOrderDetMapper.insert(omSalesOrderDet)<=0) {
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
            OmSalesOrderDet omSalesOrderDet = omSalesOrderDetMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesOrderDet)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(omSalesOrderDet, "删除");
        }
        if(omSalesOrderDetMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int update(OmSalesOrderDet omSalesOrderDet) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        omSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedTime(new Date());

        recordHistory(omSalesOrderDet, "更新");

        if(omSalesOrderDetMapper.updateByPrimaryKey(omSalesOrderDet)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<OmSalesOrderDetDto> findList(Map<String, Object> map) {
        return omSalesOrderDetMapper.findList(map);
    }

    @Override
    public List<OmSalesOrderDetDto> findHtList(Map<String, Object> map) {
        return omSalesOrderDetMapper.findHtList(map);
    }

    private void recordHistory(OmSalesOrderDet omSalesOrderDet, String operation) {

    }
}

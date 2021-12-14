package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.mapper.WmsInReceivingOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInReceivingOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInReceivingOrderServiceImpl extends BaseService<WmsInReceivingOrder> implements WmsInReceivingOrderService {

    @Resource
    private WmsInReceivingOrderMapper wmsInReceivingOrderMapper;
    @Resource
    private WmsInReceivingOrderDetMapper wmsInReceivingOrderDetMapper;

    @Override
    public List<WmsInReceivingOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInReceivingOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInReceivingOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        record.setCreateUserId(sysUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        if(StringUtils.isNotEmpty(record.getIsPdaCreate()) && record.getIsPdaCreate()==1){
            record.setOrderStatus((byte)3);
        }else {
            record.setOrderStatus((byte)1);
        }

        int num = wmsInReceivingOrderMapper.insertUseGeneratedKeys(record);
        if(!record.getWmsInReceivingOrderDets().isEmpty()){
            int i = 0;
            for (WmsInReceivingOrderDet wmsInReceivingOrderDet : record.getWmsInReceivingOrderDets()) {
                i++;
                wmsInReceivingOrderDet.setReceivingOrderId(record.getReceivingOrderId());
                wmsInReceivingOrderDet.setLineNumber(i+"");
                wmsInReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setModifiedTime(new Date());
                wmsInReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setOrgId(sysUser.getOrganizationId());

                if(StringUtils.isNotEmpty(record.getIsPdaCreate()) && record.getIsPdaCreate()==1){
                    wmsInReceivingOrderDet.setOperatorUserId(sysUser.getUserId());
                    wmsInReceivingOrderDet.setPlanQty(wmsInReceivingOrderDet.getActualQty());
                    wmsInReceivingOrderDet.setLineStatus((byte)3);

                    //新增条码记录
                }else {
                    wmsInReceivingOrderDet.setLineStatus((byte)1);
                    //获取条码记录
                }
            }
            num = wmsInReceivingOrderDetMapper.insertList(record.getWmsInReceivingOrderDets());
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInReceivingOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsInReceivingOrder> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

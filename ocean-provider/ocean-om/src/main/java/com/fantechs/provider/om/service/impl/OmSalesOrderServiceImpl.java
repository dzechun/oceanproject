package com.fantechs.provider.om.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.om.mapper.OmSalesOrderMapper;
import com.fantechs.provider.om.service.OmSalesOrderDetService;
import com.fantechs.provider.om.service.OmSalesOrderService;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
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
    @Resource
    private OmSalesOrderDetService omSalesOrderDetService;
    @Resource
    private OmHtSalesOrderService omHtSalesOrderService;

    @Override
    public int saveDto(OmSalesOrderDto omSalesOrderDto) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        OmSalesOrder omSalesOrder = new OmSalesOrder();

        BeanUtils.autoFillEqFields(omSalesOrderDto, omSalesOrder);

        if(this.save(omSalesOrder, currentUserInfo) <= 0) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(), "保存表头失败");
            return 0;
        }

        for(OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDto.getOmSalesOrderDetDtoList()) {
            omSalesOrderDetDto.setSalesOrderId(omSalesOrder.getSalesOrderId());
            if(omSalesOrderDetService.saveDto(omSalesOrderDetDto, omSalesOrder.getCustomerOrderCode(), currentUserInfo) <= 0) {
                return 0;
            }
        }

        return 1;
    }


    private int save(OmSalesOrder omSalesOrder, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUserInfo)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        if(StringUtils.isEmpty(omSalesOrder.getContractCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "合同号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getCustomerOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "客户订单号不能为空");
        }

        omSalesOrder.setSalesOrderId(null);
        omSalesOrder.setSalesOrderCode(CodeUtils.getId("SEORD"));
        omSalesOrder.setOrgId(currentUserInfo.getOrganizationId());
        omSalesOrder.setCreateTime(new Date());
        omSalesOrder.setCreateUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(new Date());

        int result = omSalesOrderMapper.insertUseGeneratedKeys(omSalesOrder);

        recordHistory(omSalesOrder, "新增");
//
        return result;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for(String id : idArray) {
            OmSalesOrder omSalesOrder = omSalesOrderMapper.selectByPrimaryKey(Long.valueOf(id));
            if(StringUtils.isEmpty(omSalesOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(omSalesOrder, "删除");
            //获取对应表体id
            //删除表体先
            SearchOmSalesOrderDetDto searchOmSalesOrderDetDto = new SearchOmSalesOrderDetDto();
            searchOmSalesOrderDetDto.setSalesOrderId(omSalesOrder.getSalesOrderId());
            List<OmSalesOrderDetDto> list = omSalesOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDetDto));
            StringBuffer detIds = new StringBuffer();
            for(OmSalesOrderDetDto omSalesOrderDetDto : list) {
                detIds.append(omSalesOrderDetDto.getSalesOrderDetId().toString());
                detIds.append(',');
            }
            detIds.deleteCharAt(detIds.length()-1);
            if(omSalesOrderDetService.batchDelete(detIds.toString()) <= 0) {
                return 0;
            }
        }
        if(omSalesOrderMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int updateDto(OmSalesOrderDto omSalesOrderDto) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        OmSalesOrder omSalesOrder = new OmSalesOrder();

        BeanUtils.autoFillEqFields(omSalesOrderDto, omSalesOrder);

        if(this.update(omSalesOrder, currentUserInfo) <= 0) {
            return 0;
        }

        for(OmSalesOrderDetDto omSalesOrderDetDto : omSalesOrderDto.getOmSalesOrderDetDtoList()) {
            if(omSalesOrderDetService.updateDto(omSalesOrderDetDto, currentUserInfo) <= 0) {
                return 0;
            }
        }

        return 1;
    }

    private int update(OmSalesOrder omSalesOrder, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty((currentUserInfo))) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        if(StringUtils.isEmpty(omSalesOrder.getSalesOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "销售订单号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getContractCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "合同号不能为空");
        }

        if(StringUtils.isEmpty(omSalesOrder.getCustomerOrderCode())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "客户订单号不能为空");
        }

        omSalesOrder.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrder.setModifiedTime(new Date());


        if(omSalesOrderMapper.updateByPrimaryKeySelective(omSalesOrder)<=0) {
            return 0;
        }
        recordHistory(omSalesOrder, "更新");
        return 1;
    }

    @Override
    public List<OmSalesOrderDto> findList(Map<String, Object> map) {
        List<OmSalesOrderDto> omSalesOrderDtoList = omSalesOrderMapper.findList(map);
        for(OmSalesOrderDto omSalesOrderDto : omSalesOrderDtoList) {
            Map<String, Object> omSalesOrderDetMap = new HashMap<>();
            omSalesOrderDetMap.put("salesOrderId", omSalesOrderDto.getSalesOrderId());
            omSalesOrderDto.setOmSalesOrderDetDtoList(omSalesOrderDetService.findList(omSalesOrderDetMap));
        }
        return omSalesOrderDtoList;
    }


    private void recordHistory(OmSalesOrder omSalesOrder, String operation) {
        OmHtSalesOrder omHtSalesOrder = new OmHtSalesOrder();
        if(StringUtils.isEmpty(omSalesOrder)) {
            return;
        }
        BeanUtils.autoFillEqFields(omSalesOrder, omHtSalesOrder);
        omHtSalesOrder.setOption1(operation);
        omHtSalesOrderService.save(omHtSalesOrder);
    }
}

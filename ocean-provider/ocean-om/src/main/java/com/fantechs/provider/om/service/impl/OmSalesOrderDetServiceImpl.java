package com.fantechs.provider.om.service.impl;


import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.om.mapper.OmSalesOrderDetMapper;
import com.fantechs.provider.om.service.OmSalesOrderDetService;
import com.fantechs.provider.om.service.ht.OmHtSalesOrderDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private OmHtSalesOrderDetService omHtSalesOrderDetService;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public int saveDto(OmSalesOrderDetDto omSalesOrderDetDto, String customerOrderCode, Integer lineNumber, SysUser currentUserInfo) {
        OmSalesOrderDet omSalesOrderDet = new OmSalesOrderDet();

        BeanUtils.autoFillEqFields(omSalesOrderDetDto, omSalesOrderDet);

        return this.save(omSalesOrderDet, customerOrderCode, lineNumber, currentUserInfo);
    }

    private int save(OmSalesOrderDet omSalesOrderDet, String customerOrderCode, Integer lineNumber, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUserInfo)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }


        if(StringUtils.isEmpty(omSalesOrderDet.getSalesOrderId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "销售订单ID不能为空");
        }

        //数据库有限制这个字段不能为空
//        if(StringUtils.isEmpty(omSalesOrderDet.getProjectCode())) {
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "项目号不能为空");
//        }

        omSalesOrderDet.setSalesOrderDetId(null);


//        if(StringUtils.isEmpty(omSalesOrderDet.getSourceLineNumber())) {
//            omSalesOrderDet.setCustomerOrderLineNumber(customerOrderCode + String.format("%03d", lineNumber));
//        }
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("wanbaoSyncData");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (specItems.isEmpty()){
            omSalesOrderDet.setCustomerOrderLineNumber(customerOrderCode + String.format("%03d", lineNumber));
        }else {
            JSONObject jsonObject = JSON.parseObject(specItems.get(0).getParaValue());
            if(!jsonObject.get("enable").equals(1)){
                omSalesOrderDet.setCustomerOrderLineNumber(customerOrderCode + String.format("%03d", lineNumber));
            }
        }
        omSalesOrderDet.setSourceLineNumber(String.format("%03d", lineNumber));
        Integer num = lineNumber+1;
        omSalesOrderDet.setLineNumber(String.format("%02d",num));

        omSalesOrderDet.setOrgId(currentUserInfo.getOrganizationId());
        omSalesOrderDet.setCreateTime(DateUtils.getDateTimeString(new DateTime()));
        omSalesOrderDet.setCreateUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));

        int result = omSalesOrderDetMapper.insertUseGeneratedKeys(omSalesOrderDet);
        recordHistory(omSalesOrderDet, currentUserInfo);

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
            OmSalesOrderDet omSalesOrderDet = omSalesOrderDetMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesOrderDet)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(omSalesOrderDet, currentUserInfo);
        }
        if(omSalesOrderDetMapper.deleteByIds(ids)<=0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int updateDto(OmSalesOrderDetDto omSalesOrderDetDto, SysUser currentUserInfo) {
        OmSalesOrderDet omSalesOrderDet = new OmSalesOrderDet();

        BeanUtils.autoFillEqFields(omSalesOrderDetDto, omSalesOrderDet);

        if(this.update(omSalesOrderDet, currentUserInfo) <= 0) {
            return 0;
        }

        return 1;
    }

    private int update(OmSalesOrderDet omSalesOrderDet, SysUser currentUserInfo) {
//        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(currentUserInfo)) {
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }
        //装车作业出货数量反写
        if(StringUtils.isNotEmpty(omSalesOrderDet.getIsWriteQty()) && omSalesOrderDet.getIsWriteQty()==1){
            OmSalesOrderDet det = omSalesOrderDetMapper.selectByPrimaryKey(omSalesOrderDet.getSalesOrderDetId());
            if(StringUtils.isEmpty(det.getTotalOutboundQty())){
                det.setTotalOutboundQty(BigDecimal.ZERO);
            }
            omSalesOrderDet.setTotalOutboundQty(omSalesOrderDet.getTotalOutboundQty().add(det.getTotalOutboundQty()));
        }
        omSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
        omSalesOrderDet.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));


        if(omSalesOrderDetMapper.updateByPrimaryKeySelective(omSalesOrderDet)<=0) {
            return 0;
        }
        recordHistory(omSalesOrderDet, currentUserInfo);
        return 1;
    }

    @Override
    public List<OmSalesOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
        searchSysSpecItemFiveRing.setSpecCode("wanbaoSelectPO");
        List<SysSpecItem> wanbaoSelectPOs = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
        if (!wanbaoSelectPOs.isEmpty() && wanbaoSelectPOs.size() > 0
            && user.getOrganizationId().toString().equals(wanbaoSelectPOs.get(0).getParaValue())){
            map.put("po", 1);
        }
        return omSalesOrderDetMapper.findList(map);
    }

    private void recordHistory(OmSalesOrderDet omSalesOrderDet, SysUser currentUserInfo){
        OmHtSalesOrderDet omHtSalesOrderDet = new OmHtSalesOrderDet();
        if(StringUtils.isEmpty(omSalesOrderDet)){
            return;
        }
        BeanUtils.autoFillEqFields(omSalesOrderDet, omHtSalesOrderDet);
//        omHtSalesOrderDet.setCustomerOrderLineNumber(Long.parseLong(omSalesOrderDet.getCustomerOrderLineNumber()));
        omHtSalesOrderDet.setModifiedTime(DateUtils.getDateTimeString(new DateTime()));
        omHtSalesOrderDet.setModifiedUserId(currentUserInfo.getUserId());
//        omHtSalesOrderDetService.save(omHtSalesOrderDet);
    }
}

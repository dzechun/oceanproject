package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.service.OmPurchaseReturnOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */
@Service
public class OmPurchaseReturnOrderDetServiceImpl extends BaseService<OmPurchaseReturnOrderDet> implements OmPurchaseReturnOrderDetService {

    @Resource
    private OmPurchaseReturnOrderDetMapper omPurchaseReturnOrderDetMapper;
    @Resource
    private OmHtPurchaseReturnOrderDetMapper omHtPurchaseReturnOrderDetMapper;

    @Override
    public List<OmPurchaseReturnOrderDetDto> findList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omPurchaseReturnOrderDetMapper.findList(map);
    }

    @Override
    public List<OmHtPurchaseReturnOrderDet> findHtList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omHtPurchaseReturnOrderDetMapper.findHtList(map);
    }

    /**
     * 更新累计下发数量
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePutDownQty(Long detId, BigDecimal putawayQty) {
        int num=1;
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        OmPurchaseReturnOrderDet omPurchaseReturnOrderDet = omPurchaseReturnOrderDetMapper.selectByPrimaryKey(detId);
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDet)){
            if(StringUtils.isEmpty(omPurchaseReturnOrderDet.getTotalIssueQty())){
                omPurchaseReturnOrderDet.setTotalIssueQty(new BigDecimal(0));
            }

            omPurchaseReturnOrderDet.setTotalIssueQty(omPurchaseReturnOrderDet.getTotalIssueQty().subtract(putawayQty));
            omPurchaseReturnOrderDet.setIfAllIssued((byte)0);
            omPurchaseReturnOrderDet.setModifiedUserId(sysUser.getUserId());
            omPurchaseReturnOrderDet.setModifiedTime(new Date());
            num+=omPurchaseReturnOrderDetMapper.updateByPrimaryKeySelective(omPurchaseReturnOrderDet);

        }
        return num;
    }
}

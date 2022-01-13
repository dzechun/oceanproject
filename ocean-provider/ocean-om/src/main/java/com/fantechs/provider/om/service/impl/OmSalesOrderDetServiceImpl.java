package com.fantechs.provider.om.service.impl;


import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmSalesOrderDetMapper;
import com.fantechs.provider.om.service.OmSalesOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Transactional(rollbackFor = Exception.class)
    public int update(OmSalesOrderDet omSalesOrderDet) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //装车作业出货数量反写
        if(StringUtils.isNotEmpty(omSalesOrderDet.getIsWriteQty()) && omSalesOrderDet.getIsWriteQty()==1){
            OmSalesOrderDet det = omSalesOrderDetMapper.selectByPrimaryKey(omSalesOrderDet.getSalesOrderDetId());
            if(StringUtils.isEmpty(det.getTotalOutboundQty())){
                det.setTotalOutboundQty(BigDecimal.ZERO);
            }
            omSalesOrderDet.setTotalOutboundQty(omSalesOrderDet.getTotalOutboundQty().add(det.getTotalOutboundQty()));
        }
        omSalesOrderDet.setModifiedUserId(user.getUserId());
        omSalesOrderDet.setModifiedTime(new DateTime());

        return omSalesOrderDetMapper.updateByPrimaryKeySelective(omSalesOrderDet);
    }

    @Override
    public List<OmSalesOrderDetDto> findList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omSalesOrderDetMapper.findList(map);
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
        OmSalesOrderDet omSalesOrderDet = omSalesOrderDetMapper.selectByPrimaryKey(detId);
        if(StringUtils.isNotEmpty(omSalesOrderDet)){
            if(StringUtils.isEmpty(omSalesOrderDet.getTotalIssueQty())){
                omSalesOrderDet.setTotalIssueQty(new BigDecimal(0));
            }

            omSalesOrderDet.setTotalIssueQty(omSalesOrderDet.getTotalIssueQty().subtract(putawayQty));
            omSalesOrderDet.setIfAllIssued((byte)0);
            omSalesOrderDet.setModifiedUserId(sysUser.getUserId());
            omSalesOrderDet.setModifiedTime(new Date());
            num+=omSalesOrderDetMapper.updateByPrimaryKeySelective(omSalesOrderDet);

        }
        return num;
    }
}

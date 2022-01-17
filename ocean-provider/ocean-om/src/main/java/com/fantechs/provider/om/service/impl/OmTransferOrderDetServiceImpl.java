package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmTransferOrderDetMapper;
import com.fantechs.provider.om.service.OmTransferOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/15.
 */
@Service
public class OmTransferOrderDetServiceImpl extends BaseService<OmTransferOrderDet> implements OmTransferOrderDetService {

    @Resource
    private OmTransferOrderDetMapper omTransferOrderDetMapper;

    @Override
    public List<OmTransferOrderDetDto> findList(Map<String, Object> map) {
        return omTransferOrderDetMapper.findList(map);
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
        OmTransferOrderDet omTransferOrderDet = omTransferOrderDetMapper.selectByPrimaryKey(detId);
        if(StringUtils.isNotEmpty(omTransferOrderDet)){
            if(StringUtils.isEmpty(omTransferOrderDet.getTotalIssueQty())){
                omTransferOrderDet.setTotalIssueQty(new BigDecimal(0));
            }

            omTransferOrderDet.setTotalIssueQty(omTransferOrderDet.getTotalIssueQty().subtract(putawayQty));
            omTransferOrderDet.setIfAllIssued((byte)0);
            omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
            omTransferOrderDet.setModifiedTime(new Date());
            num+=omTransferOrderDetMapper.updateByPrimaryKeySelective(omTransferOrderDet);

        }
        return num;
    }

}

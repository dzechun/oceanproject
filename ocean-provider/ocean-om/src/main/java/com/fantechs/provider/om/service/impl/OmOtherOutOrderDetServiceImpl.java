package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmOtherOutOrderDetMapper;
import com.fantechs.provider.om.service.OmOtherOutOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */
@Service
public class OmOtherOutOrderDetServiceImpl extends BaseService<OmOtherOutOrderDet> implements OmOtherOutOrderDetService {

    @Resource
    private OmOtherOutOrderDetMapper omOtherOutOrderDetMapper;

    @Override
    public List<OmOtherOutOrderDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omOtherOutOrderDetMapper.findList(map);
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
        OmOtherOutOrderDet omOtherOutOrderDet = omOtherOutOrderDetMapper.selectByPrimaryKey(detId);
        if(StringUtils.isNotEmpty(omOtherOutOrderDet)){
            if(StringUtils.isEmpty(omOtherOutOrderDet.getTotalIssueQty())){
                omOtherOutOrderDet.setTotalIssueQty(new BigDecimal(0));
            }

            omOtherOutOrderDet.setTotalIssueQty(omOtherOutOrderDet.getTotalIssueQty().subtract(putawayQty));
            omOtherOutOrderDet.setIfAllIssued((byte)0);
            omOtherOutOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherOutOrderDet.setModifiedTime(new Date());
            num+=omOtherOutOrderDetMapper.updateByPrimaryKeySelective(omOtherOutOrderDet);

        }
        return num;
    }
}

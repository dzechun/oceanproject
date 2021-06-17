package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmPurchaseOrderDetMapper;
import com.fantechs.provider.om.service.OmPurchaseOrderDetService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@Service
public class OmPurchaseOrderDetServiceImpl extends BaseService<OmPurchaseOrderDet> implements OmPurchaseOrderDetService {

    @Resource
    private OmPurchaseOrderDetMapper omPurchaseOrderDetMapper;

    @Override
    public List<OmPurchaseOrderDetDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        return omPurchaseOrderDetMapper.findList(map);
    }

    @Override
    public OmPurchaseOrderDet addOrUpdate(OmPurchaseOrderDet omPurchaseOrderDet) {

        Example example = new Example(OmPurchaseOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseOrderId", omPurchaseOrderDet.getPurchaseOrderId());
        criteria.andEqualTo("projectCode", omPurchaseOrderDet.getProjectCode());
        criteria.andEqualTo("orgId", omPurchaseOrderDet.getOrgId());
        List<OmPurchaseOrderDet> omPurchaseOrderDets = omPurchaseOrderDetMapper.selectByExample(example);

        omPurchaseOrderDet.setModifiedTime(new Date());
        if (StringUtils.isNotEmpty(omPurchaseOrderDets)){
            omPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrderDets.get(0).getPurchaseOrderDetId());
            omPurchaseOrderDetMapper.updateByPrimaryKey(omPurchaseOrderDet);
        }else{
            omPurchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDetMapper.insertUseGeneratedKeys(omPurchaseOrderDet);
        }
        return omPurchaseOrderDet;
    }

}

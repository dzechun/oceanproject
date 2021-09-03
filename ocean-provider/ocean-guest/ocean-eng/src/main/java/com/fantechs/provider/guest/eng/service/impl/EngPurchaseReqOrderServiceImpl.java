package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.eng.EngPurchaseReqOrderDto;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.mapper.EngPurchaseReqOrderMapper;
import com.fantechs.provider.guest.eng.service.EngPurchaseReqOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/02.
 */
@Service
public class EngPurchaseReqOrderServiceImpl extends BaseService<EngPurchaseReqOrder> implements EngPurchaseReqOrderService {

    @Resource
    private EngPurchaseReqOrderMapper engPurchaseReqOrderMapper;

    @Override
    public List<EngPurchaseReqOrderDto> findList(Map<String, Object> map) {
        return engPurchaseReqOrderMapper.findList(map);
    }

    @Override
    public int saveByApi(EngPurchaseReqOrder engPurchaseReqOrder) {
        Example example = new Example(EngPurchaseReqOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseReqOrderCode",engPurchaseReqOrder.getPurchaseReqOrderCode());
        criteria.andEqualTo("dominantTermCode",engPurchaseReqOrder.getDominantTermCode());
        criteria.andEqualTo("materialCode",engPurchaseReqOrder.getMaterialCode());
        criteria.andEqualTo("orgId",engPurchaseReqOrder.getOrgId());

        //根据合同号和主项次号查找现有数据 存在则更新 不存在则新增
        EngPurchaseReqOrder ecqoExist=engPurchaseReqOrderMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(ecqoExist)){
            engPurchaseReqOrder.setPurchaseReqOrderId(ecqoExist.getPurchaseReqOrderId());
            engPurchaseReqOrder.setModifiedTime(new Date());
            return engPurchaseReqOrderMapper.updateByPrimaryKeySelective(engPurchaseReqOrder);
        }
        else {
            engPurchaseReqOrder.setCreateTime(new Date());
            engPurchaseReqOrder.setCreateUserId((long) 1);
            engPurchaseReqOrder.setModifiedUserId((long) 1);
            engPurchaseReqOrder.setModifiedTime(new Date());
            engPurchaseReqOrder.setIsDelete((byte) 1);
            return engPurchaseReqOrderMapper.insertSelective(engPurchaseReqOrder);
        }

    }
}

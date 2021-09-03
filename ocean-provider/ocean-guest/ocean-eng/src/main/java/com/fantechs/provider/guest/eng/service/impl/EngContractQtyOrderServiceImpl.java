package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderDto;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderMapper;
import com.fantechs.provider.guest.eng.service.EngContractQtyOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@Service
public class EngContractQtyOrderServiceImpl extends BaseService<EngContractQtyOrder> implements EngContractQtyOrderService {

    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;

    @Override
    public List<EngContractQtyOrderDto> findList(Map<String, Object> map) {
        return engContractQtyOrderMapper.findList(map);
    }

    @Override
    public int saveByApi(EngContractQtyOrder engContractQtyOrder) {
        Example example = new Example(EngContractQtyOrder.class);
        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("contractCode",engContractQtyOrder.getContractCode());
//        criteria.andEqualTo("dominantTermCode",engContractQtyOrder.getDominantTermCode());
//        criteria.andEqualTo("materialCode",engContractQtyOrder.getMaterialCode());
        criteria.andEqualTo("option1",engContractQtyOrder.getOption1());
        criteria.andEqualTo("orgId",engContractQtyOrder.getOrgId());

        //根据合同号和主项次号查找现有数据 存在则更新 不存在则新增
        EngContractQtyOrder ecqoExist=engContractQtyOrderMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(ecqoExist)){
            engContractQtyOrder.setContractQtyOrderId(ecqoExist.getContractQtyOrderId());
            engContractQtyOrder.setModifiedTime(new Date());
            return engContractQtyOrderMapper.updateByPrimaryKeySelective(engContractQtyOrder);

        }
        else {
            engContractQtyOrder.setCreateTime(new Date());
            engContractQtyOrder.setCreateUserId((long) 1);
            engContractQtyOrder.setModifiedUserId((long) 1);
            engContractQtyOrder.setModifiedTime(new Date());
            engContractQtyOrder.setIsDelete((byte) 1);
            return engContractQtyOrderMapper.insertSelective(engContractQtyOrder);
        }

    }
}

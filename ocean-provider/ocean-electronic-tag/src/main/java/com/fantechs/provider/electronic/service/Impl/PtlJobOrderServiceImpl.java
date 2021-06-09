package com.fantechs.provider.electronic.service.Impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.PtlJobOrderMapper;
import com.fantechs.provider.electronic.service.PtlJobOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/01.
 */
@Service
public class PtlJobOrderServiceImpl extends BaseService<PtlJobOrder> implements PtlJobOrderService {

    @Resource
    private PtlJobOrderMapper ptlJobOrderMapper;

    @Override
    public List<PtlJobOrderDto> findList(Map<String, Object> map) {
        return ptlJobOrderMapper.findList(map);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateByRelatedOrderCode(PtlJobOrder ptlJobOrder) throws Exception {

        Example example = new Example(PtlJobOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relatedOrderCode", ptlJobOrder.getRelatedOrderCode());

        return ptlJobOrderMapper.updateByExampleSelective(ptlJobOrder, example);
    }
}

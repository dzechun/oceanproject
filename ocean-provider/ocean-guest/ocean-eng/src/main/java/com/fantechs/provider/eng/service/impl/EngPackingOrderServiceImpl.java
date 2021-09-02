package com.fantechs.provider.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eng.mapper.EngHtPackingOrderMapper;
import com.fantechs.provider.eng.mapper.EngPackingOrderMapper;
import com.fantechs.provider.eng.mapper.EngPackingOrderSummaryDetMapper;
import com.fantechs.provider.eng.mapper.EngPackingOrderSummaryMapper;
import com.fantechs.provider.eng.service.EngPackingOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class EngPackingOrderServiceImpl extends BaseService<EngPackingOrder> implements EngPackingOrderService {

    @Resource
    private EngPackingOrderMapper engPackingOrderMapper;
    @Resource
    private EngHtPackingOrderMapper engHtPackingOrderMapper;
    @Resource
    private EngPackingOrderSummaryMapper engPackingOrderSummaryMapper;
    @Resource
    private EngPackingOrderSummaryDetMapper engPackingOrderSummaryDetMapper;

    @Override
    public List<EngPackingOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());
        return engPackingOrderMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(EngPackingOrder engPackingOrder) {
        SysUser user = getUser();
        engPackingOrder.setCreateTime(new Date());
        engPackingOrder.setCreateUserId(user.getUserId());
        engPackingOrder.setModifiedTime(new Date());
        engPackingOrder.setModifiedUserId(user.getUserId());
        engPackingOrder.setStatus((byte)1);
        engPackingOrder.setOrgId(user.getOrganizationId());
        int i = engPackingOrderMapper.insertUseGeneratedKeys(engPackingOrder);

        EngHtPackingOrder engHtPackingOrder =new EngHtPackingOrder();
        BeanUtils.copyProperties(engPackingOrder, engHtPackingOrder);
        engHtPackingOrderMapper.insertSelective(engHtPackingOrder);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EngPackingOrder engPackingOrder) {
        SysUser user = getUser();
        engPackingOrder.setModifiedUserId(user.getUserId());
        engPackingOrder.setModifiedTime(new Date());

        int i = engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

        EngHtPackingOrder engHtPackingOrder =new EngHtPackingOrder();
        BeanUtils.copyProperties(engPackingOrder, engHtPackingOrder);
        engHtPackingOrderMapper.insertSelective(engHtPackingOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = getUser();

        List<EngHtPackingOrder> htList = new ArrayList<>();
        String[] split = ids.split(",");
        for (String id : split){
            EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(engPackingOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //新增履历信息
            EngHtPackingOrder engHtPackingOrder = new EngHtPackingOrder();
            BeanUtils.copyProperties(engPackingOrder, engHtPackingOrder);
            htList.add(engHtPackingOrder);

            Example example = new Example(EngPackingOrderSummary.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packingOrderId",id);
            List<EngPackingOrderSummary> engPackingOrderSummaryList = engPackingOrderSummaryMapper.selectByExample(example);
            for (EngPackingOrderSummary engPackingOrderSummary : engPackingOrderSummaryList){
                Example example1 = new Example(EngPackingOrderSummaryDet.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("packingOrderSummaryId",engPackingOrderSummary.getPackingOrderSummaryId());
                engPackingOrderSummaryDetMapper.deleteByExample(example1);
            }
            engPackingOrderSummaryMapper.deleteByExample(example);
        }

        engHtPackingOrderMapper.insertList(htList);

        return engPackingOrderMapper.deleteByIds(ids);
    }

    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }
}

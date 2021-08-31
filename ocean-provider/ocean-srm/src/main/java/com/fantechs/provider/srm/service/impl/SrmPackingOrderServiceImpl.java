package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmHtPackingOrderMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderSummaryDetMapper;
import com.fantechs.provider.srm.mapper.SrmPackingOrderSummaryMapper;
import com.fantechs.provider.srm.service.SrmPackingOrderService;
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
public class SrmPackingOrderServiceImpl extends BaseService<SrmPackingOrder> implements SrmPackingOrderService {

    @Resource
    private SrmPackingOrderMapper srmPackingOrderMapper;
    @Resource
    private SrmHtPackingOrderMapper srmHtPackingOrderMapper;
    @Resource
    private SrmPackingOrderSummaryMapper srmPackingOrderSummaryMapper;
    @Resource
    private SrmPackingOrderSummaryDetMapper srmPackingOrderSummaryDetMapper;

    @Override
    public List<SrmPackingOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());
        return srmPackingOrderMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SrmPackingOrder srmPackingOrder) {
        SysUser user = getUser();
        srmPackingOrder.setCreateTime(new Date());
        srmPackingOrder.setCreateUserId(user.getUserId());
        srmPackingOrder.setModifiedTime(new Date());
        srmPackingOrder.setModifiedUserId(user.getUserId());
        srmPackingOrder.setStatus((byte)1);
        srmPackingOrder.setOrgId(user.getOrganizationId());
        int i = srmPackingOrderMapper.insertUseGeneratedKeys(srmPackingOrder);

        SrmHtPackingOrder srmHtPackingOrder =new SrmHtPackingOrder();
        BeanUtils.copyProperties(srmPackingOrder, srmHtPackingOrder);
        srmHtPackingOrderMapper.insertSelective(srmHtPackingOrder);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SrmPackingOrder srmPackingOrder) {
        SysUser user = getUser();
        srmPackingOrder.setModifiedUserId(user.getUserId());
        srmPackingOrder.setModifiedTime(new Date());

        int i = srmPackingOrderMapper.updateByPrimaryKeySelective(srmPackingOrder);

        SrmHtPackingOrder srmHtPackingOrder =new SrmHtPackingOrder();
        BeanUtils.copyProperties(srmPackingOrder, srmHtPackingOrder);
        srmHtPackingOrderMapper.insertSelective(srmHtPackingOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = getUser();

        List<SrmHtPackingOrder> htList = new ArrayList<>();
        String[] split = ids.split(",");
        for (String id : split){
            SrmPackingOrder srmPackingOrder = srmPackingOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(srmPackingOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //新增履历信息
            SrmHtPackingOrder srmHtPackingOrder = new SrmHtPackingOrder();
            BeanUtils.copyProperties(srmPackingOrder, srmHtPackingOrder);
            htList.add(srmHtPackingOrder);

            Example example = new Example(SrmPackingOrderSummary.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packingOrderId",id);
            List<SrmPackingOrderSummary> srmPackingOrderSummaryList = srmPackingOrderSummaryMapper.selectByExample(example);
            for (SrmPackingOrderSummary srmPackingOrderSummary : srmPackingOrderSummaryList){
                Example example1 = new Example(SrmPackingOrderSummaryDet.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("packingOrderSummaryId",srmPackingOrderSummary.getPackingOrderSummaryId());
                srmPackingOrderSummaryDetMapper.deleteByExample(example1);
            }
            srmPackingOrderSummaryMapper.deleteByExample(example);
        }

        srmHtPackingOrderMapper.insertList(htList);

        return srmPackingOrderMapper.deleteByIds(ids);
    }

    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }
}

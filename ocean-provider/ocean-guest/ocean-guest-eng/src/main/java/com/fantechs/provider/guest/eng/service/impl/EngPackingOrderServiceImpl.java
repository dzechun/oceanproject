package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngHtPackingOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryDetMapper;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderSummaryMapper;
import com.fantechs.provider.guest.eng.service.EngDataExportEngPackingOrderService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderService;
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
    @Resource
    private EngDataExportEngPackingOrderService engDataExportEngPackingOrderService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<EngPackingOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return engPackingOrderMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(EngPackingOrder engPackingOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        engPackingOrder.setCreateTime(new Date());
        engPackingOrder.setCreateUserId(user.getUserId());
        engPackingOrder.setModifiedTime(new Date());
        engPackingOrder.setModifiedUserId(user.getUserId());
        engPackingOrder.setStatus((byte)1);
        engPackingOrder.setAuditStatus((byte)1);
        engPackingOrder.setOrderStatus((byte)1);
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
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
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
    public int submit(EngPackingOrder engPackingOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        check(engPackingOrder);
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
    public int censor(EngPackingOrder engPackingOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        check(engPackingOrder);
        engPackingOrder.setModifiedUserId(user.getUserId());
        engPackingOrder.setModifiedTime(new Date());
        int i = engPackingOrderMapper.updateByPrimaryKeySelective(engPackingOrder);

        EngHtPackingOrder engHtPackingOrder =new EngHtPackingOrder();
        BeanUtils.copyProperties(engPackingOrder, engHtPackingOrder);
        engHtPackingOrderMapper.insertSelective(engHtPackingOrder);

        //审核通过回传
        if(engPackingOrder.getAuditStatus()==(byte)3 && i>0) {
            String result = engDataExportEngPackingOrderService.writePackingLists(engPackingOrder);
        }

        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

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

    public void check(EngPackingOrder engPackingOrder){
        Example example = new Example(EngPackingOrderSummary.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packingOrderId",engPackingOrder.getPackingOrderId());
        List<EngPackingOrderSummary> engPackingOrderSummaryList = engPackingOrderSummaryMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(engPackingOrderSummaryList)){
            for(EngPackingOrderSummary engPackingOrderSummary : engPackingOrderSummaryList){
                Example detexample = new Example(EngPackingOrderSummaryDet.class);
                Example.Criteria detcriteria = detexample.createCriteria();
                detcriteria.andEqualTo("packingOrderSummaryId",engPackingOrderSummary.getPackingOrderSummaryId());
                List<EngPackingOrderSummaryDet> engPackingOrderSummaryDets = engPackingOrderSummaryDetMapper.selectByExample(detexample);
                if(StringUtils.isEmpty(engPackingOrderSummaryDets))  throw new BizErrorException("提交失败，装箱汇总明细信息不能为空");
            }
        }else{
            throw new BizErrorException("提交失败，未查询到装箱汇总信息");
        }
    }


    public List<BaseSupplierReUser> getSupplier(Long userId){
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(userId);
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        return list.getData();
    }
}

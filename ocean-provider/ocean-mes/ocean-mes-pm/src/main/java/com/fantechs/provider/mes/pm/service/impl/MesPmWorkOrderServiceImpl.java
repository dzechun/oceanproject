package com.fantechs.provider.mes.pm.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class MesPmWorkOrderServiceImpl extends BaseService<MesPmWorkOrder> implements MesPmWorkOrderService {

    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private MesPmHtWorkOrderMapper smtHtWorkOrderMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmWorkOrder mesPmWorkOrder) {
        SysUser currentUser = currentUser();


        if(StringUtils.isEmpty(mesPmWorkOrder.getWorkOrderCode())){
            mesPmWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));
        }else{
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());

            List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(mesPmWorkOrders)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }

        mesPmWorkOrder.setWorkOrderStatus((byte) 1);
        mesPmWorkOrder.setCreateUserId(currentUser.getUserId());
        mesPmWorkOrder.setCreateTime(new Date());
        if(mesPmWorkOrderMapper.insertSelective(mesPmWorkOrder)<=0){
            return 0;
        }

        //新增工单历史信息
        recordHistory(mesPmWorkOrder,"新增");

        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateInventoryQty(MesPmWorkOrder mesPmWorkOrder){
       return mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MesPmWorkOrder mesPmWorkOrder) {
        int i = 0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        MesPmWorkOrder order = mesPmWorkOrderMapper.selectByPrimaryKey(mesPmWorkOrder.getWorkOrderId());

        //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
        Integer workOrderStatus = order.getWorkOrderStatus().intValue();
        if (workOrderStatus != 4) {
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());

            MesPmWorkOrder workOrder = mesPmWorkOrderMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(workOrder) && !workOrder.getWorkOrderId().equals(mesPmWorkOrder.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }


            mesPmWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmWorkOrder.setModifiedTime(new Date());
            mesPmWorkOrder.setCreateTime(null);
            i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);


            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
        } else {
            throw new BizErrorException("生产完成的工单不允许修改");
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<MesPmHtWorkOrder> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] workOrderIds = ids.split(",");
        for (String workOrderId : workOrderIds) {
            MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderMapper.selectByPrimaryKey(workOrderId);
            if(StringUtils.isNotEmpty(mesPmWorkOrder.getScheduledQty()) && mesPmWorkOrder.getScheduledQty().doubleValue()>0){
                throw new BizErrorException("工单已排产，不允许删除:"+ mesPmWorkOrder.getWorkOrderCode());
            }
            if (StringUtils.isEmpty(mesPmWorkOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            list.add(mesPmHtWorkOrder);
        }
        smtHtWorkOrderMapper.insertList(list);

        return mesPmWorkOrderMapper.deleteByIds(ids);
    }


    @Override
    public List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        List<MesPmWorkOrderDto> list = mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
        return list;
    }

    @Override
    public List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        return mesPmWorkOrderMapper.pdaFindList(searchMesPmWorkOrder);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    /**
     * 记录操作历史
     * @param mesPmWorkOrder
     * @param operation
     */
    private void recordHistory(MesPmWorkOrder mesPmWorkOrder, String operation){
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        mesPmHtWorkOrder.setOption1(operation);
        if (StringUtils.isEmpty(mesPmWorkOrder)){
            return;
        }
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
    }

    @Override
    public MesPmWorkOrder updateById(MesPmWorkOrder mesPmWorkOrder){
        Example example = new Example(MesPmWorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());
        MesPmWorkOrder mesPmWorkOrderOld = mesPmWorkOrderMapper.selectOneByExample(example);
        mesPmWorkOrder.setModifiedTime(new Date());
        if(StringUtils.isEmpty(mesPmWorkOrderOld)){
            try {
                if(StringUtils.isEmpty(mesPmWorkOrder.getCreateTime())) mesPmWorkOrder.setCreateTime(new Date());
                if(mesPmWorkOrderMapper.insertSelective(mesPmWorkOrder)<=0)
                    throw new BizErrorException("保存失败");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BizErrorException("保存失败");
            }
        }else{
            int n = 0;
            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrderOld, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
            mesPmWorkOrder.setWorkOrderId(mesPmWorkOrderOld.getWorkOrderId());
            if( mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder)<=0)
                throw new BizErrorException("更新失败");
        }
        return mesPmWorkOrder;
    }

    @Override
    public List<MesPmWorkOrder> getWorkOrderList(List<String> workOrderIds) {
        return mesPmWorkOrderMapper.getWorkOrderList(workOrderIds);
    }

    @Override
    public int batchUpdate(List<MesPmWorkOrder> mesPmWorkOrders) {
        return mesPmWorkOrderMapper.batchUpdate(mesPmWorkOrders);
    }

}

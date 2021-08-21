package com.fantechs.provider.mes.pm.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderBomMapper;
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
    @Resource
    private MesPmWorkOrderBomMapper mesPmWorkOrderBomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmWorkOrderDto mesPmWorkOrderDto) {
        SysUser currentUser = currentUser();


        if(StringUtils.isEmpty(mesPmWorkOrderDto.getWorkOrderCode())){
            mesPmWorkOrderDto.setWorkOrderCode(CodeUtils.getId("WORK"));
        }else{
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrderDto.getWorkOrderCode());

            List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(mesPmWorkOrders)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }

        mesPmWorkOrderDto.setWorkOrderStatus((byte) 1);
        mesPmWorkOrderDto.setCreateUserId(currentUser.getUserId());
        mesPmWorkOrderDto.setCreateTime(new Date());
        mesPmWorkOrderDto.setModifiedUserId(currentUser.getUserId());
        mesPmWorkOrderDto.setModifiedTime(new Date());
        mesPmWorkOrderDto.setOrgId(currentUser.getOrganizationId());
        mesPmWorkOrderDto.setIsDelete((byte)1);
        int i = mesPmWorkOrderMapper.insertUseGeneratedKeys(mesPmWorkOrderDto);

        //工单履历表
        recordHistory(mesPmWorkOrderDto);
        //保存bom表
        savebom(mesPmWorkOrderDto,currentUser);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateInventoryQty(MesPmWorkOrder mesPmWorkOrder){
       return mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(MesPmWorkOrderDto mesPmWorkOrderDto) {
        int i = 0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        MesPmWorkOrder order = mesPmWorkOrderMapper.selectByPrimaryKey(mesPmWorkOrderDto.getWorkOrderId());

        //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
        Integer workOrderStatus = order.getWorkOrderStatus().intValue();
        if (workOrderStatus != 4) {
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrderDto.getWorkOrderCode());

            MesPmWorkOrder workOrder = mesPmWorkOrderMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(workOrder) && !workOrder.getWorkOrderId().equals(mesPmWorkOrderDto.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            mesPmWorkOrderDto.setModifiedUserId(currentUser.getUserId());
            mesPmWorkOrderDto.setModifiedTime(new Date());
            i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrderDto);

            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrderDto, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
        } else {
            throw new BizErrorException("生产完成的工单不允许修改");
        }

        Example detExample = new Example(MesPmWorkOrderBom.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("workOrderId", mesPmWorkOrderDto.getWorkOrderId());
        mesPmWorkOrderBomMapper.deleteByExample(detExample);
        detExample.clear();
        //保存bom表
        savebom(mesPmWorkOrderDto,currentUser);

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
        if(StringUtils.isNotEmpty(list)) smtHtWorkOrderMapper.insertList(list);
        return mesPmWorkOrderMapper.deleteByIds(ids);
    }


    @Override
    public List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        searchMesPmWorkOrder.setOrgId(user.getOrganizationId());
        return mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
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
     */
    private void recordHistory(MesPmWorkOrder mesPmWorkOrder){
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
    }

    @Override
    public MesPmWorkOrder saveByApi(MesPmWorkOrder mesPmWorkOrder){
        Example example = new Example(MesPmWorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());
        List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
        mesPmWorkOrderMapper.deleteByExample(example);
        example.clear();

        //删除该工单下的所有bom表单
        if(StringUtils.isNotEmpty(mesPmWorkOrders)) {
            Example bomExample = new Example(MesPmWorkOrderBom.class);
            Example.Criteria bomCriteria = bomExample.createCriteria();
            bomCriteria.andEqualTo("workOrderId", mesPmWorkOrders.get(0).getWorkOrderId());
            mesPmWorkOrderBomMapper.deleteByExample(bomExample);
            bomExample.clear();
        }
        mesPmWorkOrder.setModifiedTime(new Date());
        mesPmWorkOrder.setCreateTime(new Date());
        mesPmWorkOrderMapper.insertUseGeneratedKeys(mesPmWorkOrder);
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

    public  void savebom(MesPmWorkOrderDto mesPmWorkOrderDto , SysUser user){
        List<MesPmWorkOrderBom> boms = new ArrayList<>();
        if(StringUtils.isNotEmpty(mesPmWorkOrderDto.getMesPmWorkOrderBomDtos())) {
            for (MesPmWorkOrderBomDto mesPmWorkOrderBomDto :  mesPmWorkOrderDto.getMesPmWorkOrderBomDtos()) {
                mesPmWorkOrderBomDto.setCreateUserId(user.getUserId());
                mesPmWorkOrderBomDto.setCreateTime(new Date());
                mesPmWorkOrderBomDto.setModifiedUserId(user.getUserId());
                mesPmWorkOrderBomDto.setModifiedTime(new Date());
                mesPmWorkOrderBomDto.setOrgId(user.getOrganizationId());
                mesPmWorkOrderBomDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                boms.add(mesPmWorkOrderBomDto);
            }
        }
        if(StringUtils.isNotEmpty(boms))  mesPmWorkOrderBomMapper.insertList(boms);
    }

}

package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.entity.eng.EngUserFollowContractQtyOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.mapper.EngUserFollowContractQtyOrderMapper;
import com.fantechs.provider.guest.eng.service.EngContractQtyOrderAndPurOrderService;
import com.fantechs.provider.guest.eng.service.EngUserFollowContractQtyOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/11/03.
 */
@Service
public class EngUserFollowContractQtyOrderServiceImpl extends BaseService<EngUserFollowContractQtyOrder> implements EngUserFollowContractQtyOrderService {

    @Resource
    private EngUserFollowContractQtyOrderMapper engUserFollowContractQtyOrderMapper;
    @Resource
    private EngContractQtyOrderAndPurOrderService engContractQtyOrderAndPurOrderService;

    @Override
    public List<EngUserFollowContractQtyOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        map.put("userId",user.getUserId());
        return engUserFollowContractQtyOrderMapper.findList(map);
    }

    @Override
    public List<EngContractQtyOrderAndPurOrderDto> findFollowList(Map<String, Object> map) {
        List<EngContractQtyOrderAndPurOrderDto> list= new ArrayList<>();

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(EngUserFollowContractQtyOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId",user.getOrganizationId())
                .andEqualTo("userId",user.getUserId());
        List<EngUserFollowContractQtyOrder> engUserFollowContractQtyOrders = engUserFollowContractQtyOrderMapper.selectByExample(example);
        if(StringUtils.isEmpty(engUserFollowContractQtyOrders)){
            return list;
        }

        List<Long> contractQtyOrderIds = engUserFollowContractQtyOrders.stream().map(EngUserFollowContractQtyOrder::getContractQtyOrderId).collect(Collectors.toList());
        map.put("contractQtyOrderIds",contractQtyOrderIds);
        return engContractQtyOrderAndPurOrderService.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EngUserFollowContractQtyOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EngUserFollowContractQtyOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("contractQtyOrderId",record.getContractQtyOrderId())
                .andEqualTo("userId",user.getUserId());
        EngUserFollowContractQtyOrder engUserFollowContractQtyOrder = engUserFollowContractQtyOrderMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(engUserFollowContractQtyOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已关注该材料");
        }

        record.setUserId(user.getUserId());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());

        return engUserFollowContractQtyOrderMapper.insertSelective(record);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int follow(String contractQtyOrderIds) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] split = contractQtyOrderIds.split(",");
        List<Long> ids = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        Example example = new Example(EngUserFollowContractQtyOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("contractQtyOrderId",ids)
                .andEqualTo("userId",user.getUserId());
        List<EngUserFollowContractQtyOrder> engUserFollowContractQtyOrders = engUserFollowContractQtyOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(engUserFollowContractQtyOrders)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"已关注该材料");
        }

        List<EngUserFollowContractQtyOrder> list = new LinkedList<>();
        for (Long id : ids){
            EngUserFollowContractQtyOrder engUserFollowContractQtyOrder = new EngUserFollowContractQtyOrder();
            engUserFollowContractQtyOrder.setContractQtyOrderId(id);
            engUserFollowContractQtyOrder.setUserId(user.getUserId());
            engUserFollowContractQtyOrder.setCreateUserId(user.getUserId());
            engUserFollowContractQtyOrder.setCreateTime(new Date());
            engUserFollowContractQtyOrder.setModifiedUserId(user.getUserId());
            engUserFollowContractQtyOrder.setModifiedTime(new Date());
            engUserFollowContractQtyOrder.setOrgId(user.getOrganizationId());
            list.add(engUserFollowContractQtyOrder);
        }

        return engUserFollowContractQtyOrderMapper.insertList(list);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelFollow(String contractQtyOrderIds) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] split = contractQtyOrderIds.split(",");
        List<Long> ids = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        Example example = new Example(EngUserFollowContractQtyOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("contractQtyOrderId",ids)
                .andEqualTo("userId",user.getUserId());
        return engUserFollowContractQtyOrderMapper.deleteByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EngUserFollowContractQtyOrder> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

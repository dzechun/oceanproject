package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrder;
import com.fantechs.common.base.response.ControllerUtil;
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
        List<BaseSupplierReUser> suppliers = getSupplier(user.getUserId());
        if(StringUtils.isNotEmpty(suppliers))
            map.put("supplierId", suppliers.get(0).getSupplierId());
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

        if(StringUtils.isNotEmpty(engPackingOrder.getLeaveFactoryTime()) && StringUtils.isNotEmpty(engPackingOrder.getArrivalTime())
        && engPackingOrder.getLeaveFactoryTime().after(engPackingOrder.getArrivalTime())){
            throw new BizErrorException("出厂时间不能晚于出场时间");
        }
        if(StringUtils.isNotEmpty(engPackingOrder.getLeaveFactoryTime()) && StringUtils.isNotEmpty(engPackingOrder.getLeavePortTime())
                && engPackingOrder.getLeaveFactoryTime().after(engPackingOrder.getLeavePortTime())){
            throw new BizErrorException("出厂时间不能晚于离岗时间");
        }
        if(StringUtils.isNotEmpty(engPackingOrder.getLeavePortTime()) && StringUtils.isNotEmpty(engPackingOrder.getArrivalPortTime())
                && engPackingOrder.getLeavePortTime().after(engPackingOrder.getArrivalPortTime())){
            throw new BizErrorException("离岗时间不能晚于到岗时间");
        }
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
        engPackingOrder.setOrderStatus((byte)1);

        //审核通过给包装清单货品明细赋值默认收货库位及上架库位
        //获取收货库存信息
        Long warehouseId = engPackingOrderMapper.findWarehouse(ControllerUtil.dynamicCondition("orgId",user.getOrganizationId()));
        if(StringUtils.isEmpty(warehouseId)){
            throw new BizErrorException("获取仓库信息失败");
        }
        //获取库位信息
        Long storageId = engPackingOrderMapper.findStorage(ControllerUtil.dynamicCondition("orgId",user.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(storageId)){
            throw new BizErrorException("获取库位信息失败");
        }
        //获取默认上架库位
        Long putStorageId = engPackingOrderMapper.findPutStorage(ControllerUtil.dynamicCondition("orgId",user.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(storageId)){
            throw new BizErrorException("获取库位信息失败");
        }
        //获取库存状态信息
        Long inventoryStatus = engPackingOrderMapper.findInventoryStatus(ControllerUtil.dynamicCondition("orgId",user.getOrganizationId(),"warehouseId",warehouseId));
        if(StringUtils.isEmpty(inventoryStatus)){
            throw new BizErrorException("获取库位信息失败");
        }
        List<EngPackingOrderSummaryDto> list = engPackingOrderSummaryMapper.findList(ControllerUtil.dynamicCondition("packingOrderId",engPackingOrder.getPackingOrderId()));
        for (EngPackingOrderSummaryDto engPackingOrderSummaryDto : list) {
            //查询包箱货品明细
            List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos = engPackingOrderSummaryDetMapper.findList(ControllerUtil.dynamicCondition("packingOrderSummaryId",engPackingOrderSummaryDto.getPackingOrderSummaryId()));
            for (EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto : engPackingOrderSummaryDetDtos) {
                engPackingOrderSummaryDetDto.setSummaryDetStatus((byte)1);
                engPackingOrderSummaryDetDto.setReceivingStorageId(storageId);
                engPackingOrderSummaryDetDto.setPutawayStorageId(putStorageId);
                engPackingOrderSummaryDetDto.setModifiedTime(new Date());
                engPackingOrderSummaryDetDto.setModifiedUserId(user.getUserId());
                engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDetDto);
            }
            engPackingOrderSummaryDto.setSummaryStatus((byte)1);
            engPackingOrderSummaryDto.setModifiedTime(new Date());
            engPackingOrderSummaryDto.setModifiedUserId(user.getUserId());
            engPackingOrderSummaryMapper.updateByPrimaryKeySelective(engPackingOrderSummaryDto);
        }

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

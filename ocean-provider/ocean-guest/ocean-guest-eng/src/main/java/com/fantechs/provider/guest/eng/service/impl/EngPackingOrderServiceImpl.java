package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
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
import com.fantechs.provider.guest.eng.mapper.*;
import com.fantechs.provider.guest.eng.service.EngDataExportEngPackingOrderService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;

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
        List<BaseWarehouse> warehouseList = baseFeignApi.findList(new SearchBaseWarehouse()).getData();
        if(StringUtils.isEmpty(warehouseList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取仓库信息失败");
        }
        Long warehouseId = warehouseList.get(0).getWarehouseId();
        //获取库位信息
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setWarehouseId(warehouseId);
        searchBaseStorage.setStorageType((byte)2);
        List<BaseStorage> baseStorageList = baseFeignApi.findList(searchBaseStorage).getData();
        if(StringUtils.isEmpty(baseStorageList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取库位信息失败");
        }
        Long storageId = baseStorageList.get(0).getStorageId();

        //获取默认上架库位
        searchBaseStorage.setStorageType((byte)1);
        searchBaseStorage.setStorageCode("default");
        searchBaseStorage.setCodeQueryMark((byte)1);
        baseStorageList = baseFeignApi.findList(searchBaseStorage).getData();
        if(StringUtils.isEmpty(baseStorageList)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取预上架库位信息失败");
        }
        Long putStorageId = baseStorageList.get(0).getStorageId();
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

                //根据装箱清单数量回写采购量单
                //校验合同量单
                Example qtyExample = new Example(EngContractQtyOrder.class);
                Example.Criteria qtyCriteria = qtyExample.createCriteria();
                qtyCriteria.andEqualTo("contractCode",engPackingOrderSummaryDto.getContractCode());
                qtyCriteria.andEqualTo("dominantTermCode",engPackingOrderSummaryDetDto.getDominantTermCode());
                qtyCriteria.andEqualTo("deviceCode",engPackingOrderSummaryDetDto.getDeviceCode());
                qtyCriteria.andEqualTo("materialCode",engPackingOrderSummaryDetDto.getMaterialCode());
                qtyCriteria.andEqualTo("locationNum",engPackingOrderSummaryDetDto.getLocationNum());
                List<EngContractQtyOrder> engContractQtyOrders = engContractQtyOrderMapper.selectByExample(qtyExample);
                if(StringUtils.isNotEmpty(engContractQtyOrders)){
                   EngContractQtyOrder engContractQtyOrder = engContractQtyOrders.get(0);
                   BigDecimal iss = engContractQtyOrder.getIssuedQty().add(engPackingOrderSummaryDetDto.getQty());
                    engContractQtyOrder.setIssuedQty(iss);
                   if(new BigDecimal(engContractQtyOrder.getPurQty()).compareTo(iss)<=0){
                       //engContractQtyOrder.setNotIssueQty(new BigDecimal("0"));
                       engContractQtyOrder.setNotIssueQty("0");
                   }else{
                       //engContractQtyOrder.setNotIssueQty(engContractQtyOrder.getPurQty().subtract(iss));
                       engContractQtyOrder.setNotIssueQty(new BigDecimal(engContractQtyOrder.getPurQty()).subtract(iss).toString());
                   }
                    engContractQtyOrderMapper.updateByPrimaryKey(engContractQtyOrder);
                }
            }
            engPackingOrderSummaryDto.setPutawayStorageId(putStorageId);
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
            Long ifCan=engPackingOrderSummaryDetMapper.findExistCount(engPackingOrder.getPackingOrderId());
            if(ifCan>0) {
                String result = engDataExportEngPackingOrderService.writePackingLists(engPackingOrder);
            }
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
                if(StringUtils.isEmpty(engPackingOrderSummaryDets))  throw new BizErrorException("提交失败，装箱汇总"+engPackingOrderSummary.getCartonCode()+"的明细信息不能为空");
            }
        }else{
            throw new BizErrorException("提交失败，未查询到"+engPackingOrder.getPackingOrderCode()+"相关的装箱汇总信息");
        }
    }

    public List<BaseSupplierReUser> getSupplier(Long userId){
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(userId);
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        return list.getData();
    }
}

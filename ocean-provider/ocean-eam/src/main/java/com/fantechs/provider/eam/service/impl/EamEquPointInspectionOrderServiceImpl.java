package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquPointInspectionProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaintainProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderDetService;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderService;
import com.fantechs.provider.eam.service.EamEquipmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquPointInspectionOrderServiceImpl extends BaseService<EamEquPointInspectionOrder> implements EamEquPointInspectionOrderService {

    @Resource
    private EamEquPointInspectionOrderMapper eamEquPointInspectionOrderMapper;

    @Resource
    private EamHtEquPointInspectionOrderMapper eamHtEquPointInspectionOrderMapper;

    @Resource
    private EamEquipmentService eamEquipmentService;

    @Resource
    private EamEquPointInspectionOrderDetService eamEquPointInspectionOrderDetService;

    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    @Resource
    private EamEquPointInspectionProjectMapper eamEquPointInspectionProjectMapper;

    @Override
    public List<EamEquPointInspectionOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquPointInspectionOrderMapper.findList(map);
    }

    @Override
    public List<EamEquPointInspectionOrderDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquPointInspectionOrderMapper.findList(map);
    }

    @Override
    public EamEquInspectionOrderDto findListForOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        List<EamEquInspectionOrderDto> list = eamEquipmentService.findListForInspectionOrder(map);
        if (list.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此设备条码没有关联设备或者设备类别");
        }
        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EamEquPointInspectionOrderDto pdaCreateOrder(String equipmentBarcode) {
        //查设备条码信息
        Example example = new Example(EamEquipmentBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBarcode",equipmentBarcode);
        List<EamEquipmentBarcode> eamEquipmentBarcodes = eamEquipmentBarcodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(eamEquipmentBarcodes)){
            throw new BizErrorException("查不到此设备条码");
        }
        EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodes.get(0);

        EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(eamEquipmentBarcode.getEquipmentId());

        //查设备对应的点检项目
        SearchEamEquPointInspectionProject searchEamEquPointInspectionProject = new SearchEamEquPointInspectionProject();
        searchEamEquPointInspectionProject.setEquipmentCategoryId(eamEquipment.getEquipmentCategoryId());
        List<EamEquPointInspectionProjectDto> list = eamEquPointInspectionProjectMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProject));
        if(StringUtils.isEmpty(list)){
            throw new BizErrorException("查不到该设备所属类别的点检项目");
        }
        EamEquPointInspectionProjectDto eamEquPointInspectionProjectDto = list.get(0);


        SearchEamEquPointInspectionOrder searchEamEquPointInspectionOrder = new SearchEamEquPointInspectionOrder();
        searchEamEquPointInspectionOrder.setEquipmentBarcodeId(eamEquipmentBarcode.getEquipmentBarcodeId());
        searchEamEquPointInspectionOrder.setOrderStatus((byte)1);
        List<EamEquPointInspectionOrderDto> orderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrder));
        if(StringUtils.isNotEmpty(orderDtos)){
            throw new BizErrorException("已存在该设备待点检状态的单据");
        }


        //保存点检单信息
        EamEquPointInspectionOrder eamEquPointInspectionOrder = new EamEquPointInspectionOrder();
        List<EamEquPointInspectionOrderDetDto> eamEquPointInspectionOrderDets = new ArrayList<>();

        eamEquPointInspectionOrder.setEquipmentId(eamEquipmentBarcode.getEquipmentId());
        eamEquPointInspectionOrder.setEquipmentBarcodeId(eamEquipmentBarcode.getEquipmentBarcodeId());
        eamEquPointInspectionOrder.setEquPointInspectionProjectId(eamEquPointInspectionProjectDto.getEquPointInspectionProjectId());
        List<EamEquPointInspectionProjectItem> items = eamEquPointInspectionProjectDto.getItems();
        for (EamEquPointInspectionProjectItem eamEquPointInspectionProjectItem : items){
            EamEquPointInspectionOrderDetDto eamEquPointInspectionOrderDetDto = new EamEquPointInspectionOrderDetDto();
            eamEquPointInspectionOrderDetDto.setEquPointInspectionProjectItemId(eamEquPointInspectionProjectItem.getEquPointInspectionProjectItemId());
            eamEquPointInspectionOrderDets.add(eamEquPointInspectionOrderDetDto);
        }
        eamEquPointInspectionOrder.setOrderDets(eamEquPointInspectionOrderDets);

        this.save(eamEquPointInspectionOrder);


        SearchEamEquPointInspectionOrder searchEamEquPointInspectionOrder1 = new SearchEamEquPointInspectionOrder();
        searchEamEquPointInspectionOrder1.setEquPointInspectionOrderId(eamEquPointInspectionOrder.getEquPointInspectionOrderId());
        List<EamEquPointInspectionOrderDto> equPointInspectionOrderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrder1));
        return equPointInspectionOrderDtos.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pdaSubmit(EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        eamEquPointInspectionOrder.setOrderStatus((byte)2);
        int i = this.update(eamEquPointInspectionOrder);

        //修改该设备点检信息
        EamEquipmentBarcode eamEquipmentBarcode = new EamEquipmentBarcode();
        eamEquipmentBarcode.setEquipmentBarcodeId(eamEquPointInspectionOrder.getEquipmentBarcodeId());
        eamEquipmentBarcode.setEquipmentStatus((byte)5);
        eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        SysUser user = getUser();

        //修改设备状态为点检中
        EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodeMapper.selectByPrimaryKey(eamEquPointInspectionOrder.getEquipmentBarcodeId());
        if(eamEquipmentBarcode.getEquipmentStatus() == 6){
            throw new BizErrorException("点检中的设备不能新建点检单");
        }
        eamEquipmentBarcode.setEquipmentStatus((byte)6);
        eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);

        // 新增点检单
        eamEquPointInspectionOrder.setEquPointInspectionOrderCode(CodeUtils.getId("SBDJ-"));
        eamEquPointInspectionOrder.setCreateUserId(user.getUserId());
        eamEquPointInspectionOrder.setCreateTime(new Date());
        eamEquPointInspectionOrder.setModifiedUserId(user.getUserId());
        eamEquPointInspectionOrder.setModifiedTime(new Date());
        eamEquPointInspectionOrder.setOrgId(user.getOrganizationId());
        eamEquPointInspectionOrder.setStatus(StringUtils.isEmpty(eamEquPointInspectionOrder.getStatus())?1: eamEquPointInspectionOrder.getStatus());
        eamEquPointInspectionOrder.setOrderStatus((byte)1);
        eamEquPointInspectionOrderMapper.insertUseGeneratedKeys(eamEquPointInspectionOrder);

        // 新增点检单履历
        EamHtEquPointInspectionOrder eamHtEquPointInspectionOrder = new EamHtEquPointInspectionOrder();
        BeanUtil.copyProperties(eamEquPointInspectionOrder, eamHtEquPointInspectionOrder);
        int i = eamHtEquPointInspectionOrderMapper.insert(eamHtEquPointInspectionOrder);

        if(!eamEquPointInspectionOrder.getOrderDets().isEmpty()){
            List<EamEquPointInspectionOrderDet> inspectionOrderDets = eamEquPointInspectionOrder.getOrderDets()
                    .stream()
                    .map(item -> {
                        item.setEquPointInspectionOrderId(eamEquPointInspectionOrder.getEquPointInspectionOrderId());
                        item.setCreateUserId(user.getUserId());
                        item.setCreateTime(new Date());
                        item.setModifiedUserId(user.getUserId());
                        item.setModifiedTime(new Date());
                        item.setStatus(StringUtils.isEmpty(item.getStatus())?1: item.getStatus());
                        item.setOrgId(user.getOrganizationId());
                        return item;
                    }).collect(Collectors.toList());
            // 批量新增点检单明细及其履历
            eamEquPointInspectionOrderDetService.batchSave(inspectionOrderDets);
        }


        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        SysUser user = getUser();

        // 修改点检单
        eamEquPointInspectionOrder.setModifiedUserId(user.getUserId());
        eamEquPointInspectionOrder.setModifiedTime(new Date());
        eamEquPointInspectionOrderMapper.updateByPrimaryKeySelective(eamEquPointInspectionOrder);

        // 新增点检单履历
        EamHtEquPointInspectionOrder eamHtEquPointInspectionOrder = new EamHtEquPointInspectionOrder();
        BeanUtil.copyProperties(eamEquPointInspectionOrder, eamHtEquPointInspectionOrder);
        int i = eamHtEquPointInspectionOrderMapper.insert(eamHtEquPointInspectionOrder);


        // 批量删除点检单明细
        HashMap<String, Object> map = new HashMap<>();
        List<Long> idList = new ArrayList<>();
        idList.add(eamEquPointInspectionOrder.getEquPointInspectionOrderId());
        map.put("orderIds", idList);
        List<EamEquPointInspectionOrderDetDto> list = eamEquPointInspectionOrderDetService.findList(map);
        if(!list.isEmpty()){
            List<EamEquPointInspectionOrderDet> projectItems = list.stream().map(item -> {
                EamEquPointInspectionOrderDet orderDet = new EamEquPointInspectionOrderDet();
                BeanUtil.copyProperties(item, orderDet);
                return orderDet;
            }).collect(Collectors.toList());
            eamEquPointInspectionOrderDetService.batchDelete(projectItems);
        }

        // 批量新增点检单明细及其履历
        List<EamEquPointInspectionOrderDet> inspectionOrderDets = eamEquPointInspectionOrder.getOrderDets()
                .stream()
                .map(item -> {
                    item.setEquPointInspectionOrderId(eamEquPointInspectionOrder.getEquPointInspectionOrderId());
                    item.setCreateUserId(user.getUserId());
                    item.setCreateTime(new Date());
                    item.setModifiedUserId(user.getUserId());
                    item.setModifiedTime(new Date());
                    item.setStatus(StringUtils.isEmpty(item.getStatus())?1: item.getStatus());
                    item.setOrgId(user.getOrganizationId());
                    return item;
                }).collect(Collectors.toList());
        eamEquPointInspectionOrderDetService.batchSave(inspectionOrderDets);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids){
        // 查询点检单明细集合
        HashMap<String, Object> map = new HashMap<>();
        List<String> idList = Arrays.asList(ids.split(","));
        map.put("orderIds", idList);
        List<EamEquPointInspectionOrderDetDto> list = eamEquPointInspectionOrderDetService.findList(map);
        if(!list.isEmpty()){
            List<EamEquPointInspectionOrderDet> projectItems = list.stream().map(item -> {
                EamEquPointInspectionOrderDet orderDet = new EamEquPointInspectionOrderDet();
                BeanUtil.copyProperties(item, orderDet);
                return orderDet;
            }).collect(Collectors.toList());
            // 批量删除点检单明细
            eamEquPointInspectionOrderDetService.batchDelete(projectItems);
        }

        //修改设备状态为待生产
        List<EamEquPointInspectionOrder> eamEquPointInspectionOrders = eamEquPointInspectionOrderMapper.selectByIds(ids);
        List<Long> barcodeIds = eamEquPointInspectionOrders.stream().map(EamEquPointInspectionOrder::getEquipmentBarcodeId).collect(Collectors.toList());
        Example example = new Example(EamEquipmentBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("equipmentBarcodeId",barcodeIds);
        List<EamEquipmentBarcode> eamEquipmentBarcodes = eamEquipmentBarcodeMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(eamEquipmentBarcodes)) {
            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodes) {
                eamEquipmentBarcode.setEquipmentStatus((byte) 5);
                eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
            }
        }

        // 批量删除点检单
        return eamEquPointInspectionOrderMapper.deleteByIds(ids);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}

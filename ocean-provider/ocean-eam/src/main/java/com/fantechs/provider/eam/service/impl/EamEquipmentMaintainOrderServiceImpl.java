package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderDetService;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderService;
import com.fantechs.provider.eam.service.EamEquipmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentMaintainOrderServiceImpl extends BaseService<EamEquipmentMaintainOrder> implements EamEquipmentMaintainOrderService {

    @Resource
    private EamEquipmentMaintainOrderMapper eamEquipmentMaintainOrderMapper;

    @Resource
    private EamHtEquipmentMaintainOrderMapper eamHtEquipmentMaintainOrderMapper;

    @Resource
    private EamEquipmentService eamEquipmentService;

    @Resource
    private EamEquipmentMaintainOrderDetService eamEquipmentMaintainOrderDetService;

    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    @Resource
    private EamEquipmentMaintainProjectMapper eamEquipmentMaintainProjectMapper;

    @Override
    public List<EamEquipmentMaintainOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMaintainOrderMapper.findList(map);
    }

    @Override
    public List<EamEquipmentMaintainOrderDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMaintainOrderMapper.findList(map);
    }

    @Override
    public EamEquMaintainOrderDto findListForOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        List<EamEquMaintainOrderDto> list = eamEquipmentService.findListForMaintainOrder(map);
        if (list.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此设备条码没有关联设备或者设备类别");
        }
        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EamEquipmentMaintainOrderDto pdaCreateOrder(String equipmentBarcode) {
        //查设备条码信息
        Example example = new Example(EamEquipmentBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBarcode",equipmentBarcode);
        List<EamEquipmentBarcode> eamEquipmentBarcodes = eamEquipmentBarcodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(eamEquipmentBarcodes)){
            throw new BizErrorException("查不到此设备条码");
        }
        EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodes.get(0);
        //修改设备状态为保养中
        eamEquipmentBarcode.setEquipmentStatus((byte)7);
        eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);

        EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(eamEquipmentBarcode.getEquipmentId());

        //查设备对应的保养项目
        SearchEamEquipmentMaintainProject searchEamEquipmentMaintainProject = new SearchEamEquipmentMaintainProject();
        searchEamEquipmentMaintainProject.setEquipmentCategoryId(eamEquipment.getEquipmentCategoryId());
        List<EamEquipmentMaintainProjectDto> list = eamEquipmentMaintainProjectMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProject));
        if(StringUtils.isEmpty(list)){
            throw new BizErrorException("查不到该设备所属类别的保养项目");
        }
        EamEquipmentMaintainProjectDto eamEquipmentMaintainProjectDto = list.get(0);


        SearchEamEquipmentMaintainOrder searchEamEquipmentMaintainOrder = new SearchEamEquipmentMaintainOrder();
        searchEamEquipmentMaintainOrder.setEquipmentBarcodeId(eamEquipmentBarcode.getEquipmentBarcodeId());
        searchEamEquipmentMaintainOrder.setOrderStatus((byte)1);
        List<EamEquipmentMaintainOrderDto> orderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrder));
        if(StringUtils.isNotEmpty(orderDtos)){
            throw new BizErrorException("已存在该设备待保养状态的单据");
        }


        //保存保养单信息
        EamEquipmentMaintainOrder eamEquipmentMaintainOrder = new EamEquipmentMaintainOrder();
        List<EamEquipmentMaintainOrderDetDto> eamEquipmentMaintainOrderDets = new ArrayList<>();

        eamEquipmentMaintainOrder.setEquipmentMaintainOrderCode(CodeUtils.getId("SBBY-"));
        eamEquipmentMaintainOrder.setEquipmentId(eamEquipmentBarcode.getEquipmentId());
        eamEquipmentMaintainOrder.setEquipmentBarcodeId(eamEquipmentBarcode.getEquipmentBarcodeId());
        eamEquipmentMaintainOrder.setEquipmentMaintainProjectId(eamEquipmentMaintainProjectDto.getEquipmentMaintainProjectId());
        List<EamEquipmentMaintainProjectItem> items = eamEquipmentMaintainProjectDto.getItems();
        for (EamEquipmentMaintainProjectItem eamEquipmentMaintainProjectItem : items){
            EamEquipmentMaintainOrderDetDto eamEquipmentMaintainOrderDetDto = new EamEquipmentMaintainOrderDetDto();
            eamEquipmentMaintainOrderDetDto.setEquipmentMaintainProjectItemId(eamEquipmentMaintainProjectItem.getEquipmentMaintainProjectItemId());
            eamEquipmentMaintainOrderDets.add(eamEquipmentMaintainOrderDetDto);
        }
        eamEquipmentMaintainOrder.setOrderDets(eamEquipmentMaintainOrderDets);

        this.save(eamEquipmentMaintainOrder);


        SearchEamEquipmentMaintainOrder searchEamEquipmentMaintainOrder1 = new SearchEamEquipmentMaintainOrder();
        searchEamEquipmentMaintainOrder1.setEquipmentMaintainOrderId(eamEquipmentMaintainOrder.getEquipmentMaintainOrderId());
        List<EamEquipmentMaintainOrderDto> equipmentMaintainOrderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainOrder1));
        return equipmentMaintainOrderDtos.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pdaSubmit(EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        eamEquipmentMaintainOrder.setOrderStatus((byte)2);
        int i = this.update(eamEquipmentMaintainOrder);

        //修改该设备保养信息
        EamEquipmentBarcode eamEquipmentBarcode = new EamEquipmentBarcode();
        eamEquipmentBarcode.setEquipmentBarcodeId(eamEquipmentMaintainOrder.getEquipmentBarcodeId());
        eamEquipmentBarcode.setLastTimeMaintainTime(new Date());
        eamEquipmentBarcode.setCurrentMaintainTime(eamEquipmentBarcode.getCurrentMaintainTime()==null?1:eamEquipmentBarcode.getCurrentMaintainTime()+1);
        eamEquipmentBarcode.setCurrentMaintainUsageTime(0);
        eamEquipmentBarcode.setEquipmentStatus((byte)5);
        eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);

        return i;
    }

    @Override
    @Transactional
    public int save(EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        // 新增保养单
        eamEquipmentMaintainOrderMapper.insertUseGeneratedKeys(eamEquipmentMaintainOrder);

        // 新增保养单履历
        EamHtEquipmentMaintainOrder eamHtEquipmentMaintainOrder = new EamHtEquipmentMaintainOrder();
        BeanUtil.copyProperties(eamEquipmentMaintainOrder, eamHtEquipmentMaintainOrder);
        int i = eamHtEquipmentMaintainOrderMapper.insert(eamHtEquipmentMaintainOrder);

        if(!eamEquipmentMaintainOrder.getOrderDets().isEmpty()){
            List<EamEquipmentMaintainOrderDet> maintainOrderDets = eamEquipmentMaintainOrder.getOrderDets()
                    .stream()
                    .map(item -> {
                        item.setEquipmentMaintainOrderId(eamEquipmentMaintainOrder.getEquipmentMaintainOrderId());
                        return item;
                    }).collect(Collectors.toList());
            // 批量新增保养单事项及其履历
            eamEquipmentMaintainOrderDetService.batchSave(maintainOrderDets);
        }
        return i;
    }

    @Override
    @Transactional
    public int update(EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        // 修改保养单
        eamEquipmentMaintainOrderMapper.updateByPrimaryKey(eamEquipmentMaintainOrder);

        // 新增保养单履历
        EamHtEquipmentMaintainOrder eamHtEquipmentMaintainOrder = new EamHtEquipmentMaintainOrder();
        BeanUtil.copyProperties(eamEquipmentMaintainOrder, eamHtEquipmentMaintainOrder);
        int i = eamHtEquipmentMaintainOrderMapper.insert(eamHtEquipmentMaintainOrder);

        if(!eamEquipmentMaintainOrder.getOrderDets().isEmpty()){
            // 批量删除保养单明细
            List<EamEquipmentMaintainOrderDet> orderDets = eamEquipmentMaintainOrder.getOrderDets().stream().map(item -> {
                EamEquipmentMaintainOrderDet orderDet = new EamEquipmentMaintainOrderDet();
                BeanUtil.copyProperties(item, orderDet);
                return orderDet;
            }).collect(Collectors.toList());
            eamEquipmentMaintainOrderDetService.batchDelete(orderDets);
            // 批量新增保养单明细及其履历
            List<EamEquipmentMaintainOrderDet> maintainOrderDets = eamEquipmentMaintainOrder.getOrderDets()
                    .stream()
                    .map(item -> {
                        item.setEquipmentMaintainOrderId(eamEquipmentMaintainOrder.getEquipmentMaintainOrderId());
                        return item;
                    }).collect(Collectors.toList());
            eamEquipmentMaintainOrderDetService.batchSave(maintainOrderDets);
        }
        return i;
    }

    @Override
    @Transactional
    public int batchDelete(String ids){
        // 查询保养事项集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderIds", ids);
        List<EamEquipmentMaintainOrderDetDto> list = eamEquipmentMaintainOrderDetService.findList(map);
        if(!list.isEmpty()){
            List<EamEquipmentMaintainOrderDet> projectItems = list.stream().map(item -> {
                EamEquipmentMaintainOrderDet orderDet = new EamEquipmentMaintainOrderDet();
                BeanUtil.copyProperties(item, orderDet);
                return orderDet;
            }).collect(Collectors.toList());
            // 批量删除保养项目事项
            eamEquipmentMaintainOrderDetService.batchDelete(projectItems);
        }
        // 批量删除保养项目
        return this.batchDelete(ids);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}

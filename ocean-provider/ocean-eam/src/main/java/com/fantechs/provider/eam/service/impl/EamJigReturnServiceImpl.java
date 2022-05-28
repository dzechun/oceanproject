package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.general.entity.eam.EamJigReturn;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigReturn;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJig;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigReturnService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/07/30.
 */
@Service
public class EamJigReturnServiceImpl extends BaseService<EamJigReturn> implements EamJigReturnService {

    @Resource
    private EamJigReturnMapper eamJigReturnMapper;
    @Resource
    private EamHtJigReturnMapper eamHtJigReturnMapper;
    @Resource
    private EamJigRequisitionMapper eamJigRequisitionMapper;
    @Resource
    private EamJigMaterialMapper eamJigMaterialMapper;
    @Resource
    private EamEquipmentJigListMapper eamEquipmentJigListMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private EamJigScrapOrderServiceImpl eamJigScrapOrderService;

    @Override
    public List<EamJigReturnDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamJigReturnMapper.findList(map);
    }

    /**
     * PDA治具归还--保存归还记录
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<EamJigReturn> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("IsJigCanContinueUse");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        List<EamHtJigReturn> htList = new ArrayList<>();
        for (EamJigReturn eamJigReturn : list){
            //原有的归还记录不再新增数据
            if(StringUtils.isNotEmpty(eamJigReturn.getJigReturnId())){
                continue;
            }

            if(StringUtils.isEmpty(eamJigReturn.getJigRequisitionId())) {
                EamJigRequisition eamJigRequisition = getRequisitionRecord(eamJigReturn);
                eamJigReturn.setJigRequisitionId(eamJigRequisition.getJigRequisitionId());
            }

            eamJigReturn.setCreateUserId(user.getUserId());
            eamJigReturn.setCreateTime(new Date());
            eamJigReturn.setModifiedUserId(user.getUserId());
            eamJigReturn.setModifiedTime(new Date());
            eamJigReturn.setStatus(StringUtils.isEmpty(eamJigReturn.getStatus())?1: eamJigReturn.getStatus());
            eamJigReturn.setOrgId(user.getOrganizationId());
            eamJigReturnMapper.insertUseGeneratedKeys(eamJigReturn);

            //履历
            EamHtJigReturn eamHtJigReturn = new EamHtJigReturn();
            BeanUtils.copyProperties(eamJigReturn, eamHtJigReturn);
            htList.add(eamHtJigReturn);

            //修改治具使用状态及使用次数
            EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectByPrimaryKey(eamJigReturn.getJigBarcodeId());
            eamJigBarcode.setUsageStatus(eamJigBarcode.getUsageStatus()==3 ? (byte)3 : (byte)1);
            eamJigBarcode.setCurrentUsageTime(eamJigBarcode.getCurrentUsageTime()==null ?
                    eamJigReturn.getThisTimeUsageTime():
                    eamJigBarcode.getCurrentUsageTime()+eamJigReturn.getThisTimeUsageTime());
            eamJigBarcode.setCurrentMaintainUsageTime(eamJigBarcode.getCurrentMaintainUsageTime()==null ?
                    eamJigReturn.getThisTimeUsageTime():
                    eamJigBarcode.getCurrentMaintainUsageTime()+eamJigReturn.getThisTimeUsageTime());
            eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);

            //治具使用次数达到使用上限不允许继续使用的，生成报废单
            if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 0) {
                EamJig eamJig = eamJigMapper.selectByPrimaryKey(eamJigBarcode.getJigId());
                if (StringUtils.isNotEmpty(eamJigBarcode.getCurrentUsageTime(), eamJig.getMaxUsageTime())
                        && eamJig.getMaxUsageTime() != 0) {
                    if (eamJigBarcode.getCurrentUsageTime().compareTo(eamJig.getMaxUsageTime()) == 0
                            || eamJigBarcode.getCurrentUsageTime().compareTo(eamJig.getMaxUsageTime()) == 1) {
                        EamJigScrapOrderDto eamJigScrapOrderDto = new EamJigScrapOrderDto();
                        EamJigScrapOrderDetDto eamJigScrapOrderDetDto = new EamJigScrapOrderDetDto();
                        List<EamJigScrapOrderDetDto> eamJigScrapOrderDetDtos = new ArrayList<>();

                        eamJigScrapOrderDetDto.setJigBarcodeId(eamJigBarcode.getJigBarcodeId());
                        eamJigScrapOrderDetDtos.add(eamJigScrapOrderDetDto);

                        eamJigScrapOrderDto.setJigScrapOrderCode(CodeUtils.getId("ZJBF-"));
                        eamJigScrapOrderDto.setOrderStatus((byte) 1);
                        eamJigScrapOrderDto.setList(eamJigScrapOrderDetDtos);

                        eamJigScrapOrderService.save(eamJigScrapOrderDto);
                    }
                }
            }

        }

        return eamHtJigReturnMapper.insertList(htList);
    }

    public EamJigRequisition getRequisitionRecord(EamJigReturn eamJigReturn){
        SearchEamJigRequisition searchEamJigRequisition = new SearchEamJigRequisition();
        searchEamJigRequisition.setWorkOrderId(eamJigReturn.getWorkOrderId());
        searchEamJigRequisition.setJigId(eamJigReturn.getJigId());
        searchEamJigRequisition.setJigBarcodeId(eamJigReturn.getJigBarcodeId());
        searchEamJigRequisition.setIfExceptReturn(1);
        List<EamJigRequisitionDto> list = eamJigRequisitionMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));

        return list.get(0);
    }

    /**
     *  PDA治具归还--根据工单号查询治具领用记录
     * @param workOrderCode
     * @return
     */
    @Override
    public EamJigRequisitionWorkOrderDto findWorkOrder(String workOrderCode){
        EamJigRequisitionWorkOrderDto eamJigRequisitionWorkOrderDto = new EamJigRequisitionWorkOrderDto();
        //查询工单
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
        searchMesPmWorkOrder.setCodeQueryMark(1);
        List<MesPmWorkOrderDto> mesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(mesPmWorkOrderDtos)){
            throw new BizErrorException("查无此工单");
        }
        BeanUtils.copyProperties(mesPmWorkOrderDtos.get(0),eamJigRequisitionWorkOrderDto);

        //查询治具领用记录
        SearchEamJigRequisition searchEamJigRequisition = new SearchEamJigRequisition();
        searchEamJigRequisition.setWorkOrderId(eamJigRequisitionWorkOrderDto.getWorkOrderId());
        List<Long> jigIdList = eamJigRequisitionMapper.findJigId(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));

        Example example1 = new Example(EamJigRequisition.class);
        Example example2 = new Example(EamJigRequisition.class);
        List<EamJigMaterialDto> list = new ArrayList<>();
        for (Long jigId : jigIdList) {
            //获取治具信息
            SearchEamJigMaterial searchEamJigMaterial = new SearchEamJigMaterial();
            searchEamJigMaterial.setJigId(jigId);
            List<EamJigMaterialDto> eamJigMaterialDtos = eamJigMaterialMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaterial));
            EamJigMaterialDto eamJigMaterialDto = eamJigMaterialDtos.get(0);

            //获取领用数量
            example1.clear();
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("workOrderId",mesPmWorkOrderDtos.get(0).getWorkOrderId());
            criteria1.andEqualTo("jigId",jigId);
            int recordQty = eamJigRequisitionMapper.selectCountByExample(example1);
            eamJigMaterialDto.setRecordQty(recordQty);
            //获取归还数量
            example2.clear();
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("workOrderId",mesPmWorkOrderDtos.get(0).getWorkOrderId());
            criteria2.andEqualTo("jigId",jigId);
            int returnQty = eamJigReturnMapper.selectCountByExample(example2);
            eamJigMaterialDto.setReturnQty(returnQty);

            //设备名称
            Map<String,Object> map = new HashMap<>();
            map.put("jigId",eamJigMaterialDto.getJigId());
            List<EamEquipmentJigListDto> eamEquipmentJigListDtos = eamEquipmentJigListMapper.findList(map);
            if(StringUtils.isNotEmpty(eamEquipmentJigListDtos)) {
                StringBuilder sb = new StringBuilder();
                for (EamEquipmentJigListDto eamEquipmentJigListDto : eamEquipmentJigListDtos) {
                    sb.append(eamEquipmentJigListDto.getEquipmentName()).append(";");
                }
                String equipmentName = sb.toString().substring(0, sb.toString().length() - 1);
                eamJigMaterialDto.setEquipmentName(equipmentName);
            }

            list.add(eamJigMaterialDto);
        }
        eamJigRequisitionWorkOrderDto.setList(list);

        return eamJigRequisitionWorkOrderDto;
    }

    /**
     * PDA治具归还--检查治具条码
     * @param jigBarcode
     * @param jigId
     * @param workOrderId
     * @return
     */
    @Override
    public EamJigBarcode checkJigBarcode(String jigBarcode, Long jigId, Long workOrderId){

        Example example = new Example(EamJigBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcode",jigBarcode);
        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(eamJigBarcode)){
            throw new BizErrorException("查无此治具条码");
        }

        if(!jigId.equals(eamJigBarcode.getJigId())){
            throw new BizErrorException("该治具条码不属于此治具");
        }

        if(eamJigBarcode.getUsageStatus()==(byte)1){
            throw new BizErrorException("该治具处于空闲状态");
        }

        Example example1 = new Example(EamJigRequisition.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigBarcodeId",eamJigBarcode.getJigBarcodeId())
                .andEqualTo("jigId",jigId)
                .andEqualTo("workOrderId",workOrderId);
        List<EamJigRequisition> eamJigRequisitions = eamJigRequisitionMapper.selectByExample(example1);
        if(StringUtils.isEmpty(eamJigRequisitions)){
            throw new BizErrorException("查无此工单下该治具的领用记录");
        }

        /*Example example2 = new Example(EamJigReturn.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("jigBarcodeId",eamJigBarcode.getJigBarcodeId())
                .andEqualTo("jigId",jigId)
                .andEqualTo("workOrderId",workOrderId);
        List<EamJigReturn> eamJigReturns = eamJigReturnMapper.selectByExample(example2);
        if(StringUtils.isNotEmpty(eamJigReturns)){
            throw new BizErrorException("此工单下该治具已归还");
        }*/

        return eamJigBarcode;
    }

    /**
     * PDA治具归还--检查库位条码
     * @param storageCode
     * @param jigId
     * @return
     */
    @Override
    public int checkStorageCode(String storageCode, Long jigId){
        SearchEamJig searchEamJig = new SearchEamJig();
        searchEamJig.setJigId(jigId);
        List<EamJigDto> list = eamJigMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJig));
        EamJigDto eamJigDto = list.get(0);
        if(!storageCode.equals(eamJigDto.getStorageCode())){
            throw new BizErrorException("该库位非此治具的正确库位");
        }

        return 1;
    }

}

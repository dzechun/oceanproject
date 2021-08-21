package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigRequisitionService;
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
 * Created by leifengzhi on 2021/07/30.
 */
@Service
public class EamJigRequisitionServiceImpl extends BaseService<EamJigRequisition> implements EamJigRequisitionService {

    @Resource
    private EamJigRequisitionMapper eamJigRequisitionMapper;
    @Resource
    private EamHtJigRequisitionMapper eamHtJigRequisitionMapper;
    @Resource
    private EamJigMaterialMapper eamJigMaterialMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private EamJigReturnServiceImpl eamJigReturnService;

    @Override
    public List<EamJigRequisitionDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigRequisitionMapper.findList(map);
    }

    /**
     *  PDA转换工单--获取旧工单已领用数量
     * @param newWorkOrderCode
     * @param oldWorkOrderCode
     * @return
     */
    @Override
    public List<EamJigMaterialDto> getRecordQty(String newWorkOrderCode, String oldWorkOrderCode){
        //查询工单
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setCodeQueryMark(1);
        //新工单
        searchMesPmWorkOrder.setWorkOrderCode(newWorkOrderCode);
        List<MesPmWorkOrderDto> newMesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        //旧工单
        searchMesPmWorkOrder.setWorkOrderCode(oldWorkOrderCode);
        List<MesPmWorkOrderDto> oldMesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(oldMesPmWorkOrderDtos)){
            throw new BizErrorException("查无此工单");
        }

        if(!newMesPmWorkOrderDtos.get(0).getMaterialId().equals(oldMesPmWorkOrderDtos.get(0).getMaterialId())){
            throw new BizErrorException("新旧工单的物料不一致，无法转换");
        }

        //按治具id分组查询领用记录
        SearchEamJigRequisition searchEamJigRequisition = new SearchEamJigRequisition();
        searchEamJigRequisition.setWorkOrderId(oldMesPmWorkOrderDtos.get(0).getWorkOrderId());
        List<Long> jigIdList = eamJigRequisitionMapper.findJigId(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));

        Example example = new Example(EamJigRequisition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId",oldMesPmWorkOrderDtos.get(0).getWorkOrderId());

        List<EamJigMaterialDto> list = new ArrayList<>();
        for (Long jigId : jigIdList) {
            EamJigMaterialDto eamJigMaterialDto = new EamJigMaterialDto();
            eamJigMaterialDto.setJigId(jigId);
            //获取记录数量
            criteria.andEqualTo("jigId",jigId);
            int recordQty = eamJigRequisitionMapper.selectCountByExample(example);
            eamJigMaterialDto.setRecordQty(recordQty);
            eamJigMaterialDto.setOldWorkOrderId(oldMesPmWorkOrderDtos.get(0).getWorkOrderId());

            list.add(eamJigMaterialDto);
        }

        return list;
    }

    /**
     * PDA转换工单--提交
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int conversion(List<EamJigRequisition> list){
        List<EamJigReturn> eamJigReturnList = new ArrayList<>();
        int sum = 0;

        //保存归还记录
        for (EamJigRequisition eamJigRequisition : list){
            EamJigReturn eamJigReturn = new EamJigReturn();
            eamJigReturn.setWorkOrderId(eamJigRequisition.getWorkOrderId());
            eamJigReturn.setJigRequisitionId(eamJigRequisition.getJigRequisitionId());
            eamJigReturn.setJigId(eamJigRequisition.getJigId());
            eamJigReturn.setJigBarcodeId(eamJigRequisition.getJigBarcodeId());
            eamJigReturn.setThisTimeUsageTime(eamJigRequisition.getThisTimeUsageTime());
            eamJigReturnList.add(eamJigReturn);

            eamJigRequisition.setJigRequisitionId(null);
            eamJigRequisition.setWorkOrderId(eamJigRequisition.getNewWorkOrderId());
        }

        sum += eamJigReturnService.batchSave(eamJigReturnList);

        //保存领用记录
        sum +=this.batchSave(list);

        return sum;
    }

    /**
     * PDA治具领用--检查治具条码
     * @param jigBarcode
     * @param jigId
     * @return
     */
    @Override
    public EamJigBarcode checkJigBarcode(String jigBarcode,Long jigId){

        Example example = new Example(EamJigBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcode",jigBarcode);
        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(eamJigBarcode)){
            throw new BizErrorException("查无此治具条码");
        }

        EamJig eamJig = eamJigMapper.selectByPrimaryKey(eamJigBarcode.getJigId());
        if(eamJigBarcode.getCurrentUsageTime()!=null && eamJigBarcode.getCurrentUsageTime().intValue() >= eamJig.getMaxUsageTime().intValue()){
            throw new BizErrorException("该治具已使用次数已达到治具最大使用次数");
        }

        if(!jigId.equals(eamJigBarcode.getJigId())){
            throw new BizErrorException("该治具条码不属于此治具");
        }

        if(eamJigBarcode.getUsageStatus()==(byte)1){
            throw new BizErrorException("该治具正在使用中");
        }

        return eamJigBarcode;
    }

    /**
     *  PDA治具领用--根据工单号查询工单信息
     * @param workOrderCode
     * @return
     */
    @Override
    public EamJigRequisitionWorkOrderDto findWorkOrder(String workOrderCode){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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

        //查询治具与产品绑定关系
        SearchEamJigMaterial searchEamJigMaterial = new SearchEamJigMaterial();
        searchEamJigMaterial.setMaterialId(eamJigRequisitionWorkOrderDto.getMaterialId());
        searchEamJigMaterial.setOrgId(user.getOrganizationId());
        List<EamJigMaterialDto> jigList = eamJigMaterialMapper.findJigList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaterial));


        Example example = new Example(EamJigRequisition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId",mesPmWorkOrderDtos.get(0).getWorkOrderId());
        for (EamJigMaterialDto eamJigMaterialDto : jigList){
            criteria.andEqualTo("jigId",eamJigMaterialDto.getJigId());
            int recordQty = eamJigRequisitionMapper.selectCountByExample(example);
            eamJigMaterialDto.setRecordQty(recordQty);
        }

        eamJigRequisitionWorkOrderDto.setList(jigList);

        return eamJigRequisitionWorkOrderDto;
    }

    /**
     *  PDA治具领用--保存领用记录
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<EamJigRequisition> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断已扫描数量是否符合要求
        this.checkUsageQty(list.get(0).getWorkOrderId(),list.get(0).getJigId(),list.size());


        List<EamHtJigRequisition> htList = new ArrayList<>();
        for (EamJigRequisition eamJigRequisition : list){
            //原有的领用记录不再新增数据
            if(StringUtils.isNotEmpty(eamJigRequisition.getJigRequisitionId())){
                continue;
            }

            eamJigRequisition.setCreateUserId(user.getUserId());
            eamJigRequisition.setCreateTime(new Date());
            eamJigRequisition.setModifiedUserId(user.getUserId());
            eamJigRequisition.setModifiedTime(new Date());
            eamJigRequisition.setStatus(StringUtils.isEmpty(eamJigRequisition.getStatus())?1: eamJigRequisition.getStatus());
            eamJigRequisition.setOrgId(user.getOrganizationId());
            eamJigRequisitionMapper.insertUseGeneratedKeys(eamJigRequisition);

            //履历
            EamHtJigRequisition eamHtJigRequisition = new EamHtJigRequisition();
            BeanUtils.copyProperties(eamJigRequisition, eamHtJigRequisition);
            htList.add(eamHtJigRequisition);

            //修改治具使用状态
            EamJigBarcode eamJigBarcode = new EamJigBarcode();
            eamJigBarcode.setJigBarcodeId(eamJigRequisition.getJigBarcodeId());
            eamJigBarcode.setUsageStatus((byte)1);
            eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
        }

        return eamHtJigRequisitionMapper.insertList(htList);
    }

    /**
     *  PDA治具领用--根据配置项判断已扫描数量是否符合要求
     * @param workOrderId  工单id
     * @param jigId  治具id
     * @param count  已扫描数量
     * @return
     */
    public boolean checkUsageQty(Long workOrderId,Long jigId,int count) {
        MesPmWorkOrder workOrder = pmFeignApi.workOrderDetail(workOrderId).getData();

        SearchEamJigMaterial searchEamJigMaterial = new SearchEamJigMaterial();
        searchEamJigMaterial.setJigId(jigId);
        searchEamJigMaterial.setMaterialId(workOrder.getMaterialId());
        List<EamJigMaterialDto> jigList = eamJigMaterialMapper.findJigList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaterial));
        //所需数量
        int usageQty = jigList.get(0).getUsageQty();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("UsageQtyEqual");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 1){
            if(usageQty != count){
                throw new BizErrorException("治具已扫描数量必须与所需数量一致");
            }
        }else {
            if(usageQty < count){
                throw new BizErrorException("治具已扫描数量不能大于所需数量");
            }
        }

        return true;
    }
}

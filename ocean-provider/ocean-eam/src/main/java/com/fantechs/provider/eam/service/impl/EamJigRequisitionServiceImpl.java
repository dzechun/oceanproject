package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.eam.mapper.EamHtJigRequisitionMapper;
import com.fantechs.provider.eam.mapper.EamJigBarcodeMapper;
import com.fantechs.provider.eam.mapper.EamJigReMaterialMapper;
import com.fantechs.provider.eam.mapper.EamJigRequisitionMapper;
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
    private EamJigReMaterialMapper eamJigReMaterialMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;

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
     *  PDA转换工单--查询工单信息
     * @param newWorkOrderCode
     * @param oldWorkOrderCode
     * @return
     */
    @Override
    public EamJigRequisitionWorkOrderDto findWorkOrderToTransform(String newWorkOrderCode,String oldWorkOrderCode){
        EamJigRequisitionWorkOrderDto eamJigRequisitionWorkOrderDto = new EamJigRequisitionWorkOrderDto();
        //查询工单
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        //新工单
        searchMesPmWorkOrder.setWorkOrderCode(newWorkOrderCode);
        List<MesPmWorkOrderDto> newMesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        //旧工单
        searchMesPmWorkOrder.setWorkOrderCode(oldWorkOrderCode);
        List<MesPmWorkOrderDto> oldMesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(newMesPmWorkOrderDtos,oldMesPmWorkOrderDtos)){
            throw new BizErrorException("查无此工单");
        }

        if(newMesPmWorkOrderDtos.get(0).getMaterialId().longValue() != oldMesPmWorkOrderDtos.get(0).getMaterialId().longValue()){
            throw new BizErrorException("新旧工单的物料不一致");
        }
        BeanUtils.copyProperties(newMesPmWorkOrderDtos.get(0),eamJigRequisitionWorkOrderDto);

        //按治具id分组查询领用记录
        SearchEamJigRequisition searchEamJigRequisition = new SearchEamJigRequisition();
        searchEamJigRequisition.setWorkOrderId(oldMesPmWorkOrderDtos.get(0).getWorkOrderId());
        List<Long> jigIdList = eamJigRequisitionMapper.findJigId(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));

        List<EamJigReMaterialDto> list = new ArrayList<>();
        for (Long jigId : jigIdList) {
            SearchEamJigReMaterial searchEamJigReMaterial = new SearchEamJigReMaterial();
            searchEamJigReMaterial.setJigId(jigId);
            searchEamJigReMaterial.setMaterialId(oldMesPmWorkOrderDtos.get(0).getMaterialId());
            EamJigReMaterialDto eamJigReMaterialDto = eamJigReMaterialMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial)).get(0);

            //获取记录数量
            searchEamJigRequisition.setJigId(jigId);
            Integer recordQty = eamJigRequisitionMapper.getRecordQty(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
            eamJigReMaterialDto.setRecordQty(recordQty);

            list.add(eamJigReMaterialDto);
        }
        eamJigRequisitionWorkOrderDto.setList(list);

        return eamJigRequisitionWorkOrderDto;
    }


    /**
     * PDA治具领用--检查治具条码
     * @param jigBarcode
     * @param jigId
     * @return
     */
    @Override
    public EamJigBarcode checkJigBarcode(String jigBarcode,Long jigId,Integer usageQty,Integer count){
        this.checkUsageQty(usageQty,count);

        Example example = new Example(EamJigBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcode",jigBarcode);
        List<EamJigBarcode> barcodes = eamJigBarcodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(barcodes)){
            throw new BizErrorException("查无此治具条码");
        }
        EamJigBarcode eamJigBarcode = barcodes.get(0);

        if(eamJigBarcode.getJigId() != jigId){
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
        EamJigRequisitionWorkOrderDto eamJigRequisitionWorkOrderDto = new EamJigRequisitionWorkOrderDto();
        //查询工单
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
        List<MesPmWorkOrderDto> mesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(mesPmWorkOrderDtos)){
            throw new BizErrorException("查无此工单");
        }
        BeanUtils.copyProperties(mesPmWorkOrderDtos.get(0),eamJigRequisitionWorkOrderDto);

        //查询治具与产品绑定关系
        SearchEamJigReMaterial searchEamJigReMaterial = new SearchEamJigReMaterial();
        searchEamJigReMaterial.setMaterialId(eamJigRequisitionWorkOrderDto.getMaterialId());
        List<EamJigReMaterialDto> eamJigReMaterialDtos = eamJigReMaterialMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));
        eamJigRequisitionWorkOrderDto.setList(eamJigReMaterialDtos);

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

        Example example = new Example(EamJigRequisition.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigId", list.get(0).getJigId())
                .andEqualTo("workOrderId",list.get(0).getWorkOrderId());
        eamJigRequisitionMapper.deleteByExample(example);

        List<EamHtJigRequisition> htList = new ArrayList<>();
        for (EamJigRequisition eamJigRequisition : list){
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
        }

        return eamHtJigRequisitionMapper.insertList(htList);
    }

    /**
     *  PDA治具领用--配置项判断
     * @param
     * @return
     */
    public boolean checkUsageQty(int usageQty,int count) {
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

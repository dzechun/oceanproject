package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtIssue;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigReMaterial;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
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

    @Override
    public List<EamJigRequisitionDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigRequisitionMapper.findList(map);
    }

    @Override
    public EamJigRequisitionDto checkJigBarcode(String jigBarcode,Long jigId){
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

        EamJigRequisitionDto eamJigRequisitionDto = new EamJigRequisitionDto();
        eamJigRequisitionDto.setJigId(eamJigBarcode.getJigId());
        eamJigRequisitionDto.setJigBarcodeId(eamJigBarcode.getJigBarcodeId());

        return eamJigRequisitionDto;
    }

    @Override
    public EamJigRequisitionDto findWorkOrder(String workOrderCode){
        //查询工单
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
        List<MesPmWorkOrderDto> mesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(mesPmWorkOrderDtos)){
            throw new BizErrorException("查无此工单");
        }
        MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtos.get(0);

        //查询治具与产品绑定关系
        SearchEamJigReMaterial searchEamJigReMaterial = new SearchEamJigReMaterial();
        searchEamJigReMaterial.setMaterialId(mesPmWorkOrderDto.getMaterialId());
        List<EamJigReMaterialDto> eamJigReMaterialDtos = eamJigReMaterialMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));

        EamJigRequisitionDto eamJigRequisitionDto = new EamJigRequisitionDto();
        eamJigRequisitionDto.setWorkOrderCode(mesPmWorkOrderDto.getWorkOrderCode());
        eamJigRequisitionDto.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
        eamJigRequisitionDto.setMaterialDesc(mesPmWorkOrderDto.getMaterialDesc());
        eamJigRequisitionDto.setProName(mesPmWorkOrderDto.getProName());
        eamJigRequisitionDto.setList(eamJigReMaterialDtos);

        return eamJigRequisitionDto;
    }

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


}

package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.eam.EamJigRequisitionWorkOrderDto;
import com.fantechs.common.base.general.dto.eam.EamJigReturnDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.general.entity.eam.EamJigReturn;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigReturn;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJig;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRequisition;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigReturnService;
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
public class EamJigReturnServiceImpl extends BaseService<EamJigReturn> implements EamJigReturnService {

    @Resource
    private EamJigReturnMapper eamJigReturnMapper;
    @Resource
    private EamHtJigReturnMapper eamHtJigReturnMapper;
    @Resource
    private EamJigRequisitionMapper eamJigRequisitionMapper;
    @Resource
    private EamJigReMaterialMapper eamJigReMaterialMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private PMFeignApi pmFeignApi;

    @Override
    public List<EamJigReturnDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigReturn> htList = new ArrayList<>();
        for (EamJigReturn eamJigReturn : list){
            //原有的归还记录不再新增数据
            if(StringUtils.isNotEmpty(eamJigReturn.getJigReturnId())){
                continue;
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
            eamJigBarcode.setUsageStatus((byte)2);
            eamJigBarcode.setCurrentUsageTime(eamJigBarcode.getCurrentUsageTime()==null ?
                    eamJigReturn.getThisTimeUsageTime():
                    eamJigBarcode.getCurrentUsageTime()+eamJigReturn.getThisTimeUsageTime());
            eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
        }

        return eamHtJigReturnMapper.insertList(htList);
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
        List<MesPmWorkOrderDto> mesPmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if(StringUtils.isEmpty(mesPmWorkOrderDtos)){
            throw new BizErrorException("查无此工单");
        }
        BeanUtils.copyProperties(mesPmWorkOrderDtos.get(0),eamJigRequisitionWorkOrderDto);

        //查询治具领用记录
        SearchEamJigRequisition searchEamJigRequisition = new SearchEamJigRequisition();
        searchEamJigRequisition.setWorkOrderId(eamJigRequisitionWorkOrderDto.getWorkOrderId());
        List<Long> jigIdList = eamJigRequisitionMapper.findJigId(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));

        List<EamJigReMaterialDto> list = new ArrayList<>();
        for (Long jigId : jigIdList) {
            //获取治具信息
            SearchEamJigReMaterial searchEamJigReMaterial = new SearchEamJigReMaterial();
            searchEamJigReMaterial.setJigId(jigId);
            List<EamJigReMaterialDto> eamJigReMaterialDtoList = eamJigReMaterialMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));
            EamJigReMaterialDto eamJigReMaterialDto = eamJigReMaterialDtoList.get(0);

            //获取领用数量
            searchEamJigRequisition.setJigId(jigId);
            Integer recordQty = eamJigRequisitionMapper.getRecordQty(ControllerUtil.dynamicConditionByEntity(searchEamJigRequisition));
            eamJigReMaterialDto.setRecordQty(recordQty);

            list.add(eamJigReMaterialDto);
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

        if(eamJigBarcode.getUsageStatus()==(byte)2){
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

        Example example2 = new Example(EamJigReturn.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("jigBarcodeId",eamJigBarcode.getJigBarcodeId())
                .andEqualTo("jigId",jigId)
                .andEqualTo("workOrderId",workOrderId);
        List<EamJigReturn> eamJigReturns = eamJigReturnMapper.selectByExample(example2);
        if(StringUtils.isNotEmpty(eamJigReturns)){
            throw new BizErrorException("此工单下该治具已归还");
        }

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

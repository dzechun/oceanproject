package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentAttachment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackup;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderBadPhenotype;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderBadPhenotypeRepair;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderSemiProduct;
import com.fantechs.common.base.general.entity.mes.sfc.history.MesSfcHtRepairOrder;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.mapper.*;
import com.fantechs.provider.mes.sfc.service.MesSfcRepairOrderService;
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
 * Created by leifengzhi on 2021/09/10.
 */
@Service
public class MesSfcRepairOrderServiceImpl extends BaseService<MesSfcRepairOrder> implements MesSfcRepairOrderService {

    @Resource
    private MesSfcRepairOrderMapper mesSfcRepairOrderMapper;
    @Resource
    private MesSfcHtRepairOrderMapper mesSfcHtRepairOrderMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private MesSfcRepairOrderBadPhenotypeMapper mesSfcRepairOrderBadPhenotypeMapper;
    @Resource
    private MesSfcRepairOrderBadPhenotypeRepairMapper mesSfcRepairOrderBadPhenotypeRepairMapper;
    @Resource
    private MesSfcRepairOrderSemiProductMapper mesSfcRepairOrderSemiProductMapper;

    @Override
    public List<MesSfcRepairOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return mesSfcRepairOrderMapper.findList(map);
    }

    @Override
    public List<MesSfcHtRepairOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return mesSfcHtRepairOrderMapper.findHtList(map);
    }

    @Override
    public MesPmWorkOrderDto getWorkOrder(String SNCode,String workOrderCode){
        if(StringUtils.isNotEmpty(SNCode)) {
            //截取工单号
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("WorkOrderPositionOnBarcode");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            String paraValue = sysSpecItemList.get(0).getParaValue();
            int beginIndex = 0;
            int endIndex = 0;
            if (StringUtils.isNotEmpty(paraValue)) {
                String[] arry = paraValue.split("-");
                if (arry.length == 2) {
                    beginIndex = Integer.parseInt(arry[0]);
                    endIndex = Integer.parseInt(arry[1]);
                }
            }
            workOrderCode = SNCode.substring(beginIndex, endIndex);
        }

        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
        List<MesPmWorkOrderDto> pmWorkOrderDtos = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
        if (StringUtils.isEmpty(pmWorkOrderDtos)){
            throw new BizErrorException("找不到此工单");
        }

        return pmWorkOrderDtos.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesSfcRepairOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        ifCodeRepeat(record,user);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        mesSfcRepairOrderMapper.insertUseGeneratedKeys(record);

        MesSfcHtRepairOrder mesSfcHtRepairOrder = new MesSfcHtRepairOrder();
        BeanUtils.copyProperties(record, mesSfcHtRepairOrder);
        int i = mesSfcHtRepairOrderMapper.insertSelective(mesSfcHtRepairOrder);

        //不良现象
        List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypeList = record.getMesSfcRepairOrderBadPhenotypeList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeList)){
            for (MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype : mesSfcRepairOrderBadPhenotypeList){
                mesSfcRepairOrderBadPhenotype.setRepairOrderId(record.getRepairOrderId());
                mesSfcRepairOrderBadPhenotype.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotype.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotype.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotype.getStatus())?1: mesSfcRepairOrderBadPhenotype.getStatus());
                mesSfcRepairOrderBadPhenotype.setOrgId(user.getOrganizationId());
                //保存维修信息
                saveBadPhenotypeRepair(mesSfcRepairOrderBadPhenotype,user);
            }
            mesSfcRepairOrderBadPhenotypeMapper.insertList(mesSfcRepairOrderBadPhenotypeList);
        }

        //半成品
        List<MesSfcRepairOrderSemiProduct> mesSfcRepairOrderSemiProductList = record.getMesSfcRepairOrderSemiProductList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderSemiProductList)){
            for (MesSfcRepairOrderSemiProduct mesSfcRepairOrderSemiProduct : mesSfcRepairOrderSemiProductList){
                mesSfcRepairOrderSemiProduct.setRepairOrderId(record.getRepairOrderId());
                mesSfcRepairOrderSemiProduct.setCreateUserId(user.getUserId());
                mesSfcRepairOrderSemiProduct.setCreateTime(new Date());
                mesSfcRepairOrderSemiProduct.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderSemiProduct.setModifiedTime(new Date());
                mesSfcRepairOrderSemiProduct.setStatus(StringUtils.isEmpty(mesSfcRepairOrderSemiProduct.getStatus())?1: mesSfcRepairOrderSemiProduct.getStatus());
                mesSfcRepairOrderSemiProduct.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderSemiProductMapper.insertList(mesSfcRepairOrderSemiProductList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MesSfcRepairOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        ifCodeRepeat(entity,user);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = mesSfcRepairOrderMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        MesSfcHtRepairOrder mesSfcHtRepairOrder = new MesSfcHtRepairOrder();
        BeanUtils.copyProperties(entity, mesSfcHtRepairOrder);
        mesSfcHtRepairOrderMapper.insertSelective(mesSfcHtRepairOrder);

        //删除原不良现象
        Example example1 = new Example(MesSfcRepairOrderBadPhenotype.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("repairOrderId", entity.getRepairOrderId());
        List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypes = mesSfcRepairOrderBadPhenotypeMapper.selectByExample(example1);
        for (MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype : mesSfcRepairOrderBadPhenotypes){
            Example example2 = new Example(MesSfcRepairOrderBadPhenotypeRepair.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("repairOrderBadPhenotypeId",mesSfcRepairOrderBadPhenotype.getRepairOrderBadPhenotypeId());
            mesSfcRepairOrderBadPhenotypeRepairMapper.deleteByExample(example2);
        }
        mesSfcRepairOrderBadPhenotypeMapper.deleteByExample(example1);

        //不良现象
        List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypeList = entity.getMesSfcRepairOrderBadPhenotypeList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeList)){
            for (MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype : mesSfcRepairOrderBadPhenotypeList){
                mesSfcRepairOrderBadPhenotype.setRepairOrderId(entity.getRepairOrderId());
                mesSfcRepairOrderBadPhenotype.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotype.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotype.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotype.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotype.getStatus())?1: mesSfcRepairOrderBadPhenotype.getStatus());
                mesSfcRepairOrderBadPhenotype.setOrgId(user.getOrganizationId());
                mesSfcRepairOrderBadPhenotypeMapper.insertUseGeneratedKeys(mesSfcRepairOrderBadPhenotype);
                //保存维修信息
                saveBadPhenotypeRepair(mesSfcRepairOrderBadPhenotype,user);
            }
        }

        //删除原半成品
        Example example3 = new Example(MesSfcRepairOrderSemiProduct.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("repairOrderId", entity.getRepairOrderId());
        mesSfcRepairOrderSemiProductMapper.deleteByExample(example3);

        //半成品
        List<MesSfcRepairOrderSemiProduct> mesSfcRepairOrderSemiProductList = entity.getMesSfcRepairOrderSemiProductList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderSemiProductList)){
            for (MesSfcRepairOrderSemiProduct mesSfcRepairOrderSemiProduct : mesSfcRepairOrderSemiProductList){
                mesSfcRepairOrderSemiProduct.setRepairOrderId(entity.getRepairOrderId());
                mesSfcRepairOrderSemiProduct.setCreateUserId(user.getUserId());
                mesSfcRepairOrderSemiProduct.setCreateTime(new Date());
                mesSfcRepairOrderSemiProduct.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderSemiProduct.setModifiedTime(new Date());
                mesSfcRepairOrderSemiProduct.setStatus(StringUtils.isEmpty(mesSfcRepairOrderSemiProduct.getStatus())?1: mesSfcRepairOrderSemiProduct.getStatus());
                mesSfcRepairOrderSemiProduct.setOrgId(user.getOrganizationId());
            }
            mesSfcRepairOrderSemiProductMapper.insertList(mesSfcRepairOrderSemiProductList);
        }

        return i;
    }


    public void ifCodeRepeat(MesSfcRepairOrder mesSfcRepairOrder, SysUser user){
        Example example = new Example(MesSfcRepairOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("repairOrderCode",mesSfcRepairOrder.getRepairOrderCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if(StringUtils.isNotEmpty(mesSfcRepairOrder.getRepairOrderId())){
            criteria.andNotEqualTo("repairOrderId",mesSfcRepairOrder.getRepairOrderId());
        }
        MesSfcRepairOrder repairOrder = mesSfcRepairOrderMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(repairOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }


    public int saveBadPhenotypeRepair(MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype, SysUser user){
        int i = 0;

        List<MesSfcRepairOrderBadPhenotypeRepair> mesSfcRepairOrderBadPhenotypeRepairList = mesSfcRepairOrderBadPhenotype.getMesSfcRepairOrderBadPhenotypeRepairList();
        if(StringUtils.isNotEmpty(mesSfcRepairOrderBadPhenotypeRepairList)) {
            for (MesSfcRepairOrderBadPhenotypeRepair mesSfcRepairOrderBadPhenotypeRepair : mesSfcRepairOrderBadPhenotypeRepairList) {
                mesSfcRepairOrderBadPhenotypeRepair.setRepairOrderBadPhenotypeId(mesSfcRepairOrderBadPhenotype.getRepairOrderBadPhenotypeId());
                mesSfcRepairOrderBadPhenotypeRepair.setCreateUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotypeRepair.setCreateTime(new Date());
                mesSfcRepairOrderBadPhenotypeRepair.setModifiedUserId(user.getUserId());
                mesSfcRepairOrderBadPhenotypeRepair.setModifiedTime(new Date());
                mesSfcRepairOrderBadPhenotypeRepair.setStatus(StringUtils.isEmpty(mesSfcRepairOrderBadPhenotypeRepair.getStatus()) ? 1 : mesSfcRepairOrderBadPhenotypeRepair.getStatus());
                mesSfcRepairOrderBadPhenotypeRepair.setOrgId(user.getOrganizationId());
            }
            i = mesSfcRepairOrderBadPhenotypeRepairMapper.insertList(mesSfcRepairOrderBadPhenotypeRepairList);
        }

        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<MesSfcHtRepairOrder> htList = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            MesSfcRepairOrder mesSfcRepairOrder = mesSfcRepairOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(mesSfcRepairOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            MesSfcHtRepairOrder mesSfcHtRepairOrder = new MesSfcHtRepairOrder();
            BeanUtils.copyProperties(mesSfcRepairOrder, mesSfcHtRepairOrder);
            htList.add(mesSfcHtRepairOrder);

            //删除不良现象
            Example example1 = new Example(MesSfcRepairOrderBadPhenotype.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("repairOrderId", id);
            List<MesSfcRepairOrderBadPhenotype> mesSfcRepairOrderBadPhenotypes = mesSfcRepairOrderBadPhenotypeMapper.selectByExample(example1);
            for (MesSfcRepairOrderBadPhenotype mesSfcRepairOrderBadPhenotype : mesSfcRepairOrderBadPhenotypes){
                Example example2 = new Example(MesSfcRepairOrderBadPhenotypeRepair.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("repairOrderBadPhenotypeId",mesSfcRepairOrderBadPhenotype.getRepairOrderBadPhenotypeId());
                mesSfcRepairOrderBadPhenotypeRepairMapper.deleteByExample(example2);
            }
            mesSfcRepairOrderBadPhenotypeMapper.deleteByExample(example1);

            //删除半成品
            Example example3 = new Example(MesSfcRepairOrderSemiProduct.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("repairOrderId",id);
            mesSfcRepairOrderSemiProductMapper.deleteByExample(example3);
        }

        mesSfcHtRepairOrderMapper.insertList(htList);

        return mesSfcRepairOrderMapper.deleteByIds(ids);
    }
}

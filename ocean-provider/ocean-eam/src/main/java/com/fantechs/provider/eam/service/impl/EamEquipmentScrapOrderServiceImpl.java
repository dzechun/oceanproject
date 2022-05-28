package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentScrapOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquipmentScrapOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentScrapOrderServiceImpl extends BaseService<EamEquipmentScrapOrder> implements EamEquipmentScrapOrderService {

    @Resource
    private EamEquipmentScrapOrderMapper eamEquipmentScrapOrderMapper;
    @Resource
    private EamEquipmentScrapOrderDetMapper eamEquipmentScrapOrderDetMapper;
    @Resource
    private EamHtEquipmentScrapOrderMapper eamHtEquipmentScrapOrderMapper;
    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public List<EamEquipmentScrapOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        
        return eamEquipmentScrapOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoCreateOrder() {
        int sum = 0;

        //设备达到最大次数或天数是否可以继续使用
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("IsEquipmentCanContinueUse");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 0) {

            Map<String, Object> map = new HashMap<>();
            List<EamEquipmentBarcode> list = eamEquipmentBarcodeMapper.findList(map);
            for (EamEquipmentBarcode eamEquipmentBarcode : list) {
                boolean tag = false;
                EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(eamEquipmentBarcode.getEquipmentId());

                //设备当前使用次数(当前使用天数)大于等于该设备最大使用次数(最大使用天数)时创建报废单
                if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageTime(), eamEquipment.getMaxUsageTime())
                        && eamEquipment.getMaxUsageTime() != 0) {
                    if (eamEquipmentBarcode.getCurrentUsageTime().compareTo(eamEquipment.getMaxUsageTime()) == 0
                            || eamEquipmentBarcode.getCurrentUsageTime().compareTo(eamEquipment.getMaxUsageTime()) == 1) {
                        tag = true;
                    }
                }

                if (StringUtils.isNotEmpty(eamEquipmentBarcode.getCurrentUsageDays(), eamEquipment.getMaxUsageDays())
                        && eamEquipment.getMaxUsageDays() != 0) {
                    if (eamEquipmentBarcode.getCurrentUsageDays().compareTo(eamEquipment.getMaxUsageDays()) == 0
                            || eamEquipmentBarcode.getCurrentUsageDays().compareTo(eamEquipment.getMaxUsageDays()) == 1) {
                        tag = true;
                    }
                }

                //创建单据
                if (tag) {
                    EamEquipmentScrapOrderDto eamEquipmentScrapOrderDto = new EamEquipmentScrapOrderDto();
                    EamEquipmentScrapOrderDetDto eamEquipmentScrapOrderDetDto = new EamEquipmentScrapOrderDetDto();
                    List<EamEquipmentScrapOrderDetDto> eamEquipmentScrapOrderDetDtos = new ArrayList<>();

                    eamEquipmentScrapOrderDetDto.setEquipmentBarcodeId(eamEquipmentBarcode.getEquipmentBarcodeId());
                    eamEquipmentScrapOrderDetDtos.add(eamEquipmentScrapOrderDetDto);

                    eamEquipmentScrapOrderDto.setOrderStatus((byte) 1);
                    eamEquipmentScrapOrderDto.setList(eamEquipmentScrapOrderDetDtos);

                    sum += this.save(eamEquipmentScrapOrderDto);
                }
            }
        }

        return sum;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentScrapOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //this.codeIfRepeat(record);

        record.setEquipmentScrapOrderCode(CodeUtils.getId("SBBF-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus(StringUtils.isEmpty(record.getOrderStatus())?1: record.getOrderStatus());
        eamEquipmentScrapOrderMapper.insertUseGeneratedKeys(record);

        List<EamEquipmentScrapOrderDetDto> list = record.getList();
        //确认报废单时修改设备状态为报废
        if(record.getOrderStatus() == 2){
            updateEquipmentStatus(list);
        }

        //报废单明细
        if(StringUtils.isNotEmpty(list)){
            for (EamEquipmentScrapOrderDetDto eamEquipmentScrapOrderDetDto : list){
                eamEquipmentScrapOrderDetDto.setEquipmentScrapOrderId(record.getEquipmentScrapOrderId());
                eamEquipmentScrapOrderDetDto.setCreateUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setCreateTime(new Date());
                eamEquipmentScrapOrderDetDto.setModifiedUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setModifiedTime(new Date());
                eamEquipmentScrapOrderDetDto.setStatus(StringUtils.isEmpty(eamEquipmentScrapOrderDetDto.getStatus())?1: eamEquipmentScrapOrderDetDto.getStatus());
                eamEquipmentScrapOrderDetDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentScrapOrderDetMapper.insertList(list);
        }

        EamHtEquipmentScrapOrder eamHtEquipmentScrapOrder = new EamHtEquipmentScrapOrder();
        BeanUtils.copyProperties(record,eamHtEquipmentScrapOrder);
        int i = eamHtEquipmentScrapOrderMapper.insertSelective(eamHtEquipmentScrapOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentScrapOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamEquipmentScrapOrderMapper.updateByPrimaryKeySelective(entity);

        List<EamEquipmentScrapOrderDetDto> list = entity.getList();
        //确认报废单时修改设备状态为报废
        if(entity.getOrderStatus() == 2){
            updateEquipmentStatus(list);
        }

        //删除原报废单明细
        Example example1 = new Example(EamEquipmentScrapOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentScrapOrderId", entity.getEquipmentScrapOrderId());
        eamEquipmentScrapOrderDetMapper.deleteByExample(example1);

        //报废单明细
        if(StringUtils.isNotEmpty(list)){
            for (EamEquipmentScrapOrderDetDto eamEquipmentScrapOrderDetDto : list){
                eamEquipmentScrapOrderDetDto.setEquipmentScrapOrderId(entity.getEquipmentScrapOrderId());
                eamEquipmentScrapOrderDetDto.setCreateUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setCreateTime(new Date());
                eamEquipmentScrapOrderDetDto.setModifiedUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setModifiedTime(new Date());
                eamEquipmentScrapOrderDetDto.setStatus(StringUtils.isEmpty(eamEquipmentScrapOrderDetDto.getStatus())?1: eamEquipmentScrapOrderDetDto.getStatus());
                eamEquipmentScrapOrderDetDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentScrapOrderDetMapper.insertList(list);
        }

        EamHtEquipmentScrapOrder eamHtEquipmentScrapOrder = new EamHtEquipmentScrapOrder();
        BeanUtils.copyProperties(entity,eamHtEquipmentScrapOrder);
        int i = eamHtEquipmentScrapOrderMapper.insertSelective(eamHtEquipmentScrapOrder);

        return i;
    }

    public int updateEquipmentStatus(List<EamEquipmentScrapOrderDetDto> list){
        int i = 0;
        for (EamEquipmentScrapOrderDetDto eamEquipmentScrapOrderDetDto : list){
            EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodeMapper.selectByPrimaryKey(eamEquipmentScrapOrderDetDto.getEquipmentBarcodeId());
            eamEquipmentBarcode.setEquipmentStatus((byte)9);
            i += eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
        }
        return i;
    }

    private void codeIfRepeat(EamEquipmentScrapOrderDto eamEquipmentScrapOrderDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断编码是否重复
        Example example = new Example(EamEquipmentScrapOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("equipmentScrapOrderCode",eamEquipmentScrapOrderDto.getEquipmentScrapOrderCode());
        if (StringUtils.isNotEmpty(eamEquipmentScrapOrderDto.getEquipmentScrapOrderId())){
            criteria.andNotEqualTo("equipmentScrapOrderId",eamEquipmentScrapOrderDto.getEquipmentScrapOrderId());
        }

        EamEquipmentScrapOrder eamEquipmentScrapOrder = eamEquipmentScrapOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentScrapOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtEquipmentScrapOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamEquipmentScrapOrder eamEquipmentScrapOrder = eamEquipmentScrapOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamEquipmentScrapOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentScrapOrder eamHtEquipmentScrapOrder = new EamHtEquipmentScrapOrder();
            BeanUtils.copyProperties(eamEquipmentScrapOrder,eamHtEquipmentScrapOrder);
            htList.add(eamHtEquipmentScrapOrder);

            //删除报废单明细
            Example example1 = new Example(EamEquipmentScrapOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentScrapOrderId", id);
            eamEquipmentScrapOrderDetMapper.deleteByExample(example1);
        }

        eamHtEquipmentScrapOrderMapper.insertList(htList);

        return eamEquipmentScrapOrderMapper.deleteByIds(ids);
    }
}

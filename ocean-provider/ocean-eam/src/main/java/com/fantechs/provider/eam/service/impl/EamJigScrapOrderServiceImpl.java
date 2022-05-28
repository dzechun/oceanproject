package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrder;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigScrapOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigScrapOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/19.
 */
@Service
public class EamJigScrapOrderServiceImpl extends BaseService<EamJigScrapOrder> implements EamJigScrapOrderService {

    @Resource
    private EamJigScrapOrderMapper eamJigScrapOrderMapper;
    @Resource
    private EamJigScrapOrderDetMapper eamJigScrapOrderDetMapper;
    @Resource
    private EamHtJigScrapOrderMapper eamHtJigScrapOrderMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public List<EamJigScrapOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamJigScrapOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoCreateOrder() {
        int sum = 0;

        //治具达到最大次数或天数是否可以继续使用
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("IsJigCanContinueUse");
        List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 0) {

            Map<String, Object> map = new HashMap<>();
            List<EamJigBarcodeDto> list = eamJigBarcodeMapper.findList(map);
            for (EamJigBarcodeDto eamJigBarcodeDto : list) {
                boolean tag = false;
                EamJig eamJig = eamJigMapper.selectByPrimaryKey(eamJigBarcodeDto.getJigId());
                //治具当前使用次数(当前使用天数)大于等于该治具最大使用次数(最大使用天数)时创建报废单
                if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentUsageTime(), eamJig.getMaxUsageTime())
                        && eamJig.getMaxUsageTime() != 0) {
                    if (eamJigBarcodeDto.getCurrentUsageTime().compareTo(eamJig.getMaxUsageTime()) == 0
                            || eamJigBarcodeDto.getCurrentUsageTime().compareTo(eamJig.getMaxUsageTime()) == 1) {
                        tag = true;
                    }
                }

                if (StringUtils.isNotEmpty(eamJigBarcodeDto.getCurrentUsageDays(), eamJig.getMaxUsageDays())
                        && eamJig.getMaxUsageDays() != 0) {
                    if (eamJigBarcodeDto.getCurrentUsageDays().compareTo(eamJig.getMaxUsageDays()) == 0
                            || eamJigBarcodeDto.getCurrentUsageDays().compareTo(eamJig.getMaxUsageDays()) == 1) {
                        tag = true;
                    }
                }

                //创建单据
                if (tag) {
                    EamJigScrapOrderDto eamJigScrapOrderDto = new EamJigScrapOrderDto();
                    EamJigScrapOrderDetDto eamJigScrapOrderDetDto = new EamJigScrapOrderDetDto();
                    List<EamJigScrapOrderDetDto> eamJigScrapOrderDetDtos = new ArrayList<>();

                    eamJigScrapOrderDetDto.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
                    eamJigScrapOrderDetDtos.add(eamJigScrapOrderDetDto);

                    eamJigScrapOrderDto.setOrderStatus((byte) 1);
                    eamJigScrapOrderDto.setList(eamJigScrapOrderDetDtos);

                    sum += this.save(eamJigScrapOrderDto);
                }
            }
        }

        return sum;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigScrapOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //this.codeIfRepeat(record);

        record.setJigScrapOrderCode(CodeUtils.getId("ZJBF-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus(StringUtils.isEmpty(record.getOrderStatus())?1: record.getOrderStatus());
        eamJigScrapOrderMapper.insertUseGeneratedKeys(record);

        List<EamJigScrapOrderDetDto> list = record.getList();
        //确认报废单时修改治具使用状态为报废
        if(record.getOrderStatus() == 2){
            updateJigUsageStatus(list);
        }

        //报废单明细
        if(StringUtils.isNotEmpty(list)){
            for (EamJigScrapOrderDetDto eamJigScrapOrderDetDto : list){
                eamJigScrapOrderDetDto.setJigScrapOrderId(record.getJigScrapOrderId());
                eamJigScrapOrderDetDto.setCreateUserId(user.getUserId());
                eamJigScrapOrderDetDto.setCreateTime(new Date());
                eamJigScrapOrderDetDto.setModifiedUserId(user.getUserId());
                eamJigScrapOrderDetDto.setModifiedTime(new Date());
                eamJigScrapOrderDetDto.setStatus(StringUtils.isEmpty(eamJigScrapOrderDetDto.getStatus())?1: eamJigScrapOrderDetDto.getStatus());
                eamJigScrapOrderDetDto.setOrgId(user.getOrganizationId());
            }
            eamJigScrapOrderDetMapper.insertList(list);
        }

        EamHtJigScrapOrder eamHtJigScrapOrder = new EamHtJigScrapOrder();
        BeanUtils.copyProperties(record,eamHtJigScrapOrder);
        int i = eamHtJigScrapOrderMapper.insertSelective(eamHtJigScrapOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigScrapOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigScrapOrderMapper.updateByPrimaryKeySelective(entity);

        List<EamJigScrapOrderDetDto> list = entity.getList();
        //确认报废单时修改治具使用状态为报废
        if(entity.getOrderStatus() == 2){
            updateJigUsageStatus(list);
        }

        //删除原报废单明细
        Example example1 = new Example(EamJigScrapOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigScrapOrderId", entity.getJigScrapOrderId());
        eamJigScrapOrderDetMapper.deleteByExample(example1);

        //报废单明细
        if(StringUtils.isNotEmpty(list)){
            for (EamJigScrapOrderDetDto eamJigScrapOrderDetDto : list){
                eamJigScrapOrderDetDto.setJigScrapOrderId(entity.getJigScrapOrderId());
                eamJigScrapOrderDetDto.setCreateUserId(user.getUserId());
                eamJigScrapOrderDetDto.setCreateTime(new Date());
                eamJigScrapOrderDetDto.setModifiedUserId(user.getUserId());
                eamJigScrapOrderDetDto.setModifiedTime(new Date());
                eamJigScrapOrderDetDto.setStatus(StringUtils.isEmpty(eamJigScrapOrderDetDto.getStatus())?1: eamJigScrapOrderDetDto.getStatus());
                eamJigScrapOrderDetDto.setOrgId(user.getOrganizationId());
            }
            eamJigScrapOrderDetMapper.insertList(list);
        }

        EamHtJigScrapOrder eamHtJigScrapOrder = new EamHtJigScrapOrder();
        BeanUtils.copyProperties(entity,eamHtJigScrapOrder);
        int i = eamHtJigScrapOrderMapper.insertSelective(eamHtJigScrapOrder);

        return i;
    }

    public int updateJigUsageStatus(List<EamJigScrapOrderDetDto> list){
        int i = 0;
        for (EamJigScrapOrderDetDto eamJigScrapOrderDetDto : list){
            EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectByPrimaryKey(eamJigScrapOrderDetDto.getJigBarcodeId());
            eamJigBarcode.setUsageStatus((byte)6);
            i += eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
        }
       return i;
    }

    private void codeIfRepeat(EamJigScrapOrderDto eamJigScrapOrderDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断编码是否重复
        Example example = new Example(EamJigScrapOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("jigScrapOrderCode",eamJigScrapOrderDto.getJigScrapOrderCode());
        if (StringUtils.isNotEmpty(eamJigScrapOrderDto.getJigScrapOrderId())){
            criteria.andNotEqualTo("jigScrapOrderId",eamJigScrapOrderDto.getJigScrapOrderId());
        }

        EamJigScrapOrder jigScrapOrder = eamJigScrapOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigScrapOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJigScrapOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigScrapOrder eamJigScrapOrder = eamJigScrapOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigScrapOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigScrapOrder eamHtJigScrapOrder = new EamHtJigScrapOrder();
            BeanUtils.copyProperties(eamJigScrapOrder,eamHtJigScrapOrder);
            htList.add(eamHtJigScrapOrder);

            //删除原报废单明细
            Example example1 = new Example(EamJigScrapOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigScrapOrderId", id);
            eamJigScrapOrderDetMapper.deleteByExample(example1);
        }

        eamHtJigScrapOrderMapper.insertList(htList);

        return eamJigScrapOrderMapper.deleteByIds(ids);
    }
}

package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigScrapOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
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
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<EamJigScrapOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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

                    eamJigScrapOrderDto.setJigScrapOrderCode(CodeUtils.getId("BF-"));
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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamJigScrapOrderMapper.insertUseGeneratedKeys(record);

        //报废单明细
        List<EamJigScrapOrderDetDto> list = record.getList();
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
        int i = eamHtJigScrapOrderMapper.insert(eamHtJigScrapOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigScrapOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigScrapOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原报废单明细
        Example example1 = new Example(EamJigScrapOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigScrapOrderId", entity.getJigScrapOrderId());
        eamJigScrapOrderDetMapper.deleteByExample(example1);

        //报废单明细
        List<EamJigScrapOrderDetDto> list = entity.getList();
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
        int i = eamHtJigScrapOrderMapper.insert(eamHtJigScrapOrder);

        return i;
    }

    private void codeIfRepeat(EamJigScrapOrderDto eamJigScrapOrderDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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

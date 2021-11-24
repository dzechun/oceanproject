package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.dto.srm.SrmCarportTimeQuantumDto;
import com.fantechs.common.base.general.dto.srm.SrmDeliveryAppointDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryAppoint;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmCarportTimeQuantum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.srm.mapper.SrmAppointDeliveryReAsnMapper;
import com.fantechs.provider.srm.mapper.SrmCarportTimeQuantumMapper;
import com.fantechs.provider.srm.mapper.SrmDeliveryAppointMapper;
import com.fantechs.provider.srm.mapper.SrmHtDeliveryAppointMapper;
import com.fantechs.provider.srm.service.SrmDeliveryAppointService;
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
 * Created by leifengzhi on 2021/11/24.
 */
@Service
public class SrmDeliveryAppointServiceImpl extends BaseService<SrmDeliveryAppoint> implements SrmDeliveryAppointService {

    @Resource
    private SrmDeliveryAppointMapper srmDeliveryAppointMapper;
    @Resource
    private SrmHtDeliveryAppointMapper srmHtDeliveryAppointMapper;
    @Resource
    private SrmAppointDeliveryReAsnMapper srmAppointDeliveryReAsnMapper;
    @Resource
    private SrmCarportTimeQuantumMapper srmCarportTimeQuantumMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<SrmDeliveryAppointDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(sysUser.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if (StringUtils.isNotEmpty(list.getData())){
            map.put("supplierIdList", list.getData());
        }
        return srmDeliveryAppointMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SrmDeliveryAppointDto srmDeliveryAppointDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSrmCarportTimeQuantum searchSrmCarportTimeQuantum = new SearchSrmCarportTimeQuantum();
        searchSrmCarportTimeQuantum.setStartTime(DateUtils.getDateTimeString(srmDeliveryAppointDto.getAppointStartTime()));
        searchSrmCarportTimeQuantum.setEndTime(DateUtils.getDateTimeString(srmDeliveryAppointDto.getAppointEndTime()));
        searchSrmCarportTimeQuantum.setWarehouseId(srmDeliveryAppointDto.getDeliveryWarehouseId());
        List<SrmCarportTimeQuantumDto> srmCarportTimeQuantumDtos = srmCarportTimeQuantumMapper.findList(ControllerUtil.dynamicConditionByEntity(searchSrmCarportTimeQuantum));

        Example example = new Example(SrmDeliveryAppoint.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appointStartTime",srmDeliveryAppointDto.getAppointStartTime());
        criteria.andEqualTo("appointEndTime",srmDeliveryAppointDto.getAppointEndTime());
        int num = srmDeliveryAppointMapper.selectCountByExample(example);
        if(num > srmCarportTimeQuantumDtos.get(0).getCarportCount())
            throw new BizErrorException("该时间段预约已满");

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("isOrderCensor");
        List<SysSpecItem> specItemLists = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(StringUtils.isNotEmpty(specItemLists) && "1".equals(specItemLists.get(0).getParaValue())){
            srmDeliveryAppointDto.setAppointStatus((byte)1);
        }else{
            srmDeliveryAppointDto.setAppointStatus((byte)3);
        }

        srmDeliveryAppointDto.setCreateUserId(user.getUserId());
        srmDeliveryAppointDto.setCreateTime(new Date());
        srmDeliveryAppointDto.setModifiedUserId(user.getUserId());
        srmDeliveryAppointDto.setModifiedTime(new Date());
        srmDeliveryAppointDto.setStatus(StringUtils.isEmpty(srmDeliveryAppointDto.getStatus())?1: srmDeliveryAppointDto.getStatus());
        srmDeliveryAppointDto.setOrgId(user.getOrganizationId());
        srmDeliveryAppointMapper.insertUseGeneratedKeys(srmDeliveryAppointDto);

        List<SrmAppointDeliveryReAsnDto> list = new ArrayList<>();
        for(SrmAppointDeliveryReAsnDto srmAppointDeliveryReAsnDto:srmDeliveryAppointDto.getList()){
            srmAppointDeliveryReAsnDto.setCreateUserId(user.getUserId());
            srmAppointDeliveryReAsnDto.setCreateTime(new Date());
            srmAppointDeliveryReAsnDto.setModifiedUserId(user.getUserId());
            srmAppointDeliveryReAsnDto.setModifiedTime(new Date());
            srmAppointDeliveryReAsnDto.setStatus(StringUtils.isEmpty(srmDeliveryAppointDto.getStatus())?1: srmDeliveryAppointDto.getStatus());
            srmAppointDeliveryReAsnDto.setOrgId(user.getOrganizationId());
            srmAppointDeliveryReAsnDto.setDeliveryAppointId(srmDeliveryAppointDto.getDeliveryAppointId());
            list.add(srmAppointDeliveryReAsnDto);
        }
        if(StringUtils.isNotEmpty(list)) srmAppointDeliveryReAsnMapper.insertList(list);


        //保存履历表
        SrmHtDeliveryAppoint srmHtDeliveryAppoint = new SrmHtDeliveryAppoint();
        BeanUtils.copyProperties(srmDeliveryAppointDto, srmHtDeliveryAppoint);
        int i = srmHtDeliveryAppointMapper.insertSelective(srmHtDeliveryAppoint);

        return i;
    }

    @Override
    public int update(SrmDeliveryAppointDto srmDeliveryAppointDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();


        srmDeliveryAppointDto.setModifiedTime(new Date());
        srmDeliveryAppointDto.setModifiedUserId(user.getUserId());
        srmDeliveryAppointMapper.updateByPrimaryKeySelective(srmDeliveryAppointDto);

        //删除子表重新添加
        Example example = new Example(SrmAppointDeliveryReAsn.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deliveryAppointId",srmDeliveryAppointDto.getDeliveryAppointId());
        srmCarportTimeQuantumMapper.deleteByExample(example);

        List<SrmAppointDeliveryReAsnDto> list = new ArrayList<>();
        for(SrmAppointDeliveryReAsnDto srmAppointDeliveryReAsnDto:srmDeliveryAppointDto.getList()){
            srmAppointDeliveryReAsnDto.setCreateUserId(user.getUserId());
            srmAppointDeliveryReAsnDto.setCreateTime(new Date());
            srmAppointDeliveryReAsnDto.setModifiedUserId(user.getUserId());
            srmAppointDeliveryReAsnDto.setModifiedTime(new Date());
            srmAppointDeliveryReAsnDto.setStatus(StringUtils.isEmpty(srmDeliveryAppointDto.getStatus())?1: srmDeliveryAppointDto.getStatus());
            srmAppointDeliveryReAsnDto.setOrgId(user.getOrganizationId());
            srmAppointDeliveryReAsnDto.setDeliveryAppointId(srmDeliveryAppointDto.getDeliveryAppointId());
            list.add(srmAppointDeliveryReAsnDto);
        }
        if(StringUtils.isNotEmpty(list)) srmAppointDeliveryReAsnMapper.insertList(list);

        //保存履历表
        SrmHtDeliveryAppoint srmHtDeliveryAppoint = new SrmHtDeliveryAppoint();
        BeanUtils.copyProperties(srmDeliveryAppointDto, srmHtDeliveryAppoint);
        int i = srmHtDeliveryAppointMapper.insertSelective(srmHtDeliveryAppoint);

        return i;
    }

}

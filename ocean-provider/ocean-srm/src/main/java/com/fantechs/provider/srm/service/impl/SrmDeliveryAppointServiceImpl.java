package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.dto.srm.SrmCarportTimeQuantumDto;
import com.fantechs.common.base.general.dto.srm.SrmDeliveryAppointDto;
import com.fantechs.common.base.general.dto.srm.SrmHtAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
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
import com.fantechs.provider.srm.mapper.*;
import com.fantechs.provider.srm.service.SrmDeliveryAppointService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
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
    private SrmHtAppointDeliveryReAsnMapper srmHtAppointDeliveryReAsnMapper;
    @Resource
    private SrmCarportTimeQuantumMapper srmCarportTimeQuantumMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SrmInAsnOrderMapper srmInAsnOrderMapper;

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
    public int save(SrmDeliveryAppointDto srmDeliveryAppointDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSrmCarportTimeQuantum searchSrmCarportTimeQuantum = new SearchSrmCarportTimeQuantum();
      /*  searchSrmCarportTimeQuantum.setCarportStartTime(srmDeliveryAppointDto.getAppointStartTime());
        searchSrmCarportTimeQuantum.setCarportEndTime(srmDeliveryAppointDto.getAppointEndTime());*/
        searchSrmCarportTimeQuantum.setWarehouseId(srmDeliveryAppointDto.getDeliveryWarehouseId());
        List<SrmCarportTimeQuantumDto> srmCarportTimeQuantumDtos = srmCarportTimeQuantumMapper.findList(ControllerUtil.dynamicConditionByEntity(searchSrmCarportTimeQuantum));

        if(StringUtils.isEmpty(srmDeliveryAppointDto.getAppointDate()) || StringUtils.isEmpty(srmDeliveryAppointDto.getAppointDate())
        || StringUtils.isEmpty(srmDeliveryAppointDto.getAppointDate()) )
            throw new BizErrorException("预约时间不能为空");
        //拼接时间
        try {
            String date = DateUtils.getDateString(srmDeliveryAppointDto.getAppointDate(),"yyyy-MM-dd");
            String start =  DateUtils.getDateString(srmDeliveryAppointDto.getAppointStartTime(),"HH:mm:ss");
            String end =  DateUtils.getDateString(srmDeliveryAppointDto.getAppointEndTime(),"HH:mm:ss");

            srmDeliveryAppointDto.setAppointStartTime(DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss",date+" "+start));
            srmDeliveryAppointDto.setAppointEndTime(DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss",date+" "+end));
        } catch (ParseException e) {
            throw new BizErrorException("时间格式转换错误");
        }

        //预约数量校验
        Example example = new Example(SrmDeliveryAppoint.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appointStartTime",srmDeliveryAppointDto.getAppointStartTime());
        criteria.andEqualTo("appointEndTime",srmDeliveryAppointDto.getAppointEndTime());
        Integer num = srmDeliveryAppointMapper.selectCountByExample(example);

        SrmCarportTimeQuantumDto srmCarportTimeQuantumDto = null;
        for(SrmCarportTimeQuantumDto dto :srmCarportTimeQuantumDtos){
            Date startTime = dto.getStartTime();
            Date endTime = dto.getEndTime();
            String str1 =  DateUtils.getDateString(startTime,"HH:mm:ss");
            String str2 =  DateUtils.getDateString(srmDeliveryAppointDto.getAppointStartTime(),"HH:mm:ss");
            if(str1.equals(str2))
                srmCarportTimeQuantumDto = dto;
        }
        if(StringUtils.isEmpty(srmCarportTimeQuantumDto))
            throw  new  BizErrorException("未查询到对应时间段的仓库信息");
        Integer size = srmCarportTimeQuantumDto.getCarportCount();
        if(num >= size )
            throw new BizErrorException("该时间段预约已满");

        //保存供应商
        if(StringUtils.isEmpty(srmDeliveryAppointDto.getSupplierId()))   throw new BizErrorException("供应商id不能为空");
        SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
        searchBaseSupplier.setSupplierId(srmDeliveryAppointDto.getSupplierId());
        List<BaseSupplier> baseSupplier = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
        if(baseSupplier.get(0).getIfAppointDeliver() == 0)
            throw new BizErrorException("该供应商不需要预约");


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
        srmDeliveryAppointDto.setAppointStatus((byte)1);
        srmDeliveryAppointMapper.insertUseGeneratedKeys(srmDeliveryAppointDto);

        //保存履历表
        SrmHtDeliveryAppoint srmHtDeliveryAppoint = new SrmHtDeliveryAppoint();
        BeanUtils.copyProperties(srmDeliveryAppointDto, srmHtDeliveryAppoint);
        int i = srmHtDeliveryAppointMapper.insertUseGeneratedKeys(srmHtDeliveryAppoint);

        if(StringUtils.isNotEmpty(srmDeliveryAppointDto.getList())) {
            List<SrmAppointDeliveryReAsnDto> list = new ArrayList<>();
            List<SrmHtAppointDeliveryReAsnDto> htList = new ArrayList<>();
            for (SrmAppointDeliveryReAsnDto srmAppointDeliveryReAsnDto : srmDeliveryAppointDto.getList()) {
                srmAppointDeliveryReAsnDto.setCreateUserId(user.getUserId());
                srmAppointDeliveryReAsnDto.setCreateTime(new Date());
                srmAppointDeliveryReAsnDto.setModifiedUserId(user.getUserId());
                srmAppointDeliveryReAsnDto.setModifiedTime(new Date());
                srmAppointDeliveryReAsnDto.setStatus(StringUtils.isEmpty(srmDeliveryAppointDto.getStatus()) ? 1 : srmDeliveryAppointDto.getStatus());
                srmAppointDeliveryReAsnDto.setOrgId(user.getOrganizationId());
                srmAppointDeliveryReAsnDto.setDeliveryAppointId(srmDeliveryAppointDto.getDeliveryAppointId());
                list.add(srmAppointDeliveryReAsnDto);

                SrmHtAppointDeliveryReAsnDto srmHtAppointDeliveryReAsnDto = new SrmHtAppointDeliveryReAsnDto();
                BeanUtils.copyProperties(srmAppointDeliveryReAsnDto, srmHtAppointDeliveryReAsnDto);
                htList.add(srmHtAppointDeliveryReAsnDto);
            }
            if (StringUtils.isNotEmpty(list)) srmAppointDeliveryReAsnMapper.insertList(list);
            if (StringUtils.isNotEmpty(htList)) srmHtAppointDeliveryReAsnMapper.insertList(htList);
        }


        return i;
    }

    @Override
    public int update(SrmDeliveryAppointDto srmDeliveryAppointDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        srmDeliveryAppointDto.setModifiedTime(new Date());
        srmDeliveryAppointDto.setModifiedUserId(user.getUserId());

        //判断取消预约审核
        if(srmDeliveryAppointDto.getAppointStatus() == 4 ) {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("isCensor");
            List<SysSpecItem> specItemLists = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (StringUtils.isNotEmpty(specItemLists) && "1".equals(specItemLists.get(0).getParaValue())) {
                srmDeliveryAppointDto.setAppointStatus((byte) 4);
            } else {
                srmDeliveryAppointDto.setAppointStatus((byte) 5);
            }
        }
        srmDeliveryAppointMapper.updateByPrimaryKeySelective(srmDeliveryAppointDto);

        //保存履历表
        SrmHtDeliveryAppoint srmHtDeliveryAppoint = new SrmHtDeliveryAppoint();
        BeanUtils.copyProperties(srmDeliveryAppointDto, srmHtDeliveryAppoint);
        int i = srmHtDeliveryAppointMapper.insertUseGeneratedKeys(srmHtDeliveryAppoint);


        //删除子表重新添加
        if(StringUtils.isNotEmpty(srmDeliveryAppointDto.getList())) {
            Example example = new Example(SrmAppointDeliveryReAsn.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("deliveryAppointId",srmDeliveryAppointDto.getDeliveryAppointId());
            srmAppointDeliveryReAsnMapper.deleteByExample(example);

            List<SrmAppointDeliveryReAsnDto> list = new ArrayList<>();
            List<SrmHtAppointDeliveryReAsnDto> htList = new ArrayList<>();
            for (SrmAppointDeliveryReAsnDto srmAppointDeliveryReAsnDto : srmDeliveryAppointDto.getList()) {
                srmAppointDeliveryReAsnDto.setCreateUserId(user.getUserId());
                srmAppointDeliveryReAsnDto.setCreateTime(new Date());
                srmAppointDeliveryReAsnDto.setModifiedUserId(user.getUserId());
                srmAppointDeliveryReAsnDto.setModifiedTime(new Date());
                srmAppointDeliveryReAsnDto.setStatus(StringUtils.isEmpty(srmDeliveryAppointDto.getStatus()) ? 1 : srmDeliveryAppointDto.getStatus());
                srmAppointDeliveryReAsnDto.setOrgId(user.getOrganizationId());
                srmAppointDeliveryReAsnDto.setDeliveryAppointId(srmDeliveryAppointDto.getDeliveryAppointId());
                list.add(srmAppointDeliveryReAsnDto);

                SrmHtAppointDeliveryReAsnDto srmHtAppointDeliveryReAsnDto = new SrmHtAppointDeliveryReAsnDto();
                BeanUtils.copyProperties(srmAppointDeliveryReAsnDto, srmHtAppointDeliveryReAsnDto);
                htList.add(srmHtAppointDeliveryReAsnDto);
            }
            if (StringUtils.isNotEmpty(list))  srmAppointDeliveryReAsnMapper.insertList(list);
            if (StringUtils.isNotEmpty(htList)) srmHtAppointDeliveryReAsnMapper.insertList(htList);
            //反写预收货通知单
            if(srmDeliveryAppointDto.getAppointStatus() == 3 || srmDeliveryAppointDto.getAppointStatus() == 4 || srmDeliveryAppointDto.getAppointStatus() == 5){
                for(SrmAppointDeliveryReAsnDto srmAppointDeliveryReAsnDto : list) {
                    Example example1 = new Example(SrmInAsnOrder.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("asnCode", srmAppointDeliveryReAsnDto.getAsnCode());
                    List<SrmInAsnOrder> srmInAsnOrders = srmInAsnOrderMapper.selectByExample(example1);
                    if(StringUtils.isEmpty(srmInAsnOrders))  throw new BizErrorException("未查询到对应的asn单号");
                    SrmInAsnOrder order = srmInAsnOrders.get(0);

                    if(srmDeliveryAppointDto.getAppointStatus() == 3) {
                        order.setOrderStatus((byte) 5);
                        srmInAsnOrderMapper.updateByPrimaryKeySelective(order);
                    }
                    if(srmDeliveryAppointDto.getAppointStatus() == 4 &&  order.getOrderStatus() == 6  ){
                        throw new BizErrorException("ASN单已发货，无法取消");
                    }
                    if(srmDeliveryAppointDto.getAppointStatus() == 5){
                        order.setOrderStatus((byte)3);
                        srmInAsnOrderMapper.updateByPrimaryKeySelective(order);
                    }

                }
            }
        }

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            Example example = new Example(SrmAppointDeliveryReAsn.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("deliveryAppointId",id);
            srmAppointDeliveryReAsnMapper.deleteByExample(example);
        }
        int i = srmDeliveryAppointMapper.deleteByIds(ids);
        return i;
    }

}

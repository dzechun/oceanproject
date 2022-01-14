package com.fantechs.provider.srm.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderType;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.*;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.srm.mapper.*;
import com.fantechs.provider.srm.service.SrmAppointDeliveryReAsnService;
import com.fantechs.provider.srm.service.SrmInAsnOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@Service
public class SrmInAsnOrderServiceImpl extends BaseService<SrmInAsnOrder> implements SrmInAsnOrderService {

    @Resource
    private SrmInAsnOrderMapper srmInAsnOrderMapper;
    @Resource
    private SrmInHtAsnOrderMapper srmInHtAsnOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SrmInAsnOrderDetMapper srmInAsnOrderDetMapper;
    @Resource
    private SrmInHtAsnOrderDetMapper srmInHtAsnOrderDetMapper;
    @Resource
    private SrmDeliveryAppointMapper srmDeliveryAppointMapper;
    @Resource
    private SrmAppointDeliveryReAsnService srmAppointDeliveryReAsnService;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private SrmPlanDeliveryOrderDetMapper srmPlanDeliveryOrderDetMapper;


    @Override
    public List<SrmInAsnOrderDto> findList(Map<String, Object> map) {
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
        map.put("orderTypeCode", "YSHTZD");
        return srmInAsnOrderMapper.findList(map);
    }



    @Override
    @LcnTransaction
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SrmInAsnOrderDto srmInAsnOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        srmInAsnOrderDto.setAsnCode(CodeUtils.getId("SRM-ASN"));

       // 默认收货通知单
        if(StringUtils.isEmpty(srmInAsnOrderDto.getOrderTypeId())) {
            SearchBaseOrderType searchBaseOrderType = new SearchBaseOrderType();
            searchBaseOrderType.setOrderTypeCode("YSHTZD");
            List<BaseOrderTypeDto> baseOrderTypeDtos = baseFeignApi.findList(searchBaseOrderType).getData();
            if (StringUtils.isEmpty(baseOrderTypeDtos)) throw new BizErrorException("未配置对应的单据类型");
            srmInAsnOrderDto.setOrderTypeId(baseOrderTypeDtos.get(0).getOrderTypeId());
        }

        //保存附件
        if(StringUtils.isNotEmpty(srmInAsnOrderDto.getFileUrl())) {
            BaseFile baseFile = new BaseFile();
            baseFile.setRelevanceTableName("wms_in_asn_order");
            baseFile.setAccessUrl(srmInAsnOrderDto.getFileUrl());
            BaseFile date = baseFeignApi.add(baseFile).getData();
            if (StringUtils.isEmpty(date)) throw new BizErrorException("文件保存失败");
            srmInAsnOrderDto.setFileId(date.getFileId());
        }
        srmInAsnOrderDto.setCreateUserId(user.getUserId());
        srmInAsnOrderDto.setCreateTime(new Date());
        srmInAsnOrderDto.setModifiedUserId(user.getUserId());
        srmInAsnOrderDto.setModifiedTime(new Date());
        srmInAsnOrderDto.setStatus(StringUtils.isEmpty(srmInAsnOrderDto.getStatus())?1: srmInAsnOrderDto.getStatus());
        srmInAsnOrderDto.setOrgId(user.getOrganizationId());

        //保存供应商
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(user.getUserId());
        List<BaseSupplierReUser> baseSupplierReUsers = baseFeignApi.findList(searchBaseSupplierReUser).getData();
        if(StringUtils.isNotEmpty(baseSupplierReUsers))
            srmInAsnOrderDto.setSupplierId(baseSupplierReUsers.get(0).getSupplierId());

        int i = srmInAsnOrderMapper.insertUseGeneratedKeys(srmInAsnOrderDto);


        //保存履历表
        SrmInHtAsnOrder srmInHtAsnOrder = new SrmInHtAsnOrder();
        BeanUtils.copyProperties(srmInAsnOrderDto, srmInHtAsnOrder);
        srmInHtAsnOrderMapper.insertUseGeneratedKeys(srmInHtAsnOrder);

        //保存详情表
        if(StringUtils.isNotEmpty(srmInAsnOrderDto.getSrmInAsnOrderDetDtos())) {
            List<SrmInAsnOrderDetDto> list = new ArrayList<>();
            List<SrmInHtAsnOrderDetDto> htList = new ArrayList<>();
            for (SrmInAsnOrderDetDto srmInAsnOrderDetDto : srmInAsnOrderDto.getSrmInAsnOrderDetDtos()) {
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getOrderQty()))
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"采购订单数量不能为空");

                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getDeliveryQty()))
                    srmInAsnOrderDetDto.setDeliveryQty(BigDecimal.ZERO);
                if(srmInAsnOrderDetDto.getDeliveryQty().compareTo(BigDecimal.ZERO)<0)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"发货数量不能小于0");

                Map map = new HashMap();
                map.put("sourceOrderId",srmInAsnOrderDetDto.getSourceOrderId());
                List<SrmInAsnOrderDetDto> srmInAsnOrderDetDtos = srmInAsnOrderDetMapper.findList(map);
                if (StringUtils.isEmpty(srmInAsnOrderDetDtos))
                    srmInAsnOrderDetDto.setTotalDeliveryQty(BigDecimal.ZERO);
                else
                    srmInAsnOrderDetDto.setTotalDeliveryQty(srmInAsnOrderDetDtos.get(0).getTotalDeliveryQty());

                if (srmInAsnOrderDetDto.getOrderQty().compareTo(srmInAsnOrderDetDto.getTotalDeliveryQty().add(srmInAsnOrderDetDto.getDeliveryQty())) == -1)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"交货总量大于订单数量");

                srmInAsnOrderDetDto.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getDeliveryQty()))
                    srmInAsnOrderDetDto.setDeliveryQty(BigDecimal.ZERO);
                srmInAsnOrderDetDto.setCreateUserId(user.getUserId());
                srmInAsnOrderDetDto.setCreateTime(new Date());
                srmInAsnOrderDetDto.setModifiedUserId(user.getUserId());
                srmInAsnOrderDetDto.setModifiedTime(new Date());
                srmInAsnOrderDetDto.setStatus(StringUtils.isEmpty(srmInAsnOrderDetDto.getStatus()) ? 1 : srmInAsnOrderDetDto.getStatus());
                srmInAsnOrderDetDto.setOrgId(user.getOrganizationId());
                list.add(srmInAsnOrderDetDto);

                SrmInHtAsnOrderDetDto srmInHtAsnOrderDetDto = new SrmInHtAsnOrderDetDto();
                BeanUtils.copyProperties(srmInAsnOrderDetDto, srmInHtAsnOrderDetDto);
                htList.add(srmInHtAsnOrderDetDto);
            }
            if (StringUtils.isNotEmpty(list)) srmInAsnOrderDetMapper.insertList(list);
            if (StringUtils.isNotEmpty(htList)) srmInHtAsnOrderDetMapper.insertList(htList);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int update(SrmInAsnOrderDto srmInAsnOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SrmInAsnOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("asnCode",srmInAsnOrderDto.getAsnCode())
                .andNotEqualTo("asnOrderId",srmInAsnOrderDto.getAsnOrderId());;
        List<SrmInAsnOrder> srmInAsnOrders = srmInAsnOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(srmInAsnOrders)) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"ASN单号已存在，请勿重复添加");
        if(StringUtils.isNotEmpty(srmInAsnOrderDto.getFileUrl())){
            //保存附件
            BaseFile baseFile = new BaseFile();
            baseFile.setRelevanceTableName("wms_in_asn_order");
            baseFile.setAccessUrl(srmInAsnOrderDto.getFileUrl());
            BaseFile date = baseFeignApi.add(baseFile).getData();
            if (StringUtils.isEmpty(date)) throw new BizErrorException("文件保存失败");
            srmInAsnOrderDto.setFileId(date.getFileId());
        }
        srmInAsnOrderDto.setModifiedTime(new Date());
        srmInAsnOrderDto.setModifiedUserId(user.getUserId());
        int i = srmInAsnOrderMapper.updateByPrimaryKeySelective(srmInAsnOrderDto);

        //保存履历表
        SrmInHtAsnOrder srmInHtAsnOrder = new SrmInHtAsnOrder();
        BeanUtils.copyProperties(srmInAsnOrderDto, srmInHtAsnOrder);
        srmInHtAsnOrderMapper.insertSelective(srmInHtAsnOrder);


        //保存详情表
        //更新原有明细
        ArrayList<Long> idList = new ArrayList<>();
        List<SrmInAsnOrderDetDto> list = srmInAsnOrderDto.getSrmInAsnOrderDetDtos();
        if(StringUtils.isNotEmpty(list)) {
            for (SrmInAsnOrderDetDto det : list) {
                if (StringUtils.isEmpty(det.getOrderQty()))
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"采购订单数量不能为空");
                if (StringUtils.isEmpty(det.getTotalDeliveryQty()))
                    det.setTotalDeliveryQty(BigDecimal.ZERO);
                if (StringUtils.isEmpty(det.getDeliveryQty()))
                    det.setDeliveryQty(BigDecimal.ZERO);
                if(det.getDeliveryQty().compareTo(BigDecimal.ZERO)<0)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"发货数量不能小于0");
                if (det.getOrderQty().compareTo(det.getTotalDeliveryQty().add(det.getDeliveryQty())) == -1)
                    throw new BizErrorException("交货总量大于订单数量");

                if (StringUtils.isNotEmpty(det.getAsnOrderDetId())) {
                    srmInAsnOrderDetMapper.updateByPrimaryKey(det);
                    idList.add(det.getAsnOrderDetId());
                }
            }
        }

        //删除更新之外的明细
        Example example1 = new Example(SrmInAsnOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("asnOrderId", srmInAsnOrderDto.getAsnOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("asnOrderDetId", idList);
        }
        srmInAsnOrderDetMapper.deleteByExample(example1);

        //新增剩余的明细
        if(StringUtils.isNotEmpty(list)){
            List<SrmInAsnOrderDetDto> addlist = new ArrayList<>();
            for (SrmInAsnOrderDetDto det  : list){
                if (idList.contains(det.getAsnOrderDetId())) {
                    continue;
                }
                det.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
                det.setCreateUserId(user.getUserId());
                det.setCreateTime(new Date());
                det.setModifiedUserId(user.getUserId());
                det.setModifiedTime(new Date());
                det.setStatus(StringUtils.isEmpty(det.getStatus())?1: det.getStatus());
                det.setOrgId(user.getOrganizationId());
                addlist.add(det);
            }
            if (StringUtils.isNotEmpty(addlist))
                srmInAsnOrderDetMapper.insertList(addlist);
        }
        return i;
    }

    @Override
    public int batchUpdate(List<SrmInAsnOrderDto> srmInAsnOrderDtos) {
        for(SrmInAsnOrderDto srmInAsnOrderDto : srmInAsnOrderDtos){
            this.update(srmInAsnOrderDto);
        }
        return 1;
    }

    @Override
    @LcnTransaction
    public int send(List<SrmInAsnOrderDto> srmInAsnOrderDtos) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isNotEmpty(srmInAsnOrderDtos)) {
            for (SrmInAsnOrderDto dto : srmInAsnOrderDtos) {
                SearchBaseSupplier  searchBaseSupplier = new SearchBaseSupplier();
                searchBaseSupplier.setSupplierId(dto.getSupplierId());
                List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
                if(StringUtils.isEmpty(baseSuppliers) || baseSuppliers.get(0).getIfAppointDeliver()==0 ){
                    if(dto.getOrderStatus() != 3)
                        throw new BizErrorException("只有状态为审核通过的订单才能发货");
                    dto.setOrderStatus((byte)6);
                }else{
                    if(dto.getOrderStatus() != 5)
                        throw new BizErrorException("只有已预约状态的订单才能发货");
                }
                dto.setOrderStatus((byte)6);
                dto.setModifiedUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                int i = srmInAsnOrderMapper.updateByPrimaryKeySelective(dto);

                //保存履历表
                SrmInHtAsnOrder srmInHtAsnOrder = new SrmInHtAsnOrder();
                BeanUtils.copyProperties(dto, srmInHtAsnOrder);
                srmInHtAsnOrderMapper.insertSelective(srmInHtAsnOrder);

                //返写送货预约状态
                if(i==1 && (StringUtils.isNotEmpty(baseSuppliers) && baseSuppliers.get(0).getIfAppointDeliver() ==1)){
                    Map map = new HashMap();
                    map.put("asnCode",dto.getAsnCode());
                    List<SrmAppointDeliveryReAsn> srmAppointDeliveryReAsns = srmAppointDeliveryReAsnService.findList(map);
                    if(StringUtils.isEmpty(srmAppointDeliveryReAsns)) throw new BizErrorException("未查询到送货预约对应的ASN单号");

                    Example example = new Example(SrmDeliveryAppoint.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("deliveryAppointId",srmAppointDeliveryReAsns.get(0).getDeliveryAppointId());
                    List<SrmDeliveryAppoint> srmDeliveryAppoints = srmDeliveryAppointMapper.selectByExample(example);
                    if(StringUtils.isEmpty(srmAppointDeliveryReAsns)) throw new BizErrorException("未查询到id为"+srmAppointDeliveryReAsns.get(0).getDeliveryAppointId()+"送货预约单号");
                    srmDeliveryAppoints.get(0).setAppointStatus((byte)6);
                    srmDeliveryAppointMapper.updateByPrimaryKeySelective(srmDeliveryAppoints.get(0));
                }


            }
        }
        return 1;
    }

    @Override
    public SrmInAsnOrderDto detail(Long id) {
        SrmInAsnOrderDto dto = new SrmInAsnOrderDto();
        Map map = new HashMap();
        map.put("asnOrderId",id);
        List<SrmInAsnOrderDto> list = srmInAsnOrderMapper.findList(map);
        if(StringUtils.isNotEmpty(list)){
            dto = list.get(0);
            List<Long>  ids = new ArrayList<>();
            for( SrmInAsnOrderDetDto det : dto.getSrmInAsnOrderDetDtos()){
                ids.add(det.getAsnOrderDetId());
            }
            SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
            searchWmsInnerMaterialBarcodeReOrder.setOrderDetIdList(ids);
            searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode("SRM-ASN");
            List<WmsInnerMaterialBarcodeReOrderDto> wmsInnerMaterialBarcodeReOrderDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcodeReOrder).getData();
            dto.setWmsInnerMaterialBarcodeReOrderDtos(wmsInnerMaterialBarcodeReOrderDtos);
        }
        return dto;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            Example example1 = new Example(SrmInAsnOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("asnOrderId",id);
            List<SrmInAsnOrderDet> srmInAsnOrderDets = srmInAsnOrderDetMapper.selectByExample(example1);
            for(SrmInAsnOrderDet det : srmInAsnOrderDets){
                if(StringUtils.isNotEmpty(det.getSourceId())) {
                    Example example = new Example(SrmPlanDeliveryOrderDet.class);
                    example.createCriteria().andEqualTo("planDeliveryOrderDetId", det.getSourceId());
                    List<SrmPlanDeliveryOrderDet> srmPlanDeliveryOrderDets = srmPlanDeliveryOrderDetMapper.selectByExample(example1);
                    srmPlanDeliveryOrderDets.get(0).setIfCreateAsn((byte)1);
                    srmPlanDeliveryOrderDetMapper.updateByPrimaryKey(srmPlanDeliveryOrderDets.get(0));
                }
            }
            srmInAsnOrderDetMapper.deleteByExample(example1);
        }
        int i = srmInAsnOrderMapper.deleteByIds(ids);
        return i;
    }

}

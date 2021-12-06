package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderType;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.*;
import com.fantechs.provider.srm.service.SrmInAsnOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private SrmAppointDeliveryReAsnMapper srmAppointDeliveryReAsnMapper;

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
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SrmInAsnOrderDto srmInAsnOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        srmInAsnOrderDto.setAsnCode(CodeUtils.getId("ASN-"));
        Example example = new Example(SrmInAsnOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("asnCode",srmInAsnOrderDto.getAsnCode());
        List<SrmInAsnOrder> srmInAsnOrders = srmInAsnOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(srmInAsnOrders)) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"ASN单号已存在，请勿重复添加");
        //默认收货通知单
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
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getTotalDeliveryQty()))
                    srmInAsnOrderDetDto.setTotalDeliveryQty(BigDecimal.ZERO);
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getDeliveryQty()))
                    srmInAsnOrderDetDto.setDeliveryQty(BigDecimal.ZERO);
                if(srmInAsnOrderDetDto.getDeliveryQty().compareTo(BigDecimal.ZERO)<0)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"发货数量不能小于0");
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
        //保存条码表---条码未自动生成，此处只展示，暂不做保存
       /* List<SrmInAsnOrderDetBarcode> barcodeList = new ArrayList<>();
        for(SrmInAsnOrderDetBarcode srmInAsnOrderDetBarcode : srmInAsnOrderDto.getSrmInAsnOrderDetBarcodes()){
            srmInAsnOrderDetBarcode.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
            srmInAsnOrderDetBarcode.setCreateUserId(user.getUserId());
            srmInAsnOrderDetBarcode.setCreateTime(new Date());
            srmInAsnOrderDetBarcode.setModifiedUserId(user.getUserId());
            srmInAsnOrderDetBarcode.setModifiedTime(new Date());
            srmInAsnOrderDetBarcode.setStatus(StringUtils.isEmpty(srmInAsnOrderDetBarcode.getStatus())?1: srmInAsnOrderDetBarcode.getStatus());
            srmInAsnOrderDetBarcode.setOrgId(user.getOrganizationId());
            barcodeList.add(srmInAsnOrderDetBarcode);
        }
        if(StringUtils.isNotEmpty(barcodeList)) srmInAsnOrderDetBarcodeMapper.insertList(barcodeList);*/

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
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

        //删除子表重新添加
        Example example1 = new Example(SrmInAsnOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("asnOrderId",srmInAsnOrderDto.getAsnOrderId());
        srmInAsnOrderDetMapper.deleteByExample(example1);
        //保存详情表
        if(StringUtils.isNotEmpty(srmInAsnOrderDto.getSrmInAsnOrderDetDtos())) {
            List<SrmInAsnOrderDetDto> list = new ArrayList<>();
            for (SrmInAsnOrderDetDto srmInAsnOrderDetDto : srmInAsnOrderDto.getSrmInAsnOrderDetDtos()) {
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getOrderQty()))
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"采购订单数量不能为空");
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getTotalDeliveryQty()))
                    srmInAsnOrderDetDto.setTotalDeliveryQty(BigDecimal.ZERO);
                if (StringUtils.isEmpty(srmInAsnOrderDetDto.getDeliveryQty()))
                    srmInAsnOrderDetDto.setDeliveryQty(BigDecimal.ZERO);
                if(srmInAsnOrderDetDto.getDeliveryQty().compareTo(BigDecimal.ZERO)<0)
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"发货数量不能小于0");
                if (srmInAsnOrderDetDto.getOrderQty().compareTo(srmInAsnOrderDetDto.getTotalDeliveryQty().add(srmInAsnOrderDetDto.getDeliveryQty())) == -1)
                    throw new BizErrorException("交货总量大于订单数量");
                srmInAsnOrderDetDto.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
                srmInAsnOrderDetDto.setCreateUserId(user.getUserId());
                srmInAsnOrderDetDto.setCreateTime(new Date());
                srmInAsnOrderDetDto.setModifiedUserId(user.getUserId());
                srmInAsnOrderDetDto.setModifiedTime(new Date());
                srmInAsnOrderDetDto.setStatus(StringUtils.isEmpty(srmInAsnOrderDetDto.getStatus()) ? 1 : srmInAsnOrderDetDto.getStatus());
                srmInAsnOrderDetDto.setOrgId(user.getOrganizationId());
                list.add(srmInAsnOrderDetDto);
            }
            if (StringUtils.isNotEmpty(list)) srmInAsnOrderDetMapper.insertList(list);
        }
       /* Example example2 = new Example(SrmInAsnOrderDetBarcode.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("asnOrderId",srmInAsnOrderDto.getAsnOrderId());
        srmInAsnOrderDetBarcodeMapper.deleteByExample(example2);
        //保存条码表
        List<SrmInAsnOrderDetBarcode> barcodeList = new ArrayList<>();
        for(SrmInAsnOrderDetBarcode srmInAsnOrderDetBarcode : srmInAsnOrderDto.getSrmInAsnOrderDetBarcodes()){
            srmInAsnOrderDetBarcode.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
            srmInAsnOrderDetBarcode.setCreateUserId(user.getUserId());
            srmInAsnOrderDetBarcode.setCreateTime(new Date());
            srmInAsnOrderDetBarcode.setModifiedUserId(user.getUserId());
            srmInAsnOrderDetBarcode.setModifiedTime(new Date());
            srmInAsnOrderDetBarcode.setStatus(StringUtils.isEmpty(srmInAsnOrderDetBarcode.getStatus())?1: srmInAsnOrderDetBarcode.getStatus());
            srmInAsnOrderDetBarcode.setOrgId(user.getOrganizationId());
            barcodeList.add(srmInAsnOrderDetBarcode);
        }
        if(StringUtils.isNotEmpty(barcodeList)) srmInAsnOrderDetBarcodeMapper.insertList(barcodeList);*/

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
    public int send(List<SrmInAsnOrderDto> srmInAsnOrderDtos) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isNotEmpty(srmInAsnOrderDtos)) {
            for (SrmInAsnOrderDto dto : srmInAsnOrderDtos) {
                SearchBaseSupplier  searchBaseSupplier = new SearchBaseSupplier();
                searchBaseSupplier.setSupplierId(dto.getSupplierId());
                List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
                if(StringUtils.isEmpty(baseSuppliers) || "0".equals(baseSuppliers.get(0).getIfAppointDeliver())){
                    if(!"3".equals(baseSuppliers.get(0).getIfAppointDeliver()))
                        throw new BizErrorException("只有状态为审核通过的订单才能发货");
                    dto.setOrderStatus((byte)6);
                }else{
                    if(!"5".equals(baseSuppliers.get(0).getIfAppointDeliver()))
                        throw new BizErrorException("只有已预约状态的订单才能发货");
                }
                dto.setOrderStatus((byte)6);
                dto.setModifiedUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                int i = srmInAsnOrderMapper.updateByPrimaryKeySelective(dto);

                //返写送货预约状态
                if(i==1){
                    Example example2 = new Example(SrmAppointDeliveryReAsn.class);
                    Example.Criteria criteria2 = example2.createCriteria();
                    criteria2.andEqualTo("asnCode",dto.getAsnCode());
                    List<SrmAppointDeliveryReAsn> srmAppointDeliveryReAsns = srmAppointDeliveryReAsnMapper.selectByExample(example2);
                    if(StringUtils.isEmpty(srmAppointDeliveryReAsns)) throw new BizErrorException("未查询到送货预约对应的ASN单号");

                    Example example = new Example(SrmDeliveryAppoint.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("asnOrderId",srmAppointDeliveryReAsns.get(0).getAsnOrderId());
                    List<SrmDeliveryAppoint> srmDeliveryAppoints = srmDeliveryAppointMapper.selectByExample(example);
                    if(StringUtils.isEmpty(srmAppointDeliveryReAsns)) throw new BizErrorException("未查询到id为"+srmAppointDeliveryReAsns.get(0).getAsnOrderId()+"送货预约单号");
                    srmDeliveryAppoints.get(0).setAppointStatus((byte)6);
                    srmDeliveryAppointMapper.updateByPrimaryKeySelective(srmDeliveryAppoints.get(0));
                }


            }
        }
        return 1;
    }


}

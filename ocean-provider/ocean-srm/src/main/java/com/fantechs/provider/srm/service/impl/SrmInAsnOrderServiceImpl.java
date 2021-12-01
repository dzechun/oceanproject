package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderTypeDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderType;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetBarcodeMapper;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderMapper;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderMapper;
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
    private SrmInAsnOrderDetBarcodeMapper srmInAsnOrderDetBarcodeMapper;


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


        int i = srmInAsnOrderMapper.insertUseGeneratedKeys(srmInAsnOrderDto);

        //保存详情表
        List<SrmInAsnOrderDetDto> list = new ArrayList<>();
        for(SrmInAsnOrderDetDto srmInAsnOrderDetDto : srmInAsnOrderDto.getSrmInAsnOrderDetDtos()){
            srmInAsnOrderDetDto.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
            if(StringUtils.isEmpty(srmInAsnOrderDetDto.getDeliveryQty()))
                srmInAsnOrderDetDto.setDeliveryQty(BigDecimal.ZERO);
            srmInAsnOrderDetDto.setCreateUserId(user.getUserId());
            srmInAsnOrderDetDto.setCreateTime(new Date());
            srmInAsnOrderDetDto.setModifiedUserId(user.getUserId());
            srmInAsnOrderDetDto.setModifiedTime(new Date());
            srmInAsnOrderDetDto.setStatus(StringUtils.isEmpty(srmInAsnOrderDetDto.getStatus())?1: srmInAsnOrderDetDto.getStatus());
            srmInAsnOrderDetDto.setOrgId(user.getOrganizationId());
            list.add(srmInAsnOrderDetDto);
        }
        if(StringUtils.isNotEmpty(list)) srmInAsnOrderDetMapper.insertList(list);

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

        //保存履历表
        SrmInHtAsnOrder srmInHtAsnOrder = new SrmInHtAsnOrder();
        BeanUtils.copyProperties(srmInAsnOrderDto, srmInHtAsnOrder);
        srmInHtAsnOrderMapper.insertSelective(srmInHtAsnOrder);
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
        List<SrmInAsnOrderDetDto> list = new ArrayList<>();
        for(SrmInAsnOrderDetDto srmInAsnOrderDetDto : srmInAsnOrderDto.getSrmInAsnOrderDetDtos()){
            srmInAsnOrderDetDto.setAsnOrderId(srmInAsnOrderDto.getAsnOrderId());
            srmInAsnOrderDetDto.setCreateUserId(user.getUserId());
            srmInAsnOrderDetDto.setCreateTime(new Date());
            srmInAsnOrderDetDto.setModifiedUserId(user.getUserId());
            srmInAsnOrderDetDto.setModifiedTime(new Date());
            srmInAsnOrderDetDto.setStatus(StringUtils.isEmpty(srmInAsnOrderDetDto.getStatus())?1: srmInAsnOrderDetDto.getStatus());
            srmInAsnOrderDetDto.setOrgId(user.getOrganizationId());
            list.add(srmInAsnOrderDetDto);
        }
        if(StringUtils.isNotEmpty(list)) srmInAsnOrderDetMapper.insertList(list);

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


}
package com.fantechs.provider.wms.in.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderBarcode;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInReceivingOrderImport;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.wms.in.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.in.mapper.*;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderService;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInReceivingOrderServiceImpl extends BaseService<WmsInReceivingOrder> implements WmsInReceivingOrderService {

    @Resource
    private WmsInReceivingOrderMapper wmsInReceivingOrderMapper;
    @Resource
    private WmsInReceivingOrderDetMapper wmsInReceivingOrderDetMapper;
    @Resource
    private WmsInPlanReceivingOrderMapper wmsInPlanReceivingOrderMapper;
    @Resource
    private WmsInPlanReceivingOrderDetMapper wmsInPlanReceivingOrderDetMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private WmsInHtReceivingOrderMapper wmsInHtReceivingOrderMapper;
    @Resource
    private WmsInHtReceivingOrderDetMapper wmsInHtReceivingOrderDetMapper;
    @Resource
    private WmsInInPlanOrderService wmsInInPlanOrderService;

    @Override
    public List<WmsInReceivingOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInReceivingOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInReceivingOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"仓库不能为空");
        }
        record.setReceivingOrderCode(CodeUtils.getId("IN-SWK"));
        record.setCreateUserId(sysUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setSysOrderTypeCode("IN-SWK");
        if(StringUtils.isEmpty(record.getSourceBigType())){
            record.setSourceBigType((byte)2);
        }
        if(StringUtils.isNotEmpty(record.getIsPdaCreate()) && record.getIsPdaCreate()==1){
            record.setOrderStatus((byte)3);
        }else {
            record.setOrderStatus((byte)1);
        }
        int num = wmsInReceivingOrderMapper.insertUseGeneratedKeys(record);

        //履历
        WmsInHtReceivingOrder wmsInHtReceivingOrder = new WmsInHtReceivingOrder();
        BeanUtils.copyProperties(record,wmsInHtReceivingOrder);
        wmsInHtReceivingOrderMapper.insertSelective(wmsInHtReceivingOrder);

        if(!record.getWmsInReceivingOrderDets().isEmpty()){
            int i = 0;
            for (WmsInReceivingOrderDet wmsInReceivingOrderDet : record.getWmsInReceivingOrderDets()) {
                i++;
                wmsInReceivingOrderDet.setReceivingOrderId(record.getReceivingOrderId());
                wmsInReceivingOrderDet.setLineNumber(i+"");
                wmsInReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setModifiedTime(new Date());
                wmsInReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setIfAllIssued((byte)0);
                wmsInReceivingOrderDet.setOrgId(sysUser.getOrganizationId());

                if(StringUtils.isNotEmpty(record.getIsPdaCreate()) && record.getIsPdaCreate()==1){
                    wmsInReceivingOrderDet.setOperatorUserId(sysUser.getUserId());
                    wmsInReceivingOrderDet.setPlanQty(wmsInReceivingOrderDet.getActualQty());
                    wmsInReceivingOrderDet.setLineStatus((byte)3);
                    wmsInReceivingOrderDetMapper.insertUseGeneratedKeys(wmsInReceivingOrderDet);

                    List<WmsInnerMaterialBarcodeDto> wmsInnerMaterialBarcodeList = new LinkedList<>();
                        for (WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode : wmsInReceivingOrderDet.getWmsInReceivingOrderBarcodeList()) {
                            //是否系统条码
                            if (wmsInReceivingOrderDet.getIfSysBarcode() == 0) {

                                WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcode = new WmsInnerMaterialBarcodeDto();
                                wmsInnerMaterialBarcode.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
                                wmsInnerMaterialBarcode.setBarcode(wmsInReceivingOrderBarcode.getBarcode());
                                wmsInnerMaterialBarcode.setBatchCode(wmsInReceivingOrderDet.getBatchCode());
                                wmsInnerMaterialBarcode.setMaterialQty(wmsInReceivingOrderBarcode.getMaterialQty());
                                wmsInnerMaterialBarcode.setPrintOrderTypeCode("IN-SWK");
                                wmsInnerMaterialBarcode.setPrintOrderCode(record.getReceivingOrderCode());
                                wmsInnerMaterialBarcode.setPrintOrderId(record.getReceivingOrderId());
                                wmsInnerMaterialBarcode.setPrintOrderDetId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                                wmsInnerMaterialBarcode.setIfSysBarcode((byte) 0);
                                wmsInnerMaterialBarcode.setProductionTime(wmsInReceivingOrderDet.getProductionTime());
                                //产生类型(1-供应商条码 2-自己打印 3-生产条码)
                                wmsInnerMaterialBarcode.setCreateType((byte) 1);
                                //条码状态(1-已生成 2-已打印 3-已收货 4-已质检 5-已上架 6-已出库)
                                wmsInnerMaterialBarcode.setBarcodeStatus((byte) 3);
                                wmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                                wmsInnerMaterialBarcode.setCreateTime(new Date());
                                wmsInnerMaterialBarcode.setCreateUserId(sysUser.getUserId());

                                //num+=innerFeignApi.batchSave(wmsInnerMaterialBarcode);
                                wmsInnerMaterialBarcodeList.add(wmsInnerMaterialBarcode);
                                //设置来料条码ID
                            } else {
                                //系统条码更新条码状态
                                //if(wmsInReceivingOrderBarcode.getBarcodeType()!=1){
                                    SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
                                    if(wmsInReceivingOrderBarcode.getBarcodeType()==1){
                                        searchWmsInnerMaterialBarcode.setBarcode(wmsInReceivingOrderBarcode.getBarcode());
                                    }else if(wmsInReceivingOrderBarcode.getBarcodeType()==2){
                                        //彩盒
                                        searchWmsInnerMaterialBarcode.setColorBoxCode(wmsInReceivingOrderBarcode.getBarcode());
                                    }else if(wmsInReceivingOrderBarcode.getBarcodeType()==3){
                                        //箱号
                                        searchWmsInnerMaterialBarcode.setCartonCode(wmsInReceivingOrderBarcode.getBarcode());
                                    }else if(wmsInReceivingOrderBarcode.getBarcodeType()==4){
                                        //栈板
                                        searchWmsInnerMaterialBarcode.setPalletCode(wmsInReceivingOrderBarcode.getBarcode());
                                    }
                                    searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                                    List<WmsInnerMaterialBarcodeDto> barcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
                                    if(barcodeDtos.size()>0){
                                        for (WmsInnerMaterialBarcodeDto barcodeDto : barcodeDtos) {
                                            WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcode = new WmsInnerMaterialBarcodeDto();
                                            wmsInnerMaterialBarcode.setMaterialBarcodeId(barcodeDto.getMaterialBarcodeId());
                                            wmsInnerMaterialBarcode.setBarcodeStatus((byte) 3);
                                            wmsInnerMaterialBarcode.setModifiedUserId(sysUser.getUserId());
                                            wmsInnerMaterialBarcode.setModifiedTime(new Date());
                                            wmsInnerMaterialBarcodeList.add(wmsInnerMaterialBarcode);
                                        }
                                    }else {
                                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到sn条码信息");
                                    }
                            }
                    }
                    //更新条码状态
                    if(wmsInReceivingOrderDet.getIfSysBarcode()==1 && !wmsInnerMaterialBarcodeList.isEmpty()){
                        innerFeignApi.batchUpdate(wmsInnerMaterialBarcodeList);
                    }else {
                        wmsInnerMaterialBarcodeList = innerFeignApi.batchSave(wmsInnerMaterialBarcodeList).getData();
                    }
                    //条码新增到来料条码关系表
                    List<WmsInnerMaterialBarcodeReOrder> list = new LinkedList<>();
                    for (WmsInnerMaterialBarcodeDto wmsInReceivingOrderBarcode : wmsInnerMaterialBarcodeList) {
                        WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder=new WmsInnerMaterialBarcodeReOrder();
                        wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-SWK");//类型 上架
                        wmsInnerMaterialBarcodeReOrder.setOrderCode(record.getReceivingOrderCode());
                        wmsInnerMaterialBarcodeReOrder.setOrderId(record.getReceivingOrderId());
                        //来料条码ID
                        wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInReceivingOrderBarcode.getMaterialBarcodeId());

                        wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
                        wmsInnerMaterialBarcodeReOrder.setOrgId(sysUser.getOrganizationId());
                        wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                        wmsInnerMaterialBarcodeReOrder.setCreateUserId(sysUser.getUserId());

                        list.add(wmsInnerMaterialBarcodeReOrder);
                    }
                    if(!list.isEmpty()){
                        innerFeignApi.batchAdd(list);
                    }
                }else {
                    if(StringUtils.isEmpty(wmsInReceivingOrderDet.getPlanQty()) || wmsInReceivingOrderDet.getPlanQty().compareTo(BigDecimal.ZERO)<1){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"计划数量必须大于0");
                    }
                    wmsInReceivingOrderDet.setLineStatus((byte)1);
                    wmsInReceivingOrderDetMapper.insertUseGeneratedKeys(wmsInReceivingOrderDet);
                    //获取条码记录
                    if(StringUtils.isNotEmpty(wmsInReceivingOrderDet.getWmsInReceivingOrderBarcodeList())) {
                        List<WmsInnerMaterialBarcodeReOrder> wmsInnerMaterialBarcodeReOrders = new ArrayList<>();
                        for (WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode : wmsInReceivingOrderDet.getWmsInReceivingOrderBarcodeList()) {
                            WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                            wmsInnerMaterialBarcodeReOrder.setOrderId(record.getReceivingOrderId());
                            wmsInnerMaterialBarcodeReOrder.setOrderCode(record.getReceivingOrderCode());
                            wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(wmsInReceivingOrderBarcode.getMaterialBarcodeId());
                            wmsInnerMaterialBarcodeReOrder.setOrderDetId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                            wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("IN-SWK");
                            wmsInnerMaterialBarcodeReOrder.setScanStatus((byte) 1);
                            wmsInnerMaterialBarcodeReOrders.add(wmsInnerMaterialBarcodeReOrder);
                        }
                        if (!wmsInnerMaterialBarcodeReOrders.isEmpty()) {
                            ResponseEntity responseEntity = innerFeignApi.batchAdd(wmsInnerMaterialBarcodeReOrders);
                            if (responseEntity.getCode() != 0) {
                                throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                            }
                        }
                    }else {
                        if (record.getSourceBigType() != 2) {
                            List<WmsInnerMaterialBarcodeReOrder> barcodeReOrderList = new ArrayList<>();
                            SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
                            sBarcodeReOrder.setOrderTypeCode(record.getSourceSysOrderTypeCode());//单据类型
                            sBarcodeReOrder.setOrderDetId(wmsInReceivingOrderDet.getSourceId());//明细ID
                            List<WmsInnerMaterialBarcodeReOrderDto> reOrderList = innerFeignApi.findAll(sBarcodeReOrder).getData();
                            if (reOrderList.size() > 0) {
                                for (WmsInnerMaterialBarcodeReOrderDto item : reOrderList) {
                                    WmsInnerMaterialBarcodeReOrder barcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                                    BeanUtil.copyProperties(item, barcodeReOrder);
                                    barcodeReOrder.setOrderTypeCode(record.getSysOrderTypeCode());
                                    barcodeReOrder.setOrderCode(record.getReceivingOrderCode());
                                    barcodeReOrder.setOrderId(record.getReceivingOrderId());
                                    barcodeReOrder.setOrderDetId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                                    barcodeReOrder.setScanStatus((byte) 1);
                                    barcodeReOrder.setCreateUserId(sysUser.getUserId());
                                    barcodeReOrder.setCreateTime(new Date());
                                    barcodeReOrder.setModifiedUserId(sysUser.getUserId());
                                    barcodeReOrder.setModifiedTime(new Date());
                                    barcodeReOrder.setMaterialBarcodeReOrderId(null);
                                    barcodeReOrderList.add(barcodeReOrder);
                                }
                            }
                            //批量新增到条码关系表
                            if (barcodeReOrderList.size() > 0) {
                                innerFeignApi.batchAdd(barcodeReOrderList);
                            }
                        }
                    }
                }
                WmsInHtReceivingOrderDet wmsInHtReceivingOrderDet = new WmsInHtReceivingOrderDet();
                BeanUtils.copyProperties(wmsInReceivingOrderDet,wmsInHtReceivingOrderDet);
                wmsInHtReceivingOrderDetMapper.insertSelective(wmsInHtReceivingOrderDet);
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInReceivingOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        Boolean isPDA = false;
        WmsInReceivingOrder wmsInReceivingOrder = wmsInReceivingOrderMapper.selectByPrimaryKey(entity.getReceivingOrderId());
        if(wmsInReceivingOrder.getOrderStatus()==3){
            throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"单据已完成,无法更改");
        }
        if(StringUtils.isNotEmpty(entity.getIsPdaCreate()) && entity.getIsPdaCreate()==1){
            isPDA=true;
        }else {
            if(wmsInReceivingOrder.getSourceBigType()==1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"下推单据,无法修改");
            }
            //删除原有单据
            Example example = new Example(WmsInReceivingOrderDet.class);
            example.createCriteria().andEqualTo("receivingOrderId",entity.getReceivingOrderId());
            wmsInReceivingOrderDetMapper.deleteByExample(example);
        }
        int i=0;

        WmsInHtReceivingOrder wmsInHtReceivingOrder = new WmsInHtReceivingOrder();
        BeanUtils.copyProperties(entity,wmsInHtReceivingOrder);
        wmsInHtReceivingOrderMapper.insertSelective(wmsInHtReceivingOrder);

        for (WmsInReceivingOrderDet wmsInReceivingOrderDet : entity.getWmsInReceivingOrderDets()) {
            wmsInReceivingOrderDet.setModifiedTime(new Date());
            wmsInReceivingOrderDet.setModifiedUserId(sysUser.getUserId());
            if (isPDA) {
                //PDA更新数量 更新单据状态
                WmsInReceivingOrderDet inReceivingOrderDet = wmsInReceivingOrderDetMapper.selectByPrimaryKey(wmsInReceivingOrderDet.getReceivingOrderDetId());
                if (StringUtils.isEmpty(inReceivingOrderDet.getActualQty())) {
                    inReceivingOrderDet.setActualQty(BigDecimal.ZERO);
                }
                BigDecimal totalActualQty = inReceivingOrderDet.getActualQty().add(wmsInReceivingOrderDet.getActualQty());
                if(totalActualQty.compareTo(inReceivingOrderDet.getPlanQty())==1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"实收数量不能大于计划数量");
                }
                if (inReceivingOrderDet.getPlanQty().compareTo(totalActualQty) == 0) {
                    wmsInReceivingOrderDet.setLineStatus((byte) 3);
                } else {
                    wmsInReceivingOrderDet.setLineStatus((byte) 2);
                }
                wmsInReceivingOrderDet.setOperatorUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setActualQty(totalActualQty);
                wmsInReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInReceivingOrderDet);
                wmsInReceivingOrderDet.setSourceId(inReceivingOrderDet.getSourceId());
                //反写收货计划实收数量
                if(wmsInReceivingOrder.getSourceBigType()==1 && wmsInReceivingOrder.getSourceSysOrderTypeCode().equals("IN-SPO")){
                    this.writeQty(wmsInReceivingOrderDet);
                }
                List<WmsInnerMaterialBarcodeDto> wmsInnerMaterialBarcodeDtos = new LinkedList<>();
                for (WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode : wmsInReceivingOrderDet.getWmsInReceivingOrderBarcodeList()) {

                    //系统条码更新条码状态
                        SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
                        if(wmsInReceivingOrderBarcode.getBarcodeType()==1){
                            //sn
                            searchWmsInnerMaterialBarcode.setBarcode(wmsInReceivingOrderBarcode.getBarcode());
                        }else if(wmsInReceivingOrderBarcode.getBarcodeType()==2){
                            //彩盒
                            searchWmsInnerMaterialBarcode.setColorBoxCode(wmsInReceivingOrderBarcode.getBarcode());
                        }else if(wmsInReceivingOrderBarcode.getBarcodeType()==3){
                            //箱号
                            searchWmsInnerMaterialBarcode.setCartonCode(wmsInReceivingOrderBarcode.getBarcode());
                        }else if(wmsInReceivingOrderBarcode.getBarcodeType()==4){
                            //栈板
                            searchWmsInnerMaterialBarcode.setPalletCode(wmsInReceivingOrderBarcode.getBarcode());
                        }
                        searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
                        List<WmsInnerMaterialBarcodeDto> barcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
                        if(barcodeDtos.size()>0){
                            for (WmsInnerMaterialBarcodeDto barcodeDto : barcodeDtos) {
                                WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcode = new WmsInnerMaterialBarcodeDto();
                                wmsInnerMaterialBarcode.setMaterialBarcodeId(barcodeDto.getMaterialBarcodeId());
                                wmsInnerMaterialBarcode.setBarcodeStatus((byte) 3);
                                wmsInnerMaterialBarcode.setModifiedUserId(sysUser.getUserId());
                                wmsInnerMaterialBarcode.setModifiedTime(new Date());
                                wmsInnerMaterialBarcodeDtos.add(wmsInnerMaterialBarcode);

                                //更新条码关系表条码状态
                                SearchWmsInnerMaterialBarcodeReOrder sReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                                sReOrder.setOrderTypeCode("IN-SWK");
                                sReOrder.setOrderId(entity.getReceivingOrderId());
                                sReOrder.setMaterialBarcodeId(barcodeDto.getMaterialBarcodeId());
                                List<WmsInnerMaterialBarcodeReOrderDto> reOrderDtoList=innerFeignApi.findList(sReOrder).getData();
                                if(reOrderDtoList.size()<=0){
                                    throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"条码关系表找不到此条码数据 条码ID-->"+barcodeDto.getMaterialBarcodeId().toString());
                                }
                                WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                                wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeReOrderId(reOrderDtoList.get(0).getMaterialBarcodeReOrderId());
                                wmsInnerMaterialBarcodeReOrder.setScanStatus((byte)3);
                                wmsInnerMaterialBarcodeReOrder.setModifiedUserId(sysUser.getUserId());
                                wmsInnerMaterialBarcodeReOrder.setModifiedTime(new Date());
                                innerFeignApi.update(wmsInnerMaterialBarcodeReOrder);
                            }
                        }else {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到sn条码信息");
                        }
                }
                if(!wmsInnerMaterialBarcodeDtos.isEmpty()){
                    innerFeignApi.batchUpdate(wmsInnerMaterialBarcodeDtos);
                }
            } else {
                i++;
                if(StringUtils.isEmpty(wmsInReceivingOrderDet.getPlanQty()) || wmsInReceivingOrderDet.getPlanQty().compareTo(BigDecimal.ZERO)<1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"计划数量必须大于0");
                }
                wmsInReceivingOrderDet.setLineStatus((byte)1);
                wmsInReceivingOrderDet.setReceivingOrderId(entity.getReceivingOrderId());
                wmsInReceivingOrderDet.setLineNumber(i+"");
                wmsInReceivingOrderDet.setCreateUserId(sysUser.getUserId());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setIfAllIssued((byte)0);
                wmsInReceivingOrderDet.setOrgId(sysUser.getOrganizationId());
            }
        }
        if(!isPDA && !entity.getWmsInReceivingOrderDets().isEmpty()){
            wmsInReceivingOrderDetMapper.insertList(entity.getWmsInReceivingOrderDets());
        }else {
            Example example = new Example(WmsInReceivingOrderDet.class);
            example.createCriteria().andEqualTo("receivingOrderId",entity.getReceivingOrderId());
            List<WmsInReceivingOrderDet> wmsInReceivingOrderDets = wmsInReceivingOrderDetMapper.selectByExample(example);
            if(wmsInReceivingOrderDets.stream().filter(li->li.getLineStatus()==3).collect(Collectors.toList()).size()==wmsInReceivingOrderDets.size()){
                entity.setOrderStatus((byte)3);
            }else {
                entity.setOrderStatus((byte)2);
            }
        }
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idArry = ids.split(",");
        for (String s : idArry) {
            WmsInReceivingOrder wmsInReceivingOrder = wmsInReceivingOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInReceivingOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,s);
            }
            if(wmsInReceivingOrder.getSourceBigType()==1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"下推单据无法删除");
            }
            if(wmsInReceivingOrder.getOrderStatus()>1){
                throw new BizErrorException("已作业的单无法删除");
            }
            //删除明细
            Example example = new Example(WmsInReceivingOrderDet.class);
            example.createCriteria().andEqualTo("receivingOrderId",s);
            wmsInReceivingOrderDetMapper.deleteByExample(example);
        }
        return super.batchDelete(ids);
    }

    private void writeQty(WmsInReceivingOrderDet wmsInReceivingOrderDet){
        WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet = wmsInPlanReceivingOrderDetMapper.selectByPrimaryKey(wmsInReceivingOrderDet.getSourceId());
        wmsInPlanReceivingOrderDet.setActualQty(wmsInReceivingOrderDet.getActualQty());
        wmsInPlanReceivingOrderDet.setOperatorUserId(wmsInReceivingOrderDet.getOperatorUserId());
        wmsInPlanReceivingOrderDet.setLineStatus(wmsInReceivingOrderDet.getLineStatus());
        wmsInPlanReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInPlanReceivingOrderDet);
        Example example = new Example(WmsInPlanReceivingOrderDet.class);
        example.createCriteria().andEqualTo("planReceivingOrderId",wmsInPlanReceivingOrderDet.getPlanReceivingOrderId());
        List<WmsInPlanReceivingOrderDet> list = wmsInPlanReceivingOrderDetMapper.selectByExample(example);
        WmsInPlanReceivingOrder wmsInPlanReceivingOrder = new WmsInPlanReceivingOrder();
        wmsInPlanReceivingOrder.setPlanReceivingOrderId(wmsInPlanReceivingOrderDet.getPlanReceivingOrderId());
        if(list.stream().filter(li->li.getLineStatus()==3).collect(Collectors.toList()).size()==list.size()){
            wmsInPlanReceivingOrder.setOrderStatus((byte)3);
        }else{
            wmsInPlanReceivingOrder.setOrderStatus((byte)2);
        }
        wmsInPlanReceivingOrderMapper.updateByPrimaryKeySelective(wmsInPlanReceivingOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsInReceivingOrderImport> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        Map<String,String > map = new HashMap<>();
        List<Map<String,String>> failMap = new ArrayList<>();  //记录操作失败行数
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        int i=0;
        Map<String,List<WmsInReceivingOrderImport>> hashMap = list.stream().collect(Collectors.groupingBy(WmsInReceivingOrderImport::getWarehouseName));
        Set<String> set = hashMap.keySet();
        for (String s : set) {
            List<WmsInReceivingOrderImport> importList = hashMap.get(s);
            //判断非空
            String warehouseName = importList.get(0).getWarehouseName();
            if (StringUtils.isEmpty(
                    warehouseName
            )){
                map.put(s,"仓库名称、计划入库单号不鞥呢为空");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(warehouseName);
            ResponseEntity<List<BaseWarehouse>> responseEntity = baseFeignApi.findList(searchBaseWarehouse);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if (StringUtils.isEmpty(responseEntity.getData()) || responseEntity.getData().size() != 1) {
                map.put(s,"未查询到仓库或查询出的仓库不唯一");
                failMap.add(map);
                fail.add(i++);
                break;
            }
            //表头
            WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
            wmsInReceivingOrder.setReceivingOrderCode(CodeUtils.getId("IN-SWK"));
            wmsInReceivingOrder.setWarehouseId(responseEntity.getData().get(0).getWarehouseId());
            wmsInReceivingOrder.setCreateTime(new Date());
            wmsInReceivingOrder.setCreateUserId(user.getUserId());
            wmsInReceivingOrder.setModifiedTime(new Date());
            wmsInReceivingOrder.setModifiedUserId(user.getUserId());
            wmsInReceivingOrder.setOrderStatus((byte)1);
            wmsInReceivingOrder.setOrgId(user.getOrganizationId());
            int num = wmsInReceivingOrderMapper.insertUseGeneratedKeys(wmsInReceivingOrder);
            List<WmsInReceivingOrderDet> wmsInReceivingOrderDets = new ArrayList<>();
            for (WmsInReceivingOrderImport wmsInReceivingOrderImport : importList) {
                WmsInReceivingOrderDet wmsInReceivingOrderDet = new WmsInReceivingOrderDet();
                wmsInReceivingOrderDet.setReceivingOrderId(wmsInReceivingOrder.getReceivingOrderId());

                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(wmsInReceivingOrderImport.getMaterialCode());
                searchBaseMaterial.setCodeQueryMark(1);
                ResponseEntity<List<BaseMaterial>> rs = baseFeignApi.findList(searchBaseMaterial);
                if(rs.getData().isEmpty()){
                    map.put(s,"未查询到物料基础信息");
                    failMap.add(map);
                    fail.add(i++);
                    break;
                }
                wmsInReceivingOrderDet.setMaterialId(rs.getData().get(0).getMaterialId());
                wmsInReceivingOrderDet.setBatchCode(wmsInReceivingOrderImport.getBatchCode());
                wmsInReceivingOrderDet.setPlanQty(wmsInReceivingOrderImport.getPlanQty());
                wmsInReceivingOrderDet.setActualQty(BigDecimal.ZERO);
                wmsInReceivingOrderDet.setProductionTime(wmsInReceivingOrderImport.getProductionTime());
                wmsInReceivingOrderDet.setCreateTime(new Date());
                wmsInReceivingOrderDet.setCreateUserId(user.getUserId());
                wmsInReceivingOrderDet.setModifiedTime(new Date());
                wmsInReceivingOrderDet.setModifiedUserId(user.getUserId());
                wmsInReceivingOrderDet.setLineStatus((byte)1);
                wmsInReceivingOrderDet.setIfAllIssued((byte)0);
                wmsInReceivingOrderDets.add(wmsInReceivingOrderDet);
            }
            if(wmsInReceivingOrderDets.size()<1){
                wmsInPlanReceivingOrderMapper.deleteByPrimaryKey(wmsInReceivingOrder);
                map.put(s,"无详情数据");
                failMap.add(map);
                fail.add(i++);
            }else {
                wmsInReceivingOrderDetMapper.insertList(wmsInReceivingOrderDets);
            }
        }

        //添加日志
        SysImportAndExportLog log = new SysImportAndExportLog();
        log.setSucceedCount(list.size() - fail.size());
        log.setFailCount(fail.size());
        log.setFailInfo(failMap.toString());
        log.setOperatorUserId(user.getUserId());
        log.setTotalCount(list.size());
        log.setType((byte)1);
        securityFeignApi.add(log);

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pushDown(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idArry = ids.split(",");


        String sysOrderTypeCode =null;//当前单据类型编码
        String coreSourceSysOrderTypeCode = null;//核心单据类型编码
        //查当前单据的下游单据
//        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
//        searchBaseOrderFlow.setOrderTypeCode("IN-SWK");
//        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
//        if(StringUtils.isEmpty(baseOrderFlow)){
//            throw new BizErrorException("未找到当前单据配置的下游单据");
//        }

        List<WmsInReceivingOrderDet> wmsInReceivingOrderDets = wmsInReceivingOrderDetMapper.selectByIds(ids);
        Map<String,List<WmsInReceivingOrderDet>> map = new HashMap<>();
        for (WmsInReceivingOrderDet wmsInReceivingOrderDet : wmsInReceivingOrderDets) {
            if(wmsInReceivingOrderDet.getIfAllIssued()==1){
                throw new BizErrorException("重复下推");
            }
            SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
            searchBaseOrderFlow.setOrderTypeCode("IN-SWK");
            searchBaseOrderFlow.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
            BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
            if(StringUtils.isEmpty(baseOrderFlow)){
                throw new BizErrorException("未找到当前单据配置的下游单据");
            }else if (map.containsKey(baseOrderFlow.getNextOrderTypeCode())){
                List<WmsInReceivingOrderDet> list = map.get(baseOrderFlow.getNextOrderTypeCode());
                list.add(wmsInReceivingOrderDet);
                map.put(baseOrderFlow.getNextOrderTypeCode(),list);
            }else {
                List<WmsInReceivingOrderDet> list = new ArrayList<>();
                list.add(wmsInReceivingOrderDet);
                map.put(baseOrderFlow.getNextOrderTypeCode(),list);
            }
        }

        Set<String > set = map.keySet();
        for (String s : set) {
            List<WmsInReceivingOrderDet> list = map.get(s);
            WmsInReceivingOrder wmsInReceivingOrder = wmsInReceivingOrderMapper.selectByPrimaryKey(list.get(0).getReceivingOrderId());
            if(wmsInReceivingOrder.getOrderStatus()!=3){
                throw new BizErrorException("单据未完成收货，下推失败");
            }
            sysOrderTypeCode = wmsInReceivingOrder.getSysOrderTypeCode();

            if(StringUtils.isNotEmpty(wmsInReceivingOrder.getCoreSourceSysOrderTypeCode())){
                coreSourceSysOrderTypeCode = wmsInReceivingOrder.getCoreSourceSysOrderTypeCode();
            }
            if(StringUtils.isEmpty(coreSourceSysOrderTypeCode)){
                coreSourceSysOrderTypeCode="IN-SWK";
            }
            switch (s){
                case "QMS-MIIO":
                    //来料检验
                    List<QmsIncomingInspectionOrderDto> qmsIncomingInspectionOrders = new ArrayList<>();
                    for (WmsInReceivingOrderDet wmsInReceivingOrderDet : list) {

                        QmsIncomingInspectionOrderDto qmsIncomingInspectionOrder = new QmsIncomingInspectionOrderDto();
                        qmsIncomingInspectionOrder.setCoreSourceOrderCode(wmsInReceivingOrderDet.getCoreSourceOrderCode());
                        qmsIncomingInspectionOrder.setSourceOrderCode(wmsInReceivingOrder.getReceivingOrderCode());
                        qmsIncomingInspectionOrder.setWarehouseId(wmsInReceivingOrder.getWarehouseId());
                        qmsIncomingInspectionOrder.setSourceId(wmsInReceivingOrderDet.getReceivingOrderDetId());
                        qmsIncomingInspectionOrder.setCoreSourceId(wmsInReceivingOrderDet.getCoreSourceId());
                        qmsIncomingInspectionOrder.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
                        qmsIncomingInspectionOrder.setOrderQty(wmsInReceivingOrderDet.getPlanQty());
                        qmsIncomingInspectionOrder.setInspectionStatus((byte)1);
                        qmsIncomingInspectionOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                        qmsIncomingInspectionOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                        qmsIncomingInspectionOrders.add(qmsIncomingInspectionOrder);
                    }
                    ResponseEntity responseEntity =qmsFeignApi.batchAdd(qmsIncomingInspectionOrders);
                    if(responseEntity.getCode()!=0){
                        throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                    }
                    break;
                case "IN-IWK":
                    //上架作业
                    //生成上架作业单
                    //获取默认收货库位
                    SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                    searchBaseStorage.setWarehouseId(wmsInReceivingOrder.getWarehouseId());
                    searchBaseStorage.setStorageType((byte)2);
                    List<BaseStorage> storageList = baseFeignApi.findList(searchBaseStorage).getData();
                    if(storageList.size()<1){
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未维护仓库收货库位");
                    }

                    List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                    int lineNumber = 1;
                    for (WmsInReceivingOrderDet wmsInReceivingOrderDet : list) {
                        WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                        wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsInReceivingOrderDet.getCoreSourceOrderCode());
                        wmsInnerJobOrderDet.setSourceOrderCode(wmsInReceivingOrder.getReceivingOrderCode());
                        wmsInnerJobOrderDet.setSourceId(wmsInReceivingOrderDet.getReceivingOrderDetId());

                        Long coreSourceId=null;
                        if(StringUtils.isNotEmpty(wmsInReceivingOrderDet.getCoreSourceId())){
                            coreSourceId=wmsInReceivingOrderDet.getCoreSourceId();
                        }
                        if(StringUtils.isEmpty(coreSourceId)){
                            coreSourceId=wmsInReceivingOrderDet.getReceivingOrderDetId();
                        }
                        wmsInnerJobOrderDet.setCoreSourceId(coreSourceId);

                        wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                        wmsInnerJobOrderDet.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
                        wmsInnerJobOrderDet.setPlanQty(wmsInReceivingOrderDet.getPlanQty());
                        wmsInnerJobOrderDet.setLineStatus((byte)1);
                        wmsInnerJobOrderDet.setOutStorageId(storageList.get(0).getStorageId());
                        detList.add(wmsInnerJobOrderDet);
                        lineNumber++;
                    }
                    WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                    wmsInnerJobOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                    wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                    wmsInnerJobOrder.setJobOrderType((byte)1);
                    wmsInnerJobOrder.setWarehouseId(wmsInReceivingOrder.getWarehouseId());
                    wmsInnerJobOrder.setOrderStatus((byte)1);
                    wmsInnerJobOrder.setCreateUserId(sysUser.getUserId());
                    wmsInnerJobOrder.setCreateTime(new Date());
                    wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrder.setModifiedTime(new Date());
                    wmsInnerJobOrder.setStatus((byte)1);
                    wmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
                    wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);
                    wmsInnerJobOrder.setSourceBigType((byte)1);

                    ResponseEntity rs = innerFeignApi.add(wmsInnerJobOrder);
                    if(rs.getCode() != 0){
                        throw new BizErrorException(rs.getCode(),rs.getMessage());
                    }
                    break;
                case "IN-IPO":
                    //入库计划
                    //获取默认收货库位
                    SearchBaseStorage searchBaseStorage1 = new SearchBaseStorage();
                    searchBaseStorage1.setWarehouseId(wmsInReceivingOrder.getWarehouseId());
                    searchBaseStorage1.setStorageType((byte)2);
                    List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage1).getData();
                    if(baseStorages.size()<1){
                        throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未维护仓库收货库位");
                    }
                    List<WmsInInPlanOrderDetDto> wmsInInPlanOrderDetDtos = new LinkedList<>();
                    for (WmsInReceivingOrderDet wmsInReceivingOrderDet : list) {

                        WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto = new WmsInInPlanOrderDetDto();
                        wmsInInPlanOrderDetDto.setCoreSourceOrderCode(wmsInReceivingOrderDet.getCoreSourceOrderCode());
                        wmsInInPlanOrderDetDto.setSourceOrderCode(wmsInReceivingOrder.getReceivingOrderCode());
                        wmsInInPlanOrderDetDto.setMaterialId(wmsInReceivingOrderDet.getMaterialId());
                        wmsInInPlanOrderDetDto.setSourceId(wmsInReceivingOrderDet.getReceivingOrderDetId());

                        Long coreSourceId=null;
                        if(StringUtils.isNotEmpty(wmsInReceivingOrderDet.getCoreSourceId())){
                            coreSourceId=wmsInReceivingOrderDet.getCoreSourceId();
                        }
                        if(StringUtils.isEmpty(coreSourceId)){
                            coreSourceId=wmsInReceivingOrderDet.getReceivingOrderDetId();
                        }
                        wmsInInPlanOrderDetDto.setCoreSourceId(coreSourceId);

                        wmsInInPlanOrderDetDto.setBatchCode(wmsInReceivingOrderDet.getBatchCode());
                        wmsInInPlanOrderDetDto.setPlanQty(wmsInReceivingOrderDet.getActualQty());
                        wmsInInPlanOrderDetDto.setLineNumber(wmsInReceivingOrderDet.getLineNumber());
                        wmsInInPlanOrderDetDto.setLineStatus((byte)1);
                        wmsInInPlanOrderDetDto.setPutawayQty(BigDecimal.ZERO);
                        wmsInInPlanOrderDetDtos.add(wmsInInPlanOrderDetDto);
                    }
                    WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
                    wmsInInPlanOrder.setWarehouseId(wmsInReceivingOrder.getWarehouseId());
                    wmsInInPlanOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                    wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                    wmsInInPlanOrder.setOrderStatus((byte)1);
                    wmsInInPlanOrder.setSourceBigType((byte)1);
                    wmsInInPlanOrder.setMakeOrderUserId(sysUser.getUserId());
                    wmsInInPlanOrder.setStorageId(baseStorages.get(0).getStorageId());
                    wmsInInPlanOrder.setWmsInInPlanOrderDetDtos(wmsInInPlanOrderDetDtos);
                    wmsInInPlanOrderService.save(wmsInInPlanOrder);
                    break;
                default:
                    throw new BizErrorException("单据流配置错误");
            }
        }
        int num = 0;
        if(set.size()>0){
            for (WmsInReceivingOrderDet wmsInReceivingOrderDet : wmsInReceivingOrderDets) {
                wmsInReceivingOrderDet.setIfAllIssued((byte)1);
                wmsInReceivingOrderDet.setLineStatus((byte)2);
                num = wmsInReceivingOrderDetMapper.updateByPrimaryKeySelective(wmsInReceivingOrderDet);
            }
        }
        return num;
    }

    @Override
    public List<WmsInReceivingOrderBarcode> scanBarcode(String barcode) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode=new SearchWmsInnerMaterialBarcode();
        searchWmsInnerMaterialBarcode.setBarcode(barcode);
        searchWmsInnerMaterialBarcode.setOrgId(sysUser.getOrganizationId());
        searchWmsInnerMaterialBarcode.setCodeQueryMark(1);
        List<WmsInnerMaterialBarcodeDto> barcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
        WmsInReceivingOrderBarcode wmsInReceivingOrderBarcode = new WmsInReceivingOrderBarcode();
        if(barcodeDtos.size()>0){
            //SN码
            if(barcodeDtos.get(0).getBarcodeStatus()>=(byte)3){
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
            }
            wmsInReceivingOrderBarcode.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
            wmsInReceivingOrderBarcode.setBarcodeType((byte)1);
            wmsInReceivingOrderBarcode.setMaterialQty(barcodeDtos.get(0).getMaterialQty());
            wmsInReceivingOrderBarcode.setBarcode(barcode);
            wmsInReceivingOrderBarcode.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
            wmsInReceivingOrderBarcode.setMaterialId(barcodeDtos.get(0).getMaterialId());
            wmsInReceivingOrderBarcode.setMaterialCode(barcodeDtos.get(0).getMaterialCode());
            wmsInReceivingOrderBarcode.setMaterialBarcodeDtoList(barcodeDtos);
        }else {
            //彩盒
            searchWmsInnerMaterialBarcode.setBarcode(null);
            searchWmsInnerMaterialBarcode.setColorBoxCode(barcode);
            barcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
            if(barcodeDtos.size()>0){
                List<WmsInnerMaterialBarcodeDto> barcodeListOne = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                if(StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size()<=0){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到彩盒码 请确认-->"+barcode);
                }
                if(barcodeListOne.get(0).getBarcodeStatus()>=(byte)3){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
                }
                wmsInReceivingOrderBarcode.setBarcodeType((byte)2);
                wmsInReceivingOrderBarcode.setMaterialQty(barcodeDtos.get(0).getMaterialQty());
                wmsInReceivingOrderBarcode.setBarcode(barcode);
                wmsInReceivingOrderBarcode.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
                wmsInReceivingOrderBarcode.setMaterialId(barcodeDtos.get(0).getMaterialId());
                wmsInReceivingOrderBarcode.setMaterialCode(barcodeDtos.get(0).getMaterialCode());
                wmsInReceivingOrderBarcode.setMaterialBarcodeDtoList(barcodeListOne);
            }else {
                //箱码
                searchWmsInnerMaterialBarcode.setBarcode(null);
                searchWmsInnerMaterialBarcode.setColorBoxCode(null);
                searchWmsInnerMaterialBarcode.setCartonCode(barcode);
                barcodeDtos=innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
                if(barcodeDtos.size()>0){
                    List<WmsInnerMaterialBarcodeDto> barcodeListOne = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                    if(StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size()<=0){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到箱码 请确认-->"+barcode);
                    }
                    if(barcodeListOne.get(0).getBarcodeStatus()>=(byte)3){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
                    }
                    List<WmsInnerMaterialBarcodeDto> barcodeList = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                    BigDecimal totalQty=barcodeList.stream().map(WmsInnerMaterialBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                    wmsInReceivingOrderBarcode.setBarcodeType((byte)3);
                    wmsInReceivingOrderBarcode.setMaterialQty(totalQty);
                    wmsInReceivingOrderBarcode.setBarcode(barcode);
                    wmsInReceivingOrderBarcode.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
                    wmsInReceivingOrderBarcode.setMaterialId(barcodeDtos.get(0).getMaterialId());
                    wmsInReceivingOrderBarcode.setMaterialCode(barcodeDtos.get(0).getMaterialCode());
                    wmsInReceivingOrderBarcode.setMaterialBarcodeDtoList(barcodeListOne);
                }else {
                    //栈板
                    searchWmsInnerMaterialBarcode.setBarcode(null);
                    searchWmsInnerMaterialBarcode.setColorBoxCode(null);
                    searchWmsInnerMaterialBarcode.setCartonCode(null);
                    searchWmsInnerMaterialBarcode.setPalletCode(barcode);
                    barcodeDtos=innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
                    if(barcodeDtos.size()>0){
                        List<WmsInnerMaterialBarcodeDto> barcodeListOne = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                        if(StringUtils.isEmpty(barcodeListOne) || barcodeListOne.size()<=0){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未找到栈板 请确认-->"+barcode);
                        }
                        if(barcodeListOne.get(0).getBarcodeStatus()>=(byte)3){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"条码已扫描 请勿重复扫码-->"+barcode);
                        }
                        List<WmsInnerMaterialBarcodeDto> barcodeList = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())!="")).collect(Collectors.toList());
                        BigDecimal totalQty=barcodeList.stream().map(WmsInnerMaterialBarcodeDto::getMaterialQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                        wmsInReceivingOrderBarcode.setBarcodeType((byte)4);
                        wmsInReceivingOrderBarcode.setMaterialQty(totalQty);
                        wmsInReceivingOrderBarcode.setBarcode(barcode);
                        wmsInReceivingOrderBarcode.setMaterialBarcodeId(barcodeDtos.get(0).getMaterialBarcodeId());
                        wmsInReceivingOrderBarcode.setMaterialId(barcodeDtos.get(0).getMaterialId());
                        wmsInReceivingOrderBarcode.setMaterialCode(barcodeDtos.get(0).getMaterialCode());
                        wmsInReceivingOrderBarcode.setMaterialBarcodeDtoList(barcodeListOne);
                    }else {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"条码不存在");
                    }
                }
            }
        }

        SearchWmsInnerMaterialBarcodeReOrder sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
        sBarcodeReOrder.setMaterialBarcodeId(wmsInReceivingOrderBarcode.getMaterialBarcodeId());
        List<WmsInnerMaterialBarcodeReOrderDto> reOrderList=innerFeignApi.findList(sBarcodeReOrder).getData();
        if(reOrderList.size()<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"条码关系未找到此条码数据-->"+barcode);
        }

        Optional<WmsInnerMaterialBarcodeReOrderDto> barcodeReOrderDtoLast = reOrderList.stream()
                .sorted(Comparator.comparing(WmsInnerMaterialBarcodeReOrderDto::getCreateTime).reversed())
                .findFirst();
        if (barcodeReOrderDtoLast.isPresent()) {
            WmsInnerMaterialBarcodeReOrderDto reOrderDtoLast = barcodeReOrderDtoLast.get();
            if(reOrderDtoLast.getScanStatus()>(byte)1){
                throw new BizErrorException(ErrorCodeEnum.PDA40012001.getCode(),"条码已扫描-->"+barcode);
            }

        }

        //校验子级是否已经扫描
        List<WmsInnerMaterialBarcodeDto> barcodeList=wmsInReceivingOrderBarcode.getMaterialBarcodeDtoList();
        if(StringUtils.isNotEmpty(barcodeList) && barcodeList.size()>0) {
            for (WmsInnerMaterialBarcodeDto materialBarcodeDto : barcodeList) {
                sBarcodeReOrder=new SearchWmsInnerMaterialBarcodeReOrder();
                sBarcodeReOrder.setOrderTypeCode("IN-SWK");
                sBarcodeReOrder.setMaterialBarcodeId(materialBarcodeDto.getMaterialBarcodeId());
                List<WmsInnerMaterialBarcodeReOrderDto> barcodeReOrder=innerFeignApi.findList(sBarcodeReOrder).getData();
                if(StringUtils.isNotEmpty(barcodeReOrder)){
                    if(StringUtils.isNotEmpty(barcodeReOrder.get(0).getIfScan()) && barcodeReOrder.get(0).getIfScan()==(byte)1){
                        if(wmsInReceivingOrderBarcode.getBarcodeType()==(byte)1){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"SN条码已扫描 请勿重复扫描");
                        }else if(wmsInReceivingOrderBarcode.getBarcodeType()==(byte)2){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"SN条码已扫描 请勿再扫描彩盒码");
                        }else if(wmsInReceivingOrderBarcode.getBarcodeType()==(byte)3){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"SN条码已扫描 请勿再扫描箱码");
                        }else if(wmsInReceivingOrderBarcode.getBarcodeType()==(byte)4){
                            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"SN条码已扫描 请勿再扫描栈板码");
                        }
                    }
                }
            }
        }

        List<WmsInReceivingOrderBarcode> list = new ArrayList<>();
        list.add(wmsInReceivingOrderBarcode);
        return list;
    }
}

package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetImport;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderMapper;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderDetMapper;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetService;
import com.fantechs.provider.srm.util.OrderFlowUtil;
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
public class SrmInAsnOrderDetServiceImpl extends BaseService<SrmInAsnOrderDet> implements SrmInAsnOrderDetService {

    @Resource
    private SrmInAsnOrderDetMapper srmInAsnOrderDetMapper;
    @Resource
    private SrmInAsnOrderMapper srmInAsnOrderMapper;
    @Resource
    private SrmInHtAsnOrderDetMapper srmInHtAsnOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private OMFeignApi oMFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;

    @Override
    public List<SrmInAsnOrderDetDto> findList(Map<String, Object> map) {
        return srmInAsnOrderDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pushDown(List<SrmInAsnOrderDetDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //获取下推顺序
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("asnOrder");
        List<SysSpecItem> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (StringUtils.isEmpty(specItemList)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"ASN单作用顺序未找到，请在程序配置项设置");
        }else if ("1".equals(specItemList.get(0).getParaValue())) {
            //单据流配置查询实体
            SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
            searchBaseOrderFlow.setOrderTypeCode("SRM-ASN");
            searchBaseOrderFlow.setStatus((byte) 1);
            List<BaseOrderFlowDto> data = baseFeignApi.findAll(searchBaseOrderFlow).getData();
            if (StringUtils.isEmpty(data)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到当前节点的单据流配置");
            }

            //库位查询实体
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();

            //收货计划表头
            WmsInPlanReceivingOrder wmsInPlanReceivingOrder = new WmsInPlanReceivingOrder();
            //明细
            List<WmsInPlanReceivingOrderDetDto> wmsInPlanReceivingOrderDetList = new ArrayList<>();

            //收货作业表头
            WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
            //明细
            List<WmsInReceivingOrderDetDto> wmsInReceivingOrderDetList = new ArrayList<>();

            //来料检验
            List<QmsIncomingInspectionOrderDto> qmsIncomingInspectionOrderList = new ArrayList<>();

            //上架作业
            WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
            //明细
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDetList = new ArrayList<>();

            //入库计划
            WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
            //明细
            List<WmsInInPlanOrderDetDto> wmsInInPlanOrderDetList = new ArrayList<>();

            if (StringUtils.isNotEmpty(list)) {
                SrmInAsnOrderDetDto srmInAsnOrderDet = list.get(0);
                Example example = new Example(SrmInAsnOrder.class);
                example.createCriteria().andEqualTo("asnOrderId",srmInAsnOrderDet.getAsnOrderId());
                SrmInAsnOrder srmInAsnOrder = srmInAsnOrderMapper.selectOneByExample(example);

                for (SrmInAsnOrderDetDto srmInAsnOrderDetDto : list) {

                    //判断下推数量是否满足
                    BigDecimal totalIssueQty = StringUtils.isEmpty(srmInAsnOrderDetDto.getTotalIssueQty())?new BigDecimal(0):srmInAsnOrderDetDto.getTotalIssueQty();
                    BigDecimal total = totalIssueQty.add(srmInAsnOrderDetDto.getIssueQty());
                    if (srmInAsnOrderDetDto.getDeliveryQty().compareTo(total) == -1) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"下推失败，下推数量超出本次交货数量");
                    } else if (srmInAsnOrderDetDto.getDeliveryQty().compareTo(total) == 0) {
                        srmInAsnOrderDetDto.setIfAllIssued((byte) 1);
                    }else {
                        srmInAsnOrderDetDto.setIfAllIssued((byte) 0);
                    }
                    srmInAsnOrderDetDto.setTotalIssueQty(total);
                    srmInAsnOrderDetMapper.updateByPrimaryKeySelective(srmInAsnOrderDetDto);

                    //获取单据流配置
                    BaseOrderFlow baseOrderFlowDto = OrderFlowUtil.getOrderFlow(data,srmInAsnOrderDetDto.getMaterialId(),srmInAsnOrderDetDto.getSupplierId());
                    if (StringUtils.isEmpty(baseOrderFlowDto)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"请配置单据流设置");
                    }

                    //判断下推单据
                    if ("IN-SPO".equals(baseOrderFlowDto.getNextOrderTypeCode())) {
                        wmsInPlanReceivingOrder.setWarehouseId(srmInAsnOrderDetDto.getWarehouseId());
                        wmsInPlanReceivingOrder.setOrderStatus((byte) 1);
                        wmsInPlanReceivingOrder.setCoreSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInPlanReceivingOrder.setSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInPlanReceivingOrder.setSourceBigType((byte) 1);
                        wmsInPlanReceivingOrder.setSourceSysOrderTypeCode("SRM-ASN");

                        WmsInPlanReceivingOrderDetDto wmsInPlanReceivingOrderDetDto = new WmsInPlanReceivingOrderDetDto();
                        wmsInPlanReceivingOrderDetDto.setCoreSourceOrderCode(srmInAsnOrderDetDto.getCoreSourceOrderCode());
                        wmsInPlanReceivingOrderDetDto.setCoreSourceId(srmInAsnOrderDetDto.getCoreSourceId());
                        wmsInPlanReceivingOrderDetDto.setSourceOrderCode(srmInAsnOrderDetDto.getAsnCode());
                        wmsInPlanReceivingOrderDetDto.setSourceId(srmInAsnOrderDetDto.getAsnOrderDetId());
                        wmsInPlanReceivingOrderDetDto.setMaterialId(srmInAsnOrderDetDto.getMaterialId());
                        wmsInPlanReceivingOrderDetDto.setBatchCode(srmInAsnOrderDetDto.getBatchCode());
                        wmsInPlanReceivingOrderDetDto.setLineStatus((byte) 1);
                        wmsInPlanReceivingOrderDetDto.setPlanQty(srmInAsnOrderDetDto.getIssueQty());

                        wmsInPlanReceivingOrderDetList.add(wmsInPlanReceivingOrderDetDto);


                    }else if ("IN-SWK".equals(baseOrderFlowDto.getNextOrderTypeCode())) {
                        wmsInReceivingOrder.setWarehouseId(srmInAsnOrderDetDto.getWarehouseId());
                        wmsInReceivingOrder.setOrderStatus((byte) 1);
                        wmsInReceivingOrder.setCoreSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInReceivingOrder.setSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInReceivingOrder.setSourceBigType((byte) 1);
                        wmsInReceivingOrder.setSourceSysOrderTypeCode("SRM-ASN");

                        WmsInReceivingOrderDetDto wmsInReceivingOrderDetDto = new WmsInReceivingOrderDetDto();
                        wmsInReceivingOrderDetDto.setCoreSourceOrderCode(srmInAsnOrderDetDto.getCoreSourceOrderCode());
                        wmsInReceivingOrderDetDto.setCoreSourceId(srmInAsnOrderDetDto.getCoreSourceId());
                        wmsInReceivingOrderDetDto.setSourceOrderCode(srmInAsnOrderDetDto.getAsnCode());
                        wmsInReceivingOrderDetDto.setSourceId(srmInAsnOrderDetDto.getAsnOrderDetId());
                        wmsInReceivingOrderDetDto.setMaterialId(srmInAsnOrderDetDto.getMaterialId());
                        wmsInReceivingOrderDetDto.setBatchCode(srmInAsnOrderDetDto.getBatchCode());
                        wmsInReceivingOrderDetDto.setLineStatus((byte) 1);
                        wmsInReceivingOrderDetDto.setPlanQty(srmInAsnOrderDetDto.getIssueQty());
                        wmsInReceivingOrderDetList.add(wmsInReceivingOrderDetDto);

                    }else if ("QMS-MIIO".equals(baseOrderFlowDto.getNextOrderTypeCode())) {
                        QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                        qmsIncomingInspectionOrderDto.setSourceId(srmInAsnOrderDetDto.getAsnOrderDetId());
                        qmsIncomingInspectionOrderDto.setSourceOrderCode(srmInAsnOrderDetDto.getAsnCode());
                        qmsIncomingInspectionOrderDto.setSourceSysOrderTypeCode("SRM-ASN");
                        qmsIncomingInspectionOrderDto.setSourceBigType((byte) 1);
//                        qmsIncomingInspectionOrderDto.setSourceSysOrderTypeCode();
                        qmsIncomingInspectionOrderDto.setCoreSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(srmInAsnOrderDetDto.getCoreSourceOrderCode());
                        qmsIncomingInspectionOrderDto.setMaterialId(srmInAsnOrderDetDto.getMaterialId());
                        qmsIncomingInspectionOrderDto.setSupplierId(srmInAsnOrderDetDto.getSupplierId());
                        qmsIncomingInspectionOrderDto.setWarehouseId(srmInAsnOrderDetDto.getWarehouseId());
                        qmsIncomingInspectionOrderDto.setOrderQty(srmInAsnOrderDetDto.getIssueQty());
                        qmsIncomingInspectionOrderDto.setInspectionStatus((byte) 1);
                        qmsIncomingInspectionOrderList.add(qmsIncomingInspectionOrderDto);
                    }else if ("IN-IPO".equals(baseOrderFlowDto.getNextOrderTypeCode())) {
                        wmsInInPlanOrder.setWarehouseId(srmInAsnOrderDetDto.getWarehouseId());
                        wmsInInPlanOrder.setOrderStatus((byte) 1);
                        wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInInPlanOrder.setSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInInPlanOrder.setSourceSysOrderTypeCode("SRM-ASN");

                        WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto = new WmsInInPlanOrderDetDto();
                        wmsInInPlanOrderDetDto.setCoreSourceOrderCode(srmInAsnOrderDetDto.getCoreSourceOrderCode());
                        wmsInInPlanOrderDetDto.setSourceOrderCode(srmInAsnOrderDetDto.getAsnCode());
                        wmsInInPlanOrderDetDto.setSourceId(srmInAsnOrderDetDto.getAsnOrderDetId());
                        wmsInInPlanOrderDetDto.setMaterialId(srmInAsnOrderDetDto.getMaterialId());
                        wmsInInPlanOrderDetDto.setPlanQty(srmInAsnOrderDetDto.getIssueQty());
                        wmsInInPlanOrderDetDto.setLineStatus((byte) 1);

                        wmsInInPlanOrderDetList.add(wmsInInPlanOrderDetDto);

                    }else if ("IN-IWK".equals(baseOrderFlowDto.getNextOrderTypeCode())) {
                        wmsInnerJobOrder.setWarehouseId(srmInAsnOrderDetDto.getWarehouseId());
                        wmsInnerJobOrder.setJobOrderType((byte) 1);
                        wmsInnerJobOrder.setOrderStatus((byte) 1);
                        wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInnerJobOrder.setSourceSysOrderTypeCode(srmInAsnOrder.getSourceSysOrderTypeCode());
                        wmsInnerJobOrder.setSourceBigType((byte) 1);
                        wmsInnerJobOrder.setSourceSysOrderTypeCode("SRM-ASN");

                        searchBaseStorage.setWarehouseId(srmInAsnOrderDetDto.getWarehouseId());
                        searchBaseStorage.setStorageType((byte) 2);

                        List<BaseStorage> storageList = baseFeignApi.findList(searchBaseStorage).getData();
                        if (StringUtils.isEmpty(storageList)) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到上架储位");
                        }

                        WmsInnerJobOrderDet wmsInnerJobOrderDet = WmsInnerJobOrderDet.builder()
                                .coreSourceOrderCode(srmInAsnOrderDetDto.getCoreSourceOrderCode())
                                .coreSourceId(srmInAsnOrderDetDto.getCoreSourceId())
                                .sourceOrderCode(srmInAsnOrderDetDto.getAsnCode())
                                .sourceId(srmInAsnOrderDetDto.getAsnOrderDetId())
                                .planQty(srmInAsnOrderDetDto.getIssueQty())
                                .materialId(srmInAsnOrderDetDto.getMaterialId())
                                .lineStatus((byte) 1)
                                .outStorageId(StringUtils.isNotEmpty(storageList)?storageList.get(0).getStorageId():null)
                                .batchCode(srmInAsnOrderDetDto.getBatchCode()).build();

                        wmsInnerJobOrderDetList.add(wmsInnerJobOrderDet);

                    }
                }


                if (StringUtils.isNotEmpty(wmsInPlanReceivingOrderDetList)) {
                    wmsInPlanReceivingOrder.setInPlanReceivingOrderDets(wmsInPlanReceivingOrderDetList);
                    inFeignApi.add(wmsInPlanReceivingOrder);
                }
                if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderList)) {
                    qmsFeignApi.batchAdd(qmsIncomingInspectionOrderList);
                }
                if (StringUtils.isNotEmpty(wmsInnerJobOrderDetList)) {
                    wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDetList);
                    innerFeignApi.add(wmsInnerJobOrder);
                }
                if (StringUtils.isNotEmpty(wmsInInPlanOrderDetList)) {
                    wmsInInPlanOrder.setWmsInInPlanOrderDetDtos(wmsInInPlanOrderDetList);
                    inFeignApi.add(wmsInInPlanOrder);
                }
                if (StringUtils.isNotEmpty(wmsInReceivingOrderDetList)) {
                    wmsInReceivingOrder.setWmsInReceivingOrderDets(wmsInReceivingOrderDetList);
                    inFeignApi.add(wmsInReceivingOrder);
                }
                srmInAsnOrderDetMapper.batchUpdate(list);
            }
        }
        return 1;
    }

    @Override
    public List<SrmInAsnOrderDetDto> importExcels(List<SrmInAsnOrderDetImport> srmInAsnOrderDetImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SrmInAsnOrderDetDto> list = new LinkedList<>();
        LinkedList<SrmInHtAsnOrderDet> htList = new LinkedList<>();
        for (int i = 0; i < srmInAsnOrderDetImports.size(); i++) {
            SrmInAsnOrderDetImport srmInAsnOrderDetImport = srmInAsnOrderDetImports.get(i);

            //判断非空
            String purchaseOrderCode = srmInAsnOrderDetImport.getPurchaseOrderCode();
            String materialCode = srmInAsnOrderDetImport.getMaterialCode();
            String warehouseName = srmInAsnOrderDetImport.getWarehouseName();
            if (StringUtils.isEmpty(
                    purchaseOrderCode,materialCode,warehouseName
            )){
                  throw new BizErrorException("添加失败，采购单号、物料号、仓库名称不能为空,"+"错误行数为:"+(i+2));
               /* fail.add(i+2);
                continue;*/
            }

            SrmInAsnOrderDetDto srmInAsnOrderDetDto = new SrmInAsnOrderDetDto();
            BeanUtils.copyProperties(srmInAsnOrderDetImport, srmInAsnOrderDetDto);

            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(srmInAsnOrderDetImport.getMaterialCode());
            searchBaseMaterial.setOrgId(user.getOrganizationId());
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)){
                   if(StringUtils.isEmpty(baseMaterials)) throw new BizErrorException("未查询到物料编码为"+srmInAsnOrderDetImport.getMaterialCode()+"的信息");
                /*fail.add(i+2);
                continue;*/
            }
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(srmInAsnOrderDetImport.getWarehouseName());
            searchBaseWarehouse.setOrgId(user.getOrganizationId());
            List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
            if (StringUtils.isEmpty(baseWarehouses)){
                    throw new BizErrorException("未查询到仓库编码为"+srmInAsnOrderDetImport.getWarehouseName()+"的信息");
                //fail.add(i+2);
                //continue;
            }

            SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();
            searchOmPurchaseOrderDet.setOrgId(user.getOrganizationId());
            searchOmPurchaseOrderDet.setPurchaseOrderCode(purchaseOrderCode);
            List<OmPurchaseOrderDetDto> omPurchaseOrderDetDtos = oMFeignApi.findList(searchOmPurchaseOrderDet).getData();
            if(StringUtils.isNotEmpty(omPurchaseOrderDetDtos)) {
                for(OmPurchaseOrderDet det : omPurchaseOrderDetDtos){
                    if(baseMaterials.get(0).getMaterialId().equals(det.getMaterialId())){
                        srmInAsnOrderDetDto.setOrderQty(det.getOrderQty());
                        srmInAsnOrderDetDto.setSourceOrderId(det.getPurchaseOrderDetId());
                    }
                }

            }else{
                throw new BizErrorException("未查询到采购订单明细为"+ purchaseOrderCode +"的信息");
            }
            if(StringUtils.isEmpty(srmInAsnOrderDetImport.getDeliveryQty()))
                srmInAsnOrderDetDto.setDeliveryQty(BigDecimal.ZERO);

            srmInAsnOrderDetDto.setMaterialId(baseMaterials.get(0).getMaterialId());
            srmInAsnOrderDetDto.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            srmInAsnOrderDetDto.setCreateTime(new Date());
            srmInAsnOrderDetDto.setCreateUserId(user.getUserId());
            srmInAsnOrderDetDto.setModifiedTime(new Date());
            srmInAsnOrderDetDto.setModifiedUserId(user.getUserId());
            srmInAsnOrderDetDto.setStatus((byte)1);
            srmInAsnOrderDetDto.setOrgId(user.getOrganizationId());
            list.add(srmInAsnOrderDetDto);
        }

      /*  if (StringUtils.isNotEmpty(list)){
            success = srmInAsnOrderDetMapper.insertList(list);
        }

        for (SrmInAsnOrderDet srmInAsnOrderDet : list) {
            SrmInHtAsnOrderDet srmInHtAsnOrderDet = new SrmInHtAsnOrderDet();
            BeanUtils.copyProperties(srmInAsnOrderDet, srmInHtAsnOrderDet);
            htList.add(srmInHtAsnOrderDet);
        }
        if (StringUtils.isNotEmpty(htList)){
            srmInHtAsnOrderDetMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;*/
        return list;
    }

}

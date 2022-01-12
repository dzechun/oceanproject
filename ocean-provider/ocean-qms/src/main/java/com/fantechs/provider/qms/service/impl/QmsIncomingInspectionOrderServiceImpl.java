package com.fantechs.provider.qms.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.qms.imports.QmsIncomingInspectionOrderImport;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManage;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDet;
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
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.qms.mapper.*;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetService;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderService;
import com.fantechs.provider.qms.util.OrderFlowUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@Service
public class QmsIncomingInspectionOrderServiceImpl extends BaseService<QmsIncomingInspectionOrder> implements QmsIncomingInspectionOrderService {

    @Resource
    private QmsIncomingInspectionOrderMapper qmsIncomingInspectionOrderMapper;

    @Resource
    private QmsIncomingInspectionOrderDetMapper qmsIncomingInspectionOrderDetMapper;

    @Resource
    private QmsIncomingInspectionOrderDetSampleMapper qmsIncomingInspectionOrderDetSampleMapper;

    @Resource
    private QmsHtIncomingInspectionOrderMapper qmsHtIncomingInspectionOrderMapper;

    @Resource
    private QmsHtIncomingInspectionOrderDetMapper qmsHtIncomingInspectionOrderDetMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private InFeignApi inFeignApi;

    @Resource
    private InnerFeignApi innerFeignApi;

    @Resource
    private SecurityFeignApi securityFeignApi;

    @Resource
    private QmsIncomingInspectionOrderDetService qmsIncomingInspectionOrderDetService;

    @Resource
    private QmsBadnessManageMapper qmsBadnessManageMapper;

    @Override
    public List<QmsHtIncomingInspectionOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsHtIncomingInspectionOrderMapper.findHtList(map);
    }

    @Override
    public List<QmsIncomingInspectionOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIncomingInspectionOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAdd(List<QmsIncomingInspectionOrderDto> list) {
        int i = 0;

        //查询检验方式
        SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
        searchBaseInspectionWay.setInspectionType((byte)1);
        List<BaseInspectionWay> inspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
        if(StringUtils.isEmpty(inspectionWays)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到来料检验的检验方式");
        }
        BaseInspectionWay baseInspectionWay = inspectionWays.get(0);

        for (QmsIncomingInspectionOrderDto qmsIncomingInspectionOrder : list){
            if(StringUtils.isEmpty(qmsIncomingInspectionOrder.getOrderQty())){
                throw new BizErrorException("检验单总数量不能为空");
            }
            qmsIncomingInspectionOrder.setInspectionWayId(baseInspectionWay.getInspectionWayId());

            //查询检验标准
            SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
            searchBaseInspectionStandard.setInspectionType((byte)1);
            searchBaseInspectionStandard.setMaterialId(qmsIncomingInspectionOrder.getMaterialId());
            searchBaseInspectionStandard.setSupplierId(qmsIncomingInspectionOrder.getSupplierId());
            searchBaseInspectionStandard.setInspectionWayId(qmsIncomingInspectionOrder.getInspectionWayId());
            searchBaseInspectionStandard.setIfContainCommon(1);
            List<BaseInspectionStandard> inspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
            if (StringUtils.isEmpty(inspectionStandards)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到检验标准");
            }
            //按物料id倒序，物料id为0的是通用检验标准
            List<BaseInspectionStandard> collect = inspectionStandards.stream().sorted(Comparator.comparing(BaseInspectionStandard::getMaterialId).reversed()).collect(Collectors.toList());
            BaseInspectionStandard baseInspectionStandard = collect.get(0);
            qmsIncomingInspectionOrder.setInspectionStandardId(baseInspectionStandard.getInspectionStandardId());

            //根据检验标准及总数量带出明细
            List<QmsIncomingInspectionOrderDet> dets = qmsIncomingInspectionOrderDetService.showOrderDet(baseInspectionStandard.getInspectionStandardId(), qmsIncomingInspectionOrder.getOrderQty());
            qmsIncomingInspectionOrder.setList(dets);
            i += this.save(qmsIncomingInspectionOrder);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int pushDown(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //根据单据流生成入库计划单或上架作业单
        int i = 0;
        List<QmsIncomingInspectionOrder> qmsIncomingInspectionOrders = qmsIncomingInspectionOrderMapper.selectByIds(ids);
        for (QmsIncomingInspectionOrder order : qmsIncomingInspectionOrders){
            if(order.getIfAllIssued() != null && order.getIfAllIssued() == (byte)1){
                throw new BizErrorException("检验单号为"+order.getIncomingInspectionOrderCode()+"的来料检验单已下推过，无法再次下推");
            }
            if(order.getMrbResult() != null && order.getMrbResult() == (byte)3){
                throw new BizErrorException("检验单号为"+order.getIncomingInspectionOrderCode()+"的来料检验单MRB评审结果为退供应商，无法下推");
            }
            if(StringUtils.isEmpty(order.getWarehouseId())){
                throw new BizErrorException("单据仓库不能为空");
            }
        }

        //查当前单据类型的所有单据流
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("QMS-MIIO");
        searchBaseOrderFlow.setStatus((byte)1);
        List<BaseOrderFlowDto> baseOrderFlowDtos = baseFeignApi.findAll(searchBaseOrderFlow).getData();
        if (StringUtils.isEmpty(baseOrderFlowDtos)) {
            throw new BizErrorException("未找到当前单据配置的单据流");
        }

        //根据仓库分组，不同仓库生成多张单
        Map<String,List<QmsIncomingInspectionOrder>> map = new HashMap<>();
        HashMap<Long, List<QmsIncomingInspectionOrder>> collect = qmsIncomingInspectionOrders.stream().collect(Collectors.groupingBy(QmsIncomingInspectionOrder::getWarehouseId, HashMap::new, Collectors.toList()));
        Set<Long> set = collect.keySet();
        for (Long id : set) {
            List<QmsIncomingInspectionOrder> incomingInspectionOrders = collect.get(id);

            //不同单据流分组
            for (QmsIncomingInspectionOrder incomingInspectionOrder : incomingInspectionOrders){
                //当前单据的下游单据
                BaseOrderFlow baseOrderFlow = OrderFlowUtil.getOrderFlow(baseOrderFlowDtos, incomingInspectionOrder.getMaterialId(), incomingInspectionOrder.getSupplierId());
                String key = id+"_"+baseOrderFlow.getNextOrderTypeCode();
                if(map.get(key)==null){
                    List<QmsIncomingInspectionOrder> diffOrderFlows = new LinkedList<>();
                    diffOrderFlows.add(incomingInspectionOrder);
                    map.put(key,diffOrderFlows);
                }else {
                    List<QmsIncomingInspectionOrder> diffOrderFlows = map.get(key);
                    diffOrderFlows.add(incomingInspectionOrder);
                    map.put(key,diffOrderFlows);
                }
            }
        }


        Set<String> codes = map.keySet();
        for (String code : codes){
            String[] split = code.split("_");
            String nextOrderTypeCode = split[1];//下游单据类型
            List<QmsIncomingInspectionOrder> orders = map.get(code);
            String sysOrderTypeCode = orders.get(0).getSysOrderTypeCode();//当前单据类型编码
            String coreSourceSysOrderTypeCode = orders.get(0).getCoreSourceSysOrderTypeCode();//核心单据类型编码

            if ("IN-IPO".equals(nextOrderTypeCode)) {
                //生成入库计划单
                List<WmsInInPlanOrderDetDto> detList = new LinkedList<>();
                int lineNumber = 1;
                for (QmsIncomingInspectionOrder qmsIncomingInspectionOrder : orders) {
                    //挑选使用的单据下推时数量用特采数量
                    if(qmsIncomingInspectionOrder.getMrbResult()!=null&&qmsIncomingInspectionOrder.getMrbResult()==(byte)2){
                        Example example = new Example(QmsBadnessManage.class);
                        example.createCriteria().andEqualTo("incomingInspectionOrderId",qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                        QmsBadnessManage qmsBadnessManage = qmsBadnessManageMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(qmsBadnessManage)){
                            throw new BizErrorException("来料检验单"+qmsIncomingInspectionOrder.getIncomingInspectionOrderCode()+"的MRB评审结果为挑选使用，请先进行挑选使用再下推");
                        }
                        qmsIncomingInspectionOrder.setOrderQty(qmsBadnessManage.getSpecialReceiveQty());
                    }
                    WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                    wmsInInPlanOrderDet.setCoreSourceOrderCode(qmsIncomingInspectionOrder.getCoreSourceOrderCode());
                    wmsInInPlanOrderDet.setSourceOrderCode(qmsIncomingInspectionOrder.getIncomingInspectionOrderCode());
                    wmsInInPlanOrderDet.setLineNumber(lineNumber + "");
                    wmsInInPlanOrderDet.setSourceId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                    wmsInInPlanOrderDet.setMaterialId(qmsIncomingInspectionOrder.getMaterialId());
                    wmsInInPlanOrderDet.setPlanQty(qmsIncomingInspectionOrder.getOrderQty());
                    wmsInInPlanOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInInPlanOrderDet);
                    lineNumber++;

                    //修改单据下发状态
                    qmsIncomingInspectionOrder.setIfAllIssued((byte)1);
                }

                WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
                wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
                wmsInInPlanOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInInPlanOrder.setWarehouseId(orders.get(0).getWarehouseId());
                wmsInInPlanOrder.setOrderStatus((byte) 1);
                wmsInInPlanOrder.setStatus((byte) 1);
                wmsInInPlanOrder.setOrgId(user.getOrganizationId());
                wmsInInPlanOrder.setWmsInInPlanOrderDetDtos(detList);

                ResponseEntity responseEntity = inFeignApi.add(wmsInInPlanOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    qmsIncomingInspectionOrderMapper.batchUpdate(orders);
                    i++;
                }
            } else if ("IN-IWK".equals(nextOrderTypeCode)) {
                //生成上架作业单
                List<WmsInnerJobOrderDet> detList = new LinkedList<>();
                int lineNumber = 1;
                for (QmsIncomingInspectionOrder qmsIncomingInspectionOrder : orders) {
                    //挑选使用的单据下推时数量用特采数量
                    if(qmsIncomingInspectionOrder.getMrbResult()!=null&&qmsIncomingInspectionOrder.getMrbResult()==(byte)2){
                        Example example = new Example(QmsBadnessManage.class);
                        example.createCriteria().andEqualTo("incomingInspectionOrderId",qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                        QmsBadnessManage qmsBadnessManage = qmsBadnessManageMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(qmsBadnessManage)){
                            throw new BizErrorException("来料检验单"+qmsIncomingInspectionOrder.getIncomingInspectionOrderCode()+"的MRB评审结果为挑选使用，请先进行挑选使用再下推");
                        }
                        qmsIncomingInspectionOrder.setOrderQty(qmsBadnessManage.getSpecialReceiveQty());
                    }
                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                    wmsInnerJobOrderDet.setCoreSourceOrderCode(qmsIncomingInspectionOrder.getCoreSourceOrderCode());
                    wmsInnerJobOrderDet.setSourceOrderCode(qmsIncomingInspectionOrder.getIncomingInspectionOrderCode());
                    wmsInnerJobOrderDet.setSourceId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                    wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
                    wmsInnerJobOrderDet.setMaterialId(qmsIncomingInspectionOrder.getMaterialId());
                    wmsInnerJobOrderDet.setPlanQty(qmsIncomingInspectionOrder.getOrderQty());
                    wmsInnerJobOrderDet.setLineStatus((byte) 1);
                    detList.add(wmsInnerJobOrderDet);
                    lineNumber++;

                    //修改单据下发状态
                    qmsIncomingInspectionOrder.setIfAllIssued((byte)1);
                }

                WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
                wmsInnerJobOrder.setSourceBigType((byte)1);
                wmsInnerJobOrder.setSourceSysOrderTypeCode(sysOrderTypeCode);
                wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                wmsInnerJobOrder.setWarehouseId(orders.get(0).getWarehouseId());
                wmsInnerJobOrder.setJobOrderType((byte) 1);
                wmsInnerJobOrder.setOrderStatus((byte) 1);
                wmsInnerJobOrder.setOrgId(user.getOrganizationId());
                wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

                ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                } else {
                    qmsIncomingInspectionOrderMapper.batchUpdate(orders);
                    i++;
                }
            } else {
                throw new BizErrorException("单据流配置错误");
            }
        }


        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int MRBReview(Long incomingInspectionOrderId, Byte mrbResult) {
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(incomingInspectionOrderId);
        if(qmsIncomingInspectionOrder.getInspectionResult()==1){
            throw new BizErrorException("该检验单检验结果为合格，无法进行MRB评审");
        }
        qmsIncomingInspectionOrder.setMrbResult(mrbResult);

        //履历
        QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
        BeanUtils.copyProperties(qmsIncomingInspectionOrder, qmsHtIncomingInspectionOrder);
        qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);

        return qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);
    }

    @Override
    public QmsIncomingInspectionOrder selectByKey(Long incomingInspectionOrderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("incomingInspectionOrderId",incomingInspectionOrderId);
        List<QmsIncomingInspectionOrderDto> list = findList(map);
        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int save(QmsIncomingInspectionOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(record.getAccessUrl())){
            BaseFile baseFile = new BaseFile();
            baseFile.setAccessUrl(record.getAccessUrl());
            ResponseEntity<BaseFile> responseEntity = baseFeignApi.add(baseFile);
            if (responseEntity.getCode() == 0) {
                BaseFile data = responseEntity.getData();
                record.setFileId(data.getFileId());
            }
        }

        //新增来料检验单
        record.setIncomingInspectionOrderCode(CodeUtils.getId("QMS-MIIO"));
        record.setSysOrderTypeCode("QMS-MIIO");
        record.setInspectionStatus(StringUtils.isEmpty(record.getInspectionStatus())?1:record.getInspectionStatus());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1:record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = qmsIncomingInspectionOrderMapper.insertUseGeneratedKeys(record);

        //新增来料检验单明细
        List<QmsHtIncomingInspectionOrderDet> htList = new LinkedList<>();
        List<QmsIncomingInspectionOrderDet> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet:list){
                qmsIncomingInspectionOrderDet.setIncomingInspectionOrderId(record.getIncomingInspectionOrderId());
                qmsIncomingInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setCreateTime(new Date());
                qmsIncomingInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setModifiedTime(new Date());
                qmsIncomingInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getStatus())?1:qmsIncomingInspectionOrderDet.getStatus());
                qmsIncomingInspectionOrderDet.setOrgId(user.getOrganizationId());

                QmsHtIncomingInspectionOrderDet qmsHtIncomingInspectionOrderDet = new QmsHtIncomingInspectionOrderDet();
                BeanUtils.copyProperties(qmsIncomingInspectionOrderDet, qmsHtIncomingInspectionOrderDet);
                htList.add(qmsHtIncomingInspectionOrderDet);
            }
            qmsIncomingInspectionOrderDetMapper.insertList(list);
            qmsHtIncomingInspectionOrderDetMapper.insertList(htList);
        }

        //插入上游单据条码
        insertMaterialBarcode(record,user);

        //履历
        QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
        BeanUtils.copyProperties(record, qmsHtIncomingInspectionOrder);
        qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int insertMaterialBarcode(QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto,SysUser user){
        int i = 0;
        if(StringUtils.isNotEmpty(qmsIncomingInspectionOrderDto.getSourceId())) {
            //上游单据所有条码
            SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
            searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode(qmsIncomingInspectionOrderDto.getSourceSysOrderTypeCode());
            searchWmsInnerMaterialBarcodeReOrder.setOrderDetId(qmsIncomingInspectionOrderDto.getSourceId());
            List<WmsInnerMaterialBarcodeReOrderDto> materialBarcodeReOrderDtos = innerFeignApi.findAll(searchWmsInnerMaterialBarcodeReOrder).getData();
            if (StringUtils.isNotEmpty(materialBarcodeReOrderDtos)) {
                //条码写入当前单据
                List<WmsInnerMaterialBarcodeReOrder> barcodeReOrderList = new LinkedList<>();
                for (WmsInnerMaterialBarcodeReOrderDto materialBarcodeReOrderDto : materialBarcodeReOrderDtos) {
                    WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = new WmsInnerMaterialBarcodeReOrder();
                    wmsInnerMaterialBarcodeReOrder.setMaterialBarcodeId(materialBarcodeReOrderDto.getMaterialBarcodeId());
                    wmsInnerMaterialBarcodeReOrder.setOrderTypeCode("QMS-MIIO");
                    wmsInnerMaterialBarcodeReOrder.setOrderCode(qmsIncomingInspectionOrderDto.getIncomingInspectionOrderCode());
                    wmsInnerMaterialBarcodeReOrder.setOrderId(qmsIncomingInspectionOrderDto.getIncomingInspectionOrderId());
                    wmsInnerMaterialBarcodeReOrder.setCreateTime(new Date());
                    wmsInnerMaterialBarcodeReOrder.setCreateUserId(user.getUserId());
                    wmsInnerMaterialBarcodeReOrder.setOrgId(user.getOrganizationId());
                    barcodeReOrderList.add(wmsInnerMaterialBarcodeReOrder);
                    i++;
                }
                ResponseEntity responseEntity = innerFeignApi.batchAdd(barcodeReOrderList);
                if(responseEntity.getCode() != 0){
                    throw new BizErrorException("条码写入当前单据失败");
                }
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(QmsIncomingInspectionOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(entity.getAccessUrl())){
            BaseFile baseFile = new BaseFile();
            baseFile.setAccessUrl(entity.getAccessUrl());
            ResponseEntity<BaseFile> responseEntity = baseFeignApi.add(baseFile);
            if (responseEntity.getCode() == 0) {
                BaseFile data = responseEntity.getData();
                entity.setFileId(data.getFileId());
            }
        }

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<QmsIncomingInspectionOrderDet> list = entity.getList();
        if(StringUtils.isNotEmpty(list)) {
            for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet : list) {
                if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderDetId())) {
                    qmsIncomingInspectionOrderDetMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrderDet);
                    idList.add(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderDetId());
                }
            }
        }

        //删除原明细
        Example example = new Example(QmsIncomingInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("incomingInspectionOrderId",entity.getIncomingInspectionOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("incomingInspectionOrderDetId", idList);
        }
        List<QmsIncomingInspectionOrderDet> detList = qmsIncomingInspectionOrderDetMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(detList)){
            List<Long> detIds = detList.stream().map(QmsIncomingInspectionOrderDet::getIncomingInspectionOrderDetId).collect(Collectors.toList());
            Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andIn("incomingInspectionOrderDetId",detIds);
            qmsIncomingInspectionOrderDetSampleMapper.deleteByExample(example1);
        }
        qmsIncomingInspectionOrderDetMapper.deleteByExample(example);


        //新增来料检验单明细
        List<QmsHtIncomingInspectionOrderDet> htList = new LinkedList<>();
        List<QmsIncomingInspectionOrderDet> addList = new LinkedList<>();
        if(StringUtils.isNotEmpty(list)){
            for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet:list){
                if (idList.contains(qmsIncomingInspectionOrderDet.getIncomingInspectionOrderDetId())) {
                    continue;
                }
                qmsIncomingInspectionOrderDet.setIncomingInspectionOrderId(entity.getIncomingInspectionOrderId());
                qmsIncomingInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setCreateTime(new Date());
                qmsIncomingInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setModifiedTime(new Date());
                qmsIncomingInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getStatus())?1:qmsIncomingInspectionOrderDet.getStatus());
                qmsIncomingInspectionOrderDet.setOrgId(user.getOrganizationId());
                addList.add(qmsIncomingInspectionOrderDet);

                QmsHtIncomingInspectionOrderDet qmsHtIncomingInspectionOrderDet = new QmsHtIncomingInspectionOrderDet();
                BeanUtils.copyProperties(qmsIncomingInspectionOrderDet, qmsHtIncomingInspectionOrderDet);
                htList.add(qmsHtIncomingInspectionOrderDet);
            }
            if(StringUtils.isNotEmpty(addList)){
                qmsIncomingInspectionOrderDetMapper.insertList(addList);
            }
            if(StringUtils.isNotEmpty(htList)){
                qmsHtIncomingInspectionOrderDetMapper.insertList(htList);
            }
        }else{
            entity.setInspectionStatus((byte)1);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(entity);

        //返写单据信息
        checkInspectionResult(list);

        //履历
        QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
        BeanUtils.copyProperties(entity, qmsHtIncomingInspectionOrder);
        qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);

        return i;
    }

    /**
     *  根据检验单明细返写检验单检验结果
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void checkInspectionResult(List<QmsIncomingInspectionOrderDet> list){
        if(StringUtils.isNotEmpty(list)) {
            Byte inspectionResult = 1;
            int mustInspectioncount = 0;//必检项目数
            int haveInspectioncount = 0;//已检验项目数
            int mustAndHaveInspectioncount = 0;//必检且已检验项目数
            int badnessCategoryIsNull = 0;//项目不合格且未设置不良类别数
            for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet : list) {
                if (qmsIncomingInspectionOrderDet.getIfMustInspection() == 1) {
                    mustInspectioncount++;
                }
                if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderDet.getInspectionResult())) {
                    haveInspectioncount++;
                }
                if (qmsIncomingInspectionOrderDet.getIfMustInspection() == 1 &&
                        StringUtils.isNotEmpty(qmsIncomingInspectionOrderDet.getInspectionResult())) {
                    mustAndHaveInspectioncount++;
                }
                if (qmsIncomingInspectionOrderDet.getInspectionResult() != null &&
                        qmsIncomingInspectionOrderDet.getInspectionResult() == (byte) 0) {
                    inspectionResult = 0;
                }
                if(qmsIncomingInspectionOrderDet.getInspectionResult() != null &&
                        qmsIncomingInspectionOrderDet.getInspectionResult() == (byte) 0&&
                        qmsIncomingInspectionOrderDet.getBadnessCategoryId() == null){
                    badnessCategoryIsNull++;
                }
            }

            //返写检验单结果
            QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(list.get(0).getIncomingInspectionOrderId());
            if(mustInspectioncount == mustAndHaveInspectioncount) {
                qmsIncomingInspectionOrder.setInspectionResult(inspectionResult);
                //返写条码检验状态
                SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
                searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode(qmsIncomingInspectionOrder.getSysOrderTypeCode());
                searchWmsInnerMaterialBarcodeReOrder.setOrderId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                List<WmsInnerMaterialBarcodeReOrderDto> materialBarcodeReOrderDtos = innerFeignApi.findAll(searchWmsInnerMaterialBarcodeReOrder).getData();
                if(StringUtils.isEmpty(materialBarcodeReOrderDtos)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到当前单据对应的条码");
                }
                List<Long> idList = materialBarcodeReOrderDtos.stream().map(WmsInnerMaterialBarcodeReOrderDto::getMaterialBarcodeId).collect(Collectors.toList());
                updateInspectionStatus(idList,inspectionResult);
            }
            /*if(haveInspectioncount > 0){
                qmsIncomingInspectionOrder.setInspectionStatus((byte)2);
            }*/
            if(mustInspectioncount == mustAndHaveInspectioncount && badnessCategoryIsNull == 0){
                qmsIncomingInspectionOrder.setInspectionStatus((byte)3);
            }
            qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);

            //履历
            QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
            BeanUtils.copyProperties(qmsIncomingInspectionOrder, qmsHtIncomingInspectionOrder);
            qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateInspectionStatus(List<Long> idList, Byte inspectionResult) {
        int i = 0;
        SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode = new SearchWmsInnerMaterialBarcode();
        searchWmsInnerMaterialBarcode.setMaterialBarcodeIdList(idList);
        searchWmsInnerMaterialBarcode.setPageSize(9999);
        List<WmsInnerMaterialBarcodeDto> barcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
        if(StringUtils.isEmpty(barcodeDtos)){
            throw new BizErrorException("未找到条码");
        }
        for (WmsInnerMaterialBarcodeDto barcodeDto : barcodeDtos){
            barcodeDto.setInspectionStatus(inspectionResult==(byte)1 ? (byte)2 : (byte)3);
        }

        ResponseEntity responseEntity = innerFeignApi.batchUpdate(barcodeDtos);
        if(responseEntity.getCode() != 0){
            throw new BizErrorException("返写条码检验状态失败");
        }else {
            i = responseEntity.getCount();
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<QmsIncomingInspectionOrder> qmsIncomingInspectionOrders = qmsIncomingInspectionOrderMapper.selectByIds(ids);

        for (QmsIncomingInspectionOrder qmsIncomingInspectionOrder : qmsIncomingInspectionOrders){
            Example example = new Example(QmsIncomingInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("incomingInspectionOrderId",qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
            List<QmsIncomingInspectionOrderDet> detList = qmsIncomingInspectionOrderDetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(detList)){
                List<Long> detIds = detList.stream().map(QmsIncomingInspectionOrderDet::getIncomingInspectionOrderDetId).collect(Collectors.toList());
                Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andIn("incomingInspectionOrderDetId",detIds);
                qmsIncomingInspectionOrderDetSampleMapper.deleteByExample(example1);
            }
            qmsIncomingInspectionOrderDetMapper.deleteByExample(example);

            //履历
            QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
            BeanUtils.copyProperties(qmsIncomingInspectionOrder, qmsHtIncomingInspectionOrder);
            qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);
        }

        return qmsIncomingInspectionOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<QmsIncomingInspectionOrderImport> qmsIncomingInspectionOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<QmsIncomingInspectionOrder> list = new LinkedList<>();
        LinkedList<QmsHtIncomingInspectionOrder> htList = new LinkedList<>();
        LinkedList<QmsIncomingInspectionOrderImport> incomingInspectionOrderImports = new LinkedList<>();
        //日志记录
        StringBuilder succeedInfo = new StringBuilder();
        StringBuilder failInfo = new StringBuilder();
        Integer succeedCount = 0;
        Integer failCount = 0;

        for (int i = 0; i < qmsIncomingInspectionOrderImports.size(); i++) {
            QmsIncomingInspectionOrderImport qmsIncomingInspectionOrderImport = qmsIncomingInspectionOrderImports.get(i);
            String incomingInspectionOrderCode = qmsIncomingInspectionOrderImport.getIncomingInspectionOrderCode();
            String materialCode = qmsIncomingInspectionOrderImport.getMaterialCode();

            if (StringUtils.isEmpty(
                    incomingInspectionOrderCode,materialCode
            )){
                failCount++;
                failInfo.append("必填项为空").append(",");
                fail.add(i+4);
                continue;
            }

            //判断单号是否重复
            Example example = new Example(QmsIncomingInspectionOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId())
                    .andEqualTo("incomingInspectionOrderCode", incomingInspectionOrderCode);
            if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderMapper.selectOneByExample(example))){
                failCount++;
                failInfo.append("单号已存在").append(",");
                fail.add(i+4);
                continue;
            }

            //物料编码
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setCodeQueryMark(1);
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)) {
                failCount++;
                failInfo.append("物料不存在").append(",");
                fail.add(i + 4);
                continue;
            }
            qmsIncomingInspectionOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());


            //供应商编码
            String supplierCode = qmsIncomingInspectionOrderImport.getSupplierCode();
            if(StringUtils.isNotEmpty(supplierCode)){
                //供应商是否存在
                SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
                searchBaseSupplier.setCodeQueryMark((byte)1);
                searchBaseSupplier.setSupplierCode(supplierCode);
                List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
                if (StringUtils.isEmpty(baseSuppliers)) {
                    failCount++;
                    failInfo.append("供应商不存在").append(",");
                    fail.add(i + 4);
                    continue;
                }

                //供应商的物料是否存在于免检清单
                SearchBaseInspectionExemptedList searchBaseInspectionExemptedList = new SearchBaseInspectionExemptedList();
                searchBaseInspectionExemptedList.setObjType((byte)1);
                searchBaseInspectionExemptedList.setMaterialId(qmsIncomingInspectionOrderImport.getMaterialId());
                searchBaseInspectionExemptedList.setSupplierId(baseSuppliers.get(0).getSupplierId());
                List<BaseInspectionExemptedList> inspectionExemptedLists = baseFeignApi.findList(searchBaseInspectionExemptedList).getData();
                if(StringUtils.isNotEmpty(inspectionExemptedLists)){
                    failCount++;
                    failInfo.append("该供应商该物料属于免检清单").append(",");
                    fail.add(i+4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setSupplierId(baseSuppliers.get(0).getSupplierId());
            }

            //仓库编码
            String warehouseCode = qmsIncomingInspectionOrderImport.getWarehouseCode();
            if(StringUtils.isNotEmpty(warehouseCode)){
                //仓库是否存在
                SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
                searchBaseWarehouse.setCodeQueryMark(1);
                searchBaseWarehouse.setWarehouseCode(supplierCode);
                List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
                if (StringUtils.isEmpty(baseWarehouses)) {
                    failCount++;
                    failInfo.append("仓库不存在").append(",");
                    fail.add(i + 4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            //检验方式
            String inspectionWayCode = qmsIncomingInspectionOrderImport.getInspectionWayCode();
            if(StringUtils.isNotEmpty(inspectionWayCode)){
                SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
                searchBaseInspectionWay.setInspectionWayCode(inspectionWayCode);
                searchBaseInspectionWay.setQueryMark(1);
                searchBaseInspectionWay.setInspectionType((byte)1);
                List<BaseInspectionWay> inspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
                if (StringUtils.isEmpty(inspectionWays)){
                    failCount++;
                    failInfo.append("检验方式不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setInspectionWayId(inspectionWays.get(0).getInspectionWayId());
            }

            //检验标准
            String inspectionStandardCode = qmsIncomingInspectionOrderImport.getInspectionStandardCode();
            if(StringUtils.isNotEmpty(inspectionStandardCode)){
                SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
                searchBaseInspectionStandard.setInspectionStandardCode(inspectionStandardCode);
                searchBaseInspectionStandard.setMaterialId(qmsIncomingInspectionOrderImport.getMaterialId());
                searchBaseInspectionStandard.setSupplierId(qmsIncomingInspectionOrderImport.getSupplierId());
                searchBaseInspectionStandard.setInspectionWayId(qmsIncomingInspectionOrderImport.getInspectionWayId());
                List<BaseInspectionStandard> inspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
                if (StringUtils.isEmpty(inspectionStandards)){
                    failCount++;
                    failInfo.append("检验标准不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setInspectionStandardId(inspectionStandards.get(0).getInspectionStandardId());
            }

            succeedCount++;
            succeedInfo.append(i+4+"").append(",");
            incomingInspectionOrderImports.add(qmsIncomingInspectionOrderImport);
        }

        SysImportAndExportLog sysImportAndExportLog = new SysImportAndExportLog();
        sysImportAndExportLog.setModuleNames("QMS");
        sysImportAndExportLog.setFileName("来料检验单导入信息表");
        sysImportAndExportLog.setType((byte)1);
        sysImportAndExportLog.setOperatorUserId(user.getUserId());
        sysImportAndExportLog.setResult((byte)1);
        sysImportAndExportLog.setTotalCount(qmsIncomingInspectionOrderImports.size());
        sysImportAndExportLog.setFailCount(failCount);
        sysImportAndExportLog.setSucceedCount(succeedCount);
        sysImportAndExportLog.setFailInfo(failInfo.toString());
        sysImportAndExportLog.setSucceedInfo(succeedInfo.toString());
        securityFeignApi.add(sysImportAndExportLog);

        if(StringUtils.isNotEmpty(incomingInspectionOrderImports)){
            //对合格数据进行分组
            HashMap<String, List<QmsIncomingInspectionOrderImport>> map = incomingInspectionOrderImports.stream().collect(Collectors.groupingBy(QmsIncomingInspectionOrderImport::getIncomingInspectionOrderCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<QmsIncomingInspectionOrderImport> qmsIncomingInspectionOrderImports1 = map.get(code);
                QmsIncomingInspectionOrder qmsIncomingInspectionOrder = new QmsIncomingInspectionOrder();
                //新增父级数据
                BeanUtils.copyProperties(qmsIncomingInspectionOrderImports1.get(0), qmsIncomingInspectionOrder);
                qmsIncomingInspectionOrder.setInspectionStatus((byte)1);
                qmsIncomingInspectionOrder.setCreateTime(new Date());
                qmsIncomingInspectionOrder.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrder.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrder.setModifiedTime(new Date());
                qmsIncomingInspectionOrder.setOrgId(user.getOrganizationId());
                qmsIncomingInspectionOrder.setStatus((byte)1);
                success += qmsIncomingInspectionOrderMapper.insertUseGeneratedKeys(qmsIncomingInspectionOrder);

                //履历
                QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
                BeanUtils.copyProperties(qmsIncomingInspectionOrder, qmsHtIncomingInspectionOrder);
                htList.add(qmsHtIncomingInspectionOrder);

                //新增明细数据
                LinkedList<QmsIncomingInspectionOrderDet> detList = new LinkedList<>();
                    for (QmsIncomingInspectionOrderImport qmsIncomingInspectionOrderImport : qmsIncomingInspectionOrderImports1) {
                        QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = new QmsIncomingInspectionOrderDet();
                        BeanUtils.copyProperties(qmsIncomingInspectionOrderImport, qmsIncomingInspectionOrderDet);
                        qmsIncomingInspectionOrderDet.setIncomingInspectionOrderId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                        qmsIncomingInspectionOrderDet.setStatus((byte) 1);
                        qmsIncomingInspectionOrderDet.setIfMustInspection(StringUtils.isEmpty(qmsIncomingInspectionOrderImport.getIfMustInspection()) ? null : qmsIncomingInspectionOrderImport.getIfMustInspection().byteValue());
                        qmsIncomingInspectionOrderDet.setInspectionTag(StringUtils.isEmpty(qmsIncomingInspectionOrderImport.getInspectionTag()) ? null : qmsIncomingInspectionOrderImport.getInspectionTag().byteValue());
                        detList.add(qmsIncomingInspectionOrderDet);
                }
                qmsIncomingInspectionOrderDetMapper.insertList(detList);
            }
            qmsHtIncomingInspectionOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateIfAllIssued(QmsIncomingInspectionOrder entity) {
        int num=0;
        num=qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(entity);
        return num;
    }
}

package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.QmsFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtSalesReturnOrderMapper;
import com.fantechs.provider.om.service.OmSalesReturnOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/06/21.
 */
@Service
public class OmSalesReturnOrderServiceImpl extends BaseService<OmSalesReturnOrder> implements OmSalesReturnOrderService {

    @Resource
    private OmSalesReturnOrderMapper omSalesReturnOrderMapper;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private OmHtSalesReturnOrderMapper omHtSalesReturnOrderMapper;
    @Resource
    private OmHtSalesReturnOrderDetMapper omHtSalesReturnOrderDetMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;

    @Override
    public List<OmSalesReturnOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omSalesReturnOrderMapper.findList(map);
    }

    @Override
    public List<OmHtSalesReturnOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtSalesReturnOrderMapper.findList(map);
    }

    /**
     * 数量累加更新状态
     * @param omSalesReturnOrder
     * @return
     */
    private int updateStatus(OmSalesReturnOrder omSalesReturnOrder){
        int num = 0;
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrder.getOmSalesReturnOrderDets()) {
            OmSalesReturnOrderDet omSalesReturnOrderDet1 = omSalesReturnOrderDetMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderDetId());
            if(StringUtils.isEmpty(omSalesReturnOrderDet1.getIssueQty()) || StringUtils.isEmpty(omSalesReturnOrderDet.getIssueQty())){
                omSalesReturnOrderDet1.setIssueQty(BigDecimal.ZERO);
                omSalesReturnOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            omSalesReturnOrderDet.setIssueQty(omSalesReturnOrderDet.getQty().add(omSalesReturnOrderDet1.getIssueQty()));
            num+=omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
        }
        //统计总订单数量
        Example example = new Example(OmSalesReturnOrderDet.class);
        example.createCriteria().andEqualTo("salesReturnOrderId",omSalesReturnOrder.getSalesReturnOrderId());
        List<OmSalesReturnOrderDet> list = omSalesReturnOrderDetMapper.selectByExample(example);
        BigDecimal totalQty = list.stream()
                .map(OmSalesReturnOrderDet::getOrderQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalIssuseQty  = list.stream()
                .map(OmSalesReturnOrderDet::getIssueQty)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(totalQty.compareTo(totalIssuseQty)==0){
            omSalesReturnOrder.setOrderStatus((byte)3);
        }else{
            omSalesReturnOrder.setOrderStatus((byte)2);
        }
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmSalesReturnOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setSalesReturnOrderCode(CodeUtils.getId("XSTH-"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrderStatus((byte)1);
        record.setOrgId(sysUser.getOrganizationId());
        int num = omSalesReturnOrderMapper.insertUseGeneratedKeys(record);
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : record.getOmSalesReturnOrderDets()) {
            omSalesReturnOrderDet.setSalesReturnOrderId(record.getSalesReturnOrderId());
            omSalesReturnOrderDet.setCreateTime(new Date());
            omSalesReturnOrderDet.setCreateUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setModifiedTime(new Date());
            omSalesReturnOrderDet.setModifiedUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        if(StringUtils.isNotEmpty(record.getOmSalesReturnOrderDets()) && record.getOmSalesReturnOrderDets().size()>0){
            num+=omSalesReturnOrderDetMapper.insertList(record.getOmSalesReturnOrderDets());
        }
        num+=this.addHt(record, record.getOmSalesReturnOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmSalesReturnOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"单据已被操作，无法修改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        //删除原有明细
        Example example = new Example(OmSalesReturnOrderDet.class);
        example.createCriteria().andEqualTo("salesReturnOrderId",entity.getSalesReturnOrderId());
        omSalesReturnOrderDetMapper.deleteByExample(example);
        for (OmSalesReturnOrderDet omSalesReturnOrderDet : entity.getOmSalesReturnOrderDets()) {
            omSalesReturnOrderDet.setSalesReturnOrderId(entity.getSalesReturnOrderId());
            omSalesReturnOrderDet.setCreateTime(new Date());
            omSalesReturnOrderDet.setCreateUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setModifiedTime(new Date());
            omSalesReturnOrderDet.setModifiedUserId(sysUser.getUserId());
            omSalesReturnOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        int num = 0;
        if(!entity.getOmSalesReturnOrderDets().isEmpty() && entity.getOmSalesReturnOrderDets().size()>0){
            num+=omSalesReturnOrderDetMapper.insertList(entity.getOmSalesReturnOrderDets());
        }
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(entity);
        num+=this.addHt(entity, entity.getOmSalesReturnOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmSalesReturnOrder omSalesReturnOrder = omSalesReturnOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omSalesReturnOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            Example example = new Example(OmSalesReturnOrderDet.class);
            example.createCriteria().andEqualTo("salesReturnOrderId",omSalesReturnOrder.getSalesReturnOrderId());
            omSalesReturnOrderDetMapper.deleteByExample(example);
        }
        return omSalesReturnOrderMapper.deleteByIds(ids);
    }

    /**
     * 添加历史记录
     * @param omSalesReturnOrder
     * @param omSalesReturnOrderDets
     * @return
     */
    private int addHt(OmSalesReturnOrder omSalesReturnOrder, List<OmSalesReturnOrderDet> omSalesReturnOrderDets){
        int num = 0;
        if(StringUtils.isNotEmpty(omSalesReturnOrder)){
            OmHtSalesReturnOrder omHtSalesReturnOrder = new OmHtSalesReturnOrder();
            BeanUtil.copyProperties(omSalesReturnOrder,omHtSalesReturnOrder);
            num+=omHtSalesReturnOrderMapper.insertSelective(omHtSalesReturnOrder);
        }
        if(StringUtils.isNotEmpty(omSalesReturnOrderDets)){
            for (OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets) {
                OmHtSalesReturnOrderDet omHtSalesReturnOrderDet = new OmHtSalesReturnOrderDet();
                BeanUtil.copyProperties(omSalesReturnOrderDet,omHtSalesReturnOrderDet);
                num+=omHtSalesReturnOrderDetMapper.insertSelective(omHtSalesReturnOrderDet);
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(OmSalesReturnOrderDet omSalesReturnOrderDet){
        OmSalesReturnOrder omSalesReturnOrder = omSalesReturnOrderMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderId());
        if(StringUtils.isEmpty(omSalesReturnOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"查询订单失败");
        }
        OmSalesReturnOrderDet omSalesReturnOrderDet1 = omSalesReturnOrderDetMapper.selectByPrimaryKey(omSalesReturnOrderDet.getSalesReturnOrderDetId());
        if(StringUtils.isEmpty(omSalesReturnOrderDet1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到订单信息");
        }
        if(StringUtils.isEmpty(omSalesReturnOrderDet1.getReceivingQty())){
            omSalesReturnOrderDet1.setReceivingQty(BigDecimal.ZERO);
        }
        omSalesReturnOrderDet.setReceivingQty(omSalesReturnOrderDet1.getReceivingQty().add(omSalesReturnOrderDet.getReceivingQty()));
        if(omSalesReturnOrderDet.getReceivingQty().compareTo(omSalesReturnOrderDet1.getIssueQty())==0){
            omSalesReturnOrder.setOrderStatus((byte)4);
        }
        int num = omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
        num+=omSalesReturnOrderMapper.updateByPrimaryKeySelective(omSalesReturnOrder);
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<OmSalesReturnOrderDet> omSalesReturnOrderDets) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String coreSourceSysOrderTypeCode = null;
        int i = 0;
        List<OmSalesReturnOrderDet> list = new ArrayList<>();
        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setBusinessType((byte)1);
        searchBaseOrderFlow.setOrderNode((byte)5);
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到当前单据配置的下游单据");
        }

        HashSet<Long> set = new HashSet();
        for(OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets){
            set.add(omSalesReturnOrderDet.getWarehouseId());
            if(omSalesReturnOrderDet.getOrderQty().compareTo(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty())) == -1 )
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于包装数量");

        }

        if("IN-SPO".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成收货计划
            List<WmsInPlanReceivingOrderDet> detList = new LinkedList<>();

            for(OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("salesReturnOrderId",omSalesReturnOrderDet.getSalesReturnOrderId());
                List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-SRO";

                WmsInPlanReceivingOrderDet wmsInPlanReceivingOrderDet = new WmsInPlanReceivingOrderDet();
                wmsInPlanReceivingOrderDet.setCoreSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInPlanReceivingOrderDet.setSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInPlanReceivingOrderDet.setLineNumber(lineNumber+"");
                wmsInPlanReceivingOrderDet.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                wmsInPlanReceivingOrderDet.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                wmsInPlanReceivingOrderDet.setPlanQty(omSalesReturnOrderDet.getOrderQty());
                wmsInPlanReceivingOrderDet.setLineStatus((byte)1);
                wmsInPlanReceivingOrderDet.setActualQty(omSalesReturnOrderDet.getReceivingQty());
                wmsInPlanReceivingOrderDet.setOperatorUserId(user.getUserId());
                detList.add(wmsInPlanReceivingOrderDet);
                omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                if(omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty())== 0) {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                }else {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                }
                list.add(omSalesReturnOrderDet);
                set.add(omSalesReturnOrderDet.getWarehouseId());
            }

            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInPlanReceivingOrder wmsInPlanReceivingOrder = new WmsInPlanReceivingOrder();
            wmsInPlanReceivingOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInPlanReceivingOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInPlanReceivingOrder.setOrderStatus((byte)1);
            wmsInPlanReceivingOrder.setCreateUserId(user.getUserId());
            wmsInPlanReceivingOrder.setCreateTime(new Date());
            wmsInPlanReceivingOrder.setModifiedUserId(user.getUserId());
            wmsInPlanReceivingOrder.setModifiedTime(new Date());
            wmsInPlanReceivingOrder.setStatus((byte)1);
            wmsInPlanReceivingOrder.setOrgId(user.getOrganizationId());
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInPlanReceivingOrder.setWarehouseId(iterator.next());
            }
            wmsInPlanReceivingOrder.setInPlanReceivingOrderDets(detList);

            ResponseEntity responseEntity = inFeignApi.add(wmsInPlanReceivingOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成收货计划单失败");
            }else {
                i++;
            }
        }else if ("IN-SWK".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成收货作业

            List<WmsInReceivingOrderDet> detList = new LinkedList<>();

            for(OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets){
                int lineNumber = 1;
                Map map = new HashMap();
                map.put("salesReturnOrderId",omSalesReturnOrderDet.getSalesReturnOrderId());
                List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-SRO";

                WmsInReceivingOrderDet wmsInReceivingOrderDet = new WmsInReceivingOrderDet();
                wmsInReceivingOrderDet.setCoreSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInReceivingOrderDet.setSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInReceivingOrderDet.setLineNumber(lineNumber+"");
                wmsInReceivingOrderDet.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                wmsInReceivingOrderDet.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                wmsInReceivingOrderDet.setPlanQty(omSalesReturnOrderDet.getOrderQty());
                wmsInReceivingOrderDet.setLineStatus((byte)1);
                wmsInReceivingOrderDet.setActualQty(omSalesReturnOrderDet.getReceivingQty());
                wmsInReceivingOrderDet.setOperatorUserId(user.getUserId());
                detList.add(wmsInReceivingOrderDet);
                omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                if(omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty())== 0) {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                }else {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                }
                list.add(omSalesReturnOrderDet);
                set.add(omSalesReturnOrderDet.getWarehouseId());
            }
            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInReceivingOrder wmsInReceivingOrder = new WmsInReceivingOrder();
            wmsInReceivingOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInReceivingOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInReceivingOrder.setOrderStatus((byte)1);
            wmsInReceivingOrder.setCreateUserId(user.getUserId());
            wmsInReceivingOrder.setCreateTime(new Date());
            wmsInReceivingOrder.setModifiedUserId(user.getUserId());
            wmsInReceivingOrder.setModifiedTime(new Date());
            wmsInReceivingOrder.setStatus((byte)1);
            wmsInReceivingOrder.setOrgId(user.getOrganizationId());
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInReceivingOrder.setWarehouseId(iterator.next());
            }
            wmsInReceivingOrder.setWmsInReceivingOrderDets(detList);

            ResponseEntity responseEntity = inFeignApi.add(wmsInReceivingOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成收货作业单失败");
            }else {
                i++;
            }
        }else if ("QMS-MIIO".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成来料检验单

            List<QmsIncomingInspectionOrderDto> detList = new LinkedList<>();
            for(OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("salesReturnOrderId",omSalesReturnOrderDet.getSalesReturnOrderId());
                List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-SRO";

                QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                qmsIncomingInspectionOrderDto.setSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                qmsIncomingInspectionOrderDto.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                qmsIncomingInspectionOrderDto.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                qmsIncomingInspectionOrderDto.setWarehouseId(omSalesReturnOrderDet.getWarehouseId());
                qmsIncomingInspectionOrderDto.setOrderQty(omSalesReturnOrderDet.getOrderQty());
                qmsIncomingInspectionOrderDto.setInspectionStatus((byte)1);
                qmsIncomingInspectionOrderDto.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                qmsIncomingInspectionOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
                qmsIncomingInspectionOrderDto.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDto.setCreateTime(new Date());
                qmsIncomingInspectionOrderDto.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDto.setModifiedTime(new Date());
                qmsIncomingInspectionOrderDto.setStatus((byte)1);
                qmsIncomingInspectionOrderDto.setOrgId(user.getOrganizationId());
                detList.add(qmsIncomingInspectionOrderDto);
                omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                if(omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty())== 0) {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                }else {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                }
                list.add(omSalesReturnOrderDet);
            }
            ResponseEntity responseEntity = qmsFeignApi.batchAdd(detList);

            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成来料检验单失败");
            }else {
                i++;
            }

        }else if("IN-IPO".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成入库计划单
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("InPlanOrderIsWork");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isEmpty(specItems))
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"需先配置作业循序先后");
            if("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"先作业后单据无法进行下推操作");

            List<WmsInInPlanOrderDetDto> detList = new LinkedList<>();

            for(OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("salesReturnOrderId",omSalesReturnOrderDet.getSalesReturnOrderId());
                List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-SRO";

                WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                wmsInInPlanOrderDet.setCoreSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInInPlanOrderDet.setSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInInPlanOrderDet.setLineNumber(lineNumber+"");
                wmsInInPlanOrderDet.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                wmsInInPlanOrderDet.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                wmsInInPlanOrderDet.setPlanQty(omSalesReturnOrderDet.getOrderQty());
                wmsInInPlanOrderDet.setLineStatus((byte)1);
                detList.add(wmsInInPlanOrderDet);
                omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                if(omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty())== 0) {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                }else {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                }
                list.add(omSalesReturnOrderDet);
                set.add(omSalesReturnOrderDet.getWarehouseId());
            }
            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
            wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
            wmsInInPlanOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInInPlanOrder.setOrderStatus((byte)1);
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInInPlanOrder.setWarehouseId(iterator.next());
            }
            wmsInInPlanOrder.setCreateUserId(user.getUserId());
            wmsInInPlanOrder.setCreateTime(new Date());
            wmsInInPlanOrder.setModifiedUserId(user.getUserId());
            wmsInInPlanOrder.setModifiedTime(new Date());
            wmsInInPlanOrder.setStatus((byte)1);
            wmsInInPlanOrder.setOrgId(user.getOrganizationId());
            wmsInInPlanOrder.setWmsInInPlanOrderDetDtos(detList);

            ResponseEntity responseEntity = inFeignApi.add(wmsInInPlanOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成入库计划单失败");
            }else {

                i++;
            }
        }else if("IN-IWK".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成上架作业单

            List<WmsInnerJobOrderDet> detList = new LinkedList<>();
            for(OmSalesReturnOrderDet omSalesReturnOrderDet : omSalesReturnOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("salesReturnOrderId",omSalesReturnOrderDet.getSalesReturnOrderId());
                List<OmSalesReturnOrderDto> omSalesReturnOrderDto = omSalesReturnOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = "IN-SRO";

                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setCoreSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInnerJobOrderDet.setSourceOrderCode(omSalesReturnOrderDto.get(0).getSalesReturnOrderCode());
                wmsInnerJobOrderDet.setSourceId(omSalesReturnOrderDet.getSalesReturnOrderDetId());
                wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                wmsInnerJobOrderDet.setMaterialId(omSalesReturnOrderDet.getMaterialId());
                wmsInnerJobOrderDet.setPlanQty(omSalesReturnOrderDet.getQty());
                wmsInnerJobOrderDet.setLineStatus((byte)1);
                detList.add(wmsInnerJobOrderDet);
                omSalesReturnOrderDet.setTotalIssueQty(omSalesReturnOrderDet.getTotalIssueQty().add(omSalesReturnOrderDet.getQty()));
                if(omSalesReturnOrderDet.getTotalIssueQty().compareTo(omSalesReturnOrderDet.getOrderQty())== 0) {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 1);
                }else {
                    omSalesReturnOrderDet.setIfAllIssued((byte) 0);
                }
                list.add(omSalesReturnOrderDet);
                set.add(omSalesReturnOrderDet.getWarehouseId());
            }
            if (set.size()>1)
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");

            WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
            wmsInnerJobOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInnerJobOrder.setJobOrderType((byte)1);
            Iterator<Long> iterator=set.iterator();
            while(iterator.hasNext()){
                wmsInnerJobOrder.setWarehouseId(iterator.next());
            }
            wmsInnerJobOrder.setOrderStatus((byte)1);
            wmsInnerJobOrder.setCreateUserId(user.getUserId());
            wmsInnerJobOrder.setCreateTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(user.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setStatus((byte)1);
            wmsInnerJobOrder.setOrgId(user.getOrganizationId());
            wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

            ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成上架作业单失败");
            }else {
                i++;
            }
        }else {
            throw new BizErrorException("单据流配置错误");
        }

        //返写下推数据
        if(StringUtils.isNotEmpty(list)) {
            for (OmSalesReturnOrderDet omSalesReturnOrderDet : list) {
                omSalesReturnOrderDetMapper.updateByPrimaryKeySelective(omSalesReturnOrderDet);
            }
        }

        return i;
    }

}

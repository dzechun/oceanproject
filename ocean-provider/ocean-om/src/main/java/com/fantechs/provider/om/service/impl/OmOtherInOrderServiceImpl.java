package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrderFlow;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrderDet;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
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
import com.fantechs.provider.api.srm.SrmFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.om.mapper.OmOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.OmOtherInOrderMapper;
import com.fantechs.provider.om.mapper.OmSalesReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtOtherInOrderMapper;
import com.fantechs.provider.om.service.OmOtherInOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by leifengzhi on 2021/06/21.
 */
@Service
public class OmOtherInOrderServiceImpl extends BaseService<OmOtherInOrder> implements OmOtherInOrderService {

    @Resource
    private OmOtherInOrderMapper omOtherInOrderMapper;
    @Resource
    private OmOtherInOrderDetMapper omOtherInOrderDetMapper;
    @Resource
    private OmHtOtherInOrderMapper omHtOtherInOrderMapper;
    @Resource
    private OmHtOtherInOrderDetMapper omHtOtherInOrderDetMapper;
    @Resource
    private SrmFeignApi srmFeignApi;
    @Resource
    private OmTransferOrderMapper omTransferOrderMapper;
    @Resource
    private OmSalesReturnOrderDetMapper omSalesReturnOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;



    @Override
    public List<OmOtherInOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return omOtherInOrderMapper.findList(map);
    }

    @Override
    public List<OmOtherInOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return omHtOtherInOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int packageAutoOutOrder(OmOtherInOrder omOtherInOrder) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (omOtherInOrder.getOrderStatus() >= 3) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "订单已下发完成");
        }
        if (omOtherInOrder.getOmOtherInOrderDets().size() < 1) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请输入下发数量");
        }
        int num = 0;
        List<SrmInAsnOrderDetDto> srmInAsnOrderDetDtos = new ArrayList<>();
        int i = 0;

        for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrder.getOmOtherInOrderDets()) {
            i++;
            if (StringUtils.isEmpty(omOtherInOrderDet.getIssueQty())) {
                omOtherInOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            BigDecimal total = omOtherInOrderDet.getIssueQty().add(omOtherInOrderDet.getQty());
            if (total.compareTo(omOtherInOrderDet.getOrderQty()) == 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "下发数量不能大于工单数量");
            }

            //获取物料单位名称
            String unitName = omSalesReturnOrderDetMapper.findUnitName(omOtherInOrderDet.getMaterialId());

            //获取发货库位
            Map<String, Object> map = new HashMap<>();
            map.put("orgId", sysUser.getOrganizationId());
            map.put("warehouseId", omOtherInOrderDet.getWarehouseId());
            map.put("storageType", 2);
            Long storageId = omTransferOrderMapper.findStorage(map);
            if (StringUtils.isEmpty(storageId)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未获取到该仓库的发货库位");
            }

            SrmInAsnOrderDetDto srmInAsnOrderDetDto = new SrmInAsnOrderDetDto();
            srmInAsnOrderDetDto.setSourceOrderId(omOtherInOrderDet.getOtherInOrderId());
            srmInAsnOrderDetDto.setOrderDetId(omOtherInOrderDet.getOtherInOrderDetId());
            srmInAsnOrderDetDto.setWarehouseId(omOtherInOrderDet.getWarehouseId());
            srmInAsnOrderDetDto.setStorageId(storageId);
            srmInAsnOrderDetDto.setMaterialId(omOtherInOrderDet.getMaterialId());
            srmInAsnOrderDetDto.setBatchCode(omOtherInOrderDet.getBatchCode());
            srmInAsnOrderDetDto.setLineNumber(String.valueOf(i));
            srmInAsnOrderDetDtos.add(srmInAsnOrderDetDto);

        }

        SrmInAsnOrderDto srmInAsnOrderDto = new SrmInAsnOrderDto();
        srmInAsnOrderDto.setSourceOrderId(omOtherInOrder.getOtherInOrderId());
        srmInAsnOrderDto.setOrderTypeId((long) 6);
        srmInAsnOrderDto.setSrmInAsnOrderDetDtos(srmInAsnOrderDetDtos);
        ResponseEntity responseEntity = srmFeignApi.add(srmInAsnOrderDto);

        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(), responseEntity.getMessage());
        }
        num += this.updateStatus(omOtherInOrder);
        this.addHt(omOtherInOrder, omOtherInOrder.getOmOtherInOrderDets());
        //更新订单状态
        return num;
    }

    private int updateStatus(OmOtherInOrder omOtherInOrder) {
        int num = 0;
        for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrder.getOmOtherInOrderDets()) {
            OmOtherInOrderDet omOtherInOrderDet1 = omOtherInOrderDetMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderDetId());
            if (StringUtils.isEmpty(omOtherInOrderDet1.getIssueQty())) {
                omOtherInOrderDet1.setIssueQty(BigDecimal.ZERO);
                omOtherInOrderDet.setIssueQty(BigDecimal.ZERO);
            }
            omOtherInOrderDet.setIssueQty(omOtherInOrderDet.getQty().add(omOtherInOrderDet1.getIssueQty()));
            num += omOtherInOrderDetMapper.updateByPrimaryKeySelective(omOtherInOrderDet);
        }
        BigDecimal total = omOtherInOrder.getOmOtherInOrderDets().stream()
                .map(OmOtherInOrderDet::getIssueQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(omOtherInOrder.getTotalQty()) == 0) {
            omOtherInOrder.setOrderStatus((byte) 3);
        } else {
            omOtherInOrder.setOrderStatus((byte) 2);
        }
        num += omOtherInOrderMapper.updateByPrimaryKeySelective(omOtherInOrder);
        return num;
    }

    /**
     * 收货数量反写
     *
     * @param omOtherInOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int writeQty(OmOtherInOrderDet omOtherInOrderDet) {
        OmOtherInOrder omOtherInOrder = omOtherInOrderMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderId());
        if (StringUtils.isEmpty(omOtherInOrder)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "查询订单失败");
        }
        OmOtherInOrderDet omOtherInOrderDet1 = omOtherInOrderDetMapper.selectByPrimaryKey(omOtherInOrderDet.getOtherInOrderDetId());
        if (StringUtils.isEmpty(omOtherInOrderDet1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未获取到订单信息");
        }
        if (StringUtils.isEmpty(omOtherInOrderDet1.getReceivingQty())) {
            omOtherInOrderDet1.setReceivingQty(BigDecimal.ZERO);
        }
        omOtherInOrderDet.setReceivingQty(omOtherInOrderDet1.getReceivingQty().add(omOtherInOrderDet.getReceivingQty()));
        if (omOtherInOrderDet.getReceivingQty().compareTo(omOtherInOrderDet1.getIssueQty()) == 0) {
            omOtherInOrder.setOrderStatus((byte) 4);
        }
        int num = omOtherInOrderDetMapper.updateByPrimaryKeySelective(omOtherInOrderDet);
        num += omOtherInOrderMapper.updateByPrimaryKeySelective(omOtherInOrder);
        return num;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmOtherInOrder record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setOtherInOrderCode(CodeUtils.getId("IN-OIO"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte) 1);
        int num = omOtherInOrderMapper.insertUseGeneratedKeys(record);
        for (OmOtherInOrderDet omOtherInOrderDet : record.getOmOtherInOrderDets()) {
            omOtherInOrderDet.setOtherInOrderId(record.getOtherInOrderId());
            omOtherInOrderDet.setCreateTime(new Date());
            omOtherInOrderDet.setCreateUserId(sysUser.getUserId());
            omOtherInOrderDet.setModifiedTime(new Date());
            omOtherInOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherInOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        if (record.getOmOtherInOrderDets().size() > 0) {
            num += omOtherInOrderDetMapper.insertList(record.getOmOtherInOrderDets());
        }
        num += this.addHt(record, record.getOmOtherInOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmOtherInOrder entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (entity.getOrderStatus() > 1) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "单据已被操作，无法修改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setOrgId(sysUser.getOrganizationId());
        //删除原有明细
        Example example = new Example(OmOtherInOrderDet.class);
        example.createCriteria().andEqualTo("otherInOrderId", entity.getOtherInOrderId());
        omOtherInOrderDetMapper.deleteByExample(example);
        for (OmOtherInOrderDet omOtherInOrderDet : entity.getOmOtherInOrderDets()) {
            omOtherInOrderDet.setOtherInOrderId(entity.getOtherInOrderId());
            omOtherInOrderDet.setCreateTime(new Date());
            omOtherInOrderDet.setCreateUserId(sysUser.getUserId());
            omOtherInOrderDet.setModifiedTime(new Date());
            omOtherInOrderDet.setModifiedUserId(sysUser.getUserId());
            omOtherInOrderDet.setOrgId(sysUser.getOrganizationId());
        }
        int num = 0;
        if (StringUtils.isNotEmpty(entity.getOmOtherInOrderDets())) {
            num = omOtherInOrderDetMapper.insertList(entity.getOmOtherInOrderDets());
        }
        num += omOtherInOrderMapper.updateByPrimaryKeySelective(entity);
        num += this.addHt(entity, entity.getOmOtherInOrderDets());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrIds = ids.split(",");

        for (String id : arrIds) {
            OmOtherInOrder omOtherInOrder = omOtherInOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(omOtherInOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012000, id);
            }
            //删除明细
            Example example = new Example(OmOtherInOrderDet.class);
            example.createCriteria().andEqualTo("otherInOrderId", omOtherInOrder.getOtherInOrderId());
            omOtherInOrderDetMapper.deleteByExample(example);
        }
        return omOtherInOrderMapper.deleteByIds(ids);
    }

    /**
     * 添加历史记录
     *
     * @return
     */
    private int addHt(OmOtherInOrder omOtherInOrder, List<OmOtherInOrderDet> omOtherInOrderDets) {
        int num = 0;
        if (StringUtils.isNotEmpty(omOtherInOrder)) {
            OmHtOtherInOrder omHtOtherInOrder = new OmHtOtherInOrder();
            BeanUtil.copyProperties(omOtherInOrder, omHtOtherInOrder);
            num += omHtOtherInOrderMapper.insertSelective(omHtOtherInOrder);
        }
        if (StringUtils.isNotEmpty(omOtherInOrderDets)) {
            for (OmOtherInOrderDet omOtherInOrderDet : omOtherInOrderDets) {
                OmHtOtherInOrderDet omHtOtherInOrderDet = new OmHtOtherInOrderDet();
                BeanUtil.copyProperties(omOtherInOrderDet, omHtOtherInOrderDet);
                num += omHtOtherInOrderDetMapper.insertSelective(omHtOtherInOrderDet);
            }
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String coreSourceSysOrderTypeCode = null;
        int i = 0;
        List<OmOtherInOrderDet> list = new ArrayList<>();
        List<OmOtherInOrderDet> omOtherInOrderDets = omOtherInOrderDetMapper.selectByIds(ids);
        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setBusinessType((byte)1);
        searchBaseOrderFlow.setOrderNode((byte)5);
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "未找到当前单据配置的下游单据");
        }

        HashSet set = new HashSet();
        for(OmOtherInOrderDet omOtherInOrderDet : omOtherInOrderDets){
            set.add(omOtherInOrderDet.getWarehouseId());
            if(omOtherInOrderDet.getOrderQty().compareTo(omOtherInOrderDet.getTotalIssueQty().add(omOtherInOrderDet.getQty())) == -1 )
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于包装总数");

        }
        if (set.size()>1)
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "请选择相同仓库的进行下发操作");


        if("IN-SPO".equals(baseOrderFlow.getNextOrderTypeCode())){
        //生成收货计划

        }else if ("IN-SWK".equals(baseOrderFlow.getNextOrderTypeCode())){
        //生成收货作业

        }else if ("QMS-MIIO".equals(baseOrderFlow.getNextOrderTypeCode())){
        //生成来料检验单
            /*SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isEmpty(specItems))
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"需先配置作业循序先后");
            if("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"先作业后单据无法进行下推操作");*/

            List<QmsIncomingInspectionOrderDto> detList = new LinkedList<>();
            for(OmOtherInOrderDet omOtherInOrderDet : omOtherInOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("otherInOrderId",omOtherInOrderDet.getOtherInOrderId());
                List<OmOtherInOrderDto> omOtherInOrderDto = omOtherInOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = omOtherInOrderDto.get(0).getSysOrderTypeCode();

                QmsIncomingInspectionOrderDto qmsIncomingInspectionOrderDto = new QmsIncomingInspectionOrderDto();
                qmsIncomingInspectionOrderDto.setCoreSourceOrderCode(omOtherInOrderDto.get(0).getOtherInOrderCode());
                qmsIncomingInspectionOrderDto.setSourceOrderCode(omOtherInOrderDto.get(0).getOtherInOrderCode());
                qmsIncomingInspectionOrderDto.setSourceId(omOtherInOrderDet.getOtherInOrderDetId());
                qmsIncomingInspectionOrderDto.setMaterialId(omOtherInOrderDet.getMaterialId());
                qmsIncomingInspectionOrderDto.setWarehouseId(omOtherInOrderDet.getWarehouseId());
                qmsIncomingInspectionOrderDto.setOrderQty(omOtherInOrderDet.getOrderQty());
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
                omOtherInOrderDet.setTotalIssueQty(omOtherInOrderDet.getTotalIssueQty().add(omOtherInOrderDet.getQty()));
                if(omOtherInOrderDet.getTotalIssueQty().compareTo(omOtherInOrderDet.getOrderQty())== 0)
                    omOtherInOrderDet.setIfAllIssued((byte)1);
                else
                    omOtherInOrderDet.setIfAllIssued((byte)0);
                list.add(omOtherInOrderDet);
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

            for(OmOtherInOrderDet omOtherInOrderDet : omOtherInOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("otherInOrderId",omOtherInOrderDet.getOtherInOrderId());
                List<OmOtherInOrderDto> omOtherInOrderDto = omOtherInOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = omOtherInOrderDto.get(0).getSysOrderTypeCode();

                WmsInInPlanOrderDetDto wmsInInPlanOrderDet = new WmsInInPlanOrderDetDto();
                wmsInInPlanOrderDet.setCoreSourceOrderCode(omOtherInOrderDto.get(0).getOtherInOrderCode());
                wmsInInPlanOrderDet.setSourceOrderCode(omOtherInOrderDto.get(0).getOtherInOrderCode());
                wmsInInPlanOrderDet.setLineNumber(lineNumber+"");
                wmsInInPlanOrderDet.setSourceId(omOtherInOrderDet.getOtherInOrderDetId());
                wmsInInPlanOrderDet.setMaterialId(omOtherInOrderDet.getMaterialId());
                wmsInInPlanOrderDet.setPlanQty(omOtherInOrderDet.getOrderQty());
                wmsInInPlanOrderDet.setLineStatus((byte)1);
                detList.add(wmsInInPlanOrderDet);
                omOtherInOrderDet.setTotalIssueQty(omOtherInOrderDet.getTotalIssueQty().add(omOtherInOrderDet.getQty()));
                if(omOtherInOrderDet.getTotalIssueQty().compareTo(omOtherInOrderDet.getOrderQty())== 0)
                    omOtherInOrderDet.setIfAllIssued((byte)1);
                else
                    omOtherInOrderDet.setIfAllIssued((byte)0);
                list.add(omOtherInOrderDet);
            }

            WmsInInPlanOrderDto wmsInInPlanOrder = new WmsInInPlanOrderDto();
            wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
            wmsInInPlanOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInInPlanOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInInPlanOrder.setOrderStatus((byte)1);
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

            /*
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(StringUtils.isEmpty(specItems))
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"需先配置作业循序先后");
            if("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"先作业后单据无法进行下推操作");*/

            List<WmsInnerJobOrderDet> detList = new LinkedList<>();
            for(OmOtherInOrderDet omOtherInOrderDet : omOtherInOrderDets){
                int lineNumber = 1;

                Map map = new HashMap();
                map.put("otherInOrderId",omOtherInOrderDet.getOtherInOrderId());
                List<OmOtherInOrderDto> omOtherInOrderDto = omOtherInOrderMapper.findList(map);
                coreSourceSysOrderTypeCode = omOtherInOrderDto.get(0).getSysOrderTypeCode();

                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setCoreSourceOrderCode(omOtherInOrderDto.get(0).getOtherInOrderCode());
                wmsInnerJobOrderDet.setSourceOrderCode(omOtherInOrderDto.get(0).getOtherInOrderCode());
                wmsInnerJobOrderDet.setSourceId(omOtherInOrderDet.getOtherInOrderDetId());
                wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                wmsInnerJobOrderDet.setMaterialId(omOtherInOrderDet.getMaterialId());
                wmsInnerJobOrderDet.setPlanQty(omOtherInOrderDet.getQty());
                wmsInnerJobOrderDet.setLineStatus((byte)1);
                detList.add(wmsInnerJobOrderDet);
                omOtherInOrderDet.setTotalIssueQty(omOtherInOrderDet.getTotalIssueQty().add(omOtherInOrderDet.getQty()));
                if(omOtherInOrderDet.getTotalIssueQty().compareTo(omOtherInOrderDet.getOrderQty())== 0)
                    omOtherInOrderDet.setIfAllIssued((byte)1);
                else
                    omOtherInOrderDet.setIfAllIssued((byte)0);
                list.add(omOtherInOrderDet);
            }

            WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
            wmsInnerJobOrder.setSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);
            wmsInnerJobOrder.setJobOrderType((byte)1);
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
            for (OmOtherInOrderDet omOtherInOrderDet : list) {
                omOtherInOrderDetMapper.updateByPrimaryKeySelective(omOtherInOrderDet);
            }
        }

        return i;
    }


}

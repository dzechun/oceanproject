package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtTransferOrderDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtTransferOrder;
import com.fantechs.common.base.general.entity.om.OmHtTransferOrderDet;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.om.mapper.OmTransferOrderDetMapper;
import com.fantechs.provider.om.mapper.OmTransferOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtTransferOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtTransferOrderMapper;
import com.fantechs.provider.om.service.OmTransferOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */
@Service
public class OmTransferOrderServiceImpl extends BaseService<OmTransferOrder> implements OmTransferOrderService {

    @Resource
    private OmTransferOrderMapper omTransferOrderMapper;
    @Resource
    private OmTransferOrderDetMapper omTransferOrderDetMapper;
    @Resource
    private InnerFeignApi innerFeignApi;
    @Resource
    private OmHtTransferOrderMapper omHtTransferOrderMapper;
    @Resource
    private OmHtTransferOrderDetMapper omHtTransferOrderDetMapper;

    @Override
    public List<OmTransferOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        List<OmTransferOrderDto> list = omTransferOrderMapper.findList(map);
        for (OmTransferOrderDto omTransferOrderDto : list) {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("transferOrderId",omTransferOrderDto.getTransferOrderId());
            List<OmTransferOrderDetDto> omTransferOrderDetDtos = omTransferOrderDetMapper.findList(map1);
            omTransferOrderDto.setOmTransferOrderDetDtos(omTransferOrderDetDtos);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pushDown(List<OmTransferOrderDetDto> omTransferOrderDetDtos) {
        int i;
        Long outWarehouseId = omTransferOrderDetDtos.get(0).getOutWarehouseId();
        Long inWarehouseId = omTransferOrderDetDtos.get(0).getInWarehouseId();
        for (OmTransferOrderDetDto omTransferOrderDetDto : omTransferOrderDetDtos){
            if(!outWarehouseId.equals(omTransferOrderDetDto.getOutWarehouseId())||!inWarehouseId.equals(omTransferOrderDetDto.getInWarehouseId())){
                throw new BizErrorException("调出(调入)仓库需一致");
            }
            BigDecimal totalIssueQty = omTransferOrderDetDto.getTotalIssueQty() == null ? BigDecimal.ZERO : omTransferOrderDetDto.getTotalIssueQty();
            BigDecimal add = totalIssueQty.add(omTransferOrderDetDto.getIssueQty());
            if(add.compareTo(omTransferOrderDetDto.getOrderQty()) == 1){
                throw new BizErrorException("下发数量不能大于订单数量");
            }else if(add.compareTo(omTransferOrderDetDto.getOrderQty()) == 0){
                omTransferOrderDetDto.setIfAllIssued((byte)1);
            }
            omTransferOrderDetDto.setTotalIssueQty(add);
        }
        i = omTransferOrderDetMapper.batchUpdate(omTransferOrderDetDtos);

        //拣货作业
        int lineNumber = 1;
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
        for (OmTransferOrderDetDto omTransferOrderDetDto : omTransferOrderDetDtos) {
            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setCoreSourceOrderCode(omTransferOrderDetDto.getCoreSourceOrderCode());
            wmsInnerJobOrderDet.setSourceOrderCode(omTransferOrderDetDto.getTransferOrderCode());
            wmsInnerJobOrderDet.setCoreSourceId(omTransferOrderDetDto.getCoreSourceId());
            wmsInnerJobOrderDet.setSourceId(omTransferOrderDetDto.getTransferOrderDetId());
            wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
            lineNumber++;
            wmsInnerJobOrderDet.setMaterialId(omTransferOrderDetDto.getMaterialId());
            wmsInnerJobOrderDet.setBatchCode(omTransferOrderDetDto.getBatchCode());
            wmsInnerJobOrderDet.setPlanQty(omTransferOrderDetDto.getIssueQty());
            wmsInnerJobOrderDet.setLineStatus((byte) 1);
            wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
        }
        WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
        wmsInnerJobOrder.setSourceBigType((byte)1);
        wmsInnerJobOrder.setCoreSourceSysOrderTypeCode("INNER-TO");
        wmsInnerJobOrder.setSourceSysOrderTypeCode("INNER-TO");
        wmsInnerJobOrder.setWarehouseId(outWarehouseId);
        wmsInnerJobOrder.setJobOrderType((byte) 2);
        wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        } else {
            i++;
        }

        //修改单据状态
        Byte orderStatus = (byte)2;
        OmTransferOrder omTransferOrder = omTransferOrderMapper.selectByPrimaryKey(omTransferOrderDetDtos.get(0).getTransferOrderId());
        Example example = new Example(OmTransferOrderDet.class);
        example.createCriteria().andEqualTo("transferOrderId",omTransferOrder.getTransferOrderId());
        List<OmTransferOrderDet> omTransferOrderDets = omTransferOrderDetMapper.selectByExample(example);
        for (OmTransferOrderDet omTransferOrderDet : omTransferOrderDets){
            if(omTransferOrderDet.getIfAllIssued()!=(byte)1){
                orderStatus = (byte)3;
                break;
            }
        }
        omTransferOrder.setOrderStatus(orderStatus);
        omTransferOrderMapper.updateByPrimaryKeySelective(omTransferOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OmTransferOrder record) {
        SysUser sysUser = currentUser();
        record.setTransferOrderCode(CodeUtils.getId("INNER-TO"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte)1);
        int num = omTransferOrderMapper.insertUseGeneratedKeys(record);
        for (OmTransferOrderDet omTransferOrderDet : record.getOmTransferOrderDetDtos()) {
            omTransferOrderDet.setTransferOrderId(record.getTransferOrderId());
            omTransferOrderDet.setCreateTime(new Date());
            omTransferOrderDet.setCreateUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            omTransferOrderDet.setModifiedTime(new Date());
            omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
            omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
            num+=omTransferOrderDetMapper.insertSelective(omTransferOrderDet);
        }
        this.addHt(record,record.getOmTransferOrderDetDtos());
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OmTransferOrder entity) {
        SysUser sysUser = currentUser();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),entity.getOrderStatus()==2?"订单已下发，无法修改":"订单已完成");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        int i = omTransferOrderMapper.updateByPrimaryKeySelective(entity);

        ArrayList<Long> idList = new ArrayList<>();
        List<OmTransferOrderDetDto> omTransferOrderDetDtos = entity.getOmTransferOrderDetDtos();
        if(StringUtils.isNotEmpty(omTransferOrderDetDtos)) {
            for (OmTransferOrderDetDto omTransferOrderDetDto : omTransferOrderDetDtos) {
                if (StringUtils.isNotEmpty(omTransferOrderDetDto.getTransferOrderDetId())) {
                    omTransferOrderDetMapper.updateByPrimaryKeySelective(omTransferOrderDetDto);
                    idList.add(omTransferOrderDetDto.getTransferOrderDetId());
                }
            }
        }

        //删除原有明细
        Example example = new Example(OmTransferOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("transferOrderId",entity.getTransferOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("transferOrderDetId", idList);
        }
        omTransferOrderDetMapper.deleteByExample(example);

        if(StringUtils.isNotEmpty(omTransferOrderDetDtos)) {
            List<OmTransferOrderDet> addDetList = new LinkedList<>();
            for (OmTransferOrderDet omTransferOrderDet : omTransferOrderDetDtos) {
                if (idList.contains(omTransferOrderDet.getTransferOrderDetId())) {
                    continue;
                }
                omTransferOrderDet.setTransferOrderId(entity.getTransferOrderId());
                omTransferOrderDet.setCreateTime(new Date());
                omTransferOrderDet.setCreateUserId(sysUser.getUserId());
                omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
                omTransferOrderDet.setModifiedTime(new Date());
                omTransferOrderDet.setModifiedUserId(sysUser.getUserId());
                omTransferOrderDet.setOrgId(sysUser.getOrganizationId());
                addDetList.add(omTransferOrderDet);
            }
            if(StringUtils.isNotEmpty(addDetList)) {
                i += omTransferOrderDetMapper.insertList(addDetList);
            }
        }

        this.addHt(entity,entity.getOmTransferOrderDetDtos());
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            OmTransferOrder omTransferOrder = omTransferOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(omTransferOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //删除明细
            Example example = new Example(OmTransferOrderDet.class);
            example.createCriteria().andEqualTo("transferOrderId",omTransferOrder.getTransferOrderId());
            omTransferOrderDetMapper.deleteByExample(example);

            this.addHt(omTransferOrder,null);
        }
        return omTransferOrderMapper.deleteByIds(ids);
    }

    /**
     * 更改单据状态
     * @param omTransferOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateStatus(OmTransferOrder omTransferOrder) {
        SysUser sysUser = currentUser();
        omTransferOrder.setModifiedUserId(sysUser.getUserId());
        omTransferOrder.setModifiedTime(new Date());
        return omTransferOrderMapper.updateByPrimaryKeySelective(omTransferOrder);
    }

    @Override
    public List<OmHtTransferOrderDto> findHtList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return omHtTransferOrderMapper.findHtList(map);
    }

    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }


    /**
     * 添加历史记录
     * @return
     */
    private int addHt(OmTransferOrder omTransferOrder, List<OmTransferOrderDetDto> omTransferOrderDetDtos){
        int num = 0;
        if(StringUtils.isNotEmpty(omTransferOrder)){
            OmHtTransferOrder omHtTransferOrder = new OmHtTransferOrder();
            BeanUtil.copyProperties(omTransferOrder,omHtTransferOrder);
            num+=omHtTransferOrderMapper.insertSelective(omHtTransferOrder);
        }
        if(StringUtils.isNotEmpty(omTransferOrderDetDtos)){
            for (OmTransferOrderDet omTransferOrderDet : omTransferOrderDetDtos) {
                OmHtTransferOrderDet omHtTransferOrderDet = new OmHtTransferOrderDet();
                BeanUtil.copyProperties(omTransferOrderDet,omHtTransferOrderDet);
                num+=omHtTransferOrderDetMapper.insertSelective(omHtTransferOrderDet);
            }
        }
        return num;
    }
}

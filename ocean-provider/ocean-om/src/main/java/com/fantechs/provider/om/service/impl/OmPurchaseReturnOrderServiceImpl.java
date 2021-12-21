package com.fantechs.provider.om.service.impl;

import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrder;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.OmPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.OmPurchaseReturnOrderMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseReturnOrderDetMapper;
import com.fantechs.provider.om.mapper.ht.OmHtPurchaseReturnOrderMapper;
import com.fantechs.provider.om.service.OmPurchaseReturnOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */
@Service
public class OmPurchaseReturnOrderServiceImpl extends BaseService<OmPurchaseReturnOrder> implements OmPurchaseReturnOrderService {

    @Resource
    private OmPurchaseReturnOrderMapper omPurchaseReturnOrderMapper;
    @Resource
    private OmPurchaseReturnOrderDetMapper omPurchaseReturnOrderDetMapper;
    @Resource
    private OmHtPurchaseReturnOrderMapper omHtPurchaseReturnOrderMapper;
    @Resource
    private OmHtPurchaseReturnOrderDetMapper omHtPurchaseReturnOrderDetMapper;

    @Override
    public List<OmPurchaseReturnOrderDto> findList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omPurchaseReturnOrderMapper.findList(map);
    }

    @Override
    public List<OmHtPurchaseReturnOrder> findHtList(Map<String, Object> map) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",currentUserInfo.getOrganizationId());
        return omHtPurchaseReturnOrderMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(OmPurchaseReturnOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setPurchaseReturnOrderCode(CodeUtils.getId("OUT-PRO"));
        record.setOrgId(user.getOrganizationId());
        record.setCreateTime(new DateTime());
        record.setCreateUserId(user.getUserId());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new DateTime());
        int i = omPurchaseReturnOrderMapper.insertUseGeneratedKeys(record);

        //明细
        List<OmHtPurchaseReturnOrderDet> htList = new LinkedList<>();
        List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos = record.getOmPurchaseReturnOrderDetDtos();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDetDtos)){
            for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto:omPurchaseReturnOrderDetDtos){
                omPurchaseReturnOrderDetDto.setPurchaseReturnOrderId(record.getPurchaseReturnOrderId());
                omPurchaseReturnOrderDetDto.setCreateUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setCreateTime(new Date());
                omPurchaseReturnOrderDetDto.setModifiedUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setModifiedTime(new Date());
                omPurchaseReturnOrderDetDto.setOrgId(user.getOrganizationId());

                OmHtPurchaseReturnOrderDet omHtPurchaseReturnOrderDet = new OmHtPurchaseReturnOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrderDetDto, omHtPurchaseReturnOrderDet);
                htList.add(omHtPurchaseReturnOrderDet);
            }
            omPurchaseReturnOrderDetMapper.insertList(omPurchaseReturnOrderDetDtos);
            omHtPurchaseReturnOrderDetMapper.insertList(htList);
        }

        //履历
        OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
        org.springframework.beans.BeanUtils.copyProperties(record, omHtPurchaseReturnOrder);
        omHtPurchaseReturnOrderMapper.insertSelective(omHtPurchaseReturnOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OmPurchaseReturnOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = omPurchaseReturnOrderMapper.updateByPrimaryKeySelective(entity);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos = entity.getOmPurchaseReturnOrderDetDtos();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDetDtos)) {
            for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : omPurchaseReturnOrderDetDtos) {
                if (StringUtils.isNotEmpty(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId())) {
                    omPurchaseReturnOrderDetMapper.updateByPrimaryKeySelective(omPurchaseReturnOrderDetDto);
                    idList.add(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId());
                }
            }
        }

        //删除原明细
        Example example = new Example(OmPurchaseReturnOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("purchaseReturnOrderId",entity.getPurchaseReturnOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("purchaseReturnOrderDetId", idList);
        }
        omPurchaseReturnOrderDetMapper.deleteByExample(example);

        //明细
        List<OmHtPurchaseReturnOrderDet> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDetDtos)){
            List<OmPurchaseReturnOrderDetDto> addDetList = new LinkedList<>();
            for (OmPurchaseReturnOrderDetDto omPurchaseReturnOrderDetDto : omPurchaseReturnOrderDetDtos){
                OmHtPurchaseReturnOrderDet omHtPurchaseReturnOrderDet = new OmHtPurchaseReturnOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrderDetDto, omHtPurchaseReturnOrderDet);
                htList.add(omHtPurchaseReturnOrderDet);

                if (idList.contains(omPurchaseReturnOrderDetDto.getPurchaseReturnOrderDetId())) {
                    continue;
                }
                omPurchaseReturnOrderDetDto.setPurchaseReturnOrderId(entity.getPurchaseReturnOrderId());
                omPurchaseReturnOrderDetDto.setCreateUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setCreateTime(new Date());
                omPurchaseReturnOrderDetDto.setModifiedUserId(user.getUserId());
                omPurchaseReturnOrderDetDto.setModifiedTime(new Date());
                omPurchaseReturnOrderDetDto.setOrgId(user.getOrganizationId());
                addDetList.add(omPurchaseReturnOrderDetDto);
            }
            if(StringUtils.isNotEmpty(addDetList)) {
                omPurchaseReturnOrderDetMapper.insertList(addDetList);
            }
            if(StringUtils.isNotEmpty(htList)) {
                omHtPurchaseReturnOrderDetMapper.insertList(htList);
            }
        }

        //履历
        OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
        org.springframework.beans.BeanUtils.copyProperties(entity, omHtPurchaseReturnOrder);
        omHtPurchaseReturnOrderMapper.insertSelective(omHtPurchaseReturnOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        //表头履历
        List<OmPurchaseReturnOrder> omPurchaseReturnOrders = omPurchaseReturnOrderMapper.selectByIds(ids);
        List<OmHtPurchaseReturnOrder> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrders)) {
            for (OmPurchaseReturnOrder omPurchaseReturnOrder : omPurchaseReturnOrders) {
                OmHtPurchaseReturnOrder omHtPurchaseReturnOrder = new OmHtPurchaseReturnOrder();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrder, omHtPurchaseReturnOrder);
                htList.add(omHtPurchaseReturnOrder);
            }
            omHtPurchaseReturnOrderMapper.insertList(htList);
        }

        //表体履历
        List<String> idList = Arrays.asList(ids.split(","));
        Example example = new Example(OmPurchaseReturnOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("purchaseReturnOrderId",idList);
        List<OmPurchaseReturnOrderDet> omPurchaseReturnOrderDets = omPurchaseReturnOrderDetMapper.selectByExample(example);
        List<OmHtPurchaseReturnOrderDet> htDetList = new LinkedList<>();
        if(StringUtils.isNotEmpty(omPurchaseReturnOrderDets)) {
            for (OmPurchaseReturnOrderDet omPurchaseReturnOrderDet : omPurchaseReturnOrderDets) {
                OmHtPurchaseReturnOrderDet omHtPurchaseReturnOrderDet = new OmHtPurchaseReturnOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(omPurchaseReturnOrderDet, omHtPurchaseReturnOrderDet);
                htDetList.add(omHtPurchaseReturnOrderDet);
            }
            omHtPurchaseReturnOrderDetMapper.insertList(htDetList);
        }

        omPurchaseReturnOrderDetMapper.deleteByExample(example);

        return omPurchaseReturnOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<OmPurchaseReturnOrder> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

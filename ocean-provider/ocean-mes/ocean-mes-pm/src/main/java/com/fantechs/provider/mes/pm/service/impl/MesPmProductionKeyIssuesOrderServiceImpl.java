package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseProductionKeyIssues;
import com.fantechs.common.base.general.entity.basic.BaseProductionKeyIssuesDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductionKeyIssues;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrderDet;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesPmHtProductionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmProductionKeyIssuesOrderDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmProductionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmProductionKeyIssuesOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */
@Service
public class MesPmProductionKeyIssuesOrderServiceImpl extends BaseService<MesPmProductionKeyIssuesOrder> implements MesPmProductionKeyIssuesOrderService {

    @Resource
    private MesPmProductionKeyIssuesOrderMapper mesPmProductionKeyIssuesOrderMapper;
    @Resource
    private MesPmProductionKeyIssuesOrderDetMapper mesPmProductionKeyIssuesOrderDetMapper;
    @Resource
    private MesPmHtProductionKeyIssuesOrderMapper mesPmHtProductionKeyIssuesOrderMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public MesPmProductionKeyIssuesOrder PDAFindOne(String workOrderCode) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        MesPmProductionKeyIssuesOrder mesPmProductionKeyIssuesOrder = new MesPmProductionKeyIssuesOrder();

        SearchMesPmProductionKeyIssuesOrder searchMesPmProductionKeyIssuesOrder = new SearchMesPmProductionKeyIssuesOrder();
        searchMesPmProductionKeyIssuesOrder.setWorkOrderCode(workOrderCode);
        searchMesPmProductionKeyIssuesOrder.setCodeQueryMark(1);
        List<MesPmProductionKeyIssuesOrder> list = mesPmProductionKeyIssuesOrderMapper.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmProductionKeyIssuesOrder));
        //不存在数据则新增,存在则直接返回
        if(StringUtils.isEmpty(list)){
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
            searchMesPmWorkOrder.setOrgId(user.getOrganizationId());
            searchMesPmWorkOrder.setCodeQueryMark(1);
            List<MesPmWorkOrderDto> mesPmWorkOrderDtos = mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
            if(StringUtils.isEmpty(mesPmWorkOrderDtos)){
                throw new BizErrorException("不存在此工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtos.get(0);

            //产前关键事项设值
            mesPmProductionKeyIssuesOrder.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
            mesPmProductionKeyIssuesOrder.setMaterialId(mesPmWorkOrderDto.getMaterialId());

            //产前关键事项明细设值
            List<MesPmProductionKeyIssuesOrderDet> mesPmProductionKeyIssuesOrderDets = new ArrayList<>();

            SearchBaseProductionKeyIssues searchBaseProductionKeyIssues = new SearchBaseProductionKeyIssues();
            searchBaseProductionKeyIssues.setMaterialId(mesPmWorkOrderDto.getMaterialId());
            List<BaseProductionKeyIssues> baseProductionKeyIssuesList = baseFeignApi.findList(searchBaseProductionKeyIssues).getData();
            //产品关键事项是否维护有相应物料的数据
            if(StringUtils.isNotEmpty(baseProductionKeyIssuesList)){
                List<BaseProductionKeyIssuesDet> baseProductionKeyIssuesDetList = baseProductionKeyIssuesList.get(0).getBaseProductionKeyIssuesDetList();
                for (BaseProductionKeyIssuesDet baseProductionKeyIssuesDet:baseProductionKeyIssuesDetList){
                    MesPmProductionKeyIssuesOrderDet mesPmProductionKeyIssuesOrderDet = new MesPmProductionKeyIssuesOrderDet();
                    mesPmProductionKeyIssuesOrderDet.setProductionKeyIssuesDetId(baseProductionKeyIssuesDet.getProductionKeyIssuesDetId());
                    mesPmProductionKeyIssuesOrderDets.add(mesPmProductionKeyIssuesOrderDet);
                }
            }else {
                searchBaseProductionKeyIssues.setMaterialId(null);
                searchBaseProductionKeyIssues.setKeyIssuesType((byte)2);
                List<BaseProductionKeyIssues> baseProductionKeyIssuesList1 = baseFeignApi.findList(searchBaseProductionKeyIssues).getData();
                if(StringUtils.isNotEmpty(baseProductionKeyIssuesList1)) {
                    List<BaseProductionKeyIssuesDet> baseProductionKeyIssuesDetList = baseProductionKeyIssuesList1.get(0).getBaseProductionKeyIssuesDetList();
                    for (BaseProductionKeyIssuesDet baseProductionKeyIssuesDet : baseProductionKeyIssuesDetList) {
                        MesPmProductionKeyIssuesOrderDet mesPmProductionKeyIssuesOrderDet = new MesPmProductionKeyIssuesOrderDet();
                        mesPmProductionKeyIssuesOrderDet.setProductionKeyIssuesDetId(baseProductionKeyIssuesDet.getProductionKeyIssuesDetId());
                        mesPmProductionKeyIssuesOrderDets.add(mesPmProductionKeyIssuesOrderDet);
                    }
                }else {
                    throw new BizErrorException("该产品未绑定产前确认项");
                }
            }
            mesPmProductionKeyIssuesOrder.setMesPmProductionKeyIssuesOrderDetList(mesPmProductionKeyIssuesOrderDets);

            //新增
            this.save(mesPmProductionKeyIssuesOrder);
            mesPmProductionKeyIssuesOrder = this.PDAFindOne(workOrderCode);
        }else {
            mesPmProductionKeyIssuesOrder = list.get(0);
        }

        return mesPmProductionKeyIssuesOrder;
    }

    @Override
    public List<MesPmProductionKeyIssuesOrder> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            map.put("orgId", user.getOrganizationId());
        }

        return mesPmProductionKeyIssuesOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmProductionKeyIssuesOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断是否重复
        Example example = new Example(MesPmProductionKeyIssuesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId",record.getWorkOrderId());
        List<MesPmProductionKeyIssuesOrder> mesPmProductionKeyIssuesOrderList = mesPmProductionKeyIssuesOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(mesPmProductionKeyIssuesOrderList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增产前关键事项确认
        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?(byte)1:record.getStatus());
        record.setOrderStatus(StringUtils.isEmpty(record.getOrderStatus())?(byte)1:record.getOrderStatus());
        int i = mesPmProductionKeyIssuesOrderMapper.insertUseGeneratedKeys(record);

        //新增履历
        MesPmHtProductionKeyIssuesOrder mesPmHtProductionKeyIssuesOrder = new MesPmHtProductionKeyIssuesOrder();
        BeanUtils.copyProperties(record, mesPmHtProductionKeyIssuesOrder);
        mesPmHtProductionKeyIssuesOrderMapper.insertSelective(mesPmHtProductionKeyIssuesOrder);

        //新增明细
        List<MesPmProductionKeyIssuesOrderDet> mesPmProductionKeyIssuesOrderDetList = record.getMesPmProductionKeyIssuesOrderDetList();
        if(StringUtils.isNotEmpty(mesPmProductionKeyIssuesOrderDetList)){
            for (MesPmProductionKeyIssuesOrderDet mesPmProductionKeyIssuesOrderDet : mesPmProductionKeyIssuesOrderDetList){
                mesPmProductionKeyIssuesOrderDet.setProductionKeyIssuesOrderId(record.getProductionKeyIssuesOrderId());
                mesPmProductionKeyIssuesOrderDet.setCreateTime(new Date());
                mesPmProductionKeyIssuesOrderDet.setCreateUserId(user.getUserId());
                mesPmProductionKeyIssuesOrderDet.setModifiedTime(new Date());
                mesPmProductionKeyIssuesOrderDet.setModifiedUserId(user.getUserId());
                mesPmProductionKeyIssuesOrderDet.setOrgId(user.getOrganizationId());
                mesPmProductionKeyIssuesOrderDet.setStatus(StringUtils.isEmpty(mesPmProductionKeyIssuesOrderDet.getStatus())?(byte)1: mesPmProductionKeyIssuesOrderDet.getStatus());
            }
            mesPmProductionKeyIssuesOrderDetMapper.insertList(mesPmProductionKeyIssuesOrderDetList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MesPmProductionKeyIssuesOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断是否重复
        Example example = new Example(MesPmProductionKeyIssuesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId",entity.getWorkOrderId())
                .andNotEqualTo("productionKeyIssuesOrderId",entity.getProductionKeyIssuesOrderId());
        List<MesPmProductionKeyIssuesOrder> mesPmProductionKeyIssuesOrderList = mesPmProductionKeyIssuesOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(mesPmProductionKeyIssuesOrderList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //删除原明细
        Example example1 = new Example(MesPmProductionKeyIssuesOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("productionKeyIssuesOrderId",entity.getProductionKeyIssuesOrderId());
        mesPmProductionKeyIssuesOrderDetMapper.deleteByExample(example1);

        //新增明细
        int count = 0;
        List<MesPmProductionKeyIssuesOrderDet> mesPmProductionKeyIssuesOrderDetList = entity.getMesPmProductionKeyIssuesOrderDetList();
        if(StringUtils.isNotEmpty(mesPmProductionKeyIssuesOrderDetList)){
            for (MesPmProductionKeyIssuesOrderDet mesPmProductionKeyIssuesOrderDet : mesPmProductionKeyIssuesOrderDetList){
                mesPmProductionKeyIssuesOrderDet.setProductionKeyIssuesOrderId(entity.getProductionKeyIssuesOrderId());
                mesPmProductionKeyIssuesOrderDet.setCreateTime(new Date());
                mesPmProductionKeyIssuesOrderDet.setCreateUserId(user.getUserId());
                mesPmProductionKeyIssuesOrderDet.setModifiedTime(new Date());
                mesPmProductionKeyIssuesOrderDet.setModifiedUserId(user.getUserId());
                mesPmProductionKeyIssuesOrderDet.setOrgId(user.getOrganizationId());
                mesPmProductionKeyIssuesOrderDet.setStatus(StringUtils.isEmpty(mesPmProductionKeyIssuesOrderDet.getStatus())?(byte)1: mesPmProductionKeyIssuesOrderDet.getStatus());
                if(StringUtils.isNotEmpty(mesPmProductionKeyIssuesOrderDet.getYesOrNo())||StringUtils.isNotEmpty(mesPmProductionKeyIssuesOrderDet.getValue())){
                    count++;
                }
            }
            mesPmProductionKeyIssuesOrderDetMapper.insertList(mesPmProductionKeyIssuesOrderDetList);
        }

        //修改产前关键事项确认
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        entity.setOrgId(user.getOrganizationId());
        entity.setOrderStatus(count == mesPmProductionKeyIssuesOrderDetList.size() ? (byte)2 : (byte)1);
        int i = mesPmProductionKeyIssuesOrderMapper.updateByPrimaryKeySelective(entity);

        //新增履历
        MesPmHtProductionKeyIssuesOrder mesPmHtProductionKeyIssuesOrder = new MesPmHtProductionKeyIssuesOrder();
        BeanUtils.copyProperties(entity, mesPmHtProductionKeyIssuesOrder);
        mesPmHtProductionKeyIssuesOrderMapper.insertSelective(mesPmHtProductionKeyIssuesOrder);

        return i;
    }
}

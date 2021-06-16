package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseProducttionKeyIssues;
import com.fantechs.common.base.general.entity.basic.BaseProducttionKeyIssuesDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProducttionKeyIssues;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProducttionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProducttionKeyIssuesOrderDet;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProducttionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmProducttionKeyIssuesOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesPmHtProducttionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmProducttionKeyIssuesOrderDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmProducttionKeyIssuesOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmProducttionKeyIssuesOrderService;
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
public class MesPmProducttionKeyIssuesOrderServiceImpl extends BaseService<MesPmProducttionKeyIssuesOrder> implements MesPmProducttionKeyIssuesOrderService {

    @Resource
    private MesPmProducttionKeyIssuesOrderMapper mesPmProducttionKeyIssuesOrderMapper;
    @Resource
    private MesPmProducttionKeyIssuesOrderDetMapper mesPmProducttionKeyIssuesOrderDetMapper;
    @Resource
    private MesPmHtProducttionKeyIssuesOrderMapper mesPmHtProducttionKeyIssuesOrderMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public MesPmProducttionKeyIssuesOrder PDAFindOne(String workOrderCode) {
        MesPmProducttionKeyIssuesOrder mesPmProducttionKeyIssuesOrder = new MesPmProducttionKeyIssuesOrder();

        SearchMesPmProducttionKeyIssuesOrder searchMesPmProducttionKeyIssuesOrder = new SearchMesPmProducttionKeyIssuesOrder();
        searchMesPmProducttionKeyIssuesOrder.setWorkOrderCode(workOrderCode);
        searchMesPmProducttionKeyIssuesOrder.setCodeQueryMark(1);
        List<MesPmProducttionKeyIssuesOrder> list = mesPmProducttionKeyIssuesOrderMapper.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmProducttionKeyIssuesOrder));
        //不存在数据则新增,存在则直接返回
        if(StringUtils.isEmpty(list)){
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderCode(workOrderCode);
            searchMesPmWorkOrder.setCodeQueryMark(1);
            List<MesPmWorkOrderDto> mesPmWorkOrderDtos = mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
            if(StringUtils.isEmpty(mesPmWorkOrderDtos)){
                throw new BizErrorException("不存在此工单");
            }
            MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderDtos.get(0);

            //产前关键事项设值
            mesPmProducttionKeyIssuesOrder.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
            mesPmProducttionKeyIssuesOrder.setMaterialId(mesPmWorkOrderDto.getMaterialId());

            //产前关键事项明细设值
            List<MesPmProducttionKeyIssuesOrderDet> mesPmProducttionKeyIssuesOrderDets = new ArrayList<>();
            MesPmProducttionKeyIssuesOrderDet mesPmProducttionKeyIssuesOrderDet = new MesPmProducttionKeyIssuesOrderDet();

            SearchBaseProducttionKeyIssues searchBaseProducttionKeyIssues = new SearchBaseProducttionKeyIssues();
            searchBaseProducttionKeyIssues.setMaterialId(mesPmWorkOrderDto.getMaterialId());
            List<BaseProducttionKeyIssues> baseProducttionKeyIssuesList = baseFeignApi.findList(searchBaseProducttionKeyIssues).getData();
            //产品关键事项是否维护有相应物料的数据
            if(StringUtils.isNotEmpty(baseProducttionKeyIssuesList)){
                List<BaseProducttionKeyIssuesDet> baseProducttionKeyIssuesDetList = baseProducttionKeyIssuesList.get(0).getBaseProducttionKeyIssuesDetList();
                for (BaseProducttionKeyIssuesDet baseProducttionKeyIssuesDet:baseProducttionKeyIssuesDetList){
                    mesPmProducttionKeyIssuesOrderDet.setProducttionKeyIssuesDetId(baseProducttionKeyIssuesDet.getProducttionKeyIssuesDetId());
                    mesPmProducttionKeyIssuesOrderDets.add(mesPmProducttionKeyIssuesOrderDet);
                }
            }else {
                searchBaseProducttionKeyIssues.setMaterialId(null);
                searchBaseProducttionKeyIssues.setKeyIssuesType((byte)2);
                List<BaseProducttionKeyIssues> baseProducttionKeyIssuesList1 = baseFeignApi.findList(searchBaseProducttionKeyIssues).getData();
                if(StringUtils.isNotEmpty(baseProducttionKeyIssuesList1)) {
                    List<BaseProducttionKeyIssuesDet> baseProducttionKeyIssuesDetList = baseProducttionKeyIssuesList1.get(0).getBaseProducttionKeyIssuesDetList();
                    for (BaseProducttionKeyIssuesDet baseProducttionKeyIssuesDet : baseProducttionKeyIssuesDetList) {
                        mesPmProducttionKeyIssuesOrderDet.setProducttionKeyIssuesDetId(baseProducttionKeyIssuesDet.getProducttionKeyIssuesDetId());
                        mesPmProducttionKeyIssuesOrderDets.add(mesPmProducttionKeyIssuesOrderDet);
                    }
                }
            }
            mesPmProducttionKeyIssuesOrder.setMesPmProducttionKeyIssuesOrderDetList(mesPmProducttionKeyIssuesOrderDets);

            //新增
            this.save(mesPmProducttionKeyIssuesOrder);
            mesPmProducttionKeyIssuesOrder = this.PDAFindOne(workOrderCode);
        }else {
            mesPmProducttionKeyIssuesOrder = list.get(0);
        }

        return mesPmProducttionKeyIssuesOrder;
    }

    @Override
    public List<MesPmProducttionKeyIssuesOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        map.put("orgId", user.getOrganizationId());
        return mesPmProducttionKeyIssuesOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmProducttionKeyIssuesOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断是否重复
        Example example = new Example(MesPmProducttionKeyIssuesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",record.getMaterialId())
                .orEqualTo("workOrderId",record.getWorkOrderId());
        List<MesPmProducttionKeyIssuesOrder> mesPmProducttionKeyIssuesOrderList = mesPmProducttionKeyIssuesOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(mesPmProducttionKeyIssuesOrderList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增产前关键事项确认
        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?(byte)1:record.getStatus());
        int i = mesPmProducttionKeyIssuesOrderMapper.insertUseGeneratedKeys(record);

        //新增履历
        MesPmHtProducttionKeyIssuesOrder mesPmHtProducttionKeyIssuesOrder = new MesPmHtProducttionKeyIssuesOrder();
        BeanUtils.copyProperties(record,mesPmHtProducttionKeyIssuesOrder);
        mesPmHtProducttionKeyIssuesOrderMapper.insertSelective(mesPmHtProducttionKeyIssuesOrder);

        //新增明细
        List<MesPmProducttionKeyIssuesOrderDet> mesPmProducttionKeyIssuesOrderDetList = record.getMesPmProducttionKeyIssuesOrderDetList();
        if(StringUtils.isNotEmpty(mesPmProducttionKeyIssuesOrderDetList)){
            for (MesPmProducttionKeyIssuesOrderDet mesPmProducttionKeyIssuesOrderDet:mesPmProducttionKeyIssuesOrderDetList){
                mesPmProducttionKeyIssuesOrderDet.setProducttionKeyIssuesOrderId(record.getProducttionKeyIssuesOrderId());
                mesPmProducttionKeyIssuesOrderDet.setCreateTime(new Date());
                mesPmProducttionKeyIssuesOrderDet.setCreateUserId(user.getUserId());
                mesPmProducttionKeyIssuesOrderDet.setModifiedTime(new Date());
                mesPmProducttionKeyIssuesOrderDet.setModifiedUserId(user.getUserId());
                mesPmProducttionKeyIssuesOrderDet.setOrgId(user.getOrganizationId());
                mesPmProducttionKeyIssuesOrderDet.setStatus(StringUtils.isEmpty(mesPmProducttionKeyIssuesOrderDet.getStatus())?(byte)1:mesPmProducttionKeyIssuesOrderDet.getStatus());
            }
            mesPmProducttionKeyIssuesOrderDetMapper.insertList(mesPmProducttionKeyIssuesOrderDetList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MesPmProducttionKeyIssuesOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断是否重复
        Example example = new Example(MesPmProducttionKeyIssuesOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",entity.getMaterialId())
                .orEqualTo("workOrderId",entity.getWorkOrderId())
                .andNotEqualTo("producttionKeyIssuesOrderId",entity.getProducttionKeyIssuesOrderId());
        List<MesPmProducttionKeyIssuesOrder> mesPmProducttionKeyIssuesOrderList = mesPmProducttionKeyIssuesOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(mesPmProducttionKeyIssuesOrderList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改产前关键事项确认
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        entity.setOrgId(user.getOrganizationId());
        int i = mesPmProducttionKeyIssuesOrderMapper.updateByPrimaryKeySelective(entity);

        //新增履历
        MesPmHtProducttionKeyIssuesOrder mesPmHtProducttionKeyIssuesOrder = new MesPmHtProducttionKeyIssuesOrder();
        BeanUtils.copyProperties(entity,mesPmHtProducttionKeyIssuesOrder);
        mesPmHtProducttionKeyIssuesOrderMapper.insertSelective(mesPmHtProducttionKeyIssuesOrder);

        //删除原明细
        Example example1 = new Example(MesPmProducttionKeyIssuesOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("producttionKeyIssuesOrderId",entity.getProducttionKeyIssuesOrderId());
        mesPmProducttionKeyIssuesOrderDetMapper.deleteByExample(example1);

        //新增明细
        List<MesPmProducttionKeyIssuesOrderDet> mesPmProducttionKeyIssuesOrderDetList = entity.getMesPmProducttionKeyIssuesOrderDetList();
        if(StringUtils.isNotEmpty(mesPmProducttionKeyIssuesOrderDetList)){
            for (MesPmProducttionKeyIssuesOrderDet mesPmProducttionKeyIssuesOrderDet:mesPmProducttionKeyIssuesOrderDetList){
                mesPmProducttionKeyIssuesOrderDet.setProducttionKeyIssuesOrderId(entity.getProducttionKeyIssuesOrderId());
                mesPmProducttionKeyIssuesOrderDet.setCreateTime(new Date());
                mesPmProducttionKeyIssuesOrderDet.setCreateUserId(user.getUserId());
                mesPmProducttionKeyIssuesOrderDet.setModifiedTime(new Date());
                mesPmProducttionKeyIssuesOrderDet.setModifiedUserId(user.getUserId());
                mesPmProducttionKeyIssuesOrderDet.setOrgId(user.getOrganizationId());
                mesPmProducttionKeyIssuesOrderDet.setStatus(StringUtils.isEmpty(mesPmProducttionKeyIssuesOrderDet.getStatus())?(byte)1:mesPmProducttionKeyIssuesOrderDet.getStatus());
            }
            mesPmProducttionKeyIssuesOrderDetMapper.insertList(mesPmProducttionKeyIssuesOrderDetList);
        }

        return i;
    }
}

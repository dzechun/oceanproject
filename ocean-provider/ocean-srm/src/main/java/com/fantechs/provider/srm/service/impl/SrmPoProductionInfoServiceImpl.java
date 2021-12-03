package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmPoProductionInfoDto;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.srm.SrmPoProductionInfo;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPoProductionInfo;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.srm.mapper.SrmHtPoProductionInfoMapper;
import com.fantechs.provider.srm.mapper.SrmPoProductionInfoMapper;
import com.fantechs.provider.srm.service.SrmPoProductionInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/17.
 */
@Service
public class SrmPoProductionInfoServiceImpl extends BaseService<SrmPoProductionInfo> implements SrmPoProductionInfoService {

    @Resource
    private SrmPoProductionInfoMapper srmPoProductionInfoMapper;
    @Resource
    private SrmHtPoProductionInfoMapper srmHtPoProductionInfoMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private OMFeignApi omFeignApi;

    @Override
    public List<SrmPoProductionInfoDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(map.get("orgId"))) {
            map.put("orgId",user.getOrganizationId());
        }


        if (StringUtils.isNotEmpty(user.getSupplierId())) {
            map.put("supplierId", user.getSupplierId());
        }

        return srmPoProductionInfoMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SrmPoProductionInfoDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SrmPoProductionInfo> srmPoProductionInfoList = new LinkedList<>();
        LinkedList<SrmHtPoProductionInfo> srmHtPoProductionInfoList = new LinkedList<>();
        SearchOmPurchaseOrder searchOmPurchaseOrder = new SearchOmPurchaseOrder();
        searchOmPurchaseOrder.setCodeQueryMark(1);
        SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();

        for (int i = 0; i < list.size(); i++) {
            SrmPoProductionInfoDto srmPoProductionInfoDto = list.get(i);

            searchOmPurchaseOrder.setPurchaseOrderCode(srmPoProductionInfoDto.getPurchaseOrderCode());
            List<OmPurchaseOrderDto> omPurchaseOrder = omFeignApi.findList(searchOmPurchaseOrder).getData();
            if (StringUtils.isEmpty(omPurchaseOrder)) {
                fail.add(i + 4);
                continue;
            }

            srmPoProductionInfoDto.setSupplierId(omPurchaseOrder.get(0).getSupplierId());
            searchOmPurchaseOrderDet.setPurchaseOrderId(omPurchaseOrder.get(0).getPurchaseOrderId());
            List<OmPurchaseOrderDetDto> omPurchaseOrderDetDtoList = omFeignApi.findList(searchOmPurchaseOrderDet).getData();
            for (OmPurchaseOrderDetDto omPurchaseOrderDetDto : omPurchaseOrderDetDtoList) {
                if (srmPoProductionInfoDto.getMaterialCode().equals(omPurchaseOrderDetDto.getMaterialCode())) {
                    srmPoProductionInfoDto.setMaterialId(omPurchaseOrderDetDto.getMaterialId());
                    break;
                }
            }

            if (StringUtils.isEmpty(srmPoProductionInfoDto.getMaterialId())) {
                fail.add(i + 4);
                continue;
            }
            srmPoProductionInfoDto.setCreateTime(new Date());
            srmPoProductionInfoDto.setModifiedTime(new Date());
            srmPoProductionInfoDto.setModifiedUserId(user.getUserId());
            srmPoProductionInfoDto.setCreateUserId(user.getUserId());
            srmPoProductionInfoDto.setOrgId(user.getOrganizationId());
            srmPoProductionInfoDto.setStatus((byte) 1);

            srmPoProductionInfoDto.setPurchaseOrderId(omPurchaseOrder.get(0).getPurchaseOrderId());
            SrmHtPoProductionInfo srmHtPoProductionInfo = new SrmHtPoProductionInfo();
            BeanUtils.copyProperties(srmPoProductionInfoDto, srmHtPoProductionInfo);
            srmHtPoProductionInfoList.add(srmHtPoProductionInfo);

            srmPoProductionInfoList.add(srmPoProductionInfoDto);
            success++;
        }

        if (StringUtils.isNotEmpty(srmPoProductionInfoList)) {
            srmPoProductionInfoMapper.insertList(srmPoProductionInfoList);
        }

        if (StringUtils.isNotEmpty(srmHtPoProductionInfoList)) {
            srmHtPoProductionInfoMapper.insertList(srmHtPoProductionInfoList);
        }


        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SrmPoProductionInfo record) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if (StringUtils.isEmpty(record.getOrderQty()) || record.getWorkOrderQty().compareTo(record.getOrderQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"工单数量大于采购数量");
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = srmPoProductionInfoMapper.insertUseGeneratedKeys(record);



        SrmHtPoProductionInfo srmHtPoProductionInfo = new SrmHtPoProductionInfo();
        BeanUtils.copyProperties(record, srmHtPoProductionInfo);
        srmHtPoProductionInfoMapper.insertSelective(srmHtPoProductionInfo);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SrmPoProductionInfo entity) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if (StringUtils.isEmpty(entity.getOrderQty()) || entity.getWorkOrderQty().compareTo(entity.getOrderQty()) == 1) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"工单数量大于采购数量");
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());

        int i = srmPoProductionInfoMapper.updateByPrimaryKeySelective(entity);

        SrmHtPoProductionInfo srmHtPoProductionInfo = new SrmHtPoProductionInfo();
        BeanUtils.copyProperties(entity, srmHtPoProductionInfo);
        srmHtPoProductionInfoMapper.insertSelective(srmHtPoProductionInfo);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {

        List<SrmHtPoProductionInfo> htList = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            SrmPoProductionInfo srmPoProductionInfo = srmPoProductionInfoMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(srmPoProductionInfo)){
                continue;
            }

            SrmHtPoProductionInfo srmHtPoProductionInfo = new SrmHtPoProductionInfo();
            BeanUtils.copyProperties(srmPoProductionInfo, srmHtPoProductionInfo);
            htList.add(srmHtPoProductionInfo);

        }

        if (StringUtils.isNotEmpty(htList)) {
            srmHtPoProductionInfoMapper.insertList(htList);
        }

        return srmPoProductionInfoMapper.deleteByIds(ids);
    }
}

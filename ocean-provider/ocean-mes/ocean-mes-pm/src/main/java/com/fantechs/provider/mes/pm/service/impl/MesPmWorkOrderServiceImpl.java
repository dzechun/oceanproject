package com.fantechs.provider.mes.pm.service.impl;


import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessReM;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderProcessReWo;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesPmHtWorkOrderMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderBomMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderProcessReWoService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class MesPmWorkOrderServiceImpl extends BaseService<MesPmWorkOrder> implements MesPmWorkOrderService {

    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private MesPmHtWorkOrderMapper smtHtWorkOrderMapper;
    @Resource
    private MesPmWorkOrderBomMapper mesPmWorkOrderBomMapper;
    @Resource
    private MesPmWorkOrderProcessReWoService mesPmWorkOrderProcessReWoService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmWorkOrderDto mesPmWorkOrderDto) {
        SysUser currentUser = currentUser();


        if(StringUtils.isEmpty(mesPmWorkOrderDto.getWorkOrderCode())){
            mesPmWorkOrderDto.setWorkOrderCode(CodeUtils.getId("WORK"));
        }else{
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrderDto.getWorkOrderCode());

            List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(mesPmWorkOrders)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }

        mesPmWorkOrderDto.setWorkOrderStatus((byte) 1);
        mesPmWorkOrderDto.setCreateUserId(currentUser.getUserId());
        mesPmWorkOrderDto.setCreateTime(new Date());
        mesPmWorkOrderDto.setModifiedUserId(currentUser.getUserId());
        mesPmWorkOrderDto.setModifiedTime(new Date());
        mesPmWorkOrderDto.setOrgId(currentUser.getOrganizationId());
        mesPmWorkOrderDto.setIsDelete((byte)1);
        int i = mesPmWorkOrderMapper.insertUseGeneratedKeys(mesPmWorkOrderDto);

        //???????????????
        recordHistory(mesPmWorkOrderDto);
        //??????bom???
        savebom(mesPmWorkOrderDto,currentUser);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateInventoryQty(MesPmWorkOrder mesPmWorkOrder){
       return mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(MesPmWorkOrderDto mesPmWorkOrderDto) {
        int i = 0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        MesPmWorkOrder order = mesPmWorkOrderMapper.selectByPrimaryKey(mesPmWorkOrderDto.getWorkOrderId());

        //????????????(0???????????? 1???????????? 2??????????????? 3???????????????)
        Integer workOrderStatus = order.getWorkOrderStatus().intValue();
        if (workOrderStatus != 4) {
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrderDto.getWorkOrderCode());

            MesPmWorkOrder workOrder = mesPmWorkOrderMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(workOrder) && !workOrder.getWorkOrderId().equals(mesPmWorkOrderDto.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            mesPmWorkOrderDto.setModifiedUserId(currentUser.getUserId());
            mesPmWorkOrderDto.setModifiedTime(new Date());

            //?????????????????????????????????????????????????????????
            if(StringUtils.isNotEmpty(mesPmWorkOrderDto.getRouteId()) && !mesPmWorkOrderDto.getRouteId().equals(workOrder.getRouteId())){
                ResponseEntity<List<BaseRouteProcess>> responseEntity = baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012008);
                }
                List<BaseRouteProcess> routeProcessList = responseEntity.getData();
                //????????????
                Optional<BaseRouteProcess> lastRouteProcessOptional = routeProcessList.stream()
                        .filter(item -> item.getIsMustPass().equals(1))
                        .sorted(Comparator.comparing(BaseRouteProcess::getOrderNum).reversed())
                        .findFirst();
                if(lastRouteProcessOptional.isPresent()) {
                    BaseRouteProcess lastRouteProcess = lastRouteProcessOptional.get();
                    mesPmWorkOrderDto.setOutputProcessId(lastRouteProcess.getProcessId());
                }

                //????????????
                Optional<BaseRouteProcess> firstRouteProcessOptional = routeProcessList.stream()
                        .filter(item -> item.getIsMustPass().equals(1))
                        .sorted(Comparator.comparing(BaseRouteProcess::getOrderNum))
                        .findFirst();
                if(firstRouteProcessOptional.isPresent()) {
                    BaseRouteProcess firstRouteProcess = firstRouteProcessOptional.get();
                    mesPmWorkOrderDto.setPutIntoProcessId(firstRouteProcess.getProcessId());
                }

            }

            i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrderDto);

            //????????????????????????
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrderDto, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
        } else {
            throw new BizErrorException("????????????????????????????????????");
        }

        Example detExample = new Example(MesPmWorkOrderBom.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("workOrderId", mesPmWorkOrderDto.getWorkOrderId());
        mesPmWorkOrderBomMapper.deleteByExample(detExample);
        detExample.clear();
        //??????bom???
        savebom(mesPmWorkOrderDto,currentUser);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updatePmWorkOrder(MesPmWorkOrder mesPmWorkOrder) {
        int i = 0;
        //mesPmWorkOrder.setModifiedUserId(currentUser.getUserId());
        mesPmWorkOrder.setModifiedTime(new Date());
        i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);

        //????????????????????????
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        //mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
        mesPmHtWorkOrder.setModifiedTime(new Date());
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateProductionQty(List<String> workOrderIds) {
        List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.getWorkOrderList(workOrderIds);
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(!CollectionUtils.isEmpty(mesPmWorkOrders)){
            for (MesPmWorkOrder item : mesPmWorkOrders){
                item.setModifiedUserId(currentUser.getUserId());
                item.setModifiedTime(new Date());
                if(item.getProductionQty().compareTo(BigDecimal.ONE) == 1){
                    //?????????????????????>1???????????????????????????1
                    item.setProductionQty(item.getProductionQty().subtract(BigDecimal.ONE));
                }else {
                    //?????????????????????<=1 ??????????????????=2???????????????=1
                    item.setProductionQty(item.getProductionQty().subtract(BigDecimal.ZERO));
                    item.setWorkOrderStatus((byte)2);
                    item.setScheduleStatus((byte)1);
                }
            }
             return mesPmWorkOrderMapper.batchUpdateProductionQty(mesPmWorkOrders);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<MesPmHtWorkOrder> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] workOrderIds = ids.split(",");
        for (String workOrderId : workOrderIds) {
            MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderMapper.selectByPrimaryKey(workOrderId);
            if(StringUtils.isNotEmpty(mesPmWorkOrder.getScheduledQty()) && mesPmWorkOrder.getScheduledQty().doubleValue()>0){
                throw new BizErrorException("?????????????????????????????????:"+ mesPmWorkOrder.getWorkOrderCode());
            }
            if (StringUtils.isEmpty(mesPmWorkOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //????????????????????????
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            list.add(mesPmHtWorkOrder);
        }
        if(StringUtils.isNotEmpty(list)) smtHtWorkOrderMapper.insertList(list);
        return mesPmWorkOrderMapper.deleteByIds(ids);
    }


    @Override
    public List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        if(StringUtils.isEmpty(searchMesPmWorkOrder.getOrgId())) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            searchMesPmWorkOrder.setOrgId(user.getOrganizationId());
        }
        return mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
    }

    @Override
    public List<MesPmWorkOrder> findAll() {
        return mesPmWorkOrderMapper.findAll();
    }

    @Override
    public List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        return mesPmWorkOrderMapper.pdaFindList(searchMesPmWorkOrder);
    }

    /**
     * ????????????????????????
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    /**
     * ??????????????????
     * @param mesPmWorkOrder
     */
    private void recordHistory(MesPmWorkOrder mesPmWorkOrder){
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
    }

    @Override
    public MesPmWorkOrder saveByApi(MesPmWorkOrder mesPmWorkOrder){
        Example example = new Example(MesPmWorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());
        criteria.andEqualTo("orgId", mesPmWorkOrder.getOrgId());
        List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
        //mesPmWorkOrderMapper.deleteByExample(example);
        if(StringUtils.isNotEmpty(mesPmWorkOrders)) {
            mesPmWorkOrder.setWorkOrderId(mesPmWorkOrders.get(0).getWorkOrderId());
            mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
        }else{
            mesPmWorkOrder.setModifiedTime(new Date());
            mesPmWorkOrder.setCreateTime(new Date());
            mesPmWorkOrderMapper.insertUseGeneratedKeys(mesPmWorkOrder);
        }
        example.clear();

        //???????????????????????????bom??????
        if(StringUtils.isNotEmpty(mesPmWorkOrders)) {
            Example bomExample = new Example(MesPmWorkOrderBom.class);
            Example.Criteria bomCriteria = bomExample.createCriteria();
            bomCriteria.andEqualTo("workOrderId", mesPmWorkOrders.get(0).getWorkOrderId());
            mesPmWorkOrderBomMapper.deleteByExample(bomExample);
            bomExample.clear();
        }

        return mesPmWorkOrder;
    }

    @Override
    public List<MesPmWorkOrder> getWorkOrderList(List<String> workOrderIds) {
        return mesPmWorkOrderMapper.getWorkOrderList(workOrderIds);
    }

    @Override
    public int batchUpdate(List<MesPmWorkOrder> mesPmWorkOrders) {
        return mesPmWorkOrderMapper.batchUpdate(mesPmWorkOrders);
    }

    @Override
    public int batchSave(List<MesPmWorkOrder> pmWorkOrders){
        // ????????????????????????
        SearchBaseProductProcessReM searchBaseProductProcessReM = new SearchBaseProductProcessReM();
        searchBaseProductProcessReM.setPageSize(999999);
        List<BaseProductProcessReM> baseProductProcessReMS = baseFeignApi.findList(searchBaseProductProcessReM).getData();
        for (MesPmWorkOrder order : pmWorkOrders){
            mesPmWorkOrderMapper.insertUseGeneratedKeys(order);

            // ????????????????????????
            MesPmWorkOrderProcessReWo reWo = new MesPmWorkOrderProcessReWo();
            boolean flag = false;
            for (BaseProductProcessReM baseProductProcessReM : baseProductProcessReMS){
                if (order.getMaterialId().equals(baseProductProcessReM.getMaterialId())){
                    BeanUtils.copyProperties(baseProductProcessReM, reWo);

                    List<MesPmWorkOrderMaterialRePDto> list = new ArrayList<>();
                    for (BaseProductMaterialReP baseProductMaterialReP : baseProductProcessReM.getList()){
                        MesPmWorkOrderMaterialRePDto mesPmWorkOrderMaterialRePDto = new MesPmWorkOrderMaterialRePDto();
                        BeanUtils.copyProperties(baseProductMaterialReP, mesPmWorkOrderMaterialRePDto);
//                        log.info("====================mesPmWorkOrderMaterialRePDto" + JSON.toJSONString(mesPmWorkOrderMaterialRePDto));
                        list.add(mesPmWorkOrderMaterialRePDto);
                    }
                    reWo.setList(list);

                    flag = true;
                    break;
                }
            }
            if(flag){
                reWo.setWorkOrderId(order.getWorkOrderId());
                reWo.setCreateTime(new Date());
                reWo.setModifiedTime(new Date());
                mesPmWorkOrderProcessReWoService.save(reWo);
            }
        }
        return 1;
    }

    public  void savebom(MesPmWorkOrderDto mesPmWorkOrderDto , SysUser user){
        List<MesPmWorkOrderBom> boms = new ArrayList<>();
        if(StringUtils.isNotEmpty(mesPmWorkOrderDto.getMesPmWorkOrderBomDtos())) {
            for (MesPmWorkOrderBomDto mesPmWorkOrderBomDto :  mesPmWorkOrderDto.getMesPmWorkOrderBomDtos()) {
                mesPmWorkOrderBomDto.setCreateUserId(user.getUserId());
                mesPmWorkOrderBomDto.setCreateTime(new Date());
                mesPmWorkOrderBomDto.setModifiedUserId(user.getUserId());
                mesPmWorkOrderBomDto.setModifiedTime(new Date());
                mesPmWorkOrderBomDto.setOrgId(user.getOrganizationId());
                mesPmWorkOrderBomDto.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
                boms.add(mesPmWorkOrderBomDto);
            }
        }
        if(StringUtils.isNotEmpty(boms))  mesPmWorkOrderBomMapper.insertList(boms);
    }

}

package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.SaveWorkOrderAndBom;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderBomService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardCollocationService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MesPmWorkOrderServiceImpl extends BaseService<MesPmWorkOrder> implements MesPmWorkOrderService {

    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private SmtHtWorkOrderMapper smtHtWorkOrderMapper;
    @Resource
    private SmtWorkOrderBomMapper smtWorkOrderBomMapper;
    @Resource
    private SmtHtWorkOrderBomMapper smtHtWorkOrderBomMapper;
    @Resource
    private SmtWorkOrderCardCollocationMapper smtWorkOrderCardCollocationMapper;
    @Resource
    private SmtStockMapper smtStockMapper;
    @Resource
    private SmtStockDetMapper smtStockDetMapper;
    @Resource
    private SmtWorkOrderBomService smtWorkOrderBomService;
    @Resource
    private SmtWorkOrderCardCollocationService smtWorkOrderCardCollocationService;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmWorkOrder mesPmWorkOrder) {
        SysUser currentUser = currentUser();


        if(StringUtils.isEmpty(mesPmWorkOrder.getWorkOrderCode())){
            mesPmWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));
        }else{
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());

            List<MesPmWorkOrder> mesPmWorkOrders = mesPmWorkOrderMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(mesPmWorkOrders)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }
        }


        mesPmWorkOrder.setCreateUserId(currentUser.getUserId());
        mesPmWorkOrder.setCreateTime(new Date());
        if(mesPmWorkOrderMapper.insertSelective(mesPmWorkOrder)<=0){
            return 0;
        }

        //新增工单历史信息
        recordHistory(mesPmWorkOrder,"新增");


        //根据产品BOM生成工单BOM
        //特殊声明是否产生BOM
//        if(StringUtils.isEmpty(mesPmWorkOrder.getRemark()) || !mesPmWorkOrder.getRemark().equals("无BOM")){
//            //生成备料单
//            SmtStock smtStock = new SmtStock();
//            smtStock.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
//            smtStock.setDeliveryMode(new Byte("0"));
//            smtStock.setStatus(new Byte("0"));
//            smtStock.setStockCode(CodeUtils.getId("BLD-"));
//            // Date date = smtWorkOrder.getPlannedStartTime();
//            // Date afterDate = new Date(date.getTime() + 600000);
//            BeanUtils.copyProperties(mesPmWorkOrder, smtStock, new String[]{"createUserId", "createTime", "modifiedUserId", "modifiedTime"});
//            smtStockMapper.insertUseGeneratedKeys(smtStock);
//
//            genWorkOrder(mesPmWorkOrder, smtStock);
//        }

        return 1;
    }

    /**
     * 根据产品BOM明细生成工单BOM信息
     *
     * @param mesPmWorkOrder
     */
    @Transactional(rollbackFor = Exception.class)
    public void genWorkOrder(MesPmWorkOrder mesPmWorkOrder, SmtStock smtStock) {
        SysUser currentUser = currentUser();
        List<SmtWorkOrderBom> list = new ArrayList<>();
        List<SmtHtWorkOrderBom> htList = new ArrayList<>();

        List<SmtStockDet> stockDetList = new ArrayList<>();
        //根据物料ID查询产品BOM信息
        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
        searchBaseProductBom.setMaterialId(mesPmWorkOrder.getMaterialId());
        searchBaseProductBom.setIsBomDet(new Byte("1"));
        List<BaseProductBomDto> smtProductBoms = basicFeignApi.findProductBomList(searchBaseProductBom).getData();
        if (StringUtils.isNotEmpty(smtProductBoms)) {
            for (BaseProductBomDto smtProductBomDet : smtProductBoms) {
                SmtWorkOrderBom smtWorkOrderBom = new SmtWorkOrderBom();
                BeanUtils.copyProperties(smtProductBomDet, smtWorkOrderBom, new String[]{"createUserId", "createTime", "modifiedUserId", "modifiedTime"});
                BigDecimal workOrderQuantity = mesPmWorkOrder.getWorkOrderQty();
                BigDecimal quantity = StringUtils.isEmpty(smtProductBomDet.getQuantity())?new BigDecimal(1):smtProductBomDet.getQuantity();
                BigDecimal baseQuantity = StringUtils.isEmpty(smtProductBomDet.getBaseQuantity())?new BigDecimal(1):smtProductBomDet.getBaseQuantity();
                smtWorkOrderBom.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
                smtWorkOrderBom.setPartMaterialId(smtProductBomDet.getMaterialId());
                smtWorkOrderBom.setSingleQuantity(quantity);
                if (StringUtils.isNotEmpty(baseQuantity,quantity)){
                    smtWorkOrderBom.setQuantity(new BigDecimal(workOrderQuantity.toString()).multiply(quantity).multiply(baseQuantity));
                }
                smtWorkOrderBom.setCreateUserId(currentUser.getUserId());
                smtWorkOrderBom.setCreateTime(new Date());
                list.add(smtWorkOrderBom);

                //备料单明细
                SmtStockDet smtStockDet = new SmtStockDet();
                smtStockDet.setStockId(smtStock.getStockId());
                smtStockDet.setMaterialId(StringUtils.isEmpty(smtWorkOrderBom.getPartMaterialId())?smtWorkOrderBom.getSubMaterialId():smtWorkOrderBom.getPartMaterialId());
                smtStockDet.setPlanQuantity(smtWorkOrderBom.getQuantity());
                smtStockDet.setStockQuantity(StringUtils.isEmpty(smtWorkOrderBom.getBaseQuantity())?new BigDecimal(1):smtWorkOrderBom.getBaseQuantity());
                smtStockDet.setStatus(new Byte("0"));
                BeanUtils.copyProperties(smtWorkOrderBom, smtStockDet, new String[]{"createUserId", "createTime", "modifiedUserId", "modifiedTime"});
                stockDetList.add(smtStockDet);
            }
            //批量新增工单BOM信息
            smtWorkOrderBomMapper.insertList(list);

            //批量新增备料明细
            smtStockDetMapper.insertList(stockDetList);

            if (StringUtils.isNotEmpty(list)) {
                for (SmtWorkOrderBom smtWorkOrderBom : list) {
                    //新增工单BOM历史信息
                    SmtHtWorkOrderBom smtHtWorkOrderBom = new SmtHtWorkOrderBom();
                    BeanUtils.copyProperties(smtWorkOrderBom, smtHtWorkOrderBom);
                    smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                    smtHtWorkOrderBom.setModifiedTime(new Date());
                    htList.add(smtHtWorkOrderBom);
                }
            }
            //批量新增工单BOM历史信息
            smtHtWorkOrderBomMapper.insertList(htList);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MesPmWorkOrder mesPmWorkOrder) {
        int i = 0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        MesPmWorkOrder order = mesPmWorkOrderMapper.selectByPrimaryKey(mesPmWorkOrder.getWorkOrderId());

        //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
        Integer workOrderStatus = order.getWorkOrderStatus().intValue();
        if (workOrderStatus != 4) {
            Example example = new Example(MesPmWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", mesPmWorkOrder.getWorkOrderCode());

            MesPmWorkOrder workOrder = mesPmWorkOrderMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(workOrder) && !workOrder.getWorkOrderId().equals(mesPmWorkOrder.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }


            mesPmWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmWorkOrder.setModifiedTime(new Date());
            mesPmWorkOrder.setCreateTime(null);
            i = mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);


            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
        } else {
            throw new BizErrorException("生产完成的工单不允许修改");
        }

        return i;
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
                throw new BizErrorException("工单已排产，不允许删除:"+ mesPmWorkOrder.getWorkOrderCode());
            }
            if (StringUtils.isEmpty(mesPmWorkOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工单历史信息
            MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
            BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
            mesPmHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            mesPmHtWorkOrder.setModifiedTime(new Date());
            list.add(mesPmHtWorkOrder);

            Example example = new Example(SmtWorkOrderBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderId", workOrderId);
            smtWorkOrderBomMapper.deleteByExample(example);
        }
        smtHtWorkOrderMapper.insertList(list);

        return mesPmWorkOrderMapper.deleteByIds(ids);
    }


    @Override
    public List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        List<MesPmWorkOrderDto> list = mesPmWorkOrderMapper.findList(searchMesPmWorkOrder);
        return list;
    }

    @Override
    public List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder) {
        return mesPmWorkOrderMapper.pdaFindList(searchMesPmWorkOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveWorkOrderDTO(SaveWorkOrderAndBom saveWorkOrderAndBom) {
        MesPmWorkOrder mesPmWorkOrder = saveWorkOrderAndBom.getMesPmWorkOrder();
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getWorkOrderId())){
            if(this.update(mesPmWorkOrder)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }else{
            if(this.save(mesPmWorkOrder)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        return 1;
    }

    /**
     * 获取当前登录用户
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
     * 记录操作历史
     * @param mesPmWorkOrder
     * @param operation
     */
    private void recordHistory(MesPmWorkOrder mesPmWorkOrder, String operation){
        MesPmHtWorkOrder mesPmHtWorkOrder = new MesPmHtWorkOrder();
        mesPmHtWorkOrder.setOption1(operation);
        if (StringUtils.isEmpty(mesPmWorkOrder)){
            return;
        }
        BeanUtils.copyProperties(mesPmWorkOrder, mesPmHtWorkOrder);
        smtHtWorkOrderMapper.insertSelective(mesPmHtWorkOrder);
    }


}

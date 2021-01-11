package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SaveWorkOrderAndBom;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.*;
import com.fantechs.common.base.entity.apply.history.MesHtOrderMaterial;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.WmsInStorageBills;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.*;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBomService;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderService;
import com.fantechs.provider.imes.apply.utils.BarcodeRuleUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wcz on 2020/10/13.
 */
@Service
public class SmtWorkOrderServiceImpl extends BaseService<SmtWorkOrder> implements SmtWorkOrderService {

    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;
    @Resource
    private SmtHtWorkOrderMapper smtHtWorkOrderMapper;
    @Resource
    private SmtWorkOrderBomMapper smtWorkOrderBomMapper;
    @Resource
    private SmtHtWorkOrderBomMapper smtHtWorkOrderBomMapper;
    @Resource
    private SmtWorkOrderCardCollocationMapper smtWorkOrderCardCollocationMapper;
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
    @Resource
    private SmtStockMapper smtStockMapper;
    @Resource
    private SmtStockDetMapper smtStockDetMapper;
    @Resource
    private SmtWorkOrderBomService smtWorkOrderBomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkOrder smtWorkOrder) {
        SysUser currentUser = currentUser();

        /*Example example = new Example(SmtWorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderCode", smtWorkOrder.getWorkOrderCode());

        List<SmtWorkOrder> smtWorkOrders = smtWorkOrderMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtWorkOrders)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/
        smtWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));

        smtWorkOrder.setCreateUserId(currentUser.getUserId());
        smtWorkOrder.setCreateTime(new Date());
        if(smtWorkOrderMapper.insertSelective(smtWorkOrder)<=0){
            return 0;
        }

        //新增工单历史信息
        recordHistory(smtWorkOrder.getWorkOrderId(),"新增");


        //根据产品BOM生成工单BOM
        //特殊声明是否产生BOM
        if(!smtWorkOrder.getRemark().equals("华丰")){
            //生成备料单
            SmtStock smtStock = new SmtStock();
            smtStock.setWorkOrderId(smtWorkOrder.getWorkOrderId());
            smtStock.setDeliveryMode(new Byte("0"));
            smtStock.setStatus(new Byte("1"));
            smtStock.setStockCode(CodeUtils.getId("BLD-"));
            // Date date = smtWorkOrder.getPlannedStartTime();
            // Date afterDate = new Date(date.getTime() + 600000);
            BeanUtils.copyProperties(smtWorkOrder, smtStock, new String[]{"createUserId", "createTime", "modifiedUserId", "modifiedTime"});
            smtStockMapper.insertUseGeneratedKeys(smtStock);

            genWorkOrder(smtWorkOrder, smtStock);
        }

        return 1;
    }

    /**
     * 根据产品BOM明细生成工单BOM信息
     *
     * @param smtWorkOrder
     */
    @Transactional(rollbackFor = Exception.class)
    public void genWorkOrder(SmtWorkOrder smtWorkOrder, SmtStock smtStock) {
        SysUser currentUser = currentUser();
        List<SmtWorkOrderBom> list = new ArrayList<>();
        List<SmtHtWorkOrderBom> htList = new ArrayList<>();

        List<SmtStockDet> stockDetList = new ArrayList<>();
        //根据物料ID查询产品BOM信息
        List<SmtProductBomDet> smtProductBomDets = smtWorkOrderMapper.selectProductBomDet(smtWorkOrder.getMaterialId());
        if (StringUtils.isNotEmpty(smtProductBomDets)) {
            for (SmtProductBomDet smtProductBomDet : smtProductBomDets) {
                SmtWorkOrderBom smtWorkOrderBom = new SmtWorkOrderBom();
                BeanUtils.copyProperties(smtProductBomDet, smtWorkOrderBom, new String[]{"createUserId", "createTime", "modifiedUserId", "modifiedTime"});
                BigDecimal workOrderQuantity = smtWorkOrder.getWorkOrderQuantity();
                BigDecimal quantity = StringUtils.isEmpty(smtProductBomDet.getQuantity())?new BigDecimal(1):smtProductBomDet.getQuantity();
                BigDecimal baseQuantity = StringUtils.isEmpty(smtProductBomDet.getBaseQuantity())?new BigDecimal(1):smtProductBomDet.getBaseQuantity();
                smtWorkOrderBom.setWorkOrderId(smtWorkOrder.getWorkOrderId());
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
                smtStockDet.setMaterialId(smtWorkOrderBom.getPartMaterialId());
                smtStockDet.setPlanQuantity(smtWorkOrderBom.getQuantity());
                smtStockDet.setStockQuantity(StringUtils.isEmpty(smtWorkOrderBom.getBaseQuantity())?new BigDecimal(1):smtWorkOrderBom.getBaseQuantity());
                smtStockDet.setStatus(new Byte("1"));
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
    public int update(SmtWorkOrder smtWorkOrder) {
        int i = 0;
        List<SmtWorkOrderBom> list = new ArrayList();

        //备料明细
        List<SmtHtWorkOrderBom> htList = new ArrayList<>();

        List<SmtStockDet> smtStockDetList = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        SmtWorkOrder order = smtWorkOrderMapper.selectByPrimaryKey(smtWorkOrder.getWorkOrderId());

        Example stockExample = new Example(SmtStock.class);
        stockExample.createCriteria().andEqualTo("workOrderId", smtWorkOrder.getWorkOrderId());
        SmtStock smtStock = smtStockMapper.selectOneByExample(stockExample);

        //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
        Integer workOrderStatus = order.getWorkOrderStatus();
        if (workOrderStatus != 4) {
            Example example = new Example(SmtWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode", smtWorkOrder.getWorkOrderCode());

            SmtWorkOrder workOrder = smtWorkOrderMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(workOrder) && !workOrder.getWorkOrderId().equals(smtWorkOrder.getWorkOrderId())) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            //修改了工单的产品料号，对应的工单bom也要修改
            if (!order.getMaterialId().equals(smtWorkOrder.getMaterialId())) {
                //throw new BizErrorException("工单不能修改产品料号信息");
                Example example1 = new Example(SmtWorkOrderBom.class);
                example1.createCriteria().andEqualTo("workOrderId", smtWorkOrder.getWorkOrderId());
                smtWorkOrderBomMapper.deleteByExample(example1);

                //根据产品BOM生成工单BOM
                genWorkOrder(smtWorkOrder, smtStock);
            }
            //工单的工单数量改变,重新计算零件的工单用量
            if (!order.getWorkOrderQuantity().equals(smtWorkOrder.getWorkOrderQuantity()) &&
                    (workOrderStatus == 0 || workOrderStatus == 3)) {

                Example example1 = new Example(SmtWorkOrderBom.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("workOrderId", smtWorkOrder.getWorkOrderId());
                List<SmtWorkOrderBom> workOrderBoms = smtWorkOrderBomMapper.selectByExample(example1);

                Example example2 = new Example(SmtStockDet.class);
                example2.createCriteria().andEqualTo("stockId", smtStock.getStockId());
                List<SmtStockDet> smtStockDets = smtStockDetMapper.selectByExample(example2);

                if (StringUtils.isNotEmpty(workOrderBoms)) {
                    for (SmtWorkOrderBom smtWorkOrderBom : workOrderBoms) {
                        //工单BOM的单个用量
                        BigDecimal singleQuantity = smtWorkOrderBom.getSingleQuantity();
                        BigDecimal baseQuantity = smtWorkOrderBom.getBaseQuantity();
                        //重新计算后的零件工单用量
                        smtWorkOrderBom.setQuantity(new BigDecimal(smtWorkOrder.getWorkOrderQuantity().toString()).multiply(singleQuantity).multiply(baseQuantity));
                        list.add(smtWorkOrderBom);

                        //新增工单BOM历史信息
                        SmtHtWorkOrderBom smtHtWorkOrderBom = new SmtHtWorkOrderBom();
                        BeanUtils.copyProperties(smtWorkOrderBom, smtHtWorkOrderBom);
                        smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                        smtHtWorkOrderBom.setModifiedTime(new Date());
                        htList.add(smtHtWorkOrderBom);

                        //修改备料数量
                        List<SmtStockDet> smtStockDet = smtStockDets.stream().filter(st -> st.getMaterialId().equals(smtWorkOrderBom.getPartMaterialId())).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(smtStockDet)) {
                            for (SmtStockDet stockDet : smtStockDet) {
                                stockDet.setStockId(smtStock.getStockId());
                                stockDet.setMaterialId(smtWorkOrderBom.getPartMaterialId());
                                stockDet.setPlanQuantity(smtWorkOrderBom.getQuantity());
                                stockDet.setStockQuantity(smtWorkOrderBom.getBaseQuantity());
                                smtStockDetList.add(stockDet);
                            }
                        }
                    }

                    //批量修改工单BOM的用量
                    smtWorkOrderBomMapper.updateBatch(list);

                    //批量新增工单BOM历史信息
                    smtHtWorkOrderBomMapper.insertList(htList);

                    //批量修改备料明细
                    smtStockDetMapper.updateBatch(smtStockDetList);
                }
            }


            smtWorkOrder.setModifiedUserId(currentUser.getUserId());
            smtWorkOrder.setModifiedTime(new Date());
            i = smtWorkOrderMapper.updateByPrimaryKeySelective(smtWorkOrder);


            //新增工单历史信息
            SmtHtWorkOrder smtHtWorkOrder = new SmtHtWorkOrder();
            BeanUtils.copyProperties(smtWorkOrder, smtHtWorkOrder);
            smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            smtHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(smtHtWorkOrder);
        } else {
            throw new BizErrorException("生产完成的工单不允许修改");
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<SmtHtWorkOrder> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] workOrderIds = ids.split(",");
        for (String workOrderId : workOrderIds) {
            SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(workOrderId);
            if (StringUtils.isEmpty(smtWorkOrder)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工单历史信息
            SmtHtWorkOrder smtHtWorkOrder = new SmtHtWorkOrder();
            BeanUtils.copyProperties(smtWorkOrder, smtHtWorkOrder);
            smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            smtHtWorkOrder.setModifiedTime(new Date());
            list.add(smtHtWorkOrder);

            Example example = new Example(SmtWorkOrderBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderId", workOrderId);
            List<SmtWorkOrderBom> smtWorkOrderBoms = smtWorkOrderBomMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtWorkOrderBoms)) {
                throw new BizErrorException("工单被引用，不能删除");
            }

        }
        smtHtWorkOrderMapper.insertList(list);

        return smtWorkOrderMapper.deleteByIds(ids);
    }


    @Override
    public List<SmtWorkOrderDto> findList(SearchSmtWorkOrder searchSmtWorkOrder) {
        List<SmtWorkOrderDto> list = smtWorkOrderMapper.findList(searchSmtWorkOrder);
        for (SmtWorkOrderDto smtWorkOrderDto : list) {
            Long routeId = smtWorkOrderDto.getRouteId();
            //查询工艺路线配置
            List<SmtRouteProcess> routeProcesses = smtWorkOrderMapper.selectRouteProcessByRouteId(routeId);
            if (StringUtils.isNotEmpty(routeProcesses)) {
                //投入工序
                smtWorkOrderDto.setPutIntoProcessName(routeProcesses.get(0).getProcessName());
                //产出工序
                smtWorkOrderDto.setProductionProcessName(routeProcesses.get(routeProcesses.size() - 1).getProcessName());
            }
        }
        return list;
    }

    @Override
    public List<SmtWorkOrderDto> pdaFindList(SearchSmtWorkOrder searchSmtWorkOrder) {
        return smtWorkOrderMapper.pdaFindList(searchSmtWorkOrder);
    }

    @Override
    public int saveWorkOrderDTO(SaveWorkOrderAndBom saveWorkOrderAndBom) {
        SmtWorkOrder smtWorkOrder = saveWorkOrderAndBom.getSmtWorkOrder();
        if(StringUtils.isNotEmpty(smtWorkOrder.getWorkOrderId())){
            if(this.update(smtWorkOrder)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }else{
            if(this.save(smtWorkOrder)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        if(smtWorkOrderBomService.save(saveWorkOrderAndBom.getSmtWorkOrderBomList())<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        return 1;
    }

    @Override
    public int updateWorkOrderStatus(Long workOrderId,int status) {
        SmtWorkOrder smtWorkOrder = new SmtWorkOrder();
        smtWorkOrder.setWorkOrderId(workOrderId);
        smtWorkOrder.setWorkOrderStatus(status);
        return smtWorkOrderMapper.updateByPrimaryKeySelective(smtWorkOrder);
    }

    @Override
    public int finishedProduct(Long workOrderId,Double count) {
        SmtWorkOrder smtWorkOrder = this.selectByKey(workOrderId);
        if(StringUtils.isEmpty(smtWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        double workOrderQuantity = smtWorkOrder.getWorkOrderQuantity().doubleValue();
        double outputQuantity = smtWorkOrder.getOutputQuantity().doubleValue();
        if((workOrderQuantity-outputQuantity)<count){
            throw new BizErrorException("完工数据大于剩余完工数据");
        }else if((workOrderQuantity-outputQuantity)==count){
            smtWorkOrder.setWorkOrderStatus(4);
        }
        smtWorkOrder.setOutputQuantity(new BigDecimal(outputQuantity+count));
        return this.update(smtWorkOrder);
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
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        SmtHtWorkOrder smtHtWorkOrder = new SmtHtWorkOrder();
        smtHtWorkOrder.setOption1(operation);
        SmtWorkOrder smtWorkOrder = selectByKey(id);
        if (StringUtils.isEmpty(smtWorkOrder)){
            return;
        }
        BeanUtils.copyProperties(smtWorkOrder, smtHtWorkOrder);
        smtHtWorkOrderMapper.insertSelective(smtHtWorkOrder);
    }


}

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
        if(StringUtils.isEmpty(mesPmWorkOrder.getRemark()) || !mesPmWorkOrder.getRemark().equals("无BOM")){
            //生成备料单
            SmtStock smtStock = new SmtStock();
            smtStock.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
            smtStock.setDeliveryMode(new Byte("0"));
            smtStock.setStatus(new Byte("0"));
            smtStock.setStockCode(CodeUtils.getId("BLD-"));
            // Date date = smtWorkOrder.getPlannedStartTime();
            // Date afterDate = new Date(date.getTime() + 600000);
            BeanUtils.copyProperties(mesPmWorkOrder, smtStock, new String[]{"createUserId", "createTime", "modifiedUserId", "modifiedTime"});
            smtStockMapper.insertUseGeneratedKeys(smtStock);

            genWorkOrder(mesPmWorkOrder, smtStock);
        }

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
        List<SmtWorkOrderBom> list = new ArrayList();

        //备料明细
        List<SmtHtWorkOrderBom> htList = new ArrayList<>();

        List<SmtStockDet> smtStockDetList = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        MesPmWorkOrder order = mesPmWorkOrderMapper.selectByPrimaryKey(mesPmWorkOrder.getWorkOrderId());

        Example stockExample = new Example(SmtStock.class);
        stockExample.createCriteria().andEqualTo("workOrderId", mesPmWorkOrder.getWorkOrderId());
        SmtStock smtStock = smtStockMapper.selectOneByExample(stockExample);

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

            //修改了工单的产品料号，对应的工单bom也要修改
            if (!order.getMaterialId().equals(mesPmWorkOrder.getMaterialId())) {
                //throw new BizErrorException("工单不能修改产品料号信息");
                Example example1 = new Example(SmtWorkOrderBom.class);
                example1.createCriteria().andEqualTo("workOrderId", mesPmWorkOrder.getWorkOrderId());
                smtWorkOrderBomMapper.deleteByExample(example1);

                //根据产品BOM生成工单BOM
                genWorkOrder(mesPmWorkOrder, smtStock);
            }
            //工单的工单数量改变,重新计算零件的工单用量
            if (!order.getWorkOrderQty().equals(mesPmWorkOrder.getWorkOrderQty()) &&
                    (workOrderStatus == 0 || workOrderStatus == 3)) {

                Example example1 = new Example(SmtWorkOrderBom.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("workOrderId", mesPmWorkOrder.getWorkOrderId());
                List<SmtWorkOrderBom> workOrderBoms = smtWorkOrderBomMapper.selectByExample(example1);

                List<SmtStockDet> smtStockDets=null;
                if(StringUtils.isNotEmpty(smtStock)){
                    Example example2 = new Example(SmtStockDet.class);
                    example2.createCriteria().andEqualTo("stockId", smtStock.getStockId());
                    smtStockDets= smtStockDetMapper.selectByExample(example2);
                }


                if (StringUtils.isNotEmpty(workOrderBoms)) {
                    for (SmtWorkOrderBom smtWorkOrderBom : workOrderBoms) {
                        //工单BOM的单个用量
                        BigDecimal singleQuantity = smtWorkOrderBom.getSingleQuantity();
                        BigDecimal baseQuantity = smtWorkOrderBom.getBaseQuantity();
                        //重新计算后的零件工单用量
                        if(StringUtils.isEmpty(baseQuantity))
                        {
                            smtWorkOrderBom.setQuantity(new BigDecimal(mesPmWorkOrder.getWorkOrderQty().toString()).multiply(singleQuantity));
                        }else{
                            smtWorkOrderBom.setQuantity(new BigDecimal(mesPmWorkOrder.getWorkOrderQty().toString()).multiply(singleQuantity).multiply(baseQuantity));
                        }
                        list.add(smtWorkOrderBom);

                        //新增工单BOM历史信息
                        SmtHtWorkOrderBom smtHtWorkOrderBom = new SmtHtWorkOrderBom();
                        BeanUtils.copyProperties(smtWorkOrderBom, smtHtWorkOrderBom);
                        smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                        smtHtWorkOrderBom.setModifiedTime(new Date());
                        htList.add(smtHtWorkOrderBom);

                        //修改备料数量
                        if(StringUtils.isNotEmpty(smtStockDets)){
                            List<SmtStockDet> smtStockDet = smtStockDets.stream().filter(st ->st.getMaterialId() != null && st.getMaterialId().equals(smtWorkOrderBom.getPartMaterialId())).collect(Collectors.toList());
                            if (StringUtils.isNotEmpty(smtStockDet)) {
                                for (SmtStockDet stockDet : smtStockDet) {
                                    stockDet.setStockId(smtStock.getStockId());
                                    stockDet.setMaterialId(smtWorkOrderBom.getPartMaterialId());
                                    stockDet.setPlanQuantity(smtWorkOrderBom.getQuantity());
                                    if(StringUtils.isNotEmpty(smtWorkOrderBom.getBaseQuantity())){
                                        stockDet.setStockQuantity(smtWorkOrderBom.getBaseQuantity());
                                    }
                                    smtStockDetList.add(stockDet);
                                }
                            }
                        }
                    }

                    //批量修改工单BOM的用量
                    smtWorkOrderBomMapper.updateBatch(list);

                    //批量新增工单BOM历史信息
                    if(StringUtils.isNotEmpty(htList))
                    smtHtWorkOrderBomMapper.insertList(htList);

                    //批量修改备料明细
                    if(StringUtils.isNotEmpty(smtStockDetList))
                    smtStockDetMapper.updateBatch(smtStockDetList);
                }
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
        for (MesPmWorkOrderDto smtWorkOrderDto : list) {
            if(StringUtils.isNotEmpty(smtWorkOrderDto.getParentId())){
                //部件工单，找到主工单
                SearchMesPmWorkOrder searchMesPmWorkOrder1 = new SearchMesPmWorkOrder();
                searchMesPmWorkOrder1.setWorkOrderId(smtWorkOrderDto.getParentId());
                List<MesPmWorkOrderDto> workOrderDtoList = this.findList(searchMesPmWorkOrder1);
                if(StringUtils.isNotEmpty(workOrderDtoList)){
                    MesPmWorkOrderDto smtWorkOrderDto1 = workOrderDtoList.get(0);
                    smtWorkOrderDto.setMaterialCode(smtWorkOrderDto1.getMaterialCode());
                    smtWorkOrderDto.setMaterialName(smtWorkOrderDto1.getMaterialName());
                    smtWorkOrderDto.setMaterialDesc(smtWorkOrderDto1.getMaterialDesc());
                    smtWorkOrderDto.setProductModelName(smtWorkOrderDto1.getProductModelName());
                    smtWorkOrderDto.setPackingUnitName(smtWorkOrderDto1.getPackingUnitName());
                    smtWorkOrderDto.setMainUnit(smtWorkOrderDto1.getMainUnit());
                    smtWorkOrderDto.setColor(smtWorkOrderDto1.getColor());
                    smtWorkOrderDto.setPackageSpecificationQuantity(smtWorkOrderDto1.getPackageSpecificationQuantity());
                }
            }
            Long routeId = smtWorkOrderDto.getRouteId();
            //查询工艺路线配置
            List<BaseRouteProcess> routeProcesses = mesPmWorkOrderMapper.selectRouteProcessByRouteId(routeId);
            if (StringUtils.isNotEmpty(routeProcesses)) {
                StringBuffer sb =new StringBuffer();
                for (BaseRouteProcess routeProcess : routeProcesses) {
                    sb.append(routeProcess.getProcessName()+"-");
                }
                smtWorkOrderDto.setProcessLink(sb.substring(0,sb.length()-1));
                //投入工序
                smtWorkOrderDto.setPutIntoProcessName(routeProcesses.get(0).getProcessName());
                //产出工序
                smtWorkOrderDto.setProductionProcessName(routeProcesses.get(routeProcesses.size() - 1).getProcessName());
            }
        }
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
            if(StringUtils.isEmpty(saveWorkOrderAndBom.getGenerate())){
                throw new BizErrorException("请求内容请带上是否生成工单流转卡字段");
            }
            if(saveWorkOrderAndBom.getGenerate()){
                SmtWorkOrderCardCollocation smtWorkOrderCardCollocation = new SmtWorkOrderCardCollocation();
                smtWorkOrderCardCollocation.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
                smtWorkOrderCardCollocation.setProduceQuantity(1);
                if(smtWorkOrderCardCollocationService.save(smtWorkOrderCardCollocation)<=0){
                    throw new BizErrorException("生成工单流程卡失败");
                }
            }
        }

        List<SmtWorkOrderBom> smtWorkOrderBomList = saveWorkOrderAndBom.getSmtWorkOrderBomList();
        //删除原本的BOM重新进行添加
        smtWorkOrderBomService.deleteByMap(ControllerUtil.dynamicCondition("workOrderId", mesPmWorkOrder.getWorkOrderId()));
        if(StringUtils.isNotEmpty(smtWorkOrderBomList)){
            for (SmtWorkOrderBom smtWorkOrderBom : smtWorkOrderBomList) {
                if(StringUtils.isEmpty(smtWorkOrderBom.getSingleQuantity())){
                    smtWorkOrderBom.setSingleQuantity(new BigDecimal(1));
                }
                smtWorkOrderBom.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
                if(smtWorkOrderBomService.save(smtWorkOrderBom)<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                }
            }
        }
        return 1;
    }

    @Override
    public int updateWorkOrderStatus(Long workOrderId,int status) {
        SysUser sysUser = this.currentUser();
        MesPmWorkOrder mesPmWorkOrder = new MesPmWorkOrder();
        mesPmWorkOrder.setWorkOrderId(workOrderId);
        mesPmWorkOrder.setWorkOrderStatus((byte)status);
        mesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
        mesPmWorkOrder.setModifiedTime(new Date());
        return mesPmWorkOrderMapper.updateByPrimaryKeySelective(mesPmWorkOrder);
    }

    @Override
    public int finishedProduct(Long workOrderId,Double count) {
        MesPmWorkOrder mesPmWorkOrder = this.selectByKey(workOrderId);
        if(StringUtils.isEmpty(mesPmWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        double workOrderQuantity = mesPmWorkOrder.getWorkOrderQty().doubleValue();
        double outputQuantity = mesPmWorkOrder.getOutputQty().doubleValue();
        if((workOrderQuantity-outputQuantity)<count){
            throw new BizErrorException("完工数据大于剩余完工数据");
        }else if((workOrderQuantity-outputQuantity)==count){
            mesPmWorkOrder.setWorkOrderStatus((byte)4);
        }
        mesPmWorkOrder.setOutputQty(new BigDecimal(outputQuantity+count));
        return this.update(mesPmWorkOrder);
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

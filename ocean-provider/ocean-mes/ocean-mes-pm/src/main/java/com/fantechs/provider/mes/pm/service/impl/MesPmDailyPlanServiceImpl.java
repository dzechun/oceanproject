package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDetDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanStockListDto;
import com.fantechs.common.base.general.dto.mes.pm.imports.MesPmDailyPlanImport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class MesPmDailyPlanServiceImpl extends BaseService<MesPmDailyPlan> implements MesPmDailyPlanService {

    @Resource
    private MesPmDailyPlanMapper mesPmDailyPlanMapper;
    @Resource
    private MesPmHtDailyPlanMapper mesPmHtDailyPlanMapper;
    @Resource
    private MesPmDailyPlanDetMapper mesPmDailyPlanDetMapper;
    @Resource
    private MesPmWorkOrderBomMapper mesPmWorkOrderBomMapper;
    @Resource
    private MesPmDailyPlanStockListMapper mesPmDailyPlanStockListMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<MesPmDailyPlanDto> findList(SearchMesPmDailyPlan searchMesPmDailyPlan) {
        if(StringUtils.isEmpty(searchMesPmDailyPlan.getOrgId())) {
            SysUser sysUser = currentUser();
            searchMesPmDailyPlan.setOrgId(sysUser.getOrganizationId());
        }

        if(StringUtils.isNotEmpty(searchMesPmDailyPlan.getPlanStartTime())) {
            try {
                searchMesPmDailyPlan.setStartTime(DateUtils.getStrToDateTime(searchMesPmDailyPlan.getPlanStartTime()));
                searchMesPmDailyPlan.setEndTime(DateUtils.getStrToDateTime(searchMesPmDailyPlan.getPlanStartTime()));
            } catch (ParseException e) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"时间格式转化错误");
            }
        }
        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    public List<MesPmDailyPlanDto> findDaysList(SearchMesPmDailyPlan searchMesPmDailyPlan) throws ParseException {

        SysUser user = currentUser();

        searchMesPmDailyPlan.setOrgId(user.getOrganizationId());
        //如未填写计划日期则取当天时间
        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isEmpty(searchMesPmDailyPlan.getPlanStartTime())) {
            searchMesPmDailyPlan.setStartTime(new Date());
        }else{
            searchMesPmDailyPlan.setStartTime(DateUtils.getStrToDateTime(searchMesPmDailyPlan.getPlanStartTime()));
        }

        calendar.setTime(searchMesPmDailyPlan.getStartTime());
        calendar.add(Calendar.DATE, +2);
        searchMesPmDailyPlan.setEndTime(calendar.getTime());
        searchMesPmDailyPlan.setStatus((byte)1);
        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesPmDailyPlanDto mesPmDailyPlanDto) {
        SysUser user = currentUser();
        int num = 1;
        String sysOrderTypeCode=mesPmDailyPlanDto.getSysOrderTypeCode();
        Byte sourceBigType=mesPmDailyPlanDto.getSourceBigType();

        if(StringUtils.isEmpty(sysOrderTypeCode)){
            sysOrderTypeCode="MES-DPO";//单据类型 生产日计划
            mesPmDailyPlanDto.setSysOrderTypeCode(sysOrderTypeCode);
        }
        if(StringUtils.isEmpty(sourceBigType)){
            sourceBigType=(byte)2;//来源类型 自建
            mesPmDailyPlanDto.setSourceBigType(sourceBigType);
        }
        if(StringUtils.isEmpty(mesPmDailyPlanDto.getDailyPlanCode())){
            //计划单号
            mesPmDailyPlanDto.setDailyPlanCode(CodeUtils.getId("PLAN-"));
        }
        mesPmDailyPlanDto.setCreateTime(new Date());
        mesPmDailyPlanDto.setCreateUserId(user.getUserId());
        mesPmDailyPlanDto.setModifiedTime(new Date());
        mesPmDailyPlanDto.setModifiedUserId(user.getUserId());
        mesPmDailyPlanDto.setOrgId(user.getOrganizationId());
        mesPmDailyPlanDto.setIsDelete((byte) 1);
        mesPmDailyPlanDto.setStatus((byte) 1);
        num=mesPmDailyPlanMapper.insertUseGeneratedKeys(mesPmDailyPlanDto);

        List<MesPmDailyPlanDetDto> mesPmDailyPlanDets=mesPmDailyPlanDto.getMesPmDailyPlanDetDtos();
        List<MesPmDailyPlanStockList> planStockLists=new ArrayList<>();
        if(mesPmDailyPlanDets.size()>0){
            for (MesPmDailyPlanDetDto mesPmDailyPlanDet : mesPmDailyPlanDets) {
                createDet(mesPmDailyPlanDet,mesPmDailyPlanDto.getDailyPlanId(),user);
            }
        }

        //履历
        MesPmHtDailyPlan mesPmHtDailyPlan = new MesPmHtDailyPlan();
        BeanUtils.copyProperties(mesPmDailyPlanDto, mesPmHtDailyPlan);
        mesPmHtDailyPlanMapper.insertSelective(mesPmHtDailyPlan);

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(MesPmDailyPlanDto mesPmDailyPlanDto){
        SysUser user = currentUser();
        int num=1;
        if(mesPmDailyPlanDto.getSourceBigType()!=(byte)2){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"不是自建的日计划不允许修改");
        }

        Example example1 = new Example(MesPmDailyPlanDet.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("dailyPlanId",mesPmDailyPlanDto.getDailyPlanId());
        List<MesPmDailyPlanDet> oldMesPmDailyPlanDets = mesPmDailyPlanDetMapper.selectByExample(example1);

        //原来有的明细只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<MesPmDailyPlanDetDto> mesPmDailyPlanDetDtos = mesPmDailyPlanDto.getMesPmDailyPlanDetDtos();
        if(StringUtils.isNotEmpty(mesPmDailyPlanDetDtos)) {
            for (MesPmDailyPlanDetDto mesPmDailyPlanDetDto : mesPmDailyPlanDetDtos) {
                if (StringUtils.isNotEmpty(mesPmDailyPlanDetDto.getDailyPlanDetId())) {
                    for (MesPmDailyPlanDet oldMesPmDailyPlanDet : oldMesPmDailyPlanDets){
                        if(mesPmDailyPlanDetDto.getDailyPlanDetId().equals(oldMesPmDailyPlanDet.getDailyPlanDetId())){
                            //排产数量只能改大 不能改小
                            if (mesPmDailyPlanDetDto.getScheduleQty().compareTo(oldMesPmDailyPlanDet.getScheduleQty()) == -1) {
                                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "生产日计划数不能改小");
                            }
                            mesPmDailyPlanDetDto.setModifiedUserId(user.getUserId());
                            mesPmDailyPlanDetDto.setModifiedTime(new Date());
                            mesPmDailyPlanDetMapper.updateByPrimaryKeySelective(mesPmDailyPlanDetDto);
                            idList.add(mesPmDailyPlanDetDto.getDailyPlanDetId());


                            Example exampleListDet = new Example(MesPmDailyPlanStockList.class);
                            Example.Criteria criteriaListDet = exampleListDet.createCriteria();
                            criteriaListDet.andEqualTo("dailyPlanDetId", mesPmDailyPlanDetDto.getDailyPlanDetId());
                            List<MesPmDailyPlanStockList> planStockLists=mesPmDailyPlanStockListMapper.selectByExample(exampleListDet);
                            for (MesPmDailyPlanStockList planStockList : planStockLists) {
                                planStockList.setDailyPlanUsageQty(mesPmDailyPlanDetDto.getScheduleQty().multiply(StringUtils.isEmpty(planStockList.getSingleQty())?new BigDecimal(1):planStockList.getSingleQty()));
                                planStockList.setModifiedUserId(user.getUserId());
                                planStockList.setModifiedTime(new Date());
                                num=mesPmDailyPlanStockListMapper.updateByPrimaryKeySelective(planStockList);
                            }

                        }
                    }
                }
            }
        }

        //删除原明细
        example1.clear();
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("dailyPlanId", mesPmDailyPlanDto.getDailyPlanId());
        if (idList.size() > 0) {
            criteria1.andNotIn("dailyPlanDetId", idList);
        }
        mesPmDailyPlanDetMapper.deleteByExample(example1);

        //明细
        if(StringUtils.isNotEmpty(mesPmDailyPlanDetDtos)){
            for (MesPmDailyPlanDetDto mesPmDailyPlanDetDto : mesPmDailyPlanDetDtos){
                if (idList.contains(mesPmDailyPlanDetDto.getDailyPlanDetId())) {
                    continue;
                }
                createDet(mesPmDailyPlanDetDto,mesPmDailyPlanDto.getDailyPlanId(),user);
            }
        }

        mesPmDailyPlanDto.setModifiedUserId(user.getUserId());
        mesPmDailyPlanDto.setModifiedTime(new Date());
        num=mesPmDailyPlanMapper.updateByPrimaryKeySelective(mesPmDailyPlanDto);

        //履历
        MesPmHtDailyPlan mesPmHtDailyPlan = new MesPmHtDailyPlan();
        BeanUtils.copyProperties(mesPmDailyPlanDto, mesPmHtDailyPlan);
        mesPmHtDailyPlanMapper.insertSelective(mesPmHtDailyPlan);

        return num;
    }

    private int createDet(MesPmDailyPlanDetDto mesPmDailyPlanDet,Long dailyPlanId,SysUser sysUser){
        int num=0;
        List<MesPmDailyPlanStockList> planStockLists=new ArrayList<>();

        MesPmWorkOrder mesPmWorkOrder=mesPmWorkOrderMapper.selectByPrimaryKey(mesPmDailyPlanDet.getWorkOrderId());
        if(StringUtils.isEmpty(mesPmWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的工单信息");
        }
        BigDecimal nowQty=new BigDecimal(0);
        BigDecimal scheduleQty=new BigDecimal(0);
        BigDecimal workOrderQty=new BigDecimal(0);
        if(StringUtils.isNotEmpty(mesPmWorkOrder.getScheduledQty())) {
            scheduleQty = mesPmWorkOrder.getScheduledQty();//工单已排产数量
        }else {
            mesPmWorkOrder.setScheduledQty(BigDecimal.ZERO);
        }

        if(StringUtils.isNotEmpty(mesPmWorkOrder.getWorkOrderQty()))
            workOrderQty=mesPmWorkOrder.getWorkOrderQty();//工单数量

        if(StringUtils.isNotEmpty(mesPmDailyPlanDet.getScheduleQty()))
            nowQty=mesPmDailyPlanDet.getScheduleQty();//本次排产数量

        if(nowQty.compareTo(new BigDecimal(0))!=1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"排产数量必须大于0");
        }
        if((nowQty.add(scheduleQty)).compareTo(workOrderQty)==1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"排产数量不能大于工单数量");
        }

        //新增明细
        mesPmDailyPlanDet.setDailyPlanId(dailyPlanId);
        mesPmDailyPlanDet.setCoreSourceOrderCode(mesPmWorkOrder.getWorkOrderCode());
        mesPmDailyPlanDet.setSourceOrderCode(mesPmWorkOrder.getSourceOrderCode());
        mesPmDailyPlanDet.setCreateUserId(sysUser.getUserId());
        mesPmDailyPlanDet.setCreateTime(new Date());
        mesPmDailyPlanDet.setIsDelete((byte)1);
        mesPmDailyPlanDet.setOrgId(sysUser.getOrganizationId());
        num+=mesPmDailyPlanDetMapper.insertUseGeneratedKeys(mesPmDailyPlanDet);

        //更新工单排产数量
        MesPmWorkOrder upMesPmWorkOrder=new MesPmWorkOrder();
        upMesPmWorkOrder.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
        upMesPmWorkOrder.setScheduledQty(mesPmWorkOrder.getScheduledQty().add(nowQty));
        upMesPmWorkOrder.setModifiedUserId(sysUser.getUserId());
        upMesPmWorkOrder.setModifiedTime(new Date());
        num+=mesPmWorkOrderMapper.updateByPrimaryKeySelective(upMesPmWorkOrder);

        //新增日计划物料明细
        Example exampleDet = new Example(MesPmWorkOrderBom.class);
        Example.Criteria criteriaDet = exampleDet.createCriteria();
        criteriaDet.andEqualTo("workOrderId", mesPmWorkOrder.getWorkOrderId());
        List<MesPmWorkOrderBom> workOrderBoms=mesPmWorkOrderBomMapper.selectByExample(exampleDet);
        if(workOrderBoms.size()>0){
            for (MesPmWorkOrderBom workOrderBom : workOrderBoms) {
                MesPmDailyPlanStockList mesPmDailyPlanStockList=new MesPmDailyPlanStockList();
                mesPmDailyPlanStockList.setDailyPlanDetId(mesPmDailyPlanDet.getDailyPlanDetId());
                mesPmDailyPlanStockList.setPartMaterialId(workOrderBom.getPartMaterialId());
                mesPmDailyPlanStockList.setProcessId(workOrderBom.getProcessId());
                mesPmDailyPlanStockList.setSingleQty(workOrderBom.getSingleQty());
                mesPmDailyPlanStockList.setDailyPlanUsageQty(nowQty.multiply(StringUtils.isEmpty(workOrderBom.getSingleQty())?new BigDecimal(1):workOrderBom.getSingleQty()));
                mesPmDailyPlanStockList.setCreateUserId(sysUser.getUserId());
                mesPmDailyPlanStockList.setCreateTime(new Date());
                mesPmDailyPlanStockList.setOrgId(sysUser.getOrganizationId());
                planStockLists.add(mesPmDailyPlanStockList);
            }
        }

        //新增日计划物料明细
        if(planStockLists.size()>0) {
            num+=mesPmDailyPlanStockListMapper.insertList(planStockLists);
        }

        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        int num=1;
        String[] arrId = ids.split(",");
        for (String item : arrId) {
            MesPmDailyPlan mesPmDailyPlan=mesPmDailyPlanMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(mesPmDailyPlan)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到要删除的生产日计划信息");
            }

            Example exampleDet = new Example(MesPmDailyPlanDet.class);
            Example.Criteria criteriaDet = exampleDet.createCriteria();
            criteriaDet.andEqualTo("dailyPlanId", mesPmDailyPlan.getDailyPlanId());
            List<MesPmDailyPlanDet> dailyPlanDets=mesPmDailyPlanDetMapper.selectByExample(exampleDet);
            for (MesPmDailyPlanDet dailyPlanDet : dailyPlanDets) {
                Example exampleListDet = new Example(MesPmDailyPlanStockList.class);
                Example.Criteria criteriaListDet = exampleListDet.createCriteria();
                criteriaListDet.andEqualTo("dailyPlanDetId", dailyPlanDet.getDailyPlanDetId());
                criteriaListDet.andIsNotNull("totalIssueQty");
                criteriaListDet.andGreaterThan("totalIssueQty",0);
                MesPmDailyPlanStockList pmDailyPlanStockList=mesPmDailyPlanStockListMapper.selectOneByExample(exampleListDet);
                if(StringUtils.isNotEmpty(pmDailyPlanStockList)){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"日计划备料明细已经下发 不允许删除");
                }

                //删备料明细
                Example exampleListDetDelete = new Example(MesPmDailyPlanStockList.class);
                Example.Criteria criteriaListDetDelete = exampleListDetDelete.createCriteria();
                criteriaListDetDelete.andEqualTo("dailyPlanDetId", dailyPlanDet.getDailyPlanDetId());

                num=mesPmDailyPlanStockListMapper.deleteByExample(exampleListDetDelete);

                //删日计划明细
                num=mesPmDailyPlanDetMapper.deleteByPrimaryKey(dailyPlanDet);
            }

            //删除日计划
            num=mesPmDailyPlanMapper.delete(mesPmDailyPlan);
        }
        return num;
    }

    /**
     * 下推备料计划
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pushDown(List<MesPmDailyPlanStockListDto> mesPmDailyPlanStockListDtos) {
        int num=1;
        SysUser user=currentUser();
        Map<Long, List<MesPmDailyPlanStockListDto>> collect = mesPmDailyPlanStockListDtos.stream().collect(Collectors.groupingBy(MesPmDailyPlanStockListDto::getWarehouseId));
        if(collect.size()>1){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"下推数据存在不同仓库 请选择同一仓库后再下推");
        }
        for (MesPmDailyPlanStockListDto PlanStockListDto : mesPmDailyPlanStockListDtos) {
            if (PlanStockListDto.getIfAllIssued() != null && PlanStockListDto.getIfAllIssued() == (byte) 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"生产日计划物料已下推完成，无法再次下推");
            }
            if(StringUtils.isEmpty(PlanStockListDto.getDailyPlanUsageQty()))
                PlanStockListDto.setDailyPlanUsageQty(BigDecimal.ZERO);
            if(StringUtils.isEmpty(PlanStockListDto.getTotalIssueQty()))
                PlanStockListDto.setTotalIssueQty(BigDecimal.ZERO);
            if(StringUtils.isEmpty(PlanStockListDto.getIssueQty()))
                PlanStockListDto.setIssueQty(BigDecimal.ZERO);

            if(PlanStockListDto.getDailyPlanUsageQty().compareTo(PlanStockListDto.getTotalIssueQty().add(PlanStockListDto.getIssueQty())) == -1 )
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "累计下发数量大于日计划使用数量");
        }

        Long warehouseId=mesPmDailyPlanStockListDtos.get(0).getWarehouseId();

        //核心单据类型编码
        String coreSourceSysOrderTypeCode=mesPmDailyPlanStockListDtos.get(0).getCoreSourceSysOrderTypeCode();
        if(StringUtils.isEmpty(coreSourceSysOrderTypeCode))
            coreSourceSysOrderTypeCode="MES-DPO";//为空 等于生产日计划

        WmsOutPlanStockListOrderDto stockListOrderDto=new WmsOutPlanStockListOrderDto();
        List<WmsOutPlanStockListOrderDetDto> stockListOrderDetDtos=new ArrayList<>();

        stockListOrderDto.setWarehouseId(warehouseId);
        stockListOrderDto.setCoreSourceSysOrderTypeCode(coreSourceSysOrderTypeCode);//核心单据编码
        stockListOrderDto.setSourceSysOrderTypeCode("MES-DPO");//来源单据编码 生产日计划
        stockListOrderDto.setSourceBigType((byte)1);//来源大类 下推
        stockListOrderDto.setOrderStatus((byte)1);//待执行

        for (MesPmDailyPlanStockListDto planStockListDto : mesPmDailyPlanStockListDtos) {
            if (planStockListDto.getIfAllIssued() != null && planStockListDto.getIfAllIssued() == (byte) 1) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"生产日计划物料已下推完成，无法再次下推");
            }

            WmsOutPlanStockListOrderDetDto stockListOrderDetDto=new WmsOutPlanStockListOrderDetDto();
            //核心单据编号 工单号
            stockListOrderDetDto.setCoreSourceOrderCode(planStockListDto.getWorkOrderCode());
            //工单ID
            stockListOrderDetDto.setWorkOrderId(planStockListDto.getWorkOrderId());
            stockListOrderDetDto.setWorkOrderCode(planStockListDto.getWorkOrderCode());
            //来源单据编号 生产日计划单号
            stockListOrderDetDto.setSourceOrderCode(planStockListDto.getDailyPlanCode());
            //核心单据ID 工单BOM明细ID
            stockListOrderDetDto.setCoreSourceId(planStockListDto.getWorkOrderBomId());
            //来源单据ID
            stockListOrderDetDto.setSourceId(planStockListDto.getDailyPlanStockListId());

            stockListOrderDetDto.setMaterialId(planStockListDto.getPartMaterialId());
            stockListOrderDetDto.setOrderQty(planStockListDto.getDailyPlanUsageQty());
            stockListOrderDetDto.setLineStatus((byte)1);
            stockListOrderDetDtos.add(stockListOrderDetDto);

            //更新明细
            MesPmDailyPlanStockList planStockList=new MesPmDailyPlanStockList();
            planStockList.setDailyPlanStockListId(planStockListDto.getDailyPlanStockListId());
            //planStockList.setTotalIssueQty(planStockListDto.getTotalIssueQty().add(planStockListDto.getIssueQty()));
//            if(planStockListDto.getDailyPlanUsageQty().compareTo(planStockListDto.getTotalIssueQty().add(planStockListDto.getWorkOrderQty()))==0)
//                planStockList.setIfAllIssued((byte)1);
            planStockList.setTotalIssueQty(planStockListDto.getDailyPlanUsageQty());
            planStockList.setIfAllIssued((byte)1);

            num=mesPmDailyPlanStockListMapper.updateByPrimaryKeySelective(planStockList);
            if(num<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        stockListOrderDto.setWmsOutPlanStockListOrderDetDtos(stockListOrderDetDtos);
        //下推备料计划
        ResponseEntity responseEntity = outFeignApi.add(stockListOrderDto);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }

        return num;
    }

    /**
     * 从Excel导入数据
     * @return
     */
    @Override
    public Map<String, Object> importExcel(List<MesPmDailyPlanImport> mesPmDailyPlanImportsTemp) throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        List<MesPmDailyPlanImport> mesPmDailyPlanImports=new ArrayList<>();
        for (MesPmDailyPlanImport mesPmDailyPlanImport : mesPmDailyPlanImportsTemp) {
            if(StringUtils.isNotEmpty(mesPmDailyPlanImport.getDailyPlanCode())){
                mesPmDailyPlanImports.add(mesPmDailyPlanImport);
            }
        }
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<MesPmDailyPlanImport> iterator = mesPmDailyPlanImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            MesPmDailyPlanImport mesPmDailyPlanImport = iterator.next();
            String proName = mesPmDailyPlanImport.getProName();
            String workOrderTypeName = mesPmDailyPlanImport.getWorkOrderTypeName();
            String workOrderCode=mesPmDailyPlanImport.getWorkOrderCode();
            String scheduleQty = mesPmDailyPlanImport.getScheduleQty();

            //判断必传字段
            if (StringUtils.isEmpty(
                    proName,workOrderTypeName,workOrderCode,scheduleQty
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断产线信息是否存在
            SearchBaseProLine searchBaseProLine=new SearchBaseProLine();
            searchBaseProLine.setProName(proName);
            ResponseEntity<List<BaseProLine>> baseProLineList=baseFeignApi.findList(searchBaseProLine);
            if(StringUtils.isNotEmpty(baseProLineList.getData())){
                BaseProLine baseProLine=baseProLineList.getData().get(0);
                if (StringUtils.isEmpty(baseProLine)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                mesPmDailyPlanImport.setProLineId(baseProLine.getProLineId());
                i++;
            }

            //工单类型 (0、量产 1、试产 2、返工 3、维修)
            if("量产".equals(workOrderTypeName))
                mesPmDailyPlanImport.setWorkOrderType((byte)0);
            else if("试产".equals(workOrderTypeName))
                mesPmDailyPlanImport.setWorkOrderType((byte)1);
            else if("返工".equals(workOrderTypeName))
                mesPmDailyPlanImport.setWorkOrderType((byte)2);
            else if("维修".equals(workOrderTypeName))
                mesPmDailyPlanImport.setWorkOrderType((byte)3);
            else
                mesPmDailyPlanImport.setWorkOrderType((byte)0);

            //工单是否存在
            Example example = new Example(MesPmWorkOrder.class);
            example.createCriteria().andEqualTo("workOrderCode",workOrderCode);
            MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(mesPmWorkOrder)){
                if (mesPmWorkOrder.getScheduledQty().add(new BigDecimal(scheduleQty)).compareTo(mesPmWorkOrder.getWorkOrderQty())==1){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                mesPmDailyPlanImport.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
                i++;
            }
        }

        //对合格数据进行分组
        Map<String, List<MesPmDailyPlanImport>> map = mesPmDailyPlanImports.stream().collect(Collectors.groupingBy(MesPmDailyPlanImport::getDailyPlanCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<MesPmDailyPlanImport> mesPmDailyPlanImport1 = map.get(code);

            //新增表头
            Long dailyPlanId=null;
            if(StringUtils.isEmpty(dailyPlanId)){
                MesPmDailyPlan mesPmDailyPlan=new MesPmDailyPlan();
                mesPmDailyPlan.setDailyPlanCode(code);
                mesPmDailyPlan.setSourceBigType((byte)2);
                mesPmDailyPlan.setProLineId(mesPmDailyPlanImport1.get(0).getProLineId());
                mesPmDailyPlan.setWorkOrderType(mesPmDailyPlanImport1.get(0).getWorkOrderType());
                mesPmDailyPlan.setPlanStartTime(mesPmDailyPlanImport1.get(0).getPlanStartTime());
                mesPmDailyPlan.setRemark(mesPmDailyPlanImport1.get(0).getRemark());
                mesPmDailyPlan.setStatus((byte)1);
                mesPmDailyPlan.setOrgId(currentUser.getOrganizationId());
                mesPmDailyPlan.setCreateUserId(currentUser.getUserId());
                mesPmDailyPlan.setCreateTime(new Date());
                mesPmDailyPlan.setModifiedUserId(currentUser.getUserId());
                mesPmDailyPlan.setModifiedTime(new Date());
                mesPmDailyPlanMapper.insertUseGeneratedKeys(mesPmDailyPlan);
                dailyPlanId=mesPmDailyPlan.getDailyPlanId();
            }
            //新增明细 mesPmDailyPlanDet
            if(StringUtils.isNotEmpty(dailyPlanId)) {
                List<MesPmDailyPlanDet> mesPmDailyPlanDetList=new ArrayList<>();
                for (MesPmDailyPlanImport item : mesPmDailyPlanImport1) {
                    MesPmDailyPlanDet mesPmDailyPlanDetNew=new MesPmDailyPlanDet();
                    mesPmDailyPlanDetNew.setDailyPlanId(dailyPlanId);
                    mesPmDailyPlanDetNew.setWorkOrderId(item.getWorkOrderId());
                    mesPmDailyPlanDetNew.setScheduleQty(new BigDecimal(item.getScheduleQty()));
                    mesPmDailyPlanDetNew.setOrgId(currentUser.getOrganizationId());
                    mesPmDailyPlanDetNew.setCreateUserId(currentUser.getUserId());
                    mesPmDailyPlanDetNew.setCreateTime(new Date());
                    mesPmDailyPlanDetList.add(mesPmDailyPlanDetNew);
                }

                if(mesPmDailyPlanDetList.size()>0){
                    success += mesPmDailyPlanDetMapper.insertList(mesPmDailyPlanDetList);
                }
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}

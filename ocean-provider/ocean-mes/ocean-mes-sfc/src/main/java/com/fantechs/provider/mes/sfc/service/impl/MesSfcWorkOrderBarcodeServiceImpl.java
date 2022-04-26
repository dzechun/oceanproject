package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.basic.BatchGenerateCodeDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcodeReprint;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeReprintService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mr.Lei on 2021/04/07.
 */
@Service
@Slf4j
public class MesSfcWorkOrderBarcodeServiceImpl extends BaseService<MesSfcWorkOrderBarcode> implements MesSfcWorkOrderBarcodeService {

    @Resource
    private MesSfcWorkOrderBarcodeMapper mesSfcWorkOrderBarcodeMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private MesSfcWorkOrderBarcodeReprintService mesSfcWorkOrderBarcodeReprintService;

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode) {
        if (StringUtils.isEmpty(searchMesSfcWorkOrderBarcode.getOrgId())) {
            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            searchMesSfcWorkOrderBarcode.setOrgId(sysUser.getOrganizationId());
        }
        return mesSfcWorkOrderBarcodeMapper.findList(searchMesSfcWorkOrderBarcode);
    }

    /**
     * 打印/补打条码
     *
     * @param ids       条码唯一标识
     * @param printType 打印类型
     * @return 1
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(String ids, Byte printType, String printName, String userCode, String password,String printId) {
        if (printType == 2) {
            //获取程序配置项
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("ReprintCode");
            List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            String isRoot = itemList.get(0).getParaValue().trim();
            if (isRoot.equals("true")) {
                String hashPass = mesSfcWorkOrderBarcodeMapper.findSysUser(userCode);
                if (StringUtils.isEmpty(hashPass)) {
                    throw new BizErrorException("用户无权限，请联系管理员");
                }
                BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
                boolean flag = bcryptPasswordEncoder.matches(password, hashPass);
                if (!flag) {
                    throw new BizErrorException("密码错误");
                }
            }
        }
        if (StringUtils.isEmpty(ids, printName)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "参数传递错误");
        }
        if (StringUtils.isEmpty(printName)) {
            throw new BizErrorException("请设置打印机名称");
        }
        String[] arrId = ids.split(",");
        Map<Long, PrintDto> map = new HashMap<>();
        for (String s : arrId) {
            //查询模版信息
            MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeMapper.selectByPrimaryKey(s);
            LabelRuteDto labelRuteDto = null;
            Byte barcodeType;
            if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 56) {//获取工单类别模版
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01", mesSfcWorkOrderBarcode.getWorkOrderId());
                if (StringUtils.isEmpty(labelRuteDto) && StringUtils.isEmpty(labelRuteDto.getLabelName())) {
                    //获取默认模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("01");
                }
                mesSfcWorkOrderBarcode.setBarcodeType((byte) 2);
            } else if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 57 && "4".equals(mesSfcWorkOrderBarcode.getOption1())) {//获取销售类别模版
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02", mesSfcWorkOrderBarcode.getWorkOrderId());
                if (StringUtils.isEmpty(labelRuteDto) || StringUtils.isEmpty(labelRuteDto.getLabelName())) {
                    //获取默认模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("02");
                    if (StringUtils.isEmpty(labelRuteDto)) {
                        throw new BizErrorException("未匹配到默认模版");
                    }
                }
                mesSfcWorkOrderBarcode.setBarcodeType((byte) 4);
            } else if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 57 && "5".equals(mesSfcWorkOrderBarcode.getOption1())) {
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findOmRule(mesSfcWorkOrderBarcode.getWorkOrderId());
                mesSfcWorkOrderBarcode.setBarcodeType((byte) 1);
            }
            //PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(mesSfcWorkOrderBarcode.getLabelCategoryId(), mesSfcWorkOrderBarcode.getWorkOrderId());

            //查询是否有模版视图
            String labelView = mesSfcWorkOrderBarcodeMapper.findLabelView(labelRuteDto.getLabelCode());
            if (StringUtils.isEmpty(labelView)) {
                throw new BizErrorException("未查询到模版视图");
            }

            PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(ControllerUtil.dynamicCondition("labelCode", labelRuteDto.getLabelCode(), "id", mesSfcWorkOrderBarcode.getWorkOrderBarcodeId()));
            if (StringUtils.isEmpty(printModel)) {
                throw new BizErrorException("获取视图数据信息失败，请检查视图");
            }
            if (StringUtils.isEmpty(labelRuteDto.getSize())) {
                labelRuteDto.setSize(1);
            }
            printModel.setSize(labelRuteDto.getSize());
            if (StringUtils.isEmpty(labelRuteDto)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "获取标签信息失败");
            }
            if (printType == 1) {
                mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 0);
                mesSfcWorkOrderBarcode.setPrintTime(new Date());
                this.update(mesSfcWorkOrderBarcode);
            } else if (printType == 2) {
                SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
                MesSfcWorkOrderBarcodeReprint reprint = new MesSfcWorkOrderBarcodeReprint();
                reprint.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                reprint.setBarcodeCode(mesSfcWorkOrderBarcode.getBarcode());
                reprint.setBarcodeType(mesSfcWorkOrderBarcode.getBarcodeType());
                reprint.setCreateTime(new Date());
                reprint.setCreateUserId(sysUser.getUserId());
                reprint.setModifiedTime(new Date());
                reprint.setModifiedUserId(sysUser.getUserId());
                reprint.setReprintTime(new Date());
                reprint.setReprintUserId(sysUser.getUserId());
                reprint.setOrgId(sysUser.getOrganizationId());
                reprint.setIsDelete((byte) 1);
                mesSfcWorkOrderBarcodeReprintService.save(reprint);

                if (mesSfcWorkOrderBarcode.getBarcodeStatus().equals((byte) 3)){
                    mesSfcWorkOrderBarcode.setBarcodeStatus((byte) 0);
                    mesSfcWorkOrderBarcode.setPrintTime(new Date());
                    this.update(mesSfcWorkOrderBarcode);
                }
            }
            printModel.setQrCode(mesSfcWorkOrderBarcode.getBarcode());

            if (map.containsKey(mesSfcWorkOrderBarcode.getWorkOrderId())) {
                PrintDto printDto = map.get(mesSfcWorkOrderBarcode.getWorkOrderId());
                List<PrintModel> printModelList = printDto.getPrintModelList();
                printModelList.add(printModel);
                map.put(mesSfcWorkOrderBarcode.getWorkOrderId(), printDto);
            } else {
                PrintDto printDto = new PrintDto();
                printDto.setLabelName(labelRuteDto.getLabelName() + ".btw");
                printDto.setLabelVersion(labelRuteDto.getLabelVersion());
                printDto.setPrintName(printName);
                List<PrintModel> printModelList = new ArrayList<>();
                printModelList.add(printModel);
                printDto.setPrintModelList(printModelList);
                map.put(mesSfcWorkOrderBarcode.getWorkOrderId(), printDto);
            }

        }
        for (Map.Entry<Long, PrintDto> m : map.entrySet()) {
            rabbitProducer.sendPrint(m.getValue(),printId);
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print_rewrite(String ids, Byte printType, String printName, String userCode, String password, String printId) {
        // 1、补打校验是否有权限
        if (printType == 2) {
            //获取程序配置项
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("ReprintCode");
            List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            String isRoot = itemList.get(0).getParaValue().trim();
            if (isRoot.equals("true")) {
                String hashPass = mesSfcWorkOrderBarcodeMapper.findSysUser(userCode);
                if (StringUtils.isEmpty(hashPass)) {
                    throw new BizErrorException("用户无权限，请联系管理员");
                }
                BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
                boolean flag = bcryptPasswordEncoder.matches(password, hashPass);
                if (!flag) {
                    throw new BizErrorException("密码错误");
                }
            }
        }
        if (StringUtils.isEmpty(ids, printName)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "参数传递错误");
        }
        if (StringUtils.isEmpty(printName)) {
            throw new BizErrorException("请设置打印机名称");
        }
        String[] arrId = ids.split(",");
        if (arrId.length <= 0){
            throw new BizErrorException("未选中条码或条码未产生");
        }
        // 2、查询当前打印任务所有条码
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        example.createCriteria().andIn("workOrderBarcodeId", Arrays.asList(arrId));
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = mesSfcWorkOrderBarcodeMapper.selectByExample(example);
        if (workOrderBarcodes.isEmpty()){
            throw new BizErrorException("条码不存在系统或已被删除");
        }

        // 3、查询模版信息
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = workOrderBarcodes.get(0);
        LabelRuteDto labelRuteDto = null;
        Byte barcodeType = null;
        if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 56) {
            // 获取工单类别模版
            labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01", mesSfcWorkOrderBarcode.getWorkOrderId());
            barcodeType = (byte) 2;
        } else if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 57 && "4".equals(mesSfcWorkOrderBarcode.getOption1())) {
            // 获取销售类别模版
            labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02", mesSfcWorkOrderBarcode.getWorkOrderId());
            barcodeType = (byte) 4;
        } else if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 57 && "5".equals(mesSfcWorkOrderBarcode.getOption1())) {
            labelRuteDto = mesSfcWorkOrderBarcodeMapper.findOmRule(mesSfcWorkOrderBarcode.getWorkOrderId());
            barcodeType = (byte) 1;
        }
        if (StringUtils.isEmpty(labelRuteDto)){
            // 获取默认模版
            if (barcodeType == (byte) 2) {
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("01");
            }else if (barcodeType == (byte) 4) {
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("02");
            }
        }
        if (StringUtils.isEmpty(labelRuteDto)) {
            throw new BizErrorException("未匹配到模版");
        }

        // 4、查询是否有模版视图
        String labelView = mesSfcWorkOrderBarcodeMapper.findLabelView(labelRuteDto.getLabelCode());
        if (StringUtils.isEmpty(labelView)) {
            throw new BizErrorException("未查询到模版视图");
        }

        PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(ControllerUtil.dynamicCondition("labelCode", labelRuteDto.getLabelCode(), "id", mesSfcWorkOrderBarcode.getWorkOrderBarcodeId()));
        if (StringUtils.isEmpty(printModel)) {
            throw new BizErrorException("获取视图数据信息失败，请检查视图");
        }
        if (StringUtils.isEmpty(labelRuteDto.getSize())) {
            labelRuteDto.setSize(1);
        }
        printModel.setSize(labelRuteDto.getSize());
        if (StringUtils.isEmpty(labelRuteDto)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "获取标签信息失败");
        }

        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        // 5、循环条码
        List<PrintModel> printModelList = new ArrayList<>();
        List<MesSfcWorkOrderBarcodeReprint> reprintAddList = new ArrayList<>();
        List<MesSfcWorkOrderBarcode> updateList = new ArrayList<>();
        for (MesSfcWorkOrderBarcode barcode : workOrderBarcodes){

            if (printType == 1) {
                barcode.setBarcodeStatus((byte) 0);
            } else if (printType == 2) {
                MesSfcWorkOrderBarcodeReprint reprint = new MesSfcWorkOrderBarcodeReprint();
                reprint.setWorkOrderBarcodeId(barcode.getWorkOrderBarcodeId());
                reprint.setBarcodeCode(barcode.getBarcode());
                reprint.setBarcodeType(barcodeType);
                reprint.setCreateTime(new Date());
                reprint.setCreateUserId(sysUser.getUserId());
                reprint.setModifiedTime(new Date());
                reprint.setModifiedUserId(sysUser.getUserId());
                reprint.setReprintTime(new Date());
                reprint.setReprintUserId(sysUser.getUserId());
                reprint.setOrgId(sysUser.getOrganizationId());
                reprint.setIsDelete((byte) 1);
                reprintAddList.add(reprint);

                if (barcode.getBarcodeStatus().equals((byte) 3)){
                    barcode.setBarcodeStatus((byte) 0);
                }
            }
            barcode.setPrintTime(new Date());
            updateList.add(barcode);

            // 打印条码集合
            PrintModel res = new PrintModel();
            BeanUtil.copyProperties(printModel, res);
            res.setQrCode(barcode.getBarcode());
            printModelList.add(res);
        }
        // 批量新增补打条码
        if (printType == 2 && !reprintAddList.isEmpty()){
            mesSfcWorkOrderBarcodeReprintService.batchSave(reprintAddList);
        }

        // 批量修改条码状态
        if (!updateList.isEmpty()){
            this.batchUpdate(updateList);
        }

        // 6、构造打印条码结构
        if (!printModelList.isEmpty()){
            List<PrintModel> list = new ArrayList<>();
            // 万宝项目 打印条码只有一式一联，一式两联，一式四联
            if (labelRuteDto.getSize() == 1) {
                // 两个条码一组
                List<List<PrintModel>> fixedGrouping = fixedGrouping(printModelList, 2);
                for (List<PrintModel> item : fixedGrouping){
                    PrintModel source = item.get(0);
                    if (item.size() == 2){
                        source = buildPrintModel(source, item.get(1));
                    }else {
                        source = buildPrintModel(source, null);
                    }
                    list.add(source);
                }
            }else if (labelRuteDto.getSize() == 2 || labelRuteDto.getSize() == 4) {
                for (PrintModel source : printModelList){
                    PrintModel target = new PrintModel();
                    BeanUtil.copyProperties(source, target);
                    source = buildPrintModel(source, target);
                    list.add(source);
                }
            }

            // 构造数据并往mq塞数据
            PrintDto printDto = new PrintDto();
            printDto.setLabelName(labelRuteDto.getLabelName() + ".btw");
            printDto.setLabelVersion(labelRuteDto.getLabelVersion());
            printDto.setPrintName(printName);
            printDto.setPrintModelList(list);
            log.info("================== PrintDto : " + JSON.toJSONString(printDto));
            rabbitProducer.sendPrint(printDto,printId);
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int printByOrderCode(Long id, Byte barcodeType, String printName, String userCode, String password,String printId) {
        int i=0;
        StringBuilder sb=new StringBuilder();
        Example example=new Example(MesSfcWorkOrderBarcode.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("workOrderId",id);
        criteria.andEqualTo("option1",barcodeType);
        List<MesSfcWorkOrderBarcode> workOrderBarcodeList=mesSfcWorkOrderBarcodeMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(workOrderBarcodeList) && workOrderBarcodeList.size()>0){
            for (MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode : workOrderBarcodeList) {
                if(sb.toString().length()==0) {
                    sb.append(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId().toString());
                }
                else {
                    sb.append(","+mesSfcWorkOrderBarcode.getWorkOrderBarcodeId().toString());
                }
            }

            i=this.print_rewrite(sb.toString(),(byte)2,printName,userCode,password,printId);
        }
        else{
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"未产生条码 无需补打");
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSyncBarcode(BatchSyncBarcodeDto dto) {
        log.info("======= 新增集合: " + JSON.toJSONString(dto.getList()));
        log.info("======= 修改集合: " + JSON.toJSONString(dto.getUpdateList()));
        // 1、批量新增部分
        if (StringUtils.isNotEmpty(dto.getList())){
            List<MesSfcBarcodeProcess> list = new ArrayList<>();
            for (BatchSyncBarcodeSaveDto entity : dto.getList()){
                // 保存条码表并返回ID
                mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(entity.getWorkOrderBarcode());

                MesSfcBarcodeProcess barcodeProcess = entity.getBarcodeProcess();
                barcodeProcess.setWorkOrderBarcodeId(entity.getWorkOrderBarcode().getWorkOrderBarcodeId());
                list.add(barcodeProcess);
            }

            // 批量保存条码流程表
            mesSfcBarcodeProcessMapper.insertList(list);
        }

        // 2、批量修改部分
        if (StringUtils.isNotEmpty(dto.getUpdateList())){
            mesSfcBarcodeProcessMapper.batchUpdateCustomerBarcode(dto.getUpdateList());
        }
        return 1;
    }

    @Override
    public SyncFindBarcodeDto syncFindBarcode(Long labelCategoryId) {
        long start = System.currentTimeMillis();
        List<SyncWorkOrderBarcodeDto> workOrderBarcodes = mesSfcWorkOrderBarcodeMapper.selectPartField(labelCategoryId);
        long start1 = System.currentTimeMillis();
        List<SyncBarcodeProcessDto> sfcBarcodeProcesses = mesSfcBarcodeProcessMapper.findByLabelCategory(labelCategoryId);
        log.info("======== 查询条码表：" + (start1 - start));
        log.info("======== 查询条码过站表：" + (System.currentTimeMillis() - start1));
        SyncFindBarcodeDto dto = new SyncFindBarcodeDto();
        dto.setWorkOrderBarcodes(workOrderBarcodes);
        dto.setBarcodeProcesses(sfcBarcodeProcesses);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, Object> importExcel(List<ImportCustomerBarcodeDto> list) {
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        HashMap<String, List<ImportCustomerBarcodeDto>> map = list.stream().collect(Collectors.groupingBy(ImportCustomerBarcodeDto::getSalesCode, HashMap::new, Collectors.toList()));
        List<OmSalesOrderDetDto> orderDetDtos = new ArrayList<>();
        map.forEach((key, value) -> {
            SearchOmSalesOrderDetDto detDto = new SearchOmSalesOrderDetDto();
            detDto.setSalesCode(value.get(0).getSalesCode());
            List<OmSalesOrderDetDto> salesOrderDetDtoList = omFeignApi.findList(detDto).getData();
            if (salesOrderDetDtoList.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "销售编码:" + value.get(0).getSalesCode() + "在系统中不存在，不可操作");
            }
            if (value.size() > 1){
                Example example = new Example(MesSfcWorkOrderBarcode.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("option3", salesOrderDetDtoList.get(0).getSalesOrderDetId())
                        .andEqualTo("workOrderId", salesOrderDetDtoList.get(0).getSalesOrderDetId());
                int countByExample = this.selectCountByExample(example);
                if (countByExample>0) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(), "销售编码:" + value.get(0).getSalesCode() + "下的客户条码还未删除，不能保存");
                }
            }
            orderDetDtos.addAll(salesOrderDetDtoList);
        });
        for (int i = 0; i < list.size(); i++){
            ImportCustomerBarcodeDto dto = list.get(i);
            // 计算固定值
            String fixedValue = this.longestCommonPrefix(new String[]{dto.getStartCode(), dto.getEndCode()});
            // 获取销售订单明细
            Long salesOrderDetId = null;
            for (OmSalesOrderDetDto item : orderDetDtos){
                if (item.getSalesCode().equals(dto.getSalesCode())){
                    salesOrderDetId = item.getSalesOrderDetId();
                    break;
                }
            }
            if (salesOrderDetId == null){
                fail.add(i + 4);
                continue;
            }

            // 生成条码
            String initialValue = dto.getStartCode().substring(fixedValue.length());
            String finalValue = dto.getEndCode().substring(fixedValue.length());
            this.wanbaoAddCustomerBarcode(salesOrderDetId, fixedValue, initialValue, finalValue, true);
            success += 1;
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行数", fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(MesSfcWorkOrderBarcode entity) {
        if (entity.getOrgId() == 1000L) {
            entity.setModifiedTime(new Date());
        } else {
            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            entity.setModifiedUserId(sysUser.getUserId());
            entity.setModifiedTime(new Date());
            entity.setOrgId(sysUser.getOrganizationId());
        }
        return mesSfcWorkOrderBarcodeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LabelRuteDto findLabelRute(Long workOrderId, Byte barcodeType) {
        LabelRuteDto labelRuteDto = null;
        Long materialId = null;
        if (barcodeType == 5) {
            //查询销售订单明细
            SearchOmSalesOrderDetDto salesOrderDetDto = new SearchOmSalesOrderDetDto();
            salesOrderDetDto.setSalesOrderDetId(workOrderId);
            OmSalesOrderDetDto omSalesOrderDetDto = omFeignApi.findList(salesOrderDetDto).getData().get(0);
            materialId = omSalesOrderDetDto.getMaterialId();
        } else {
            //查询工单是否绑定条码规则
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(workOrderId);
            MesPmWorkOrderDto mesPmWorkOrder = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);
            if (StringUtils.isEmpty(mesPmWorkOrder.getBarcodeRuleSetId())) {
                throw new BizErrorException("工单未绑定条码规则");
            }
            materialId = mesPmWorkOrder.getMaterialId();
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new BizErrorException("获取物料信息失败");
        }
        //查询物料是否绑定标签模版
        SearchBaseLabelMaterial searchBaseLabelMaterial = new SearchBaseLabelMaterial();
        searchBaseLabelMaterial.setMaterialId(materialId.toString());
        if (barcodeType == 2) {
            searchBaseLabelMaterial.setLabelCategoryCode("01");
        } else if (barcodeType == 4) {
            searchBaseLabelMaterial.setLabelCategoryCode("02");
        }
        List<BaseLabelMaterialDto> baseLabelMaterialDtos = baseFeignApi.findLabelMaterialList(searchBaseLabelMaterial).getData();
        if (StringUtils.isEmpty(baseLabelMaterialDtos) || baseLabelMaterialDtos.size() < 1) {
            throw new BizErrorException("物料未绑定标签模版");
        }
        switch (barcodeType) {
            case 2:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01", workOrderId);
                break;
            case 4:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02", workOrderId);
                break;
            case 5:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findOmRule(workOrderId);
                break;
            default:
                break;
        }
        if (StringUtils.isEmpty(labelRuteDto) || StringUtils.isEmpty(labelRuteDto.getBarcodeRuleId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), barcodeType == 2 ? "匹配工单条码规则失败" : "匹配到销售订单条码规则失败");
        }
        return labelRuteDto;
    }

    /**
     * 打印客户端标签版本校验下载
     *
     * @param labelName 标签名称
     * @param request
     * @param response
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void checkOutLabel(String labelName, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(labelName)) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "参数错误");
        }
        //String fileName =labelName.substring(0,labelName.indexOf("."));
        String baseLabelCategory = mesSfcWorkOrderBarcodeMapper.findByOneLabel(labelName);
        if (StringUtils.isEmpty(baseLabelCategory)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "获取标签信息失败");
        }
        //查询版本号
        String version = mesSfcWorkOrderBarcodeMapper.findVersion(labelName);
        if (StringUtils.isEmpty(version)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "标签版本获取失败");
        }
        String userAgent = request.getHeader("User-Agent");
        labelName = labelName + ".btw";
        Path file = Paths.get("/label/" + baseLabelCategory + "/" + version + "/" + labelName);
        if (Files.exists(file)) {
            response.setContentType("application/vnd.android.package-archive;charset=utf-8");
            try {
                if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                    labelName = java.net.URLEncoder.encode(labelName, "UTF-8");
                } else {
                    labelName = new String(labelName.getBytes("UTF-8"), "ISO-8859-1");
                }
                response.addHeader("Content-Disposition",
                        "attachment; filename=" + labelName);

                System.out.println("以输出流的形式对外输出提供下载");
                Files.copy(file, response.getOutputStream());// 以输出流的形式对外输出提供下载
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new BizErrorException("文件不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<MesSfcWorkOrderBarcode> add(MesSfcWorkOrderBarcode record) {
        long startTime = System.currentTimeMillis();
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(record.getBarcodeType()==(byte)4){
//            MesPmWorkOrder mesPmWorkOrder = pmFeignApi.workOrderDetail(record.getWorkOrderId()).getData();
//            record.setWorkOrderId(mesPmWorkOrder.getSalesOrderId());
//        }
        switch (record.getBarcodeType()) {
            case 2:
                if (StringUtils.isEmpty(record.getWorkOrderId())) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "绑定单据唯一码不能为空");
                }
                record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"));
                record.setOption1(record.getBarcodeType().toString());
                break;
            case 4:
                if (StringUtils.isEmpty(record.getWorkOrderId())) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "绑定单据唯一码不能为空");
                }
                record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("销售订单条码"));
                record.setOption1(record.getBarcodeType().toString());
                break;
            case 5:
                if (StringUtils.isEmpty(record.getSalesOrderDetId())) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "绑定单据唯一码不能为空");
                }
                record.setLabelCategoryId(Long.parseLong("57"));
                record.setWorkOrderId(record.getSalesOrderDetId());
                record.setWorkOrderQty(StringUtils.isEmpty(record.getOrderQty()) ? BigDecimal.ZERO : record.getOrderQty());
                record.setWorkOrderCode(record.getSalesOrderCode());
                record.setOption1(record.getBarcodeType().toString());
                break;
        }
        //判断条码产生数量不能大于工单数量
        Integer count = mesSfcWorkOrderBarcodeMapper.findCountCode(record.getLabelCategoryId(), record.getWorkOrderId());
        logger.info("工单相关查询 执行总时长 : {}毫秒)",(System.currentTimeMillis() - startTime));

        if (count + record.getQty() > record.getWorkOrderQty().doubleValue()) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012009);
        }
        List<MesSfcWorkOrderBarcode> mesSfcWorkOrderBarcodeList = new ArrayList<>();

        //查询条码规则集合
        LabelRuteDto labelRuteDto = record.getLabelRuteDto();
        SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        searchBaseBarcodeRuleSpec.setBarcodeRuleId(labelRuteDto.getBarcodeRuleId());

        long findSpecTime = System.currentTimeMillis();
        ResponseEntity<List<BaseBarcodeRuleSpec>> responseEntity = baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
        logger.info("查询条码规则配置 执行总时长 : {}毫秒)",(System.currentTimeMillis() - findSpecTime));

        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(responseEntity.getMessage());
        }
        List<BaseBarcodeRuleSpec> list = responseEntity.getData();
        if (list.size() < 1) {
            throw new BizErrorException("请设置条码规则");
        }

        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
        searchBaseBarcodeRule.setBarcodeRuleId(list.get(0).getBarcodeRuleId());
        List<BaseBarcodeRuleDto> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
        if (StringUtils.isEmpty(barcodeRulList)) throw new BizErrorException("未查询到编码规则");

        MesPmWorkOrderDto mesPmWorkOrderDto = new MesPmWorkOrderDto();
        BaseRouteProcess routeProcess = new BaseRouteProcess();
        if (labelRuteDto.getBarcodeType() == (byte) 1) {
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(record.getWorkOrderId());
            mesPmWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);

            if (StringUtils.isEmpty(mesPmWorkOrderDto.getLogicId())) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "工单未选择ERP逻辑仓库");
            }
            if (StringUtils.isEmpty(mesPmWorkOrderDto.getRouteId())) {
                throw new BizErrorException("工单未选择工艺路线");
            }

            //查询工艺路线
            ResponseEntity<List<BaseRouteProcess>> res = baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
            if (res.getCode() != 0) {
                throw new BizErrorException(res.getCode(), res.getMessage());
            }
            if (res.getData().isEmpty()) {
                throw new BizErrorException("请检查工单工艺路线");
            }
            routeProcess = res.getData().get(0);
        }

        //boolean hasKey = redisUtil.hasKey(barcodeRulList.get(0).getBarcodeRule());
        // 不同组织使用同一个编码规则可能产生相同的条码 key值在原有基础上加组织ID作为键值 huangshuijun 2021-10-08
        String orgIDStr = sysUser.getOrganizationId().toString();

        String key = barcodeRulList.get(0).getBarcodeRule() + orgIDStr + ":" + record.getMaterialCode();
        //万宝销售条码打印 按销售订单更新流水号
        if (StringUtils.isNotEmpty(record.getOption1()) && record.getOption1().equals("5")) {
            key = barcodeRulList.get(0).getBarcodeRule() + orgIDStr + ":" + record.getSalesOrderDetId().toString();
        }
        //生成条码
        BatchGenerateCodeDto dto = new BatchGenerateCodeDto();
        dto.setList(list);
        dto.setQty(record.getQty());
        dto.setKey(key);
        dto.setCode(record.getMaterialCode());
        dto.setParams(record.getWorkOrderId().toString());

        //传入工单计划生产日期
        if(StringUtils.isNotEmpty(mesPmWorkOrderDto.getPlanStartTime())){
            String planDate= DateUtil.format(mesPmWorkOrderDto.getPlanStartTime(), DatePattern.NORM_DATE_PATTERN);
            String planYear=planDate.substring(0,4);
            String planMonth=new Integer(planDate.substring(5,7)).toString();
            String planDay=new Integer(planDate.substring(8,10)).toString();
            dto.setPlanYear(planYear);
            dto.setPlanMonth(planMonth);
            dto.setPlanDay(planDay);
        }

        long selectTime = System.currentTimeMillis();
        ResponseEntity<List<String>> rs = baseFeignApi.batchGenerateCode(dto);
        if (rs.getCode() != 0) {
            throw new BizErrorException(rs.getMessage());
        }
        logger.info("批量生成条码 执行总时长 : {}毫秒)",(System.currentTimeMillis() - selectTime));

        List<MesSfcWorkOrderBarcode> mesSfcWorkOrderBarcodes = new ArrayList<>();
        //获取最大值id
        Long workOrderBarcodeId =  mesSfcWorkOrderBarcodeMapper.selectMaxWorkOrderBarcodeId();
        int i = 0;

        long forTime = System.currentTimeMillis();
        List<MesSfcBarcodeProcess> processList = new ArrayList<>();
        for (String barcode : rs.getData()) {
            i++;
            //record.setWorkOrderBarcodeId(null);
            //Integer max = mesSfcWorkOrderBarcodeMapper.findCountCode(record.getBarcodeType(),record.getWorkOrderId())
            record.setBarcode(barcode);

            if (StringUtils.isNotEmpty(record.getOption1()) && record.getOption1().equals("5")) {
                //判断是否已经打完
                BigDecimal barCodeTotalQty = mesSfcWorkOrderBarcodeMapper.saleOrderTotalQty(ControllerUtil.dynamicCondition("type", 1,
                        "salesOrderId", record.getSalesOrderId(),
                        "barcodeTypeId", record.getLabelCategoryId()));
                BigDecimal salesTotalQty = mesSfcWorkOrderBarcodeMapper.saleOrderTotalQty(ControllerUtil.dynamicCondition("type", 1, "salesOrderId", record.getSalesOrderId()));
                if (barCodeTotalQty.compareTo(BigDecimal.ZERO) != 0 && salesTotalQty.compareTo(barCodeTotalQty) == 0) {
                    log.info("=========== redis 失效了");
                    //三秒后失效
                    redisUtil.expire(key, 3);
                }
            }

            //待打印状态
            record.setBarcodeStatus((byte) 3);
            record.setCreateTime(new Date());
            record.setCreateUserId(sysUser.getUserId());
            record.setModifiedTime(new Date());
            record.setModifiedUserId(sysUser.getUserId());
            record.setOrgId(sysUser.getOrganizationId());
            record.setCreateBarcodeTime(new Date());
            record.setWorkOrderBarcodeId(workOrderBarcodeId + i);
            mesSfcWorkOrderBarcodes.add(record);

            MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
            BeanUtil.copyProperties(record, mesSfcWorkOrderBarcode);
            if (labelRuteDto.getBarcodeType() == (byte) 1) {
                //生成条码过站记录
                MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                mesSfcBarcodeProcess.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                mesSfcBarcodeProcess.setWorkOrderCode(mesSfcWorkOrderBarcode.getWorkOrderCode());
                mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                mesSfcBarcodeProcess.setBarcodeType((byte) 2);
                mesSfcBarcodeProcess.setBarcode(mesSfcWorkOrderBarcode.getBarcode());
                mesSfcBarcodeProcess.setProLineId(mesPmWorkOrderDto.getProLineId());
                mesSfcBarcodeProcess.setProcessCode(mesPmWorkOrderDto.getProCode());
                mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
                mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getMaterialVersion());
                mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
                mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
                mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());
                mesSfcBarcodeProcess.setProcessId(routeProcess.getProcessId());
                mesSfcBarcodeProcess.setProcessName(routeProcess.getProcessName());
                mesSfcBarcodeProcess.setNextProcessId(routeProcess.getProcessId());
                mesSfcBarcodeProcess.setNextProcessName(routeProcess.getProcessName());
                mesSfcBarcodeProcess.setSectionId(routeProcess.getSectionId());
                mesSfcBarcodeProcess.setSectionName(routeProcess.getSectionName());
                processList.add(mesSfcBarcodeProcess);
            }

            mesSfcWorkOrderBarcodeList.add(mesSfcWorkOrderBarcode);
        }
        if(CollectionUtil.isNotEmpty(mesSfcWorkOrderBarcodes)){
            //批量生成
            mesSfcWorkOrderBarcodeMapper.insertList(mesSfcWorkOrderBarcodes);
            //设置自增id的初始值
            mesSfcWorkOrderBarcodeMapper.setAutoIncrement(i);
        }

        logger.info("fro循环 执行总时长 : {}毫秒)",(System.currentTimeMillis() - forTime));

        // 批量保存条码过站表
        if (!processList.isEmpty()) {
            long insertListTime = System.currentTimeMillis();
            mesSfcBarcodeProcessMapper.insertList(processList);
            logger.info("批量保存条码过站表 执行总时长 : {}毫秒)",(System.currentTimeMillis() - insertListTime));
        }
        logger.info("新增条码 执行总时长 : {}毫秒)",(System.currentTimeMillis() - startTime));
        return mesSfcWorkOrderBarcodeList;
    }

    @Override
    public MesSfcWorkOrderBarcode findBarcode(String barcode) {
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        example.createCriteria().andEqualTo("barcode", barcode);
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeMapper.selectOneByExample(example);
        return mesSfcWorkOrderBarcode;
    }

    @Override
    public int batchUpdate(List<MesSfcWorkOrderBarcode> workOrderBarcodes) {
        return mesSfcWorkOrderBarcodeMapper.batchUpdate(workOrderBarcodes);
    }

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findByWorkOrderGroup(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findByWorkOrderGroup(map);
    }

    @Override
    public List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findListGroupByWorkOrder(map);
    }

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findListByPalletDet(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findListByPalletDet(map);
    }

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findListByCartonDet(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findListByCartonDet(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<String> wanbaoAddCustomerBarcode(Long salesOrderDetId, String fixedValue, String initialValue, String finalValue, boolean isImport) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (!isImport){
            Example example = new Example(MesSfcWorkOrderBarcode.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("option3", salesOrderDetId).andEqualTo("workOrderId", salesOrderDetId);
            int countByExample = this.selectCountByExample(example);
            if (countByExample>0) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(), "该销售明细单下的客户条码还未删除，不能保存");
            }
        }
        // 保存固定值/初始值/最终值
        OmSalesOrderDet salesOrderDet = new OmSalesOrderDet();
        salesOrderDet.setSalesOrderDetId(salesOrderDetId);
        salesOrderDet.setFixedValue(fixedValue);
        salesOrderDet.setInitialValue(initialValue);
        salesOrderDet.setFinalValue(finalValue);
        omFeignApi.update(salesOrderDet).getData();

        // 生成条码
        Integer start = Integer.valueOf(initialValue);
        Integer end = Integer.valueOf(finalValue);
        int length = finalValue.length();
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = new ArrayList<>();
        List<String> barcodeList = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            String code = String.valueOf(i);
            while (true) {
                if (length == code.length()) {
                    break;
                }
                code = "0" + code;
            }
            MesSfcWorkOrderBarcode record = new MesSfcWorkOrderBarcode();
            record.setOption3(salesOrderDetId.toString());
            record.setWorkOrderId(salesOrderDetId);
            record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("客户条码"));
            record.setBarcode(fixedValue + code);
            record.setBarcodeStatus((byte) 0);
            record.setCreateTime(new Date());
            record.setCreateUserId(sysUser.getUserId());
            record.setModifiedTime(new Date());
            record.setModifiedUserId(sysUser.getUserId());
            record.setOrgId(sysUser.getOrganizationId());
            record.setCreateBarcodeTime(new Date());
            workOrderBarcodes.add(record);
            barcodeList.add(fixedValue + code);
        }
        mesSfcWorkOrderBarcodeMapper.insertList(workOrderBarcodes);
        return barcodeList;
    }

    @Override
    public int wanbaoDeleteCustomerBarcode(Long salesOrderDetId, String fixedValue) {
        // 查找销售明细行下所有客户条码，判断条码状态是否为待投产
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("option3", salesOrderDetId);
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = this.selectByExample(example);
        long count = workOrderBarcodes.stream().filter(item -> !item.getBarcodeStatus().equals((byte) 0)).count();
        if (count > 0) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(), "存在被使用的客户条码，不可删除");
        }
        criteria.andLike("barcode", fixedValue + "%");
        return this.deleteByExample(example);
    }

    @Override
    public List<MesSfcWorkOrderBarcode> wanbaoFindCustomerBarcode(Long salesOrderDetId) {
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("option3", salesOrderDetId);
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = this.selectByExample(example);
        return workOrderBarcodes;
    }


    // region 私有方法

    /**
     * 获取数组中最长公共前缀
     *
     * @param strs
     * @return
     */
    private String longestCommonPrefix(String[] strs) {
        String ret = "";

        if(strs.length == 0) return ret;
        if(strs.length == 1) return strs[0];

        ret = strs[0];

        for(int i = 1; i < strs.length; i++){
            while (!strs[i].startsWith(ret)){ //判断与第一个元素的相同字符
                ret = ret.substring(0, ret.length()-1);
                if (ret.length() == 0){
                    return "";
                }
            }
        }
        return ret;
    }

    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     * @return
     */
    private List<List<PrintModel>> fixedGrouping(List<PrintModel> source, int n) {
        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<List<PrintModel>> result = new ArrayList<List<PrintModel>>();
        int remainder = source.size() % n;
        int size = (source.size() / n);
        for (int i = 0; i < size; i++) {
            List<PrintModel> subset = null;
            subset = source.subList(i * n, (i + 1) * n);
            result.add(subset);
        }
        if (remainder > 0) {
            List<PrintModel> subset = null;
            subset = source.subList(size * n, size * n + remainder);
            result.add(subset);
        }
        return result;
    }

    /**
     * 构造打印条码模板结构
     * @param source
     * @param target
     */
    private PrintModel buildPrintModel(PrintModel source, PrintModel target){
        if (target == null){
            // 若条码是单数，最后一个条码的字段数据不能为null，要赋值为""
            if (source.getOption1() != null){
                source.setSecond1("");
            }
            if (source.getOption2() != null){
                source.setSecond2("");
            }
            if (source.getOption3() != null){
                source.setSecond3("");
            }
            if (source.getOption4() != null){
                source.setSecond4("");
            }
            if (source.getOption5() != null){
                source.setSecond5("");
            }
            if (source.getOption6() != null){
                source.setSecond6("");
            }
            if (source.getOption7() != null){
                source.setSecond7("");
            }
            if (source.getOption8() != null){
                source.setSecond8("");
            }
            if (source.getOption9() != null){
                source.setSecond9("");
            }
            if (source.getOption10() != null){
                source.setSecond10("");
            }
            if (source.getOption11() != null){
                source.setSecond11("");
            }
            if (source.getOption12() != null){
                source.setSecond12("");
            }
            if (source.getOption13() != null){
                source.setSecond13("");
            }
            if (source.getOption14() != null){
                source.setSecond14("");
            }
            if (source.getOption15() != null){
                source.setSecond15("");
            }
            source.setSecondQrCode("");
        }else {
            source.setSecond1(target.getOption1());
            source.setSecond2(target.getOption2());
            source.setSecond3(target.getOption3());
            source.setSecond4(target.getOption4());
            source.setSecond5(target.getOption5());
            source.setSecond6(target.getOption6());
            source.setSecond7(target.getOption7());
            source.setSecond8(target.getOption8());
            source.setSecond9(target.getOption9());
            source.setSecond10(target.getOption10());
            source.setSecond11(target.getOption11());
            source.setSecond12(target.getOption12());
            source.setSecond13(target.getOption13());
            source.setSecond14(target.getOption14());
            source.setSecond15(target.getOption15());
            source.setSecondQrCode(target.getQrCode());
        }
        return source;
    }

    // endregion
}

package com.fantechs.provider.mes.pm.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardCollocationService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardPoolService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderService;
import com.fantechs.provider.mes.pm.utils.BarcodeRuleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wcz on 2020/11/20.
 */
@Service
public class SmtWorkOrderCardCollocationServiceImpl extends BaseService<SmtWorkOrderCardCollocation> implements SmtWorkOrderCardCollocationService {

    @Resource
    private SmtWorkOrderCardCollocationMapper smtWorkOrderCardCollocationMapper;
    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;
    @Resource
    private SmtWorkOrderService smtWorkOrderService;
    @Resource
    private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
    @Resource
    private SmtWorkOrderBarcodePoolMapper smtWorkOrderBarcodePoolMapper;
    @Resource
    private SmtWorkOrderCardPoolService smtWorkOrderCardPoolService;
    @Resource
    private SmtBarcodeRuleSetDetMapper SmtBarcodeRuleSetDetMapper;
    @Resource
    private SmtBarcodeRuleMapper SmtBarcodeRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkOrderCardCollocation smtWorkOrderCardCollocation) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //通过工单中的物料ID查询到物料料号中的转移批量
        Long workOrderId = smtWorkOrderCardCollocation.getWorkOrderId();
        SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);
        if (StringUtils.isEmpty(smtWorkOrderDto)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //工单数量
        BigDecimal workOrderQuantity = smtWorkOrderDto.getWorkOrderQuantity();
        //转移批量
        Integer transferQuantity = smtWorkOrderDto.getTransferQuantity();
        if(StringUtils.isEmpty(transferQuantity)){
            //如果产品转移批量为空，有可能此工单属于部件工单，转移批量换成部件用量
            transferQuantity=smtWorkOrderDto.getQuantity();
        }
        if(StringUtils.isEmpty(transferQuantity)){
            transferQuantity=1;//默认为1
        }
        //工单总转移批次
        int sumBatchQuantity = (int) Math.ceil(workOrderQuantity.doubleValue() / transferQuantity);

        //产生数量
        Integer produceQuantity = smtWorkOrderCardCollocation.getProduceQuantity();
        //已产生数量
        Integer generatedQuantity = 0;
        Example example = new Example(SmtWorkOrderCardCollocation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId", smtWorkOrderCardCollocation.getWorkOrderId());
        List<SmtWorkOrderCardCollocation> cardCollocations = smtWorkOrderCardCollocationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(cardCollocations)) {
            SmtWorkOrderCardCollocation workOrderCardCollocation = cardCollocations.get(0);
            generatedQuantity = workOrderCardCollocation.getProduceQuantity();
        }
        if (produceQuantity + generatedQuantity > sumBatchQuantity) {
            throw new BizErrorException("工单产生流转卡总数量不能大于工单转移批次数量");
        } else if (produceQuantity + generatedQuantity == sumBatchQuantity) {
            smtWorkOrderCardCollocation.setStatus((byte) 2);
        } else {
            smtWorkOrderCardCollocation.setStatus((byte) 1);
        }

        smtWorkOrderCardCollocation.setGeneratedQuantity(produceQuantity + generatedQuantity);
        smtWorkOrderCardCollocation.setModifiedUserId(currentUser.getUserId());
        smtWorkOrderCardCollocation.setModifiedTime(new Date());

        Long cardCode = null;
        Long barcodeRuleId = null;
        //通过条码集合找到对应的条码规则、流转卡规则
        SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet = new SearchSmtBarcodeRuleSetDet();
        searchSmtBarcodeRuleSetDet.setBarcodeRuleSetId(smtWorkOrderDto.getBarcodeRuleSetId());
        List<SmtBarcodeRuleSetDetDto> smtBarcodeRuleSetDetList = SmtBarcodeRuleSetDetMapper.findList(searchSmtBarcodeRuleSetDet);
        if (StringUtils.isEmpty(smtBarcodeRuleSetDetList)) {
            throw new BizErrorException("没有找到相关的条码集合规则");
        }
        for (SmtBarcodeRuleSetDet smtBarcodeRuleSetDet : smtBarcodeRuleSetDetList) {
            SmtBarcodeRule smtBarcodeRule = SmtBarcodeRuleMapper.selectByPrimaryKey(smtBarcodeRuleSetDet.getBarcodeRuleId());
            if (StringUtils.isEmpty(smtBarcodeRule)) {
                throw new BizErrorException("未找到条码规则");
            }
            //产品条码规则
            if (smtBarcodeRule.getBarcodeRuleCategoryId() == 1) {
                barcodeRuleId = smtBarcodeRule.getBarcodeRuleId();
                continue;
            }
            if (smtBarcodeRule.getBarcodeRuleCategoryId() == 6) {
                //流转卡条码规则
                cardCode = smtBarcodeRule.getBarcodeRuleId();
                continue;
            }
        }

        if (StringUtils.isEmpty(cardCode)) {
            throw new BizErrorException("没有找到相关的流转卡条码规则");
        }
        //工单流转卡
        List<SmtWorkOrderCardPool> list = generateCardCode(smtWorkOrderCardCollocation, cardCode, produceQuantity);
        //产品条码流转卡
        if (StringUtils.isNotEmpty(barcodeRuleId)) {
            generateBarcode(list, smtWorkOrderCardCollocation, barcodeRuleId, transferQuantity);
        }
        return smtWorkOrderCardCollocationMapper.insertSelective(smtWorkOrderCardCollocation);
    }

    /**
     * 生成工单规则解析码
     *
     * @param smtWorkOrderCardCollocation
     * @param barcodeRuleId
     * @param quantity
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateBarcode(List<SmtWorkOrderCardPool> list, SmtWorkOrderCardCollocation smtWorkOrderCardCollocation, Long barcodeRuleId, Integer quantity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<SmtWorkOrderBarcodePool> workOrderBarcodePools = new ArrayList<>();
        String workOrderBarcode = null;
        //查询该规则生成的条码规则解析码条数
        Example example = new Example(SmtWorkOrderBarcodePool.class);
        example.createCriteria().andEqualTo("barcodeRuleId", barcodeRuleId);
        example.setOrderByClause("`barcode` desc");
        List<SmtWorkOrderBarcodePool> smtWorkOrderBarcodePools = smtWorkOrderBarcodePoolMapper.selectByExample(example);

        Example example1 = new Example(SmtBarcodeRuleSpec.class);
        example1.createCriteria().andEqualTo("barcodeRuleId", barcodeRuleId);
        List<SmtBarcodeRuleSpec> ruleSpecs = smtBarcodeRuleSpecMapper.selectByExample(example1);
        if (StringUtils.isNotEmpty(smtWorkOrderBarcodePools)) {
            workOrderBarcode = smtWorkOrderBarcodePools.get(0).getBarcode();
        }

        for (SmtWorkOrderCardPool smtWorkOrderCardPool : list) {
            Long workOrderCardPoolId = smtWorkOrderCardPool.getWorkOrderCardPoolId();
            for (int i = 0; i < quantity; i++) {
                if (StringUtils.isNotEmpty(ruleSpecs)) {
                    workOrderBarcode = BarcodeRuleUtils.getMaxSerialNumber(ruleSpecs, workOrderBarcode);
                    workOrderBarcode = BarcodeRuleUtils.analysisCode(ruleSpecs, workOrderBarcode, null);
                } else {
                    throw new BizErrorException("该工单条码规则没有配置");
                }

                SmtWorkOrderBarcodePool smtWorkOrderBarcodePool = new SmtWorkOrderBarcodePool();
                smtWorkOrderBarcodePool.setTaskCode(CodeUtils.getId("PROD"));
                smtWorkOrderBarcodePool.setWorkOrderId(smtWorkOrderCardCollocation.getWorkOrderId());
                smtWorkOrderBarcodePool.setWorkOrderCardPoolId(workOrderCardPoolId);
                smtWorkOrderBarcodePool.setBarcodeRuleId(barcodeRuleId);
                smtWorkOrderBarcodePool.setBarcode(workOrderBarcode);
                smtWorkOrderBarcodePool.setTaskStatus((byte) 0);
                smtWorkOrderBarcodePool.setStatus((byte) 1);
                smtWorkOrderBarcodePool.setCreateUserId(currentUser.getUserId());
                smtWorkOrderBarcodePool.setCreateTime(new Date());
                smtWorkOrderBarcodePool.setModifiedUserId(currentUser.getUserId());
                smtWorkOrderBarcodePool.setModifiedTime(new Date());
                workOrderBarcodePools.add(smtWorkOrderBarcodePool);
            }
        }
        smtWorkOrderBarcodePoolMapper.insertList(workOrderBarcodePools);
    }

    /**
     * 生成工单流转卡解析码
     *
     * @param smtWorkOrderCardCollocation
     * @param barcodeRuleId
     * @param produceQuantity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SmtWorkOrderCardPool> generateCardCode(SmtWorkOrderCardCollocation smtWorkOrderCardCollocation, Long barcodeRuleId, Integer produceQuantity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<SmtWorkOrderCardPool> workOrderCardPools = new ArrayList<>();
        String workOrderCardCode = null;
        //查询该规则生成的工单流转卡解析码条数
        Example example = new Example(SmtWorkOrderCardPool.class);
        example.createCriteria().andEqualTo("barcodeRuleId", barcodeRuleId);
        example.setOrderByClause("`work_order_card_id` desc");
        List<SmtWorkOrderCardPool> smtWorkOrderCardPools = smtWorkOrderCardPoolMapper.selectByExample(example);

        Example example1 = new Example(SmtBarcodeRuleSpec.class);
        example1.createCriteria().andEqualTo("barcodeRuleId", barcodeRuleId);
        List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecMapper.selectByExample(example1);
        if (StringUtils.isNotEmpty(smtWorkOrderCardPools)) {
            workOrderCardCode = smtWorkOrderCardPools.get(0).getWorkOrderCardId();
        }

        //=====判断当前工单是否存在父级工单，如果存在父级工单，是否产生父级流程卡
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(smtWorkOrderCardCollocation.getWorkOrderId());
        Long parentWorkOrderCardId=0L;
        if(smtWorkOrder.getParentId()!=0){
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setWorkOrderId(smtWorkOrder.getParentId());
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
            if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
                throw new BizErrorException("父级工单未生成流程卡："+smtWorkOrder.getParentId());
            }
            SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = smtWorkOrderCardPoolDtoList.get(0);
            parentWorkOrderCardId=smtWorkOrderCardPoolDto.getWorkOrderCardPoolId();
        }
        //=====

        for (int i = 0; i < produceQuantity; i++) {
            if (StringUtils.isNotEmpty(list)) {
                workOrderCardCode = BarcodeRuleUtils.getMaxSerialNumber(list, workOrderCardCode);
                workOrderCardCode = BarcodeRuleUtils.analysisCode(list, workOrderCardCode, null);
            } else {
                throw new BizErrorException("该工单流转卡规则没有配置");
            }

            SmtWorkOrderCardPool smtWorkOrderCardPool = new SmtWorkOrderCardPool();
            smtWorkOrderCardPool.setTaskCode(CodeUtils.getId("WOED"));
            smtWorkOrderCardPool.setParentId(parentWorkOrderCardId);
            smtWorkOrderCardPool.setWorkOrderId(smtWorkOrderCardCollocation.getWorkOrderId());
            smtWorkOrderCardPool.setBarcodeRuleId(barcodeRuleId);
            smtWorkOrderCardPool.setWorkOrderCardId(workOrderCardCode);
            smtWorkOrderCardPool.setIsDelete((byte)1);
            smtWorkOrderCardPool.setCardStatus((byte) 0);
            smtWorkOrderCardPool.setStatus((byte) 1);
            smtWorkOrderCardPool.setCreateUserId(currentUser.getUserId());
            smtWorkOrderCardPool.setCreateTime(new Date());
            smtWorkOrderCardPool.setModifiedUserId(currentUser.getUserId());
            smtWorkOrderCardPool.setModifiedTime(new Date());

            workOrderCardPools.add(smtWorkOrderCardPool);
        }
        smtWorkOrderCardPoolMapper.insertList(workOrderCardPools);
        return workOrderCardPools;
    }


    @Override
    public List<SmtWorkOrderCardCollocationDto> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation) {
        List<SmtWorkOrderCardCollocationDto> list = smtWorkOrderCardCollocationMapper.findList(searchSmtWorkOrderCardCollocation);
        return list;
    }
}

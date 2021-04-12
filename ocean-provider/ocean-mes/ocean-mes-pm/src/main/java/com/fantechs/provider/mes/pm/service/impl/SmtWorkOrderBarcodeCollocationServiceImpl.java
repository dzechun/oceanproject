package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.utils.BarcodeRuleUtils;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderBarcodeCollocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2020/11/21.
 */
@Service
public class SmtWorkOrderBarcodeCollocationServiceImpl  extends BaseService<SmtWorkOrderBarcodeCollocation> implements SmtWorkOrderBarcodeCollocationService {

    @Resource
    private SmtWorkOrderBarcodeCollocationMapper smtWorkOrderBarcodeCollocationMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;
    @Resource
    private SmtWorkOrderBarcodePoolMapper smtWorkOrderBarcodePoolMapper;
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
    @Resource
    private SmtBarcodeRuleSetDetMapper SmtBarcodeRuleSetDetMapper;
    @Resource
    private SmtBarcodeRuleMapper SmtBarcodeRuleMapper;

    @Override
    public List<SmtWorkOrderBarcodeCollocationDto> findList(SearchSmtWorkOrderBarcodeCollocation searchSmtWorkOrderBarcodeCollocation) {
        return smtWorkOrderBarcodeCollocationMapper.findList(searchSmtWorkOrderBarcodeCollocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkOrderBarcodeCollocation record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Long workOrderId = record.getWorkOrderId();
        MesPmWorkOrderDto smtWorkOrderDto = mesPmWorkOrderMapper.selectByWorkOrderId(workOrderId);
        //通过条码集合找到对应的条码规则、流转卡规则
        SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet = new SearchSmtBarcodeRuleSetDet();
        searchSmtBarcodeRuleSetDet.setBarcodeRuleSetId(smtWorkOrderDto.getBarcodeRuleSetId());
        List<SmtBarcodeRuleSetDetDto> smtBarcodeRuleSetDetList = SmtBarcodeRuleSetDetMapper.findList(searchSmtBarcodeRuleSetDet);
        if(StringUtils.isEmpty(smtBarcodeRuleSetDetList)){
            throw new BizErrorException("没有找到相关的条码集合规则");
        }
        Long barcodeRuleId = null;
        for(SmtBarcodeRuleSetDet smtBarcodeRuleSetDet:smtBarcodeRuleSetDetList){
            SmtBarcodeRule smtBarcodeRule = SmtBarcodeRuleMapper.selectByPrimaryKey(smtBarcodeRuleSetDet.getBarcodeRuleId());
            if(StringUtils.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //产品条码规则
            if(smtBarcodeRule.getBarcodeRuleCategoryId()==1){
                barcodeRuleId = smtBarcodeRule.getBarcodeRuleId();
                break;
            }
        }
        //工单数量
        BigDecimal workOrderQuantity = smtWorkOrderDto.getWorkOrderQty();

        //产生数量
        Integer produceQuantity = record.getProduceQuantity();
        //已产生数量
        Integer generatedQuantity = 0;
        Example example = new Example(SmtWorkOrderBarcodeCollocation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId",record.getWorkOrderId());
        List<SmtWorkOrderBarcodeCollocation> barcodeCollocations = smtWorkOrderBarcodeCollocationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(barcodeCollocations)){
            for (SmtWorkOrderBarcodeCollocation barcodeCollocation : barcodeCollocations) {
                generatedQuantity+=barcodeCollocation.getProduceQuantity();
            }
        }
        if(produceQuantity+generatedQuantity>workOrderQuantity.intValue()){
            throw new BizErrorException("工单产生条码总数量不能大于工单数量");
        }else if(produceQuantity+generatedQuantity==workOrderQuantity.intValue()){
            record.setStatus((byte) 2);
        }else {
            record.setStatus((byte) 1);
        }

        record.setGeneratedQuantity(produceQuantity+generatedQuantity);
        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        record.setModifiedTime(new Date());

        //生成工单规则解析码
        generateBarcode(record, barcodeRuleId, produceQuantity);
        return smtWorkOrderBarcodeCollocationMapper.insertSelective(record);
    }

    /**
     * 生成工单规则解析码
     * @param smtWorkOrderBarcodeCollocation
     * @param barcodeRuleId
     * @param quantity
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateBarcode(SmtWorkOrderBarcodeCollocation smtWorkOrderBarcodeCollocation, Long barcodeRuleId, Integer quantity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<SmtWorkOrderBarcodePool> workOrderBarcodePools=new ArrayList<>();
        String workOrderBarcode=null;
        //查询该规则生成的条码规则解析码条数
        Example example= new Example(SmtWorkOrderBarcodePool.class);
        example.createCriteria().andEqualTo("barcodeRuleId",barcodeRuleId);
        example.setOrderByClause("`barcode` desc");
        List<SmtWorkOrderBarcodePool> smtWorkOrderBarcodePools = smtWorkOrderBarcodePoolMapper.selectByExample(example);

        Example example1= new Example(SmtBarcodeRuleSpec.class);
        example1.createCriteria().andEqualTo("barcodeRuleId",barcodeRuleId);
        List<SmtBarcodeRuleSpec> ruleSpecs = smtBarcodeRuleSpecMapper.selectByExample(example1);
        if(StringUtils.isNotEmpty(smtWorkOrderBarcodePools)){
            workOrderBarcode = smtWorkOrderBarcodePools.get(0).getBarcode();
        }
        for (int i=0;i<quantity;i++){
            if(StringUtils.isNotEmpty(ruleSpecs)){
                workOrderBarcode= BarcodeRuleUtils.getMaxSerialNumber(ruleSpecs, workOrderBarcode);
                workOrderBarcode= BarcodeRuleUtils.analysisCode(ruleSpecs,workOrderBarcode,null);
            }else {
                throw new BizErrorException("该工单条码规则没有配置");
            }

            SmtWorkOrderBarcodePool smtWorkOrderBarcodePool=new SmtWorkOrderBarcodePool();
            smtWorkOrderBarcodePool.setTaskCode(CodeUtils.getId("PROD"));
            smtWorkOrderBarcodePool.setWorkOrderId(smtWorkOrderBarcodeCollocation.getWorkOrderId());
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
        smtWorkOrderBarcodePoolMapper.insertList(workOrderBarcodePools);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWorkOrderBarcodeCollocation entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        entity.setModifiedUserId(currentUser.getUserId());
        entity.setModifiedTime(new Date());
        return smtWorkOrderBarcodeCollocationMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] items = ids.split(",");
        for (String item : items) {
            SmtWorkOrderBarcodeCollocation smtWorkOrderBarcodeCollocation = smtWorkOrderBarcodeCollocationMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(smtWorkOrderBarcodeCollocation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtWorkOrderBarcodeCollocationMapper.deleteByIds(ids);
    }
}

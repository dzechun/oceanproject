package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleSpecMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBarcodeCollocationMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBarcodePoolMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBarcodeCollocationService;
import com.fantechs.provider.imes.apply.utils.BarcodeRuleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    private SmtWorkOrderMapper smtWorkOrderMapper;
    @Resource
    private SmtWorkOrderBarcodePoolMapper smtWorkOrderBarcodePoolMapper;
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;

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
        SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);
        Long barcodeRuleId = smtWorkOrderDto.getBarcodeRuleId();
        Integer workOrderQuantity = smtWorkOrderDto.getWorkOrderQuantity();
        Integer transferQuantity = smtWorkOrderDto.getTransferQuantity();
        //工单的转移批次
        int sumBatchQuantity = (int) Math.ceil((double)workOrderQuantity / transferQuantity);
        //产生数量
        Integer produceQuantity = record.getProduceQuantity();
        //已产生数量
        Integer generatedQuantity = 0;
        Example example = new Example(SmtWorkOrderCardCollocation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderId",record.getWorkOrderId());
        List<SmtWorkOrderBarcodeCollocation> barcodeCollocations = smtWorkOrderBarcodeCollocationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(barcodeCollocations)){
            for (SmtWorkOrderBarcodeCollocation barcodeCollocation : barcodeCollocations) {
                generatedQuantity+=barcodeCollocation.getProduceQuantity();
            }
        }
        if(produceQuantity+generatedQuantity>sumBatchQuantity){
            throw new BizErrorException("工单产生条码总数量不能大于工单数量");
        }else if(produceQuantity+generatedQuantity==sumBatchQuantity){
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
            smtWorkOrderBarcodePool.setTaskCode(UUIDUtils.getUUID());
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

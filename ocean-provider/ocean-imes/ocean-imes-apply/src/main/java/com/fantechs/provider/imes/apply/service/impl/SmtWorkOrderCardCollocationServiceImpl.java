package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardPool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleSpecMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderCardCollocationMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderCardPoolMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderCardCollocationService;
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
 * Created by wcz on 2020/11/20.
 */
@Service
public class SmtWorkOrderCardCollocationServiceImpl extends BaseService<SmtWorkOrderCardCollocation> implements SmtWorkOrderCardCollocationService {

        @Resource
        private SmtWorkOrderCardCollocationMapper smtWorkOrderCardCollocationMapper;
        @Resource
        private SmtWorkOrderMapper smtWorkOrderMapper;
        @Resource
        private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
        @Resource
        private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtWorkOrderCardCollocation smtWorkOrderCardCollocation) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            //通过工单中的物料ID查询到物料料号中的转移批量
            Long workOrderId = smtWorkOrderCardCollocation.getWorkOrderId();
            SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);
            Long barcodeRuleId = smtWorkOrderDto.getBarcodeRuleId();
            Integer workOrderQuantity = smtWorkOrderDto.getWorkOrderQuantity();
            Integer transferQuantity = smtWorkOrderDto.getTransferQuantity();
            //工单的转移批次
            int sumBatchQuantity = (int) Math.ceil((double)workOrderQuantity / transferQuantity);

            //产生数量
            Integer produceQuantity = smtWorkOrderCardCollocation.getProduceQuantity();
            //已产生数量
            Integer generatedQuantity = 0;
            Example example = new Example(SmtWorkOrderCardCollocation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderId",smtWorkOrderCardCollocation.getWorkOrderId());
            List<SmtWorkOrderCardCollocation> cardCollocations = smtWorkOrderCardCollocationMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(cardCollocations)){
                for (SmtWorkOrderCardCollocation cardCollocation : cardCollocations) {
                    generatedQuantity+=cardCollocation.getProduceQuantity();
                }
            }
            if(produceQuantity+generatedQuantity>sumBatchQuantity){
                   throw new BizErrorException("工单产生条码总数量不能大于工单数量");
            }else if(produceQuantity+generatedQuantity==sumBatchQuantity){
                smtWorkOrderCardCollocation.setStatus((byte) 2);
            }else {
                smtWorkOrderCardCollocation.setStatus((byte) 1);
            }

            smtWorkOrderCardCollocation.setGeneratedQuantity(produceQuantity+generatedQuantity);
            smtWorkOrderCardCollocation.setCreateUserId(currentUser.getUserId());
            smtWorkOrderCardCollocation.setCreateTime(new Date());
            smtWorkOrderCardCollocation.setModifiedUserId(currentUser.getUserId());
            smtWorkOrderCardCollocation.setModifiedTime(new Date());

            //生成工单流转卡编码
            generateCardCode(smtWorkOrderCardCollocation, barcodeRuleId, produceQuantity);
            //生成工单编码
            //generateBarcode();

            return smtWorkOrderCardCollocationMapper.insertSelective(smtWorkOrderCardCollocation);
        }

        @Transactional(rollbackFor = Exception.class)
        public void generateCardCode(SmtWorkOrderCardCollocation smtWorkOrderCardCollocation, Long barcodeRuleId, Integer produceQuantity) {
            List<SmtWorkOrderCardPool> workOrderCardPools=new ArrayList<>();
            String workOrderCardCode=null;
            int maxLength=0;
            Example example1= new Example(SmtWorkOrderCardPool.class);
            example1.createCriteria().andEqualTo("barcodeRuleId",barcodeRuleId);
            List<SmtWorkOrderCardPool> smtWorkOrderCardPools = smtWorkOrderCardPoolMapper.selectByExample(example1);

            for (int i=0;i<produceQuantity;i++){
                if(StringUtils.isNotEmpty(smtWorkOrderCardPools)){
                    maxLength=smtWorkOrderCardPools.size();
                }

                Example example2= new Example(SmtBarcodeRuleSpec.class);
                example2.createCriteria().andEqualTo("barcodeRuleId",barcodeRuleId);
                List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecMapper.selectByExample(example2);
                if(StringUtils.isNotEmpty(list)){
                    workOrderCardCode= BarcodeRuleUtils.analysisSerialNumber(list, maxLength, null);
                    for (SmtBarcodeRuleSpec smtBarcodeRuleSpec : list) {
                        String specification = smtBarcodeRuleSpec.getSpecification();
                        Integer step = smtBarcodeRuleSpec.getStep();
                        Integer initialValue = smtBarcodeRuleSpec.getInitialValue();
                        if("[S]".equals(specification)||"[F]".equals(specification)||"[b]".equals(specification)||"[c]".equals(specification)){
                            maxLength=i*step+initialValue;
                        }
                    }
                }

                SmtWorkOrderCardPool smtWorkOrderCardPool=new SmtWorkOrderCardPool();
                smtWorkOrderCardPool.setTaskCode(UUIDUtils.getUUID());
                smtWorkOrderCardPool.setWorkOrderId(smtWorkOrderCardCollocation.getWorkOrderId());
                smtWorkOrderCardPool.setBarcodeRuleId(barcodeRuleId);
                smtWorkOrderCardPool.setWorkOrderCardId(workOrderCardCode);
                smtWorkOrderCardPool.setCardStatus((byte) 0);
                smtWorkOrderCardPool.setStatus((byte) 1);

                workOrderCardPools.add(smtWorkOrderCardPool);
            }
            smtWorkOrderCardPoolMapper.insertList(workOrderCardPools);
        }


        @Override
        public List<SmtWorkOrderCardCollocationDto> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation) {
            List<SmtWorkOrderCardCollocationDto> list = smtWorkOrderCardCollocationMapper.findList(searchSmtWorkOrderCardCollocation);
            return list;
        }
}

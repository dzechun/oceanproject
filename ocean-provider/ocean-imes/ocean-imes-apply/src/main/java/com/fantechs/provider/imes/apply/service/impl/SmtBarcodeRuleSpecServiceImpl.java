package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleMapper;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleSpecMapper;
import com.fantechs.provider.imes.apply.service.SmtBarcodeRuleSpecService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by wcz on 2020/11/07.
 */
@Service
public class SmtBarcodeRuleSpecServiceImpl extends BaseService<SmtBarcodeRuleSpec> implements SmtBarcodeRuleSpecService {

         @Resource
         private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
         @Resource
         private SmtBarcodeRuleMapper smtBarcodeRuleMapper;

        @Override
        public List<SmtBarcodeRuleSpec> findList(SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec) {
            return smtBarcodeRuleSpecMapper.findList(searchSmtBarcodeRuleSpec);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchSave(List<SmtBarcodeRuleSpec> list) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("条码规则没有配置");
            }

            //查询条码规则
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(list.get(0).getBarcodeRuleId());

            //校验设置的条码规则是否符合
            String barcodeRule = checkBarcodeRule(list);

            //配置好条码规则后，设置进条码规则中
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            smtBarcodeRuleMapper.updateByPrimaryKey(smtBarcodeRule);
            return smtBarcodeRuleSpecMapper.insertList(list);
        }

        @Transactional(rollbackFor = Exception.class)
        public String checkBarcodeRule(List<SmtBarcodeRuleSpec> list) {
            List<String> specs=new ArrayList<>();
            StringBuilder sb=new StringBuilder();
            for (int i=0;i<list.size();i++){
                if(i>0&&list.get(i-1).getSpecId().equals(list.get(i).getSpecId())){
                    throw new BizErrorException("相邻的属性类别不能相同");
                }
                SmtBarcodeRuleSpec smtBarcodeRuleSpec = list.get(i);

                String specification = smtBarcodeRuleSpec.getSpecification();
                Integer step = smtBarcodeRuleSpec.getStep();
                if(specification.contains("]")){
                    //例如：将[Y][Y][Y][Y]转成[YYYY]
                    String spec = getRuleSpec(specification, step);
                    sb.append(spec);
                }else {
                    sb.append(specification);
                }

                specs.add(specification);
            }

            //判断S、F、b、c只能使用一个
            boolean sCode = sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            boolean fCode = !sb.toString().contains("S") && sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            boolean bCode = !sb.toString().contains("S") && !sb.toString().contains("F") && sb.toString().contains("b") && !sb.toString().contains("c");
            boolean cCode = !sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && sb.toString().contains("c");
            boolean baseCode = !sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            if(!sCode&&!fCode&&!bCode&&!cCode&&!baseCode){
                throw new BizErrorException("条码规则配置错误");
            }

            long materialNum = specs.stream().filter("[P]"::equals).count();
            long lineNum = specs.stream().filter("[L]"::equals).count();
            long customerNum = specs.stream().filter("[C]"::equals).count();
            if(materialNum>1||lineNum>1||customerNum>1){
                throw new BizErrorException("条码规则配置错误");
            }
            return sb.toString();
        }


        @Transactional(rollbackFor = Exception.class)
        public String getRuleSpec(String specification, Integer step) {
            StringBuilder sb=new StringBuilder();
            for (int j=0;j<step;j++){
                sb.append(specification);
            }

            Pattern pattern = Pattern.compile("](.*?)\\[");
            Matcher matcher = pattern.matcher(sb.toString());
            String spec = matcher.replaceAll("");
            return spec;
        }

        @Override
        public int batchUpdate(List<SmtBarcodeRuleSpec> list) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            //查询条码规则
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(list.get(0).getBarcodeRuleId());

            //校验设置的条码规则是否符合
            String barcodeRule = checkBarcodeRule(list);
            //配置好条码规则后，设置进条码规则中
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            smtBarcodeRuleMapper.updateByPrimaryKey(smtBarcodeRule);
            return smtBarcodeRuleSpecMapper.updateBatch(list);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Long barcodeRuleId=null;
            String barcodeRule=null;
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                SmtBarcodeRuleSpec smtBarcodeRuleSpec = smtBarcodeRuleSpecMapper.selectByPrimaryKey(id);
                if(StringUtils.isEmpty(smtBarcodeRuleSpec)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }

                barcodeRuleId = smtBarcodeRuleSpec.getBarcodeRuleId();
            }

            //删除条码规则配置
            int i = smtBarcodeRuleSpecMapper.deleteByIds(ids);

            /**
             *  删除条码规则配置后的条码规则
             */
            //查询条码规则
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(barcodeRuleId);

            Example example = new Example(SmtBarcodeRuleSpec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleId",barcodeRuleId);
            List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecMapper.selectByExample(example);

            //校验设置的条码规则是否符合
            if(StringUtils.isNotEmpty(list)){
                barcodeRule = checkBarcodeRule(list);
            }

            //配置好条码规则后，设置进条码规则中
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            smtBarcodeRuleMapper.updateByPrimaryKey(smtBarcodeRule);
            return i;
        }

}

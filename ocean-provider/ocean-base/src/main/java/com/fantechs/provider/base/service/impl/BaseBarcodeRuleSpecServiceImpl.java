package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleMapper;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleSpecMapper;
import com.fantechs.provider.base.service.BaseBarcodeRuleSpecService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/11/07.
 */
@Service
public class BaseBarcodeRuleSpecServiceImpl extends BaseService<BaseBarcodeRuleSpec> implements BaseBarcodeRuleSpecService {

        @Resource
        private BaseBarcodeRuleSpecMapper baseBarcodeRuleSpecMapper;
        @Resource
        private BaseBarcodeRuleMapper baseBarcodeRuleMapper;

        @Override
        public List<BaseBarcodeRuleSpecDto> findList(Map<String, Object> map) {
            return baseBarcodeRuleSpecMapper.findList(map);
        }

        @Override
        public List<BaseBarcodeRuleSpec> findSpec(SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec) {
                Example example = new Example(BaseBarcodeRuleSpec.class);
                example.createCriteria().andEqualTo("barcodeRuleId", searchBaseBarcodeRuleSpec.getBarcodeRuleId());
                return baseBarcodeRuleSpecMapper.selectByExample(example);
        }

        @Override
        public List<String> findFunction() {
                return baseBarcodeRuleSpecMapper.findFunction();
        }

        @Override
        public String executeFunction(String functionName, String params) {
                return baseBarcodeRuleSpecMapper.executeFunction(functionName,params);
        }


       /* @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchSave(List<SmtBarcodeRuleSpec> list) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("????????????????????????");
            }

            //??????????????????
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(list.get(0).getBarcodeRuleId());

            //???????????????????????????????????????
            String barcodeRule = checkBarcodeRule(list);

            //???????????????????????????????????????????????????
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
                    throw new BizErrorException("?????????????????????????????????");
                }
                SmtBarcodeRuleSpec smtBarcodeRuleSpec = list.get(i);

                String specification = smtBarcodeRuleSpec.getSpecification();
                Integer barcodeLength = smtBarcodeRuleSpec.getBarcodeLength();
                if(specification.contains("]")){
                    //????????????[Y][Y][Y][Y]??????[YYYY]
                    String spec = getRuleSpec(specification, barcodeLength);
                    sb.append(spec);
                }else {
                    sb.append(specification);
                }

                specs.add(specification);
            }

            //??????S???F???b???c??????????????????
            boolean sCode = sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            boolean fCode = !sb.toString().contains("S") && sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            boolean bCode = !sb.toString().contains("S") && !sb.toString().contains("F") && sb.toString().contains("b") && !sb.toString().contains("c");
            boolean cCode = !sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && sb.toString().contains("c");
            boolean baseCode = !sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            if(!sCode&&!fCode&&!bCode&&!cCode&&!baseCode){
                throw new BizErrorException("????????????????????????");
            }

            //specs ???????????????[P]??????
            long materialNum = specs.stream().filter("[P]"::equals).count();
            //specs ???????????????[L]??????
            long lineNum = specs.stream().filter("[L]"::equals).count();
            //specs ???????????????[C]??????
            long customerNum = specs.stream().filter("[C]"::equals).count();

            if(materialNum>1||lineNum>1||customerNum>1){
                throw new BizErrorException("????????????????????????");
            }
            return sb.toString();
        }


        @Transactional(rollbackFor = Exception.class)
        public String getRuleSpec(String specification, Integer barcodeLength) {
            StringBuilder sb=new StringBuilder();
            for (int j=0;j<barcodeLength;j++){
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

            //??????????????????
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(list.get(0).getBarcodeRuleId());

            //???????????????????????????????????????
            String barcodeRule = checkBarcodeRule(list);
            //???????????????????????????????????????????????????
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            smtBarcodeRuleMapper.updateByPrimaryKey(smtBarcodeRule);
            return smtBarcodeRuleSpecMapper.updateBatch(list);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

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

            //????????????????????????
            int i = smtBarcodeRuleSpecMapper.deleteByIds(ids);

            *//**
             *  ??????????????????????????????????????????
             *//*
            //??????????????????
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(barcodeRuleId);

            Example example = new Example(SmtBarcodeRuleSpec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleId",barcodeRuleId);
            List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecMapper.selectByExample(example);

            //???????????????????????????????????????
            if(StringUtils.isNotEmpty(list)){
                barcodeRule = checkBarcodeRule(list);
            }

            //???????????????????????????????????????????????????
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            smtBarcodeRuleMapper.updateByPrimaryKey(smtBarcodeRule);
            return i;
        }*/

}

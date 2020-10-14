package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */
@Service
public class SmtWorkOrderBomServiceImpl extends BaseService<SmtWorkOrderBom> implements SmtWorkOrderBomService {

        @Resource
        private SmtWorkOrderBomMapper smtWorkOrderBomMapper;
        @Resource
        private SmtHtWorkOrderBomMapper smtHtWorkOrderBomMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtWorkOrderBom smtWorkOrderBom) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtWorkOrderBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("partMaterialId",smtWorkOrderBom.getPartMaterialId());

            List<SmtWorkOrderBom> smtWorkOrderBoms = smtWorkOrderBomMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtWorkOrderBoms)){
                throw new BizErrorException("零件料号已存在");
            }

            smtWorkOrderBom.setCreateUserId(currentUser.getUserId());
            smtWorkOrderBom.setCreateTime(new Date());
            smtWorkOrderBomMapper.insertUseGeneratedKeys(smtWorkOrderBom);

            //新增工单BOM历史信息
            SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
            BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
            int i = smtHtWorkOrderBomMapper.insertSelective(smtHtWorkOrderBom);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtWorkOrderBom smtWorkOrderBom) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtWorkOrderBom.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("partMaterialId",smtWorkOrderBom.getPartMaterialId());

            SmtWorkOrderBom workOrderBom = smtWorkOrderBomMapper.selectOneByExample(example);

            if(StringUtils.isNotEmpty(workOrderBom)&&!workOrderBom.getWorkOrderBomId().equals(smtWorkOrderBom.getWorkOrderBomId())){
                throw new BizErrorException("零件料号已存在");
            }

            smtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
            smtWorkOrderBom.setModifiedTime(new Date());
            int i= smtWorkOrderBomMapper.updateByPrimaryKeySelective(smtWorkOrderBom);

            //新增工单BOM历史信息
            SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
            BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
            smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
            smtHtWorkOrderBom.setModifiedTime(new Date());
            smtHtWorkOrderBomMapper.insertSelective(smtHtWorkOrderBom);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<SmtHtWorkOrderBom> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            String[] workOrderBomIds = ids.split(",");
            for (String workOrderBomId : workOrderBomIds) {
                SmtWorkOrderBom smtWorkOrderBom = smtWorkOrderBomMapper.selectByPrimaryKey(workOrderBomId);
                if(StringUtils.isEmpty(smtWorkOrderBom)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //新增工单BOM历史信息
                SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
                BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
                smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                smtHtWorkOrderBom.setModifiedTime(new Date());
                list.add(smtHtWorkOrderBom);

            }
            smtHtWorkOrderBomMapper.insertList(list);

            return smtWorkOrderBomMapper.deleteByIds(ids);
        }

        @Override
        public List<SmtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom) {
            return smtWorkOrderBomMapper.findList(searchSmtWorkOrderBom);
        }
}

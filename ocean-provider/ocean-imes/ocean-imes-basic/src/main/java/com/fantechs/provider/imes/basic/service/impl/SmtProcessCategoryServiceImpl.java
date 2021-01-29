package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.service.SmtProcessCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * Created by leifengzhi on 2020/10/15.
 */
@Service
public class SmtProcessCategoryServiceImpl extends BaseService<SmtProcessCategory> implements SmtProcessCategoryService {

    @Resource
    private SmtProcessCategoryMapper smtProcessCategoryMapper;

    @Resource
    private SmtHtProcessCategoryMapper smtHtProcessCategoryMapper;

    @Resource
    private SmtProcessMapper smtProcessMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProcessCategory smtProcessCategory) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCategoryCode",smtProcessCategory.getProcessCategoryCode());
        List<SmtProcessCategory> smtProcessCategories = smtProcessCategoryMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProcessCategories)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcessCategory.setCreateUserId(currentUserInfo.getUserId());
        smtProcessCategory.setCreateTime(new Date());
        smtProcessCategory.setModifiedTime(new Date());
        smtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
        smtProcessCategoryMapper.insertUseGeneratedKeys(smtProcessCategory);

        //新增工序类别历史信息
        SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
        BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
        return smtHtProcessCategoryMapper.insertSelective(smtHtProcessCategory);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProcessCategory smtProcessCategory) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCategoryCode",smtProcessCategory.getProcessCategoryCode());
        SmtProcessCategory processCategory = smtProcessCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(processCategory)&&!processCategory.getProcessCategoryId().equals(smtProcessCategory.getProcessCategoryId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
        smtProcessCategory.setModifiedTime(new Date());
        smtProcessCategoryMapper.updateByPrimaryKeySelective(smtProcessCategory);

        //新增工序列表历史信息
        SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
        BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
        return smtHtProcessCategoryMapper.insertSelective(smtHtProcessCategory);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        ArrayList<SmtHtProcessCategory> list = new ArrayList<>();

        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] processCategoryIds = ids.split(",");
        for (String processCategoryId : processCategoryIds) {
            SmtProcessCategory smtProcessCategory = smtProcessCategoryMapper.selectByPrimaryKey(processCategoryId);
            if (StringUtils.isEmpty(smtProcessCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被工序以用
            Example example = new Example(SmtProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processCategoryId",smtProcessCategory.getProcessCategoryId());
            List<SmtProcess> smtProcesses = smtProcessMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProcesses)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增工序历史信息
            SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
            BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
            smtHtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
            smtHtProcessCategory.setModifiedTime(new Date());
            list.add(smtHtProcessCategory);
        }
        smtHtProcessCategoryMapper.insertList(list);

        return smtProcessCategoryMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtProcessCategoryDto> findList(Map<String, Object> map) {
        return smtProcessCategoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtProcessCategoryDto> smtProcessCategoryDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtProcessCategory> list = new LinkedList<>();
        LinkedList<SmtHtProcessCategory> htList = new LinkedList<>();
        for (int i = 0; i < smtProcessCategoryDtos.size(); i++) {
            SmtProcessCategoryDto smtProcessCategoryDto = smtProcessCategoryDtos.get(i);
            String processCategoryCode = smtProcessCategoryDto.getProcessCategoryCode();
            String processCategoryName = smtProcessCategoryDto.getProcessCategoryName();
            if (StringUtils.isEmpty(
                    processCategoryCode,processCategoryName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtProcessCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("processCategoryCode",processCategoryCode);
            if (StringUtils.isNotEmpty(smtProcessCategoryMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            SmtProcessCategory smtProcessCategory = new SmtProcessCategory();
            BeanUtils.copyProperties(smtProcessCategoryDto,smtProcessCategory);
            smtProcessCategory.setCreateTime(new Date());
            smtProcessCategory.setCreateUserId(currentUser.getUserId());
            smtProcessCategory.setModifiedTime(new Date());
            smtProcessCategory.setModifiedUserId(currentUser.getUserId());
            smtProcessCategory.setStatus((byte) 1);
            list.add(smtProcessCategory);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtProcessCategoryMapper.insertList(list);
        }

        for (SmtProcessCategory smtProcessCategory : list) {
            SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
            BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
            htList.add(smtHtProcessCategory);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtProcessCategoryMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}

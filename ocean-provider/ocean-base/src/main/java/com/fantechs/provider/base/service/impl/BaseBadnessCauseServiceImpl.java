package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessCauseDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessCauseImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessPhenotypeImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessPhenotype;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBadnessCauseMapper;
import com.fantechs.provider.base.mapper.BaseHtBadnessCauseMapper;
import com.fantechs.provider.base.service.BaseBadnessCauseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class BaseBadnessCauseServiceImpl extends BaseService<BaseBadnessCause> implements BaseBadnessCauseService {

    @Resource
    private BaseBadnessCauseMapper baseBadnessCauseMapper;
    @Resource
    private BaseHtBadnessCauseMapper baseHtBadnessCauseMapper;

    @Override
    public List<BaseBadnessCauseDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseBadnessCauseMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessCause baseBadnessCause) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseBadnessCause.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("badnessCauseCode",baseBadnessCause.getBadnessCauseCode());
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        BaseBadnessCause baseBadnessCause1 = baseBadnessCauseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCause1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseBadnessCause.setCreateTime(new Date());
        baseBadnessCause.setCreateUserId(currentUser.getUserId());
        baseBadnessCause.setModifiedTime(new Date());
        baseBadnessCause.setModifiedUserId(currentUser.getUserId());
        baseBadnessCause.setStatus((byte) 1);
        baseBadnessCause.setOrgId(currentUser.getOrganizationId());
        int i = baseBadnessCauseMapper.insertUseGeneratedKeys(baseBadnessCause);

        BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
        BeanUtils.copyProperties(baseBadnessCause,baseHtBadnessCause);
        baseHtBadnessCause.setModifiedTime(new Date());
        baseHtBadnessCause.setModifiedUserId(currentUser.getUserId());
        baseHtBadnessCauseMapper.insertSelective(baseHtBadnessCause);

        return i;
    }

    @Override
    public int update(BaseBadnessCause baseBadnessCause) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseBadnessCause.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("badnessCauseCode",baseBadnessCause.getBadnessCauseCode())
                .andNotEqualTo("badnessCauseId",baseBadnessCause.getBadnessCauseId());
        BaseBadnessCause baseBadnessCause1 = baseBadnessCauseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCause1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseBadnessCause.setModifiedTime(new Date());
        baseBadnessCause.setModifiedUserId(currentUser.getUserId());
        int i = baseBadnessCauseMapper.updateByPrimaryKeySelective(baseBadnessCause);

        BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
        BeanUtils.copyProperties(baseBadnessCause,baseHtBadnessCause);
        baseHtBadnessCauseMapper.insertSelective(baseHtBadnessCause);

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        LinkedList<BaseHtBadnessCause> htList = new LinkedList<>();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BaseBadnessCause baseBadnessCause = baseBadnessCauseMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseBadnessCause)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
            BeanUtils.copyProperties(baseBadnessCause,baseHtBadnessCause);
            htList.add(baseHtBadnessCause);
        }

        baseHtBadnessCauseMapper.insertList(htList);
        return baseBadnessCauseMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseBadnessCauseImport> baseBadnessCauseImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseBadnessCause> list = new LinkedList<>();
        LinkedList<BaseHtBadnessCause> htList = new LinkedList<>();
        LinkedList<BaseBadnessCauseImport> badnessCauseImports = new LinkedList<>();

        for (int i = 0; i < baseBadnessCauseImports.size(); i++) {
            BaseBadnessCauseImport baseBadnessCauseImport = baseBadnessCauseImports.get(i);
            String badnessCauseCode = baseBadnessCauseImport.getBadnessCauseCode();


            if (StringUtils.isEmpty(
                    badnessCauseCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseBadnessCause.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("badnessCauseCode",badnessCauseCode);
            if (StringUtils.isNotEmpty(baseBadnessCauseMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(badnessCauseImports)){
                for (BaseBadnessCauseImport badnessCauseImport: badnessCauseImports) {
                    if (badnessCauseCode.equals(badnessCauseImport.getBadnessCauseCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            badnessCauseImports.add(baseBadnessCauseImport);
        }

        if (StringUtils.isNotEmpty(badnessCauseImports)){

            for (BaseBadnessCauseImport baseBadnessCauseImport : badnessCauseImports) {
                BaseBadnessCause baseBadnessCause = new BaseBadnessCause();
                BeanUtils.copyProperties(baseBadnessCauseImport,baseBadnessCause);
                baseBadnessCause.setCreateTime(new Date());
                baseBadnessCause.setCreateUserId(currentUser.getUserId());
                baseBadnessCause.setModifiedTime(new Date());
                baseBadnessCause.setModifiedUserId(currentUser.getUserId());
                baseBadnessCause.setOrgId(currentUser.getOrganizationId());
                baseBadnessCause.setStatus((byte)1);
                list.add(baseBadnessCause);
            }
            success = baseBadnessCauseMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseBadnessCause baseBadnessCause : list) {
                    BaseHtBadnessCause baseHtBadnessCause = new BaseHtBadnessCause();
                    BeanUtils.copyProperties(baseBadnessCause, baseHtBadnessCause);
                    htList.add(baseHtBadnessCause);
                }
                baseHtBadnessCauseMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int saveByApi(BaseBadnessCause baseBadnessCause) {
        Example example = new Example(BaseBadnessCause.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("badnessCauseCode", baseBadnessCause.getBadnessCauseCode());
        criteria.andEqualTo("orgId", baseBadnessCause.getOrgId());
       // baseBadnessCauseMapper.deleteByExample(example);
        BaseBadnessCause cause = baseBadnessCauseMapper.selectOneByExample(example);
        int i = 0;
        if(StringUtils.isEmpty(cause)){
            baseBadnessCause.setCreateTime(new Date());
            i =  baseBadnessCauseMapper.insertSelective(baseBadnessCause);
        }else{
            baseBadnessCause.setBadnessCauseId(cause.getBadnessCauseId());
            i =  baseBadnessCauseMapper.updateByPrimaryKeySelective(baseBadnessCause);
        }
        return  i;
    }

}

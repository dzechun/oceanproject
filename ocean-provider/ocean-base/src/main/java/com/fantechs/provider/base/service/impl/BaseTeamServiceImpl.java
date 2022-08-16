package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseTeamImport;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShop;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtTeamMapper;
import com.fantechs.provider.base.mapper.BaseTeamMapper;
import com.fantechs.provider.base.mapper.BaseWorkShopMapper;
import com.fantechs.provider.base.service.BaseTeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BaseTeamServiceImpl  extends BaseService<BaseTeam> implements BaseTeamService {

    @Resource
    private BaseTeamMapper baseTeamMapper;
    @Resource
    private BaseHtTeamMapper baseHtTeamMapper;
    @Resource
    private BaseWorkShopMapper baseWorkShopMapper;

    @Override
    public List<BaseTeamDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseTeamMapper.findList(map);
    }

    @Override
    public int save(BaseTeam baseTeam) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseTeam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("teamCode",baseTeam.getTeamCode());
        BaseTeam baseTeam1 = baseTeamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseTeam1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseTeam.setCreateTime(new Date());
        baseTeam.setCreateUserId(user.getUserId());
        baseTeam.setModifiedTime(new Date());
        baseTeam.setModifiedUserId(user.getUserId());
        baseTeam.setOrganizationId(user.getOrganizationId());
        int i = baseTeamMapper.insertUseGeneratedKeys(baseTeam);

        BaseHtTeam baseHtTeam = new BaseHtTeam();
        BeanUtils.copyProperties(baseTeam,baseHtTeam);
        baseHtTeamMapper.insertSelective(baseHtTeam);
        return i;
    }

    @Override
    public int update(BaseTeam baseTeam) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseTeam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("teamCode",baseTeam.getTeamCode())
                .andNotEqualTo("teamId",baseTeam.getTeamId());
        BaseTeam baseTeam1 = baseTeamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseTeam1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseTeam.setModifiedTime(new Date());
        baseTeam.setModifiedUserId(user.getUserId());
        baseTeam.setOrganizationId(user.getOrganizationId());

        BaseHtTeam baseHtTeam = new BaseHtTeam();
        BeanUtils.copyProperties(baseTeam,baseHtTeam);
        baseHtTeamMapper.insertSelective(baseHtTeam);

        return baseTeamMapper.updateByPrimaryKeySelective(baseTeam);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtTeam> baseHtTeams = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseTeam baseTeam = baseTeamMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseTeam)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtTeam baseHtTeam = new BaseHtTeam();
            BeanUtils.copyProperties(baseTeam,baseHtTeam);
            baseHtTeams.add(baseHtTeam);
        }

        baseHtTeamMapper.insertList(baseHtTeams);
        return baseTeamMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseTeamImport> baseTeamImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseTeam> list = new LinkedList<>();
        LinkedList<BaseHtTeam> htList = new LinkedList<>();
        ArrayList<BaseTeamImport> baseTeamImportList = new ArrayList<>();
        for (int i = 0; i < baseTeamImports.size(); i++) {
            BaseTeamImport baseTeamImport = baseTeamImports.get(i);
            String teamCode = baseTeamImport.getTeamCode();
            String teamName = baseTeamImport.getTeamName();
            String workShopCode = baseTeamImport.getWorkShopCode();
            if (StringUtils.isEmpty(
                    teamCode,teamName,workShopCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseTeam.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("teamCode",teamCode);
            if (StringUtils.isNotEmpty(baseTeamMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断车间是否存在
            SearchBaseWorkShop searchBaseWorkShop = new SearchBaseWorkShop();
            searchBaseWorkShop.setWorkShopCode(workShopCode);
            searchBaseWorkShop.setCodeQueryMark(1);
            List<BaseWorkShopDto> smtWorkShopDtos = baseWorkShopMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShop));
            if (StringUtils.isEmpty(smtWorkShopDtos)){
                fail.add(i+4);
                continue;
            }
            baseTeamImport.setWorkShopId(smtWorkShopDtos.get(0).getWorkShopId());

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(baseTeamImportList)){
                for (BaseTeamImport teamImport : baseTeamImportList) {
                    if (teamImport.getTeamCode().equals(teamCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            baseTeamImportList.add(baseTeamImport);
        }

        if (StringUtils.isNotEmpty(baseTeamImportList)){
            for (BaseTeamImport baseTeamImport : baseTeamImportList) {
                BaseTeam baseTeam = new BaseTeam();
                BeanUtils.copyProperties(baseTeamImport,baseTeam);
                baseTeam.setCreateTime(new Date());
                baseTeam.setCreateUserId(currentUser.getUserId());
                baseTeam.setModifiedTime(new Date());
                baseTeam.setModifiedUserId(currentUser.getUserId());
                baseTeam.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseTeam);
            }
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseTeamMapper.insertList(list);

            for (BaseTeam baseTeam : list) {
                BaseHtTeam baseHtTeam = new BaseHtTeam();
                BeanUtils.copyProperties(baseTeam,baseHtTeam);
                htList.add(baseHtTeam);
            }

            if (StringUtils.isNotEmpty(htList)){
                baseHtTeamMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

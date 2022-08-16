package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShopImport;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShop;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTeam;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseWorkShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
public class BaseWorkShopServiceImpl extends BaseService<BaseWorkShop> implements BaseWorkShopService {

    @Resource
    private BaseWorkShopMapper baseWorkShopMapper;
    @Resource
    private BaseHtWorkShopMapper baseHtWorkShopMapper;
    @Resource
    private BaseProLineMapper baseProLineMapper;
    @Resource
    private BaseFactoryMapper baseFactoryMapper;
    @Resource
    private BaseTeamMapper baseTeamMapper;

    @Override
    public List<BaseWorkShopDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseWorkShopMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWorkShop baseWorkShop) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("workShopCode", baseWorkShop.getWorkShopCode());
        BaseWorkShop odlBaseWorkShop = baseWorkShopMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(odlBaseWorkShop)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseWorkShop.setCreateUserId(user.getUserId());
        baseWorkShop.setCreateTime(new Date());
        baseWorkShop.setModifiedUserId(user.getUserId());
        baseWorkShop.setModifiedTime(new Date());
        baseWorkShop.setStatus(StringUtils.isEmpty(baseWorkShop.getStatus())?1: baseWorkShop.getStatus());
        baseWorkShop.setOrganizationId(user.getOrganizationId());
        baseWorkShopMapper.insertUseGeneratedKeys(baseWorkShop);

        BaseHtWorkShop baseHtWorkShop = new BaseHtWorkShop();
        BeanUtils.copyProperties(baseWorkShop, baseHtWorkShop);
        int i = baseHtWorkShopMapper.insertSelective(baseHtWorkShop);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<BaseHtWorkShop> list=new LinkedList<>();
        String[] idsArr =  ids.split(",");
        for(String id : idsArr){
            BaseWorkShop baseWorkShop = baseWorkShopMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseWorkShop)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被生产线引用
            Example example = new Example(BaseProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workShopId", baseWorkShop.getWorkShopId());
            List<BaseProLine> baseProLines = baseProLineMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProLines)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //被班组引用
            SearchBaseTeam searchBaseTeam = new SearchBaseTeam();
            searchBaseTeam.setWorkShopId(Long.valueOf(id));
            List<BaseTeamDto> baseTeamDtos = baseTeamMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseTeam));
            if (StringUtils.isNotEmpty(baseTeamDtos)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            BaseHtWorkShop baseHtWorkShop = new BaseHtWorkShop();
            BeanUtils.copyProperties(baseWorkShop, baseHtWorkShop);
            baseHtWorkShop.setModifiedUserId(currentUser.getUserId());
            baseHtWorkShop.setModifiedTime(new Date());
            list.add(baseHtWorkShop);
        }
         baseHtWorkShopMapper.insertList(list);
        return baseWorkShopMapper.deleteByIds(ids);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWorkShop baseWorkShop) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", user.getOrganizationId());
        criteria.andEqualTo("workShopCode", baseWorkShop.getWorkShopCode());

        BaseWorkShop odlsmtWorkShop = baseWorkShopMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtWorkShop)&&!odlsmtWorkShop.getWorkShopId().equals(baseWorkShop.getWorkShopId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseWorkShop.setModifiedTime(new Date());
        baseWorkShop.setModifiedUserId(user.getUserId());
        baseWorkShop.setOrganizationId(user.getOrganizationId());

        BaseHtWorkShop baseHtWorkShop = new BaseHtWorkShop();
        BeanUtils.copyProperties(baseWorkShop, baseHtWorkShop);

        baseHtWorkShopMapper.insertSelective(baseHtWorkShop);

        return baseWorkShopMapper.updateByPrimaryKeySelective(baseWorkShop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWorkShopImport> baseWorkShopImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWorkShop> list = new LinkedList<>();
        LinkedList<BaseHtWorkShop> htList = new LinkedList<>();
        ArrayList<BaseWorkShopImport> workShopImportList = new ArrayList<>();
        for (int i = 0; i < baseWorkShopImports.size(); i++) {
            BaseWorkShopImport baseWorkShopImport = baseWorkShopImports.get(i);
            String workShopCode = baseWorkShopImport.getWorkShopCode();
            String workShopName = baseWorkShopImport.getWorkShopName();
            String factoryCode = baseWorkShopImport.getFactoryCode();
            if (StringUtils.isEmpty(
                    workShopCode,workShopName,factoryCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWorkShop.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("workShopCode",workShopCode);
            if (StringUtils.isNotEmpty(baseWorkShopMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断工厂是否存在
            Example example1 = new Example(BaseFactory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("factoryCode",factoryCode);
            BaseFactory baseFactory = baseFactoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseFactory)){
                fail.add(i+4);
                continue;
            }
            baseWorkShopImport.setFactoryId(baseFactory.getFactoryId());


            //判断集合中是否存在该车间
            boolean tag = false;
            if (StringUtils.isNotEmpty(workShopImportList)){
                for (BaseWorkShopImport workShopImport : workShopImportList) {
                    if (workShopImport.getWorkShopCode().equals(workShopCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }
            workShopImportList.add(baseWorkShopImport);
        }

        for (BaseWorkShopImport baseWorkShopImport : workShopImportList) {
            BaseWorkShop baseWorkShop = new BaseWorkShop();
            BeanUtils.copyProperties(baseWorkShopImport, baseWorkShop);
            baseWorkShop.setStatus(1);
            baseWorkShop.setCreateTime(new Date());
            baseWorkShop.setCreateUserId(currentUser.getUserId());
            baseWorkShop.setModifiedTime(new Date());
            baseWorkShop.setModifiedUserId(currentUser.getUserId());
            baseWorkShop.setOrganizationId(currentUser.getOrganizationId());
            list.add(baseWorkShop);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseWorkShopMapper.insertList(list);
        }

        for (BaseWorkShop baseWorkShop : list) {
            BaseHtWorkShop baseHtWorkShop = new BaseHtWorkShop();
            BeanUtils.copyProperties(baseWorkShop, baseHtWorkShop);
            htList.add(baseHtWorkShop);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtWorkShopMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    @Override
    public List<BaseWorkShop> batchAdd(List<BaseWorkShop> baseWorkShops ) {
        List<BaseWorkShop> ins = new ArrayList<BaseWorkShop>();
        List<BaseHtWorkShop> baseHtWorkShops = new ArrayList<BaseHtWorkShop>();

        for(BaseWorkShop baseWorkShop : baseWorkShops) {
            Example example = new Example(BaseWorkShop.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", baseWorkShop.getOrganizationId());
            criteria.andEqualTo("workShopCode", baseWorkShop.getWorkShopCode());
            BaseWorkShop oldWorkShop = baseWorkShopMapper.selectOneByExample(example);

            if (StringUtils.isNotEmpty(oldWorkShop)) {
                baseWorkShop.setWorkShopId(oldWorkShop.getWorkShopId());
                baseWorkShopMapper.updateByPrimaryKey(baseWorkShop);
            }else{
                ins.add(baseWorkShop);
                BaseHtWorkShop baseHtWorkShop =new BaseHtWorkShop();
                BeanUtils.copyProperties(baseWorkShop, baseHtWorkShop);
                baseHtWorkShops.add(baseHtWorkShop);
            }

        }
        if(StringUtils.isNotEmpty(ins)) {
            int i = baseWorkShopMapper.insertList(ins);
        }
        //新增车间历史信息
        if(StringUtils.isNotEmpty(baseHtWorkShops))
            baseHtWorkShopMapper.insertList(baseHtWorkShops);
        return ins;
    }
}

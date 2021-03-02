package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.imports.SmtFactoryImport;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.SmtDeptMapper;
import com.fantechs.provider.imes.basic.mapper.SmtFactoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtHtFactoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtFactoryService;
import com.sun.jersey.spi.container.ResourceFilters;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.execute.Execute;
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
@Slf4j
public class SmtFactoryServiceImpl extends BaseService<SmtFactory> implements SmtFactoryService {
    @Resource
    private SmtFactoryMapper smtFactoryMapper;
    @Resource
    private SmtHtFactoryMapper smtHtFactoryMapper;
    @Resource
    private SmtDeptMapper smtDeptMapper;
    @Resource
    private SmtWorkShopMapper smtWorkShopMapper;
    @Resource
    private BaseFeignApi baseFeignApi;


    @Override
    public List<SmtFactoryDto> findList(Map<String, Object> map) {
        return smtFactoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtFactory smtFactory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtFactory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("factoryCode", smtFactory.getFactoryCode());

        SmtFactory odlsmtFactory = smtFactoryMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtFactory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtFactory.setCreateUserId(user.getUserId());
        smtFactory.setCreateTime(new Date());
        smtFactory.setModifiedUserId(user.getUserId());
        smtFactory.setModifiedTime(new Date());
        smtFactory.setStatus(StringUtils.isEmpty(smtFactory.getStatus())?1:smtFactory.getStatus());
        int i = smtFactoryMapper.insertUseGeneratedKeys(smtFactory);

        SmtHtFactory smtHtFactory  = new SmtHtFactory();
        BeanUtils.copyProperties(smtFactory,smtHtFactory);
        smtHtFactoryMapper.insert(smtHtFactory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete (String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtHtFactory>  smtHtFactorys = new LinkedList<>();
        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            SmtFactory smtFactory = smtFactoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtFactory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被部门应用
            Long factoryId = smtFactory.getFactoryId();
            Example example = new Example(SmtDept.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("factoryId",factoryId);
            List<SmtDept> smtDepts = smtDeptMapper.selectByExample(example);

            //被车间引用
            Example example1 = new Example(SmtWorkShop.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("factoryId",factoryId);
            List<SmtWorkShop> smtWorkShops = smtWorkShopMapper.selectByExample(example1);
            if(StringUtils.isNotEmpty(smtDepts)||StringUtils.isNotEmpty(smtWorkShops)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            SmtHtFactory smtHtFactory  = new SmtHtFactory();
            BeanUtils.copyProperties(smtFactory,smtHtFactory);

            smtHtFactory.setModifiedTime(new Date());
            smtHtFactory.setModifiedUserId(user.getUserId());
            smtHtFactory.setCreateTime(new Date());
            smtHtFactory.setCreateUserId(user.getUserId());
            smtHtFactorys.add(smtHtFactory);

        }
        smtHtFactoryMapper.insertList(smtHtFactorys);
        return smtFactoryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtFactory smtFactory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtFactory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("factoryCode", smtFactory.getFactoryCode());

        SmtFactory odlsmtFactory = smtFactoryMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtFactory)&&!odlsmtFactory.getFactoryId().equals(smtFactory.getFactoryId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtFactory.setModifiedTime(new Date());
        smtFactory.setModifiedUserId(user.getUserId());

        SmtHtFactory smtHtFactory  = new SmtHtFactory();
        BeanUtils.copyProperties(smtFactory,smtHtFactory);

        smtHtFactoryMapper.insert(smtHtFactory);

        return smtFactoryMapper.updateByPrimaryKeySelective(smtFactory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtFactoryImport> smtFactoryImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtFactory> list = new LinkedList<>();
        LinkedList<SmtHtFactory> htList = new LinkedList<>();
        for (int i = 0; i < smtFactoryImports.size(); i++) {
            SmtFactoryImport smtFactoryImport = smtFactoryImports.get(i);

            String factoryCode = smtFactoryImport.getFactoryCode();
            String factoryName = smtFactoryImport.getFactoryName();
            String organizationCode = smtFactoryImport.getOrganizationCode();
            if (StringUtils.isEmpty(
                    factoryCode,factoryName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("factoryCode",factoryCode);
            if (StringUtils.isNotEmpty(smtFactoryMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //如果组织编码不为空，判断组织信息是否存在
            if (StringUtils.isNotEmpty(organizationCode)){
                SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
                searchBaseOrganization.setOrganizationCode(organizationCode);
                searchBaseOrganization.setCodeQueryMark(1);
                List<BaseOrganizationDto> baseOrganizationDtos = baseFeignApi.findOrganizationList(searchBaseOrganization).getData();
                if (StringUtils.isEmpty(baseOrganizationDtos)){
                    fail.add(i+4);
                    continue;
                }
                smtFactoryImport.setOrganizationId(baseOrganizationDtos.get(0).getOrganizationId());
            }

            //判断集合中是否已经存在同样的数据
            if (StringUtils.isNotEmpty(list)){
                for (SmtFactory smtFactory : list) {
                    if (smtFactory.getFactoryCode().equals(factoryCode)){
                        fail.add(i+4);
                        continue;
                    }
                }
            }


            SmtFactory smtFactory = new SmtFactory();
            BeanUtils.copyProperties(smtFactoryImport,smtFactory);
            smtFactory.setCreateTime(new Date());
            smtFactory.setCreateUserId(currentUser.getUserId());
            smtFactory.setModifiedTime(new Date());
            smtFactory.setModifiedUserId(currentUser.getUserId());
            smtFactory.setStatus(1);
            list.add(smtFactory);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtFactoryMapper.insertList(list);
        }

        for (SmtFactory smtFactory : list) {
            SmtHtFactory smtHtFactory = new SmtHtFactory();
            BeanUtils.copyProperties(smtFactory,smtHtFactory);
            htList.add(smtHtFactory);
        }
        if (StringUtils.isNotEmpty(htList)){
            smtHtFactoryMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}

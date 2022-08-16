package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtRoute;
import com.fantechs.common.base.general.dto.basic.imports.BaseRouteImport;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.ClassCompareUtil;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProductProcessRouteService;
import com.fantechs.provider.base.service.BaseRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class BaseRouteServiceImpl extends BaseService<BaseRoute> implements BaseRouteService {

      @Resource
      private BaseRouteMapper baseRouteMapper;
      @Resource
      private BaseHtRouteMapper baseHtRouteMapper;
      @Resource
      private BaseRouteProcessMapper baseRouteProcessMapper;
      @Resource
      private BaseProductProcessRouteService baseProductProcessRouteService;
      @Resource
      private BaseProcessCategoryMapper baseProcessCategoryMapper;
      @Resource
      private BaseProcessMapper baseProcessMapper;

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int save(BaseRoute baseRoute) {
          SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

          Example example = new Example(BaseRoute.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
          criteria.andEqualTo("routeName", baseRoute.getRouteName())
                  .orEqualTo("routeCode", baseRoute.getRouteCode());
          List<BaseRoute> baseRoutes = baseRouteMapper.selectByExample(example);
          if(StringUtils.isNotEmpty(baseRoutes)){
              throw new BizErrorException("工艺路线名称或编码已存在");
          }

          baseRoute.setCreateUserId(currentUser.getUserId());
          baseRoute.setCreateTime(new Date());
          baseRoute.setModifiedUserId(currentUser.getUserId());
          baseRoute.setModifiedTime(new Date());
          baseRoute.setOrganizationId(currentUser.getOrganizationId());
          baseRouteMapper.insertUseGeneratedKeys(baseRoute);

          //新增工艺路线历史信息
          BaseHtRoute baseHtRoute =new BaseHtRoute();
          BeanUtils.copyProperties(baseRoute, baseHtRoute);
          int i = baseHtRouteMapper.insertSelective(baseHtRoute);
          return i;
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BaseRoute baseRoute) {
          SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

          Example example = new Example(BaseRoute.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
          criteria.andEqualTo("routeName", baseRoute.getRouteName())
                  .orEqualTo("routeCode", baseRoute.getRouteCode());

          BaseRoute route = baseRouteMapper.selectOneByExample(example);

          if(StringUtils.isNotEmpty(route)&&!route.getRouteId().equals(baseRoute.getRouteId())){
              throw new BizErrorException("工艺路线名称或编码已存在");
          }

          baseRoute.setModifiedUserId(currentUser.getUserId());
          baseRoute.setModifiedTime(new Date());
          baseRoute.setOrganizationId(currentUser.getOrganizationId());
          int i= baseRouteMapper.updateByPrimaryKeySelective(baseRoute);

          //新增工艺路线历史信息
          BaseHtRoute baseHtRoute =new BaseHtRoute();
          BeanUtils.copyProperties(baseRoute, baseHtRoute);
          baseHtRouteMapper.insertSelective(baseHtRoute);
          return i;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public int batchDelete(String ids) {
         int i=0;
         List<BaseHtRoute> list=new ArrayList<>();

         SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

         String[] routeIds = ids.split(",");
         for (String routeId : routeIds) {
             BaseRoute baseRoute = baseRouteMapper.selectByPrimaryKey(Long.parseLong(routeId));
             if(StringUtils.isEmpty(baseRoute)){
                 throw new BizErrorException(ErrorCodeEnum.OPT20012003);
             }
             //新增工艺路线历史信息
             BaseHtRoute baseHtRoute =new BaseHtRoute();
             BeanUtils.copyProperties(baseRoute, baseHtRoute);
             baseHtRoute.setModifiedUserId(currentUser.getUserId());
             baseHtRoute.setModifiedTime(new Date());
             list.add(baseHtRoute);

             //删除工艺路线和工序的绑定关系
             Example example = new Example(BaseRouteProcess.class);
             Example.Criteria criteria = example.createCriteria();
             criteria.andEqualTo("routeId",routeId);
             baseRouteProcessMapper.deleteByExample(example);
         }
         baseHtRouteMapper.insertList(list);

         return baseRouteMapper.deleteByIds(ids);
     }

    @Override
    public List<BaseRoute> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseRouteMapper.findList(map);
    }

    @Override
    public int addOrUpdateRoute(BaseRoute baseRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        BaseRoute baseRoute1 = baseRouteMapper.selectByPrimaryKey(baseRoute.getRouteId());
        if (ClassCompareUtil.compareObject(baseRoute, baseRoute1)){
            //两个对象相同不做任何操作
            return 1;
        }else {
            //两个对象不同做新增操作
            baseRoute.setRouteId(null);
            int i = baseRouteMapper.insertUseGeneratedKeys(baseRoute);

            //更新产品工艺路线绑定的工艺路线
            BaseProductProcessRoute baseProductProcessRoute = baseRoute.getBaseProductProcessRoute();
            baseProductProcessRoute.setRouteId(baseRoute.getRouteId());
            baseProductProcessRoute.setOrganizationId(currentUser.getOrganizationId());
            baseProductProcessRouteService.update(baseProductProcessRoute);
            return i;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int configureRout(BaseRoute baseRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        // 删除上一次配置的工艺路线
        Example example = new Example(BaseRouteProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("routeId", baseRoute.getRouteId());
        baseRouteProcessMapper.deleteByExample(example);

        //已检验工序
        List<Long> processIds= new ArrayList<>();
        List<BaseRouteProcess> baseRouteProcesses = baseRoute.getBaseRouteProcesses();
        if (StringUtils.isNotEmpty(baseRouteProcesses)){
            for(BaseRouteProcess baseRouteProcess : baseRouteProcesses){
                //当前工序
                SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
                searchBaseProcess.setProcessId(baseRouteProcess.getProcessId());
                BaseProcess baseProcess = baseProcessMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcess)).get(0);
                //是否维修工序
                if("repair".equals(baseProcess.getProcessCategoryCode())){
                    if(!processIds.contains(baseRouteProcess.getNextProcessId())){
                        throw new BizErrorException("维修工序返回工序有误");
                    }
                }else {
                    processIds.add(baseRouteProcess.getProcessId());
                }
            }
            for (int i = 0; i < baseRouteProcesses.size(); i++) {
                baseRouteProcesses.get(i).setOrderNum(i);
            }
            baseRouteProcessMapper.insertList(baseRouteProcesses);
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseRouteImport> baseRouteImports) throws ParseException {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<BaseRouteImport> iterator = baseRouteImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            BaseRouteImport baseRouteImport = iterator.next();
            String routeCode = baseRouteImport.getRouteCode();
            String routeName = baseRouteImport.getRouteName();
            String processName = baseRouteImport.getProcessName();
            Integer orderNum = baseRouteImport.getOrderNum();

            //判断必传字段
            if (StringUtils.isEmpty(
                    routeCode,routeName,processName,orderNum
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断工艺路线编码或名称是否重复
            Example example = new Example(BaseRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeCode",routeCode)
                    .orEqualTo("routeName",routeName);
            List<BaseRoute> baseRoutes = baseRouteMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseRoutes)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断工序信息是否存在
            Example example1 = new Example(BaseProcess.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processName",processName);
            BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseProcess)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }
            baseRouteImport.setProcessId(baseProcess.getProcessId());
            baseRouteImport.setSectionId(baseProcess.getSectionId());
            i++;
        }

        //对合格数据进行分组
        Map<String, List<BaseRouteImport>> map = baseRouteImports.stream().collect(Collectors.groupingBy(BaseRouteImport::getRouteCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<BaseRouteImport> baseRouteImports1 = map.get(code);
            //新增工艺路线信息
            BaseRoute baseRoute = new BaseRoute();
            BeanUtils.copyProperties(baseRouteImports1.get(0), baseRoute);
            baseRoute.setCreateTime(new Date());
            baseRoute.setCreateUserId(currentUser.getUserId());
            baseRoute.setModifiedTime(new Date());
            baseRoute.setModifiedUserId(currentUser.getUserId());
            baseRoute.setStatus(1);
            baseRoute.setOrganizationId(currentUser.getOrganizationId());
            baseRouteMapper.insertUseGeneratedKeys(baseRoute);

            //新增工艺路线历史信息
            BaseHtRoute baseHtRoute =new BaseHtRoute();
            BeanUtils.copyProperties(baseRoute, baseHtRoute);
            baseHtRouteMapper.insertSelective(baseHtRoute);

            for (BaseRouteImport baseRouteImport : baseRouteImports1) {
                BaseRouteProcess baseRouteProcess = new BaseRouteProcess();
                BeanUtils.copyProperties(baseRouteImport, baseRouteProcess);
                baseRouteProcess.setRouteId(baseRoute.getRouteId());
                baseRouteProcess.setStandardTime(baseRouteImport.getStandardTime());
                baseRouteProcess.setOrganizationId(currentUser.getOrganizationId());
                baseRouteProcess.setReadinessTime(baseRouteImport.getReadinessTime());

                success += baseRouteProcessMapper.insertSelective(baseRouteProcess);
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

    @Override
    public BaseRoute addOrUpdate (BaseRoute baseRoute) {

        Example example = new Example(BaseRoute.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("routeCode", baseRoute.getRouteCode());
        criteria.andEqualTo("organizationId", baseRoute.getOrganizationId());
        List<BaseRoute> baseRouteOld = baseRouteMapper.selectByExample(example);

        baseRoute.setModifiedTime(new Date());
        if (StringUtils.isNotEmpty(baseRouteOld)){
            baseRoute.setRouteId(baseRouteOld.get(0).getRouteId());
            baseRouteMapper.updateByPrimaryKey(baseRoute);
        }else{
            baseRoute.setCreateTime(new Date());
            baseRouteMapper.insertUseGeneratedKeys(baseRoute);
        }
        return baseRoute;
    }
}

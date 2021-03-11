package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.dto.basic.imports.SmtRouteImport;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.ClassCompareUtil;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtProductProcessRouteService;
import com.fantechs.provider.imes.basic.service.SmtRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtRouteServiceImpl extends BaseService<SmtRoute> implements SmtRouteService {

      @Resource
      private SmtRouteMapper smtRouteMapper;
      @Resource
      private SmtHtRouteMapper smtHtRouteMapper;
      @Resource
      private SmtRouteProcessMapper smtRouteProcessMapper;
      @Resource
      private SmtProductProcessRouteService smtProductProcessRouteService;
      @Resource
      private SmtProcessCategoryMapper smtProcessCategoryMapper;
      @Resource
      private SmtProcessMapper smtProcessMapper;
      @Resource
      private BaseFeignApi baseFeignApi;

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int save(SmtRoute smtRoute) {
          SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(currentUser)){
              throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }

          Example example = new Example(SmtRoute.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("routeName",smtRoute.getRouteName());
          List<SmtRoute> smtRoutes = smtRouteMapper.selectByExample(example);
          if(StringUtils.isNotEmpty(smtRoutes)){
              throw new BizErrorException("工艺路线名称已存在");
          }

          smtRoute.setCreateUserId(currentUser.getUserId());
          smtRoute.setCreateTime(new Date());
          smtRoute.setModifiedUserId(currentUser.getUserId());
          smtRoute.setModifiedTime(new Date());
          smtRouteMapper.insertUseGeneratedKeys(smtRoute);

          //新增工艺路线历史信息
          SmtHtRoute smtHtRoute=new SmtHtRoute();
          BeanUtils.copyProperties(smtRoute,smtHtRoute);
          int i = smtHtRouteMapper.insertSelective(smtHtRoute);
          return i;
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(SmtRoute smtRoute) {
          SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(currentUser)){
              throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }

          Example example = new Example(SmtRoute.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("routeName",smtRoute.getRouteName());

          SmtRoute route = smtRouteMapper.selectOneByExample(example);

          if(StringUtils.isNotEmpty(route)&&!route.getRouteId().equals(smtRoute.getRouteId())){
              throw new BizErrorException("工艺路线名称已存在");
          }

          smtRoute.setModifiedUserId(currentUser.getUserId());
          smtRoute.setModifiedTime(new Date());
          int i= smtRouteMapper.updateByPrimaryKeySelective(smtRoute);

          //新增工艺路线历史信息
          SmtHtRoute smtHtRoute=new SmtHtRoute();
          BeanUtils.copyProperties(smtRoute,smtHtRoute);
          smtHtRouteMapper.insertSelective(smtHtRoute);
          return i;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public int batchDelete(String ids) {
         int i=0;
         List<SmtHtRoute> list=new ArrayList<>();

         SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
         if(StringUtils.isEmpty(currentUser)){
             throw new BizErrorException(ErrorCodeEnum.UAC10011039);
         }

         String[] routeIds = ids.split(",");
         for (String routeId : routeIds) {
             SmtRoute smtRoute = smtRouteMapper.selectByPrimaryKey(Long.parseLong(routeId));
             if(StringUtils.isEmpty(smtRoute)){
                 throw new BizErrorException(ErrorCodeEnum.OPT20012003);
             }
             //新增工艺路线历史信息
             SmtHtRoute smtHtRoute=new SmtHtRoute();
             BeanUtils.copyProperties(smtRoute,smtHtRoute);
             smtHtRoute.setModifiedUserId(currentUser.getUserId());
             smtHtRoute.setModifiedTime(new Date());
             list.add(smtHtRoute);

             //删除工艺路线和工序的绑定关系
             Example example = new Example(SmtRouteProcess.class);
             Example.Criteria criteria = example.createCriteria();
             criteria.andEqualTo("routeId",routeId);
             smtRouteProcessMapper.deleteByExample(example);
         }
         smtHtRouteMapper.insertList(list);

         return smtRouteMapper.deleteByIds(ids);
     }

    @Override
    public List<SmtRoute> findList(SearchSmtRoute searchSmtRoute) {
        return smtRouteMapper.findList(searchSmtRoute);
    }

    @Override
    public int addOrUpdateRoute(SmtRoute smtRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        SmtRoute smtRoute1 = smtRouteMapper.selectByPrimaryKey(smtRoute.getRouteId());
        if (ClassCompareUtil.compareObject(smtRoute,smtRoute1)){
            //两个对象相同不做任何操作
            return 1;
        }else {
            //两个对象不同做新增操作
            smtRoute.setRouteId(null);
            int i = smtRouteMapper.insertUseGeneratedKeys(smtRoute);

            //更新产品工艺路线绑定的工艺路线
            SmtProductProcessRoute smtProductProcessRoute = smtRoute.getSmtProductProcessRoute();
            smtProductProcessRoute.setRouteId(smtRoute.getRouteId());
            smtProductProcessRouteService.update(smtProductProcessRoute);
            return i;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int configureRout(SmtRoute smtRoute) {

        // 删除上一次配置的工艺路线
        Example example = new Example(SmtRouteProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("routeId",smtRoute.getRouteId());
        smtRouteProcessMapper.deleteByExample(example);

        //获取维修工序的对象
        example =  new Example(SmtProcessCategory.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("processCategoryCode","repair");
        SmtProcessCategory smtProcessCategory= smtProcessCategoryMapper.selectOneByExample(example);

        //已检验工序
        List<Long> processIds= new ArrayList<>();
        List<SmtRouteProcess> smtRouteProcesses = smtRoute.getSmtRouteProcesses();
        if (StringUtils.isNotEmpty(smtRouteProcesses)){
            for(SmtRouteProcess smtRouteProcess :smtRouteProcesses){
                //当前工序
                SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(smtRouteProcess.getProcessId());
                //是否维修工序
                if(smtProcess.getProcessCategoryId()==smtProcessCategory.getProcessCategoryId()){
                    if(!processIds.contains(smtRouteProcess.getNextProcessId())){
                        throw new BizErrorException("维修工序返回工序有误");
                    }
                }else {
                    processIds.add(smtRouteProcess.getProcessId());
                }
            }
            for (int i = 0; i < smtRouteProcesses.size(); i++) {
                smtRouteProcesses.get(i).setOrderNum(i);
            }
            smtRouteProcessMapper.insertList(smtRouteProcesses);
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtRouteImport> smtRouteImports) throws ParseException {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<SmtRouteImport> iterator = smtRouteImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            SmtRouteImport smtRouteImport = iterator.next();
            String routeCode = smtRouteImport.getRouteCode();
            String routeName = smtRouteImport.getRouteName();
            String processCode = smtRouteImport.getProcessCode();
            Integer orderNum = smtRouteImport.getOrderNum();

            //判断必传字段
            if (StringUtils.isEmpty(
                    routeCode,routeName,processCode,orderNum
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断工艺路线编码是否重复
            Example example = new Example(SmtRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeCode",routeCode);
            SmtRoute smtRoute = smtRouteMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(smtRoute)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断工序信息是否存在
            Example example1 = new Example(SmtProcess.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processCode",processCode);
            SmtProcess smtProcess = smtProcessMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtProcess)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }
            smtRouteImport.setProcessId(smtProcess.getProcessId());
            smtRouteImport.setSectionId(smtProcess.getSectionId());

            i++;
        }


        //对合格数据进行分组
        Map<String, List<SmtRouteImport>> map = smtRouteImports.stream().collect(Collectors.groupingBy(SmtRouteImport::getRouteCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<SmtRouteImport> smtRouteImports1 = map.get(code);
            //新增工艺路线信息
            SmtRoute smtRoute = new SmtRoute();
            BeanUtils.copyProperties(smtRouteImports1.get(0), smtRoute);
            smtRoute.setCreateTime(new Date());
            smtRoute.setCreateUserId(currentUser.getUserId());
            smtRoute.setModifiedTime(new Date());
            smtRoute.setModifiedUserId(currentUser.getUserId());
            smtRoute.setStatus(1);
            smtRouteMapper.insertUseGeneratedKeys(smtRoute);

            //新增工艺路线历史信息
            SmtHtRoute smtHtRoute=new SmtHtRoute();
            BeanUtils.copyProperties(smtRoute,smtHtRoute);
            smtHtRouteMapper.insertSelective(smtHtRoute);

            for (SmtRouteImport smtRouteImport : smtRouteImports1) {
                SmtRouteProcess smtRouteProcess = new SmtRouteProcess();
                BeanUtils.copyProperties(smtRouteImport,smtRouteProcess);
                smtRouteProcess.setRouteId(smtRoute.getRouteId());
                smtRouteProcess.setStandardTime(smtRouteImport.getStandardTime());
                smtRouteProcess.setReadinessTime(smtRouteImport.getReadinessTime());

                success += smtRouteProcessMapper.insertSelective(smtRouteProcess);
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
}

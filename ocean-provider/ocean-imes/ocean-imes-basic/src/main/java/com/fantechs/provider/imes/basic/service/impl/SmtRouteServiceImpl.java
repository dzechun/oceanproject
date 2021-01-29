package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.ClassCompareUtil;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtProductProcessRouteService;
import com.fantechs.provider.imes.basic.service.SmtRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

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
    public Map<String, Object> importExcel(List<SmtRoute> smtRoutes) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtRoute> list = new LinkedList<>();
        LinkedList<SmtHtRoute> htList = new LinkedList<>();
        for (int i = 0; i < smtRoutes.size(); i++) {
            SmtRoute smtRoute = smtRoutes.get(i);
            String routeCode = smtRoute.getRouteCode();
            String routeName = smtRoute.getRouteName();
            if (StringUtils.isEmpty(
                    routeCode,routeName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码和名称是否重复
            Example example = new Example(SmtRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeCode",routeCode)
                    .orEqualTo("routeName",routeCode);
            List<SmtRoute> smtRoutes1 = smtRouteMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtRoutes1)){
                fail.add(i+3);
                continue;
            }

            smtRoute.setCreateTime(new Date());
            smtRoute.setCreateUserId(currentUser.getUserId());
            smtRoute.setModifiedTime(new Date());
            smtRoute.setModifiedUserId(currentUser.getUserId());
            smtRoute.setStatus(1);
            list.add(smtRoute);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtRouteMapper.insertList(list);
        }

        for (SmtRoute smtRoute : list) {
            SmtHtRoute smtHtRoute = new SmtHtRoute();
            BeanUtils.copyProperties(smtRoute,smtHtRoute);
            htList.add(smtHtRoute);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtRouteMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}

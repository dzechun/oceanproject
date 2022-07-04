package com.fantechs.provider.base.service.impl;



import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProLineImport;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.history.BaseHtDept;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProLineService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BaseProLineServiceImpl extends BaseService<BaseProLine> implements BaseProLineService {

    @Resource
    private BaseProLineMapper baseProLineMapper;

    @Resource
    private BaseHtProLineMapper baseHtProLineMapper;

    @Resource
    private BaseProductProcessRouteMapper baseProductProcessRouteMapper;

    @Resource
    private BaseWorkShopMapper baseWorkShopMapper;


    @Override
    public List<BaseProLine> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseProLineMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProLine baseProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        int i=0;
        Example example = new Example(BaseProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("proCode", baseProLine.getProCode());
        List<BaseProLine> baseProLines = baseProLineMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProLines)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProLine.setCreateUserId(currentUser.getUserId());
        baseProLine.setCreateTime(new Date());
        baseProLine.setModifiedUserId(currentUser.getUserId());
        baseProLine.setModifiedTime(new Date());
        baseProLine.setOrganizationId(currentUser.getOrganizationId());
        baseProLine.setIsDelete((byte)1);
        baseProLineMapper.insertUseGeneratedKeys(baseProLine);

        //新增生产线历史信息
        BaseHtProLine baseHtProLine =new BaseHtProLine();
        BeanUtils.copyProperties(baseProLine, baseHtProLine);
        i = baseHtProLineMapper.insertSelective(baseHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProLine baseProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("proCode", baseProLine.getProCode());
        BaseProLine proLine = baseProLineMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(proLine)&&!proLine.getProLineId().equals(baseProLine.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProLine.setModifiedUserId(currentUser.getUserId());
        baseProLine.setModifiedTime(new Date());
        baseProLine.setOrganizationId(currentUser.getOrganizationId());
        int i= baseProLineMapper.updateByPrimaryKeySelective(baseProLine);

        //新增生产线历史信息
        BaseHtProLine baseHtProLine =new BaseHtProLine();
        BeanUtils.copyProperties(baseProLine, baseHtProLine);
        baseHtProLineMapper.insertSelective(baseHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<BaseHtProLine> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idsArr = ids.split(",");
        for (String proLineId : idsArr) {
            BaseProLine baseProLine = baseProLineMapper.selectByPrimaryKey(proLineId);
            if(StringUtils.isEmpty(baseProLine)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }


            //被产品工艺路线引用
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proLineId",proLineId);
            List<BaseProductProcessRoute> baseProductProcessRoutes = baseProductProcessRouteMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseProductProcessRoutes)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增生产线历史信息
            BaseHtProLine baseHtProLine =new BaseHtProLine();
            BeanUtils.copyProperties(baseProLine, baseHtProLine);
            baseHtProLine.setModifiedUserId(currentUser.getUserId());
            baseHtProLine.setModifiedTime(new Date());
            list.add(baseHtProLine);

        }
         baseHtProLineMapper.insertList(list);
         i= baseProLineMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProLineImport> baseProLineImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseProLine> list = new LinkedList<>();
        LinkedList<BaseHtProLine> htList = new LinkedList<>();
        ArrayList<BaseProLineImport> baseProLineImportList = new ArrayList<>();

        for (int i = 0; i < baseProLineImports.size(); i++) {
            BaseProLineImport baseProLineImport = baseProLineImports.get(i);
            String proCode = baseProLineImport.getProCode();
            String proName = baseProLineImport.getProName();
            String workShopCode = baseProLineImport.getWorkShopCode();
            if (StringUtils.isEmpty(
                    proCode,proName,workShopCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proCode",proCode);
            if (StringUtils.isNotEmpty(baseProLineMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断车间是否存在
            Example example2 = new Example(BaseWorkShop.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("workShopCode",workShopCode);
            BaseWorkShop baseWorkShop = baseWorkShopMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseWorkShop)){
                fail.add(i+4);
                continue;
            }
            baseProLineImport.setWorkShopId(baseWorkShop.getWorkShopId());
            baseProLineImport.setFactoryId(baseWorkShop.getFactoryId());

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(baseProLineImportList)){
                for (BaseProLineImport proLineImport : baseProLineImportList) {
                    if (proLineImport.getProCode().equals(proCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            baseProLineImportList.add(baseProLineImport);
        }

        if (StringUtils.isNotEmpty(baseProLineImportList)){

            for (BaseProLineImport baseProLineImport : baseProLineImportList) {
                BaseProLine baseProLine = new BaseProLine();
                BeanUtils.copyProperties(baseProLineImport, baseProLine);
                baseProLine.setCreateTime(new Date());
                baseProLine.setCreateUserId(currentUser.getUserId());
                baseProLine.setModifiedTime(new Date());
                baseProLine.setModifiedUserId(currentUser.getUserId());
                baseProLine.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseProLine);
            }

            success = baseProLineMapper.insertList(list);

            for (BaseProLine baseProLine : list) {
                BaseHtProLine baseHtProLine = new BaseHtProLine();
                BeanUtils.copyProperties(baseProLine, baseHtProLine);
                htList.add(baseHtProLine);
            }
             baseHtProLineMapper.insertList(htList);

        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }


    @Override
    public BaseProLine addOrUpdate(BaseProLine baseProLine) {

        Example example = new Example(BaseProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode", baseProLine.getProCode());
        List<BaseProLine> baseProLines = baseProLineMapper.selectByExample(example);

        baseProLine.setStatus(1);
        baseProLine.setModifiedTime(new Date());
        if (StringUtils.isNotEmpty(baseProLines)){
            baseProLine.setProLineId(baseProLines.get(0).getProLineId());
            baseProLineMapper.updateByPrimaryKey(baseProLine);
        }else{
            baseProLine.setCreateTime(new Date());
            baseProLineMapper.insertUseGeneratedKeys(baseProLine);
        }
        return baseProLine;
    }

    @Override
    public List<BaseProLine> batchAdd(List<BaseProLine> baseProLines ) {
        List<BaseProLine> ins = new ArrayList<BaseProLine>();
        List<BaseHtProLine> baseHtProLines = new ArrayList<BaseHtProLine>();

        for(BaseProLine baseProLine : baseProLines) {
            Example example = new Example(BaseProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", baseProLine.getOrganizationId());
            criteria.andEqualTo("proCode", baseProLine.getProCode());
            BaseProLine oldProLine = baseProLineMapper.selectOneByExample(example);
            logger.info("------------oldProLine--------------"+oldProLine);
            if (StringUtils.isNotEmpty(oldProLine)) {
                baseProLine.setProLineId(oldProLine.getProLineId());
                baseProLineMapper.updateByPrimaryKey(baseProLine);
            }else{
                ins.add(baseProLine);
                BaseHtProLine baseHtProLine =new BaseHtProLine();
                BeanUtils.copyProperties(baseProLine, baseHtProLine);
                baseHtProLines.add(baseHtProLine);
            }

        }
        logger.info("------------ins--------------"+ins.size());
        if(StringUtils.isNotEmpty(ins)) {
            int i = baseProLineMapper.insertList(ins);
        }
        //新增产线历史信息
        if(StringUtils.isNotEmpty(baseHtProLines))
            baseHtProLineMapper.insertList(baseHtProLines);
        return ins;
    }
}

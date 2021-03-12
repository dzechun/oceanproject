package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.imports.SmtProLineImport;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtProLineService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SmtProLineServiceImpl  extends BaseService<SmtProLine> implements SmtProLineService {

    @Resource
    private SmtProLineMapper smtProLineMapper;

    @Resource
    private SmtHtProLineMapper smtHtProLineMapper;

    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;

    @Resource
    private SmtWorkShopMapper smtWorkShopMapper;

    @Override
    public List<SmtProLine> findList(SearchSmtProLine searchSmtProLine) {
        return smtProLineMapper.findList(searchSmtProLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProLine smtProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i=0;
        Example example = new Example(SmtProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode",smtProLine.getProCode());
        List<SmtProLine> smtProLines = smtProLineMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtProLines)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProLine.setCreateUserId(currentUser.getUserId());
        smtProLine.setCreateTime(new Date());
        smtProLine.setModifiedUserId(currentUser.getUserId());
        smtProLine.setModifiedTime(new Date());
        smtProLineMapper.insertUseGeneratedKeys(smtProLine);

        //新增生产线历史信息
        SmtHtProLine smtHtProLine=new SmtHtProLine();
        BeanUtils.copyProperties(smtProLine,smtHtProLine);
        i = smtHtProLineMapper.insertSelective(smtHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProLine smtProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode",smtProLine.getProCode());
        SmtProLine proLine = smtProLineMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(proLine)&&!proLine.getProLineId().equals(smtProLine.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProLine.setModifiedUserId(currentUser.getUserId());
        smtProLine.setModifiedTime(new Date());
        int i= smtProLineMapper.updateByPrimaryKeySelective(smtProLine);

        //新增生产线历史信息
        SmtHtProLine smtHtProLine=new SmtHtProLine();
        BeanUtils.copyProperties(smtProLine,smtHtProLine);
        smtHtProLineMapper.insertSelective(smtHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtProLine> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String proLineId : idsArr) {
            SmtProLine smtProLine = smtProLineMapper.selectByPrimaryKey(proLineId);
            if(StringUtils.isEmpty(smtProLine)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }


            //被产品工艺路线引用
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proLineId",proLineId);
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductProcessRoutes)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增生产线历史信息
            SmtHtProLine smtHtProLine=new SmtHtProLine();
            BeanUtils.copyProperties(smtProLine,smtHtProLine);
            smtHtProLine.setModifiedUserId(currentUser.getUserId());
            smtHtProLine.setModifiedTime(new Date());
            list.add(smtHtProLine);

        }
         smtHtProLineMapper.insertList(list);
         i=smtProLineMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtProLineImport> smtProLineImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtProLine> list = new LinkedList<>();
        LinkedList<SmtHtProLine> htList = new LinkedList<>();
        ArrayList<SmtProLineImport> smtProLineImportList = new ArrayList<>();

        for (int i = 0; i < smtProLineImports.size(); i++) {
            SmtProLineImport smtProLineImport = smtProLineImports.get(i);
            String proCode = smtProLineImport.getProCode();
            String proName = smtProLineImport.getProName();
            String workShopCode = smtProLineImport.getWorkShopCode();
            if (StringUtils.isEmpty(
                    proCode,proName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proCode",proCode);
            if (StringUtils.isNotEmpty(smtProLineMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断车间是否存在
            if (StringUtils.isNotEmpty(workShopCode)){
                Example example2 = new Example(SmtWorkShop.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("workShopCode",workShopCode);
                SmtWorkShop smtWorkShop = smtWorkShopMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(smtWorkShop)){
                    fail.add(i+4);
                    continue;
                }
                smtProLineImport.setWorkShopId(smtWorkShop.getFactoryId());
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(smtProLineImportList)){
                for (SmtProLineImport proLineImport : smtProLineImportList) {
                    if (proLineImport.getProCode().equals(proCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            smtProLineImportList.add(smtProLineImport);
        }

        if (StringUtils.isNotEmpty(smtProLineImportList)){

            for (SmtProLineImport smtProLineImport : smtProLineImportList) {
                SmtProLine smtProLine = new SmtProLine();
                BeanUtils.copyProperties(smtProLineImport,smtProLine);
                smtProLine.setCreateTime(new Date());
                smtProLine.setCreateUserId(currentUser.getUserId());
                smtProLine.setModifiedTime(new Date());
                smtProLine.setModifiedUserId(currentUser.getUserId());
                smtProLine.setOrganizationId(currentUser.getOrganizationId());
                list.add(smtProLine);
            }

            success = smtProLineMapper.insertList(list);

            for (SmtProLine smtProLine : list) {
                SmtHtProLine smtHtProLine = new SmtHtProLine();
                BeanUtils.copyProperties(smtProLine,smtHtProLine);
                htList.add(smtHtProLine);
            }

             smtHtProLineMapper.insertList(htList);

        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

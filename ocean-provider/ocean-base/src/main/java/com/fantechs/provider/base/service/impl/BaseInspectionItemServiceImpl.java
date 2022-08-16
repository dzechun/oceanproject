package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseInspectionItemImport;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.general.entity.basic.BaseInspectionStandardDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionItemMapper;
import com.fantechs.provider.base.mapper.BaseInspectionItemMapper;
import com.fantechs.provider.base.mapper.BaseInspectionStandardDetMapper;
import com.fantechs.provider.base.service.BaseInspectionItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/06/03.
 */
@Service
public class BaseInspectionItemServiceImpl extends BaseService<BaseInspectionItem> implements BaseInspectionItemService {

    @Resource
    private BaseInspectionItemMapper baseInspectionItemMapper;
    @Resource
    private BaseHtInspectionItemMapper baseHtInspectionItemMapper;
    @Resource
    private BaseInspectionStandardDetMapper baseInspectionStandardDetMapper;



    @Override
    public List<BaseInspectionItem> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        map.put("orgId", user.getOrganizationId());
        List<BaseInspectionItem> baseInspectionItemList = baseInspectionItemMapper.findList(map);
        SearchBaseInspectionItem searchBaseInspectionItem = new SearchBaseInspectionItem();

        for (BaseInspectionItem baseInspectionItem : baseInspectionItemList){
            searchBaseInspectionItem.setParentId(baseInspectionItem.getInspectionItemId());
            List<BaseInspectionItem> detList = baseInspectionItemMapper.findDetList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionItem));
            if (StringUtils.isNotEmpty(detList)){
                baseInspectionItem.setBaseInspectionItemDets(detList);
            }
        }

        return baseInspectionItemList;
    }

    @Override
    public List<BaseInspectionItem> findDetList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseInspectionItemMapper.findDetList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionItem baseInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //检验项目小类
        List<BaseInspectionItem> baseInspectionItems = baseInspectionItem.getBaseInspectionItemDets();

        //判断编码是否重复
        List<String> codeList = new ArrayList<>();
        codeList.add(baseInspectionItem.getInspectionItemCode());
        if(StringUtils.isNotEmpty(baseInspectionItems)) {
            for (BaseInspectionItem inspectionItem : baseInspectionItems) {
                if (codeList.contains(inspectionItem.getInspectionItemCode())) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001);
                } else {
                    codeList.add(inspectionItem.getInspectionItemCode());
                }
            }
        }

        Example example = new Example(BaseInspectionItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("inspectionItemCode", codeList)
                .andEqualTo("organizationId", user.getOrganizationId());
        List<BaseInspectionItem> baseInspectionItems1 = baseInspectionItemMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionItems1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增检验项目
        baseInspectionItem.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem.getInspectionItemType())?(byte)1:baseInspectionItem.getInspectionItemType());
        baseInspectionItem.setCreateUserId(user.getUserId());
        baseInspectionItem.setCreateTime(new Date());
        baseInspectionItem.setModifiedUserId(user.getUserId());
        baseInspectionItem.setModifiedTime(new Date());
        baseInspectionItem.setStatus(StringUtils.isEmpty(baseInspectionItem.getStatus())?1: baseInspectionItem.getStatus());
        baseInspectionItem.setOrganizationId(user.getOrganizationId());
        int i = baseInspectionItemMapper.insertUseGeneratedKeys(baseInspectionItem);

        //新增检验项目明细
        if(StringUtils.isNotEmpty(baseInspectionItems)){
            for (BaseInspectionItem baseInspectionItem2:baseInspectionItems){
                baseInspectionItem2.setParentId(baseInspectionItem.getInspectionItemId());
                baseInspectionItem2.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem2.getInspectionItemType())?(byte)2:baseInspectionItem2.getInspectionItemType());
                baseInspectionItem2.setCreateUserId(user.getUserId());
                baseInspectionItem2.setCreateTime(new Date());
                baseInspectionItem2.setModifiedUserId(user.getUserId());
                baseInspectionItem2.setModifiedTime(new Date());
                baseInspectionItem2.setStatus(StringUtils.isEmpty(baseInspectionItem2.getStatus())?1:baseInspectionItem2.getStatus());
                baseInspectionItem2.setOrganizationId(user.getOrganizationId());
            }
            baseInspectionItemMapper.insertList(baseInspectionItems);
        }

        BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
        BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
        baseHtInspectionItemMapper.insertSelective(baseHtInspectionItem);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionItem baseInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //检验项目小类
        List<BaseInspectionItem> baseInspectionItems = baseInspectionItem.getBaseInspectionItemDets();

        //判断编码是否重复
        List<String> codeList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        codeList.add(baseInspectionItem.getInspectionItemCode());
        idList.add(baseInspectionItem.getInspectionItemId());
        if(StringUtils.isNotEmpty(baseInspectionItems)) {
            for (BaseInspectionItem inspectionItem : baseInspectionItems) {
                if (codeList.contains(inspectionItem.getInspectionItemCode())) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001);
                } else {
                    codeList.add(inspectionItem.getInspectionItemCode());
                }
                if(StringUtils.isNotEmpty(inspectionItem.getInspectionItemId())){
                    idList.add(inspectionItem.getInspectionItemId());
                }
            }
        }

        Example example = new Example(BaseInspectionItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("inspectionItemCode", codeList)
                .andEqualTo("organizationId", user.getOrganizationId())
                .andNotIn("inspectionItemId",idList);
        List<BaseInspectionItem> baseInspectionItems1 = baseInspectionItemMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionItems1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改检验项目
        baseInspectionItem.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem.getInspectionItemType())?(byte)1:baseInspectionItem.getInspectionItemType());
        baseInspectionItem.setModifiedTime(new Date());
        baseInspectionItem.setModifiedUserId(user.getUserId());
        baseInspectionItem.setOrganizationId(user.getOrganizationId());
        int i = baseInspectionItemMapper.updateByPrimaryKeySelective(baseInspectionItem);

        //删除原有检验项目明细
        Example example1 = new Example(BaseInspectionItem.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("parentId", baseInspectionItem.getInspectionItemId());
        baseInspectionItemMapper.deleteByExample(example1);

        //新增检验项目明细
        if(StringUtils.isNotEmpty(baseInspectionItems)){
            for (BaseInspectionItem baseInspectionItem2:baseInspectionItems){
                baseInspectionItem2.setParentId(baseInspectionItem.getInspectionItemId());
                baseInspectionItem2.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem2.getInspectionItemType())?(byte)2:baseInspectionItem2.getInspectionItemType());
                baseInspectionItem2.setCreateUserId(user.getUserId());
                baseInspectionItem2.setCreateTime(new Date());
                baseInspectionItem2.setModifiedUserId(user.getUserId());
                baseInspectionItem2.setModifiedTime(new Date());
                baseInspectionItem2.setStatus(StringUtils.isEmpty(baseInspectionItem2.getStatus())?1:baseInspectionItem2.getStatus());
                baseInspectionItem2.setOrganizationId(user.getOrganizationId());
            }
            baseInspectionItemMapper.insertList(baseInspectionItems);
        }

        BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
        BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
        baseHtInspectionItemMapper.insertSelective(baseHtInspectionItem);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtInspectionItem> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            Example example = new Example(BaseInspectionStandardDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("inspectionItemId", id);
            List<BaseInspectionStandardDet> baseInspectionStandardDets = baseInspectionStandardDetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseInspectionStandardDets))
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);

            BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
            BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
            list.add(baseHtInspectionItem);

            //删除原有检验项目明细
            Example example1 = new Example(BaseInspectionItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("parentId", baseInspectionItem.getInspectionItemId());
            baseInspectionItemMapper.deleteByExample(example1);
        }

        baseHtInspectionItemMapper.insertList(list);

        return baseInspectionItemMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseInspectionItemImport> baseInspectionItemImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseInspectionItem> list = new LinkedList<>();
        LinkedList<BaseHtInspectionItem> htList = new LinkedList<>();
        LinkedList<BaseInspectionItemImport> inspectionItemImports = new LinkedList<>();

        for (int i = 0; i < baseInspectionItemImports.size(); i++) {
            BaseInspectionItemImport baseInspectionItemImport = baseInspectionItemImports.get(i);
            String inspectionItemCodeBig = baseInspectionItemImport.getInspectionItemCodeBig();
            String inspectionItemDescBig = baseInspectionItemImport.getInspectionItemDescBig();
            String inspectionItemCodeSmall = baseInspectionItemImport.getInspectionItemCodeSmall();
            String inspectionItemDescSmall = baseInspectionItemImport.getInspectionItemDescSmall();

            if (StringUtils.isEmpty(
                    inspectionItemCodeBig,inspectionItemDescBig,inspectionItemCodeSmall,inspectionItemDescSmall
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            List<String> codeList = new ArrayList<>();
            codeList.add(inspectionItemCodeBig);
            codeList.add(inspectionItemCodeSmall);
            Example example = new Example(BaseInspectionItem.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId())
                    .andIn("inspectionItemCode",codeList);
            if (StringUtils.isNotEmpty(baseInspectionItemMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(inspectionItemImports)){
                for (BaseInspectionItemImport inspectionItemImport: inspectionItemImports) {
                    if (inspectionItemCodeSmall.equals(inspectionItemImport.getInspectionItemCodeSmall())
                        &&inspectionItemCodeBig.equals(inspectionItemImport.getInspectionItemCodeBig())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            inspectionItemImports.add(baseInspectionItemImport);
        }

        if (StringUtils.isNotEmpty(inspectionItemImports)){

            //对合格数据进行分组
            HashMap<String, List<BaseInspectionItemImport>> map = inspectionItemImports.stream().collect(Collectors.groupingBy(BaseInspectionItemImport::getInspectionItemCodeBig, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<BaseInspectionItemImport> baseInspectionItemImports1 = map.get(code);
                //新增检验项目父级数据
                BaseInspectionItem baseInspectionItem = new BaseInspectionItem();
                baseInspectionItem.setInspectionItemType((byte)1);
                baseInspectionItem.setInspectionItemCode(baseInspectionItemImports1.get(0).getInspectionItemCodeBig());
                baseInspectionItem.setInspectionItemDesc(baseInspectionItemImports1.get(0).getInspectionItemDescBig());
                baseInspectionItem.setCreateTime(new Date());
                baseInspectionItem.setCreateUserId(currentUser.getUserId());
                baseInspectionItem.setModifiedUserId(currentUser.getUserId());
                baseInspectionItem.setModifiedTime(new Date());
                baseInspectionItem.setOrganizationId(currentUser.getOrganizationId());
                baseInspectionItem.setStatus((byte)1);
                success += baseInspectionItemMapper.insertUseGeneratedKeys(baseInspectionItem);

                BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
                BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
                htList.add(baseHtInspectionItem);

                //新增检验项目明细数据
                LinkedList<BaseInspectionItem> detList = new LinkedList<>();
                for (BaseInspectionItemImport baseInspectionItemImport : baseInspectionItemImports1) {
                    BaseInspectionItem inspectionItem = new BaseInspectionItem();
                    inspectionItem.setParentId(baseInspectionItem.getInspectionItemId());
                    inspectionItem.setInspectionItemType((byte)2);
                    inspectionItem.setInspectionItemCode(baseInspectionItemImport.getInspectionItemCodeSmall());
                    inspectionItem.setInspectionItemDesc(baseInspectionItemImport.getInspectionItemDescSmall());
                    inspectionItem.setInspectionItemStandard(baseInspectionItemImport.getInspectionItemStandard());
                    inspectionItem.setCreateTime(new Date());
                    inspectionItem.setCreateUserId(currentUser.getUserId());
                    inspectionItem.setModifiedUserId(currentUser.getUserId());
                    inspectionItem.setModifiedTime(new Date());
                    inspectionItem.setStatus((byte) 1);
                    inspectionItem.setOrganizationId(currentUser.getOrganizationId());
                    detList.add(inspectionItem);
                }
                baseInspectionItemMapper.insertList(detList);
            }
            baseHtInspectionItemMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

}

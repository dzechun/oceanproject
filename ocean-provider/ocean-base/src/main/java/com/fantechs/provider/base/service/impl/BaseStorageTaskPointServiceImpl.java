package com.fantechs.provider.base.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessCauseImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageTaskPointImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageTaskPoint;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtStorageTaskPointMapper;
import com.fantechs.provider.base.mapper.BaseStorageMapper;
import com.fantechs.provider.base.mapper.BaseStorageTaskPointMapper;
import com.fantechs.provider.base.service.BaseStorageTaskPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/09/09.
 */
@Service
public class BaseStorageTaskPointServiceImpl extends BaseService<BaseStorageTaskPoint> implements BaseStorageTaskPointService {

    @Resource
    private BaseStorageTaskPointMapper baseStorageTaskPointMapper;
    @Resource
    private BaseHtStorageTaskPointMapper baseHtStorageTaskPointMapper;
    @Resource
    private BaseStorageMapper baseStorageMapper;

    @Override
    public List<BaseStorageTaskPoint> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseStorageTaskPointMapper.findList(map);
    }

    @Override
    public List<BaseHtStorageTaskPoint> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtStorageTaskPointMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStorageTaskPoint record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStorageTaskPoint.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("taskPointCode", record.getTaskPointCode());
        BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPointMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStorageTaskPoint)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria1.andEqualTo("storageId", record.getStorageId());
        BaseStorageTaskPoint baseStorageTaskPoint1 = baseStorageTaskPointMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStorageTaskPoint1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"该库位已绑定其他配送点");
        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(currentUser.getOrganizationId());
        record.setStorageTaskPointStatus(StringUtils.isEmpty(record.getStorageTaskPointStatus())?1:record.getStorageTaskPointStatus());
        baseStorageTaskPointMapper.insertUseGeneratedKeys(record);

        //新增历史信息
        BaseHtStorageTaskPoint baseHtStorageTaskPoint = new BaseHtStorageTaskPoint();
        org.springframework.beans.BeanUtils.copyProperties(record, baseHtStorageTaskPoint);
        int i = baseHtStorageTaskPointMapper.insertSelective(baseHtStorageTaskPoint);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int update(BaseStorageTaskPoint entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStorageTaskPoint.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("taskPointCode", entity.getTaskPointCode());
        criteria.andNotEqualTo("storageTaskPointId", entity.getStorageTaskPointId());
        BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPointMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStorageTaskPoint)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria1.andEqualTo("storageId", entity.getStorageId());
        criteria1.andNotEqualTo("storageTaskPointId", entity.getStorageTaskPointId());
        BaseStorageTaskPoint baseStorageTaskPoint1 = baseStorageTaskPointMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseStorageTaskPoint1)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"该库位已绑定其他配送点");
        }

        entity.setModifiedUserId(currentUser.getUserId());
        entity.setModifiedTime(new Date());
        baseStorageTaskPointMapper.updateByPrimaryKeySelective(entity);

        //新增历史信息
        BaseHtStorageTaskPoint baseHtStorageTaskPoint = new BaseHtStorageTaskPoint();
        org.springframework.beans.BeanUtils.copyProperties(entity, baseHtStorageTaskPoint);
        int i = baseHtStorageTaskPointMapper.insertSelective(baseHtStorageTaskPoint);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtStorageTaskPoint> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPointMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseStorageTaskPoint)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtStorageTaskPoint baseHtStorageTaskPoint = new BaseHtStorageTaskPoint();
            BeanUtils.copyProperties(baseStorageTaskPoint,baseHtStorageTaskPoint);
            list.add(baseHtStorageTaskPoint);
        }

        baseHtStorageTaskPointMapper.insertList(list);

        return baseStorageTaskPointMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStorageTaskPointImport> baseStorageTaskPointImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseStorageTaskPoint> list = new LinkedList<>();
        LinkedList<BaseHtStorageTaskPoint> htList = new LinkedList<>();
        LinkedList<BaseStorageTaskPointImport> storageTaskPointImports = new LinkedList<>();

        for (int i = 0; i < baseStorageTaskPointImports.size(); i++) {
            BaseStorageTaskPointImport baseStorageTaskPointImport = baseStorageTaskPointImports.get(i);
            String taskPointCode = baseStorageTaskPointImport.getTaskPointCode();
            String taskPointName = baseStorageTaskPointImport.getTaskPointName();
            String storageCode = baseStorageTaskPointImport.getStorageCode();
            String xyzCode = baseStorageTaskPointImport.getXyzCode();
            Integer usePriority = baseStorageTaskPointImport.getUsePriority();

            if (StringUtils.isEmpty(
                    taskPointCode,taskPointName,storageCode,xyzCode,usePriority
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseStorageTaskPoint.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("taskPointCode",taskPointCode);
            if (StringUtils.isNotEmpty(baseStorageTaskPointMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //库位
            Example example1 = new Example(BaseStorage.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", user.getOrganizationId());
            criteria1.andEqualTo("storageCode",storageCode);
            BaseStorage baseStorage = baseStorageMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseStorage)){
                fail.add(i+4);
                continue;
            }
            baseStorageTaskPointImport.setStorageId(baseStorage.getStorageId());

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(storageTaskPointImports)){
                for (BaseStorageTaskPointImport storageTaskPointImport: storageTaskPointImports) {
                    if (taskPointCode.equals(storageTaskPointImport.getTaskPointCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            storageTaskPointImports.add(baseStorageTaskPointImport);
        }

        if (StringUtils.isNotEmpty(storageTaskPointImports)){

            for (BaseStorageTaskPointImport baseStorageTaskPointImport : storageTaskPointImports) {
                BaseStorageTaskPoint baseStorageTaskPoint = new BaseStorageTaskPoint();
                BeanUtils.copyProperties(baseStorageTaskPointImport,baseStorageTaskPoint);
                baseStorageTaskPoint.setTaskPointType(baseStorageTaskPointImport.getTaskPointType().byteValue());
                baseStorageTaskPoint.setStorageTaskPointStatus(baseStorageTaskPointImport.getStorageTaskPointStatus().byteValue());
                baseStorageTaskPoint.setCreateTime(new Date());
                baseStorageTaskPoint.setCreateUserId(user.getUserId());
                baseStorageTaskPoint.setModifiedTime(new Date());
                baseStorageTaskPoint.setModifiedUserId(user.getUserId());
                baseStorageTaskPoint.setOrgId(user.getOrganizationId());
                baseStorageTaskPoint.setStatus((byte)1);
                list.add(baseStorageTaskPoint);
            }
            success = baseStorageTaskPointMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseStorageTaskPoint baseStorageTaskPoint : list) {
                    BaseHtStorageTaskPoint baseHtStorageTaskPoint = new BaseHtStorageTaskPoint();
                    BeanUtils.copyProperties(baseStorageTaskPoint, baseHtStorageTaskPoint);
                    htList.add(baseHtStorageTaskPoint);
                }
                baseHtStorageTaskPointMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

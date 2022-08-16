package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseAreaImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkingAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.BaseWorkingArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkingArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkingAreaMapper;
import com.fantechs.provider.base.mapper.BaseWarehouseAreaMapper;
import com.fantechs.provider.base.mapper.BaseWorkingAreaMapper;
import com.fantechs.provider.base.service.BaseWorkingAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseWorkingAreaServiceImpl extends BaseService<BaseWorkingArea> implements BaseWorkingAreaService {

    @Resource
    private BaseWorkingAreaMapper baseWorkingAreaMapper;

    @Resource
    private BaseHtWorkingAreaMapper baseHtWorkingAreaMapper;

    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;

    @Override
    public List<BaseWorkingAreaDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseWorkingAreaMapper.findList(map);
    }

    @Override
    public int save(BaseWorkingArea record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(record.getStatus() == null ? 1 : record.getStatus());

        int i = baseWorkingAreaMapper.insertUseGeneratedKeys(record);

        BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
        BeanUtils.copyProperties(record,baseHtWorkingArea);
        baseHtWorkingAreaMapper.insertSelective(baseHtWorkingArea);

        return i;
    }

    @Override
    public int update(BaseWorkingArea entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        entity.setStatus(entity.getStatus() == null ? 1 : entity.getStatus());
        BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
        BeanUtils.copyProperties(entity,baseHtWorkingArea);
        baseHtWorkingAreaMapper.insertSelective(baseHtWorkingArea);

        return baseWorkingAreaMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtWorkingArea> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseWorkingArea baseWorkingArea = baseWorkingAreaMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseWorkingArea)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
            BeanUtils.copyProperties(baseWorkingArea,baseHtWorkingArea);
            list.add(baseHtWorkingArea);
        }

        baseHtWorkingAreaMapper.insertList(list);
        return baseWorkingAreaMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseWorkingArea entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWorkingArea.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("workingAreaCode",entity.getWorkingAreaCode());
        if (StringUtils.isNotEmpty(entity.getWorkingAreaId())){
            criteria.andNotEqualTo("workingAreaId",entity.getWorkingAreaId());
        }
        BaseWorkingArea baseBadnessCategory = baseWorkingAreaMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWorkingAreaImport> baseWorkingAreaImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWorkingArea> list = new LinkedList<>();
        LinkedList<BaseHtWorkingArea> htList = new LinkedList<>();
        LinkedList<BaseWorkingAreaImport> workingAreaImports = new LinkedList<>();

        for (int i = 0; i < baseWorkingAreaImports.size(); i++) {
            BaseWorkingAreaImport baseWorkingAreaImport = baseWorkingAreaImports.get(i);
            String workingAreaCode = baseWorkingAreaImport.getWorkingAreaCode();
            String warehouseAreaCode = baseWorkingAreaImport.getWarehouseAreaCode();


            if (StringUtils.isEmpty(
                    workingAreaCode,warehouseAreaCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWorkingArea.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("workingAreaCode",workingAreaCode);
            if (StringUtils.isNotEmpty(baseWorkingAreaMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断库区信息是否存在
            Example example1 = new Example(BaseWorkingArea.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria1.andEqualTo("warehouseAreaCode", warehouseAreaCode);
            BaseWarehouseArea baseWarehouseArea = baseWarehouseAreaMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseWarehouseArea)) {
                fail.add(i + 4);
                continue;
            }
            baseWorkingAreaImport.setWarehouseAreaId(baseWarehouseArea.getWarehouseAreaId());

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(workingAreaImports)){
                for (BaseWorkingAreaImport workingAreaImport: workingAreaImports) {
                    if (workingAreaCode.equals(workingAreaImport.getWorkingAreaCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            workingAreaImports.add(baseWorkingAreaImport);
        }

        if (StringUtils.isNotEmpty(workingAreaImports)){

            for (BaseWorkingAreaImport baseWorkingAreaImport : workingAreaImports) {
                BaseWorkingArea baseWorkingArea = new BaseWorkingArea();
                BeanUtils.copyProperties(baseWorkingAreaImport,baseWorkingArea);
                baseWorkingArea.setCreateTime(new Date());
                baseWorkingArea.setCreateUserId(currentUser.getUserId());
                baseWorkingArea.setModifiedTime(new Date());
                baseWorkingArea.setModifiedUserId(currentUser.getUserId());
                baseWorkingArea.setOrgId(currentUser.getOrganizationId());
                baseWorkingArea.setStatus((byte)1);
                list.add(baseWorkingArea);
            }
            success = baseWorkingAreaMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseWorkingArea baseWorkingArea : list) {
                    BaseHtWorkingArea baseHtWorkingArea = new BaseHtWorkingArea();
                    BeanUtils.copyProperties(baseWorkingArea, baseHtWorkingArea);
                    htList.add(baseHtWorkingArea);
                }
                baseHtWorkingAreaMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}

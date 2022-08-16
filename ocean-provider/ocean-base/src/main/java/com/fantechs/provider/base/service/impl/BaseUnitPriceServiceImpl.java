package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseUnitPriceImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.general.entity.basic.BaseUnitPriceDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseUnitPriceDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseUnitPriceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/01/27.
 */
@Service
public class BaseUnitPriceServiceImpl extends BaseService<BaseUnitPrice> implements BaseUnitPriceService {

    @Resource
    private BaseUnitPriceMapper baseUnitPriceMapper;
    @Resource
    private BaseHtUnitPriceMapper baseHtUnitPriceMapper;
    @Resource
    private BaseUnitPriceDetMapper baseUnitPriceDetMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Override
    public List<BaseUnitPriceDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseUnitPriceDto> list = baseUnitPriceMapper.findList(map);
        if (StringUtils.isNotEmpty(list)){
            SearchBaseUnitPriceDet searchBaseUnitPriceDet = new SearchBaseUnitPriceDet();
            for (BaseUnitPriceDto baseUnitPriceDto : list) {
                searchBaseUnitPriceDet.setUnitPriceId(baseUnitPriceDto.getUnitPriceId());
                List<BaseUnitPriceDetDto> baseUnitPriceDetDtos = baseUnitPriceDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseUnitPriceDet));
                if (StringUtils.isNotEmpty(baseUnitPriceDetDtos)){
                    baseUnitPriceDto.setBaseUnitPriceDetDtoList(baseUnitPriceDetDtos);
                }
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseUnitPrice baseUnitPrice) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseUnitPrice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseUnitPrice.getMaterialId());
        BaseUnitPrice baseUnitPrice1 = baseUnitPriceMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseUnitPrice1)){
            throw new BizErrorException("该物料的单价信息已经存在");
        }

        //新增单价信息
        baseUnitPrice.setCreateTime(new Date());
        baseUnitPrice.setCreateUserId(user.getUserId());
        baseUnitPrice.setModifiedTime(new Date());
        baseUnitPrice.setModifiedUserId(user.getUserId());
        baseUnitPrice.setOrganizationId(user.getOrganizationId());
        int i = baseUnitPriceMapper.insertUseGeneratedKeys(baseUnitPrice);

        //新增履历
        BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
        BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
        baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);

        //删除原绑定关系
        Example example1 = new Example(BaseUnitPriceDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("unitPriceId",baseUnitPrice.getUnitPriceId());
        baseUnitPriceDetMapper.deleteByExample(example1);

        //新增明细
        List<BaseUnitPriceDetDto> baseUnitPriceDetDtoList = baseUnitPrice.getBaseUnitPriceDetDtoList();
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            for (BaseUnitPriceDet baseUnitPriceDet : baseUnitPriceDetDtoList) {
                baseUnitPriceDet.setUnitPriceId(baseUnitPrice.getUnitPriceId());
                baseUnitPriceDet.setCreateTime(new Date());
                baseUnitPriceDet.setCreateUserId(user.getUserId());
                baseUnitPriceDet.setModifiedTime(new Date());
                baseUnitPriceDet.setModifiedUserId(user.getUserId());
                baseUnitPriceDet.setOrganizationId(user.getOrganizationId());
            }
        }
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            baseUnitPriceDetMapper.insertList(baseUnitPriceDetDtoList);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseUnitPrice baseUnitPrice) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseUnitPrice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseUnitPrice.getMaterialId())
                .andNotEqualTo("unitPriceId",baseUnitPrice.getUnitPriceId());
        BaseUnitPrice baseUnitPrice1 = baseUnitPriceMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseUnitPrice1)){
            throw new BizErrorException("该物料的单价信息已经存在");
        }

        baseUnitPrice.setModifiedTime(new Date());
        baseUnitPrice.setModifiedUserId(user.getUserId());
        baseUnitPrice.setOrganizationId(user.getOrganizationId());

        BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
        BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
        baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);

        //删除原绑定关系
        Example example1 = new Example(BaseUnitPriceDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("unitPriceId",baseUnitPrice.getUnitPriceId());
        baseUnitPriceDetMapper.deleteByExample(example1);

        //新增明细
        List<BaseUnitPriceDetDto> baseUnitPriceDetDtoList = baseUnitPrice.getBaseUnitPriceDetDtoList();
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            for (BaseUnitPriceDet baseUnitPriceDet : baseUnitPriceDetDtoList) {
                baseUnitPriceDet.setUnitPriceId(baseUnitPrice.getUnitPriceId());
                baseUnitPriceDet.setCreateTime(new Date());
                baseUnitPriceDet.setCreateUserId(user.getUserId());
                baseUnitPriceDet.setModifiedTime(new Date());
                baseUnitPriceDet.setModifiedUserId(user.getUserId());
                baseUnitPriceDet.setOrganizationId(user.getOrganizationId());
            }
        }
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            baseUnitPriceDetMapper.insertList(baseUnitPriceDetDtoList);
        }

        return baseUnitPriceMapper.updateByPrimaryKeySelective(baseUnitPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        List<BaseHtUnitPrice>  baseHtUnitPrices = new LinkedList<>();
        for (String id : idsArr) {
            BaseUnitPrice baseUnitPrice = baseUnitPriceMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseUnitPrice)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
            BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
            baseHtUnitPrice.setCreateTime(new Date());
            baseHtUnitPrice.setCreateUserId(user.getUserId());
            baseHtUnitPrice.setModifiedTime(new Date());
            baseHtUnitPrice.setMaterialId(user.getUserId());
            baseHtUnitPrices.add(baseHtUnitPrice);

            //删除原绑定关系
            Example example = new Example(BaseUnitPriceDet.class);
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("unitPriceId",id);
            baseUnitPriceDetMapper.deleteByExample(example);
        }

        baseHtUnitPriceMapper.insertList(baseHtUnitPrices);

        return baseUnitPriceMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseUnitPriceImport> baseUnitPriceImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseUnitPriceImport> unitPriceImports = new LinkedList<>();

        for (int i = 0; i < baseUnitPriceImports.size(); i++) {
            BaseUnitPriceImport baseUnitPriceImport = baseUnitPriceImports.get(i);
            String materialCode = baseUnitPriceImport.getMaterialCode();
            String processCode = baseUnitPriceImport.getProcessCode();
            BigDecimal unitPrice = baseUnitPriceImport.getUnitPrice();

            if (StringUtils.isEmpty(materialCode,processCode,unitPrice)){
                fail.add(i + 4);
                continue;
            }

            //判断物料和工序信息是否存在
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId",currentUser.getOrganizationId());
            criteria1.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);

            Example example2 = new Example(BaseProcess.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId",currentUser.getOrganizationId());
            criteria2.andEqualTo("processCode",processCode);
            BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example2);

            if (StringUtils.isEmpty(baseMaterial,baseProcess)){
                fail.add(i + 4);
                continue;
            }
            baseUnitPriceImport.setMaterialId(baseMaterial.getMaterialId());
            baseUnitPriceImport.setProcessId(baseProcess.getProcessId());

            //物料唯一，工序多条且唯一
            boolean tag = false;
            Example example3 = new Example(BaseUnitPrice.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("materialId",baseUnitPriceImport.getMaterialId());
            BaseUnitPrice baseUnitPrice = baseUnitPriceMapper.selectOneByExample(example3);

            if (StringUtils.isNotEmpty(baseUnitPrice)) {
                Example example4 = new Example(BaseUnitPriceDet.class);
                Example.Criteria criteria4 = example4.createCriteria();
                criteria4.andEqualTo("unitPriceId", baseUnitPrice.getUnitPriceId());
                List<BaseUnitPriceDet> baseUnitPriceDets = baseUnitPriceDetMapper.selectByExample(example4);
                if (StringUtils.isNotEmpty(baseUnitPriceDets)) {
                    for (BaseUnitPriceDet baseUnitPriceDet : baseUnitPriceDets) {
                        if (baseUnitPriceImport.getProcessId().equals(baseUnitPriceDet.getProcessId())) {
                            tag = true;
                            break;
                        }
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            if (StringUtils.isNotEmpty(unitPriceImports)){
                for (BaseUnitPriceImport unitPriceImport : unitPriceImports) {
                    if (materialCode.equals(unitPriceImport.getMaterialCode())&&processCode.equals(unitPriceImport.getProcessCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            unitPriceImports.add(baseUnitPriceImport);
        }

        //对合格数据进行分组
        Map<String, List<BaseUnitPriceImport>> map = unitPriceImports.stream().collect(Collectors.groupingBy(BaseUnitPriceImport::getMaterialCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<BaseUnitPriceImport> baseUnitPriceImportList = map.get(code);

            //若物料已存在则只增加子表，不存在则新增父表和子表
            Example example5 = new Example(BaseUnitPrice.class);
            Example.Criteria criteria5 = example5.createCriteria();
            criteria5.andEqualTo("materialId",baseUnitPriceImportList.get(0).getMaterialId());
            BaseUnitPrice baseUnitPrice = baseUnitPriceMapper.selectOneByExample(example5);

            if(StringUtils.isEmpty(baseUnitPrice)){
                //新增单价信息父表
                baseUnitPrice = new BaseUnitPrice();
                BeanUtils.copyProperties(baseUnitPriceImportList.get(0),baseUnitPrice);
                baseUnitPrice.setCreateTime(new Date());
                baseUnitPrice.setCreateUserId(currentUser.getUserId());
                baseUnitPrice.setModifiedTime(new Date());
                baseUnitPrice.setModifiedUserId(currentUser.getUserId());
                baseUnitPrice.setStatus((byte) 1);
                baseUnitPrice.setOrganizationId(currentUser.getOrganizationId());
                baseUnitPriceMapper.insertUseGeneratedKeys(baseUnitPrice);

                //新增单价信息履历
                BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
                BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
                baseHtUnitPrice.setModifiedTime(new Date());
                baseHtUnitPrice.setModifiedUserId(currentUser.getUserId());
                baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);
            }

            //新增单价明细
            for (BaseUnitPriceImport baseUnitPriceImport : baseUnitPriceImportList) {
                BaseUnitPriceDet baseUnitPriceDet = new BaseUnitPriceDet();
                BeanUtils.copyProperties(baseUnitPriceImport,baseUnitPriceDet);
                baseUnitPriceDet.setUnitPriceId(baseUnitPrice.getUnitPriceId());
                success += baseUnitPriceDetMapper.insertUseGeneratedKeys(baseUnitPriceDet);
            }

        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }

}

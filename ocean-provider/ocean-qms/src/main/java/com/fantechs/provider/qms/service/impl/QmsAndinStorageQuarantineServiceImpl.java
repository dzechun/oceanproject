package com.fantechs.provider.qms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.dto.qms.QmsAndinStorageQuarantineDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarning;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import com.fantechs.common.base.general.entity.qms.history.QmsHtAndinStorageQuarantine;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.qms.mapper.QmsAndinStorageQuarantineMapper;
import com.fantechs.provider.qms.mapper.QmsHtAndinStorageQuarantineMapper;
import com.fantechs.provider.qms.service.QmsAndinStorageQuarantineService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class QmsAndinStorageQuarantineServiceImpl extends BaseService<QmsAndinStorageQuarantine> implements QmsAndinStorageQuarantineService {

    @Resource
    private QmsAndinStorageQuarantineMapper qmsAndinStorageQuarantineMapper;
    @Resource
    private InFeignApi inFeignApi;
    @Resource
    private QmsHtAndinStorageQuarantineMapper qmsHtAndinStorageQuarantineMapper;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private BcmFeignApi bcmFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;


    @Override
    public List<QmsAndinStorageQuarantineDto> findList(Map<String, Object> map) {
        return qmsAndinStorageQuarantineMapper.findList(map);
    }

    @Override
    public MesPackageManagerDTO analysisCode(Map<String, Object> map) {
        MesPackageManagerDTO managerDTO = new MesPackageManagerDTO();

        Object barcode = map.get("barcode");
        SearchMesPackageManagerListDTO search = new SearchMesPackageManagerListDTO();
        search.setBarcode(barcode == null?"-1":barcode.toString());
        ResponseEntity<List<MesPackageManagerDTO>> list = inFeignApi.list(search);
        Long parentId = 0L;
        //判断是箱码还是栈板码
        if (StringUtils.isEmpty(list.getData())){
            throw new BizErrorException("该条码不存在");
        }else if (StringUtils.isNotEmpty(list.getData()) && list.getData().get(0).getType() == 1){
            parentId = list.getData().get(0).getParentId();
            if (parentId == 0){
                throw new BizErrorException("该箱码未绑定栈板");
            }

        }else{
            parentId = list.getData().get(0).getPackageManagerId();
        }

        search.setBarcode("");
        search.setParentId(parentId);
        list = inFeignApi.list(search);

        int total = 0;
        if (StringUtils.isNotEmpty(list.getData())){
            for (MesPackageManagerDTO datum : list.getData()) {
                total += datum.getTotal().intValue();
            }
        }

        search.setPackageManagerId(parentId);
        search.setParentId(null);
        list = inFeignApi.list(search);

        if (StringUtils.isNotEmpty(list.getData()) && list.getData().size() != 0){

            search.setParentId(null);
            search.setPackageManagerId(parentId);
            list = inFeignApi.list(search);

            Example example = new Example(QmsAndinStorageQuarantine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("palletId", parentId);
            List<QmsAndinStorageQuarantine> qmsAndinStorageQuarantines = qmsAndinStorageQuarantineMapper.selectByExample(example);
            if (qmsAndinStorageQuarantines.size()!=0){
                throw new BizErrorException("该栈板已绑定待检区域");
            }

            managerDTO.setMaterialCode(list.getData().get(0).getMaterialCode());
            managerDTO.setWorkOrderCode(list.getData().get(0).getWorkOrderCode());
            managerDTO.setMaterialDesc(list.getData().get(0).getMaterialDesc());
            managerDTO.setBarCode(list.getData().get(0).getBarCode());

            managerDTO.setPackageSpecificationQuantity(list.getData().get(0).getTotal());
        }

        managerDTO.setPackageManagerId(parentId);
        managerDTO.setTotal(new BigDecimal(total));
        return managerDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsAndinStorageQuarantine qmsAndinStorageQuarantine) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        MesPackageManagerDTO mesPackageManagerDTO = null;
        qmsAndinStorageQuarantine.setCreateTime(new Date());
        qmsAndinStorageQuarantine.setCreateUserId(user.getUserId());
        qmsAndinStorageQuarantine.setModifiedTime(new Date());
        qmsAndinStorageQuarantine.setModifiedUserId(user.getUserId());
        qmsAndinStorageQuarantine.setOrganizationId(user.getOrganizationId());
        qmsAndinStorageQuarantine.setStatus(StringUtils.isEmpty(qmsAndinStorageQuarantine.getStatus())?1:qmsAndinStorageQuarantine.getStatus());

        //判断是否是栈板码逻辑
        SearchMesPackageManagerListDTO searchMesPackageManagerListDTO = new SearchMesPackageManagerListDTO();
        searchMesPackageManagerListDTO.setPackageManagerId(qmsAndinStorageQuarantine.getPalletId());
        ResponseEntity<List<MesPackageManagerDTO>> list = inFeignApi.list(searchMesPackageManagerListDTO);

        if (StringUtils.isNotEmpty(list) && list.getData().size()!=0 && list.getData().get(0).getParentId() > 0){
            qmsAndinStorageQuarantine.setPalletId(list.getData().get(0).getPackageManagerId());
            if (list.getData().get(0).getType() == 1){
                searchMesPackageManagerListDTO.setPackageManagerId(list.getData().get(0).getPackageManagerId());
                List<MesPackageManagerDTO> mesPackageManagerDTOS = inFeignApi.list(searchMesPackageManagerListDTO).getData();
                if (StringUtils.isNotEmpty(mesPackageManagerDTOS)){
                    mesPackageManagerDTO = mesPackageManagerDTOS.get(0);
                }
            }else {
                mesPackageManagerDTO = list.getData().get(0);
            }
        }else {
            mesPackageManagerDTO = list.getData().get(0);
        }



        int i = qmsAndinStorageQuarantineMapper.insertUseGeneratedKeys(qmsAndinStorageQuarantine);

        QmsHtAndinStorageQuarantine qmsHtAndinStorageQuarantine = new QmsHtAndinStorageQuarantine();
        BeanUtils.copyProperties(qmsAndinStorageQuarantine,qmsHtAndinStorageQuarantine);
        qmsHtAndinStorageQuarantine.setOperation("新增");
        qmsHtAndinStorageQuarantineMapper.insert(qmsHtAndinStorageQuarantine);

        String msg = list.getData().get(0).getWorkOrderCode()+";";
        msg += mesPackageManagerDTO.getBarCode()+";";

        SmtWarehouseArea smtWarehouseArea = basicFeignApi.warehouseAreaDetail(qmsAndinStorageQuarantine.getInspectionWaitingAreaId()).getData();
        if (StringUtils.isEmpty(smtWarehouseArea)){
            msg += "区域信息不存在";
        }else {
            msg += smtWarehouseArea.getWarehouseAreaCode();
        }
        SearchBaseWarning searchBaseWarning = new SearchBaseWarning();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("warningType");
        List<SysSpecItem> warningTypes = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if (StringUtils.isNotEmpty(warningTypes)){
            for (int j = 0; j < JSONArray.parseArray(warningTypes.get(0).getParaValue()).size(); j++) {
                Object label = JSONObject.parseObject(JSONArray.parseArray(warningTypes.get(0).getParaValue()).get(j).toString()).get("label");
                if ("质检预警".equals(label)){
                    Object value = JSONObject.parseObject(JSONArray.parseArray(warningTypes.get(0).getParaValue()).get(j).toString()).get("value");
                    searchBaseWarning.setWarningType(Long.decode(value.toString()));
                    break;
                }

            }

        }
        List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
        if (StringUtils.isNotEmpty(baseWarningDtos)){
            BaseWarningDto baseWarningDto = baseWarningDtos.get(0);
            List<BaseWarningPersonnelDto> baseWarningPersonnelList = baseWarningDto.getBaseWarningPersonnelDtoList();
            for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningPersonnelList) {
                bcmFeignApi.sendSimpleMail(baseWarningPersonnelDto.getEmail(),"入库待检",msg);
            }
        }

        return i;
    }

    @Override
    public int update(QmsAndinStorageQuarantine qmsAndinStorageQuarantine) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        qmsAndinStorageQuarantine.setModifiedTime(new Date());
        qmsAndinStorageQuarantine.setModifiedUserId(user.getUserId());

        QmsHtAndinStorageQuarantine qmsHtAndinStorageQuarantine = new QmsHtAndinStorageQuarantine();
        BeanUtils.copyProperties(qmsAndinStorageQuarantine,qmsHtAndinStorageQuarantine);
        qmsHtAndinStorageQuarantine.setOperation("修改");
        qmsHtAndinStorageQuarantineMapper.insert(qmsHtAndinStorageQuarantine);

        return qmsAndinStorageQuarantineMapper.updateByPrimaryKeySelective(qmsAndinStorageQuarantine);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<QmsHtAndinStorageQuarantine> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsAndinStorageQuarantine qmsAndinStorageQuarantine = qmsAndinStorageQuarantineMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsAndinStorageQuarantine)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtAndinStorageQuarantine qmsHtAndinStorageQuarantine = new QmsHtAndinStorageQuarantine();
            BeanUtils.copyProperties(qmsAndinStorageQuarantine,qmsHtAndinStorageQuarantine);
            qmsHtAndinStorageQuarantine.setOperation("删除");
            list.add(qmsHtAndinStorageQuarantine);
        }

        qmsHtAndinStorageQuarantineMapper.insertList(list);

        return qmsAndinStorageQuarantineMapper.deleteByIds(ids);
    }
}

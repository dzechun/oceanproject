package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarning;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNote;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNotePallet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtShippingNote;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutShippingNote;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutHtShippingNoteMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNotePalletMapper;
import com.fantechs.provider.wms.out.service.WmsOutShippingNoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutShippingNoteServiceImpl extends BaseService<WmsOutShippingNote> implements WmsOutShippingNoteService {

    @Resource
    private WmsOutShippingNoteMapper wmsOutShippingNoteMapper;
    @Resource
    private WmsOutShippingNoteDetMapper wmsOutShippingNoteDetMapper;
    @Resource
    private WmsOutHtShippingNoteMapper wmsOutHtShippingNoteMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;
    @Resource
    private WmsOutShippingNotePalletMapper wmsOutShippingNotePalletMapper;

    @Resource
    private BcmFeignApi bcmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WmsOutShippingNoteDto> findList(Map<String, Object> map) {
        return wmsOutShippingNoteMapper.findList(map);
    }

    @Override
    public List<WmsOutShippingNote> PDAfindList(SearchWmsOutShippingNote searchWmsOutShippingNote) {
        Example example = new Example(WmsOutShippingNote.class);
        if (searchWmsOutShippingNote.getStockStatusIn() != null) {
            example.createCriteria().andIn("stockStatus", searchWmsOutShippingNote.getStockStatusIn());
        }
        if (searchWmsOutShippingNote.getOutStatusIn() != null) {
            example.createCriteria().andIn("outStatus", searchWmsOutShippingNote.getOutStatusIn()).andEqualTo("stockStatus", 2);
        }

        return wmsOutShippingNoteMapper.selectByExample(example);
    }

    @Override
    public int sendMessageTest() throws Exception{
        int i = 0;
        //发送邮件
        //获取预警人员
        try {
            SearchBaseWarning searchBaseWarning = new SearchBaseWarning();
            searchBaseWarning.setWarningType((long) 4);
            List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
            if (StringUtils.isNotEmpty(baseWarningDtos)) {
                for (BaseWarningDto baseWarningDto : baseWarningDtos) {
                    List<BaseWarningPersonnelDto> baseWarningPersonnelDtoList = baseWarningDto.getBaseWarningPersonnelDtoList();
                    if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)) {
                        for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningPersonnelDtoList) {
                            String email = baseWarningPersonnelDto.getEmail();//获取邮箱
                            String shippingNoteCode = "CPRK20210331001";//单号
                            bcmFeignApi.sendSimpleMail(email, "有新的出货通知单", "出货通知单单号：" + shippingNoteCode
                                    + "，调出储位名称：测试储位1"
                                    + "，调入储位名称: 测试储位2");
                        }
                    }
                    i = 1;
                }
            }
        }catch (Exception e){
            e.getMessage();
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(WmsOutShippingNote wmsOutShippingNote) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsOutShippingNote.getWmsOutShippingNoteDetList())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        WmsOutShippingNote dataShippingNote = wmsOutShippingNoteMapper.selectByPrimaryKey(wmsOutShippingNote.getShippingNoteId());


        for (WmsOutShippingNoteDet wmsOutShippingNoteDet : wmsOutShippingNote.getWmsOutShippingNoteDetList()) {
            if (StringUtils.isEmpty(wmsOutShippingNoteDet.getStockPalletList())) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100);
            }

            //判断库存
            Example example = new Example(WmsOutShippingNotePallet.class);
            example.createCriteria().andEqualTo("shippingNoteDetId", wmsOutShippingNoteDet.getShippingNoteDetId());
            List<WmsOutShippingNoteDet> wmsOutShippingNoteDets = wmsOutShippingNoteDetMapper.selectByExample(example);

            SearchSmtStorageInventory searchSmtStorageInventor = new SearchSmtStorageInventory();
            searchSmtStorageInventor.setStorageId(wmsOutShippingNoteDets.get(0).getStorageId());
            List<SmtStorageInventoryDto> smtStorageInventoryDtos = storageInventoryFeignApi.findList(searchSmtStorageInventor).getData();
            if (StringUtils.isEmpty(smtStorageInventoryDtos)) {
                throw new BizErrorException(ErrorCodeEnum.STO30012001);
            }
            Boolean flag = false;
            for (SmtStorageInventoryDto smtStorageInventoryDto : smtStorageInventoryDtos) {
                if (smtStorageInventoryDto.getMaterialId().equals(wmsOutShippingNoteDets.get(0).getMaterialId())) {
                    if (smtStorageInventoryDto.getQuantity().compareTo(wmsOutShippingNoteDet.getRealityTotalQty()) >= 0) {
                        flag = true;
                        break;
                    }
                }
            }

            if (!flag) {
                throw new BizErrorException(ErrorCodeEnum.STO30012000);
            }

            //删除关系表
            wmsOutShippingNotePalletMapper.deleteByExample(example);

            for (String s : wmsOutShippingNoteDet.getStockPalletList()) {
                SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
                searchSmtStoragePallet.setPalletCode(s);
                searchSmtStoragePallet.setIsBinding((byte) 1);
                searchSmtStoragePallet.setIsDelete((byte) 1);
                //判断栈板是否绑定区域
                List<SmtStoragePalletDto> smtStoragePallets = storageInventoryFeignApi.findList(searchSmtStoragePallet).getData();
                if (smtStoragePallets.size() <= 0) {
                    throw new BizErrorException(ErrorCodeEnum.GL99990100);
                }

                //修改栈板对应的区域 （调拨）
                SmtStoragePallet smtStoragePallet = new SmtStoragePallet();
                smtStoragePallet.setStoragePalletId(smtStoragePallets.get(0).getStoragePalletId());
                smtStoragePallet.setStorageId(wmsOutShippingNoteDet.getMoveStorageId());
                smtStoragePallet.setModifiedTime(new Date());
                smtStoragePallet.setModifiedUserId(user.getUserId());
                storageInventoryFeignApi.update(smtStoragePallet);

                //重新添加子表与栈板关系表
                WmsOutShippingNotePallet wmsOutShippingNotePallet = new WmsOutShippingNotePallet();
                wmsOutShippingNotePallet.setShippingNoteDetId(wmsOutShippingNoteDet.getShippingNoteDetId());
                wmsOutShippingNotePallet.setPalletCode(s);
                wmsOutShippingNotePallet.setCreateTime(new Date());
                wmsOutShippingNotePallet.setCreateUserId(user.getCreateUserId());
                wmsOutShippingNotePallet.setOrganizationId(user.getOrganizationId());
                wmsOutShippingNotePalletMapper.insertSelective(wmsOutShippingNotePallet);

            }

            wmsOutShippingNoteDet.setModifiedTime(new Date());
            wmsOutShippingNoteDet.setModifiedUserId(user.getUserId());
            wmsOutShippingNoteDet.setStockStatus((byte) 1);
            wmsOutShippingNoteDetMapper.updateByPrimaryKeySelective(wmsOutShippingNoteDet);
        }
        //判断单据状态
        Map<String, Object> map = new HashMap<>();
        map.put("shippingNoteId", wmsOutShippingNote.getShippingNoteId());
        List<WmsOutShippingNoteDetDto> wmsOutShippingNoteDets = wmsOutShippingNoteDetMapper.findList(map);
        boolean flag = true;
        for (WmsOutShippingNoteDetDto wmsOutShippingNoteDet : wmsOutShippingNoteDets) {
            if (wmsOutShippingNoteDet.getPlanCartonQty().compareTo(wmsOutShippingNoteDet.getRealityCartonQty()) == 0) {
                if (wmsOutShippingNoteDet.getPlanTotalQty().compareTo(wmsOutShippingNoteDet.getRealityTotalQty()) == 0) {
                    wmsOutShippingNoteDet.setStockStatus((byte) 2);//备料完成
                    wmsOutShippingNoteDetMapper.updateByPrimaryKeySelective(wmsOutShippingNoteDet);
                } else {
                    flag = false;
                }
            } else {
                flag = false;
            }
        }

        if (flag) {
            wmsOutShippingNote.setStockStatus((byte) 2);//备料完成
        } else {
            wmsOutShippingNote.setStockStatus((byte) 1);//备料中
        }

        wmsOutShippingNote.setModifiedUserId(user.getUserId());
        wmsOutShippingNote.setModifiedTime(new Date());
        wmsOutShippingNote.setProcessorUserId(user.getUserId());
        //履历
//        WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
//        BeanUtils.copyProperties(wmsOutShippingNote,wmsOutHtShippingNote);
//        wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);

        return wmsOutShippingNoteMapper.updateByPrimaryKeySelective(wmsOutShippingNote);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutShippingNote wmsOutShippingNote) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsOutShippingNote.getWmsOutShippingNoteDetList())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutShippingNote.setShippingNoteCode(CodeUtils.getId("CHTZ"));
        wmsOutShippingNote.setTrafficType(StringUtils.isEmpty(wmsOutShippingNote.getTrafficType()) ? 0 : wmsOutShippingNote.getTrafficType());
        wmsOutShippingNote.setCurrencyType(StringUtils.isEmpty(wmsOutShippingNote.getCurrencyType()) ? 0 : wmsOutShippingNote.getCurrencyType());
        wmsOutShippingNote.setOutStatus((byte) 0);
        wmsOutShippingNote.setStockStatus((byte) 0);
        wmsOutShippingNote.setStatus((byte) 1);
        wmsOutShippingNote.setIsDelete((byte) 1);
        wmsOutShippingNote.setCreateTime(new Date());
        wmsOutShippingNote.setCreateUserId(user.getUserId());
        wmsOutShippingNote.setOrganizationId(user.getOrganizationId());

        int result = wmsOutShippingNoteMapper.insertUseGeneratedKeys(wmsOutShippingNote);

        System.out.println("456");
        //履历
        WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
        BeanUtils.copyProperties(wmsOutShippingNote, wmsOutHtShippingNote);
        wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);

        String storageName = "", moveStorageName = "";
        
        for (WmsOutShippingNoteDet wmsOutShippingNoteDet : wmsOutShippingNote.getWmsOutShippingNoteDetList()) {

            wmsOutShippingNoteDet.setShippingNoteId(wmsOutShippingNote.getShippingNoteId());
            wmsOutShippingNoteDet.setCreateTime(new Date());
            wmsOutShippingNoteDet.setCreateUserId(user.getUserId());
            wmsOutShippingNoteDetMapper.insertSelective(wmsOutShippingNoteDet);


            List<WmsOutShippingNoteDetDto> temp = wmsOutShippingNoteDetMapper.findList(ControllerUtil.dynamicCondition("shippingNoteId", wmsOutShippingNote.getShippingNoteId()));
            storageName = temp.get(0).getStorageName();
            moveStorageName = temp.get(0).getMoveStorageName();
        }

        //发送邮件
        //获取预警人员
        SearchBaseWarning searchBaseWarning = new SearchBaseWarning();
        searchBaseWarning.setWarningType((long) 4);
        List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
        if (StringUtils.isNotEmpty(baseWarningDtos)) {
            for (BaseWarningDto baseWarningDto : baseWarningDtos) {
                List<BaseWarningPersonnelDto> baseWarningPersonnelDtoList = baseWarningDto.getBaseWarningPersonnelDtoList();
                if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)) {
                    for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningPersonnelDtoList) {
                        String email = baseWarningPersonnelDto.getEmail();//获取邮箱
                        String shippingNoteCode = wmsOutHtShippingNote.getShippingNoteCode();//单号
                        bcmFeignApi.sendSimpleMail(email, "有新的出货通知单", "出货通知单单号：" + shippingNoteCode
                                + "，调出储位名称：" + storageName
                                + "，调入储位名称: " + moveStorageName);
                    }
                }
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutShippingNote wmsOutShippingNote) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsOutShippingNote.getWmsOutShippingNoteDetList())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutShippingNote.setModifiedUserId(user.getUserId());
        wmsOutShippingNote.setModifiedTime(new Date());
        //履历
        WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
        BeanUtils.copyProperties(wmsOutShippingNote, wmsOutHtShippingNote);
        wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);

        Example example = new Example(WmsOutShippingNoteDet.class);
        example.createCriteria().andEqualTo("shippingNoteId", wmsOutShippingNote.getShippingNoteId());
        int result = wmsOutShippingNoteDetMapper.deleteByExample(example);
        if (result > 0) {
            for (WmsOutShippingNoteDet wmsOutShippingNoteDet : wmsOutShippingNote.getWmsOutShippingNoteDetList()) {
                wmsOutShippingNoteDet.setShippingNoteId(wmsOutShippingNote.getShippingNoteId());
                wmsOutShippingNoteDet.setCreateTime(new Date());
                wmsOutShippingNoteDet.setCreateUserId(user.getUserId());
                wmsOutShippingNoteDetMapper.insertSelective(wmsOutShippingNoteDet);
            }
        } else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012000);
        }

        return wmsOutShippingNoteMapper.updateByPrimaryKeySelective(wmsOutShippingNote);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            WmsOutShippingNote wmsOutShippingNote = wmsOutShippingNoteMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutShippingNote)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //履历
            WmsOutHtShippingNote wmsOutHtShippingNote = new WmsOutHtShippingNote();
            BeanUtils.copyProperties(wmsOutShippingNote, wmsOutHtShippingNote);
            wmsOutHtShippingNoteMapper.insertSelective(wmsOutHtShippingNote);

            //删除子表
            Example example = new Example(WmsOutShippingNoteDet.class);
            example.createCriteria().andEqualTo("shippingNoteId", id);
            wmsOutShippingNoteDetMapper.deleteByExample(example);

        }
        return wmsOutShippingNoteMapper.deleteByIds(ids);
    }
}

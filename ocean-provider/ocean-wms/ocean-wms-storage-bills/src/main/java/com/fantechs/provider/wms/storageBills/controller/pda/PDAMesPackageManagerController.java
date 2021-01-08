package com.fantechs.provider.wms.storageBills.controller.pda;

import com.fantechs.common.base.dto.storage.SaveMesPackageManagerDTO;
import com.fantechs.common.base.entity.storage.MesPackageManager;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.provider.wms.storageBills.service.MesPackageManagerService;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2021年1月7日 16:38
 * @Description: 包装管理管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "包装管理管理",basePath = "pda/mesPackageManager")
@RequestMapping("pda/mesPackageManager")
@Slf4j
public class PDAMesPackageManagerController {

    @Resource
    private MesPackageManagerService mesPackageManagerService;

    @ApiOperation("查询包装管理列表")
    @PostMapping("list")
    public ResponseEntity<List<MesPackageManagerDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO
    ){
        //如果是需要查询子级数据，首先通过查询条件查询出父级数据
        if (searchMesPackageManagerListDTO.getIsFindChildren()){
            List<MesPackageManagerDTO> mesPackageManagerDTOList = mesPackageManagerService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPackageManagerListDTO));
            if(StringUtils.isEmpty(mesPackageManagerDTOList)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            searchMesPackageManagerListDTO.setParentId(mesPackageManagerDTOList.get(0).getPackageManagerId());
        }
        Page<Object> page = PageHelper.startPage(searchMesPackageManagerListDTO.getStartPage(), searchMesPackageManagerListDTO.getPageSize());
        List<MesPackageManagerDTO> mesPackageManagerDTOList = mesPackageManagerService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPackageManagerListDTO));
        return ControllerUtil.returnDataSuccess(mesPackageManagerDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询包装管理")
    @GetMapping("one")
    public ResponseEntity<MesPackageManager> one(@ApiParam(value = "包装管理对象ID",required = true)@RequestParam Long id){
        MesPackageManager mesPackageManager = mesPackageManagerService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesPackageManager, StringUtils.isEmpty(mesPackageManager)?0:1);
    }

    @ApiOperation("增加包装管理数据")
    @PostMapping("add")
    public ResponseEntity<MesPackageManager> add(@ApiParam(value = "包装管理对象",required = true)@RequestBody SaveMesPackageManagerDTO saveMesPackageManagerDTO){
        MesPackageManager mesPackageManager = mesPackageManagerService.saveChildren(saveMesPackageManagerDTO);
        return ControllerUtil.returnDataSuccess(mesPackageManager,1);
    }

    @ApiOperation("删除包装管理数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "包装管理对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesPackageManagerService.deleteByKey(id));
    }

    @ApiOperation("补打条码")
    @GetMapping("printCode")
    public ResponseEntity printCode(@ApiParam(value = "包装管理对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesPackageManagerService.printCode(id));
    }

    @ApiOperation("批量删除包装管理数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "包装管理对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesPackageManagerService.batchDelete(ids));
    }

    @ApiOperation("修改包装管理数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "包装管理对象，对象ID必传",required = true)@RequestBody MesPackageManager mesPackageManager){
        return ControllerUtil.returnCRUD(mesPackageManagerService.update(mesPackageManager));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesPackageManagerDTO> mesPackageManagerDTOList = mesPackageManagerService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesPackageManagerListDTO));
        if(StringUtils.isEmpty(mesPackageManagerDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesPackageManagerDTOList ,"包装管理信息","包装管理信息", MesPackageManagerDTO.class, "包装管理信息.xls", response);
    }
}

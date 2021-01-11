package com.fantechs.provider.wms.in.controller.history;

import com.fantechs.common.base.entity.basic.history.MesHtPackageManager;
import com.fantechs.common.base.dto.basic.history.MesHtPackageManagerDTO;
import com.fantechs.provider.wms.in.service.history.MesHtPackageManagerService;
import com.fantechs.common.base.dto.basic.history.SearchMesHtPackageManagerListDTO;
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
 * @Date: 2021年1月9日 14:04
 * @Description: 包装管理履历表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "包装管理履历表管理",basePath = "mesHtPackageManager")
@RequestMapping("mesHtPackageManager")
@Slf4j
public class MesHtPackageManagerController {

    @Resource
    private MesHtPackageManagerService mesHtPackageManagerService;

    @ApiOperation("查询包装管理履历表列表")
    @PostMapping("list")
    public ResponseEntity<List<MesHtPackageManagerDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtPackageManagerListDTO searchMesHtPackageManagerListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesHtPackageManagerListDTO.getStartPage(), searchMesHtPackageManagerListDTO.getPageSize());
        List<MesHtPackageManagerDTO> mesHtPackageManagerDTOList = mesHtPackageManagerService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtPackageManagerListDTO));
        return ControllerUtil.returnDataSuccess(mesHtPackageManagerDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询包装管理履历表")
    @GetMapping("one")
    public ResponseEntity<MesHtPackageManager> one(@ApiParam(value = "包装管理履历表对象ID",required = true)@RequestParam Long id){
        MesHtPackageManager mesHtPackageManager = mesHtPackageManagerService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesHtPackageManager, StringUtils.isEmpty(mesHtPackageManager)?0:1);
    }

    @ApiOperation("增加包装管理履历表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "包装管理履历表对象",required = true)@RequestBody MesHtPackageManager mesHtPackageManager){
        return ControllerUtil.returnCRUD(mesHtPackageManagerService.save(mesHtPackageManager));
    }

    @ApiOperation("删除包装管理履历表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "包装管理履历表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(mesHtPackageManagerService.deleteByKey(id));
    }

    @ApiOperation("批量删除包装管理履历表数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "包装管理履历表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesHtPackageManagerService.batchDelete(ids));
    }

    @ApiOperation("修改包装管理履历表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "包装管理履历表对象，对象ID必传",required = true)@RequestBody MesHtPackageManager mesHtPackageManager){
        return ControllerUtil.returnCRUD(mesHtPackageManagerService.update(mesHtPackageManager));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesHtPackageManagerListDTO searchMesHtPackageManagerListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesHtPackageManagerDTO> mesHtPackageManagerDTOList = mesHtPackageManagerService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesHtPackageManagerListDTO));
        if(StringUtils.isEmpty(mesHtPackageManagerDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesHtPackageManagerDTOList ,"包装管理履历表信息","包装管理履历表信息", MesHtPackageManagerDTO.class, "包装管理履历表信息.xls", response);
    }
}

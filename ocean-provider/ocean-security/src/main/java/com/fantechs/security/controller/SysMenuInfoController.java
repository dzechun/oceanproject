package com.fantechs.security.controller;

import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import com.fantechs.common.base.entity.security.history.SysHtMenuInfo;
import com.fantechs.common.base.entity.security.search.SearchSysMenuInfo;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysHtMenuInfoService;
import com.fantechs.security.service.SysMenuInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Api(tags = "菜单管理（SysMenuinfo）",basePath = "menuinfo")
@RequestMapping("/menuinfo")
@Slf4j
@Validated
public class SysMenuInfoController {

    @Resource
    private SysMenuInfoService sysMenuInfoService;
    @Resource
    private SysHtMenuInfoService sysHtMenuInfoService;

    @ApiOperation(value = "获取菜单列表",notes = "返回数据包含菜单对应的角色权限")
    @PostMapping("/findAllList")
    public ResponseEntity<List<SysMenuInListDTO>> getListAll(
            @ApiParam(value = "菜单所属平台类型（1、WEB 2、Windos 3、PDA）")@RequestParam(required = true) @NotNull(message="菜单所属平台类型不能为空") Integer menuType){
        List<SysMenuInListDTO> menuList = sysMenuInfoService.findMenuList(ControllerUtil.dynamicCondition(
                "parentId","0",
                "menuType",menuType
        ),null);
        return ControllerUtil.returnDataSuccess(menuList, StringUtils.isEmpty(menuList)?0:1);
    }

    @ApiOperation("增加菜单")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：orderNum、menuName、menuType、parentId（如果没有父级传0）",required = true)@RequestBody @Validated SysMenuInfo sysMenuInfo){
        return ControllerUtil.returnCRUD(sysMenuInfoService.save(sysMenuInfo));
    }

    @ApiOperation("删除菜单")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "菜单ID",required = true)  @RequestParam  @NotBlank(message="ids不能为空")String ids){
        return ControllerUtil.returnCRUD(sysMenuInfoService.batchDelete(ids));
    }

    @ApiOperation("修改菜单")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "菜单对象，必传菜单对象ID",required = true)@RequestBody  @Validated(value =SysMenuInfo.update.class ) SysMenuInfo sysMenuInfo) {
        return ControllerUtil.returnCRUD(sysMenuInfoService.update(sysMenuInfo));
    }

    @ApiOperation(value = "菜单列表",notes = "返回数据包含菜单对应的角色权限")
    @PostMapping("/findHtList")
    public ResponseEntity< List<SysHtMenuInfo>> getHtPageList(
            @ApiParam(value = "菜单编码")@RequestBody(required = false) SearchSysMenuInfo searchSysMenuInfo){
        Page<Object> page = PageHelper.startPage(searchSysMenuInfo.getStartPage(),searchSysMenuInfo.getPageSize());
        List<SysHtMenuInfo> menuList = sysHtMenuInfoService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSysMenuInfo));
        return ControllerUtil.returnDataSuccess(menuList,(int)page.getTotal());
    }

    @ApiOperation("菜单详情")
    @PostMapping("/detail")
    public ResponseEntity<SysMenuInfoDto> getMenuDetail(@ApiParam(value = "菜单ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id){
        SysMenuInfoDto sysMenuInfoDto = sysMenuInfoService.findById(id);
        return  ControllerUtil.returnDataSuccess(sysMenuInfoDto,StringUtils.isEmpty(sysMenuInfoDto)?0:1);
    }
}

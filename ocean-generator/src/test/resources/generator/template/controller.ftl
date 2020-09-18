package ${basePackage}.controller.${sign};

import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by ${author} on ${date}.
 */
@RestController
@Api(tags = "${baseRequestMapping}控制器")
@RequestMapping("/${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {

    @Autowired
    ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.save(${modelNameLowerCamel}));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        if(StringUtils.isEmpty(${modelNameLowerCamel}.get${modelNameUpperCamel}Id()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.update(${modelNameLowerCamel}));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<${modelNameUpperCamel}> detail(@ApiParam(value = "工厂ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        ${modelNameUpperCamel}  ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(${modelNameLowerCamel},StringUtils.isEmpty(${modelNameLowerCamel})?0:1);
    }

    @ApiOperation("根据条件查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<${modelNameUpperCamel}> findList(@ApiParam(value = "查询对象")@RequestBody Search${modelNameUpperCamel} search${modelNameUpperCamel}) {
        Page<Object> page = PageHelper.startPage(search${modelNameLowerCamel}.getStartPage(),search${modelNameLowerCamel}.getPageSize());
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}

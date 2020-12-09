package com.fantechs.provider.client.controller;

import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lfz on 2020/11/27.
 */
@RestController
@Api(tags = "电子标签控制器")
public class ElectronicTagStorageController {
    @Autowired
    private ElectronicTagStorageService electronicTagStorageService;
    /**
     * 发送需要亮灯的储位、标签信息
     * @param Sorting
     * @throws Exception
     */
    @PostMapping(value="/sendElectronicTagStorage")
    @ApiOperation(value = "发送需要亮灯",notes = "发送需要亮灯")
    public ResponseEntity sendElectronicTagStorage(@RequestBody  @Validated List<SmtSorting>  Sorting ) {
        return ControllerUtil.returnCRUD(electronicTagStorageService.sendElectronicTagStorage(Sorting));
    }
}

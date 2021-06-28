package com.fantechs.provider.wms.inner.controller;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仓库作业-库内移位作业管理
 */
@RestController
@Api(tags = "仓库作业-库内移位作业管理")
@RequestMapping("/wmsInnerShiftWork")
@Validated
public class WmsInnerShiftWorkController {
}

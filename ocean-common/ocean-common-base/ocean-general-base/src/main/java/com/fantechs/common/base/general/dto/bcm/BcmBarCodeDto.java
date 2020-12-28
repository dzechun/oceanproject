package com.fantechs.common.base.general.dto.bcm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/22
 */
@Data
public class BcmBarCodeDto extends BcmBarCode implements Serializable {
//    /**
//     * 标签类别Id
//     */
//    @Transient
//    @ApiModelProperty(name = "labelCategoryId",value = "标签类别Id")
//    @Excel(name = "标签类别Id", height = 20, width = 30,orderNum="6")
//    private Long labelCategoryId;

    /**
     * 打印方式
     */
    @Transient
    @ApiModelProperty(name = "printMode",value = "打印方式")
    @Excel(name = "printMode", height = 20, width = 30,orderNum="6")
    private String printMode;

    /**
     * 保存路径
     */
    @Transient
    @ApiModelProperty(name = "savePath",value = "保存路径")
    @Excel(name = "savePath", height = 20, width = 30,orderNum="6")
    private String savePath;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;
}

package com.fantechs.common.base.general.dto.daq;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DaqDataCollectModel implements Serializable {

    List<String> tableName;

    List<String> collectDate;
}

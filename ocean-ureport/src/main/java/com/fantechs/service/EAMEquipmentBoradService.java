package com.fantechs.service;

import com.fantechs.entity.EAMEquipmentBorad;
import com.fantechs.entity.search.SearchProLineBoard;

public interface EAMEquipmentBoradService {
    EAMEquipmentBorad findList(SearchProLineBoard searchProLineBoard);
}

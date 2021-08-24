package com.fantechs;

import com.fantechs.service.CodeGeneratorManager;

class CodeGeneratorMain {

    private static final String TABLE = "smt_solder_paste_job";

    private static final String MODEL_NAME = "ITest";

    private static final String[] TABLES = {
            "eam_equipment_standing_book", "eam_equipment_standing_book_attachment","eam_equipment_jig","eam_equipment_jig_list",
            "eam_ht_equipment_standing_book","eam_ht_equipment_standing_book_attachment","eam_ht_equipment_jig","eam_ht_equipment_jig_list"
    };

    /**
     * 说明:
     * 以表名 gen_test_demo 为例子, 主要是以下几种情况:
     * 		1. gen_test_demo ==> Demo 可以传入多表
     * 		genCodeWithSimpleName("gen_test_demo");
     *
     * 		2. gen_test_demo ==> GenTestDemo 可以传入多表
     * 		genCodeWithDetailName("gen_test_demo");
     *
     * 		3. gen_test_demo ==> IDemo 自定义名称
     * 		genCodeWithCustomName("gen_test_demo", "IDemo");
     */
    public static void main(String[] args) {
        CodeGeneratorManager cgm = new CodeGeneratorManager();

        //cgm.genCodeWithSimpleName(TABLE);

		cgm.genCodeWithDetailName(TABLES);

//		cgm.genCodeWithCustomName(TABLE, MODEL_NAME);
    }

}

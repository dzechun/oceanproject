package com.fantechs.common.base.agv.dto;

public class MaterialAndPositionCodeEnum {

    public enum MaterialAndPositionCode {

        BOTTOOM_PLATE("911001", "005400XY003050", "002860XY001550", "1"),
        WHEEL("911002", "005400XY002700", "003110XY001550", "2"),
        CAR_BODY1("911003", "005400XY002350", "003350XY001550", "3"),
        CAR_BODY2("911004", "005400XY004450", "002860XY006950", "5"),
        SIDE_PANEL("911005", "005400XY004100", "003110XY006950", "6"),
        BODY_PANEL("911006", "005400XY003750", "003350XY006950", "7"),
        COVER_PLATE("911007", "005400XY003400", "003600XY006950", "8"),
        SIMULATION_CAR("911", "003600XY001550", "005400XY002000", "4"),
        TEMPORARY_AREA("2249", "", "005000XY006950", "");

        private String materialCode;

        private String startPositionCode;

        private String endPositionCode;

        private String number;

        MaterialAndPositionCode(String materialCode, String startPositionCode, String endPositionCode, String number) {
            this.materialCode = materialCode;
            this.startPositionCode = startPositionCode;
            this.endPositionCode = endPositionCode;
            this.number = number;
        }

        public String getMaterialCode() {
            return materialCode;
        }

        public String getStartPositionCode() {
            return startPositionCode;
        }

        public String getEndPositionCode() {
            return endPositionCode;
        }

        public String getNumber() {
            return number;
        }
    }
}

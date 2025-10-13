package com.winmart.common.Constant;

import java.util.List;

public class AppConstants {

    public static final String DECIMAL_FORMAT = "#,##0.000";
    public static final int NUMBER_AFTER_DOT_IN_PERCENT = 2;
    public static final String NULL = "null";
    public static final String SUCCESS_CODE = "00";
    public static final String MAIL_SUFFIX = "@viettel.com.vn";
    public static final String COMMA = ",";
    public static final String APP_CODE = "KPI_AM";
    public static final String DATA_PERIOD_LOCK_CODE = "DATA_PERIOD_LOCK";
    public static final String RESULT_CR_EXPIRE_DAY_CODE = "RESULT_CR_EXPIRE_DAY";
    public static final String SEMI_COLON = ";";
    public static final double CONVERT_MULTIPART_FILE_SIZE_TO_KB_FACTOR = 0.0009765625;
    public static final double MAX_UPLOAD_FILE_SIZE_IN_KB = 50000.0;
    public static final String COMPANY_CODE = "VTIT";
    public static final String DATE_FORMAT_MM_YYYY = "MM/yyyy";
    public static final String VIEW = "VIEW";
    public static final String APPROVE = "APPROVE";
    public static final String ROLE_CREATE_PLAN_SLSX = "ROLE_CREATE_PLAN_SLSX";
    public static final String ROLE_CREATE_PLAN_SLNT = "ROLE_CREATE_PLAN_SLNT";
    public static final String ROLE_CREATE_EXE_SLSX = "ROLE_CREATE_EXECUTED_SLSX";
    public static final String ROLE_CREATE_EXE_SLNT = "ROLE_CREATE_EXECUTED_SLNT";
    public static final String ROLE_CREATE_RESULT_CR = "ROLE_CREATE_RESULT_CR";
    int MAX_PAGE_SIZE = 50;
    public static final List<String> operator = List.of("+", "-", "*", "/");
    public static final List<String> bracket = List.of("(", ")");
    public static final List<String> function = List.of("SUM", "AVG", "GEOMEAN", "MAX", "MIN");
    public static boolean isOperands(String s) {
        return !AppConstants.operator.contains(s) && !AppConstants.bracket.contains(s) && !AppConstants.function.contains(s);
    }

    public static final String DATE_FORMAT_YYMMDD = "yyMMdd";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY_HH_SS = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_DD_MM_YYYY_REQ = "dd-MM-yyyy";
    public static final String DATE_TIME_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd/MM/YYYY hh:mm:ss";
    public static final String DATE_TIME_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_ZONE = "Asia/Ho_Chi_Minh";
    public static final int HOUR_TO_MILLIS = 60 * 60 * 1000;
    public static final String ZERO_STRING = "0";
    public static final String YYYY_MM = "yyyy-MM";


    public static class ActionLog {
        private ActionLog() {
        }

        public static final String ADD = "Thêm mới";
        public static final String UPDATE = "Cập nhật";
        public static final String DELETE = "Xóa";
        public static final String RESTORE = "Khôi phục";
    }

    public static class SQL_PRO_TYPE {
        public static String STRING = "string";
        public static String LONG = "long";
        public static String DATE = "date";
        public static String DOUBLE = "double";
        public static String BYTE = "byte";
        public static String INT = "int";
        public static String BOOLEAN = "boolean";
    }

    public static class SQL_OPERATOR {
        public static String EQUAL = "EQUAL";
        public static String NOT_EQUAL = "NOT_EQUAL";
        public static String GREATER = "GREATER";
        public static String GREATER_EQAL = "GREATER_EQUAL";
        public static String LOWER = "LOWER";
        public static String LOWER_EQUAL = "LOWER_EQUAL";
        public static String IN = "IN";
        public static String LIKE = "LIKE";
        public static String ORDER = "ORDER";
        public static String BETWEEN = "BETWEEN";
        public static String LIMIT = "LIMIT";
        public static String OFFSET = "OFFSET";
        public static String NOT_IN = "NOT_IN";

    }

    public static class SQL_LOGIC {
        public static String OR = "or";
        public static String AND = "and";
    }

    //check response message
    public static class TYPE_GET_TICKET {
        public static final Byte BY_TICKET_ID = 0;
        public static final Byte BY_TICKET_NUMBER = 1;
    }

    public static class AttachFileRefType {
        private AttachFileRefType() {
        }

        public static final String ASSIGN_TARGET = "ASSIGN_TARGET";
    }

    public static final String SEND_WARNING_JOB_BEAN = "SendWarning";
    public static final String SEND_WARNING_JOB_STATUS = "PAUSE";
    public static final String SEND_WARNING_JOB_STATUS_SCHEDULED = "SCHEDULED";
    public static final int EXPORT_LIMIT = 1000000;
    public static final int NUMBER_AFTER_DOT = 3;

    public static class SPECIAL_CHARACTER {
        public static final String COMMA = ";";
        public static final String COMMAS = ",";
        public static final String EMPTY = "";
    }
    public static class SPLIT {
        public static final String SPLIT_METHOD_WARNING_FREQUENCY_OBJECT = ",";
        public static final String SPLIT_TARGET_ASSIGN = "\n";
    }

    public static class Regex {
        private Regex() {
        }

        // Biểu thức chính quy để tìm các toán tử liên tiếp
        public static final String PATTERN_OPERATOR_CONSECUTIVE = "[\\+\\-\\*/]+";

        //(?![-+*/]) ngay sau dấu ngoặc khong duoc la toan tu
        public static final String PATTERN_OPERATOR_CONSECUTIVE2 = "\\([-+*/]+.*\\)";
        //check numerics and characters
        public static final String CHECK_NUMERIC =  "\\d{3}(?!\\d)";

    }

    public static class FormatMariadb {
        private FormatMariadb() {
        }

        public static final String DD_MM = "%d/%m";
        public static final String DD_MM_YYYY_HH_SS = "%d/%m/%Y %H:%i";
        public static final String DD_MM_YYYY = "%d/%m/%Y";
        public static final String HH_MM = "%H:%i";
    }

    public static class CycleId {
        private CycleId() {
        }

        public static final long MONTH = 1L;
        public static final long QUARTER = 2L;
        public static final long YEAR = 3L;
    }

    public static class Arrow {
        private Arrow() {
        }

        public static final String UP = "UP";
        public static final String DOWN = "DOWN";
    }

    public static class FormulaTreeColorCode {
        public static final String RED = "#FF0000";
        public static final String GREY = "#a6a6a6";
        public static final String GREEN = "#00e64d";
        private FormulaTreeColorCode() {
        }
    }

    public static class TargetRankingConst {
        public static final String TH_KEY = "th";
        public static final String KH_KEY = "kh";
        public static final String PASSING_POINT_KEY = "passing_point";
        public static final String STANDARD_POINT_KEY = "benchmark";
        public static final String FACTOR = "factor";
        public static final String CAL_RESULT_POINT_TYPE_DAY = "DAY";
        public static final String CAL_RESULT_POINT_TYPE_MONTH = "MONTH";

        private TargetRankingConst() {

        }
    }

    public static class ExecutedValueSource {
        public static final String SYNC = "sync";

        private ExecutedValueSource() {
        }
    }
}
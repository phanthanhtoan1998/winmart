package com.winmart.common.Constant;


public class CommonConstants {

    public static final Integer ACTIVE = 1;
    public static final Integer IN_ACTIVE = 0;

    public static final String FILE_XLSX = ".xlsx";
    public static final String FILE_ZIP = ".zip";
    public static final String EMPTY = "";
    public static final String ROLE = "ROLE";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static class SPECIAL_CHARACTER {
        private SPECIAL_CHARACTER() {
        }

        public static final String PERCENT = "%";
        public static final String COMMA = ", ";
        public static final String SPACE = " ";
        public static final String NEW_LINE = " \n ";
        public static final String SUBTRACTION = "-";
        public static final String SLASH = "/";
        public static final String APOSTROPHE = "'";

        public static final String SQUARE_BRACKET_OPEND = "[";

        public static final String SQUARE_BRACKET_CLOSE = "]";
        public static final String UNDERLINED = "_";
        public static final String COLON = ":";
        public static final String DOLLAR = "$";
        public static final String VND = "đ";

        public static final String EQUAL = "=";
        public static final String AMPERSAND = "&";
        public static final String OR_OPERATION = "|";
        public static final String PARENTHESES_OPEN = "(";
        public static final String PARENTHESES_CLOSE = ")";

        public static final String LDAP_PASSWORD_SLASH = "\"";

    }

    public static class ActionLog {
        private ActionLog() {
        }

        public static final String ADD = "Thêm mới";
        public static final String UPDATE = "Cập nhật";
        public static final String DELETE = "Xóa";
        public static final String EXPORT = "Export";
    }


    public static class DATE_TIME_FORMAT {
        private DATE_TIME_FORMAT() {
        }

        public static final String YYYY_MM_DD = "yyyy-MM-dd";
        public static final String YYYY_MM_DD_HYPHEN = "yyyy-MM-dd HH:mm:ss";
        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String DD_MM_YYYY_HH = "dd/MM/yyyy HH:mm";
        public static final String DD_MM_YYYY_HH_SS = "dd/MM/yyyy HH:mm:ss";
        public static final String HH_MM_SS_DD_MM_YYYY = "HH:mm:ss dd/MM/yyyy";
        public static final String TIME_ZONE = "Asia/Ho_Chi_Minh";
        public static final String YY_MM = "yyMM";
        public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";
        public static final String YY_MM_DD_HH_MM = "yyMMddHHmm";
        public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        public static final String ISO_DATE_FORMAT_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
        public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
        public static final String DATE_TIME_FORMAT_DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
        public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        public static final String MM_YYYY = "MM-yyyy";
        public static final String YYYYMMDD = "yyyyMMdd";
        public static final String HH_MM_SS = "HH:mm:ss";
        public static final String HH_MM = "HH:mm";
        public static final String DD_MM = "dd/MM";
    }

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static class SQL_OPERATOR {
        private SQL_OPERATOR() {
        }

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

    public static class SQL_PRO_TYPE {
        private SQL_PRO_TYPE() {
        }

        public static String STRING = "string";
        public static String LONG = "long";
        public static String DATE = "date";
        public static String DOUBLE = "double";
        public static String BYTE = "byte";
        public static String INT = "int";
    }

    public static class SQL_LOGIC {
        private SQL_LOGIC() {
        }

        public static String OR = "or";
        public static String AND = "and";
    }

    public static class REGEX {
        private REGEX() {
        }

        public static String E164Format = "^\\+[1-9]\\d{1,14}$";
        public static String TEXT = "^[a-zA-Z0-9]*";
        public static String CUSTOMER_NAME = "[^A-Za-zẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴắằẳẵặăấầẩẫậâáàãảạđếềểễệêéèẻẽẹíìỉĩịốồổỗộôớờởỡợơóòõỏọứừửữựưúùủũụýỳỷỹỵ\\s]";
        public static String NUMBER = "^[0-9]*";
        public static String TAX_CODE = "^[0-9-]*";
        public static String EMAIL = "^(?=.{3,64}@)[A-Za-z0-9_]+(\\.[A-Za-z0-9_]+)*@[^-][A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public static final String VIETNAMESE_AND_NUMBER_ALLOW_SPACE = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ0-9\\s]*$";

        public static final String NUMBER_ALLOW_SPACE = "^[0-9%\\s]*$";

        public static final String TEXT_NUMBER = "^[a-zA-Z0-9_]*";

        String VIETNAMESE_DIACRITIC_CHARACTERS = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";
    }


    public static class ERROR_CODE {
        private ERROR_CODE() {
        }

        public static final String EXPORT_CSV = "EXPORT_CSV";

        public static final String ONE = "01";
        public static final String TWO = "02";
        public static final String THREE = "03";
    }

    public static class SQL_STATEMENT {
        private SQL_STATEMENT() {
        }

        public static final String ORDER_BY = " ORDER BY ";
        public static final String DESC = "DESC";
        public static final String ASC = "ASC";
        public static final String PAGING = " OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ";
    }

    public static class ExportExcel {
        private ExportExcel() {

        }

        public static final String ERROR_IMPORT_EXCEL = "ERROR_IMPORT_EXCEL";
        public static final String KEY_VALUES = "values";
        public static final String KEY_FULL_NAME = "fullName";
        public static final String KEY_TEL_NUMBER = "telNumber";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_START_TIME = "startTime";
        public static final String KEY_END_TIME = "endTime";
        public static final String KEY_ERROR = "error";
        public static final String KEY_ORGANIZATION_ID = "organizationId";
        public static final String KEY_SATISFACTION_INDEX = "satisfactionIndex";
        public static final String KEY_SATISFACTION_INDEX_MAX = "satisfactionIndexMax";
    }

    public static class FolderMinioUpload {
        private FolderMinioUpload() {
        }

        public static final String TARGET_ASSIGN_IMPORT = "assign-import";

    }
}


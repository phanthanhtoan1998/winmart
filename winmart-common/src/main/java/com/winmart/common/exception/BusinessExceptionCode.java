package com.winmart.common.exception;

/**
 * author
 */
public final class BusinessExceptionCode {


    public static final String ATTACH_FILE_NOT_FOUND = "exception.message.AttachFileNotFound";
    public static final String ATTACH_FILE_SIZE_EXCEED = "exception.message.AttachFileSizeExceed";

    private BusinessExceptionCode() {
    }
    public static class  ExamExceptionCode{
        public static final String EXAM_NOT_FOUND = "exception.message.ExamNotFound";
        public static final String EXAM_NOT_FOUND_BY_ID = "exception.message.ExamNotFoundById";
        public static final String EXAM_NOT_FOUND_BY_NAME = "exception.message.ExamNotFoundByName";
        public static final String EXAM_NOT_FOUND_BY_CODE = "exception.message.ExamNotFoundByCode";
        public static final String EXAM_NOT_FOUND_BY_CODE_OR_NAME = "exception.message.ExamNotFoundByCodeOrName";
    }


}

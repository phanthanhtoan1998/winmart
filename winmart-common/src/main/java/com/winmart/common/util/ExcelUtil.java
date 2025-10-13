package com.winmart.common.util;


import com.winmart.common.Constant.CommonConstants;
import com.winmart.common.exception.BusinessException;
import com.winmart.common.file.excel.*;
import com.winmart.common.file.excel.CellValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExcelUtil {

    private ExcelUtil() {
    }

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ExcelUtil.class);

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String FIELD_IS_ERROR = "isError";

    protected static final String[] FILE_EXCEL = {"xls", "xlsx", "xlsm"};

    public static final String NEW_LINE = "\n";

    /**
     * Check cell is null
     *
     * @return true if null
     */
    public static boolean isNullOrBlankCell(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE;
    }

    public static void setDefaultStyle(CellStyle cellStyle) {
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setWrapText(true);
    }

    public static void setStyleHeader(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public static CellStyle createNormalCell(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        setDefaultStyle(cellStyle);
        return cellStyle;
    }

    public static CellStyle createErrorCell(Workbook workbook) {
        CellStyle cellStyle = createNormalCell(workbook);
        var font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle makeErrorHeaderCellStyle(Workbook workbook) {
        var cellStyle = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle createCellHeader(Workbook workbook) {
        CellStyle cellStyleHeader = workbook.createCellStyle();
        setDefaultStyle(cellStyleHeader);
        setStyleHeader(cellStyleHeader);
        return cellStyleHeader;
    }

    public static List<String> getDataSpecificRow(MultipartFile file, int startRow, Integer startColumn) {
        checkFileExcel(file);
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            List<String> lstHeader = new ArrayList<>();
            Row row = xssfSheet.getRow(startRow);
            int lastIndexCol = row.getLastCellNum();
            startColumn = startColumn == null ? row.getFirstCellNum() : startColumn;
            for (int i = startColumn; i < lastIndexCol; i++) {
                if (isNullOrBlankCell(row.getCell(i))) break;
                lstHeader.add(row.getCell(i).getStringCellValue());
            }
            return lstHeader;
        } catch (Exception e) {
            throw new BusinessException("EXCEL_READ_FAILED", e.getMessage());
        }
    }

    public static Object getDataCell(Cell cell) {
        if (isNullOrBlankCell(cell)) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    /**
     * Check extensions
     *
     * @return true if excel file
     */
    public static void checkFileExcel(MultipartFile file) {
        if (checkFileExcel(file.getOriginalFilename()))
            throw new BusinessException("WRONG_EXTENSION");
    }

    @SneakyThrows
    public static boolean checkFileExcel(String fileName) {
        return Arrays.stream(FILE_EXCEL).noneMatch(x -> x.equals(FilenameUtils.getExtension(fileName)));
    }


    public static List<String> checkListFile(MultipartFile[] files) {
        var result = new ArrayList<String>();
        String fileName;
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename();
            try {
                checkFileExcel(file);
            } catch (IllegalArgumentException e) {
                result.add(String.format("File %s is not Excel file!", fileName));
            } catch (Exception e) {
                result.add(String.format("Error when check file %s!", fileName));
                break;
            }
        }
        return result;
    }

    public static void checkAndGetData(String currentData, FieldExcelData field,
                                       StringBuilder strErrorBuilder, StringBuilder strIdBuilder,
                                       Map<String, Object> dataRead) {
        dataRead.putIfAbsent(FIELD_IS_ERROR, false);
        if (DataUtil.isNullOrEmpty(currentData) && (field.isNotEmpty() || field.isPrimaryKey())) {
            strErrorBuilder.append(field.getErrorEmpty());
            dataRead.put(FIELD_IS_ERROR, true);
        }
        if (!DataUtil.isNullOrEmpty(currentData) && field.isPrimaryKey()) {
            strIdBuilder.append(currentData).append(".");
        }
        if (!DataUtil.isNullOrEmpty(field.getCheckDataFieldList())) {
            field.getCheckDataFieldList().forEach(x -> {
                if (x.getPredicate().test(currentData)) {
                    dataRead.put(FIELD_IS_ERROR, true);
                    strErrorBuilder.append(x.getErrorMessage());
                }
            });
        }
        dataRead.put(field.getFieldName(), currentData);
    }

    /**
     * @return object of row record
     * @author If show error, class must have field 'error'
     */
    public static <T> void getRecord(Row row, Integer startColumn, List<FieldExcelData> fieldData,
                                     Set<String> ids, String messageDuplicate, ResultReadExcel<T> resultImport, Class<T> clazz) {
        if (row == null) return;
        int lastRowCell = row.getLastCellNum();
        int currentField = 0;
        var dataRead = new LinkedHashMap<String, Object>();
        var strErrorBuilder = new StringBuilder();
        var strIdBuilder = new StringBuilder();
        DataFormatter formatter = new DataFormatter();

        startColumn = startColumn == null ? row.getFirstCellNum() : startColumn;
        boolean rowEmptyData = true;
        for (int cn = startColumn; cn < lastRowCell && cn < fieldData.size(); cn++) {
            String currentValueCell = DataUtil.safeToString(formatter.formatCellValue(row.getCell(cn)), "").trim();
            rowEmptyData = rowEmptyData && DataUtil.isNullOrEmpty(currentValueCell);
            checkAndGetData(
                    currentValueCell,
                    fieldData.get(currentField), strErrorBuilder, strIdBuilder, dataRead);
            currentField++;
        }
        if (rowEmptyData) return;

        if (ids.contains(strIdBuilder.toString())) {
            strErrorBuilder.append(messageDuplicate);
            dataRead.put(FIELD_IS_ERROR, true);
        } else {
            ids.add(strIdBuilder.toString());
        }
        dataRead.put("error", strErrorBuilder.toString());
        resultImport.setInvalidData(resultImport.getInvalidData() || !DataUtil.isNullOrEmpty(strErrorBuilder.toString()));
        resultImport.getResult().add(DataUtil.convertTo(dataRead, clazz));
    }

    /**
     * @return List data and all duplicate if exists
     * @author Import data from excel files and distinct data
     * If show error, class must have field 'error'
     */
    public static <T> ResultReadExcel<T> importData(MultipartFile[] files, int startRow, Integer startColumn,
                                                    List<FieldExcelData> fieldData, Set<String> currentIds,
                                                    String messageDuplicate, Class<T> clazz) {
        var resultImport = new ResultReadExcel<T>();
        resultImport.setInvalidData(false);

        for (MultipartFile file : files) {
            checkFileExcel(file);
        }
        String fileName;
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename();
            try {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
                int lastRow = xssfSheet.getPhysicalNumberOfRows();
                for (int i = startRow; i < lastRow; i++) {
                    Row row = xssfSheet.getRow(i);
                    getRecord(row, startColumn, fieldData, currentIds, messageDuplicate, resultImport, clazz);
                }
            } catch (Exception e) {
                throw new BusinessException("ERROR_IMPORT_EXCEL", String.format("%s:%s", fileName, e.getMessage()));
            }
        }
        return resultImport;
    }

    private static List<CellValue> addRawDataRow(Row row, Integer startColumn) {
        DataFormatter formatter = new DataFormatter();
        var cellValues = new ArrayList<CellValue>();
        int lastRowCell = row.getLastCellNum();
        int start = startColumn == null ? 0 : startColumn;
        int indexFormatData = 0;
        for (int i = start; i < lastRowCell; i++) {
            var cell = row.getCell(i);
            var cellValue = new CellValue();
            cellValue.setIndex(i);
            if (cell == null) {
                cellValue.setCellType(CellType.BLANK);
                cellValue.setRawValue(null);
                cellValue.setStringValue(null);
            } else {
                cellValue.setCellType(cell.getCellType());
                cellValue.setRawValue(getDataCell(cell));
                cellValue.setStringValue(formatter.formatCellValue(cell));
            }
            cellValues.add(cellValue);
            if (indexFormatData < lastRowCell) {
                indexFormatData++;
            }
        }
        return cellValues;
    }

    private static List<CellValue> addRawDataRow(Row row, Integer startColumn, List<CellType> formatData) {
        DataFormatter formatter = new DataFormatter();
        var cellValues = new ArrayList<CellValue>();
        int lastRowCell = row.getLastCellNum();
        int start = startColumn == null ? 0 : startColumn;
        int indexFormatData = 0;
        for (int i = start; i < lastRowCell; i++) {
            var cell = row.getCell(i);
            var cellValue = new CellValue();
            cellValue.setIndex(i);
            if (cell == null) {
                cellValue.setCellType(CellType.BLANK);
                cellValue.setRawValue(null);
                cellValue.setStringValue(null);
                cellValue.setWrongTypeColumn(CellType.BLANK.equals(formatData.get(indexFormatData)));
            } else {
                cellValue.setCellType(cell.getCellType());
                cellValue.setRawValue(getDataCell(cell));
                cellValue.setStringValue(formatter.formatCellValue(cell));
                cellValue.setWrongTypeColumn(formatData.get(indexFormatData) != cell.getCellType());
            }
            cellValues.add(cellValue);
            if (indexFormatData < lastRowCell) {
                indexFormatData++;
            }
        }
        return cellValues;
    }

    private static List<CellType> getDefaultFormatTypeColumn(Row row, Integer startColumn) {
        int lastRowCell = row.getLastCellNum();
        int start = startColumn == null ? 0 : startColumn;
        var result = new ArrayList<CellType>();
        for (int i = start; i < lastRowCell; i++) {
            var currentCell = row.getCell(i);
            if (currentCell == null) {
                result.add(CellType.BLANK);
                continue;
            }
            result.add(currentCell.getCellType());
        }
        return result;
    }

    private static List<String> getRawData(Row row) {
        List<String> rawData = new ArrayList<>();
        int lastRowCell = row.getLastCellNum();
        DataFormatter formatter = new DataFormatter();
        for (int cn = 0; cn < lastRowCell; cn++) {
            rawData.add(DataUtil.safeToString(formatter.formatCellValue(row.getCell(cn)), "").trim());
        }
        return rawData;
    }

    private static Map<String, Object> getRawData(List<String> headers, Row row) {
        Map<String, Object> rawData = new LinkedHashMap<>();
        int lastRowCell = headers.size();
        DataFormatter formatter = new DataFormatter();
        for (int cn = 0; cn < lastRowCell; cn++) {
            rawData.put(headers.get(cn), DataUtil.safeToString(formatter.formatCellValue(row.getCell(cn)), "").trim());
        }
        return rawData;
    }

    public static <T> ResultReadExcel<T> importData(MultipartFile file, TypeReference<List<T>> type) {
        checkFileExcel(file);
        ResultReadExcel<T> resultImport = new ResultReadExcel<>();
        resultImport.setInvalidData(false);
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            int lastRow = xssfSheet.getPhysicalNumberOfRows();
            List<String> headers = new ArrayList<>();
            List<Map<String, Object>> data = new ArrayList<>();
            for (int i = 0; i < lastRow; i++) {
                if (i == 0) {
                    headers.addAll(getRawData(xssfSheet.getRow(i)));
                } else {
                    data.add(getRawData(headers, xssfSheet.getRow(i)));
                }
            }
            resultImport.setResult(DataUtil.convertTo(data, type));
        } catch (IOException e) {
            resultImport.setInvalidData(true);
            logger.error("File IOException: {}", e.getMessage());
            resultImport.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            resultImport.setInvalidData(true);
            logger.error("File Exception: {}", e.getMessage());
            resultImport.setErrorMessage(e.getMessage());
        }
        return resultImport;
    }

    public static List<RowValue> getRawData(MultipartFile file, int startRow, Integer startColumn) {
        checkFileExcel(file);
        var result = new ArrayList<RowValue>();
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            int lastRow = xssfSheet.getPhysicalNumberOfRows();
            RowValue rowResult;
            for (int i = startRow; i < lastRow; i++) {
                rowResult = new RowValue();
                rowResult.setRecord(addRawDataRow(xssfSheet.getRow(i), startColumn));
                result.add(rowResult);
            }
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return result;
    }

    public static boolean checkTemplateFile(InputStream[] files, Integer sheetIndex, int headerRow,
                                            Map<Integer, String> checkMaps) {
        for (InputStream file : files) {
            try {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
                Row row = xssfSheet.getRow(headerRow);
                if (row.getPhysicalNumberOfCells() < checkMaps.size()) {
                    return false;
                }
                DataFormatter formatter = new DataFormatter();
                AtomicBoolean check = new AtomicBoolean(true);
                checkMaps.forEach((pos, valueMatch) -> {
                    String lookUpField = DataUtil.safeToString(formatter.formatCellValue(row.getCell(pos)), "").trim();
                    if (!valueMatch.equals(lookUpField)) {
                        check.set(false);
                    }
                });
                return check.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }

        return false;
    }

    public static <T> ResultReadExcel<T> importData(InputStream[] files, int startRow,
                                                    List<FieldExcelData> fieldData, Class<T> clazz, String fileName,
                                                    Integer sheetIndex) {
        var resultImport = new ResultReadExcel<T>();
        resultImport.setInvalidData(false);
        String error;
        for (InputStream file : files) {
            if (checkFileExcel(fileName)) continue;
            try {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
                int lastRow = xssfSheet.getPhysicalNumberOfRows();
                // Không lấy dòng cuối?
                for (int i = startRow; i < lastRow; i++) {
                    Row row = xssfSheet.getRow(i);
                    getRecord(row, fieldData, resultImport, clazz);
                }
            } catch (IOException e) {
                error = String.format("File %s IOException: %s", fileName, e.getMessage());
                resultImport.setErrorMessage(error);
                resultImport.setInvalidData(true);
                logger.error(error);
            } catch (Exception e) {
                error = String.format("File %s Exception: %s", fileName, e.getMessage());
                resultImport.setErrorMessage(error);
                resultImport.setInvalidData(true);
                logger.error(error);
            }
        }
        return resultImport;
    }

    public static <T> ResultReadExcel<T> importDataV2(InputStream[] files, int startRow,
                                                      List<FieldExcelData> fieldData, Class<T> clazz, String fileName,
                                                      Integer sheetIndex) {
        var resultImport = new ResultReadExcel<T>();
        resultImport.setInvalidData(false);
        String error;
        for (InputStream file : files) {
            if (checkFileExcel(fileName)) continue;
            try {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);
                int lastRow = xssfSheet.getPhysicalNumberOfRows();
                for (int i = startRow; i <= lastRow; i++) {
                    Row row = xssfSheet.getRow(i);
                    getRecord(row, fieldData, resultImport, clazz);
                }
            } catch (IOException e) {
                error = String.format("File %s IOException: %s", fileName, e.getMessage());
                resultImport.setErrorMessage(error);
                resultImport.setInvalidData(true);
                logger.error(error);
            } catch (Exception e) {
                error = String.format("File %s Exception: %s", fileName, e.getMessage());
                resultImport.setErrorMessage(error);
                resultImport.setInvalidData(true);
                logger.error(error);
            }
        }
        return resultImport;
    }

    public static <T> void getRecord(Row row, List<FieldExcelData> fieldData, ResultReadExcel<T> resultImport, Class<T> clazz) {
        if (row == null) return;
        int lastRowCell = row.getLastCellNum();
        int currentField = 0;
        var dataRead = new LinkedHashMap<String, Object>();
        var strErrorBuilder = new StringBuilder();
        var strIdBuilder = new StringBuilder();
        DataFormatter formatter = new DataFormatter();
        boolean rowEmptyData = true;
        for (int cn = row.getFirstCellNum(); cn < lastRowCell && cn < fieldData.size(); cn++) {
            String currentCellValue = DataUtil.safeToString(formatter.formatCellValue(row.getCell(cn)), "").trim();
            rowEmptyData = rowEmptyData && DataUtil.isNullOrEmpty(currentCellValue);
            checkAndGetData(
                    currentCellValue,
                    fieldData.get(currentField), strErrorBuilder, strIdBuilder, dataRead);
            currentField++;
        }
        if (rowEmptyData) return;
        resultImport.getResult().add(DataUtil.convertTo(dataRead, clazz));
    }

    //Import Survey
//    public static <T> DataImportExcel<T> getDataExcel(MultipartFile file,
//                                                      Integer startRow,
//                                                      Integer startColumn,
//                                                      Integer sheet,
//                                                      List<FieldExcelData> fieldDataList,
//                                                      FieldInfo fieldInfo,
//                                                      Class<T> clazz) throws IOException {
//
//        DataImportExcel<T> dataImportExcel = new DataImportExcel<>();
//        List<T> fileData = new ArrayList<>();
//        boolean hasError = Boolean.FALSE;
//
//        //Check extension file
//        checkFileExcel(file);
//
//        //Get data file excel
//        LOG.info("______________READ FILE IMPORT SURVEY: {}______________", System.currentTimeMillis());
//        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
//        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheet);
//        LOG.info("______________END READ FILE IMPORT SURVEY: {}______________", System.currentTimeMillis());
//
//        //Get data row
//        if (1 == xssfSheet.getPhysicalNumberOfRows())
//            throw new BusinessException(CommonConstants.ERROR_CODE.TWO, fieldInfo.getMessageErrorEmptyData());
//
//        LOG.info("______________VALIDATE IMPORT SURVEY: {}______________", System.currentTimeMillis());
//        for (int i = startRow; i < xssfSheet.getPhysicalNumberOfRows(); i++) {
//            Row row = xssfSheet.getRow(i);
//            Map<String, Object> rowData = getRowData(
//                    Objects.isNull(startColumn) ? row.getFirstCellNum() : startColumn,
//                    row,
//                    fieldDataList,
//                    fieldInfo
//            );
//
//            //Check has error
//            Object errorMessage = rowData.get("error");
//            if (!DataUtil.isNullOrEmpty(errorMessage)) {
//                CellStyle cellStyle = row.getCell(0).getCellStyle();
//                cellStyle.setWrapText(Boolean.TRUE);
//                cellStyle.setAlignment(HorizontalAlignment.LEFT);
//
//                Cell cellError = row.createCell(row.getLastCellNum());
//                cellError.setCellValue((String) errorMessage);
//                cellError.setCellStyle(cellStyle);
//                hasError = Boolean.TRUE;
//            }
//
//            fileData.add(DataUtil.convertTo(rowData, clazz));
//        }
//        LOG.info("______________END VALIDATE IMPORT SURVEY: {}______________", System.currentTimeMillis());
//
//        LOG.info("______________WRITE COLUMN ERROR IMPORT SURVEY: {}______________", System.currentTimeMillis());
//        //Get title column dynamic
//        if (Boolean.TRUE.equals(fieldInfo.isHasDynamicColumn())) {
//            List<String> dataDynamic = ExcelUtils.getDataDynamicColumn(xssfSheet, fieldDataList.size());
//            dataImportExcel.setDynamicField(dataDynamic);
//        }
//
//        //Set title error column
//        if (Boolean.TRUE.equals(hasError)) {
//            //set style
//            Row row = xssfSheet.getRow(0);
//            CellStyle cellStyle = row.getCell(0).getCellStyle();
//            cellStyle.setAlignment(HorizontalAlignment.CENTER);
//
//            //set title
//            Cell errorCell = row.createCell(row.getLastCellNum());
//            errorCell.setCellValue(fieldInfo.getTitleColumnError());
//            errorCell.setCellStyle(cellStyle);
//
//            //set with
//            xssfSheet.autoSizeColumn(row.getLastCellNum() - 1);
//        }
//
//        //Set data file
//        dataImportExcel.setFileData(fileData);
//        dataImportExcel.setRawData(workbookToByteArray(xssfWorkbook));
//        dataImportExcel.setHasError(hasError);
//        LOG.info("______________END WRITE COLUMN ERROR IMPORT SURVEY: {}______________", System.currentTimeMillis());
//        return dataImportExcel;
//    }

    public static Map<String, Object> getRowData(Integer startColumn, Row row, List<FieldExcelData> fieldDataList, FieldInfo fieldInfo) {
        LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
        StringBuilder errorMessage = new StringBuilder();
        int currentField = 0;

        //get data dynamic column
        if (Boolean.TRUE.equals(fieldInfo.isHasDynamicColumn())) {
            List<CellType> checkCellType = getDefaultFormatTypeColumn(row, fieldDataList.size());

            if (DataUtil.isNullOrEmpty(checkCellType))
                errorMessage.append(fieldInfo.getMessageErrorFieldDynamicRequire()).append(NEW_LINE);
            else
                rowData.put(CommonConstants.ExportExcel.KEY_VALUES, addRawDataRow(row, fieldDataList.size(), checkCellType));
        }

        //get data specific column
        for (int i = startColumn; i < fieldDataList.size(); i++) {
            String currentData = DataUtil.safeToString(new DataFormatter().formatCellValue(row.getCell(i)), CommonConstants.EMPTY).trim();
            FieldExcelData field = fieldDataList.get(currentField);

            //check is empty cell data
            if (DataUtil.isNullOrEmpty(currentData) && field.isNotEmpty())
                errorMessage.append(field.getErrorEmpty()).append(NEW_LINE);

            //check invalid cell data
            if (!DataUtil.isNullOrEmpty(field.getCheckDataFieldList())) {
                field.getCheckDataFieldList().forEach(fieldCondition -> {
                    if (fieldCondition.getPredicate().test(currentData))
                        errorMessage.append(fieldCondition.getErrorMessage()).append(NEW_LINE);
                });
            }

            //validate two column
            errorMessage.append(validateDoubleColumn(field, fieldInfo, row, i, currentData));

            currentField++;
            rowData.put(field.getFieldName(), currentData);
        }

        //put message error
        rowData.put(CommonConstants.ExportExcel.KEY_ERROR, errorMessage.toString());

        return rowData;
    }

    public static List<String> getDataDynamicColumn(XSSFSheet xssfSheet, Integer startColumn) {
        List<String> resultData = new ArrayList<>();
        Row row = xssfSheet.getRow(0);
        startColumn = startColumn == null ? row.getFirstCellNum() : startColumn;

        for (int i = startColumn; i < row.getLastCellNum(); i++) {
            if (isNullOrBlankCell(row.getCell(i))) break;
            resultData.add(row.getCell(i).getStringCellValue());
        }
        return resultData;
    }

    public static ByteArrayOutputStream workbookToByteArray(XSSFWorkbook xssfWorkbook) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            xssfWorkbook.write(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            return null;
        }
    }

    public static Date convertStringToDate(String dateInString, String dateFormat) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormat);
            if (Strings.isNullOrEmpty(dateInString)) {
                return null;
            }
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
    }

    private static StringBuilder validateDoubleColumn(FieldExcelData field, FieldInfo fieldInfo, Row row, Integer index, String currentData) {
        StringBuilder errorMessage = new StringBuilder();

        //check invalid date
        if (CommonConstants.ExportExcel.KEY_START_TIME.equals(field.getFieldName()) && !DataUtil.isNullOrEmpty(currentData)) {
            String nextData = DataUtil.safeToString(new DataFormatter().formatCellValue(row.getCell(index + 1)), CommonConstants.EMPTY).trim();
            Date startDate = convertStringToDate(currentData, CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY_HH_SS);
            Date endDate = convertStringToDate(nextData, CommonConstants.DATE_TIME_FORMAT.DD_MM_YYYY_HH_SS);

            if (!Objects.isNull(startDate) &&
                    !Objects.isNull(endDate) &&
                    startDate.getTime() > endDate.getTime())
                errorMessage.append(fieldInfo.getMessageErrorInterval()).append(NEW_LINE);
        }

        //check satisfaction index
        if (CommonConstants.ExportExcel.KEY_SATISFACTION_INDEX.equals(field.getFieldName())) {
            Long satisfactionIndexMax = getDataLong(new DataFormatter().formatCellValue(row.getCell(index + 1)));
            Long satisfactionIndex = getDataLong(currentData);

            if (DataUtil.isNullOrEmpty(satisfactionIndex) && !DataUtil.isNullOrEmpty(satisfactionIndexMax))
                errorMessage.append(fieldInfo.getSatisfactionIndexRequire()).append(NEW_LINE);

            if (!DataUtil.isNullOrEmpty(satisfactionIndex) && DataUtil.isNullOrEmpty(satisfactionIndexMax))
                errorMessage.append(fieldInfo.getSatisfactionIndexMaxRequire()).append(NEW_LINE);

            if (!DataUtil.isNullOrEmpty(satisfactionIndex) &&
                    !DataUtil.isNullOrEmpty(satisfactionIndexMax) &&
                    satisfactionIndex > satisfactionIndexMax)
                errorMessage.append(fieldInfo.getSatisfactionIndexInvalid()).append(NEW_LINE);
        }

        return errorMessage;
    }

    public static Long getDataLong(String strNumber) {
        try {
            return Long.parseLong(strNumber);
        } catch (Exception e) {
            return null;
        }
    }

    public static void appendError(String value, StringBuilder errorBuilder, String textError) {
        if (StringUtilJr.isNullOrEmpty(value)) {
            errorBuilder.append(textError);
        }
    }

    private void appendError(Double value, StringBuilder errorBuilder, String textError) {
        if (value == null) {
            errorBuilder.append(textError);
        }
    }

    private void appendError(BigDecimal value, StringBuilder errorBuilder, String textError) {
        if (value == null) {
            errorBuilder.append(textError);
        }
    }

    private void appendError(Float value, StringBuilder errorBuilder, String textError) {
        if (value == null) {
            errorBuilder.append(textError);
        }
    }
}


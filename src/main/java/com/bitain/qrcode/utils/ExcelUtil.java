package com.bitain.qrcode.utils;

import com.bitain.qrcode.config.ExcelCellConfig;
import com.bitain.qrcode.config.ExcelImportConfig;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExcelUtil {
    public Workbook getWorkbookStream(MultipartFile importFile) {
        try {
            InputStream inputStream;
            inputStream = importFile.getInputStream();
            return WorkbookFactory.create(inputStream);
        } catch (IOException e)  {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public List<T> readDataFromExcelFile(Workbook workbook, ExcelImportConfig importConfig){
        List<T> list = new ArrayList<>();
        List<ExcelCellConfig> excelCellConfigs = importConfig.getCellImportConfigs();
        Sheet sheet = workbook.getSheet(importConfig.getSheetName());
        int countRow = 0;
        for(Row row : sheet){
            if (countRow< importConfig.getStartRow()){
                countRow++;
                continue;
            }
            if (isEmptyRow(row)){
                break;
            }
            T rowData =
        }
    }

    private boolean isEmptyRow(Row row) {
        for(Cell cell : row) {
            if(cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private T getRowData(Row row,List<ExcelCellConfig> cellConfigs,Class dataClazz){
        T instance = null;
        try{
            instance = (T)dataClazz.getDeclaredConstructor().newInstance();
            for(int i = 0 ; i<cellConfigs.size();i++){
                ExcelCellConfig currentCell = cellConfigs.get(i);
                Field field = dataClazz.getDeclaredField(currentCell.getFieldName());
                field.setAccessible(true);
                Cell cell = row.getCell(currentCell.getColumnIndex());
                if(!ObjectUtils.isEmpty(cell)){
                    cell.setCellType(CellType.STRING);
                    Object cellValue = cell.getStringCellValue();

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldValue(Objects instance, Field field,Object cellValue){
        
    }
}

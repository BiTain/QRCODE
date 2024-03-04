package com.bitain.qrcode.config;

import com.bitain.qrcode.dto.user.UserRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportConfig {
    private String sheetName;
    private Integer headerIndex;
    private Integer startRow;
    private Class dataClazz;
    private List<ExcelCellConfig> cellImportConfigs;

    public static ExcelImportConfig userImportConfig;
    public static ExcelImportConfig categoryImportConfig;
    public static ExcelImportConfig brandImportConfig;

    static {
        userImportConfig = ExcelImportConfig.builder()
                .sheetName("Users")
                .headerIndex(0)
                .startRow(1)
                .dataClazz(UserRequest.class)
                .build();

        List<ExcelCellConfig> userCellConfigs = List.of(
                new ExcelCellConfig(0, "email"),
                new ExcelCellConfig(1, "fullName"),
                new ExcelCellConfig(2, "password"),
                new ExcelCellConfig(3, "roleNames"),
                new ExcelCellConfig(4, "enabled")
        );

        userImportConfig.setCellImportConfigs(userCellConfigs);
    }


    public static ExcelImportConfig getImportConfig(String entityName) {
        return switch (entityName) {
            case "User" -> userImportConfig;
            default -> throw new IllegalArgumentException("Invalid entity name: " + entityName);
        };
    }
}

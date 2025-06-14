package com.steve.pizzeria.utils;

import com.steve.pizzeria.dto.OrderDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class OrderExcelExporter {

    public static ByteArrayInputStream exportToExcel(List<OrderDto> orders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pedidos");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Producto");
            header.createCell(2).setCellValue("Cantidad");
            header.createCell(3).setCellValue("Precio");

            int rowIdx = 1;
            for (OrderDto order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getProduct_name());
                row.createCell(2).setCellValue(order.getQuantity());
                row.createCell(3).setCellValue(order.getPrice());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}

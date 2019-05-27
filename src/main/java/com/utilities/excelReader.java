package com.utilities;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelReader {

	public static HashMap<String, String> readExcel(String sheetName, String rowval) throws IOException {
		HashMap<String, String> colvalues = new HashMap<>();
		ArrayList<String> headers = new ArrayList<>();
		String fileName = propertyReader.readingProperty("testdatafilename");
		// Create an object of File class to open xlsx file

		File file = new File(propertyReader.readingProperty("testdatasheet"));

		// Create an object of FileInputStream class to read excel file

		FileInputStream inputStream = new FileInputStream(file);

		Workbook Workbook = null;

		// Find the file extension by splitting file name in substring and getting only
		// extension name

		String fileExtensionName = fileName.substring(fileName.indexOf("."));

		// Check condition if the file is xlsx file

		if (fileExtensionName.equals(".xlsx")) {

			// If it is xlsx file then create object of XSSFWorkbook class

			Workbook = new XSSFWorkbook(inputStream);

		}

		// Check condition if the file is xls file

		else if (fileExtensionName.equals(".xls")) {

			// If it is xls file then create object of XSSFWorkbook class

			Workbook = new HSSFWorkbook(inputStream);

		}

		// Read sheet inside the workbook by its name

		Sheet Sheet = Workbook.getSheet(sheetName);
		// Find number of rows in excel file

		int rowCount = Sheet.getLastRowNum() - Sheet.getFirstRowNum();

		// Create a loop over all the rows of excel file to read it

		Row row = Sheet.getRow(0);

		if (row != null) {
			int noofcells = row.getPhysicalNumberOfCells();
			for (int c = 0; c < noofcells; c++) {
				headers.add(row.getCell(c).getStringCellValue());
			}
		}

		for (int l = 1; l < rowCount + 1; l++) {

			Row row1 = Sheet.getRow(l);

			if (row1.getCell(0).getStringCellValue().equals(rowval)) {

				for (int j = 0; j < row1.getLastCellNum(); j++) {

					colvalues.put(headers.get(j), row1.getCell(j).getStringCellValue());

				}
				break;
			}

		}
		return colvalues;

	}

}

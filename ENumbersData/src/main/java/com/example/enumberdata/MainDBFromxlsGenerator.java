package com.example.enumberdata;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.BeanUtils;


import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Iuliia on 02.10.2015.
 */
public class MainDBFromxlsGenerator {

    public static void main(String[] args) {
        ArrayList<ENumber> data = readxls("data.xls");

        XlsService xlsService = new XlsService();
        xlsService.fillSQLiteDB(data);
    }

    private static ArrayList<ENumber> readxls(String file) {
        ArrayList<ENumber> result = new ArrayList<ENumber>();

        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            int rows; // No of rows

            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) cols = tmp;
                }
            }

            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(ENumber.class); //without Spring: Introspector.getBeanInfo(data.get(0).getClass()).getPropertyDescriptors())
            ArrayList<Method> setters = new ArrayList<Method>();
            ArrayList<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>(); //filtered

            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals("class")) {
                    //excludes  <class>class com.example.ENumbersData.ENumber</class> from excel shit
                } else {
                    Method setter = pd.getWriteMethod();
                    setters.add(setter);
                    properties.add(pd);
                }
            }

            for (int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    ENumber datarow = new ENumber();
                    for (int i = 0; i < setters.size(); i++) {

                        Class<?> type = BeanUtils.findPropertyType(properties.get(i).getName(), ENumber.class);

                        Object obj = GetObjFromString(row.getCell((short) i).getRichStringCellValue().getString());

                        setters.get(i).invoke(datarow, type.cast(obj));
                    }
                    result.add((datarow));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object GetObjFromString(String value) {
        try {
            //boolean field
            if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
                return Boolean.parseBoolean(value);
            }

            //enum field
            Object[] enumValues = ENumber.DangerLevel.class.getEnumConstants();
            for (int i = 0; i < enumValues.length; i++) {
                if (value.equals(enumValues[i])) {
                    return ENumber.DangerLevel.valueOf(value);
                }
            }

            return value;


        } catch (Exception e) {
            return null; // There was some error, so return null.
        }
    }

}

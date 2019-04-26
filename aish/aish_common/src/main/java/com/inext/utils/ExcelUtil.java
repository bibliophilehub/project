package com.inext.utils;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel操作类
 *
 * @author LIUTQ
 */
public class ExcelUtil {
    public static String DateFormat = "yyyy-MM-dd HH:mm:ss";
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void setFileDownloadHeader(HttpServletRequest request,
                                             HttpServletResponse response, String fileName) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        String finalFileName = fileName;
        try {
            if (StringUtils.contains(userAgent, "firefox")) {// 火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + finalFileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (Exception e) {
            logger.error("setFileDownloadHeader error", e);
        }
    }

    /**
     * @param out
     * @param data
     * @param title LinkedHashMap
     * @return
     * @throws Exception
     */
    public static WritableWorkbook getWritableWorkbook(OutputStream out, List<Map> data, LinkedHashMap<String, String> title) throws Exception {

        int beginRow = 0;
        try {
            /** **********创建工作簿************ */
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            /** **********创建工作表************ */
            WritableSheet sheet = null;

            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);

            /** ************以下设置三种单元格样式************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行
            if (data == null || data.size() == 0) {
            } else {
                int count = 0;//记录创建了多少工作表
                for (int i = 0; i < data.size(); i++) {
                    if ((i % 60000) == 0) {//防止溢出
                        if (i > 0) {
                            count++;
                        }
                        /** **********创建工作表************ */
                        sheet = workbook.createSheet("Sheet" + (count + 1), count - 1);
                        /** **********设置纵横打印（默认为纵打）、打印纸***************** */
                        jxl.SheetSettings sheetset = sheet.getSettings();
                        sheetset.setProtected(false);

                        /** ***************以下是EXCEL第一行列标题********************* */
                        Iterator<Map.Entry<String, String>> entries = title.entrySet().iterator();
                        int n = 0;
                        while (entries.hasNext()) {
                            Map.Entry<String, String> entry = entries.next();
                            sheet.addCell(new Label(n, beginRow, entry.getValue(), wcf_center));
                            n++;
                        }
                    }
                    /** ***************以下是EXCEL正文数据********************* */
                    Iterator<Map.Entry<String, String>> entries = title.entrySet().iterator();
                    int j = 0;
                    while (entries.hasNext()) {
                        Map.Entry<String, String> entry = entries.next();
                        //System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
                        String value = "";
                        if (data.get(i).get(entry.getKey()) instanceof String) {
                            value = data.get(i).get(entry.getKey()).toString();
                        } else if (data.get(i).get(entry.getKey()) instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
                            value = sdf.format(sdf.parse(data.get(i).get(entry.getKey()).toString()));
                        } else {
                            Object obj = data.get(i).get(entry.getKey());
                            if (obj != null)
                                value = obj.toString();
                        }
                        sheet.addCell(new Label(j, i - (60000 * count) + 1, value, wcf_left));
                        j++;
                    }
                }
            }
            workbook.write();
            workbook.close();
            return workbook;
        } catch (Exception e) {
            logger.error("buildExcel error,", e);
        }
        return null;
    }

    public static void writableWorkbook(OutputStream out, List<Map<String, String>> data, LinkedHashMap<String, String> title) throws Exception {

        int beginRow = 0;
        try {
            /** **********创建工作簿************ */
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            /** **********创建工作表************ */
            WritableSheet sheet = null;

            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);

            /** ************以下设置三种单元格样式************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行
            if (data == null || data.size() == 0) {
            } else {
                int count = 0;//记录创建了多少工作表
                for (int i = 0; i < data.size(); i++) {
                    if ((i % 60000) == 0) {//防止溢出
                        if (i > 0) {
                            count++;
                        }
                        /** **********创建工作表************ */
                        sheet = workbook.createSheet("Sheet" + (count + 1), count - 1);
                        /** **********设置纵横打印（默认为纵打）、打印纸***************** */
                        jxl.SheetSettings sheetset = sheet.getSettings();
                        sheetset.setProtected(false);

                        /** ***************以下是EXCEL第一行列标题********************* */
                        Iterator<Map.Entry<String, String>> entries = title.entrySet().iterator();
                        int n = 0;
                        while (entries.hasNext()) {
                            Map.Entry<String, String> entry = entries.next();
                            sheet.addCell(new Label(n, beginRow, entry.getValue(), wcf_center));
                            n++;
                        }
                    }
                    /** ***************以下是EXCEL正文数据********************* */
                    Iterator<Map.Entry<String, String>> entries = title.entrySet().iterator();
                    int j = 0;
                    while (entries.hasNext()) {
                        Map.Entry<String, String> entry = entries.next();
                        //System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
                        String value = "";
                        if (data.get(i).get(entry.getKey()) instanceof String) {
                            value = data.get(i).get(entry.getKey()).toString();
                        } else {
                            Object obj = data.get(i).get(entry.getKey());
                            if (obj != null)
                                value = obj.toString();
                        }
                        sheet.addCell(new Label(j, i - (60000 * count) + 1, value, wcf_left));
                        j++;
                    }
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.error("buildExcel error,", e);
        }
    }

    public static void writeWorkbook(OutputStream out, List<Map<String, Object>> data, Map<String, String> title) throws Exception {

        int beginRow = 0;
        try {
            /** **********创建工作簿************ */
            WritableWorkbook workbook = Workbook.createWorkbook(out);

            /** **********创建工作表************ */
            WritableSheet sheet = null;

            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);

            /** ************以下设置三种单元格样式************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行
            if (data == null || data.size() == 0) {
            } else {
                int count = 0;//记录创建了多少工作表
                for (int i = 0; i < data.size(); i++) {
                    if ((i % 60000) == 0) {//防止溢出
                        if (i > 0) {
                            count++;
                        }
                        /** **********创建工作表************ */
                        sheet = workbook.createSheet("Sheet" + (count + 1), count - 1);
                        /** **********设置纵横打印（默认为纵打）、打印纸***************** */
                        jxl.SheetSettings sheetset = sheet.getSettings();
                        sheetset.setProtected(false);

                        /** ***************以下是EXCEL第一行列标题********************* */
                        Iterator<Map.Entry<String, String>> entries = title.entrySet().iterator();
                        int n = 0;
                        while (entries.hasNext()) {
                            Map.Entry<String, String> entry = entries.next();
                            sheet.addCell(new Label(n, beginRow, entry.getValue(), wcf_center));
                            n++;
                        }
                    }
                    /** ***************以下是EXCEL正文数据********************* */
                    Iterator<Map.Entry<String, String>> entries = title.entrySet().iterator();
                    int j = 0;
                    while (entries.hasNext()) {
                        Map.Entry<String, String> entry = entries.next();
                        //System.out.println("key:" + entry.getKey() + ",value:" + entry.getValue());
                        String value = "";
                        if (data.get(i).get(entry.getKey()) instanceof String) {
                            value = data.get(i).get(entry.getKey()).toString();
                        } else {
                            Object obj = data.get(i).get(entry.getKey());
                            if (obj != null)
                                value = obj.toString();
                        }
                        sheet.addCell(new Label(j, i - (60000 * count) + 1, value, wcf_left));
                        j++;
                    }
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.error("buildExcel error,", e);
        }
    }

    /**
     * 创建excel文档并写入outStream输出流中<br>
     * 单sheet导出，65536行以内<br>
     *
     * @param outStream 输出流
     * @param mainTitle 导出的sheet标题
     * @param titles    标题
     * @param contents  内容
     */
    public final static void buildExcel(OutputStream outStream,
                                        String mainTitle, String[] titles, List<Object[]> contents) {
        int beginRow = 0;
        try {
            /** **********创建工作簿************ */
            WritableWorkbook workbook = Workbook.createWorkbook(outStream);

            /** **********创建工作表************ */
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);

            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);

            /** ************以下设置三种单元格样式************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行

            // excel大标题
            if (mainTitle != null && !mainTitle.equals("")) {
                sheet.mergeCells(0, 0, titles.length - 1, 0);
                sheet.addCell(new Label(0, 0, mainTitle, wcf_center));
                beginRow = beginRow + 1;
            }

            /** ***************以下是EXCEL第一行列标题********************* */
            if (titles != null && titles.length > 1) {
                for (int i = 0; i < titles.length; i++) {
                    sheet.addCell(new Label(i, beginRow, titles[i], wcf_center));
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL正文数据********************* */
            for (int i = 0; i < contents.size(); i++) {// row
                Object[] rowContent = contents.get(i);
                for (int j = 0; j < titles.length; j++) { // cell
                    String content = "";
                    if (j < rowContent.length) {
                        if (rowContent[j] != null
                                && !"null".equals(rowContent[j])
                                && !"".equals(rowContent[j])) {
                            content = String.valueOf(rowContent[j]);
                            if (rowContent[j] instanceof Math) {
                                if ("null".equals(content)) {
                                    content = "0.00";
                                }
                            }
                        }
                    }
                    sheet.addCell(new Label(j, i + beginRow, content, wcf_left));
                }
            }
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            logger.error("buildExcel error,", e);
        }
    }

    /**
     * 多sheet导出
     *
     * @param workbook
     * @param mainTitle
     * @param titles
     * @param contents
     * @param sheetNum
     * @param totalSheet
     */
    public final static void buildExcel(WritableWorkbook workbook,
                                        String mainTitle, String[] titles, List<Object[]> contents,
                                        int sheetNum, int totalSheet) {
        int beginRow = 0;
        try {
            /** **********创建工作表************ */
            WritableSheet sheet = workbook.createSheet("Sheet" + sheetNum,
                    sheetNum);

            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);

            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);

            /** ************以下设置三种单元格样式************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_left.setWrap(false); // 文字是否换行

            // excel大标题
            if (mainTitle != null && !mainTitle.equals("")) {
                sheet.mergeCells(0, 0, titles.length - 1, 0);
                sheet.addCell(new Label(0, 0, mainTitle, wcf_center));
                beginRow = beginRow + 1;
            }

            /** ***************以下是EXCEL第一行列标题********************* */
            if (titles != null && titles.length > 1) {
                for (int i = 0; i < titles.length; i++) {
                    sheet.addCell(new Label(i, beginRow, titles[i], wcf_center));
                }
                beginRow = beginRow + 1;
            }
            /** ***************以下是EXCEL正文数据********************* */
            for (int i = 0; i < contents.size(); i++) {// row
                Object[] rowContent = contents.get(i);
                for (int j = 0; j < titles.length; j++) { // cell
                    String content = "";
                    if (j < rowContent.length) {
                        content = String.valueOf(rowContent[j]);
                        if ("null".equals(content)) {
                            content = "";
                        }
                    }
                    sheet.addCell(new Label(j, i + beginRow, content, wcf_left));
                }
            }
            if (totalSheet == sheetNum) {
                workbook.write();
                workbook.close();
            }

        } catch (Exception e) {
            logger.error("buildExcel error,", e);
        }
    }

}

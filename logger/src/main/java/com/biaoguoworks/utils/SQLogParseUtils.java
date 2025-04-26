package com.biaoguoworks.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wuxin
 * @date 2025/04/26 19:36:52
 */
public class SQLogParseUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String regex = "\\?";
    private static final Pattern compile = Pattern.compile(regex);

    public static String setParamToRawSql(String rawSql, List<Object> parma){
        if(Objects.isNull(parma) || parma.isEmpty()){
            return rawSql;
        }
        StringBuffer result = new StringBuffer();
        Matcher matcher = compile.matcher(rawSql);
        int index = 0;
        while (matcher.find()){
            String valueAsString = "";
            if(index < parma.size()){
                valueAsString = parseValueToString(parma.get(index));
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(valueAsString));
            index++;
        }
        matcher.appendTail(result);
        return result.toString();
    }


    public static String pretty(String sql){
        if(Objects.isNull(sql)){
            return null;
        }
        return sql.replaceAll("[\n\\s]+"," ");
    }


    public static String parseValueToString(Object e){
        if (Objects.isNull(e)) {
            return "";
        }
        if (e instanceof String) {
            String str = (String) e;
            str = str.replace("'", "''");
            return "'" + str + "'";
        }
        if (e instanceof LocalDateTime) {
            return "'" + ((LocalDateTime) e).format(DATE_TIME_FORMATTER) + "'";
        }
        if (e instanceof LocalDate) {
            return "'" + ((LocalDate) e).format(DATE_FORMATTER) + "'";
        }
        if (e instanceof java.util.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + sdf.format((Date) e) + "'";
        }
        return e.toString();
    }
}

package org.example.demo_ssr_v1_v1._core.utils;


import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class MyDateUtil {

    // 정적 메서드 (기능) 시간 포맷터 기능
    // 정적 변수로 포맷터를 선언해두면 성능상 더 유리
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String timestampFormat(Timestamp time) {
        if (time == null) {
            return null;
        }
        return time.toLocalDateTime().format(FORMATTER);
    }
//    public static LocalDate getCurrentDate() {
//        return LocalDate.now();
//    }
//
//    public static String getCurrentDate(String pattern) {
//        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
//    }
}

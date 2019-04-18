package com.spring.server;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

public class Common {
    private Common() {

    }
    private static class LazyHolder {
        public static final Common INSTANCE = new Common();
    }
    public static Common getInstance() {
        return LazyHolder.INSTANCE;
    }

    public StringBuffer createJson(HttpServletRequest request) {
        String line = null;
        StringBuffer json = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

}

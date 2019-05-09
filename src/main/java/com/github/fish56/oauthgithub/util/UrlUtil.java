package com.github.fish56.oauthgithub.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class UrlUtil {
    public static Map<String, String> queryToMap(String query){
        String decodedString = query;
        // String decodedString = URLDecoder.decode(query, StandardCharsets.UTF_8);

        Map<String, String> query_pairs = new LinkedHashMap<String, String>();

        String[] pairs = decodedString.split("&");

        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }

        return query_pairs;
    }
}

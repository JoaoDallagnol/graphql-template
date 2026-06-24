package com.graphql.template.Utils;

import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor
public class CursorUtil {
    public static String encode(Long id) {
        String raw = String.valueOf(id);
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    // Decodes a Base64 cursor string back into its original ID
    public static Long decode(String cursor) {
        byte[] decodedBytes = Base64.getDecoder().decode(cursor);
        String raw = new String(decodedBytes, StandardCharsets.UTF_8);
        return Long.parseLong(raw);
    }
}

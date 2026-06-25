package com.graphql.template.Utils;

import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility for encoding/decoding cursor values.
 * Converts ID integers to Base64 strings to hide internal structure.
 * Cursor format is opaque to client — only used for pagination.
 */
@NoArgsConstructor
public class CursorUtil {
    /**
     * Encodes an ID into a Base64 cursor string.
     * Example: 42 -> "NDI=" (Base64 of "42")
     *
     * @param id the numeric ID to encode
     * @return Base64 encoded cursor
     */
    public static String encode(Long id) {
        String raw = String.valueOf(id);
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes a Base64 cursor string back into its original ID.
     * Reverse of encode(). Used when client sends cursor with pagination request.
     *
     * @param cursor Base64 encoded cursor string
     * @return decoded numeric ID
     */
    public static Long decode(String cursor) {
        byte[] decodedBytes = Base64.getDecoder().decode(cursor);
        String raw = new String(decodedBytes, StandardCharsets.UTF_8);
        return Long.parseLong(raw);
    }
}

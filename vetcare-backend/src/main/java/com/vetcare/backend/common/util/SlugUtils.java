package com.vetcare.backend.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private SlugUtils() {}

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");

        String normalized = noWhitespace.replace("ł", "l").replace("Ł", "L");
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);

        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH)
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
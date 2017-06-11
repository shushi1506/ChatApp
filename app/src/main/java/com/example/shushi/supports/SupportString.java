package com.example.shushi.supports;

/**
 * Created by Shushi on 4/24/2017.
 */

public final class SupportString {
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
    public static String addSuffixeString(String string) {
        return string+"@halo.com";
    }
    public static String subSuffixeString(String string) {
        return string.replace("@halo.com","").trim();
    }
}

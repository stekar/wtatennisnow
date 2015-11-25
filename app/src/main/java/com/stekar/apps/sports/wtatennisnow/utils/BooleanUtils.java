package com.stekar.apps.sports.wtatennisnow.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by stekar on 1/11/15.
 */
public final class BooleanUtils {
    private static String BOOLEAN_TRUE = "true";
    private static String ORDINAL_TRUE = "1";


    public static boolean isOrdinalTrue(String bit) {
        return bit.equalsIgnoreCase(BOOLEAN_TRUE);
    }

    // "1" or "0" as input
    public static Boolean ordinalToBoolean(String bit) {
        boolean ordinalBooleanValue = false;

        if(bit.equalsIgnoreCase(ORDINAL_TRUE)) {
            ordinalBooleanValue = Boolean.TRUE;
        }

        return ordinalBooleanValue;
    }

    public static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
        @Override public void write(JsonWriter out, Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }
        @Override public Boolean read(JsonReader in) throws IOException {
            JsonToken peek = in.peek();
            switch (peek) {
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return in.nextInt() != 0;
                case STRING:
                    return BooleanUtils.ordinalToBoolean(in.nextString());
                default:
                    throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
            }
        }
    };
}

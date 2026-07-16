package com.hmdm.launcher.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CryptoHelperTest {
    @Test
    public void removesOnlyJsonWhitespaceFromSignedPayload() {
        assertEquals("{\"name\":\"Health\u00A0Connect\"}",
                CryptoHelper.removeJsonWhitespace(" {\n\t\"name\" : \"Health\u00A0Connect\" } "));
    }
}

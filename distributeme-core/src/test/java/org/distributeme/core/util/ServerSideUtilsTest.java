package org.distributeme.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerSideUtilsTest {
    @Test
    public void testShouldSecurityManagerBeSet() {
        assertEquals(true, ServerSideUtils.shouldSecurityManagerBeSet("1.8.0_311"));
        assertEquals(false, ServerSideUtils.shouldSecurityManagerBeSet("21"));
        assertEquals(false, ServerSideUtils.shouldSecurityManagerBeSet("21.0.1"));
        assertEquals(false, ServerSideUtils.shouldSecurityManagerBeSet("21.0.1-LTE-ea"));
    }
}

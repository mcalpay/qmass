package org.mca.ir;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.*;

/**
 * User: malpay
 * Date: 24.May.2011
 * Time: 10:43:34
 */
public class IRTests {

    @Test
    public void devQMassIRisJavaLangString() throws Exception {
        assertTrue(IR.get("dev", "QMassIR") instanceof String);
    }

    @Test
    public void webQMassIRisArrayList() throws Exception {
        assertTrue(IR.get("web", "QMassIR") instanceof ArrayList);
    }

}

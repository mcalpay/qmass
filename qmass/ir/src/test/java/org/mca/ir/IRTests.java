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
    public void defaultQMassIRisJavaLangString() throws Exception {
        assertTrue(IR.get(new IRKey("default", "QMassIR")) instanceof String);
    }

    @Test
    public void webQMassIRisJavaLangString() throws Exception {
        assertTrue(IR.get(new IRKey("web", "QMassIR")) instanceof ArrayList);
    }
}

package org.mca.qmass.cache;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import org.mca.ir.IR;
import org.mca.qmass.core.ir.DefaultQMassIR;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.ir.QMassIR;

import java.util.ArrayList;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 13:48:41
 */
public class DefaultQCacheTests {

    @Before
    public void configure() {
        IR.putIR(QMassIR.class, new DefaultQMassIR() {
            @Override
            public int getDefaultThreadWait() {
                return 100;
            }
        });
    }
    
    @Test
    public void removeFromOneQmassInstanceCheckIfItsRemovedFromOtherToo() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
        QCache c1 = new DefaultQCache("cache",q1,null,null);
        QCache c2 = new DefaultQCache("cache",q2,null,null);
        c1.put("1L","Test");
        c2.put("1L","Test");
        assertNotNull(c2.get("1L"));
        c1.remove("1L");
        Thread.sleep(250);
        assertNull(c2.get("1L"));
        q1.end();
        q2.end();
    }

    @Test
    public void clearFromOneQmassInstanceCheckIfOtherIsCleared() throws Exception {
        QMass q1 = new QMass("test");
        QMass q2 = new QMass("test");
        Thread.sleep(250);
        QCache c1 = new DefaultQCache("cache",q1,null,new ArrayList());
        QCache c2 = new DefaultQCache("cache",q2,null,new ArrayList());
        c1.put("1L","Test");
        c2.put("1L","Test");
        assertNotNull(c2.get("1L"));
        c1.clear();
        Thread.sleep(250);
        assertNull(c2.get("1L"));
        q1.end();
        q2.end();
    }

}

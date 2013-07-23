/*
 * Copyright 2011 MCA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mca.qmass.event;

import org.junit.Assert;
import org.junit.Test;
import org.mca.qmass.core.QMass;
import org.mca.qmass.core.event.NOOPService;
import org.mca.utils.io.BufferedPrintStream;
import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

/**
 * User: malpay
 * Date: 09.May.2011
 * Time: 10:44:13
 */
public class LogChangeEventTests {

    private static BufferedPrintStream errorStream;

    static {
        errorStream = new BufferedPrintStream(System.err);
        System.setErr(errorStream);
    }

    @Test
    public void changeLogLevels() throws Exception {
        YALog log = YALogFactory.getLog(LogChangeEventTests.class);
        String testBefore = "testing before";
        String testAfter = "testing after";
        log.debug(testBefore);
        Assert.assertFalse(errorStream.getBuffer().contains(testBefore));
        new DefaultLogService("log",QMass.getQMass()).changeLog("org", "DEBUG");
        Thread.sleep(2000);
        log.debug(testAfter);
        Assert.assertTrue(errorStream.getBuffer().contains(testAfter));
        QMass.getQMass().end();
    }

}

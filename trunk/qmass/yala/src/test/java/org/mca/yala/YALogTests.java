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
package org.mca.yala;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mca.utils.io.BufferedPrintStream;

import java.io.*;

/**
 * User: malpay
 * Date: 16.07.2013
 * Time: 06:03
 */
public class YALogTests {

    private static BufferedPrintStream errorStream;

    static {
        errorStream = new BufferedPrintStream(System.err);
        System.setErr(errorStream);
    }

    @Before
    public void setup() {
        errorStream.resetBuffer();
    }

    @Test
    public void testDefaultLogLevels() throws Exception {
        String testInfo = "testInfo1";
        String testDebug = "testDebug1";
        YALog logger = YALogFactory.getLog(YALogTests.class);
        logger.info(testInfo);
        logger.debug(testDebug);
        Assert.assertTrue(errorStream.getBuffer().contains(testInfo));
        Assert.assertFalse(errorStream.getBuffer().contains(testDebug));
    }

    @Test
    public void testEnableDebugLogLevelsWithManager() throws Exception {
        String testDebug = "testDebug2";
        YALog logger = YALogFactory.getLog(YALogTests.class);
        YALogManager.setLoggerLevel(YALogTests.class.getName(), "DEBUG");
        logger.debug(testDebug);
        Assert.assertTrue(errorStream.getBuffer().contains(testDebug));
    }

    @Test
    public void testEnableParentsDebugLogLevelsWithManager() throws Exception {
        String testDebug = "testDebug3";
        YALog logger = YALogFactory.getLog(YALogTests.class);
        Assert.assertFalse(errorStream.getBuffer().contains(testDebug));
        YALogManager.setLoggerLevel("org.mca", "DEBUG");
        logger.debug(testDebug);
        Assert.assertTrue(errorStream.getBuffer().contains(testDebug));
    }

}

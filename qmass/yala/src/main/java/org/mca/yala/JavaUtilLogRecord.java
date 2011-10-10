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

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * User: malpay
 * Date: 10.10.2011
 * Time: 15:04
 */
public class JavaUtilLogRecord extends LogRecord {

    private String sourceClassName;

    private String sourceMethodName;

    public JavaUtilLogRecord(Level level, String msg) {
        super(level, msg);
    }

    @Override
    public String getSourceClassName() {
        if (sourceClassName == null) {
            StackTraceElement stack[] = new Throwable().getStackTrace();
            sourceClassName = stack[6].getClassName();
            sourceMethodName = stack[6].getMethodName();
        }
        return sourceClassName;
    }

    @Override
    public String getSourceMethodName() {
        if (sourceMethodName == null) {
            StackTraceElement stack[] = new Throwable().getStackTrace();
            sourceClassName = stack[6].getClassName();
            sourceMethodName = stack[6].getMethodName();
        }
        return sourceMethodName;
    }
}

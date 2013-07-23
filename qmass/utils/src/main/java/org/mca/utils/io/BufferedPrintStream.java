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
package org.mca.utils.io;

import java.io.PrintStream;
import java.io.StringWriter;

/**
 * User: malpay
 * Date: 16.07.2013
 * Time: 08:18
 */
public class BufferedPrintStream extends PrintStream {

    private StringWriter sb;

    public BufferedPrintStream(PrintStream stream) {
        super(stream);
        this.sb = new StringWriter();
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        String str = new String(buf).trim();
        sb.append(str);
        super.write(buf, off, len);
    }

    public String getBuffer() {
        return sb.toString();
    }

    public void resetBuffer() {
        sb = new StringWriter();
    }
}
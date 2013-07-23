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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: malpay
 * Date: 15.07.2013
 * Time: 07:55
 */
public class NonBlockingLineReader extends BufferedReader {

    private boolean lineReady = false;

    private StringBuilder linePeek = new StringBuilder();

    public NonBlockingLineReader(InputStreamReader reader) {
        super(reader);
    }

    @Override
    public String readLine() throws IOException {
        if(lineReady()) {
            String line = linePeek.toString();
            linePeek = new StringBuilder();
            lineReady = false;
            return line;
        }
        return null;
    }

    public boolean lineReady() {
        if (lineReady) {
            return true;
        } else {
            try {
                while(ready()) {
                    char c = (char) read();
                    if(c == '\n') {
                        lineReady = true;
                        break;
                    } else {
                        linePeek.append(c);
                    }
                }
                return lineReady;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

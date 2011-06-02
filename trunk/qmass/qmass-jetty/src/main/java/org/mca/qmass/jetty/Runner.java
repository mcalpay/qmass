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
package org.mca.qmass.jetty;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 30.May.2011
 * Time: 15:19:58
 */
public class Runner {

    public static void main(String[] args) throws Exception {
        int len = 5;
        if (args.length > 0) {
            len = Integer.valueOf(args[0]);
        }

        int port = 8080;
        if (args.length > 1) {
            port = Integer.valueOf(args[1]);
        }

        int i = 0;
        final List<Process> processes = new ArrayList<Process>(len);
        final List<BufferedReader> inputs = new ArrayList<BufferedReader>(len);
        final List<BufferedReader> errors = new ArrayList<BufferedReader>(len);
        final List<String> ids = new ArrayList<String>(len);
        while (len > i) {
            System.out.println("Starting at port " + port);
            Process p = Runtime.getRuntime().exec("java -jar qmass_demo.jar " + port + " " + i);
            processes.add(p);
            inputs.add(new BufferedReader(new InputStreamReader(p.getInputStream())));
            ids.add(Integer.toString(port));
            errors.add(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            i++;
            port++;
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                for (Process p : processes) {
                    p.destroy();
                }
                System.out.println("Bye");
            }
        });

        while (true) {
            int j = 0;
            for (BufferedReader reader : errors) {
                if (reader.ready()) {
                    System.out.println(ids.get(j) + " err>" + reader.readLine());
                }
                j++;
            }

            j = 0;
            for (BufferedReader reader : inputs) {
                if (reader.ready()) {
                    System.out.println(ids.get(j) + " out>" + reader.readLine());
                }
                j++;
            }
        }
    }
}

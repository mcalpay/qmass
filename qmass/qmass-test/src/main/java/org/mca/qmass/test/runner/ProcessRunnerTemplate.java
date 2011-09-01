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
package org.mca.qmass.test.runner;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 08.Tem.2011
 * Time: 16:10:08
 */
public abstract class ProcessRunnerTemplate extends Thread {

    private Integer numberOfInstances = 5;

    private List<Process> processes = new ArrayList<Process>(numberOfInstances);

    private List<BufferedReader> inputs = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedReader> errors = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedOutputStream> inputsStream = new ArrayList<BufferedOutputStream>(numberOfInstances);

    private List<BufferedOutputStream> errorsStream = new ArrayList<BufferedOutputStream>(numberOfInstances);

    private volatile boolean running = true;

    private String outputDir;

    public ProcessRunnerTemplate(Integer numberOfInstances, String outputDir) {
        this.numberOfInstances = numberOfInstances;
        this.outputDir = outputDir;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                end();
            }
        });
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (numberOfInstances > i) {
                Process p = Runtime.getRuntime().exec(getRunString());
                processes.add(p);
                if (isTrackInputStreams()) {
                    inputs.add(new BufferedReader(new InputStreamReader(p.getInputStream())));
                    inputsStream.add(new BufferedOutputStream(
                            new FileOutputStream(getOutputDir() + i + ".in")));
                }

                if (isTrackErrorStreams()) {
                    errors.add(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                    errorsStream.add(new BufferedOutputStream(new FileOutputStream(getOutputDir() + i + ".err")));
                }
                i++;
            }

            while (running) {
                int j = 0;
                for (BufferedReader reader : inputs) {
                    if (reader.ready()) {
                        String line = reader.readLine() + "\n";
                        BufferedOutputStream bos = inputsStream.get(j);
                        bos.write(line.getBytes());
                        bos.flush();
                    }

                    j++;
                }

                j = 0;
                for (BufferedReader reader : errors) {
                    if (reader.ready()) {
                        String line = reader.readLine() + "\n";
                        BufferedOutputStream bos = inputsStream.get(j);
                        bos.write(line.getBytes());
                        bos.flush();
                    }

                    j++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isTrackErrorStreams() {
        return true;
    }

    protected boolean isTrackInputStreams() {
        return true;
    }

    private String getOutputDir() {
        return outputDir;
    }

    public void end() {
        try {
            for (BufferedOutputStream bos : inputsStream) {
                bos.close();
            }

            for (BufferedOutputStream bos : errorsStream) {
                bos.close();
            }

        } catch (Exception e) {
            System.out.println("Exception closing streams");
        }

        for (Process p : processes) {
            p.destroy();
        }

        System.out.println("Bye");
        running = false;
    }

    protected abstract String getRunString();
}

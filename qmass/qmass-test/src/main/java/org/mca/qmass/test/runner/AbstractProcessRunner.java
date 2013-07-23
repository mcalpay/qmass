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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 08.Tem.2011
 * Time: 16:10:08
 */
public abstract class AbstractProcessRunner extends Thread {

    private Integer numberOfInstances = 5;

    private List<Process> processes = new ArrayList<Process>(numberOfInstances);

    private List<BufferedReader> inputs = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedReader> errors = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedOutputStream> outputStreams = new ArrayList<BufferedOutputStream>(numberOfInstances);

    private volatile boolean running = true;

    private String outputDir;

    public AbstractProcessRunner(Integer numberOfInstances, String outputDir) {
        this.numberOfInstances = numberOfInstances;
        this.outputDir = outputDir;
        File of = new File(outputDir);
        if(!of.exists()) {
            of.mkdir();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                end();
            }
        });
    }

    @Override
    public void run() {
        while (running) {
            int j = 0;
            for (BufferedReader reader : errors) {
                try {
                    if (reader.ready()) {
                        String line = reader.readLine() + "\n";
                        BufferedOutputStream bos = outputStreams.get(j);
                        bos.write(line.getBytes());
                        bos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }

                j++;
            }
        }
    }

    public void initProcesses() throws IOException {
        int i = 0;
        while (numberOfInstances > i) {
            String runString = getRunString();
            System.out.println(runString);
            Process p = Runtime.getRuntime().exec(runString);
            processes.add(p);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(getOutputDir() + i + ".in"));
            inputs.add(inputReader);
            outputStreams.add(outputStream);

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            errors.add(errorReader);

            boolean pIsReady = false;
            while (!pIsReady) {
                if (inputReader.ready()) {
                    String line = inputReader.readLine() + "\n";
                    pIsReady = line.contains("Welcome to QMassConsole!");
                }
                if (errorReader.ready()) {
                    String line = errorReader.readLine() + "\n";
                    outputStream.write(line.getBytes());
                    outputStream.flush();
                }
            }

            i++;
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
            for (BufferedOutputStream bos : outputStreams) {
                bos.flush();
            }
        } catch (Exception e) {
            System.err.println("Exception closing streams");
        }

        for (Process p : processes) {
            p.destroy();
        }

        System.err.println("Bye");
        running = false;
    }

    protected abstract String getRunString();

}

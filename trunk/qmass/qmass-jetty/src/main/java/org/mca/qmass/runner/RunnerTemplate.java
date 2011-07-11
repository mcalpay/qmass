package org.mca.qmass.runner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: malpay
 * Date: 08.Tem.2011
 * Time: 16:10:08
 */
public abstract class RunnerTemplate extends Thread {

    private Integer numberOfInstances = 5;

    private List<Process> processes = new ArrayList<Process>(numberOfInstances);

    private List<BufferedReader> inputs = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedReader> errors = new ArrayList<BufferedReader>(numberOfInstances);

    private boolean runing = true;

    public RunnerTemplate(Integer numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                for (Process p : processes) {
                    p.destroy();
                }
                System.out.println("Bye");
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
                if (isTrackPrints()) {
                    inputs.add(new BufferedReader(new InputStreamReader(p.getInputStream())));
                    errors.add(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                }
                i++;
            }

            while (runing) {
                int j = 0;
                for (BufferedReader reader : errors) {
                    if (reader.ready()) {
                        System.out.println(j + " err>" + reader.readLine());
                    }
                    j++;
                }

                j = 0;
                for (BufferedReader reader : inputs) {
                    if (reader.ready()) {
                        System.out.println(j + " out>" + reader.readLine());
                    }
                    j++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isTrackPrints() {
        return true;
    }

    public void end() {
        runing = false;
    }

    public Integer getNumberOfInstances() {
        return numberOfInstances;
    }

    protected abstract String getRunString();
}

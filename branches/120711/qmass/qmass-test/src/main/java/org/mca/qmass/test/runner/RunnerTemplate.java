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
public abstract class RunnerTemplate extends Thread {

    private Integer numberOfInstances = 5;

    private List<Process> processes = new ArrayList<Process>(numberOfInstances);

    private List<BufferedReader> inputs = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedReader> errors = new ArrayList<BufferedReader>(numberOfInstances);

    private List<BufferedOutputStream> inputsStream = new ArrayList<BufferedOutputStream>(numberOfInstances);

    private List<BufferedOutputStream> errorsStream = new ArrayList<BufferedOutputStream>(numberOfInstances);


    private boolean runing = true;

    public RunnerTemplate(Integer numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
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

                inputs.add(new BufferedReader(new InputStreamReader(p.getInputStream())));
                inputsStream.add(new BufferedOutputStream(
                        new FileOutputStream("f:/dists/" + i + ".in")));

//                errors.add(new BufferedReader(new InputStreamReader(p.getErrorStream())));
//                errorsStream.add(new BufferedOutputStream(new FileOutputStream("f:/dists/" + i + ".err")));
                i++;
            }

            while (runing) {
                int j = 0;
                for (BufferedReader reader : inputs) {
                    if (reader.ready()) {
                        String line = reader.readLine()+ "\n";
                        //System.out.println(j + " out>" + line);
                        inputsStream.get(j).write(line.getBytes());
                    }

                    j++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void end() {
        runing = false;
    }

    public Integer getNumberOfInstances() {
        return numberOfInstances;
    }

    protected abstract String getRunString();
}

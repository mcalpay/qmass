package org.mca.qmass.test;

import com.hazelcast.core.Hazelcast;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import org.mca.qmass.runner.MainArgs;
import org.mca.qmass.runner.RunnerTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 13:37:16
 */
public class DistributeAFileAndGetItBackHz {
    public static void main(String... args) throws Exception {
        int numOfInstances = MainArgs.getNumberOfInstances(args);
        RunnerTemplate rt = new RunnerTemplate(numOfInstances) {

            @Override
            protected boolean isTrackPrints() {
                return true;
            }

            @Override
            protected String getRunString() {
                String elConsole = "java -cp " +
                        "qmass.jar;" +
                        "dependencies/commons-logging-1.1.1.jar;" +
                        "dependencies/log4j-1.2.16.jar;" +
                        "dependencies/hazelcast-1.9.2.jar" +
                        " " +
                        "org.mca.qmass.test.DistributeAFileAndGetItBackHzMain";
                return elConsole;
            }

        };

        rt.start();
        Map grid = Hazelcast.getMap("m");

        Thread.sleep(15000);
        // wait till the cluster is up
        System.out.println("Start...");
        long startTime = System.currentTimeMillis();
        BufferedInputStream is = new BufferedInputStream(new FileInputStream("f:/kbs.JPG"));
        //BufferedInputStream is = new BufferedInputStream(new FileInputStream("f:/file.txt"));
        int totalChunks = 0;
        int len = 256;
        //int len = 3;

        while (is.available() != 0) {
            byte chunk[] = new byte[len];
            int red = is.read(chunk, 0, len);
            if (red < len) {
                byte correctChunk[] = new byte[red];
                System.arraycopy(chunk, 0, correctChunk, 0, red);
                chunk = correctChunk;
            }
            totalChunks++;

            //System.out.println("READ> " + totalChunks + ", " + new String(chunk));
            grid.put(totalChunks, chunk);
        }

        is.close();
        
        long putEndTime = System.currentTimeMillis();

        System.err.println("Total # of chunks : " + totalChunks);

        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream("f:/rewrite.JPG"));
        //BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream("f:/refile.txt"));
        int i = 1;
        while (i <= totalChunks) {
            byte chunk[] = (byte[]) grid.get(i);
            //System.out.print("WRITE> " + i + ", ");
            //System.out.println(new String(chunk));
            os.write(chunk);
            i++;
        }
        os.close();


        long endTime = System.currentTimeMillis();
        System.out.println("Spent on get/put : " + (endTime - startTime) +
                ", put : " + (putEndTime - startTime) +
                ", get : " + (endTime - putEndTime));
    }

}

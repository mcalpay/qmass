package org.mca.qmass.test.grid;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 15:11:20
 */
public class ThreadsWatcher {

    private int numOfRunning = 0;

    public synchronized boolean isAllDone() {
        return (numOfRunning == 0) ? true : false;
    }

    public synchronized void started() {
        numOfRunning++;
    }

    public synchronized void done() {
        numOfRunning--;
    }
}

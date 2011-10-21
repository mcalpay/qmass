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
package org.mca.qmass.persistence;

import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * User: malpay
 * Date: 20.10.2011
 * Time: 15:37
 */
public abstract class AbstractQueueRunnable<T> implements Runnable {

    protected final YALog log = YALogFactory.getLog(getClass());

    private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

    public void queue(T obj) {
        queue.offer(obj);
    }

    private void end() {
        log.info("ending.");
        queue.clear();
        queue = null;
    }

    @Override
    public void run() {
        initJustBeforeRun();
        try {
            while (true) {

                doOnQueuedElement(queue.take());

            }
        } catch (InterruptedException e) {
        }
        end();
    }

    protected abstract void initJustBeforeRun();

    protected abstract void doOnQueuedElement(T take);

}

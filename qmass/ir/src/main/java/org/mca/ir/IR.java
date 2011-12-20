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
package org.mca.ir;

import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 09:15:34
 */
public class IR {

    private static final YALog logger = YALogFactory.getLog(IR.class);
    private static IR instance = new IR();
    private Map<String[], Object> irs = new HashMap<String[], Object>();
    private Map<String[], Object> defaultIrs = new HashMap<String[], Object>();

    private IR() {
        load("/ir.properties", irs);
        load("/ir-defaults.properties", defaultIrs);
    }

    private void load(String path, Map<String[], Object> map) {
        Properties props = new Properties();
        try {
            InputStream resourceAsStream = IR.class.getResourceAsStream(path);
            if (resourceAsStream != null) {
                props.load(
                        resourceAsStream);
                for (Object keyObj : props.keySet()) {
                    String key = (String) keyObj;
                    try {
                        Object obj = Class.forName(props.getProperty(key.toString())).newInstance();
                        map.put(key.split(","), obj);
                        logger.info("configured : " + key + ", " + obj.getClass().getName());
                    } catch (Exception e) {
                        logger.warn("can't configure : " + key + " ", e);
                    }
                }
            } else {
                logger.warn(path + " does not exist");
            }
        } catch (Exception e) {
            logger.warn("can't load " + path, e);
        }
    }

    public static <R> R get(String... keys) {
        for (String[] args : instance.irs.keySet()) {
            boolean found = true;
            for (String k : keys) {
                boolean allfound = false;
                for (String arg : args) {
                    if (arg.equals(k)) {
                        allfound = true;
                        break;
                    }
                }

                if (!allfound) {
                    found = false;
                    break;
                }
            }

            if (found) {
                R r = (R) instance.irs.get(args);
                //logger.info("returning " + r.getClass().getName() + " for " + Arrays.asList(keys));
                return r;
            }
        }

        for (String[] args : instance.defaultIrs.keySet()) {
            for (String k : keys) {
                for (String arg : args) {
                    if (arg.equals(k)) {
                        R r = (R) instance.defaultIrs.get(args);
                        //logger.info("returning " + r.getClass().getName() + " for " + Arrays.asList(keys));
                        return r;
                    }
                }
            }
        }
        throw new RuntimeException("cant configure " + Arrays.asList(keys));
    }

    public static void put(Object obj, String... keys) {
        instance.irs.put(keys, obj);
    }

}

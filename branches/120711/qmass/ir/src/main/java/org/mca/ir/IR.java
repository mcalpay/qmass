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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: malpay
 * Date: 27.Nis.2011
 * Time: 09:15:34
 */
public class IR {

    private static final Log logger = LogFactory.getLog(IR.class);

    private static IR instance = new IR();

    private Map<Serializable, Map> irs = new HashMap<Serializable, Map>();

    private IR() {
        Properties props = new Properties();
        try {
            props.load(
                    IR.class.getResourceAsStream("/ir.properties"));
            for (Object key : props.keySet()) {
                String keyName = (String) key;
                String key1 = keyName.split(",")[0];
                String key2 = keyName.split(",")[1];
                Map irMap = irs.get(key2);
                if (irMap == null) {
                    irMap = new HashMap();
                    irs.put(key2, irMap);
                }

                try {
                    Object obj = Class.forName(props.getProperty(key.toString())).newInstance();
                    irMap.put(key1, obj);
                    logger.info("configured : " + key + ", " + obj);
                } catch (Exception e) {
                    logger.warn("can't configure : " + key + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.warn("can't load /ir.properties, will use defaults : " + e.getMessage());
        }
    }

    public static <R> R get(IRKey key) {
        if (instance != null && instance.irs != null) {
            Map irMap = instance.irs.get(key.getType());
            return (R) irMap.get(key.getId());
        }
        return null;
    }

    public static IR put(IRKey key, Object obj) {
        Map irMap = instance.irs.get(key.getType());
        if (irMap == null) {
            irMap = new HashMap();
            instance.irs.put(key.getId(), irMap);
        }

        irMap.put(key.getId(), obj);
        return instance;
    }

    public static IR putIfDoesNotContain(IRKey key, Object obj) {
        Map irMap = instance.irs.get(key.getType());
        if (irMap == null) {
            irMap = new HashMap();
            instance.irs.put(key.getType(), irMap);
        }

        if (!irMap.containsKey(key.getId())) {
            irMap.put(key.getId(), obj);
        }
        return instance;
    }

    public static <R> R get(IRKey key, Object def) {
        R res = IR.<R>get(key);
        if (res == null) {
            put(key, def);
            res = (R) def;
        }
        return res;
    }

}

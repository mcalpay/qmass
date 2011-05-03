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

    private Map<Serializable,Object> map = new HashMap<Serializable,Object>();

    private IR() {
        Properties props = new Properties();
        try {
            props.load(
                    IR.class.getResourceAsStream("/ir.properties"));
            for (Object key : props.keySet()) {
                try {
                    Object obj = Class.forName(props.getProperty(key.toString())).newInstance();
                    map.put(Class.forName((String) key), obj);
                } catch (Exception e) {
                    logger.warn("can't configure : " + key + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.warn("can't load /ir.properties, will use defaults : " + e.getMessage());
        }
    }

    public static <R> R get(Serializable id) {
        return (R) instance.map.get(id);
    }

    public static IR put(Serializable id, Object obj) {
        instance.map.put(id, obj);
        return instance;
    }
}

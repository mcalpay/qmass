package org.mca.qmass.http;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: malpay
 * Date: 25.May.2011
 * Time: 13:49:16
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface Shared {
}

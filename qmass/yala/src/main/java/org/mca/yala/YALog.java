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
package org.mca.yala;

/**
 * User: malpay
 * Date: 06.10.2011
 * Time: 11:37
 */
public interface YALog {

    void trace(Object msg);

    void trace(Object msg, Throwable t);

    void debug(Object msg);

    void debug(Object msg, Throwable t);

    void info(Object msg);

    void info(Object msg, Throwable t);

    void warn(Object msg);

    void warn(Object msg, Throwable t);

    void error(Object msg);

    void error(Object msg, Throwable t);

}

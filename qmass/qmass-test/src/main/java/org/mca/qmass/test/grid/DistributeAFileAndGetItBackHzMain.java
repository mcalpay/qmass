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
package org.mca.qmass.test.grid;

import com.hazelcast.core.Hazelcast;

import java.util.Map;

/**
 * User: malpay
 * Date: 11.Tem.2011
 * Time: 14:02:24
 */
public class DistributeAFileAndGetItBackHzMain {

    public static void main(String... args) throws Exception {
        Hazelcast.getMap("m");
        while (true) {
        }
    }

}

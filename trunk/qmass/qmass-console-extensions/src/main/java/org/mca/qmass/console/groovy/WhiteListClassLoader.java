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
package org.mca.qmass.console.groovy;

/**
 * User: malpay
 * Date: 27.08.2011
 * Time: 16:03
 */
public class WhiteListClassLoader extends ClassLoader {

    private String[] whiteList = {
            "org.mca.qmass.grid",
            "org.mca.qmass.console.service",
            "Script"
    };

    public WhiteListClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (String s : whiteList) {
            if (name.startsWith(s)) {
                return super.findClass(name);
            }
        }
        return Black.class;
    }

}

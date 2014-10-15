/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.chtijbug.drools.platform.runtime.builder.internals;


import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;


public class GuvnorRepositoryImplTestCase {

    @Test
    @Ignore
    public void testCreatePackage() throws IOException {
        GuvnorRepositoryImpl toTest = new GuvnorRepositoryImpl("loyalty", "latest", "tomcat", "tomcat", "255");

        toTest.createBusinessPackage("pymma");

    }

    @Test
    @Ignore
    public void testCreateModel() throws IOException {
        InputStream inputStream = Class.class.getResource("/model.jar").openStream();

        GuvnorRepositoryImpl toTest = new GuvnorRepositoryImpl("loyalty", "latest", "tomcat", "tomcat", "255");

        toTest.createBusinessModel("pymma", inputStream);
    }

    @Test
    @Ignore
    public void testCreateProcess() throws IOException {
        GuvnorRepositoryImpl toTest = new GuvnorRepositoryImpl("loyalty", "latest", "tomcat", "tomcat", "255");

        toTest.createBusinessProcess("pymma", "new-process");

    }

}

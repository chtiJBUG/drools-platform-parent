package org.chtijbug.drools.platform.runtime.builder.internals;


import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;


public class GuvnorRepositoryImplTestCase {

    @Test
    @Ignore
    public void testCreatePackage() throws IOException {
        GuvnorRepositoryImpl toTest = new GuvnorRepositoryImpl("http://localhost:8080", "/drools-guvnor", "tomcat", "tomcat");

        toTest.createBusinessPackage("pymma");

    }

    @Test
    @Ignore
    public void testCreateModel() throws IOException {
        InputStream inputStream = Class.class.getResource("/model.jar").openStream();

        GuvnorRepositoryImpl toTest = new GuvnorRepositoryImpl("http://localhost:8080", "/drools-guvnor", "tomcat", "tomcat");

        toTest.createBusinessModel("pymma", inputStream);
    }

    @Test
    @Ignore
    public void testCreateProcess() throws IOException {
        GuvnorRepositoryImpl toTest = new GuvnorRepositoryImpl("http://localhost:8080", "/drools-guvnor", "tomcat", "tomcat");

        toTest.createBusinessProcess("pymma", "new-process");

    }

}

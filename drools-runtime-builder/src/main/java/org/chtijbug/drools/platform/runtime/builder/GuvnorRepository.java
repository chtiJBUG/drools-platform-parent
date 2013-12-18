package org.chtijbug.drools.platform.runtime.builder;

import java.io.InputStream;


public interface GuvnorRepository {

    void createBusinessPackage(String businessPackage);

    void createBusinessModel(String businessPackage, InputStream businessModel);

    void createBusinessProcess(String businessPackage, String processName);
}

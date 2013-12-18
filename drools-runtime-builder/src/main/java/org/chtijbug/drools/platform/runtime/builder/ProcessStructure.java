package org.chtijbug.drools.platform.runtime.builder;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProcessStructure {
    private final String processName;
    private final String inputClassname;
    private final String outputClassname;

    public ProcessStructure(String processName, String inputClassname, String outputClassname) {
        this.processName = processName;
        this.inputClassname = inputClassname;
        this.outputClassname = outputClassname;
    }

    public String getProcessName() {
        return processName;
    }

    public String getInputClassname() {
        return inputClassname;
    }

    public String getOutputClassname() {
        return outputClassname;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("processName", processName)
                .append("inputClassname", inputClassname)
                .append("outputClassname", outputClassname)
                .toString();
    }
}

package org.chtijbug.drools.platform.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlatformRuntimeInstanceData {
    Long id;

    String startDate;
    String status;
    String rulePackage;
    String environment;
    String runtimeId;
    String rulePackageVersion;
}

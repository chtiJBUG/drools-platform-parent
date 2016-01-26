package org.chtijbug.drools.platform.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PlatformRuntimeInstanceData {
    Long id;
    Date startDate;
    String status;
    String rulePackage;
    String environment;
    String runtimeId;
    String rulePackageVersion;
}

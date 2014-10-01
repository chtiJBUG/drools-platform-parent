
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

package org.chtijbug.drools.platform.web.model;

public class PackageSnapshot {
    public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private String packageName;
    private String version;
    private Boolean isRelease = false;

    public PackageSnapshot() {
    }

    public PackageSnapshot(String packageName, String snapshot) {
        this.packageName = packageName;
        //___ Check if the snapshot is a SNAPSHOT
        if (snapshot.endsWith(SNAPSHOT_SUFFIX)) {
            this.isRelease = false;
            //___ snapshot format : x.y.z-SNAPSHOT
            this.version = snapshot.substring(0, snapshot.length() - SNAPSHOT_SUFFIX.length());
        } else {
            //___ By default it is true
            this.isRelease = true;
            //___ release format : x.y.z
            this.version = snapshot;
        }

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(Boolean isRelease) {
        this.isRelease = isRelease;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String constructVersionName() {
        if (isRelease)
            return version;
        return version.concat(SNAPSHOT_SUFFIX);
    }
}

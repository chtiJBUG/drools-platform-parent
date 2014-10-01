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
package org.chtijbug.drools.platform.rules.management;

public enum AssetStatus {
    DEV,
    INT,
    PROD,
    NONE;

    public static AssetStatus getEnum(String assetStatus) {
        for (AssetStatus status : AssetStatus.values()) {
            if (status.name().equals(assetStatus))
                return status;
        }
        return null;
    }

    public AssetStatus getNextStatus() {
        AssetStatus result = null;
        switch (this) {
            case DEV:
                result = INT;
                break;
            case INT:
                result = PROD;
                break;
        }
        return result;
    }

    public AssetStatus getPreviousStatus() {
        AssetStatus result = null;
        switch (this) {
            case INT:
                result = DEV;
                break;
            case PROD:
                result = INT;
                break;
        }
        return result;
    }

}

package org.chtijbug.drools.platform.web;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageSnapshotRequestTest {

    @Test
    public void should_get_version_name_suffixed_with_SNAPSHOT() {
        PackageSnapshotRequest request = new PackageSnapshotRequest();
        request.setVersion("1.2.3");
        request.setIsRelease(false);

        assertThat(request.constructVersionName()).isEqualTo("1.2.3-SNAPSHOT");
    }

    @Test
    public void should_get_definitive_version_name() {
        PackageSnapshotRequest request = new PackageSnapshotRequest();
        request.setVersion("1.2.3");
        request.setIsRelease(true);

        assertThat(request.constructVersionName()).isEqualTo("1.2.3");
    }
}

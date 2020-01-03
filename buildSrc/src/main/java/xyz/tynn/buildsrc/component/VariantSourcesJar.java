//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.buildsrc.component;

import com.android.build.gradle.api.LibraryVariant;
import com.android.builder.model.SourceProvider;

import org.gradle.jvm.tasks.Jar;

import javax.inject.Inject;

@Deprecated // obsolete with 3.6
abstract class VariantSourcesJar extends Jar {

    @Inject
    public VariantSourcesJar(LibraryVariant variant) {
        getArchiveClassifier().set("sources");
        for (SourceProvider sourceSets : variant.getSourceSets())
            from(sourceSets.getJavaDirectories());
    }
}

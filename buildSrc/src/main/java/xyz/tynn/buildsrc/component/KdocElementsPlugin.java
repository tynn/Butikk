//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.buildsrc.component;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.DocsType;
import org.gradle.api.attributes.Usage;
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

import static org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE;
import static org.gradle.api.attributes.Bundling.EXTERNAL;
import static org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE;
import static org.gradle.api.attributes.Category.DOCUMENTATION;
import static org.gradle.api.attributes.DocsType.DOCS_TYPE_ATTRIBUTE;
import static org.gradle.api.attributes.Usage.JAVA_RUNTIME;
import static org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE;

public final class KdocElementsPlugin implements Plugin<Project> {

    private static final String KDOC_USAGE = "kdoc";
    private static final String KDOC_CONFIGURATION = "kdocElements";
    private static final String KDOC_TASK = "kdocJar";

    @Override
    public void apply(Project project) {
        TaskProvider<Jar> kdoc = project.getTasks().register(KDOC_TASK, Jar.class);
        kdoc.configure(jar -> jar.getArchiveClassifier().set(KDOC_USAGE));
        Configuration elements = project.getConfigurations().create(KDOC_CONFIGURATION);
        configureKdocElements(elements, project, kdoc);
    }

    private static void configureKdocElements(Configuration configuration, Project project, TaskProvider<Jar> task) {
        configuration.setCanBeConsumed(true);
        configuration.setCanBeResolved(false);
        configuration.setDescription("kdoc elements for main.");
        configuration.getArtifacts().addLater(task.map(ArchivePublishArtifact::new));
        setKdocAttributes(configuration.getAttributes(), project.getObjects());
    }

    private static void setKdocAttributes(AttributeContainer attributes, ObjectFactory objects) {
        attributes.attribute(BUNDLING_ATTRIBUTE, objects.named(Bundling.class, EXTERNAL));
        attributes.attribute(CATEGORY_ATTRIBUTE, objects.named(Category.class, DOCUMENTATION));
        attributes.attribute(USAGE_ATTRIBUTE, objects.named(Usage.class, JAVA_RUNTIME));
        attributes.attribute(DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.class, KDOC_USAGE));
    }
}

//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.buildsrc.component;

import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.api.LibraryVariant;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.Usage;
import org.gradle.api.component.AdhocComponentWithVariants;
import org.gradle.api.component.SoftwareComponentFactory;
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import javax.inject.Inject;

import static org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE;
import static org.gradle.api.attributes.Bundling.EXTERNAL;
import static org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE;
import static org.gradle.api.attributes.Category.LIBRARY;
import static org.gradle.api.attributes.Usage.JAVA_API;
import static org.gradle.api.attributes.Usage.JAVA_RUNTIME;
import static org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE;

public final class AndroidAdhocPlugin implements Plugin<Project> {

    private static final String EXT_MESSAGE = "The Android Components plugin requires an Android Library plugin to be applied.";

    private final ObjectFactory objectFactory;
    private final SoftwareComponentFactory softwareComponentFactory;

    @Inject
    AndroidAdhocPlugin(ObjectFactory objectFactory, SoftwareComponentFactory softwareComponentFactory) {
        this.objectFactory = objectFactory;
        this.softwareComponentFactory = softwareComponentFactory;
    }

    @Override
    public void apply(Project project) {
        LibraryExtension extension = requireLibraryExtension(project.getExtensions());
        AdhocComponentWithVariants component = createComponent(project);
        ConfigurationContainer configurations = project.getConfigurations();
        Configuration api = createConfiguration(configurations, component, true);
        Configuration runtime = createConfiguration(configurations, component, false);
        extension.getLibraryVariants().all(variant -> addArtifacts(project, variant, runtime, api));
    }

    private AdhocComponentWithVariants createComponent(Project project) {
        AdhocComponentWithVariants component = softwareComponentFactory.adhoc("android");
        project.getComponents().add(component);
        return component;
    }

    private Configuration createConfiguration(ConfigurationContainer configurations, AdhocComponentWithVariants component, boolean isApi) {
        Configuration configuration = configurations.create("android" + (isApi ? "Api" : "Runtime") + "Elements");
        configuration.setCanBeConsumed(false);
        configuration.setCanBeResolved(false);
        configuration.setDescription("Android " + (isApi ? "API" : "runtime") + " elements");
        putAttributes(configuration.getAttributes(), isApi ? JAVA_API : JAVA_RUNTIME);
        component.addVariantsFromConfiguration(configuration, it -> it.mapToMavenScope(isApi ? "compile" : "runtime"));
        return configuration;
    }

    private void putAttributes(AttributeContainer attributes, String usage) {
        attributes.attribute(BUNDLING_ATTRIBUTE, objectFactory.named(Bundling.class, EXTERNAL));
        attributes.attribute(CATEGORY_ATTRIBUTE, objectFactory.named(Category.class, LIBRARY));
        attributes.attribute(USAGE_ATTRIBUTE, objectFactory.named(Usage.class, usage));
    }

    private static void addArtifacts(Project project, LibraryVariant variant, Configuration runtime, Configuration api) {
        if (!variant.getFlavorName().equals(""))
            throw new IllegalStateException("Android flavors are not yet supported.");
        if (!variant.getBuildType().getName().equals("release")) return;
        runtime.extendsFrom(project.getConfigurations().getByName("releaseRuntimeElements"));
        runtime.getArtifacts().addLater(variant.getPackageLibraryProvider().map(ArchivePublishArtifact::new));
        api.extendsFrom(project.getConfigurations().getByName("releaseApiElements"));
        api.getArtifacts().addLater(variant.getPackageLibraryProvider().map(ArchivePublishArtifact::new));
        api.getArtifacts().addLater(registerSourcesJar(variant, project.getTasks()).map(ArchivePublishArtifact::new));
    }

    private static TaskProvider<VariantSourcesJar> registerSourcesJar(LibraryVariant variant, TaskContainer tasks) {
        return tasks.register(variant.getName() + "SourcesJar", VariantSourcesJar.class, variant);
    }

    private static LibraryExtension requireLibraryExtension(ExtensionContainer extensions) {
        LibraryExtension extension = extensions.findByType(LibraryExtension.class);
        if (extension != null) return extension;
        throw new IllegalStateException(EXT_MESSAGE);
    }
}

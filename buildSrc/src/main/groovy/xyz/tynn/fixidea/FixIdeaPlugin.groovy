//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.fixidea

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Fix minor issues with IDEA whenever these popup.
 */
final class FixIdeaPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Closure fixIdea = FixIdeaPlugin.&withPlugin.curry(project)

        // Setup kotlin sources for Android Studio
        fixIdea('kotlin-android') {
            afterEvaluate {
                android {
                    sourceSets.all {
                        java.srcDirs += kotlin.srcDirs
                    }
                }
            }
        }

        // Add test fixture sources as test sources in IDEA
        fixIdea('java-test-fixtures') {
            apply plugin: 'idea'

            configurations {
                ideaTestFixturesClasspath {
                    extendsFrom testFixturesApi
                    extendsFrom testFixturesImplementation
                    canBeConsumed false
                    canBeResolved true
                }
            }

            idea {
                module {
                    sourceDirs -= sourceSets.testFixtures.allSource
                    testSourceDirs += sourceSets.testFixtures.allSource.srcDirs
                    scopes.COMPILE.plus.add(configurations.ideaTestFixturesClasspath)
                }
            }
        }
    }

    /**
     * Run closure with no owner and project as delegate when the plugin was added.
     */
    private static void withPlugin(Project project, String pluginId, Closure closure) {
        project.plugins.withId(pluginId) {
            //noinspection UnnecessaryQualifiedReference
            closure.resolveStrategy = Closure.DELEGATE_ONLY
            closure.delegate = project
            closure()
        }
    }
}

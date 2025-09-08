package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.dependencies.ProjectDependencyInternal;
import org.gradle.api.internal.artifacts.DefaultProjectDependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.catalog.DelegatingProjectDependency;
import org.gradle.api.internal.catalog.TypeSafeProjectDependencyFactory;
import javax.inject.Inject;

@NonNullApi
public class FeatureProjectDependency extends DelegatingProjectDependency {

    @Inject
    public FeatureProjectDependency(TypeSafeProjectDependencyFactory factory, ProjectDependencyInternal delegate) {
        super(factory, delegate);
    }

    /**
     * Creates a project dependency on the project at path ":feature:home"
     */
    public Feature_HomeProjectDependency getHome() { return new Feature_HomeProjectDependency(getFactory(), create(":feature:home")); }

    /**
     * Creates a project dependency on the project at path ":feature:login"
     */
    public Feature_LoginProjectDependency getLogin() { return new Feature_LoginProjectDependency(getFactory(), create(":feature:login")); }

    /**
     * Creates a project dependency on the project at path ":feature:register"
     */
    public Feature_RegisterProjectDependency getRegister() { return new Feature_RegisterProjectDependency(getFactory(), create(":feature:register")); }

}

package deploydb.dao

import groovy.transform.InheritConstructors
import com.google.common.base.Optional
import io.dropwizard.hibernate.AbstractDAO

import deploydb.models.Artifact

/**
 * Artifact Data access object
 */
@InheritConstructors
class ArtifactDAO extends AbstractDAO<Artifact> {

    /**
     * Locate a Artifact based on the artifactId passed in
     */
    Artifact findById(Long artifactId) {
        return get(artifactId)
    }

    long create(Artifact artifact) {
        return persist(artifact).id
    }
}
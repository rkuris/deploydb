package deploydb.resources

import com.codahale.metrics.annotation.Timed
import io.dropwizard.jersey.caching.CacheControl
import io.dropwizard.jersey.params.*
import io.dropwizard.hibernate.UnitOfWork
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

import deploydb.registry.ModelRegistry
import deploydb.models.Service

/**
 * ServiceResource class registered with JettyClient for servicing REST request
 */
@Path("/api/services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(['application/json', 'application/vnd.deploydb.v1+json'])
public class ServiceResource {
    private final ModelRegistry<Service> serviceRegistry
    private final Logger logger = LoggerFactory.getLogger(ServiceResource.class)

    ServiceResource(ModelRegistry<Service> serviceRegistry) {
        this.serviceRegistry = serviceRegistry
    }

    /**
     * Returns all Service objects
     */
    @GET
    @UnitOfWork
    @Timed(name = "get-requests")
    List<Service> getAll() {
        List<Service> serviceTable = this.serviceRegistry.getAll()

        if (serviceTable.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        }
        return serviceTable
    }

    /**
     * Returns a Service object
     */
    @GET
    @Path("{name}")
    @UnitOfWork
    @Timed(name = "get-requests")
    Service byName(@PathParam("name") String serviceIdent) {
        Service service = this.serviceRegistry.get(serviceIdent)

        if (service == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        }
        return service
    }
}


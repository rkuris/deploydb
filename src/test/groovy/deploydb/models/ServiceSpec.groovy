package deploydb.models

import spock.lang.*

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.collect.Lists
import com.google.common.io.Resources
import io.dropwizard.jackson.Jackson
import org.assertj.core.data.MapEntry
import io.dropwizard.configuration.ConfigurationFactory
import io.dropwizard.configuration.ConfigurationParsingException
import io.dropwizard.configuration.ConfigurationValidationException

import javax.validation.Validation
import javax.validation.Validator
import java.io.File
import java.util.*
import java.io.IOException
 

class ServiceSpec extends Specification {
    def "ensure Service object can be instantiated"() {
        when:
        def service = new Service()

        then:
        service instanceof Service
    }
}

class ServiceWithArgsSpec extends Specification {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator()
    private final ConfigurationFactory<Service> factory =
            new ConfigurationFactory<>(Service.class, validator, Jackson.newObjectMapper(), "dw")

    def "Loading of valid service config file suceeds"() {
        given:
        ClassLoader classLoader = getClass().getClassLoader()
        File validFile = new File(classLoader.getResource("services/test-valid.yml").toURI())
        Service service = factory.build(validFile)

        expect:
        service.name == "Fun as a Service"
        service.artifacts[0] == "com.github.lookout:foas"
        service.artifacts[1] == "com.github.lookout.puppet:puppet-foas"
        service.artifacts[2] == "com.github.lookout:puppet-mysql"
        service.pipelines[0] == "devtoprod"
        service.promotions[0] == "status-check"
        service.promotions[1] == "jenkins-smoke"
    }

    def "Loading an empty service config file throws a null pointer exception"() {
        given:
        ClassLoader classLoader = getClass().getClassLoader()
        File emptyFile = new File(classLoader.getResource("services/test-empty.yml").toURI())
        
        when:
        Service service = factory.build(emptyFile)

        then:
        thrown(NullPointerException)
    }

    def "Loading a malformed service config file throws throws a parsing exception"() {
        given:
        ClassLoader classLoader = getClass().getClassLoader()
        File malformedFile = new File(classLoader.getResource("services/test-malformed.yml").toURI())
        
        when:
        Service service = factory.build(malformedFile)

        then:
        thrown(ConfigurationParsingException)
    }

    def "Loading a invalid service config file throws throws a validation exception"() {
        given:
        ClassLoader classLoader = getClass().getClassLoader()
        File invalidFile = new File(classLoader.getResource("services/test-invalid.yml").toURI())
        
        when:
        Service service = factory.build(invalidFile)

        then:
        thrown(ConfigurationValidationException)
    }
}

package deploydb.models.Webhook


import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.Size

/**
 * Class for webhook's promotion configuration.
 */
class Promotion {

    /**
     * List of URIs for promotion completed state
     */
    @JsonProperty
    List<String> completed

    /**
     * Empty constructor. Used by Jackson for object deserialization
     */
    Promotion() {}

    /*
     * Constructor with parameter used by deployDb
     */
    Promotion(List<String> completed){
        this.completed = completed
    }

}

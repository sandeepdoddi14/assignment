package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;


public class JsonSchemaValidator {

    /*
    Validates The Response With The Schema JSON provided
    @Return : Boolean
     */
    public boolean validate(Response response, String schemaFileName) throws Exception {
        // Convert response to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.asString());

        // Convert JsonNode to JSONObject
        JSONObject jsonObject = new JSONObject(mapper.writeValueAsString(jsonNode));

        // Load schema from classpath
        try (InputStream schemaStream = JsonSchemaValidator.class.getClassLoader()
                .getResourceAsStream("schemas/" + schemaFileName)) {

            if (schemaStream == null) {
                throw new RuntimeException("Schema file not found: " + schemaFileName);
            }

            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(rawSchema);

            // Validate JSON against schema
            schema.validate(jsonObject);
            return true;

        }
    }
}

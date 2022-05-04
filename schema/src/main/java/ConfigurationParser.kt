import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.malinskiy.vdm.Configuration
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import java.nio.file.Path

/*
 * SPDX-License-Identifier: Apache-2.0
 */

class ConfigurationParser(private val schemaPath: Path) {
    private val mapper = ObjectMapper(YAMLFactory()).registerModule(
        KotlinModule.Builder().build()
    )
    private val factory =
        JsonSchemaFactory
            .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
            .objectMapper(mapper)
            .build()
    private val schema = factory.getSchema(schemaPath.toUri())

    fun parse(data: String): Configuration {
        val readTree = mapper.readTree(data)
        val validate = schema.validate(readTree)
        if (validate.isNotEmpty()) {
            throw IllegalStateException("Validation error $validate")
        }
        return mapper.readValue(data, Configuration::class.java)
    }
}

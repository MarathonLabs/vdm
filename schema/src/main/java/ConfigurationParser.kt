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
    fun parse(data: String): Configuration {
        val mapper = ObjectMapper(YAMLFactory()).registerModule(
            KotlinModule.Builder().build()
        )
        val factory =
            JsonSchemaFactory
                .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
                .objectMapper(mapper)
                .build()
        val schema = factory.getSchema(schemaPath.toUri())
        val readTree = mapper.readTree(data)
        val validate = schema.validate(readTree)
        if (validate.isNotEmpty()) {
            throw IllegalStateException("Validation error $validate")
        }
        return mapper.readValue(data, Configuration::class.java)
    }
}

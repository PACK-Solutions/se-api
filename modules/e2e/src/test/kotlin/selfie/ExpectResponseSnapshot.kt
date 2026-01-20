package selfie

import com.diffplug.selfie.Camera
import com.diffplug.selfie.Snapshot
import com.diffplug.selfie.StringSelfie
import com.diffplug.selfie.coroutines.expectSelfie
import io.kotest.common.runBlocking
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

fun canonicalizeAndClean(
    element: JsonElement,
): JsonElement =
    when (element) {
        is JsonObject -> JsonObject(
            element.entries
                .sortedBy { it.key }
                .associate { (k, v) ->
                    k to canonicalizeAndClean(v)
                },
        )

        is JsonArray -> JsonArray(
            element.map { canonicalizeAndClean(it) },
        )

        else -> element
    }

private val KTOR_RESPONSE_CAMERA: Camera<HttpResponse> = Camera { response: HttpResponse ->
    runBlocking {
        Snapshot.of(
            Json { prettyPrint = true }.encodeToString(canonicalizeAndClean(response.body<JsonElement>())),
        )
            .plusFacet("StatusCode", response.status.value.toString())
            .plusFacet("ContentType", response.contentType()?.toString() ?: "null")
    }
}

suspend fun expectResponseSnapshot(response: HttpResponse): StringSelfie {
    return expectSelfie(response, KTOR_RESPONSE_CAMERA)
}

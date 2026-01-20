import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

infix fun HttpResponse.shouldHaveStatus(status: HttpStatusCode): HttpResponse {
    this.status shouldBe status
    return this
}

inline infix fun <reified T> HttpResponse.shouldReturn(body: T): HttpResponse {
    runBlocking { this.body<T>() shouldBe body }
    return this
}

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

fun HttpRequestBuilder.tenantId(tenantId: String) = header("tenantId", tenantId)
fun HttpRequestBuilder.noTenandId() = headers.remove("tenantId")
fun HttpRequestBuilder.login(login: String) = header("login", login)

import com.ps.kommand.Context

class ContextFixture {

    fun build() = Context()
}

fun aContext() = ContextFixture()

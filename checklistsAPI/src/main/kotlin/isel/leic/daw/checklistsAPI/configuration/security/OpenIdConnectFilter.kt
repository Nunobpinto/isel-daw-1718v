package isel.leic.daw.checklistsAPI.configuration.security

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.leic.daw.checklistsAPI.exceptions.BadRequestException
import isel.leic.daw.checklistsAPI.exceptions.UnauthenticatedException
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class OpenIdConnectFilter : Filter {
    val CLIENT_ID = "daw"
    val CLIENT_SECRET = "secret"
    val INTROSPECT_ENDPOINT = "http://35.189.66.182/openid-connect-server-webapp/introspect"

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userInfo: UserInfo

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = (request as HttpServletRequest)
        if( !checkIfProtectedResource(httpRequest) || httpRequest.method == "OPTIONS" ) {
            return chain.doFilter(request, response)
        }

        val bearerToken = getBearerToken(httpRequest) ?: throw BadRequestException()
        val introspectionResponse = getIntrospectionResponse(bearerToken)

        if( introspectionResponse.active ) {
            userService.saveUser(User(introspectionResponse.sub!!))
            userInfo.sub = introspectionResponse.sub
            userInfo.user_id = introspectionResponse.user_id
            return chain.doFilter(request, response)
        }
        throw UnauthenticatedException("Unauthorized")
    }

    override fun destroy() {
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    private fun getIntrospectionResponse(bearerToken: String): IntrospectionResponse {
        val encoded = String(Base64.getEncoder().encode(("$CLIENT_ID:$CLIENT_SECRET").toByteArray()))
        val authHeader = "Basic $encoded"
        val contentType = "application/x-www-form-urlencoded"
        val token = bearerToken.split(" ")[1]
        val body = "token=$token"

        val url = URL(INTROSPECT_ENDPOINT)
        val con = url.openConnection() as HttpURLConnection
        con.doOutput = true
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", contentType)
        con.setRequestProperty("Authorization", authHeader)

        con.outputStream.use {
            val osw = OutputStreamWriter(it, "UTF-8")
            osw.write(body)
            osw.flush()
            osw.close()
        }

        con.connect()

        val response = StringBuffer()
        BufferedReader(InputStreamReader(con.inputStream)).use {
            var inputLine = it.readLine()
            while( inputLine != null ) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
        }
        val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(response.toString(), IntrospectionResponse::class.java)
    }

    private fun getBearerToken(request: HttpServletRequest): String? {
        val auth = request.getHeader("Authorization")
        return if( auth != null && auth.startsWith("Bearer") ) auth else null
    }

    private fun checkIfProtectedResource(request: HttpServletRequest) = request.requestURL.toString().contains("/api")

}
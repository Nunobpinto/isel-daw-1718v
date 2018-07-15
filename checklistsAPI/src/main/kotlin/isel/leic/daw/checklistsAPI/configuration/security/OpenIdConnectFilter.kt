package isel.leic.daw.checklistsAPI.configuration.security

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.leic.daw.checklistsAPI.outputModel.error.ErrorOutputModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OpenIdConnectFilter : Filter {
    val CLIENT_ID = "daw"
    val CLIENT_SECRET = "secret"
    val INTROSPECT_ENDPOINT = "http://35.197.215.43/openid-connect-server-webapp/introspect"

    private val log = LoggerFactory.getLogger(OpenIdConnectFilter::class.java)

    @Autowired
    lateinit var userInfo: UserInfo

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        log.info("Received ${request.method} request for ${request.requestURI}")

        if(request.requestURL.toString().contains("/v2/api-docs") || request.requestURL.toString().contains("/swagger-ui") || ! checkIfProtectedResource(httpRequest) || httpRequest.method == "OPTIONS" ){
            return chain.doFilter(request, response)
        }

        val bearerToken = getBearerToken(httpRequest) ?: return throwBadRequestException(httpResponse)
        val introspectionResponse = getIntrospectionResponse(bearerToken)

        if(introspectionResponse.active) {
            log.info("User with user_id = ${introspectionResponse.user_id} and sub = ${introspectionResponse.sub} successfully authenticated")
            userInfo.sub = introspectionResponse.sub
            userInfo.user_id = introspectionResponse.user_id
            return chain.doFilter(request, response)
        }
        log.info("User not found, responding with 401 Unauthorized")
        throwUnauthorizedException(httpResponse)
    }

    override fun destroy() {
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    private fun throwUnauthorizedException(httpResponse: HttpServletResponse) {
        httpResponse.status = HttpStatus.UNAUTHORIZED.value()
        httpResponse.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8.toString()
        val entity = ResponseEntity(
                ErrorOutputModel(
                        title = "Not Authenticated",
                        detail = "Authentication Required",
                        status = 401
                ),
                HttpStatus.UNAUTHORIZED)
        httpResponse.writer.write(ObjectMapper().writeValueAsString(entity))
    }

    private fun throwBadRequestException(response: HttpServletResponse) {
        response.status = HttpStatus.BAD_REQUEST.value()
        response.contentType = MediaType.APPLICATION_PROBLEM_JSON_UTF8.toString()
        val entity = ResponseEntity(
                ErrorOutputModel(
                        title = "Invalid Syntax",
                        detail = "Server could not understand the request",
                        status = 400
                ),
                HttpStatus.BAD_REQUEST)
        response.writer.write(ObjectMapper().writeValueAsString(entity))
    }

    private fun getIntrospectionResponse(bearerToken: String): IntrospectionResponse {
        val authHeader = "Basic ${String(Base64.getEncoder().encode(("$CLIENT_ID:$CLIENT_SECRET").toByteArray()))}"
        val contentType = "application/x-www-form-urlencoded"
        val body = "token=${bearerToken.split(" ")[1]}"

        val url = URL(INTROSPECT_ENDPOINT)
        val con = url.openConnection() as HttpURLConnection
        con.doOutput = true
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", contentType)
        con.setRequestProperty("Authorization", authHeader)

        log.info("Making introspect request with ${con.requestMethod} method")
        log.info("with headers: ")
        log.info("  Content-Type: $contentType")
        log.info("  Authorization: $authHeader")
        log.info("with body: ")
        log.info("  $body")
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
            while(inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
        }
        log.info("Recieved introspect request response: ")
        log.info("  $response")
        val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(response.toString(), IntrospectionResponse::class.java)
    }

    private fun getBearerToken(request: HttpServletRequest): String? {
        val auth = request.getHeader("Authorization")
        return if(auth != null && auth.startsWith("Bearer")) auth else null
    }

    private fun checkIfProtectedResource(request: HttpServletRequest) = request.requestURL.toString().contains("/api")

}
package isel.leic.daw.checklistsAPI.configuration.security

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.leic.daw.checklistsAPI.configuration.DeployedData
import isel.leic.daw.checklistsAPI.exceptions.UnauthenticatedException
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

    override fun destroy() {
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val method = (request as HttpServletRequest).method
        if( !checkIfProtectedResource(request) ) {
            return chain.doFilter(request, response)
        }
        val bearerToken = getBearerToken(request)
        val introspectionResponse = getIntrospectionResponse(bearerToken)
        if( bearerToken != null && introspectionResponse.active ) {
            return chain.doFilter(request, response)
        }
        throw UnauthenticatedException("Unauthorized")
    }

    private fun getIntrospectionResponse(bearerToken: String?): IntrospectionResponse {
        val authHeader = "Basic ${Base64.getEncoder().encode((DeployedData.CLIENT_ID + ":" + DeployedData.CLIENT_SECRET).toByteArray())}"
        val contentType = "application/x-www-form-urlencoded"
        val body = "token=$bearerToken"

        val url = URL(DeployedData.INTROSPECT_ENDPOINT)
        val con = url.openConnection() as HttpURLConnection
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

    private fun getBearerToken(request: ServletRequest): String? {
        val req: HttpServletRequest = (request as HttpServletRequest)
        val auth = req.getHeader("Authorization")
        val ac = req.getHeader("Access-Control-Allow-Origin")
        val user = req.getHeader("User-Agent")
        return if( auth != null && auth.startsWith("Bearer") ) auth else null
    }

    private fun checkIfProtectedResource(request: ServletRequest) = (request as HttpServletRequest).requestURL.toString().contains("/api")

}
package isel.leic.daw.checklistsAPI.configuration.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomBasicAuthenticationEntryPoint : BasicAuthenticationEntryPoint() {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.addHeader("WWW-Authenticate", "Basic realm=\"${realmName}\"")

        val writer = response.writer
        writer.println("HTTP Status 401 : ${authException.message}")
    }

    override fun afterPropertiesSet() {
        realmName = SecurityConfig.REALM
        super.afterPropertiesSet()
    }

}

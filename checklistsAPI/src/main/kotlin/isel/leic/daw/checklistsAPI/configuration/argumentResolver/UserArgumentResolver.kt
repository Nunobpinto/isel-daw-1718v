package isel.leic.daw.checklistsAPI.configuration.argumentResolver


import isel.leic.daw.checklistsAPI.model.User
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == User::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?): Any? {

        val username: String? = webRequest.getParameter("username")
        val familyName: String? = webRequest.getParameter("family_name")
        val givenName: String? = webRequest.getParameter("given_name")
        val email: String? = webRequest.getParameter("email")
        val password: String? = webRequest.getParameter("password")
        return User(
                username = username!!,
                familyName = familyName,
                givenName = givenName,
                email = email,
                password = password!!
        )
    }
}
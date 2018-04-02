package isel.leic.daw.checklistsAPI.configuration.argumentResolver

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class ChecklistTemplateArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == ChecklistTemplate::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?): Any? {

        val name: String? = webRequest.getParameter("checklistTemplate_name")
        val description: String? = webRequest.getParameter("checklistTemplate_description")
        val httpServletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)
        val username =httpServletRequest!!.userPrincipal.name!!
        return ChecklistTemplate(
                checklistTemplateName = name,
                checklisttemplateDescription = description,
                user= User(username = username)
        )
    }
}
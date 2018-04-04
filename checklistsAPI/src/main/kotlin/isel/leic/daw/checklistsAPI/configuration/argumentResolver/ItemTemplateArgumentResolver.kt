package isel.leic.daw.checklistsAPI.configuration.argumentResolver

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

class ItemTemplateArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == ItemTemplate::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?): Any? {

        val name: String? = webRequest.getParameter("itemTemplate_name")
        val desc: String? = webRequest.getParameter("itemTemplate_desc")

        val id = getPathVariables(webRequest)["checklistTemplate"]?.toLong()
        return ItemTemplate(
                itemTemplateName = name,
                itemTemplateDescription = desc,
                checklistTemplate = ChecklistTemplate(checklistTemplateId = id!!)
        )
    }

    private fun getPathVariables(webRequest: NativeWebRequest): Map<String, String> {

        val httpServletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)
        return httpServletRequest?.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>
    }
}
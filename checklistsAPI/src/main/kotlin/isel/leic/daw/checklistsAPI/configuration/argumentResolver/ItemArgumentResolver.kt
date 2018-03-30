package isel.leic.daw.checklistsAPI.configuration.argumentResolver

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest


class ItemArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == Item::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?): Any? {

        val name: String? = webRequest.getParameter("item_name")
        val desc: String? = webRequest.getParameter("item_desc")
        val id = getPathVariables(webRequest)["checklistId"]?.toLong()

        return Item(
                itemName = name,
                itemDescription = desc,
                checklist = Checklist(checklistId = id!!)
        )
    }

    private fun getPathVariables(webRequest: NativeWebRequest): Map<String, String> {

        val httpServletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)
        return httpServletRequest?.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>
    }

}

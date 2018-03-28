package isel.leic.daw.checklistsAPI

import isel.leic.daw.checklistsAPI.model.Checklist
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

class ChecklistArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == Checklist::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?): Any? {

        val name: String? = webRequest.getParameter("checklist_name")
        val completionDate: LocalDate? = LocalDate.parse(webRequest.getParameter("completion_date"))
        return Checklist(
                checklistName = name,
                completionDate = completionDate
        )
    }

}

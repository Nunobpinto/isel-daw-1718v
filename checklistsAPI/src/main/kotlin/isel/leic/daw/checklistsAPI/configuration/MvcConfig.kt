package isel.leic.daw.checklistsAPI.configuration

import isel.leic.daw.checklistsAPI.configuration.argumentResolver.ChecklistArgumentResolver
import isel.leic.daw.checklistsAPI.configuration.argumentResolver.ChecklistTemplateArgumentResolver
import isel.leic.daw.checklistsAPI.configuration.argumentResolver.ItemArgumentResolver
import isel.leic.daw.checklistsAPI.configuration.argumentResolver.ItemTemplateArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class MvcConfig : WebMvcConfigurationSupport() {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(ChecklistArgumentResolver())
        argumentResolvers.add(ItemArgumentResolver())
        argumentResolvers.add(ChecklistTemplateArgumentResolver())
        argumentResolvers.add(ItemTemplateArgumentResolver())
    }
}
package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import java.util.*

interface ChecklistTemplateService {

    fun getTemplatesByUser(user: User): List<ChecklistTemplate>

    fun getTemplatesByUserPaginated(user: User, offset: Int, limit:Int): List<ChecklistTemplate>

    fun getTemplateByIdAndUser(checklistTemplateId: Long, user: User): Optional<ChecklistTemplate>

    fun saveTemplate(template: ChecklistTemplate): ChecklistTemplate

    fun saveAllTemplates(templates: Iterable<ChecklistTemplate>): MutableIterable<ChecklistTemplate>

    fun deleteTemplateById(checklistTemplateId: Long)

    fun deleteAllTemplatesByUser(user: User): Long

}
package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ItemTemplateRepository : CrudRepository<ItemTemplate, Long> {

    @Transactional
    fun deleteByChecklistTemplate(checklistTemplate: ChecklistTemplate): Long

    @Transactional
    fun deleteByChecklistTemplateAndItemTemplateId(checklistTemplate: ChecklistTemplate, itemTemplateId: Long): Long

    fun findByChecklistTemplate(checklistTemplate: ChecklistTemplate): List<ItemTemplate>

    fun findByChecklistTemplateAndItemTemplateId(checklistTemplate: ChecklistTemplate, itemTemplateId: Long): ItemTemplate
}
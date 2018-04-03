package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ItemTemplateRepository : CrudRepository<ItemTemplate, Long> {

    fun findByChecklistTemplateId(checklistTemplate: ChecklistTemplate) : List<ItemTemplate>

    fun findByChecklistTemplateIdAndItemTemplateId(checklistTemplate: ChecklistTemplate,itemTemplateId: Long)
            : ItemTemplate

    @Transactional
    fun deleteItemTemplateByChecklistTemplateId(checklistTemplateId : Long) : List<ItemRepository>
}
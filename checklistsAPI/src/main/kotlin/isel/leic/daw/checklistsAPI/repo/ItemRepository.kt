package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ItemRepository : CrudRepository<Item,Long> {

    @Transactional
    fun deleteByChecklist(checklist: Checklist): Long

    @Transactional
    fun deleteByChecklistAndItemId(checklist: Checklist, itemId: Long): Long

    fun findByChecklist(checklist: Checklist) : List<Item>

    fun findByChecklistAndItemId(checklist: Checklist, itemId: Long): Item
}
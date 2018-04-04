package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChecklistRepository : CrudRepository<Checklist,Long> {

    fun findByTemplate(template: ChecklistTemplate): List<Checklist>
}
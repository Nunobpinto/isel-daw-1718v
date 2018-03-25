package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChecklistTemplateRepository: CrudRepository<ChecklistTemplate, Long> {
}
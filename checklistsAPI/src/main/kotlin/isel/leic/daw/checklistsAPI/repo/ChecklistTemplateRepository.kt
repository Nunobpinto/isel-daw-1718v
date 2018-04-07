package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface ChecklistTemplateRepository: CrudRepository<ChecklistTemplate, Long> {

    fun findByUser(user: User): List<ChecklistTemplate>

    fun findByChecklistTemplateIdAndUser(checklistTemplateId: Long, user: User): Optional<ChecklistTemplate>

    @Transactional
    fun deleteByUser(user: User): Long
}
package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface ChecklistRepository : CrudRepository<Checklist,Long> {

    fun findByUser(user: User): List<Checklist>

    fun findByTemplate(template: ChecklistTemplate): List<Checklist>

    fun findByChecklistIdAndUser(checklistId: Long, user: User): Optional<Checklist>

    @Transactional
    fun deleteByUser(user: User): Long

    @Transactional
    fun deleteByChecklistIdAndUser(checklistId: Long, user: User): Long
}
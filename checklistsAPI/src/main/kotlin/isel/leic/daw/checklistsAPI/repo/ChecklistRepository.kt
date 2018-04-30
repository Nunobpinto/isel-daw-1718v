package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface ChecklistRepository : JpaRepository<Checklist,Long> {

    fun findByUser(user: User): List<Checklist>

    fun findByUser(user: User, pageable: Pageable): List<Checklist>

    fun findByTemplate(template: ChecklistTemplate): List<Checklist>

    fun findByTemplate(template: ChecklistTemplate, pageable: Pageable): List<Checklist>

    fun findByChecklistIdAndUser(checklistId: Long, user: User): Optional<Checklist>

    @Transactional
    fun deleteByUser(user: User): Long

    @Transactional
    fun deleteByChecklistIdAndUser(checklistId: Long, user: User): Long
}
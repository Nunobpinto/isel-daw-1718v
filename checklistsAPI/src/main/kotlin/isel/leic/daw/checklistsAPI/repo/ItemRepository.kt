package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
interface ItemRepository : JpaRepository<Item,Long> {

    @Transactional
    fun deleteByChecklist(checklist: Checklist): Long

    @Transactional
    fun deleteByChecklistAndItemId(checklist: Checklist, itemId: Long): Long

    fun findByChecklist(checklist: Checklist) : List<Item>

    fun findByChecklist(checklist: Checklist, pageable: Pageable) : List<Item>

    fun findByChecklistAndItemId(checklist: Checklist, itemId: Long): Item
}
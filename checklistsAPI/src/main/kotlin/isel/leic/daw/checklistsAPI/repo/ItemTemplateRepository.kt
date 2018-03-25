package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.ItemTemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemTemplateRepository : CrudRepository<ItemTemplate, Long> {
}
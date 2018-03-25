package isel.leic.daw.checklistsAPI.repo

import isel.leic.daw.checklistsAPI.model.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : CrudRepository<Item,Long> {
}
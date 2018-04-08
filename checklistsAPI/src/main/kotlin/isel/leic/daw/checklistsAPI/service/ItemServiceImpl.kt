package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemServiceImpl : ItemService {

    @Autowired
    lateinit var itemRepository: ItemRepository

    override fun getItemByChecklistAndItemId(checklist: Checklist, itemId: Long) =
            itemRepository.findByChecklistAndItemId(checklist, itemId)

    override fun getItemsByChecklist(checklist: Checklist) =
            itemRepository.findByChecklist(checklist)

    override fun saveItem(item: Item) =
            itemRepository.save(item)

    override fun deleteAllItemsByChecklist(checklist: Checklist) =
            itemRepository.deleteByChecklist(checklist)

    override fun deleteItemByIdAndChecklist(checklist: Checklist, itemId: Long) =
            itemRepository.deleteByChecklistAndItemId(checklist, itemId)

    override fun saveAllItems(items: List<Item>) =
            itemRepository.saveAll(items)

}
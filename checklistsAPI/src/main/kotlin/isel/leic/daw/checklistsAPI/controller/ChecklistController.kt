package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checklists", produces = [Siren4J.JSON_MEDIATYPE])
class ChecklistController {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    @GetMapping
    fun getAllChecklists() = checklistRepository.findAll()

    @GetMapping("/{checklistId}")
    fun getChecklist(@PathVariable checklistId: Long) = checklistRepository.findById(checklistId)

    @GetMapping("/{checklistId}/items")
    fun getItemsOfChecklist(@PathVariable checklistId: Long) : List<Item>{
        val checklist = checklistRepository.findById(checklistId).get()
        return itemRepository.findByChecklist(checklist)
    }

    @GetMapping("/{checklistId}/items/{itemId}")
    fun getItemOfChecklist(
            @PathVariable checklistId: Long,
            @PathVariable itemId: Long
    ):ResponseEntity<Entity> {
        val checklist = checklistRepository.findById(checklistId).get()
        val item = itemRepository.findByChecklistAndItemId(checklist,itemId)
        val output = ItemOutputModel(
                identifier = item.itemId.toInt(),
                name = item.itemName!!,
                description = item.itemDescription!!,
                state = item.itemState.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @PostMapping
    fun addChecklist(checklist: Checklist) = checklistRepository.save(checklist)

    @PostMapping("/{checklistId}/items")
    fun addItemToList(
            @PathVariable checklistId: Int,
            item: Item
    ) = itemRepository.save(item)

    @PutMapping
    fun updateChecklists(checklists: List<Checklist>) = checklistRepository.saveAll(checklists)

    @PutMapping("/{checklistId}")
    fun updateSpecificChecklist(
            @PathVariable checklistId: Long,
            checklist: Checklist
    ) = checklistRepository.save(checklist)

    @PutMapping("/{checklistId}/items")
    fun updateItems(
            @PathVariable checklistId: Long,
            items: List<Item>
    ) = itemRepository.saveAll(items)

    @PutMapping("/{checklistId}/items/{itemId}")
    fun updateItem(
            @PathVariable checklistId: Long,
            @PathVariable itemId: Long,
            item: Item
    ) = itemRepository.save(item)



    @DeleteMapping
    fun deleteAllChecklists() = checklistRepository.deleteAll()

    @DeleteMapping("/{checklistId}")
    fun deleteSpecificChecklist(@PathVariable checklistId: Long) = checklistRepository.deleteById(checklistId)

    @DeleteMapping("{checklistId}/items")
    fun deleteItem(@PathVariable checklistId: Long) = itemRepository.deleteByChecklist(Checklist(checklistId = checklistId))

    @DeleteMapping("{checklistId}/items/{itemId}")
    fun deleteSpecificItem(@PathVariable checklistId: Long, @PathVariable itemId: Long) = itemRepository.deleteByChecklistAndItemId(Checklist(checklistId = checklistId), itemId)

    //TODO: PATCHs

}
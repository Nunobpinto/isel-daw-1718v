package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemInputModel
import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.model.State
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checklists", produces = [Siren4J.JSON_MEDIATYPE])
class ChecklistController {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return User(username = auth.name)
    }

    @GetMapping
    fun getAllChecklists() = checklistRepository.findAll()

    @GetMapping("/{checklistId}")
    fun getChecklist(@PathVariable checklistId: Long) = checklistRepository.findById(checklistId).get()

    @GetMapping("/{checklistId}/items")
    fun getItemsOfChecklist(@PathVariable checklistId: Long): List<Item> {
        val checklist = checklistRepository.findById(checklistId).get()
        return itemRepository.findByChecklist(checklist)
    }

    @GetMapping("/{checklistId}/items/{itemId}")
    fun getItemOfChecklist(
            @PathVariable checklistId: Long,
            @PathVariable itemId: Long
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findById(checklistId).get()
        val item = itemRepository.findByChecklistAndItemId(checklist, itemId)
        val output = ItemOutputModel(
                identifier = item.itemId.toInt(),
                name = item.itemName!!,
                description = item.itemDescription!!,
                state = item.itemState.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @PostMapping
    fun addChecklist(@RequestBody input: ChecklistInputModel): Checklist {
        val checklist = Checklist(
                checklistName = input.checklistName,
                checklistDescription = input.checklistDescription,
                checklistId = input.checklistId,
                completionDate = input.completionDate,
                user = getUser()
        )
        return checklistRepository.save(checklist)
    }

    @PostMapping("/{checklistId}/items")
    fun addItemToList(
            @PathVariable checklistId: Long,
            @RequestBody input: ItemInputModel
    ): Item {
        val checklist = checklistRepository.findById(checklistId).get()
        val item = Item(
                itemName = input.itemName,
                itemDescription = input.itemDescription,
                itemState = State.valueOf(input.itemState),
                checklist = checklist
        )
        return itemRepository.save(item)
    }

    @PutMapping
    fun updateChecklists(@RequestBody input: ChecklistCollectionInputModel): List<Checklist> {
        val checklists = input
                .checklists
                .map {
                    Checklist(
                            checklistName = it.checklistName,
                            checklistDescription = it.checklistDescription,
                            checklistId = it.checklistId,
                            user = getUser(),
                            items = itemRepository.findByChecklist(
                                    checklistRepository.findById(it.checklistId).get()
                            ).toMutableSet()
                    )
                }
        return checklistRepository.saveAll(checklists.asIterable()).toList()
    }

    @PutMapping("/{checklistId}")
    fun updateSpecificChecklist(
            @PathVariable checklistId: Long,
            @RequestBody input: ChecklistInputModel
    ): Checklist {
        val checklist = Checklist(
                checklistName = input.checklistName,
                checklistId = checklistId,
                checklistDescription = input.checklistDescription,
                user = getUser(),
                items = itemRepository.findByChecklist(
                        checklistRepository.findById(checklistId).get()
                ).toMutableSet()
        )
        return checklistRepository.save(checklist)
    }

    @PutMapping("/{checklistId}/items")
    fun updateItems(
            @PathVariable checklistId: Long,
            @RequestBody input: ItemCollectionInputModel
    ): List<Item> {
        val checklist = checklistRepository.findById(checklistId).get()
        val items = input
                .items
                .map {
                    Item(
                            itemName = it.itemName,
                            itemDescription = it.itemDescription,
                            itemState = State.valueOf(it.itemState),
                            checklist = checklist,
                            itemId = it.itemId
                    )
                }
        return itemRepository.saveAll(items.asIterable()).toList()
    }

    @PutMapping("/{checklistId}/items/{itemId}")
    fun updateItem(
            @PathVariable checklistId: Long,
            @PathVariable itemId: Long,
            @RequestBody input: ItemInputModel
    ): Item {
        val checklist = checklistRepository.findById(checklistId).get()
        val item = Item(
                itemName = input.itemName,
                itemDescription = input.itemDescription,
                itemState = State.valueOf(input.itemState),
                checklist = checklist,
                itemId = itemId
        )
        return itemRepository.save(item)
    }


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
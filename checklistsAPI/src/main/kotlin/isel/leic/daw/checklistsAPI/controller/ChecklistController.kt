package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.ApiOperation
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemInputModel
import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.model.State
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistOutputModel
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/checklists", produces = [Siren4J.JSON_MEDIATYPE])
class ChecklistController {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return User(username = auth.name)
    }

    @ApiOperation(value = "Returns all the Checklists")
    @GetMapping
    fun getAllChecklists() = checklistRepository.findAll()

    @ApiOperation(value = "Returns the details of a specific Checklist")
    @GetMapping("/{checklistId}")
    fun getChecklist(@PathVariable checklistId: Long, principal: Principal): ResponseEntity<Entity> {
        val checklist = checklistRepository.findById(checklistId).get()
        val output = ChecklistOutputModel(
                checklistId = checklist.checklistId,
                name = checklist.checklistName,
                description = checklist.checklistDescription,
                completionDate = checklist.completionDate.toString(),
                username = principal.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns all Items of a specific Checklist")
    @GetMapping("/{checklistId}/items")
    fun getItemsOfChecklist(@PathVariable checklistId: Long): ResponseEntity<Entity>{
        val checklist = checklistRepository.findById(checklistId).get()
        return itemRepository.findByChecklist(checklist)
    }

    @ApiOperation(value = "Returns the details of a specific Item")
    @GetMapping("/{checklistId}/items/{itemId}")
    fun getItemOfChecklist(@PathVariable checklistId: Long, @PathVariable itemId: Long): ResponseEntity<Entity> {
        val checklist = checklistRepository.findById(checklistId).get()
        val item = itemRepository.findByChecklistAndItemId(checklist, itemId)
        val output = ItemOutputModel(
                checklistId = checklistId,
                itemId = item.itemId,
                name = item.itemName,
                description = item.itemDescription,
                state = item.itemState.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Checklist")
    @PostMapping
    fun addChecklist(@RequestBody input: ChecklistInputModel, principal: Principal): ResponseEntity<Entity> {
        var checklist = Checklist(
                checklistName = input.checklistName,
                checklistDescription = input.checklistDescription,
                checklistId = input.checklistId,
                completionDate = input.completionDate,
                user = getUser()
        )
        checklist = checklistRepository.save(checklist)
        val output = ChecklistOutputModel(
                checklistId = checklist.checklistId,
                name = checklist.checklistName,
                description = checklist.checklistDescription,
                completionDate = checklist.completionDate.toString(),
                username = principal.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Item on a given Checklist")
    @PostMapping("/{checklistId}/items")
    fun addItemToList(
            @PathVariable checklistId: Long,
            @RequestBody input: ItemInputModel
    ): ResponseEntity<Entity>  {
        val checklist = checklistRepository.findById(checklistId).get()
        var item = Item(
                itemName = input.itemName,
                itemDescription = input.itemDescription,
                itemState = State.valueOf(input.itemState),
                checklist = checklist
        )
        item = itemRepository.save(item)
        val output = ItemOutputModel(
                checklistId = checklist.checklistId,
                name = item.itemName,
                description = item.itemDescription,
                state = item.itemState.toString()
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates a set of Checklists")
    @PutMapping
    fun updateChecklists(@RequestBody input: ChecklistCollectionInputModel): ResponseEntity<Entity>{
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

    @ApiOperation(value = "Updates specific Checklist")
    @PutMapping("/{checklistId}")
    fun updateSpecificChecklist(
            @PathVariable checklistId: Long,
            @RequestBody input: ChecklistInputModel
    ): ResponseEntity<Entity> {
        var checklist = Checklist(
                checklistName = input.checklistName,
                checklistId = checklistId,
                checklistDescription = input.checklistDescription,
                user = getUser(),
                items = itemRepository.findByChecklist(
                        checklistRepository.findById(checklistId).get()
                ).toMutableSet()
        )
        checklist = checklistRepository.save(checklist)
        val output = ChecklistOutputModel(
                checklistId = checklist.checklistId,
                name = checklist.checklistName,
                description = checklist.checklistDescription,
                completionDate = checklist.completionDate.toString(),
                username = principal.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates a set of Items from a Checklist")
    @PutMapping("/{checklistId}/items")
    fun updateItems(
            @PathVariable checklistId: Long,
            @RequestBody input: ItemCollectionInputModel
    ): ResponseEntity<Entity>{
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

    @ApiOperation(value = "Updates specific Iem from a Checklist")
    @PutMapping("/{checklistId}/items/{itemId}")
    fun updateItem(
            @PathVariable checklistId: Long,
            @PathVariable itemId: Long,
            @RequestBody input: ItemInputModel
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findById(checklistId).get()
        var item = Item(
                itemName = input.itemName,
                itemDescription = input.itemDescription,
                itemState = State.valueOf(input.itemState),
                checklist = checklist,
                itemId = itemId
        )
        item = itemRepository.save(item)
        val output = ItemOutputModel(
                checklistId = checklist.checklistId,
                name = item.itemName,
                description = item.itemDescription,
                state = item.itemState.toString()
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Deletes all Checklists")
    @DeleteMapping
    fun deleteAllChecklists() = checklistRepository.deleteAll()

    @ApiOperation(value = "Deletes specific Checklist")
    @DeleteMapping("/{checklistId}")
    fun deleteSpecificChecklist(@PathVariable checklistId: Long) = checklistRepository.deleteById(checklistId)

    @ApiOperation(value = "Deletes all Items from a specific Checklist")
    @DeleteMapping("{checklistId}/items")
    fun deleteItem(@PathVariable checklistId: Long) = itemRepository.deleteByChecklist(Checklist(checklistId = checklistId))

    @ApiOperation(value = "Deletes specific Item from a Checklist")
    @DeleteMapping("{checklistId}/items/{itemId}")
    fun deleteSpecificItem(@PathVariable checklistId: Long, @PathVariable itemId: Long) = itemRepository.deleteByChecklistAndItemId(Checklist(checklistId = checklistId), itemId)

}
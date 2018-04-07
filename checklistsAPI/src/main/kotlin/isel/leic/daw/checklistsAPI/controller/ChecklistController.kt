package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemInputModel
import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.model.State
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.outputModel.collection.ChecklistCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistOutputModel
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/checklists", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Checklists")
class ChecklistController {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    @ApiOperation(value = "Returns all Checklists")
    @GetMapping
    fun getAllChecklists(
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklists = checklistRepository.findByUser(User(username = principal.name))
        val output = ChecklistCollectionOutputModel(
                checklists.map {
                    ChecklistOutputModel(
                            checklistId = it.checklistId,
                            name = it.checklistName,
                            description = it.checklistDescription,
                            completionDate = it.completionDate.toString(),
                            username = principal.name
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns the details of a specific Checklist")
    @GetMapping("/{checklistId}")
    fun getChecklist(
            @ApiParam(value = "The identifier of the desire Checklist ", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
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
    fun getItemsOfChecklist(
            @ApiParam(value = "The identifier of the Checklist where the Items belong", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
        val items = itemRepository.findByChecklist(checklist)
        val output = ItemCollectionOutputModel(
                checklistId,
                items.map {
                    ItemOutputModel(
                            itemId = it.itemId,
                            name = it.itemName,
                            description = it.itemDescription,
                            state = it.itemState.toString(),
                            checklistId = checklistId
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns the details of a specific Item")
    @GetMapping("/{checklistId}/items/{itemId}")
    fun getItemOfChecklist(
            @ApiParam(value = "The identifier of the Checklist where the Item belongs", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "The identifier of the Item", required = true)
            @PathVariable itemId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
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
    fun addChecklist(
            @ApiParam(value = "Input that represents the Checklist to be created", required = true)
            @RequestBody input: ChecklistInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        var checklist = Checklist(
                checklistName = input.checklistName,
                checklistDescription = input.checklistDescription,
                checklistId = input.checklistId,
                completionDate = input.completionDate,
                user = User(username = principal.name)
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
            @ApiParam(value = "The identifier of the Checklist for which a new Item will be created", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "Input that represents the Item to be created", required = true)
            @RequestBody input: ItemInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
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
    fun updateChecklists(
            @ApiParam(value = "Input that represents a set of Checklists to be updated", required = true)
            @RequestBody input: ChecklistCollectionInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklists = input.checklists
                .map {
                    Checklist(
                            checklistName = it.checklistName,
                            checklistDescription = it.checklistDescription,
                            checklistId = it.checklistId,
                            user = User(username = principal.name),
                            items = itemRepository.findByChecklist(
                                    checklistRepository.findByChecklistIdAndUser(it.checklistId, User(username = principal.name))
                                            .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
                            ).toMutableSet()
                    )
                }
        checklistRepository.saveAll(checklists.asIterable())
        val output = ChecklistCollectionOutputModel(
                checklists.map {
                    ChecklistOutputModel(
                            checklistId = it.checklistId,
                            name = it.checklistName,
                            description = it.checklistDescription,
                            completionDate = it.completionDate.toString(),
                            username = principal.name
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates specific Checklist")
    @PutMapping("/{checklistId}")
    fun updateSpecificChecklist(
            @ApiParam(value = "The identifier of the Checklist to be updated", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "Input that represents the Checklist updated", required = true)
            @RequestBody input: ChecklistInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        var checklist = Checklist(
                checklistName = input.checklistName,
                checklistId = checklistId,
                checklistDescription = input.checklistDescription,
                user = User(username = principal.name),
                items = itemRepository.findByChecklist(
                        checklistRepository.findByChecklistIdAndUser(input.checklistId, User(username = principal.name))
                                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
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
            @ApiParam(value = "The identifier of the Checklist for wich the Items will be updated", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "Input that represents a set of Items updated", required = true)
            @RequestBody input: ItemCollectionInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
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
        val output = ItemCollectionOutputModel(
                checklistId,
                items.map {
                    ItemOutputModel(
                            itemId = it.itemId,
                            name = it.itemName,
                            description = it.itemDescription,
                            state = it.itemState.toString(),
                            checklistId = checklistId
                    )
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates specific Item from a Checklist")
    @PutMapping("/{checklistId}/items/{itemId}")
    fun updateItem(
            @ApiParam(value = "The identifier of the Checklist for wich the Item will be updated", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "The identifier of the Item to be updated", required = true)
            @PathVariable itemId: Long,
            @ApiParam(value = "Input that represents the Item updated", required = true)
            @RequestBody input: ItemInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
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
    fun deleteAllChecklists(principal: Principal) = checklistRepository.deleteByUser(User(username = principal.name))

    @ApiOperation(value = "Deletes specific Checklist")
    @DeleteMapping("/{checklistId}")
    fun deleteSpecificChecklist(
            @ApiParam(value = "The identifier of the Checklist to be deleted", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ) = checklistRepository.deleteByChecklistIdAndUser(checklistId, User(username = principal.name))

    @ApiOperation(value = "Deletes all Items from a specific Checklist")
    @DeleteMapping("{checklistId}/items")
    fun deleteItem(
            @ApiParam(value = "The identifier of the Checklist from which the Items will be deleted", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ) = itemRepository.deleteByChecklist(checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
            .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") }))

    @ApiOperation(value = "Deletes specific Item from a Checklist")
    @DeleteMapping("{checklistId}/items/{itemId}")
    fun deleteSpecificItem(
            @ApiParam(value = "The identifier of the Checklist from wich the Item will be deleted", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "The identifier of the Item to be deleted", required = true)
            @PathVariable itemId: Long,
            principal: Principal
    ) = itemRepository.deleteByChecklistAndItemId(
            checklistRepository.findByChecklistIdAndUser(checklistId, User(username = principal.name))
                    .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
            , itemId
    )

}
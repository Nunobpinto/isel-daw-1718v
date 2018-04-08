package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemInputModel
import isel.leic.daw.checklistsAPI.mappers.InputMapper
import isel.leic.daw.checklistsAPI.mappers.OutputMapper
import isel.leic.daw.checklistsAPI.model.*
import isel.leic.daw.checklistsAPI.outputModel.collection.ChecklistCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistOutputModel
import isel.leic.daw.checklistsAPI.outputModel.single.ItemOutputModel
import isel.leic.daw.checklistsAPI.service.ChecklistService
import isel.leic.daw.checklistsAPI.service.ItemServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/checklists", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Checklists")
class ChecklistController {

    @Autowired
    lateinit var checklistService: ChecklistService
    @Autowired
    lateinit var itemServiceImpl: ItemServiceImpl

    lateinit var inputMapper: InputMapper
    lateinit var outputMapper: OutputMapper

    @ApiOperation(value = "Returns all Checklists")
    @ApiResponses(
            ApiResponse(code = 200, message = "Checklists successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request")
    )
    @GetMapping
    fun getAllChecklists(
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklists = checklistService.getChecklistByUser(User(username = principal.name))
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
    @ApiResponses(
            ApiResponse(code = 200, message = "Checklist successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @GetMapping("/{checklistId}")
    fun getChecklist(
            @ApiParam(value = "The identifier of the desire Checklist ", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
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
    @ApiResponses(
            ApiResponse(code = 200, message = "Items successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @GetMapping("/{checklistId}/items")
    fun getItemsOfChecklist(
            @ApiParam(value = "The identifier of the Checklist where the Items belong", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
        val items = itemServiceImpl.getItemsByChecklist(checklist)
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
    @ApiResponses(
            ApiResponse(code = 200, message = "Item successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist or Item Not Found")
    )
    @GetMapping("/{checklistId}/items/{itemId}")
    fun getItemOfChecklist(
            @ApiParam(value = "The identifier of the Checklist where the Item belongs", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "The identifier of the Item", required = true)
            @PathVariable itemId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
        val item = itemServiceImpl.getItemByChecklistAndItemId(checklist, itemId)
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
    @ApiResponses(
            ApiResponse(code = 201, message = "Checklist successfully created"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct")
    )
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
        checklist = checklistService.saveChecklist(checklist)
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
    @ApiResponses(
            ApiResponse(code = 201, message = "Item successfully created"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @PostMapping("/{checklistId}/items")
    fun addItemToList(
            @ApiParam(value = "The identifier of the Checklist for which a new Item will be created", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "Input that represents the Item to be created", required = true)
            @RequestBody input: ItemInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
        var item = Item(
                itemName = input.itemName,
                itemDescription = input.itemDescription,
                itemState = State.valueOf(input.itemState),
                checklist = checklist
        )
        item = itemServiceImpl.saveItem(item)
        val output = ItemOutputModel(
                checklistId = checklist.checklistId,
                name = item.itemName,
                description = item.itemDescription,
                state = item.itemState.toString()
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates a set of Checklists")
    @ApiResponses(
            ApiResponse(code = 200, message = "Checklists successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct")
    )
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
                            items = itemServiceImpl.getItemsByChecklist(
                                    checklistService.getChecklistByIdAndUser(it.checklistId, User(username = principal.name))
                                            .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
                            ).toMutableSet()
                    )
                }
        checklistService.saveAllChecklists(checklists.asIterable())
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
    @ApiResponses(
            ApiResponse(code = 200, message = "Checklist successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @PutMapping("/{checklistId}")
    fun updateSpecificChecklist(
            @ApiParam(value = "The identifier of the Checklist to be updated", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "Input that represents the Checklist updated", required = true)
            @RequestBody input: ChecklistInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val originalChecklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
        val checklist = Checklist(
                checklistName = input.checklistName,
                checklistId = checklistId,
                checklistDescription = input.checklistDescription,
                completionDate = input.completionDate,
                user = User(username = principal.name),
                items = itemServiceImpl.getItemsByChecklist(originalChecklist).toMutableSet(),
                template = originalChecklist.template
        )
        checklistService.saveChecklist(checklist)
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
    @ApiResponses(
            ApiResponse(code = 200, message = "Items successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @PutMapping("/{checklistId}/items")
    fun updateItems(
            @ApiParam(value = "The identifier of the Checklist for wich the Items will be updated", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "Input that represents a set of Items updated", required = true)
            @RequestBody input: ItemCollectionInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
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
    @ApiResponses(
            ApiResponse(code = 200, message = "Item successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist or Item Not Found")
    )
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
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
        var item = Item(
                itemName = input.itemName,
                itemDescription = input.itemDescription,
                itemState = State.valueOf(input.itemState),
                checklist = checklist,
                itemId = itemId
        )
        item = itemServiceImpl.saveItem(item)
        val output = ItemOutputModel(
                checklistId = checklist.checklistId,
                name = item.itemName,
                description = item.itemDescription,
                state = item.itemState.toString()
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Deletes all Checklists")
    @ApiResponses(
            ApiResponse(code = 200, message = "All Checklists successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request")
    )
    @DeleteMapping
    fun deleteAllChecklists(principal: Principal) = checklistService.deleteAllChecklistByUser(User(username = principal.name))

    @ApiOperation(value = "Deletes specific Checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "Checklist successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @DeleteMapping("/{checklistId}")
    fun deleteSpecificChecklist(
            @ApiParam(value = "The identifier of the Checklist to be deleted", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ) = checklistService.deleteChecklistByIdAndUser(checklistId, User(username = principal.name))

    @ApiOperation(value = "Deletes all Items from a specific Checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "All Items successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist Not Found")
    )
    @DeleteMapping("{checklistId}/items")
    fun deleteItems(
            @ApiParam(value = "The identifier of the Checklist from which the Items will be deleted", required = true)
            @PathVariable checklistId: Long,
            principal: Principal
    ) = itemServiceImpl.deleteAllItemsByChecklist(checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
            .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") }))

    @ApiOperation(value = "Deletes specific Item from a Checklist")
    @ApiResponses(
            ApiResponse(code = 200, message = "Item successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Checklist or Item Not Found")
    )
    @DeleteMapping("{checklistId}/items/{itemId}")
    fun deleteSpecificItem(
            @ApiParam(value = "The identifier of the Checklist from wich the Item will be deleted", required = true)
            @PathVariable checklistId: Long,
            @ApiParam(value = "The identifier of the Item to be deleted", required = true)
            @PathVariable itemId: Long,
            principal: Principal
    ) = itemServiceImpl.deleteItemByIdAndChecklist(
            checklistService.getChecklistByIdAndUser(checklistId, User(username = principal.name))
                    .orElseThrow({ AccessDeniedException("No permission granted to access this checklist") })
            , itemId
    )

}
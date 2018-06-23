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
import isel.leic.daw.checklistsAPI.service.ChecklistService
import isel.leic.daw.checklistsAPI.service.ItemService
import isel.leic.daw.checklistsAPI.service.ItemServiceImpl
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    lateinit var itemService: ItemService

    val inputMapper: InputMapper = InputMapper()
    val outputMapper: OutputMapper = OutputMapper()

    @ApiOperation(value = "Returns all Checklists")
    @ApiResponses(
            ApiResponse(code = 200, message = "Checklists successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request")
    )
    @GetMapping
    fun getAllChecklists(
            principal: Principal,
            @ApiParam(value = "Number of elements to skip", required = false)
            @RequestParam(value = "offset", required = false, defaultValue = "0") offset: String,
            @ApiParam(value = "Limit the elements to be shown", required = false)
            @RequestParam(value = "limit", required = false, defaultValue = "0") limit: String
    ): ResponseEntity<Entity> {
        val user = User(sub = principal.name)
        val checklists: List<Checklist>
        checklists = if (offset == "0" && limit == "0") checklistService.getChecklistByUser(user)
        else checklistService.getChecklistByUserPaginated(user, offset.toInt(), limit.toInt())
        val output = ChecklistCollectionOutputModel(
                offset = offset,
                limit = limit,
                checklists = checklists.map {
                    outputMapper.toChecklistOutput(it, principal.name)
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
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        val output = outputMapper.toChecklistOutput(checklist, principal.name)
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
            principal: Principal,
            @ApiParam(value = "Number of elements to skip", required = false)
            @RequestParam(value = "offset", required = false, defaultValue = "0") offset: String,
            @ApiParam(value = "Limit the elements to be shown", required = false)
            @RequestParam(value = "limit", required = false, defaultValue = "0") limit: String
    ): ResponseEntity<Entity> {
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        val items: List<Item>
        items = if (offset == "0" && limit == "0") itemService.getItemsByChecklist(checklist)
        else itemService.getItemsByChecklistPaginated(checklist, offset.toInt(), limit.toInt())
        val output = ItemCollectionOutputModel(
               checklistId = checklistId,
               items = items.map {
                    outputMapper.toItemOutput(it, checklistId)
                },
                offset = offset,
                limit = limit
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
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        val item = itemService.getItemByChecklistAndItemId(checklist, itemId)
        val output = outputMapper.toItemOutput(item, checklistId)
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
        var checklist = inputMapper.toChecklist(input, User(sub = principal.name))
        checklist = checklistService.saveChecklist(checklist)
        val output = outputMapper.toChecklistOutput(checklist, principal.name)
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
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        var item = inputMapper.toItem(input, checklist)
        item = itemService.saveItem(item)
        val output = outputMapper.toItemOutput(item, checklistId)
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
                    val items =
                            itemService.getItemsByChecklist(
                                    checklistService.getChecklistByIdAndUser(
                                            it.checklistId,
                                            User(sub = principal.name)
                                    ).orElseThrow({ NotFoundException("The resource doesn't exist") })
                            ).toMutableSet()

                    inputMapper.toChecklist(
                            it,
                            User(sub = principal.name),
                            items
                    )
                }
        checklistService.saveAllChecklists(checklists.asIterable())
        val output = ChecklistCollectionOutputModel(
                checklists.map {
                    outputMapper.toChecklistOutput(it, principal.name)
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
        val originalChecklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        input.checklistId = checklistId
        val checklist = inputMapper.toChecklist(
                input,
                User(sub = principal.name),
                itemService.getItemsByChecklist(originalChecklist).toMutableSet(),
                originalChecklist.template
        )
        checklistService.saveChecklist(checklist)
        val output = outputMapper.toChecklistOutput(checklist, principal.name)
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
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        val items = input
                .items
                .map {
                    inputMapper.toItem(it, checklist)
                }
        val output = ItemCollectionOutputModel(
                checklistId,
                items.map {
                    outputMapper.toItemOutput(it, checklistId)
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
        val checklist = checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        input.itemId = itemId
        var item = inputMapper.toItem(input, checklist)
        item = itemService.saveItem(item)
        val output = outputMapper.toItemOutput(item, checklistId)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Deletes all Checklists")
    @ApiResponses(
            ApiResponse(code = 200, message = "All Checklists successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request")
    )
    @DeleteMapping
    fun deleteAllChecklists(principal: Principal) = checklistService.deleteAllChecklistByUser(User(sub = principal.name))

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
    ) = checklistService.deleteChecklistByIdAndUser(checklistId, User(sub = principal.name))

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
    ) = itemService.deleteAllItemsByChecklist(checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
            .orElseThrow({ NotFoundException("The resource doesn't exist") }))

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
    ) = itemService.deleteItemByIdAndChecklist(
            checklistService.getChecklistByIdAndUser(checklistId, User(sub = principal.name))
                    .orElseThrow({ NotFoundException("The resource doesn't exist") })
            , itemId
    )

}
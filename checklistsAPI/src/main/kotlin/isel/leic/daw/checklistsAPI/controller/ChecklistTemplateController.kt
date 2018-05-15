package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistTemplateCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemTemplateCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistTemplateInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemTemplateInputModel
import isel.leic.daw.checklistsAPI.mappers.InputMapper
import isel.leic.daw.checklistsAPI.mappers.OutputMapper
import isel.leic.daw.checklistsAPI.model.*
import isel.leic.daw.checklistsAPI.outputModel.collection.ChecklistCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.collection.ChecklistTemplateCollectionOutputModel
import isel.leic.daw.checklistsAPI.outputModel.collection.ItemTemplateCollectionOutputModel
import isel.leic.daw.checklistsAPI.service.ChecklistService
import isel.leic.daw.checklistsAPI.service.ChecklistTemplateService
import isel.leic.daw.checklistsAPI.service.ItemService
import isel.leic.daw.checklistsAPI.service.ItemTemplateService
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/templates", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Templates of Checklists")
class ChecklistTemplateController {

    @Autowired
    lateinit var checklistTemplateService: ChecklistTemplateService
    @Autowired
    lateinit var itemTemplateService: ItemTemplateService
    @Autowired
    lateinit var checklistService: ChecklistService
    @Autowired
    lateinit var itemService: ItemService

    val inputMapper: InputMapper = InputMapper()
    val outputMapper: OutputMapper = OutputMapper()


    @ApiOperation(value = "Returns all Templates")
    @ApiResponses(
            ApiResponse(code = 200, message = "Templates successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request")
    )
    @GetMapping
    fun getAllTemplates(
            principal: Principal,
            @ApiParam(value = "Number of elements to skip", required = false)
            @RequestParam(value = "offset", required = false, defaultValue = "0") offset: String,
            @ApiParam(value = "Limit the elements to be shown", required = false)
            @RequestParam(value = "limit", required = false, defaultValue = "0") limit: String
    ): ResponseEntity<Entity> {
        val user = User(username = principal.name)
        val checklistTemplates: List<ChecklistTemplate>
        checklistTemplates = if (offset == "0" && limit == "0") checklistTemplateService.getTemplatesByUser(user)
        else checklistTemplateService.getTemplatesByUserPaginated(user, offset.toInt(), limit.toInt())
        val output = ChecklistTemplateCollectionOutputModel(
                offset = offset,
                limit = limit,
                checklistTemplates = checklistTemplates.map {
                    outputMapper.toChecklistTemplateOutput(it, principal.name)
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns the details of a specific Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Template successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(
            @ApiParam(value = "The identifier of the desire Template ", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        val output = outputMapper.toChecklistTemplateOutput(template, principal.name)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns all Items of a specific Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Items successfully retrieved"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(
            @ApiParam(value = "The identifier of the Template where the Items belong", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal,
            @ApiParam(value = "Number of elements to skip", required = false)
            @RequestParam(value = "offset", required = false, defaultValue = "0") offset: String,
            @ApiParam(value = "Limit the elements to be shown", required = false)
            @RequestParam(value = "limit", required = false, defaultValue = "0") limit: String
    ): ResponseEntity<Entity> {
        val checklistTemplate = checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist")})
        val itemTemplates: List<ItemTemplate>
        itemTemplates = if (offset == "0" && limit == "0") itemTemplateService.getItemsByTemplate(checklistTemplate)
        else itemTemplateService.getItemsByTemplatePaginated(checklistTemplate, offset.toInt(), limit.toInt())
        val output = ItemTemplateCollectionOutputModel(
                templateId = checklistTemplateId,
                itemTemplates = itemTemplates.map {
                    outputMapper.toItemTemplateOutput(it, checklistTemplateId)
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
            ApiResponse(code = 404, message = "Template or Item Not Found")
    )
    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklistTemplate(
            @ApiParam(value = "The identifier of the Template where the Item belongs", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item", required = true)
            @PathVariable itemId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist")})
        val itemTemplate = itemTemplateService.getItemTemplateByIdAndTemplate(template, itemId)
        val output = outputMapper.toItemTemplateOutput(itemTemplate, checklistTemplateId)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Template")
    @ApiResponses(
            ApiResponse(code = 201, message = "Template successfully created"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct")
    )
    @PostMapping
    fun addChecklistTemplate(
            @ApiParam(value = "Input that represents the Template to be created", required = true)
            @RequestBody input: ChecklistTemplateInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.saveTemplate(
                ChecklistTemplate(
                        checklistTemplateName = input.checklistTemplateName,
                        checklistTemplateDescription = input.checklistTemplateDescription,
                        user = User(username = principal.name)
                )
        )
        val output = outputMapper.toChecklistTemplateOutput(template, principal.name)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Checklist from a specific Template")
    @ApiResponses(
            ApiResponse(code = 201, message = "Checklist successfully created"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @PostMapping("/{checklistTemplateId}")
    fun createChecklistFromTemplate(
            @ApiParam(value = "The identifier of the Template from which the Checklist will be created ", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Checklist to be created", required = true)
            @RequestBody input: ChecklistInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist")})
        val checklist = checklistService.saveChecklist(
                inputMapper.toChecklist(
                        input = input,
                        user = User(username = principal.name),
                        template = template
                )
        )
        val items = itemTemplateService
                .getItemsByTemplate(template)
                .map { itemTemplate ->
                    Item(
                            itemName = itemTemplate.itemTemplateName,
                            itemDescription = itemTemplate.itemTemplateDescription,
                            itemState = itemTemplate.itemTemplateState,
                            checklist = checklist
                    )
                }
        itemService.saveAllItems(items)
        val output = outputMapper.toChecklistOutput(checklist, principal.name)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Item on a given Template")
    @ApiResponses(
            ApiResponse(code = 201, message = "Item successfully created"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @PostMapping("/{checklistTemplateId}/items")
    fun addItemTemplateToCheklistTemplate(
            @ApiParam(value = "The identifier of the Template for which a new Item will be created", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Item to be created", required = true)
            @RequestBody input: ItemTemplateInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.getTemplateByIdAndUser(
                checklistTemplateId,
                User(username = principal.name)
        ).orElseThrow({ NotFoundException("The resource doesn't exist") })

        val itemTemplate = itemTemplateService.saveItemTemplate(
                inputMapper.toItemTemplate(input, template))
        val output = outputMapper.toItemTemplateOutput(itemTemplate, checklistTemplateId)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates a set of Templates")
    @ApiResponses(
            ApiResponse(code = 200, message = "Templates successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct")
    )
    @PutMapping
    fun updateChecklistTemplates(
            @ApiParam(value = "Input that represents a set of Templates to be updated", required = true)
            @RequestBody input: ChecklistTemplateCollectionInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val templates = input
                .checklists
                .map {
                    inputMapper.toChecklistTemplate(
                            it,
                            User(username = principal.name),
                            itemTemplateService.getItemsByTemplate(
                                    checklistTemplateService.getTemplateByIdAndUser(
                                            it.checklistTemplateId,
                                            User(username = principal.name)
                                    ).orElseThrow({ NotFoundException("The resource doesn't exist") })
                            ).toMutableSet())
                }
        checklistTemplateService.saveAllTemplates(templates.asIterable())
        val output = ChecklistTemplateCollectionOutputModel(
                templates.map {
                    outputMapper.toChecklistTemplateOutput(it, principal.name)
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates specific Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Template successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @PutMapping("/{checklistTemplateId}")
    fun updateSpecificChecklistTemplate(
            @ApiParam(value = "The identifier of the Template to be updated", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Template updated", required = true)
            @RequestBody input: ChecklistTemplateInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val items = itemTemplateService.getItemsByTemplate(
                checklistTemplateService.getTemplateByIdAndUser(
                        checklistTemplateId,
                        User(username = principal.name)
                ).orElseThrow({ NotFoundException("The resource doesn't exist") })
        ).toMutableSet()
        input.checklistTemplateId = checklistTemplateId
        val template = checklistTemplateService.saveTemplate(
                inputMapper.toChecklistTemplate(
                        input,
                        User(username = principal.name),
                        items
                )
        )
        val output = outputMapper.toChecklistTemplateOutput(template, principal.name)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates a set of Items from a Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Items successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @PutMapping("/{checklistTemplateId}/items")
    fun updateItemTemplates(
            @ApiParam(value = "The identifier of the Template for wich the Items will be updated", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents a set of Items updated", required = true)
            @RequestBody input: ItemTemplateCollectionInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        val itemTemplates =
                input
                        .itemTemplates
                        .map {
                            inputMapper.toItemTemplate(it, template)
                        }
        itemTemplateService.saveAllItemTemplates(itemTemplates.asIterable())
        val output = ItemTemplateCollectionOutputModel(
                checklistTemplateId,
                itemTemplates.map {
                    outputMapper.toItemTemplateOutput(it, checklistTemplateId)
                }
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Updates specific Item from a Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Item successfully updated"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template or Item Not Found")
    )
    @PutMapping("/{checklistTemplateId}/items/{itemId}")
    fun updateItemTemplate(
            @ApiParam(value = "The identifier of the Template for wich the Item will be updated", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item to be updated", required = true)
            @PathVariable itemId: Long,
            @ApiParam(value = "Input that represents the Item updated", required = true)
            @RequestBody input: ItemTemplateInputModel,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ NotFoundException("The resource doesn't exist") })
        input.itemTemplateId = itemId
        val itemTemplate = inputMapper.toItemTemplate(input, template)
        itemTemplateService.saveItemTemplate(itemTemplate)
        val output = outputMapper.toItemTemplateOutput(itemTemplate, checklistTemplateId)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Deletes all Templates")
    @ApiResponses(
            ApiResponse(code = 200, message = "All Templates successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request")
    )
    @DeleteMapping
    fun deleteAllTemplates(principal: Principal) = checklistTemplateService.deleteAllTemplatesByUser(User(username = principal.name))

    @ApiOperation(value = "Deletes specific Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Template successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @DeleteMapping("/{checklistTemplateId}")
    fun deleteSpecificTemplate(
            @ApiParam(value = "The identifier of the Template to be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ) {
        val checklists: List<Checklist> = checklistService.getChecklistsByTemplate(
                checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                        .orElseThrow({ NotFoundException("The resource doesn't exist") })
        )
        if (checklists.isNotEmpty()) {
            checklists.forEach { it.template = null }
            checklistService.saveAllChecklists(checklists)
        }
        checklistTemplateService.deleteTemplateById(checklistTemplateId)
    }

    @ApiOperation(value = "Deletes all Items from a specific Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "All Items successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template Not Found")
    )
    @DeleteMapping("{checklistTemplateId}/items")
    fun deleteAllItemTemplates(
            @ApiParam(value = "The identifier of the Template from wich the Items will be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ) = itemTemplateService.deleteAllItemsByTemplate(
            checklistTemplateService.getTemplateByIdAndUser(
                    checklistTemplateId,
                    User(username = principal.name)
            ).orElseThrow({ NotFoundException("The resource doesn't exist") })
    )

    @ApiOperation(value = "Deletes specific Item from a Template")
    @ApiResponses(
            ApiResponse(code = 200, message = "Item successfully deleted"),
            ApiResponse(code = 400, message = "Bad Request - Parameters may not be correct"),
            ApiResponse(code = 404, message = "Template or Item Not Found")
    )
    @DeleteMapping("{checklistTemplateId}/items/{itemTemplateId}")
    fun deleteSpecificItemTemplate(
            @ApiParam(value = "The identifier of the Template from wich the Item will be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item to be deleted", required = true)
            @PathVariable itemTemplateId: Long,
            principal: Principal
    ) = itemTemplateService.deleteItemByIdAndTemplate(
            checklistTemplateService.getTemplateByIdAndUser(checklistTemplateId, User(username = principal.name))
                    .orElseThrow({ NotFoundException("The resource doesn't exist") })
            , itemTemplateId)

}
package isel.leic.daw.checklistsAPI.controller

import io.swagger.annotations.Api
import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistTemplateCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemTemplateCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistTemplateInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemTemplateInputModel
import isel.leic.daw.checklistsAPI.model.*
import isel.leic.daw.checklistsAPI.outputModel.single.ChecklistTemplateOutputModel
import isel.leic.daw.checklistsAPI.outputModel.single.ItemTemplateOutputModel
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ChecklistTemplateRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import isel.leic.daw.checklistsAPI.repo.ItemTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.context.SecurityContextHolder
import java.security.Principal

@RestController
@RequestMapping("/api/templates", produces = [Siren4J.JSON_MEDIATYPE])
@Api(description = "Operations pertaining to Templates of Checklists")
class ChecklistTemplateController {

    @Autowired
    lateinit var checklistTemplateRepository: ChecklistTemplateRepository
    @Autowired
    lateinit var itemTemplateRepository: ItemTemplateRepository
    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    @ApiOperation(value = "Returns all Templates")
    @GetMapping
    fun getAllTemplates(principal: Principal) = checklistTemplateRepository.findByUser(User(username = principal.name))

    @ApiOperation(value = "Returns the details of a specific Template")
    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(
            @ApiParam(value = "The identifier of the desire Template ", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        val output = ChecklistTemplateOutputModel(
                templateId = checklistTemplateId,
                name = template.checklistTemplateName,
                description = template.checklistTemplateDescription,
                username = principal.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns all Items of a specific Template")
    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(
            @ApiParam(value = "The identifier of the Template where the Items belong", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ): List<ItemTemplate> {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        return itemTemplateRepository.findByChecklistTemplate(template)
    }

    @ApiOperation(value = "Returns the details of a specific Item")
    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklistTemplate(
            @ApiParam(value = "The identifier of the Template where the Item belongs", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item", required = true)
            @PathVariable itemId: Long,
            principal: Principal
    ): ResponseEntity<Entity> {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        val itemTemplate = itemTemplateRepository.findByChecklistTemplateAndItemTemplateId(template, itemId)
        val output = ItemTemplateOutputModel(
                name = itemTemplate.itemTemplateName,
                description = itemTemplate.itemTemplateDescription,
                state = itemTemplate.itemTemplateState.toString(),
                itemTemplateId = itemTemplate.itemTemplateId,
                templateId = checklistTemplateId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Template")
    @PostMapping
    fun addChecklistTemplate(
            @ApiParam(value = "Input that represents the Template to be created", required = true)
            @RequestBody input: ChecklistTemplateInputModel,
            principal: Principal
    ): ChecklistTemplate {
        val template = ChecklistTemplate(
                checklistTemplateName = input.checklistTemplateName,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = User(username = principal.name)
        )
        return checklistTemplateRepository.save(template)
    }

    @ApiOperation(value = "Creates a new Checklist from a specific Template")
    @PostMapping("/{checklistTemplateId}")
    fun createChecklistFromTemplate(
            @ApiParam(value = "The identifier of the Template from which the Checklist will be created ", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Checklist to be created", required = true)
            @RequestBody input: ChecklistInputModel,
            principal: Principal
    ): Checklist {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        var checklist = Checklist(
                checklistName = input.checklistName,
                completionDate = input.completionDate,
                checklistDescription = input.checklistDescription,
                template = template,
                user = User(username = principal.name)
        )
        checklist = checklistRepository.save(checklist)
        val items = itemTemplateRepository
                .findByChecklistTemplate(template)
                .map { itemTemplate ->
                    Item(itemName = itemTemplate.itemTemplateName,
                            itemDescription = itemTemplate.itemTemplateDescription,
                            itemState = itemTemplate.itemTemplateState,
                            checklist = checklist)
                }
        itemRepository.saveAll(items)
        return checklist
    }

    @ApiOperation(value = "Creates a new Item on a given Template")
    @PostMapping("/{checklistTemplateId}/items")
    fun addItemTemplateToCheklistTemplate(
            @ApiParam(value = "The identifier of the Template for which a new Item will be created", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Item to be created", required = true)
            @RequestBody input: ItemTemplateInputModel,
            principal: Principal
    ): ItemTemplate {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        val itemTemplate = ItemTemplate(
                itemTemplateName = input.itemTemplateName,
                itemTemplateDescription = input.itemTemplateDescription,
                itemTemplateState = State.valueOf(input.itemTemplateState),
                checklistTemplate = template
        )
        return itemTemplateRepository.save(itemTemplate)
    }

    @ApiOperation(value = "Updates a set of Templates")
    @PutMapping
    fun updateChecklistTemplates(
            @ApiParam(value = "Input that represents a set of Templates to be updated", required = true)
            @RequestBody input: ChecklistTemplateCollectionInputModel,
            principal: Principal
    ): List<ChecklistTemplate> {
        val templates = input
                .checklists
                .map {
                    ChecklistTemplate(
                            checklistTemplateName = it.checklistTemplateName,
                            checklistTemplateId = it.checklistTemplateId,
                            checklistTemplateDescription = it.checklistTemplateDescription,
                            user = User(username = principal.name),
                            items = itemTemplateRepository.findByChecklistTemplate(
                                    checklistTemplateRepository.findByChecklistTemplateIdAndUser(it.checklistTemplateId, User(username = principal.name))
                                            .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
                            ).toMutableSet()
                    )
                }
        return checklistTemplateRepository.saveAll(templates.asIterable()).toList()
    }

    @ApiOperation(value = "Updates specific Template")
    @PutMapping("/{checklistTemplateId}")
    fun updateSpecificChecklistTemplate(
            @ApiParam(value = "The identifier of the Template to be updated", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Template updated", required = true)
            @RequestBody input: ChecklistTemplateInputModel,
            principal: Principal
    ): ChecklistTemplate {
        val template = ChecklistTemplate(
                checklistTemplateName = input.checklistTemplateName,
                checklistTemplateId = checklistTemplateId,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = User(username = principal.name),
                items = itemTemplateRepository.findByChecklistTemplate(
                        checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
                ).toMutableSet()
        )
        return checklistTemplateRepository.save(template)
    }

    @ApiOperation(value = "Updates a set of Items from a Template")
    @PutMapping("/{checklistTemplateId}/items")
    fun updateItemTemplates(
            @ApiParam(value = "The identifier of the Template for wich the Items will be updated", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents a set of Items updated", required = true)
            @RequestBody input: ItemTemplateCollectionInputModel,
            principal: Principal
    ): List<ItemTemplate> {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        val itemTemplates = input
                .itemTemplates
                .map {
                    ItemTemplate(
                            itemTemplateName = it.itemTemplateName,
                            itemTemplateDescription = it.itemTemplateDescription,
                            itemTemplateState = State.valueOf(it.itemTemplateState),
                            checklistTemplate = template,
                            itemTemplateId = it.itemTemplateId
                    )
                }
        return itemTemplateRepository.saveAll(itemTemplates.asIterable()).toList()
    }

    @ApiOperation(value = "Updates specific Item from a Template")
    @PutMapping("/{checklistTemplateId}/items/{itemId}")
    fun updateItemTemplate(
            @ApiParam(value = "The identifier of the Template for wich the Item will be updated", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item to be updated", required = true)
            @PathVariable itemId: Long,
            @ApiParam(value = "Input that represents the Item updated", required = true)
            @RequestBody input: ItemTemplateInputModel,
            principal: Principal
    ): ItemTemplate {
        val template = checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        val itemTemplate = ItemTemplate(
                itemTemplateName = input.itemTemplateName,
                itemTemplateDescription = input.itemTemplateDescription,
                itemTemplateState = State.valueOf(input.itemTemplateState),
                checklistTemplate = template,
                itemTemplateId = itemId
        )
        return itemTemplateRepository.save(itemTemplate)
    }

    @ApiOperation(value = "Deletes all Templates")
    @DeleteMapping
    fun deleteAllTemplates(principal: Principal) = checklistTemplateRepository.deleteByUser(User(username = principal.name))

    @ApiOperation(value = "Deletes specific Template")
    @DeleteMapping("/{checklistTemplateId}")
    fun deleteSpecificTemplate(
            @ApiParam(value = "The identifier of the Template to be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ) {
        val checklists: List<Checklist> = checklistRepository.findByTemplate(
                checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                        .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
        )
        if (checklists.isNotEmpty()) {
            checklists.forEach { it.template = null }
            checklistRepository.saveAll(checklists)
        }
        checklistTemplateRepository.deleteById(checklistTemplateId)
    }

    @ApiOperation(value = "Deletes all Items from a specific Template")
    @DeleteMapping("{checklistTemplateId}/items")
    fun deleteItemTemplate(
            @ApiParam(value = "The identifier of the Template from wich the Items will be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            principal: Principal
    ) = itemTemplateRepository.deleteByChecklistTemplate(checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
            .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
    )

    @ApiOperation(value = "Deletes specific Item from a Template")
    @DeleteMapping("{checklistTemplateId}/items/{itemTemplateId}")
    fun deleteSpecificItemTemplate(
            @ApiParam(value = "The identifier of the Template from wich the Item will be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item to be deleted", required = true)
            @PathVariable itemTemplateId: Long,
            principal: Principal
    ) = itemTemplateRepository.deleteByChecklistTemplateAndItemTemplateId(
            checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, User(username = principal.name))
                    .orElseThrow({ AccessDeniedException("No permission granted to access this template") })
            , itemTemplateId)

}


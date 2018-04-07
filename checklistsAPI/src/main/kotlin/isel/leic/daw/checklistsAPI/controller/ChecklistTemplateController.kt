package isel.leic.daw.checklistsAPI.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import isel.leic.daw.checklistsAPI.inputModel.collection.ChecklistTemplateCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.collection.ItemTemplateCollectionInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistTemplateInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemTemplateInputModel
import isel.leic.daw.checklistsAPI.model.*
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ChecklistTemplateRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import isel.leic.daw.checklistsAPI.repo.ItemTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/api/templates")
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

    fun getUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        return User(username = auth.name)
    }

    @ApiOperation(value = "Returns all Templates")
    @GetMapping
    fun getAllTemplates() = checklistTemplateRepository.findAll()

    @ApiOperation(value = "Returns the details of a specific Template")
    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(
            @ApiParam(value = "The identifier of the desire Template ", required = true)
            @PathVariable checklistTemplateId: Long
    ) = checklistTemplateRepository.findById(checklistTemplateId).get()

    @ApiOperation(value = "Returns all Items of a specific Template")
    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(
            @ApiParam(value = "The identifier of the Template where the Items belong", required = true)
            @PathVariable checklistTemplateId: Long
    ): List<ItemTemplate> {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplate(checklistTemplate)
    }

    @ApiOperation(value = "Returns the details of a specific Item")
    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklistTemplate(
            @ApiParam(value = "The identifier of the Template where the Item belongs", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item", required = true)
            @PathVariable itemId: Long
    ): ItemTemplate {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplateAndItemTemplateId(checklistTemplate, itemId)
    }

    @ApiOperation(value = "Creates a new Template")
    @PostMapping
    fun addChecklistTemplate(
            @ApiParam(value = "Input that represents the Template to be created", required = true)
            @RequestBody input: ChecklistTemplateInputModel
    ): ChecklistTemplate {
        val template = ChecklistTemplate(
                checklistTemplateName = input.checklistTemplateName,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = getUser()
        )
        return checklistTemplateRepository.save(template)
    }

    @ApiOperation(value = "Creates a new Checklist from a specific Template")
    @PostMapping("/{checklistTemplateId}")
    fun createChecklistFromTemplate(
            @ApiParam(value = "The identifier of the Template from which the Checklist will be created ", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "Input that represents the Checklist to be created", required = true)
            @RequestBody input: ChecklistInputModel
    ): Checklist {
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
        var checklist = Checklist(
                checklistName = input.checklistName,
                completionDate = input.completionDate,
                template = template,
                user = getUser()
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
            @RequestBody input: ItemTemplateInputModel
    ): ItemTemplate {
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
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
            @RequestBody input: ChecklistTemplateCollectionInputModel
    ): List<ChecklistTemplate> {
        val templates = input
                .checklists
                .map {
                    ChecklistTemplate(
                            checklistTemplateName = it.checklistTemplateName,
                            checklistTemplateId = it.checklistTemplateId,
                            checklistTemplateDescription = it.checklistTemplateDescription,
                            user = getUser(),
                            items = itemTemplateRepository.findByChecklistTemplate(
                                    checklistTemplateRepository.findById(it.checklistTemplateId).get()
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
            @RequestBody input: ChecklistTemplateInputModel
    ): ChecklistTemplate {
        val template = ChecklistTemplate(
                checklistTemplateName = input.checklistTemplateName,
                checklistTemplateId = checklistTemplateId,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = getUser(),
                items = itemTemplateRepository.findByChecklistTemplate(
                        checklistTemplateRepository.findById(checklistTemplateId).get()
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
            @RequestBody input: ItemTemplateCollectionInputModel
    ): List<ItemTemplate> {
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
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
            @RequestBody input: ItemTemplateInputModel
    ): ItemTemplate {
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
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
    fun deleteAllTemplates() = checklistTemplateRepository.deleteAll()

    @ApiOperation(value = "Deletes specific Template")
    @DeleteMapping("/{checklistTemplateId}")
    fun deleteSpecificTemplate(
            @ApiParam(value = "The identifier of the Template to be deleted", required = true)
            @PathVariable checklistTemplateId: Long
    ) {
        val checklists: List<Checklist> = checklistRepository.findByTemplate(ChecklistTemplate(checklistTemplateId = checklistTemplateId))
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
            @PathVariable checklistTemplateId: Long
    ) = itemTemplateRepository.deleteByChecklistTemplate(ChecklistTemplate(checklistTemplateId = checklistTemplateId))

    @ApiOperation(value = "Deletes specific Item from a Template")
    @DeleteMapping("{checklistTemplateId}/items/{itemTemplateId}")
    fun deleteSpecificItemTemplate(
            @ApiParam(value = "The identifier of the Template from wich the Item will be deleted", required = true)
            @PathVariable checklistTemplateId: Long,
            @ApiParam(value = "The identifier of the Item to be deleted", required = true)
            @PathVariable itemTemplateId: Long
    ) = itemTemplateRepository.deleteByChecklistTemplateAndItemTemplateId(ChecklistTemplate(checklistTemplateId = checklistTemplateId), itemTemplateId)

}


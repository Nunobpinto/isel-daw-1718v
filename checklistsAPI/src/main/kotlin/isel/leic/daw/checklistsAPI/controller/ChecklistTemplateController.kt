package isel.leic.daw.checklistsAPI.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.ApiOperation
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
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/api/templates", produces = [Siren4J.JSON_MEDIATYPE])
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

    @ApiOperation(value = "Returns all the Templates")
    @GetMapping
    fun getAllTemplates() : ResponseEntity<Entity> = checklistTemplateRepository.findAll()

    @ApiOperation(value = "Returns the details of a specific Template")
    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(@PathVariable checklistTemplateId: Long) : ResponseEntity<Entity>{
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
        val output = ChecklistTemplateOutputModel(
                checklistId = checklist.checklistId,
                name = checklist.checklistName,
                description = checklist.checklistDescription,
                completionDate = checklist.completionDate.toString(),
                username = principal.name
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Returns all Items of a specific Template")
    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(@PathVariable checklistTemplateId: Long): ResponseEntity<Entity> {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplate(checklistTemplate)
    }

    @ApiOperation(value = "Returns the details of a specific Item")
    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklistTemplate(@PathVariable checklistTemplateId: Long, @PathVariable itemId: Long): ResponseEntity<Entity> {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        val itemTemplate = itemTemplateRepository.findByChecklistTemplateAndItemTemplateId(checklistTemplate, itemId)
        val output = ItemTemplateOutputModel(
                name = itemTemplate.itemTemplateName!!,
                description = itemTemplate.itemTemplateDescription!!,
                state = itemTemplate.itemTemplateState.toString(),
                itemTemplateId = itemTemplate.itemTemplateId,
                templateId = checklistTemplateId
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(output))
    }

    @ApiOperation(value = "Creates a new Template")
    @PostMapping
    fun addChecklistTemplate(@RequestBody input: ChecklistTemplateInputModel): ResponseEntity<Entity> {
        val template = ChecklistTemplate(
                checklistTemplateName = input.checklistTemplateName,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = getUser()
        )
        return checklistTemplateRepository.save(template)
    }

    @ApiOperation(value = "Creates a new Checklist from a specific Template")
    @PostMapping("/{checklistTemplateId}")
    fun createChecklistFromTemplate(@PathVariable checklistTemplateId: Long, @RequestBody input: ChecklistInputModel): ResponseEntity<Entity> {
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
            @PathVariable checklistTemplateId: Long,
            @RequestBody input: ItemTemplateInputModel
    ): ResponseEntity<Entity> {
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
    fun updateChecklistTemplates(@RequestBody input: ChecklistTemplateCollectionInputModel): ResponseEntity<Entity> {
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
            @PathVariable checklistTemplateId: Long,
            @RequestBody input: ChecklistTemplateInputModel
    ): ResponseEntity<Entity> {
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
            @PathVariable checklistTemplateId: Long,
            @RequestBody input: ItemTemplateCollectionInputModel
    ): ResponseEntity<Entity> {
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

    @ApiOperation(value = "Updates specific Iem from a Template")
    @PutMapping("/{checklistTemplateId}/items/{itemId}")
    fun updateItemTemplate(
            @PathVariable checklistTemplateId: Long,
            @PathVariable itemId: Long,
            @RequestBody input: ItemTemplateInputModel
    ): ResponseEntity<Entity> {
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
    fun deleteSpecificTemplate(@PathVariable checklistTemplateId: Long) {
        val checklists: List<Checklist> = checklistRepository.findByTemplate(ChecklistTemplate(checklistTemplateId = checklistTemplateId))
        if (checklists.isNotEmpty()) {
            checklists.forEach { it.template = null }
            checklistRepository.saveAll(checklists)
        }
        checklistTemplateRepository.deleteById(checklistTemplateId)
    }

    @ApiOperation(value = "Deletes all Items from a specific Template")
    @DeleteMapping("{checklistTemplateId}/items")
    fun deleteItemTemplate(@PathVariable checklistTemplateId: Long) = itemTemplateRepository.deleteByChecklistTemplate(ChecklistTemplate(checklistTemplateId = checklistTemplateId))

    @ApiOperation(value = "Deletes specific Item from a Template")
    @DeleteMapping("{checklistTemplateId}/items/{itemTemplateId}")
    fun deleteSpecificItemTemplate(@PathVariable checklistTemplateId: Long, @PathVariable itemTemplateId: Long) = itemTemplateRepository.deleteByChecklistTemplateAndItemTemplateId(ChecklistTemplate(checklistTemplateId = checklistTemplateId), itemTemplateId)

    //TODO: PATCHs

}


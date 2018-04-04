package isel.leic.daw.checklistsAPI.controller

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
@RequestMapping("/templates")
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

    @GetMapping
    fun getAllTemplates() = checklistTemplateRepository.findAll()

    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(@PathVariable checklistTemplateId: Long) = checklistTemplateRepository.findById(checklistTemplateId)

    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(@PathVariable checklistTemplateId: Long): List<ItemTemplate> {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplate(checklistTemplate)
    }

    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklistTemplate(@PathVariable checklistTemplateId: Long, @PathVariable itemId: Long): ItemTemplate {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplateAndItemTemplateId(checklistTemplate, itemId)
    }

    @PostMapping
    fun addChecklistTemplate(@RequestBody input: ChecklistTemplateInputModel): ChecklistTemplate {
        val template = ChecklistTemplate(
                checklistTemplateName = input.checklistTemplateName,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = getUser()
        )
        return checklistTemplateRepository.save(template)
    }

    @PostMapping("/{checklistTemplateId}")
    fun createChecklistFromTemplate(@PathVariable checklistTemplateId: Long, @RequestBody input: ChecklistInputModel): Checklist {
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

    @PostMapping("/{checklistTemplateId}/items")
    fun addItemTemplateToCheklistTemplate(
            @PathVariable checklistTemplateId: Long,
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

    @PutMapping
    fun updateChecklistTemplates(@RequestBody input: ChecklistTemplateCollectionInputModel): List<ChecklistTemplate> {
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

    @PutMapping("/{checklistTemplateId}")
    fun updateSpecificChecklistTemplate(
            @PathVariable checklistTemplateId: Long,
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

    @PutMapping("/{checklistTemplateId}/items")
    fun updateItemTemplates(
            @PathVariable checklistTemplateId: Long,
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

    @PutMapping("/{checklistTemplateId}/items/{itemId}")
    fun updateItemTemplate(
            @PathVariable checklistTemplateId: Long,
            @PathVariable itemId: Long,
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

    @DeleteMapping
    fun deleteAllTemplates() = checklistTemplateRepository.deleteAll()

    @DeleteMapping("/{checklistTemplateId}")
    fun deleteSpecificTemplate(@PathVariable checklistTemplateId: Long) {
        val checklists: List<Checklist> = checklistRepository.findByTemplate(ChecklistTemplate(checklistTemplateId = checklistTemplateId))
        if (checklists.isNotEmpty()) {
            checklists.forEach { it.template = null }
            checklistRepository.saveAll(checklists)
        }
        checklistTemplateRepository.deleteById(checklistTemplateId)
    }

    @DeleteMapping("{checklistTemplateId}/items")
    fun deleteItemTemplate(@PathVariable checklistTemplateId: Long) = itemTemplateRepository.deleteByChecklistTemplate(ChecklistTemplate(checklistTemplateId = checklistTemplateId))

    @DeleteMapping("{checklistTemplateId}/items/{itemTemplateId}")
    fun deleteSpecificItemTemplate(@PathVariable checklistTemplateId: Long, @PathVariable itemTemplateId: Long) = itemTemplateRepository.deleteByChecklistTemplateAndItemTemplateId(ChecklistTemplate(checklistTemplateId = checklistTemplateId), itemTemplateId)

    //TODO: PATCHs

}


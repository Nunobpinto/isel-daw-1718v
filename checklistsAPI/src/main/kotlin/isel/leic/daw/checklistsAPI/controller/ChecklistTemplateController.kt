package isel.leic.daw.checklistsAPI.controller

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import isel.leic.daw.checklistsAPI.repo.ChecklistTemplateRepository
import isel.leic.daw.checklistsAPI.repo.ItemTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/templates")
class ChecklistTemplateController{

    @Autowired
    lateinit var checklistTemplateRepository: ChecklistTemplateRepository
    @Autowired
    lateinit var itemTemplateRepository: ItemTemplateRepository

    @GetMapping("/")
    fun getAllTemplates() = checklistTemplateRepository.findAll()

    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(@PathVariable checklistTemplateId: Long)
            = checklistTemplateRepository.findById(checklistTemplateId)

    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(@PathVariable checklistTemplateId: Long)
            = checklistTemplateRepository.findById(checklistTemplateId).get().items

    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklist(
            @PathVariable checklistTemplateId: Long,
            @PathVariable itemId: Long
    ) = checklistTemplateRepository.findById(checklistTemplateId).get().items?.find { it.itemTemplateId == itemId }

    //TODO: extend WbMvcConfigurationSupport
    //TODO: create checklistTemplateArgumentResolver
    @PostMapping("/")
    fun addChecklistTemplate(checklistTemplate: ChecklistTemplate) = checklistTemplateRepository.save(checklistTemplate)

    //TODO: create itemTemplateArgumentResolver
    @PostMapping("/{checklistId}/items")
    fun addItemTemplateToListTemplate(
            @PathVariable checklistTemplateId: Int,
            itemTemplate: ItemTemplate
    ) = itemTemplateRepository.save(itemTemplate)

    //TODO: PUTs

    //TODO: DELETEs

    //TODO: PATCHs


}
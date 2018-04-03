package isel.leic.daw.checklistsAPI.controller

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import isel.leic.daw.checklistsAPI.repo.ChecklistTemplateRepository
import isel.leic.daw.checklistsAPI.repo.ItemTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/templates")
class ChecklistTemplateController{

    @Autowired
    lateinit var checklistTemplateRepository: ChecklistTemplateRepository
    @Autowired
    lateinit var itemTemplateRepository: ItemTemplateRepository

    @GetMapping
    fun getAllTemplates() = checklistTemplateRepository.findAll()

    @GetMapping("/{checklistTemplateId}")
    fun getTemplate(@PathVariable checklistTemplateId: Long)
            = checklistTemplateRepository.findById(checklistTemplateId)

    @GetMapping("/{checklistTemplateId}/items")
    fun getItemsOfChecklistTemplate(@PathVariable checklistTemplateId: Long):List<ItemTemplate>{
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplateId(checklistTemplate)
    }

    @GetMapping("/{checklistTemplateId}/items/{itemId}")
    fun getItemOfChecklist(
            @PathVariable checklistTemplateId: Long,
            @PathVariable itemId: Long
    ) : ItemTemplate {
        val checklistTemplate = checklistTemplateRepository.findById(checklistTemplateId).get()
        return itemTemplateRepository.findByChecklistTemplateIdAndItemTemplateId(checklistTemplate,itemId)
    }

    @PostMapping
    fun addChecklistTemplate(checklistTemplate: ChecklistTemplate) = checklistTemplateRepository.save(checklistTemplate)

    /*@PostMapping("/{checklistTemplateId}")
    fun */

    @PostMapping("/{checklistTemplateId}/items")
    fun addItemTemplateToListTemplate(
            @PathVariable checklistTemplateId: Long,
            itemTemplate: ItemTemplate
    ) = itemTemplateRepository.save(itemTemplate)

    @PutMapping
    fun updateTemplates(checklistTemplates : List<ChecklistTemplate>) = checklistTemplateRepository.saveAll(checklistTemplates)

    @PutMapping("/{checklistTemplateId}")
    fun updateSpecificTemplate(
            @PathVariable checklistTemplateId: Long,
            checklistTemplate: ChecklistTemplate
    ) = checklistTemplateRepository.save(checklistTemplate)

    @PutMapping("/{checklistTemplateId}/items")
    fun updateItemTemplates(
            @PathVariable checklistTemplateId: Long,
            itemTemplates: List<ItemTemplate>
    ) = itemTemplateRepository.saveAll(itemTemplates)

    @PutMapping("/{checklistTemplateId}/items/{itemId}")
    fun updateItemTemplates(
            @PathVariable checklistTemplateId: Long,
            @PathVariable itemId: Long,
            itemTemplates: List<ItemTemplate>
    ) = itemTemplateRepository.saveAll(itemTemplates)

    @DeleteMapping
    fun deleteAllTemplates() = checklistTemplateRepository.deleteAll()

    @DeleteMapping("/{checklistTemplateId}")
    fun deleteSpecificTemplate(@PathVariable checklistTemplateId: Long) {
        //TODO ver se é necessário apagar os itens associados primeiro
        checklistTemplateRepository.delete(checklistTemplateRepository.findById(checklistTemplateId).get())
    }

    @DeleteMapping("{checklistTemplateId}/items")
    fun deleteItemTemplates(
            @PathVariable checklistTemplateId: Long
    ) = itemTemplateRepository.deleteItemTemplateByChecklistTemplateId(checklistTemplateId)

    //TODO: DELETEs

    //TODO: PATCHs


}
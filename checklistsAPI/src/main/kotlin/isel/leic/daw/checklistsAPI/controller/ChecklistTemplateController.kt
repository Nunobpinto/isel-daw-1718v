package isel.leic.daw.checklistsAPI.controller

import isel.leic.daw.checklistsAPI.inputModel.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.ChecklistTemplateInputModel
import isel.leic.daw.checklistsAPI.inputModel.ItemTemplateInputModel
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
class ChecklistTemplateController{

    @Autowired
    lateinit var checklistTemplateRepository: ChecklistTemplateRepository
    @Autowired
    lateinit var itemTemplateRepository: ItemTemplateRepository
    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    fun getUsername() : String {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.name
    }

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
    fun addChecklistTemplate(@RequestBody input: ChecklistTemplateInputModel) : ChecklistTemplate {
        val template = ChecklistTemplate(
                checklistTemplateName =input.checklistTemplateName,
                checklistTemplateDescription = input.checklistTemplateDescription,
                user = User(username = getUsername())
        )
       return checklistTemplateRepository.save(template)
    }

    @PostMapping("/{checklistTemplateId}")
    fun createChecklistFromTemplate(
            @PathVariable checklistTemplateId: Long,
            @RequestBody input : ChecklistInputModel
    ) : Checklist {
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
        var checklist = Checklist(
                checklistName = input.checklistName,
                completionDate = input.completionDate,
                //items = items.toMutableSet(),
                template = template,
                user = User(username = getUsername())
        )
        checklist = checklistRepository.save(checklist)
        val items = itemTemplateRepository
                .findByChecklistTemplateId(template)
                .map { itemTemplate -> Item(itemName = itemTemplate.itemTemplateName,
                        itemDescription = itemTemplate.itemTemplateDescription,
                        itemState = itemTemplate.itemTemplateState,
                        checklist = checklist)
                }
        itemRepository.saveAll(items)
        return checklist
    }

    @PostMapping("/{checklistTemplateId}/items")
    fun addItemTemplateToListTemplate(
            @PathVariable checklistTemplateId: Long,
            @RequestBody input: ItemTemplateInputModel
    ) : ItemTemplate{
        val template = checklistTemplateRepository.findById(checklistTemplateId).get()
        val itemTemplate = ItemTemplate(
                itemTemplateName = input.itemTemplateName,
                itemTemplateDescription = input.itemTemplateDescription,
                itemTemplateState = State.valueOf(input.itemTemplateState),
                checklistTemplateId = template
        )
        return itemTemplateRepository.save(itemTemplate)
    }

    @PutMapping
    fun updateChecklistTemplates(checklistTemplates : List<ChecklistTemplate>) = checklistTemplateRepository.saveAll(checklistTemplates)

    @PutMapping("/{checklistTemplateId}")
    fun updateSpecificChecklistTemplate(
            @PathVariable checklistTemplateId: Long,
            checklistTemplate: ChecklistTemplate
    ) = checklistTemplateRepository.save(checklistTemplate)

    @PutMapping("/{checklistTemplateId}/items")
    fun updateItemTemplates(
            @PathVariable checklistTemplateId: Long,
            itemTemplates: List<ItemTemplate>
    ) = itemTemplateRepository.saveAll(itemTemplates)

    @PutMapping("/{checklistTemplateId}/items/{itemId}")
    fun updateItemTemplate(
            @PathVariable checklistTemplateId: Long,
            @PathVariable itemId: Long,
            itemTemplate: ItemTemplate
    ) = itemTemplateRepository.save(itemTemplate)

    @DeleteMapping
    fun deleteAllChecklistTemplates() = checklistTemplateRepository.deleteAll()

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
package isel.leic.daw.checklistsAPI.controller

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item
import isel.leic.daw.checklistsAPI.model.State
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import isel.leic.daw.checklistsAPI.repo.ItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
class ChecklistController {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository
    @Autowired
    lateinit var itemRepository: ItemRepository

    @GetMapping("/checklists")
    fun getAllChecklists() = checklistRepository.findAll()

    @GetMapping("/checklists/{checklistId}")
    fun getChecklist(@PathVariable checklistId: Long) = checklistRepository.findById(checklistId)

    @GetMapping("/checklists/{checklistId}/items")
    fun getItemsOfChecklist(@PathVariable checklistId: Long) = checklistRepository.findById(checklistId).get().items

    @GetMapping("/checklists/{checklistId}/items/{itemId}")
    fun getItemOfChecklist(
            @PathVariable checklistId: Long,
            @PathVariable itemId: Long
    ) = checklistRepository.findById(checklistId).get().items?.find { it.itemId == itemId }

    //TODO: extend WbMvcConfigurationSupport
    //TODO: create checklistArgumentResolver
    @PostMapping("/checklists")
    fun addChecklist(checklist: Checklist) = checklistRepository.save(checklist)

    //TODO: create itemArgumentResolver
    @PostMapping("/checklists/{checklistId}/items")
    fun addItemToList(
            @PathVariable checklistId: Int,
            item: Item
    ) = itemRepository.save(item)

    //TODO: PUTs

    //TODO: DELETEs

    //TODO: PATCHs

}
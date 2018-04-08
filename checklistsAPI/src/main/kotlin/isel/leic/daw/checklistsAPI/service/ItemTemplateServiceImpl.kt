package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import isel.leic.daw.checklistsAPI.repo.ItemTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemTemplateServiceImpl : ItemTemplateService {

    @Autowired
    lateinit var itemTemplateRepository: ItemTemplateRepository

    override fun getItemsByTemplate(template: ChecklistTemplate) =
            itemTemplateRepository.findByChecklistTemplate(template)

    override fun getItemTemplateByIdAndTemplate(template: ChecklistTemplate, itemId: Long) =
            itemTemplateRepository.findByChecklistTemplateAndItemTemplateId(template, itemId)

    override fun saveItemTemplate(itemTemplate: ItemTemplate) =
            itemTemplateRepository.save(itemTemplate)

    override fun saveAllItemTemplates(itemTemplates: Iterable<ItemTemplate>) =
            itemTemplateRepository.saveAll(itemTemplates)

    override fun deleteItemByIdAndTemplate(checklistTemplate: ChecklistTemplate, itemId: Long) =
            itemTemplateRepository.deleteByChecklistTemplateAndItemTemplateId(checklistTemplate, itemId)

    override fun deleteAllItemsByTemplate(checklistTemplate: ChecklistTemplate) =
            itemTemplateRepository.deleteByChecklistTemplate(checklistTemplate)

}
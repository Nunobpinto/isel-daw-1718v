package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.MyCustomPageable
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate
import isel.leic.daw.checklistsAPI.repo.ItemTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ItemTemplateServiceImpl : ItemTemplateService {

    @Autowired
    lateinit var itemTemplateRepository: ItemTemplateRepository

    override fun getItemsByTemplate(checklistTemplate: ChecklistTemplate) =
            itemTemplateRepository.findByChecklistTemplate(checklistTemplate)

    override fun getItemsByTemplatePaginated(checklistTemplate: ChecklistTemplate, offset: Int, limit: Int): List<ItemTemplate> {
        val pageable = MyCustomPageable(offset, limit, Sort(Sort.Direction.ASC,"itemTemplateId")) as Pageable
        return itemTemplateRepository.findByChecklistTemplate(checklistTemplate, pageable)
    }

    override fun getItemTemplateByIdAndTemplate(checklistTemplate: ChecklistTemplate, itemId: Long) =
            itemTemplateRepository.findByChecklistTemplateAndItemTemplateId(checklistTemplate, itemId)

    override fun saveItemTemplate(itemTemplate: ItemTemplate) =
            itemTemplateRepository.save(itemTemplate)

    override fun saveAllItemTemplates(itemTemplates: Iterable<ItemTemplate>) =
            itemTemplateRepository.saveAll(itemTemplates)

    override fun deleteItemByIdAndTemplate(checklistTemplate: ChecklistTemplate, itemId: Long) =
            itemTemplateRepository.deleteByChecklistTemplateAndItemTemplateId(checklistTemplate, itemId)

    override fun deleteAllItemsByTemplate(checklistTemplate: ChecklistTemplate) =
            itemTemplateRepository.deleteByChecklistTemplate(checklistTemplate)

}
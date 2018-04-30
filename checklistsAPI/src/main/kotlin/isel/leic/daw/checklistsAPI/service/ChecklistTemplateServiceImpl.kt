package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.MyCustomPageable
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.ChecklistTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ChecklistTemplateServiceImpl : ChecklistTemplateService {

    @Autowired
    lateinit var checklistTemplateRepository: ChecklistTemplateRepository

    override fun getTemplatesByUserPaginated(user: User, offset: Int, limit: Int): List<ChecklistTemplate> {
        val pageable = MyCustomPageable(offset, limit, Sort(Sort.Direction.ASC,"checklistTemplateId")) as Pageable
        return checklistTemplateRepository.findByUser(user, pageable)
    }

    override fun getTemplatesByUser(user: User) =
            checklistTemplateRepository.findByUser(user)

    override fun getTemplateByIdAndUser(checklistTemplateId: Long, user: User) =
            checklistTemplateRepository.findByChecklistTemplateIdAndUser(checklistTemplateId, user)

    override fun saveTemplate(template: ChecklistTemplate) =
            checklistTemplateRepository.save(template)

    override fun saveAllTemplates(templates: Iterable<ChecklistTemplate>) =
            checklistTemplateRepository.saveAll(templates)

    override fun deleteTemplateById(checklistTemplateId: Long) =
            checklistTemplateRepository.deleteById(checklistTemplateId)

    override fun deleteAllTemplatesByUser(user: User) =
            checklistTemplateRepository.deleteByUser(user)

}
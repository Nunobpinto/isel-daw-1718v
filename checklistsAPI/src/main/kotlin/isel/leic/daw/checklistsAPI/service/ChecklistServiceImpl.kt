package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.MyCustomPageable
import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import isel.leic.daw.checklistsAPI.repo.ChecklistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ChecklistServiceImpl : ChecklistService {

    @Autowired
    lateinit var checklistRepository: ChecklistRepository

    override fun getChecklistByUserPaginated(user: User, offset: Int, limit: Int): List<Checklist> {
        val pageable = MyCustomPageable(offset, limit, Sort(Sort.Direction.ASC, "checklistId")) as Pageable
        return checklistRepository.findByUser(user, pageable)
    }

    override fun getChecklistsByTemplatePaginated(checklistTemplate: ChecklistTemplate, offset: Int, limit: Int): List<Checklist> {
        val pageable = MyCustomPageable(offset, limit, Sort(Sort.Direction.ASC, "checklistId")) as Pageable
        return checklistRepository.findByTemplate(checklistTemplate, pageable)
    }

    override fun getChecklistByUser(user: User) =
            checklistRepository.findByUser(user)

    override fun getChecklistByIdAndUser(checklistId: Long, user: User) =
            checklistRepository.findByChecklistIdAndUser(checklistId, user)

    override fun getChecklistsByTemplate(checklistTemplate: ChecklistTemplate) =
            checklistRepository.findByTemplate(checklistTemplate)

    override fun saveChecklist(checklist: Checklist) =
            checklistRepository.save(checklist)

    override fun saveAllChecklists(checklists: Iterable<Checklist>) =
            checklistRepository.saveAll(checklists)

    override fun deleteAllChecklistByUser(user: User) =
            checklistRepository.deleteByUser(user)

    override fun deleteChecklistByIdAndUser(checklistId: Long, user: User) =
            checklistRepository.deleteByChecklistIdAndUser(checklistId, user)

}
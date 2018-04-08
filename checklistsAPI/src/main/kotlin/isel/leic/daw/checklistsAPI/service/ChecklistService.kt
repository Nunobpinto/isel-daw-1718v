package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.User
import java.util.*

interface ChecklistService {

    fun getChecklistByUser(user: User): List<Checklist>

    fun getChecklistByIdAndUser(checklistId: Long, user: User): Optional<Checklist>

    fun getChecklistsByTemplate(checklistTemplate: ChecklistTemplate): List<Checklist>

    fun saveChecklist(checklist: Checklist): Checklist

    fun saveAllChecklists(checklists: Iterable<Checklist>): MutableIterable<Checklist>

    fun deleteChecklistByIdAndUser(checklistId: Long, user: User): Long

    fun deleteAllChecklistByUser(user: User): Long

}
package isel.leic.daw.checklistsAPI.model

import javax.persistence.*

@Entity
@Table(name="checklist_template")
class ChecklistTemplate (
    @Column(name="checklist_template_name")
    val checklisttemplateName : String,

    @Column(name="checklist_template_description")
    val checklisttemplateDescription : String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="checklist_template_id")
    val checklisttemplateID : Long = -1){

    private constructor() : this("","")
}
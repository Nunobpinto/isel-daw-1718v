package isel.leic.daw.checklistsAPI.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name="checklist")
class Checklist (
    @Column(name = "checklist_name")
    val checklistName : String,

    @Column(name = "checklist_completion_date")
    @Temporal(TemporalType.DATE)
    val completionDate: Date,

    @ManyToOne
    @JoinColumn(name = "checklist_template_id")
    val template: ChecklistTemplate,

    @Column(name = "username")
    val username : String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "checklist_id")
    val checklistId:Long = -1){

    private constructor():this("", Date(),ChecklistTemplate("","", ""), "")
}
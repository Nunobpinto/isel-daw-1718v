package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name="checklist")
data class Checklist (
    @Column(name = "checklist_name")
    val checklistName : String? = null,

    @Column(name = "checklist_completion_date")
    val completionDate: LocalDate?= null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checklist_template_id")
    val template: ChecklistTemplate? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    val user: User? = null,

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "checklist")
    val items : MutableSet<Item>?=null,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "checklist_id")
    val checklistId : Long = -1
) : Serializable
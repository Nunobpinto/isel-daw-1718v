package isel.leic.daw.checklistsAPI.model

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name="checklist")
data class Checklist (
    @Column(name = "checklist_name")
    val checklistName : String? = null,

    @Column(name = "checklist_completion_date")
    @Temporal(TemporalType.DATE)
    val completionDate: Date ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_template_id")
    val template: ChecklistTemplate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    val user: User,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemComposeKey.checklist")
    val items : MutableSet<Item>?=null,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "checklist_id")
    val checklistId : Long = -1
) : Serializable
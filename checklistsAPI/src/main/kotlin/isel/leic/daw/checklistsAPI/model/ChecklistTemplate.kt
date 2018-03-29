package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "checklist_template")
data class ChecklistTemplate(
        @Column(name = "checklist_template_name")
        val checklisttemplateName: String ?= null,

        @Column(name = "checklist_template_description")
        val checklisttemplateDescription: String ?= null,

        @JsonIgnore
        @OneToMany(mappedBy = "template")
        val checklists:MutableSet<Checklist>?=null,

        @JsonIgnore
        @OneToMany(mappedBy = "checklistTemplate")
        val  items:MutableSet<ItemTemplate>?=null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username")
        val user: User? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "checklist_template_id")
        val checklisttemplateID: Long = -1
) : Serializable
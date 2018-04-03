package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "checklist_template")
data class ChecklistTemplate(
        @Column(name = "checklist_template_name")
        val checklistTemplateName: String ?= null,

        @Column(name = "checklist_template_description")
        val checklisttemplateDescription: String ?= null,

        @JsonIgnore
        @OneToMany(fetch = FetchType.LAZY,mappedBy = "template")
        val checklists:MutableSet<Checklist>?=null,

        @JsonIgnore
        @OneToMany(fetch = FetchType.LAZY,mappedBy = "checklistTemplateId")
        val items:MutableSet<ItemTemplate>?=null,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "username")
        val user: User? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "checklist_template_id")
        val checklistTemplateId: Long = -1
) : Serializable
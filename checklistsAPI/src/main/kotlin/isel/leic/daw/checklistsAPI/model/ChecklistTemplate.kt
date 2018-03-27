package isel.leic.daw.checklistsAPI.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "checklist_template")
data class ChecklistTemplate(
        @Column(name = "checklist_template_name")
        val checklisttemplateName: String,

        @Column(name = "checklist_template_description")
        val checklisttemplateDescription: String,

        @OneToMany(mappedBy = "template")
        val checklists:MutableSet<Checklist>?=null,

        @OneToMany(mappedBy = "itemTemplateComposeKey.checklistTemplateId")
        val  items:MutableSet<ItemTemplate>?=null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username")
        val user: User,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "checklist_template_id")
        val checklisttemplateID: Long
) : Serializable
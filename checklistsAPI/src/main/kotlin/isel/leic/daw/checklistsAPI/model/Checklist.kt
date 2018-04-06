package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "checklist")
data class Checklist(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "checklist_id")
        val checklistId: Long = -1,

        @Column(name = "checklist_name")
        val checklistName: String = "",

        @Column(name = "checklist_description")
        val checklistDescription: String = "",

        @Column(name = "checklist_completion_date")
        val completionDate: LocalDate = LocalDate.MAX,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_template_id", nullable = true)
        var template: ChecklistTemplate? = ChecklistTemplate(),

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username", nullable = false)
        val user: User? = User(),

        @JsonIgnore
        @OneToMany(mappedBy = "checklist", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
        val items: MutableSet<Item> = mutableSetOf()
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is Checklist) false else checklistId == other.checklistId
        }

        override fun hashCode(): Int = 33
}
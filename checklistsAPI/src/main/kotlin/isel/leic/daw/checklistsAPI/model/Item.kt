package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.builder.EqualsBuilder
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "item")
data class Item(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "item_id")
        val itemId: Long = -1,

        @Column(name = "item_name")
        val itemName: String = "",

        @Column(name = "item_description")
        val itemDescription: String = "",

        @Enumerated(EnumType.STRING)
        @Column(name = "state")
        val itemState: State = State.Uncompleted,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_id", nullable = false)
        val checklist: Checklist? = Checklist()
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is Item) false else itemId == other.itemId
        }

        override fun hashCode(): Int = 31
}
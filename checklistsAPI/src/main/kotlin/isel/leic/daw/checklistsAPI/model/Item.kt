package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name="item")
data class Item(
        @Column(name="item_name")
        val itemName : String? = null,

        @Column(name = "item_description")
        val itemDescription : String?= null,

        @Enumerated(EnumType.STRING)
        @Column(name="state")
        val itemState: State = State.Uncompleted,

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "checklist_id")
        val checklist: Checklist? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "item_id")
        val itemId: Long = -1
//        @EmbeddedId
//        val itemComposeKey: ItemComposeKey
) : Serializable

//@IdClass(ItemComposeKey::class)
//@Embeddable
//data class ItemComposeKey(
////        @Generated(GenerationTime.INSERT)
//        @GeneratedValue(strategy = GenerationType.SEQUENCE)
////        @Column(name="item_id", columnDefinition = "serial")
//        val itemId : Long = -1,
//
//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "checklist_id")
//        val checklist: Checklist
//):Serializable
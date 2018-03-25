package isel.leic.daw.checklistsAPI.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name="item")
class Item(
        @Column(name="item_name")
        val itemName : String,

        @Column(name = "item_description")
        val itemDescription : String,

        @Column(name="state")
        val itemState: State,

        @EmbeddedId
        val itemComposeKey: ItemComposeKey){
      private constructor() :this(
              "",
              "",
              State.Uncompleted,
              ItemComposeKey(
                      Checklist("",
                              Date(),
                              ChecklistTemplate("","", ""),
                              ""
                      )
              )
      )
}

@Embeddable
class ItemComposeKey(
        @ManyToOne
        @JoinColumn(name="checklist_id")
        val checklist: Checklist,
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="item_id")
        val itemId:Long = -1){
    private constructor():this(
            Checklist("",
                      Date(),
                    ChecklistTemplate("","", ""),
                    ""
            )
    )
}
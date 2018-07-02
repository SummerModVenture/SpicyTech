package net.came20.spicytech.item

object MachineComponents {
    val all = arrayOf(
            CrusherComponentItem,
            SawmillComponentItem
    )
}

object CrusherComponentItem: SpicyTechItem("crusher_component")
object SawmillComponentItem: SpicyTechItem("sawmill_component")
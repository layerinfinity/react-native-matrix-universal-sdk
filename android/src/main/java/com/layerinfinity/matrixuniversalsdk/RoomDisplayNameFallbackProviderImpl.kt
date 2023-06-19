package com.layerinfinity.matrixuniversalsdk

import org.matrix.android.sdk.api.provider.RoomDisplayNameFallbackProvider

class RoomDisplayNameFallbackProviderImpl : RoomDisplayNameFallbackProvider {
    override fun getNameFor1member(name: String): String {
        return name
    }

    override fun getNameFor2members(name1: String, name2: String): String {
        return String.format("%s and %s.", name1, name2)
    }

    override fun getNameFor3members(name1: String, name2: String, name3: String): String {
        return String.format("%s and %s and %s.", name1, name2, name3)
    }

    override fun getNameFor4members(name1: String, name2: String, name3: String, name4: String): String {
        return String.format("%s, %s, %s and %s.", name1, name2, name3, name4)
    }

    override fun getNameFor4membersAndMore(name1: String, name2: String, name3: String, remainingCount: Int): String {
        return String.format("%s, %s, %s and %s others", name1, name2, name3, remainingCount)
    }

    override fun getNameForEmptyRoom(isDirect: Boolean, leftMemberNames: List<String>): String {
        return "Empty room"
    }

    override fun getNameForRoomInvite(): String {
        return "Room invite"
    }
}

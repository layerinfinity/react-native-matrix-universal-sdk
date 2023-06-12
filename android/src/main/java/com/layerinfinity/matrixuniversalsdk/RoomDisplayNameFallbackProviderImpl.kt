package com.layerinfinity.matrixuniversalsdk

import org.matrix.android.sdk.api.provider.RoomDisplayNameFallbackProvider

class RoomDisplayNameFallbackProviderImpl : RoomDisplayNameFallbackProvider {
    override fun getNameFor1member(s: String): String {
        return s
    }

    override fun getNameFor2members(name1: String, name2: String): String {
        return String.format("%s and %s.", name1, name2)
    }

    override fun getNameFor3members(s: String, s1: String, s2: String): String {
        return String.format("%s and %s and %s.", s, s1, s2)
    }

    override fun getNameFor4members(s: String, s1: String, s2: String, s3: String): String {
        return String.format("%s, %s, %s and %s.", s, s1, s2, s3)
    }

    override fun getNameFor4membersAndMore(s: String, s1: String, s2: String, i: Int): String {
        return String.format("%s, %s, %s and %s others", s, s1, s2, i)
    }

    override fun getNameForEmptyRoom(b: Boolean, list: List<String>): String {
        return "Empty room"
    }

    override fun getNameForRoomInvite(): String {
        return "Room invite"
    }
}

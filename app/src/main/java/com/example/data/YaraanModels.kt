package com.example.data

import com.example.R

data class UserProfile(
    val nickname: String = "❤ Mašoom",
    val userId: String = "10001",
    val email: String = "yaraan0300@gmail.com",
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop",
    val followersCount: Int = 2048,
    val followCount: Int = 12,
    val fansCount: Int = 2048,
    val visitorsCount: String = "1.11K",
    val familyName: String = "FAMILY",
    val level: Int = 70,
    val svipLevel: String = "SVIP2",
    val brandBadge: String = "Brand",
    val gender: String = "male", // male / female
    val location: String = "Pakistan",
    val locationFlag: String = "🇵🇰",
    val bio: String = "مجھے میری مغرور آنکھیں پسند ہیں۔\nجو ہر کسی کی طرف بے تکلف نہیں اٹھتیں۔",
    val birthday: String = "1998-05-15",
    val constellation: String = "Taurus",
    val tags: List<String> = listOf("Voice Lover", "Gamer", "Urdu Poetry", "VIP"),
    val photos: List<String> = emptyList(),
    val coins: Int = 58200
)

data class VoiceRoom(
    val id: String,
    val title: String,
    val hostName: String,
    val hostAvatarRes: Int = R.drawable.img_user_avatar,
    val hostGender: String = "male", // "male" or "female"
    val hostAge: Int = 22,
    val listenersCount: Int = 1000,
    val tag: String = "Party",
    val isCPStar: Boolean = false,
    val isTreeRoom: Boolean = false,
    val coupleName1: String = "Noo...",
    val coupleName2: String = "Ama...",
    val roomRanking: Int = 1,
    val roomCoverRes: Int = R.drawable.img_cover_bg
)

data class RankingCardData(
    val title: String,
    val bgGradientStart: Long,
    val bgGradientEnd: Long,
    val type: String
)

data class IntimacyCouple(
    val isLinked: Boolean = true,
    val level: String = "Lv.3",
    val daysText: String = "2026-07-12 now 165 days",
    val userName: String = "❤ Mašoom",
    val userScore: Int = 185892,
    val partnerName: String = "Sahil ❤",
    val partnerScore: Int = 185892,
    val partnerAvatar: String = "yaraan_dp.png"
)

data class IntimacyFriend(
    val level: String,
    val name: String,
    val points: Int
)

enum class NavRoute {
    LOGIN,
    HOME,
    PROFILE,
    EDIT_PROFILE,
    VOICE_ROOM,
    STORE,
    WALLET,
    MESSAGES,
    GAMES,
    SVIP,
    VIP,
    LEADERBOARD,
    LEVEL
}

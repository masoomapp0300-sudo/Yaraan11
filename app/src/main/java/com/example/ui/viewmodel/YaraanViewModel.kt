package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.IntimacyCouple
import com.example.data.IntimacyFriend
import com.example.data.NavRoute
import com.example.data.UserProfile
import com.example.data.VoiceRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RoomChatMessage(
    val senderName: String,
    val message: String,
    val isSystem: Boolean = false
)

data class YaraanUiState(
    val currentRoute: NavRoute = NavRoute.HOME,
    val isLoggedIn: Boolean = true,
    val userProfile: UserProfile = UserProfile(
        photos = listOf("avatar_default_1", "avatar_default_2", "avatar_default_3")
    ),
    val activeBottomTab: Int = 0, // 0: Home, 1: Games, 2: Messages, 3: Profile
    val activeHomeTab: String = "Popular", // "Mine", "Popular", "Recent"
    val activeProfileSubTab: String = "Profile", // "Profile", "Intimacy"
    val selectedVoiceRoom: VoiceRoom? = null,
    val isRoomMinimized: Boolean = false,
    val isMicMuted: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val roomMessages: List<RoomChatMessage> = listOf(
        RoomChatMessage("System", "Welcome to Yaraan Voice Room! Please respect community rules.", isSystem = true),
        RoomChatMessage("❤ Mašoom", "Salam everyone! Welcome to the party 🎉"),
        RoomChatMessage("Ayesha", "Hi Mašoom! Beautiful voice room! 💕"),
        RoomChatMessage("Sahil ❤", "Sending CP heart gifts! ✨")
    ),
    val intimacyCouple: IntimacyCouple = IntimacyCouple(),
    val intimacyFriends: List<IntimacyFriend> = listOf(
        IntimacyFriend("Lv.1", "Anya", 1167),
        IntimacyFriend("Lv.3", "Rohan", 105458),
        IntimacyFriend("Lv.2", "Zain", 72685)
    ),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val userCoins: Int = 58200,
    val userExp: Long = 81992682L, // Default Level 70 EXP
    val claimedLevelRewards: Set<Int> = (2..69).toSet(), // Levels 2 to 69 claimed
    val svipLevel: Int = 2,
    val svipTotalRecharge: Int = 58200,
    val svipPeriodRecharge: Int = 18500,
    val svipDaysLeft: Int = 48,
    val activeVipTier: String = "VIP Green",
    val activeVipKey: String = "name-vip-green",
    val purchasedVipTiers: Set<String> = setOf("name-vip-green"),
    val leaderboardInitialTab: Int = 0 // 0: Contribution, 1: Charm, 2: Room
)

class YaraanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(YaraanUiState())
    val uiState: StateFlow<YaraanUiState> = _uiState.asStateFlow()

    fun navigateTo(route: NavRoute) {
        _uiState.update { it.copy(currentRoute = route) }
    }

    fun openLeaderboard(tabIndex: Int = 0) {
        _uiState.update { it.copy(leaderboardInitialTab = tabIndex, currentRoute = NavRoute.LEADERBOARD) }
    }

    fun selectBottomTab(index: Int) {
        _uiState.update {
            val nextRoute = when (index) {
                0 -> NavRoute.HOME
                1 -> NavRoute.GAMES
                2 -> NavRoute.MESSAGES
                3 -> NavRoute.PROFILE
                else -> NavRoute.HOME
            }
            it.copy(activeBottomTab = index, currentRoute = nextRoute)
        }
    }

    fun setHomeTab(tab: String) {
        _uiState.update { it.copy(activeHomeTab = tab) }
    }

    fun setProfileSubTab(tab: String) {
        _uiState.update { it.copy(activeProfileSubTab = tab) }
    }

    private var userIdCounter = 10001

    private fun getNextSequentialUserId(): String {
        return (userIdCounter++).toString()
    }

    fun login(provider: String) {
        _uiState.update { state ->
            val cleanId = getNextSequentialUserId()
            state.copy(
                isLoggedIn = true,
                userProfile = state.userProfile.copy(userId = cleanId),
                currentRoute = NavRoute.HOME,
                activeBottomTab = 0
            )
        }
    }

    fun loginWithGoogle(
        displayName: String?,
        email: String?,
        photoUrl: String?,
        uid: String?
    ) {
        _uiState.update { state ->
            val cleanName = displayName?.takeIf { name -> name.isNotBlank() } ?: email?.substringBefore("@") ?: "❤ Mašoom"
            val cleanId = getNextSequentialUserId()
            val updatedProfile = state.userProfile.copy(
                nickname = cleanName,
                email = email ?: "",
                avatarUrl = photoUrl?.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop" } ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop",
                userId = cleanId
            )
            val updatedCouple = state.intimacyCouple.copy(
                userName = cleanName
            )
            state.copy(
                isLoggedIn = true,
                userProfile = updatedProfile,
                intimacyCouple = updatedCouple,
                currentRoute = NavRoute.HOME,
                activeBottomTab = 0
            )
        }
    }

    fun loginWithEmail(
        emailInput: String,
        nameInput: String = ""
    ) {
        _uiState.update { state ->
            val cleanName = nameInput.ifBlank { emailInput.substringBefore("@") }
            val cleanId = getNextSequentialUserId()
            val updatedProfile = state.userProfile.copy(
                nickname = cleanName,
                email = emailInput,
                userId = cleanId,
                avatarUrl = state.userProfile.avatarUrl.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop" }
            )
            val updatedCouple = state.intimacyCouple.copy(
                userName = cleanName
            )
            state.copy(
                isLoggedIn = true,
                userProfile = updatedProfile,
                intimacyCouple = updatedCouple,
                currentRoute = NavRoute.HOME,
                activeBottomTab = 0
            )
        }
    }

    fun updateAvatarUrl(url: String) {
        _uiState.update { state ->
            val updatedProfile = state.userProfile.copy(avatarUrl = url)
            state.copy(userProfile = updatedProfile)
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(isLoggedIn = false, currentRoute = NavRoute.LOGIN)
        }
    }

    fun updateProfile(nickname: String, birthday: String, constellation: String, bio: String) {
        _uiState.update { state ->
            val updatedProfile = state.userProfile.copy(
                nickname = nickname,
                birthday = birthday,
                constellation = constellation,
                bio = bio
            )
            state.copy(userProfile = updatedProfile)
        }
    }

    fun addPhoto(photoUri: String) {
        _uiState.update { state ->
            val updatedPhotos = state.userProfile.photos + photoUri
            state.copy(userProfile = state.userProfile.copy(photos = updatedPhotos))
        }
    }

    fun removePhoto(photoIndex: Int) {
        _uiState.update { state ->
            if (photoIndex in state.userProfile.photos.indices) {
                val updatedPhotos = state.userProfile.photos.toMutableList().apply { removeAt(photoIndex) }
                state.copy(userProfile = state.userProfile.copy(photos = updatedPhotos))
            } else {
                state
            }
        }
    }

    fun joinVoiceRoom(room: VoiceRoom) {
        _uiState.update {
            it.copy(selectedVoiceRoom = room, isRoomMinimized = false, currentRoute = NavRoute.VOICE_ROOM)
        }
    }

    fun minimizeVoiceRoom() {
        _uiState.update {
            it.copy(isRoomMinimized = true, currentRoute = NavRoute.HOME)
        }
    }

    fun expandVoiceRoom() {
        _uiState.update {
            it.copy(isRoomMinimized = false, currentRoute = NavRoute.VOICE_ROOM)
        }
    }

    fun leaveVoiceRoom() {
        _uiState.update {
            it.copy(selectedVoiceRoom = null, isRoomMinimized = false, currentRoute = NavRoute.HOME)
        }
    }

    fun toggleMic() {
        _uiState.update { it.copy(isMicMuted = !it.isMicMuted) }
    }

    fun toggleSpeaker() {
        _uiState.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    fun sendRoomMessage(text: String) {
        if (text.isBlank()) return
        val msg = RoomChatMessage(_uiState.value.userProfile.nickname, text)
        _uiState.update { it.copy(roomMessages = it.roomMessages + msg) }
    }

    fun rechargeCoins(amount: Int) {
        _uiState.update { it.copy(userCoins = it.userCoins + amount) }
    }

    fun claimLevelReward(lvl: Int, coinReward: Int) {
        _uiState.update {
            it.copy(
                userCoins = it.userCoins + coinReward,
                claimedLevelRewards = it.claimedLevelRewards + lvl
            )
        }
    }

    fun claimAllLevelRewards(claimedLevels: List<Int>, totalCoinsAdded: Int) {
        _uiState.update {
            it.copy(
                userCoins = it.userCoins + totalCoinsAdded,
                claimedLevelRewards = it.claimedLevelRewards + claimedLevels
            )
        }
    }

    fun addExp(amount: Long) {
        _uiState.update { it.copy(userExp = it.userExp + amount) }
    }

    fun activateVip(vipKey: String, vipName: String) {
        _uiState.update {
            it.copy(
                activeVipKey = vipKey,
                activeVipTier = vipName
            )
        }
    }

    fun buyVip(vipKey: String, vipName: String, coinPrice: Int): Boolean {
        val currentCoins = _uiState.value.userCoins
        if (currentCoins >= coinPrice) {
            _uiState.update {
                it.copy(
                    userCoins = it.userCoins - coinPrice,
                    purchasedVipTiers = it.purchasedVipTiers + vipKey,
                    activeVipKey = vipKey,
                    activeVipTier = vipName
                )
            }
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleSearch(active: Boolean) {
        _uiState.update { it.copy(isSearchActive = active) }
    }

    fun acceptCpPartner(partnerName: String) {
        _uiState.update {
            it.copy(
                intimacyCouple = IntimacyCouple(
                    isLinked = true,
                    level = "Lv.3",
                    daysText = "2026-07-30 now 1 day",
                    userName = it.userProfile.nickname.ifBlank { "❤ Mašoom" },
                    partnerName = "$partnerName ❤"
                )
            )
        }
    }

    fun unlinkCpPartner() {
        _uiState.update {
            it.copy(
                intimacyCouple = IntimacyCouple(
                    isLinked = false,
                    level = "Broken Up",
                    daysText = "CP Broken Up",
                    userName = it.userProfile.nickname.ifBlank { "❤ Mašoom" },
                    partnerName = ""
                )
            )
        }
    }

    fun addBestFriend(name: String, level: String = "Lv.1") {
        _uiState.update {
            val updatedList = it.intimacyFriends + IntimacyFriend(level, name, 1200)
            it.copy(intimacyFriends = updatedList)
        }
    }
}

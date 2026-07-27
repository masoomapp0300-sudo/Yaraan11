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
    val svipLevel: Int = 2,
    val svipTotalRecharge: Int = 58200,
    val svipPeriodRecharge: Int = 18500,
    val svipDaysLeft: Int = 48,
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

    fun login(provider: String) {
        _uiState.update {
            it.copy(isLoggedIn = true, currentRoute = NavRoute.HOME, activeBottomTab = 0)
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

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleSearch(active: Boolean) {
        _uiState.update { it.copy(isSearchActive = active) }
    }
}

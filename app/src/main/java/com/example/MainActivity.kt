package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.ui.components.FloatingRoomOverlay
import com.example.data.NavRoute
import com.example.data.VoiceRoom
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.StoreScreen
import com.example.ui.screens.SvipScreen
import com.example.ui.screens.VoiceRoomScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.YaraanTheme
import com.example.ui.viewmodel.YaraanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YaraanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F9FE)
                ) {
                    YaraanApp()
                }
            }
        }
    }
}

@Composable
fun YaraanApp(
    viewModel: YaraanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = uiState.currentRoute, label = "ScreenTransition") { route ->
        when (route) {
            NavRoute.LOGIN -> {
                LoginScreen(
                    onLoginSuccess = { provider ->
                        viewModel.login(provider)
                    },
                    onBack = {
                        viewModel.navigateTo(NavRoute.HOME)
                    }
                )
            }

            NavRoute.HOME -> {
                HomeScreen(
                    selectedTopTab = uiState.activeHomeTab,
                    onTopTabSelected = { viewModel.setHomeTab(it) },
                    bottomNavIndex = uiState.activeBottomTab,
                    onBottomTabSelected = { viewModel.selectBottomTab(it) },
                    onJoinRoom = { room -> viewModel.joinVoiceRoom(room) },
                    onOpenCreateRoom = {
                        val defaultRoom = VoiceRoom(
                            id = "my_room_1",
                            title = "Mašoom's Party Corner 🎉",
                            hostName = uiState.userProfile.nickname,
                            hostAvatarRes = R.drawable.img_user_avatar,
                            listenersCount = 1024,
                            tag = "Music"
                        )
                        viewModel.joinVoiceRoom(defaultRoom)
                    },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    isSearchActive = uiState.isSearchActive,
                    onToggleSearch = { viewModel.toggleSearch(it) },
                    onOpenLeaderboard = { tabIndex -> viewModel.openLeaderboard(tabIndex) }
                )
            }

            NavRoute.LEADERBOARD -> {
                LeaderboardScreen(
                    userProfile = uiState.userProfile,
                    initialMainTab = uiState.leaderboardInitialTab,
                    onBack = { viewModel.navigateTo(NavRoute.HOME) }
                )
            }

            NavRoute.PROFILE -> {
                ProfileScreen(
                    userProfile = uiState.userProfile,
                    activeSubTab = uiState.activeProfileSubTab,
                    onSubTabSelected = { viewModel.setProfileSubTab(it) },
                    bottomNavIndex = uiState.activeBottomTab,
                    onBottomTabSelected = { viewModel.selectBottomTab(it) },
                    onOpenEditProfile = { viewModel.navigateTo(NavRoute.EDIT_PROFILE) },
                    onOpenWallet = { viewModel.navigateTo(NavRoute.WALLET) },
                    onOpenStore = { viewModel.navigateTo(NavRoute.STORE) },
                    onOpenSvip = { viewModel.navigateTo(NavRoute.SVIP) },
                    onAddPhoto = { viewModel.addPhoto(it) },
                    onRemovePhoto = { viewModel.removePhoto(it) },
                    onLogout = { viewModel.logout() },
                    intimacyCouple = uiState.intimacyCouple,
                    intimacyFriends = uiState.intimacyFriends
                )
            }

            NavRoute.SVIP -> {
                SvipScreen(
                    userProfile = uiState.userProfile,
                    userActualLevel = uiState.svipLevel,
                    totalRecharge = uiState.svipTotalRecharge,
                    periodRecharge = uiState.svipPeriodRecharge,
                    daysLeft = uiState.svipDaysLeft,
                    onBack = { viewModel.navigateTo(NavRoute.PROFILE) }
                )
            }

            NavRoute.EDIT_PROFILE -> {
                EditProfileScreen(
                    userProfile = uiState.userProfile,
                    onSaveProfile = { nickname, birthday, constellation, bio ->
                        viewModel.updateProfile(nickname, birthday, constellation, bio)
                        viewModel.navigateTo(NavRoute.PROFILE)
                    },
                    onAddPhoto = { viewModel.addPhoto(it) },
                    onRemovePhoto = { viewModel.removePhoto(it) },
                    onBack = { viewModel.navigateTo(NavRoute.PROFILE) }
                )
            }

            NavRoute.VOICE_ROOM -> {
                val room = uiState.selectedVoiceRoom ?: VoiceRoom(
                    id = "room_default",
                    title = "Yaraan Live Voice Party",
                    hostName = "❤ Mašoom",
                    hostAvatarRes = R.drawable.img_user_avatar,
                    listenersCount = 1200,
                    tag = "Party"
                )

                VoiceRoomScreen(
                    room = room,
                    messages = uiState.roomMessages,
                    isMicMuted = uiState.isMicMuted,
                    isSpeakerOn = uiState.isSpeakerOn,
                    onToggleMic = { viewModel.toggleMic() },
                    onToggleSpeaker = { viewModel.toggleSpeaker() },
                    onSendMessage = { viewModel.sendRoomMessage(it) },
                    onMinimizeRoom = { viewModel.minimizeVoiceRoom() },
                    onLeaveRoom = { viewModel.leaveVoiceRoom() }
                )
            }

            NavRoute.WALLET -> {
                WalletScreen(
                    currentCoins = uiState.userCoins,
                    onRecharge = { amount -> viewModel.rechargeCoins(amount) },
                    onBack = { viewModel.navigateTo(NavRoute.PROFILE) }
                )
            }

            NavRoute.STORE -> {
                StoreScreen(
                    userCoins = uiState.userCoins,
                    onBuyItem = { item ->
                        if (uiState.userCoins >= item.price) {
                            viewModel.rechargeCoins(-item.price)
                        }
                    },
                    onBack = { viewModel.navigateTo(NavRoute.PROFILE) }
                )
            }

            NavRoute.MESSAGES -> {
                MessagesScreen(
                    bottomNavIndex = uiState.activeBottomTab,
                    onBottomTabSelected = { viewModel.selectBottomTab(it) }
                )
            }

            NavRoute.GAMES -> {
                GamesScreen(
                    bottomNavIndex = uiState.activeBottomTab,
                    onBottomTabSelected = { viewModel.selectBottomTab(it) }
                )
            }
        }
        }

        // Draggable / Floating Voice Room Overlay when room is minimized
        val minimizedRoom = uiState.selectedVoiceRoom
        if (minimizedRoom != null && uiState.isRoomMinimized) {
            FloatingRoomOverlay(
                room = minimizedRoom,
                onExpand = { viewModel.expandVoiceRoom() },
                onClose = { viewModel.leaveVoiceRoom() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 90.dp, end = 16.dp)
            )
        }
    }
}

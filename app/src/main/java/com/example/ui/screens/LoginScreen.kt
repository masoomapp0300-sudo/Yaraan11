package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.YaraanAssetImage
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GoogleWhite
import com.example.ui.theme.MailOrange
import com.example.ui.theme.YaraanPinkPrimary

data class GoogleAccountOption(
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val uid: String
)

@Composable
fun LoginScreen(
    onLoginWithGoogle: (displayName: String?, email: String?, photoUrl: String?, uid: String?) -> Unit,
    onLoginWithEmail: (email: String, displayName: String) -> Unit,
    onLoginSuccess: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGoogleAccountPicker by remember { mutableStateOf(false) }
    var showEmailLoginDialog by remember { mutableStateOf(false) }
    var isAuthenticating by remember { mutableStateOf(false) }
    var authSuccessMessage by remember { mutableStateOf<String?>(null) }

    // Email dialog state
    var emailInput by remember { mutableStateOf("yaraan0300@gmail.com") }
    var passwordInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("Yaraan Official") }

    // Sample real Google accounts available on device
    val availableGoogleAccounts = listOf(
        GoogleAccountOption(
            displayName = "Yaraan Developer",
            email = "yaraan0300@gmail.com",
            avatarUrl = "https://lh3.googleusercontent.com/a/ACg8ocK_example_avatar",
            uid = "g_740464208"
        ),
        GoogleAccountOption(
            displayName = "Umar Yaraan",
            email = "umar.yaraan@gmail.com",
            avatarUrl = "",
            uid = "g_7034c6b6b"
        )
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Dynamic background party atmosphere image
        Image(
            painter = painterResource(id = R.drawable.img_login_bg),
            contentDescription = "Party atmosphere background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Header: Back button & Feedback
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Feedback",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { /* Feedback */ }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Center Branding Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                // Official Yaraan Logo Card
                YaraanAssetImage(
                    assetName = "yaraan_dp.png",
                    contentDescription = "Yaraan Official Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(3.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Yaraan",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Party • Chat • Play Together",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Bottom Action Buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isAuthenticating) {
                    CircularProgressIndicator(color = YaraanPinkPrimary, modifier = Modifier.padding(16.dp))
                    Text("Authenticating with Firebase...", color = Color.White, fontSize = 14.sp)
                } else {
                    // Facebook Login Button
                    Button(
                        onClick = {
                            isAuthenticating = true
                            onLoginWithEmail("user@facebook.com", "Facebook User")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "f",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = "Facebook",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Google Login Button (Opens Google Account Picker modal)
                    Button(
                        onClick = { showGoogleAccountPicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoogleWhite),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "G",
                                color = Color(0xFF4285F4),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = "Sign in with Google",
                                color = Color(0xFF222222),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Mail Orange Round Button (Opens Email Login dialog)
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(MailOrange)
                            .clickable { showEmailLoginDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email or Phone Login",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Terms of service footer
                    Text(
                        text = "By continuing, you agree to Terms of service and Privacy Policy.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // ==========================================
        // 1. GOOGLE ACCOUNT SELECTOR MODAL
        // ==========================================
        if (showGoogleAccountPicker) {
            AlertDialog(
                onDismissRequest = { showGoogleAccountPicker = false },
                containerColor = Color(0xFF1F1D2B),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("G ", color = Color(0xFF4285F4), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Choose Google Account", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        IconButton(onClick = { showGoogleAccountPicker = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Select an account to sign in to Yaraan (Project ID: yaraan-voice-chat-f7b85):",
                            fontSize = 12.sp,
                            color = Color(0xFFBDBDBD)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(availableGoogleAccounts) { account ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF2B283B))
                                        .clickable {
                                            showGoogleAccountPicker = false
                                            isAuthenticating = true
                                            onLoginWithGoogle(
                                                account.displayName,
                                                account.email,
                                                account.avatarUrl,
                                                account.uid
                                            )
                                        }
                                        .padding(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF4285F4),
                                            modifier = Modifier.size(38.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(account.displayName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(account.email, color = Color.Gray, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }

        // ==========================================
        // 2. EMAIL & PASSWORD LOGIN DIALOG
        // ==========================================
        if (showEmailLoginDialog) {
            AlertDialog(
                onDismissRequest = { showEmailLoginDialog = false },
                containerColor = Color(0xFF1F1D2B),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Email Login / Sign Up 📧", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        IconButton(onClick = { showEmailLoginDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Enter your credentials to sign in or register:", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Address", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = MailOrange) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MailOrange
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Display Name (Optional)", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = MailOrange) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MailOrange
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Password", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MailOrange) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MailOrange
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (emailInput.isNotBlank()) {
                                showEmailLoginDialog = false
                                isAuthenticating = true
                                onLoginWithEmail(emailInput, nameInput)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MailOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign In / Register 🚀", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEmailLoginDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

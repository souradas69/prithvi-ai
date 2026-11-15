package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.auth.AuthUser
import com.example.data.i18n.LanguageRegistry
import com.example.data.i18n.PrithviI18n
import com.example.ui.components.FarmingBackground
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgPrimaryGreen

@Composable
fun AuthScreen(
    currentLanguage: String,
    isFirebaseCloudConfigured: Boolean,
    onSignIn: (email: String, pass: String, onSuccess: (AuthUser) -> Unit, onError: (String) -> Unit) -> Unit,
    onRegister: (email: String, pass: String, name: String, phone: String, lang: String, prefLangs: List<String>, onSuccess: (AuthUser) -> Unit, onError: (String) -> Unit) -> Unit,
    onSendPasswordReset: (email: String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit,
    onAuthSuccess: (AuthUser) -> Unit,
    onDismissOrBack: () -> Unit,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Sign In, 1: Register
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var resetDialogMessage by remember { mutableStateOf<String?>(null) }
    var isResetLoading by remember { mutableStateOf(false) }

    var showTermsDialog by remember { mutableStateOf(false) }

    fun t(key: String): String = PrithviI18n.t(key, currentLanguage)

    FarmingBackground(isDarkMode = isDarkMode) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with back navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismissOrBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = t("back")
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = t("community_title"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Title
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(AgPrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon),
                    contentDescription = t("app_name"),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "PRITHVI AI",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AgPrimaryGreen
            )

            Text(
                text = "Smart Farming Assistant",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )

            Text(
                text = "Please sign in or create an account to access PRITHVI AI features.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Firebase Status Pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isFirebaseCloudConfigured) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isFirebaseCloudConfigured) AgDarkGreen else Color(0xFFFFA000)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isFirebaseCloudConfigured) Icons.Default.CloudDone else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isFirebaseCloudConfigured) AgDarkGreen else Color(0xFFFFA000),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFirebaseCloudConfigured) {
                            "Firebase Cloud Authentication & Firestore Sync Active"
                        } else {
                            "Offline-First Secure Local Profile Active. Place google-services.json in /app to link external Firebase Cloud."
                        },
                        fontSize = 11.sp,
                        color = if (isFirebaseCloudConfigured) AgDarkGreen else Color(0xFF5D4037),
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error / Success feedback
            if (!errorMessage.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (!successMessage.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = successMessage ?: "",
                        color = AgDarkGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // Tab Switcher
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        errorMessage = null
                    },
                    text = { Text("Sign In", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        errorMessage = null
                    },
                    text = { Text("Create Account", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == 0) {
                // SIGN IN FORM
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = { Text(t("email")) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AgPrimaryGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AgPrimaryGreen) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            resetEmailInput = email
                            resetDialogMessage = null
                            showForgotPasswordDialog = true
                        }
                    ) {
                        Text(
                            text = "Forgot Password?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AgDarkGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        onSignIn(
                            email,
                            password,
                            { user ->
                                isLoading = false
                                successMessage = "Login successful"
                                onAuthSuccess(user)
                            },
                            { err ->
                                isLoading = false
                                errorMessage = err
                            }
                        )
                    },
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("auth_sign_in_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgDarkGreen)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                    } else {
                        Text("Sign In", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Demo Login Button for convenient testing
                OutlinedButton(
                    onClick = {
                        email = "farmer.ramesh@prithvi.ai"
                        password = "kisanpassword123"
                        isLoading = true
                        errorMessage = null
                        onSignIn(
                            email,
                            password,
                            { user ->
                                isLoading = false
                                successMessage = "Login successful"
                                onAuthSuccess(user)
                            },
                            { err ->
                                isLoading = false
                                errorMessage = err
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("auth_quick_demo_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("⚡ Quick Sign In as Verified Farmer", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

            } else {
                // REGISTER FORM
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        errorMessage = null
                    },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AgPrimaryGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_reg_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        errorMessage = null
                    },
                    label = { Text("Mobile Phone") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AgPrimaryGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_reg_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AgPrimaryGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_reg_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AgPrimaryGreen) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_reg_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AgPrimaryGreen) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible) "Hide Password" else "Show Password"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_reg_confirm_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null
                        onRegister(
                            email,
                            password,
                            fullName,
                            phoneNumber,
                            "English",
                            listOf("English"),
                            { user ->
                                isLoading = false
                                successMessage = "Registration successful"
                                onAuthSuccess(user)
                            },
                            { err ->
                                isLoading = false
                                errorMessage = err
                            }
                        )
                    },
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank() && fullName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("auth_register_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgDarkGreen)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                    } else {
                        Text("Create Account", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Terms & Privacy link
            Text(
                text = "By registering, you agree to our Terms of Service & Privacy Policy.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { showTermsDialog = true }
            )
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text(text = "Forgot Password", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "Enter your registered email address to receive password reset instructions:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = resetEmailInput,
                        onValueChange = { resetEmailInput = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    if (!resetDialogMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = resetDialogMessage ?: "",
                            color = AgDarkGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isResetLoading = true
                        onSendPasswordReset(
                            resetEmailInput,
                            {
                                isResetLoading = false
                                resetDialogMessage = "Password reset link sent"
                            },
                            { err ->
                                isResetLoading = false
                                resetDialogMessage = err
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgDarkGreen)
                ) {
                    if (isResetLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                    } else {
                        Text("Send Reset Link")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Terms of Service & Privacy Policy Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(text = "Terms of Service & Privacy", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "PRITHVI AI — Farmer Privacy Commitment",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "1. Data Ownership: Your crop, soil, and yield data belongs solely to you.\n" +
                               "2. Security: Farmer communications and community contributions are protected.\n" +
                               "3. Multilingual AI: Language models are optimized for Indian regional dialects without storing unapproved recordings.\n" +
                               "4. Community Guidelines: Be respectful and share genuine agricultural knowledge with fellow kisan brothers and sisters.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showTermsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AgDarkGreen)
                ) {
                    Text(t("close"))
                }
            }
        )
    }
}

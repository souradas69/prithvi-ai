package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.data.i18n.LocalAppLanguage
import com.example.data.i18n.PrithviI18n
import com.example.data.voice.IndianVoiceLanguage
import com.example.data.voice.IndianVoiceRecognitionManager
import com.example.ui.components.AccessibleFarmerVoiceDialog
import com.example.ui.components.AccountCentreDialog
import com.example.ui.components.BottomTab
import com.example.ui.components.PrithviBottomBar
import com.example.ui.components.PrithviSidebar
import com.example.ui.components.SidebarItem
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.CropAdvisoryScreen
import com.example.ui.screens.CropScannerScreen
import com.example.ui.screens.FarmFinanceScreen
import com.example.ui.screens.FarmingCalendarScreen
import com.example.ui.screens.GovernmentSchemesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MarketScreen
import com.example.ui.screens.MyFarmScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PrithviAiChatScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SmartIrrigationScreen
import com.example.ui.screens.SoilHealthScreen
import com.example.ui.screens.WeatherScreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.PrithviTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            PrithviTheme(darkTheme = isDarkMode) {
                val profile by viewModel.farmerProfile.collectAsState()
                val farms by viewModel.farms.collectAsState()
                val fields by viewModel.fields.collectAsState()
                val chatMessages by viewModel.chatMessages.collectAsState()
                val isAiThinking by viewModel.isAiThinking.collectAsState()
                val scans by viewModel.scans.collectAsState()
                val expenses by viewModel.expenses.collectAsState()
                val harvests by viewModel.harvests.collectAsState()
                val soilRecords by viewModel.soilRecords.collectAsState()
                val posts by viewModel.communityPosts.collectAsState()
                val selectedLanguage = "English"
                val isSimpleMode by viewModel.isSimpleMode.collectAsState()
                val directMessages by viewModel.directMessages.collectAsState()

                val layoutDirection = androidx.compose.ui.unit.LayoutDirection.Ltr

                CompositionLocalProvider(
                    LocalAppLanguage provides selectedLanguage,
                    LocalLayoutDirection provides layoutDirection
                ) {
                    val isOnboardingCompleted = profile?.onboardingCompleted == true

                    if (!isOnboardingCompleted) {
                        OnboardingScreen(
                            onOnboardingComplete = { name, phone, lang, state, dist, village, farmName, area, unit, soil, irr, crop, variety, stage, goals ->
                                viewModel.saveFarmerProfile(name, phone, lang, state, dist, village, goals)
                                viewModel.addFarm(farmName, "$village, $dist", area, unit, soil, irr, "Borewell", "Conventional", "Kharif")
                                viewModel.addField(1L, "$crop Field", area, crop, variety, stage, soil, irr, "Borewell", "Primary active field")
                            }
                        )
                    } else {
                        MainAppContent(
                            viewModel = viewModel,
                            profile = profile,
                            farms = farms,
                            fields = fields,
                            chatMessages = chatMessages,
                            isAiThinking = isAiThinking,
                            scans = scans,
                            expenses = expenses,
                            harvests = harvests,
                            soilRecords = soilRecords,
                            posts = posts,
                            selectedLanguage = selectedLanguage,
                            isSimpleMode = isSimpleMode,
                            directMessages = directMessages,
                            isDarkMode = isDarkMode
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: MainViewModel,
    profile: com.example.data.entity.FarmerProfileEntity?,
    farms: List<com.example.data.entity.FarmEntity>,
    fields: List<com.example.data.entity.FieldEntity>,
    chatMessages: List<com.example.ui.viewmodel.ChatMessage>,
    isAiThinking: Boolean,
    scans: List<com.example.data.entity.CropScanEntity>,
    expenses: List<com.example.data.entity.ExpenseEntity>,
    harvests: List<com.example.data.entity.HarvestEntity>,
    soilRecords: List<com.example.data.entity.SoilHealthEntity>,
    posts: List<com.example.data.entity.CommunityPostEntity>,
    selectedLanguage: String,
    isSimpleMode: Boolean,
    directMessages: Map<String, List<com.example.ui.viewmodel.DirectMessage>>,
    isDarkMode: Boolean
) {
    val configuration = LocalConfiguration.current
    val isExpandedWidth = configuration.screenWidthDp >= 600

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val currentBottomTab = remember(currentRoute) {
        BottomTab.values().firstOrNull { it.route == currentRoute } ?: BottomTab.HOME
    }
    val currentSidebarItem = remember(currentRoute) {
        SidebarItem.values().firstOrNull { it.id == currentRoute } ?: SidebarItem.HOME
    }

    var showAccountCentreDialog by remember { mutableStateOf(false) }

    val farmerName = profile?.name ?: "Farmer"
    val state = profile?.state ?: "West Bengal"
    val district = profile?.district ?: "Hooghly"
    val village = profile?.village ?: "Singur"

    val activeField = fields.firstOrNull()
    val cropName = activeField?.cropName ?: "Tomato"

    val weatherData by viewModel.weatherData.collectAsState()
    val isWeatherLoading by viewModel.isWeatherLoading.collectAsState()
    val authUser by viewModel.authUser.collectAsState()
    val isFirebaseConfigured by viewModel.isFirebaseCloudConfigured.collectAsState()

    val context = LocalContext.current

    fun navigateTo(route: String) {
        if (route == "account_centre") {
            showAccountCentreDialog = true
            return
        }
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateToTab(tabRoute: String) {
        if (tabRoute == "account_centre") {
            showAccountCentreDialog = true
            return
        }
        navController.navigate(tabRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun handleBackNavigation() {
        if (!navController.popBackStack()) {
            navController.navigate("home") {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    // Intercept back button when not on the root Home screen to prevent premature app exit
    BackHandler(enabled = currentRoute != "home") {
        handleBackNavigation()
    }

    if (showAccountCentreDialog) {
        AccountCentreDialog(
            profile = profile,
            activeField = activeField,
            onDismiss = { showAccountCentreDialog = false },
            onSaveAccountInfo = { name, phone, lang, st, dist, vil, crop, variety, stage, area, soil, irr ->
                viewModel.updateAccountAndFieldDetails(
                    name = name,
                    phone = phone,
                    language = lang,
                    state = st,
                    district = dist,
                    village = vil,
                    cropName = crop,
                    cropVariety = variety,
                    growthStage = stage,
                    area = area,
                    soilType = soil,
                    irrigationMethod = irr
                )
            },
            onSwitchAccount = {
                showAccountCentreDialog = false
                navigateTo("auth")
            },
            onLanguageChanged = { lang ->
                viewModel.setSelectedLanguage(lang)
            },
            authUser = authUser,
            isFirebaseConfigured = isFirebaseConfigured,
            onSignOut = {
                viewModel.signOut()
            }
        )
    }





    @Composable
    fun AppNavHost(modifier: Modifier = Modifier) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier
        ) {
            composable("home") {
                HomeScreen(
                    profile = profile,
                    fields = fields,
                    weatherData = weatherData,
                    isWeatherLoading = isWeatherLoading,
                    onRefreshWeather = { viewModel.refreshWeatherData() },
                    onNavigateToScan = { navigateTo("scan_crop") },
                    onNavigateToIrrigation = { navigateTo("irrigation") },
                    onNavigateToMarket = { navigateTo("market") },
                    onNavigateToSchemes = { navigateTo("govt_schemes") },
                    onNavigateToChat = { navigateTo("prithvi_ai") },
                    onNavigateToAdvisory = { navigateTo("crop_advisory") },
                    onNavigateToWeather = { navigateTo("weather") },
                    onNavigateToCommunity = { navigateTo("community") },
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                    onOpenAccountCentre = { navigateTo("account_centre") },
                    onOpenVoiceQuery = { navigateTo("prithvi_ai") }
                )
            }

            composable("my_farm") {
                MyFarmScreen(
                    farms = farms,
                    fields = fields,
                    onAddField = { farmId, name, area, crop, variety, stage ->
                        viewModel.addField(farmId, name, area, crop, variety, stage, "Alluvial", "Drip", "Borewell", "")
                    },
                    onNavigateToAdvisory = { navigateTo("crop_advisory") },
                    onNavigateToIrrigation = { navigateTo("irrigation") },
                    onNavigateToSoilHealth = { navigateTo("soil_health") },
                    onNavigateToFinance = { navigateTo("farm_finance") },
                    onNavigateToCalendar = { navigateTo("calendar") }
                )
            }

            composable("crop_advisory") {
                CropAdvisoryScreen(fields = fields)
            }

            composable("scan_crop") {
                CropScannerScreen(
                    scansHistory = scans,
                    isAnalyzing = isAiThinking,
                    onAnalyzeImage = { bitmap -> viewModel.analyzeCropScanImage(bitmap) },
                    onShareToCommunity = { scan -> viewModel.shareScanToCommunity(scan) },
                    onNavigateToChat = { navigateTo("prithvi_ai") },
                    isDarkMode = isDarkMode
                )
            }

            composable("weather") {
                WeatherScreen(
                    weather = weatherData,
                    cropName = cropName,
                    isWeatherLoading = isWeatherLoading,
                    onRefreshWeather = { viewModel.refreshWeatherData() },
                    isDarkMode = isDarkMode
                )
            }

            composable("irrigation") {
                SmartIrrigationScreen(
                    field = fields.firstOrNull(),
                    onMarkWatered = { fieldId -> viewModel.markFieldWatered(fieldId) }
                )
            }

            composable("market") {
                MarketScreen(
                    currentCrop = cropName,
                    state = state,
                    district = district
                )
            }

            composable("govt_schemes") {
                GovernmentSchemesScreen(state = state)
            }

            composable("soil_health") {
                SoilHealthScreen(
                    records = soilRecords,
                    onAddRecord = { ph, n, p, k, oc, date, notes ->
                        viewModel.addSoilHealth(1L, ph, n, p, k, oc, date, notes)
                    }
                )
            }

            composable("farm_finance") {
                FarmFinanceScreen(
                    expenses = expenses,
                    harvests = harvests,
                    onAddExpense = { cat, amt, note -> viewModel.addExpense(1L, cat, amt, note) },
                    onAddHarvest = { crop, qty, unit, price, mandi -> viewModel.addHarvest(1L, crop, qty, unit, price, mandi) },
                    onDeleteExpense = { id -> viewModel.deleteExpense(id) },
                    onDeleteHarvest = { id -> viewModel.deleteHarvest(id) }
                )
            }

            composable("calendar") {
                FarmingCalendarScreen(cropName = cropName)
            }

            composable("community") {
                CommunityScreen(
                    posts = posts,
                    onAddPost = { content, tag, imageUri -> viewModel.addCommunityPost(content, tag, imageUri) },
                    onToggleLike = { post -> viewModel.toggleLikePost(post) },
                    directMessagesMap = directMessages,
                    onSendDirectMessage = { recipient, msg -> viewModel.sendDirectMessage(recipient, msg) },
                    isDarkMode = isDarkMode,
                    currentUser = authUser,
                    onRequireAuth = { navigateTo("auth") }
                )
            }

            composable("auth") {
                AuthScreen(
                    currentLanguage = selectedLanguage,
                    isFirebaseCloudConfigured = isFirebaseConfigured,
                    onSignIn = { email, pass, onSuccess, onError ->
                        viewModel.signIn(email, pass, onSuccess, onError)
                    },
                    onRegister = { email, pass, name, phone, lang, prefLangs, onSuccess, onError ->
                        viewModel.register(email, pass, name, phone, lang, prefLangs, onSuccess, onError)
                    },
                    onSendPasswordReset = { email, onSuccess, onError ->
                        viewModel.sendPasswordReset(email, onSuccess, onError)
                    },
                    onAuthSuccess = {
                        handleBackNavigation()
                    },
                    onDismissOrBack = {
                        handleBackNavigation()
                    },
                    isDarkMode = isDarkMode
                )
            }

            composable("prithvi_ai") {
                PrithviAiChatScreen(
                    messages = chatMessages,
                    isAiThinking = isAiThinking,
                    onSendMessage = { msg -> viewModel.sendMessageToPrithvi(msg) },
                    onSendMessageWithImage = { msg, bitmap -> viewModel.sendMessageToPrithvi(msg, bitmap) },
                    onTellMeMore = { previousMsg -> viewModel.requestTellMeMore(previousMsg) },
                    onClearChat = { viewModel.clearChat() },
                    onBack = { handleBackNavigation() },
                    farmerName = profile?.name ?: "Farmer",
                    cropName = cropName,
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { lang -> viewModel.setSelectedLanguage(lang) },
                    isSimpleMode = isSimpleMode
                )
            }

            composable("settings") {
                SettingsScreen(
                    profile = profile,
                    onSaveProfile = { name, phone, lang, st, dist, vil ->
                        viewModel.saveFarmerProfile(name, phone, lang, st, dist, vil, profile?.goals ?: "")
                    },
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { enabled -> viewModel.setDarkMode(enabled) },
                    onOpenAccountCentre = { showAccountCentreDialog = true },
                    onSwitchAccount = { navigateTo("auth") }
                )
            }
        }
    }

    if (isExpandedWidth) {
        // Tablet / Large Screen Layout with Navigation Sidebar
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PrithviSidebar(
                selectedItem = currentSidebarItem,
                onItemSelected = { item ->
                    if (item.id == "account_centre") {
                        showAccountCentreDialog = true
                    } else {
                        navigateToTab(item.id)
                    }
                },
                farmerName = farmerName,
                farmerLocation = "$village, $district",
                isDarkMode = isDarkMode,
                onToggleDarkMode = { viewModel.toggleDarkMode() }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                AppNavHost()
            }
        }
    } else {
        // Mobile Layout with Bottom Navigation Bar
        val isKeyboardOpen = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp
        val isMainTab = currentRoute in BottomTab.values().map { it.route }
        val showBottomBar = isMainTab && !isKeyboardOpen

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showBottomBar) {
                    PrithviBottomBar(
                        currentTab = currentBottomTab,
                        onTabSelected = { tab -> navigateToTab(tab.route) }
                    )
                }
            }
        ) { innerPadding ->
            val bottomPadding = if (showBottomBar) {
                innerPadding.calculateBottomPadding()
            } else if (!isKeyboardOpen) {
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            } else {
                0.dp
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                        bottom = bottomPadding
                    )
            ) {
                AppNavHost()
            }
        }
    }
}

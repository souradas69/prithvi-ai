package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.auth.AuthUser
import com.example.data.entity.CommunityPostEntity
import com.example.ui.components.FarmingBackground
import com.example.ui.theme.AgContainerGreen
import com.example.ui.theme.AgDarkGreen
import com.example.ui.theme.AgLeafGreen
import com.example.ui.theme.AgPrimaryGreen
import com.example.ui.theme.AgSproutGreen
import com.example.ui.viewmodel.DirectMessage

data class CommentItem(
    val author: String,
    val text: String,
    val time: String = "Just now"
)

@Composable
fun CommunityScreen(
    posts: List<CommunityPostEntity>,
    onAddPost: (content: String, cropTag: String, imageUri: String) -> Unit,
    onToggleLike: (CommunityPostEntity) -> Unit,
    directMessagesMap: Map<String, List<DirectMessage>> = emptyMap(),
    onSendDirectMessage: ((recipient: String, messageText: String) -> Unit)? = null,
    isDarkMode: Boolean = false,
    currentUser: AuthUser? = null,
    onRequireAuth: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showNewPostDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var activeDirectMsgUser by remember { mutableStateOf<String?>(null) }
    var activeCommentPost by remember { mutableStateOf<CommunityPostEntity?>(null) }

    val commentsMap = remember {
        mutableStateOf(
            mutableMapOf<Long, MutableList<CommentItem>>(
                1L to mutableListOf(
                    CommentItem("Sita Devi", "Ji, Jeevamrut spray worked great on my tomato crop too!"),
                    CommentItem("Anil Biswas", "What was the dilution ratio used per liter of water?")
                ),
                2L to mutableListOf(
                    CommentItem("Mukesh Verma", "Drip irrigation saved almost 50% water in Singur block last month.")
                )
            )
        )
    }

    val categories = listOf("All", "Tomato", "Rice", "Wheat", "Potato", "Disease", "Market", "Schemes")

    val filteredPosts = posts.filter { post ->
        val matchesCategory = if (selectedCategory == "All") true else post.cropTag.contains(selectedCategory, ignoreCase = true) || post.content.contains(selectedCategory, ignoreCase = true)
        val matchesSearch = if (searchQuery.isBlank()) true else post.content.contains(searchQuery, ignoreCase = true) || post.authorName.contains(searchQuery, ignoreCase = true) || post.cropTag.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    FarmingBackground(isDarkMode = isDarkMode) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Farmer Community",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = AgPrimaryGreen.copy(alpha = 0.2f),
                                border = BorderStroke(0.8.dp, AgPrimaryGreen)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(11.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "Verified", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                                }
                            }
                        }
                        Text(
                            text = "Connect with 50,000+ Indian progressive farmers",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = {
                            if (currentUser == null) {
                                onRequireAuth()
                            } else {
                                showNewPostDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Post", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Authentication Status Banner
                if (currentUser == null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        border = BorderStroke(1.dp, Color(0xFFFFE082))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🔒 Community Login Required",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB78103)
                                )
                                Text(
                                    text = "Sign in or register to publish farm advisories, comment, or like posts.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onRequireAuth,
                                colors = ButtonDefaults.buttonColors(containerColor = AgDarkGreen),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Sign In", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9),
                        border = BorderStroke(0.8.dp, AgPrimaryGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AgPrimaryGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Authenticated as ${currentUser.fullName} (${currentUser.email.ifBlank { "+91 " + currentUser.phoneNumber }})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AgDarkGreen
                            )
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search posts, crops, diseases...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = AgPrimaryGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        AssistChip(
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) AgPrimaryGreen else MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                // Community Posts List
                if (filteredPosts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No community posts found for '$searchQuery'. Be the first to share!", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    }
                } else {
                    filteredPosts.forEach { post ->
                        val currentComments = commentsMap.value[post.id] ?: emptyList()
                        CommunityPostCard(
                            post = post,
                            commentsCount = currentComments.size + post.commentsCount,
                            onToggleLike = onToggleLike,
                            onOpenComments = { activeCommentPost = post },
                            onOpenDirectMsg = { activeDirectMsgUser = post.authorName }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        // New Post Dialog with Image Attachment
        if (showNewPostDialog) {
            var content by remember { mutableStateOf("") }
            var cropTag by remember { mutableStateOf("Tomato") }
            var selectedImagePreset by remember { mutableStateOf("🌾 Paddy Harvest") }
            var customImageUri by remember { mutableStateOf("") }

            val imagePresets = listOf(
                "🌾 Paddy Harvest",
                "🍃 Tomato Leaf Blight",
                "🥔 Potato Field",
                "💧 Drip Irrigation Setup",
                "🌱 Soil Test Card"
            )

            AlertDialog(
                onDismissRequest = { showNewPostDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Agriculture, contentDescription = null, tint = AgPrimaryGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Farmer Post", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        OutlinedTextField(
                            value = cropTag,
                            onValueChange = { cropTag = it },
                            label = { Text("Crop Tag (e.g. Tomato, Paddy, Wheat)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            label = { Text("Share advice, question, or farm update...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            maxLines = 5
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "📸 Attach Photo / Crop Scan:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgPrimaryGreen
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            items(imagePresets) { preset ->
                                val isSelected = preset == selectedImagePreset
                                Surface(
                                    onClick = {
                                        selectedImagePreset = preset
                                        customImageUri = preset
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) AgPrimaryGreen else MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, AgPrimaryGreen)
                                ) {
                                    Text(
                                        text = preset,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = customImageUri,
                            onValueChange = { customImageUri = it },
                            label = { Text("Photo Description / Image Note") },
                            placeholder = { Text("e.g. Photo of yellow leaf spots") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (content.isNotBlank()) {
                                onAddPost(content, cropTag, customImageUri.ifBlank { selectedImagePreset })
                                showNewPostDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgPrimaryGreen)
                    ) {
                        Text("Share Post", color = Color.White)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showNewPostDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Comments Section Dialog
        if (activeCommentPost != null) {
            val targetPost = activeCommentPost!!
            val postComments = commentsMap.value.getOrPut(targetPost.id) { mutableListOf() }
            var newCommentInput by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { activeCommentPost = null },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Comments (${postComments.size + targetPost.commentsCount})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        IconButton(onClick = { activeCommentPost = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                        ) {
                            items(postComments) { comment ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = comment.author, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                                            Text(text = comment.time, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = comment.text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = newCommentInput,
                            onValueChange = { newCommentInput = it },
                            placeholder = { Text("Write a comment for farmers...") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (currentUser == null) {
                                            activeCommentPost = null
                                            onRequireAuth()
                                        } else if (newCommentInput.isNotBlank()) {
                                            postComments.add(CommentItem(author = currentUser.fullName, text = newCommentInput))
                                            newCommentInput = ""
                                        }
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = "Post Comment", tint = AgPrimaryGreen)
                                }
                            }
                        )
                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = { activeCommentPost = null }) {
                        Text("Done")
                    }
                }
            )
        }

        // Direct Messaging Dialog
        if (activeDirectMsgUser != null) {
            val recipient = activeDirectMsgUser!!
            val messageThread = directMessagesMap[recipient] ?: emptyList()
            var messageInput by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { activeDirectMsgUser = null },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Message, contentDescription = null, tint = AgPrimaryGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chat with $recipient", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                        ) {
                            items(messageThread) { msg ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (msg.isUser) AgPrimaryGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = if (msg.isUser) "You" else msg.senderName,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AgPrimaryGreen
                                        )
                                        Text(text = msg.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = messageInput,
                            onValueChange = { messageInput = it },
                            placeholder = { Text("Write private message...") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (messageInput.isNotBlank()) {
                                            onSendDirectMessage?.invoke(recipient, messageInput)
                                            messageInput = ""
                                        }
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = AgPrimaryGreen)
                                }
                            }
                        )
                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = { activeDirectMsgUser = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun CommunityPostCard(
    post: CommunityPostEntity,
    commentsCount: Int,
    onToggleLike: (CommunityPostEntity) -> Unit,
    onOpenComments: () -> Unit,
    onOpenDirectMsg: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, AgPrimaryGreen.copy(alpha = 0.25f)),
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Post Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AgPrimaryGreen, AgLeafGreen)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (post.isUserPost) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = AgPrimaryGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "YOU",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AgPrimaryGreen,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(11.dp))
                            Text(text = post.location, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 2.dp))
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AgPrimaryGreen.copy(alpha = 0.15f),
                    border = BorderStroke(0.8.dp, AgPrimaryGreen.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "#${post.cropTag}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgPrimaryGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Content Text
            Text(
                text = post.content,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )

            // Attached Photo Card (if imageUri is present)
            if (post.imageUri.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(0.8.dp, AgPrimaryGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AgPrimaryGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null, tint = AgPrimaryGreen, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "📸 Attached Farm Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                            Text(text = post.imageUri, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Footer (Like, Comments, Message)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onToggleLike(post) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${post.likesCount}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onOpenComments() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = "Comments", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$commentsCount", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                if (!post.isUserPost) {
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        onClick = onOpenDirectMsg,
                        shape = CircleShape,
                        color = AgPrimaryGreen.copy(alpha = 0.15f),
                        border = BorderStroke(0.8.dp, AgPrimaryGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Message, contentDescription = "Message", tint = AgPrimaryGreen, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AgPrimaryGreen)
                        }
                    }
                }
            }
        }
    }
}

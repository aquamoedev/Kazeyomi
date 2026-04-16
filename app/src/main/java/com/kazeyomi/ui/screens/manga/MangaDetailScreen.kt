package com.kazeyomi.ui.screens.manga

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.domain.model.DownloadState
import com.kazeyomi.domain.model.Manga
import com.kazeyomi.domain.model.MangaStatus
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    mangaId: Int,
    onBackClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    viewModel: MangaDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(mangaId) {
        viewModel.loadManga(mangaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.manga?.title ?: "Manga") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            if (uiState.manga?.inLibrary == true) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                    IconButton(onClick = { viewModel.fetchChapters() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        bottomBar = {
            if (uiState.manga != null && uiState.chapters.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val unreadChapter = uiState.chapters.firstOrNull { !it.read }
                        val lastReadChapter = uiState.chapters.firstOrNull { it.lastPageRead > 0 }
                        
                        Button(
                            onClick = { 
                                val chapter = unreadChapter ?: lastReadChapter ?: uiState.chapters.last()
                                onChapterClick(chapter.id)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (unreadChapter != null) "Continue" else "Start Reading")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(paddingValues))
            uiState.error != null -> ErrorMessage(
                message = uiState.error!!,
                onRetry = { viewModel.loadManga(mangaId) },
                modifier = Modifier.padding(paddingValues)
            )
            uiState.manga != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        MangaHeader(
                            manga = uiState.manga!!,
                            onAddToLibrary = { viewModel.toggleFavorite() }
                        )
                    }

                    item {
                        TabRow(selectedTabIndex = selectedTab) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("Chapters (${uiState.chapters.size})") }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("Info") }
                            )
                        }
                    }

                    if (selectedTab == 0) {
                        item {
                            ChapterSortBar(
                                onSortChange = { viewModel.setChapterSort(it) }
                            )
                        }

                        items(uiState.chapters, key = { it.id }) { chapter ->
                            ChapterItem(
                                chapter = chapter,
                                onClick = { onChapterClick(chapter.id) },
                                onDownload = { viewModel.downloadChapter(chapter.id) },
                                onToggleRead = { viewModel.toggleChapterRead(chapter) }
                            )
                        }
                    } else {
                        item {
                            MangaInfo(manga = uiState.manga!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MangaHeader(
    manga: Manga,
    onAddToLibrary: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(120.dp)
        ) {
            SubcomposeAsyncImage(
                model = manga.thumbnailUrl,
                contentDescription = manga.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = manga.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            manga.author?.let {
                Text(
                    text = "by $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            AssistChip(
                onClick = {},
                label = { Text(manga.status.name.replace("_", " ")) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!manga.inLibrary) {
                    FilledTonalButton(onClick = onAddToLibrary) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add to Library")
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterSortBar(
    onSortChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf("source") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(currentSort.replaceFirstChar { it.uppercase() })
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("source", "number").forEach { sort ->
                    DropdownMenuItem(
                        text = { Text(sort.replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            currentSort = sort
                            onSortChange(sort)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChapterItem(
    chapter: Chapter,
    onClick: () -> Unit,
    onDownload: () -> Unit,
    onToggleRead: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = chapter.title,
                style = if (chapter.read) MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ) else MaterialTheme.typography.bodyMedium
            )
        },
        supportingContent = {
            Text(
                text = buildString {
                    if (chapter.scanlator != null) append("${chapter.scanlator} • ")
                    append("Page ${chapter.lastPageRead}/${chapter.pageCount}")
                },
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingContent = {
            Icon(
                imageVector = when {
                    chapter.read -> Icons.Default.CheckCircle
                    chapter.bookmarked -> Icons.Default.Bookmark
                    else -> Icons.Default.Circle
                },
                contentDescription = null,
                tint = when {
                    chapter.read -> MaterialTheme.colorScheme.primary
                    chapter.bookmarked -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.outline
                }
            )
        },
        trailingContent = {
            Row {
                when (chapter.downloadState) {
                    DownloadState.NOT_DOWNLOADED -> {
                        IconButton(onClick = onDownload) {
                            Icon(Icons.Default.Download, contentDescription = "Download")
                        }
                    }
                    DownloadState.QUEUED, DownloadState.DOWNLOADING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    DownloadState.DOWNLOADED -> {
                        Icon(
                            Icons.Default.DownloadDone,
                            contentDescription = "Downloaded",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    DownloadState.ERROR -> {
                        IconButton(onClick = onDownload) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
    Divider()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MangaInfo(manga: Manga) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!manga.description.isNullOrBlank()) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = manga.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        manga.genre?.let { genres ->
            if (genres.isNotEmpty()) {
                Text(
                    text = "Genres",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genres.forEach { genre ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(genre) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoItem("Author", manga.author ?: "Unknown")
            InfoItem("Artist", manga.artist ?: manga.author ?: "Unknown")
        }
        Spacer(modifier = Modifier.height(8.dp))
        InfoItem("Status", manga.status.name.replace("_", " "))
        InfoItem("Chapters", manga.chapterCount.toString())
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

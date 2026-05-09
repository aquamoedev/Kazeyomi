package com.kazeyomi.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChapterActionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    isRead: Boolean,
    isBookmarked: Boolean,
    onMarkRead: () -> Unit,
    onMarkUnread: () -> Unit,
    onBookmark: () -> Unit,
    onDownload: () -> Unit,
    onDeleteDownload: () -> Unit = {}
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        if (isRead) {
            DropdownMenuItem(
                text = { Text("Mark as Unread") },
                onClick = { onMarkUnread(); onDismiss() },
                leadingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null) }
            )
        } else {
            DropdownMenuItem(
                text = { Text("Mark as Read") },
                onClick = { onMarkRead(); onDismiss() },
                leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = null) }
            )
        }
        DropdownMenuItem(
            text = { Text(if (isBookmarked) "Remove Bookmark" else "Bookmark") },
            onClick = { onBookmark(); onDismiss() },
            leadingIcon = { Icon(if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, contentDescription = null) }
        )
        DropdownMenuItem(
            text = { Text("Download") },
            onClick = { onDownload(); onDismiss() },
            leadingIcon = { Icon(Icons.Default.Download, contentDescription = null) }
        )
    }
}

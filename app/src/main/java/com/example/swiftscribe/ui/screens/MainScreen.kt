package com.example.swiftscribe.ui.screens

import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftscribe.R
import com.example.swiftscribe.domain.Note
import com.example.swiftscribe.ui.NoteViewModel

@Composable
fun MainScreen(
    viewModel: NoteViewModel = viewModel(factory = NoteViewModel.Factory)
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDialog by remember { mutableStateOf(false) }
    var editNote by remember { mutableStateOf(false) }
    var noteId by remember { mutableIntStateOf(0) }

    var expand by remember { mutableStateOf(false) }

    val noteList by viewModel.getAllNotes().collectAsState(initial = emptyList())
    
    var lastTitle by remember { mutableStateOf("") }
    var lastDescription by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredNotes = noteList.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            Icon(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .size(45.dp)
                    .clickable { isDialog = true },
                painter = painterResource(id = R.drawable.edit_24px),
                contentDescription = "Add Note",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Swift Scribe",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(8.dp).background(MaterialTheme.colorScheme.background))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search_24px),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentDescription = "Search Notes",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        maxLines = 1,
                        placeholder = {Text(text = "Search Note")},
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        )
                    )
                }
            }
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(count = 2)) {
                items(filteredNotes) { noteItem ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                editNote = true
                                noteId = noteItem.id
                                lastTitle = noteItem.title
                                lastDescription = noteItem.description
                            },
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(4.dp)
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = noteItem.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Icon(
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                        .clickable {
                                            expand = !expand
                                            noteId = noteItem.id
                                        },
                                    painter = if (expand && noteId == noteItem.id) painterResource(
                                        id = R.drawable.expand_circle_up_24px
                                    ) else painterResource(
                                        id = R.drawable.expand_circle_down_24px
                                    ),
                                    contentDescription = "Expand",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                text = noteItem.description,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 4,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            if(expand && noteId == noteItem.id) {
                                Row(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .fillMaxWidth()
                                        .padding(
                                            start = dimensionResource(R.dimen.padding_medium),
                                            top = dimensionResource(R.dimen.padding_small),
                                            bottom = dimensionResource(R.dimen.padding_medium),
                                            end = dimensionResource(R.dimen.padding_medium)
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer).clickable { viewModel.deleteNote(noteItem) },
                                        painter = painterResource(id = R.drawable.cleaning_bucket_24px),
                                        contentDescription = "Delete Note",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Icon(
                                        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer).clickable {
                                            val shareText = "${noteItem.title}\n${noteItem.description}"
                                            shareNote(context = context, text = shareText)
                                        },
                                        painter = painterResource(id = R.drawable.share_24px),
                                        contentDescription = "Share Note",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if(isDialog) {
        AlertDialog(
            modifier = Modifier
                .height(500.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            onDismissRequest = { isDialog = !isDialog },
            confirmButton = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp)
                                .clickable {
                                    if (title.isNotBlank() && description.isNotBlank()) {
                                        viewModel.insertNote(
                                            title = title,
                                            description = description
                                        )
                                        title = ""
                                        description = ""
                                    }
                                    isDialog = false
                                },
                            painter = painterResource(id = R.drawable.keyboard_double_arrow_left_24px),
                            contentDescription = "Save Note",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier
                        .height(8.dp))
                    CustomTextField(editText = title, onValueChange = { title=it }, placeholder = "Title", flag = true)

                    Spacer(modifier = Modifier
                        .height(4.dp))
                    CustomTextField(editText = description, onValueChange = { description = it }, placeholder = "Description", flag = false)
                }
            }
        )
    }

    if(editNote) {
        AlertDialog(
            modifier = Modifier
                .height(500.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            onDismissRequest = { editNote = !editNote },
            confirmButton = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp)
                                .clickable {
                                    val updatedNote = Note(
                                        id = noteId,
                                        title = lastTitle,
                                        description = lastDescription
                                    )
                                    viewModel.updateNote(updatedNote)
                                    editNote = false
                                },
                            painter = painterResource(id = R.drawable.keyboard_double_arrow_left_24px),
                            contentDescription = "Save Note",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(editText = lastTitle, onValueChange = { lastTitle=it }, placeholder = "Title", flag = true)

                    Spacer(modifier = Modifier.height(4.dp))
                    CustomTextField(editText = lastDescription, onValueChange = { lastDescription = it }, placeholder = "Description", flag = false)
                }
            }
        )
    }
}

fun shareNote(context: Context, text: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    context.startActivity(createChooser(intent, "Share via"))
}
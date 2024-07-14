package com.example.swiftscribe.ui.screens

import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var themeColor by remember { mutableStateOf(Color.Black) }
    var isThemeChange by remember { mutableStateOf(false) }

    val noteList by viewModel.getAllNotes().collectAsState(initial = emptyList())
    
    var lastTitle by remember { mutableStateOf("") }
    var lastDescription by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredNotes = noteList.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }


    Scaffold(
        modifier = Modifier.background(themeColor),
        floatingActionButton = {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .background(themeColor)
                    .clickable { isDialog = true },
                painter = painterResource(id = R.drawable.add_note),
                contentDescription = "Add Note"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(themeColor),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(themeColor),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Swift Scribe",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    color = if(!isThemeChange) Color.White else Color.Black
                )
                IconButton(
                    onClick = {
                        isThemeChange = !isThemeChange
                    }
                ) {
                    themeColor = if (isThemeChange) {
                        Image(painter = painterResource(id = R.drawable.night), contentDescription = "Dark")
                        Color.White
                    } else {
                        Image(painter = painterResource(id = R.drawable.sun), contentDescription = "Light")
                        Color.Black
                    }
                }
            }
            Spacer(modifier = Modifier
                .height(8.dp)
                .background(themeColor))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(8.dp),
                color = themeColor,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if(!isThemeChange) Color.White else Color.Black)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(35.dp)
                            .padding(start = 10.dp)
                            .background(themeColor),
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search Notes"
                    )
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = if(!isThemeChange) Color.White else Color.Black,
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
                        color = themeColor,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, if(!isThemeChange) Color.White else Color.Black)
                    ) {
                        Column(modifier = Modifier
                            .padding(4.dp)
                            .background(themeColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .background(themeColor),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = noteItem.title,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    color = if(isThemeChange) Color.Black else Color.White
                                )
                                IconButton(
                                    modifier = Modifier.background(themeColor),
                                    onClick = { viewModel.deleteNote(noteItem) }
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(25.dp)
                                            .background(themeColor),
                                        painter = painterResource(id = R.drawable.delete_notes),
                                        contentDescription = "Delete Note"
                                    )
                                }
                                IconButton(
                                    modifier = Modifier.background(themeColor),
                                    onClick = {
                                        val shareText = "${noteItem.title}\n${noteItem.description}"
                                        shareNote(context = context, text = shareText)
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(25.dp)
                                            .background(themeColor),
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share"
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                text = noteItem.description,
                                maxLines = 8,
                                color = if(isThemeChange) Color.Black else Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    if(isDialog) {
        AlertDialog(
            modifier = Modifier.height(500.dp),
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
                        Image(
                            modifier = Modifier
                                .size(30.dp)
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
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save Note"
                        )
                        Image(
                            modifier = Modifier
                                .size(30.dp)
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
                            painter = painterResource(id = R.drawable.mark),
                            contentDescription = "Save Note"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(editText = title, onValueChange = { title=it }, placeholder = "Title", flag = true)

                    Spacer(modifier = Modifier.height(4.dp))
                    CustomTextField(editText = description, onValueChange = { description = it }, placeholder = "Description", flag = false)
                }
            }
        )
    }

    if(editNote) {
        AlertDialog(
            modifier = Modifier.height(500.dp),
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
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    val updatedNote = Note(
                                        id = noteId,
                                        title = lastTitle,
                                        description = lastDescription
                                    )
                                    viewModel.updateNote(updatedNote)
                                    editNote = false
                                },
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save Note"
                        )
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    val updatedNote = Note(
                                        id = noteId,
                                        title = lastTitle,
                                        description = lastDescription
                                    )
                                    viewModel.updateNote(updatedNote)
                                    editNote = false
                                },
                            painter = painterResource(id = R.drawable.mark),
                            contentDescription = "Save Note"
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
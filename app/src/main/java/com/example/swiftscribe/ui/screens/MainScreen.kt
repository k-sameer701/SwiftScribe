package com.example.swiftscribe.ui.screens

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDialog by remember { mutableStateOf(false) }
    var editNote by remember { mutableStateOf(false) }
    var noteId by remember { mutableIntStateOf(0) }
    var themeColor by remember { mutableStateOf(Color.Black) }
    var isThemeChange by remember { mutableStateOf(false) }

    val noteList by viewModel.getAllNotes().collectAsState(initial = emptyList())

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
                .background(themeColor) )
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(count = 2)) {
                items(noteList) { noteItem ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                editNote = true
                                noteId = noteItem.id
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
                            }
                            Text(
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
                                        title = title,
                                        description = description
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
                                        title = title,
                                        description = description
                                    )
                                    viewModel.updateNote(updatedNote)
                                    editNote = false
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
}
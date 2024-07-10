package com.example.swiftscribe.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    var noteId by remember { mutableStateOf(0) }
    val noteList by viewModel.getAllNotesOrderByTitle().collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            IconButton(
                modifier = Modifier.size(70.dp),
                onClick = {
                    isDialog = true
                }
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.add_note),
                    contentDescription = "Add Note"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Swift Scribe",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.Black
                )
                // SORT

            }
            Spacer(modifier = Modifier.height(8.dp))

            // LAZY COLUMN
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(count = 2)) {
                items(noteList) { noteItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                editNote = true
                                noteId = noteItem.id
                            },
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(text = noteItem.title)
                            Text(
                                text = noteItem.description,
                                maxLines = 5
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.deleteNote(noteItem) }
                                ) {
                                    Image(
                                        modifier = Modifier.size(25.dp),
                                        painter = painterResource(id = R.drawable.delete_note),
                                        contentDescription = "Delete Note"
                                    )
                                    //Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
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
            modifier = Modifier.height(500.dp),
            onDismissRequest = { isDialog = !isDialog },
            confirmButton = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    if(title.isNotBlank() && description.isNotBlank()) {
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
                                    if(title.isNotBlank() && description.isNotBlank()) {
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
                    CustomTextField(editText = title, onValueChange = { title=it }, placeholder = "Title", flag = false)

                    Spacer(modifier = Modifier.height(4.dp))
                    CustomTextField(editText = description, onValueChange = { description = it }, placeholder = "Description", flag = true)

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
                    Button(
                        onClick = {
                            //if(title.isNotBlank() && description.isNotBlank()) {
                                val updatedNote = Note(
                                    id = noteId,
                                    title = title,
                                    description = description
                                )
                                viewModel.updateNote(updatedNote)

                            editNote = false
                        }
                    ) {
                        Text(text = "Ok")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(editText = title, onValueChange = { title=it }, placeholder = "Title", flag = false)

                    Spacer(modifier = Modifier.height(4.dp))
                    CustomTextField(editText = description, onValueChange = { description = it }, placeholder = "Description", flag = true)

                }
            }
        )
    }


}
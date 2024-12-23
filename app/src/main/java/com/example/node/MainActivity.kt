package com.example.node

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.node.ViewModel.NewsViewModel
import com.example.node.data.NewsData1
import com.example.node.repo.Frepository
import com.example.node.ui.theme.NodeTheme
import com.google.firebase.firestore.Query

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Frepository() // Create an instance of FirebaseRepository
        viewModel = ViewModelProvider(this, NewsViewModelFactory(repository)).get(NewsViewModel::class.java)
        setContent {
            NodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsApp(viewModel)

                }
            }
        }
    }
}

class NewsViewModelFactory(private val repository: Frepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsApp(viewModel: NewsViewModel) {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {

        },
        content = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Add, "News 1") },
                        label = { Text("News") },
                        selected = selectedItem == 0,
                        onClick = { selectedItem = 0 }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Add, "News 2") },
                        label = { Text("Flip News") },
                        selected = selectedItem == 1,
                        onClick = { selectedItem = 1 }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Add, "News 2") },
                        label = { Text("YTlink") },
                        selected = selectedItem == 2,
                        onClick = { selectedItem = 2 }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Add, "News 2") },
                        label = { Text("userVerify") },
                        selected = selectedItem == 3,
                        onClick = { selectedItem = 3 }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Add, "News 2") },
                        label = { Text("Posts") },
                        selected = selectedItem == 4,
                        onClick = { selectedItem = 4 }
                    )
                }
                when (selectedItem) {
                    0 -> news1(viewModel)
                    1 -> news2(viewModel)
                    2 -> youtubeLinks(viewModel)
                    3 -> UserVerification(viewModel)
                    4 -> PostScreen(viewModel)
                }
            }

        }
    )
}

@Composable
fun news1(viewModel: NewsViewModel) {
    var heading by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var report by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    val timeStamp: Long = System.currentTimeMillis()
    var showMessage by remember { mutableStateOf(false) }


    Column(verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()) {

        TextField(
            value = heading,
            onValueChange = { heading = it },
            label = { Text("Enter Heading") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Enter description") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = report,
            onValueChange = { report = it },
            label = { Text("Enter report") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image url") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Button(onClick = {
            val tagsList = tags.split(",").map { it.trim() }
            viewModel.uploadData(heading, description, report, imageUrl, tagsList, timeStamp)
            showMessage = true
        }) {
            Text("Upload to Firebase")
        }
        if (showMessage) {
            LaunchedEffect(Unit) {
                heading = ""
                description = ""
                report = ""
                imageUrl = ""
                tags = ""
                showMessage = false
            }
        }

    }
}


@Composable
fun news2(viewModel: NewsViewModel) {
    var heading1 by remember { mutableStateOf("") }
    var description1 by remember { mutableStateOf("") }
    var report1 by remember { mutableStateOf("") }
    var imageUrl1 by remember { mutableStateOf("") }
    val timeStamp: Long = System.currentTimeMillis()
    var heading2 by remember { mutableStateOf("") }
    var description2 by remember { mutableStateOf("") }
    var report2 by remember { mutableStateOf("") }
    var imageUrl2 by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()) {

        TextField(
            value = heading1,
            onValueChange = { heading1 = it },
            label = { Text("Enter Heading") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description1,
            onValueChange = { description1 = it },
            label = { Text("Enter description") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = report1,
            onValueChange = { report1 = it },
            label = { Text("Enter report") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = imageUrl1,
            onValueChange = { imageUrl1 = it },
            label = { Text("Image url") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = heading2,
            onValueChange = { heading2 = it },
            label = { Text("heading 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description2,
            onValueChange = { description2 = it },
            label = { Text("description 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = report2,
            onValueChange = { report2 = it },
            label = { Text("report 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = imageUrl2,
            onValueChange = { imageUrl2 = it },
            label = { Text("image 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Button(onClick = {
            viewModel.uploadData2(heading1, description1, report1, imageUrl1, timeStamp, heading2, description2, report2, imageUrl2)
            showMessage = true
        }) {
            Text("Upload to Firebase")
        }
        if (showMessage) {
            LaunchedEffect(Unit) {
                heading1 = ""
                description1 = ""
                report1 = ""
                imageUrl1 = ""
                heading2 = ""
                description2 = ""
                report2 = ""
                imageUrl2 = ""
                showMessage = false
            }
        }


    }
}


@Composable
fun youtubeLinks(viewModel: NewsViewModel) {
    var videoUrl by remember { mutableStateOf("") }
    var heading by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val timeStamp: Long = System.currentTimeMillis()
    var showMessage by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()) {

        TextField(
            value = videoUrl,
            onValueChange = { videoUrl = it },
            label = { Text("video link") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = heading,
            onValueChange = { heading = it },
            label = { Text("heading") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image Url") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )


        Button(onClick = {
            viewModel.uploadData3(videoUrl, heading,imageUrl, timeStamp)
            showMessage = true
        }) {
            Text("Upload to Firebase")
        }
        if (showMessage) {
            LaunchedEffect(Unit) {
                videoUrl = ""
                imageUrl = ""
                heading = ""
                showMessage = false
            }
        }

    }
}


@Composable
fun UserVerification(viewModel: NewsViewModel) {
    var uuid by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()) {

        TextField(
            value = uuid,
            onValueChange = { uuid = it },
            label = { Text("uuid of user") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Button(onClick = {
            viewModel.uploadUserData(uuid)
            showMessage = true
        }) {
            Text("Upload to Firebase")
        }
        if (showMessage) {
            LaunchedEffect(Unit) {
                uuid = ""
                showMessage = false
            }
        }

    }
}

@Composable
fun PostScreen(viewModel: NewsViewModel) {

    val news1 by viewModel.news1.observeAsState(initial = emptyList())
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Spacer(modifier = Modifier.height(16.dp))
        news1.take(10).forEach { news1 ->
            NewsBox(news1, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsBox(data: NewsData1, viewModel: NewsViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = { /* Handle single tap if needed */ },
                onLongClick = { showDialog = true }
            ).background(color = Color.White)
            ,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()

        ) {


            Image(
                modifier = Modifier.fillMaxWidth(0.07f).padding(start = 4.dp , end = 4.dp),
                painter = painterResource(id = R.drawable.screenshot_2024_10_27_002831),
                contentDescription = null,
            )



            Image(
                painter = rememberImagePainter(data.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
        }

        Column(modifier = Modifier.padding(start = 24.dp, end = 8.dp, top = 8.dp , bottom = 12.dp)){

            Text(
                text = data.heading,
                fontSize = 18.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W600,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = data.description,
                color = Color.hsl(0.75f, 0.4f, 0.4f, 0.73f) ,
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                lineHeight = 19.sp, // Same line height for consistency
                letterSpacing = 0.3.sp,
                maxLines = 4,
                fontWeight = FontWeight.W500,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Alert") },
                text = { Text(text = "Want to delete this news ?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteNews(data.id,
                            onSuccess = {  },
                            onFailure = { exception -> /* Handle error */ }
                        )
                        showDialog = false
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }


    }

}


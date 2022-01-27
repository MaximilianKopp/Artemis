package com.ataraxia.hunterexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataraxia.hunterexam.model.QuestionViewModel
import com.ataraxia.hunterexam.ui.theme.HunterExamTheme
import com.ataraxia.hunterexam.ui.theme.HuntingGreen
import com.ataraxia.hunterexam.ui.theme.Teal200

class MainActivity : ComponentActivity() {
    private lateinit var questionViewModel: QuestionViewModel
    private val helloViewModel: HelloViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        questionViewModel = ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(application)
            .create(QuestionViewModel::class.java)



        setContent {
            HunterExamTheme {
                HelloScreen()
            }
        }
    }

    class HelloViewModel : ViewModel() {

        private val _name: MutableLiveData<String> = MutableLiveData()
        val name: LiveData<String> = _name

        fun onNameChange(newName: String) {
            _name.value = newName
        }
    }

    @Preview
    @Composable
    fun HelloScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize().background(color = HuntingGreen)
                .padding(5.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.deer),
                contentDescription = "animal"
            )
            val name by helloViewModel.name.observeAsState("")
            HelloContent(name = name, onNameChange = { helloViewModel.onNameChange("Hallo Max") })
        }
    }

    @Composable
    fun HelloContent(name: String, onNameChange: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Hello, $name",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
//            OutlinedTextField(
//                value = name,
//                onValueChange = onNameChange,
//                label = { Text("Name") }
//            )
            TextButton(
                onClick = onNameChange,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Blue
                ),
                //modifier = Modifier.shadow(11.dp, CircleShape, clip = true)
            ) {
                Text(
                    text = "Click on me",
                    color = Color.White
                )
            }
        }
    }

    @Composable
    fun StartScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .size(12.dp)
                .padding(5.dp, 0.dp, 5.dp, 25.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        )
        {
            Row {
                Card(
                    modifier = Modifier
                        .size(180.dp, 150.dp)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier =
                        Modifier
                            .wrapContentSize(Alignment.Center)
                    ) {

                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_menu_book_24),
                                contentDescription = "Learn"
                            )
                            Text(
                                text = "1. Schritt lernen",
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .size(180.dp, 150.dp)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "2. Schritt üben",
                            fontSize = 12.sp,
                        )
                    }
                }
            }
            Row {
                Card(
                    modifier = Modifier
                        .size(180.dp, 150.dp)
                        .padding(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "Einstellungen",
                            fontSize = 12.sp,
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .size(180.dp, 150.dp)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = "Statistik",
                            fontSize = 12.sp,
                        )
                    }
                }
            }
        }
    }
}


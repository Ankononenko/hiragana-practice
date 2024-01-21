package com.example.hiragana

import com.pushwoosh.Pushwoosh
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hiragana.ui.theme.HiraganaTheme
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiraganaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("mainScreen") {
                        Pushwoosh.getInstance().registerForPushNotifications()
                        Greeting(name = "Misha") {
                            navController.navigate("romanizedHiraganaPractice")
                        }
                    }
                    composable("romanizedHiraganaPractice") {
                        // New screen content
                        RandomizedHiraganaToRomajiPractice(navigateToMainScreen = {
                            navController.navigate("mainScreen")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .align(Alignment.Center)
                .border(BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Text(
                text = "Start hiragana practice!",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    drawStyle = Stroke(
                        width = 8f
                    )
                )
            )
            Text(
                text = "Start hiragana practice!",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HiraganaTheme {
        Greeting(name = "Misha", onClick = {})
    }
}

@Composable
fun RandomizedHiraganaToRomajiPractice(navigateToMainScreen: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

    var hiraganaToRomajiMap = mapOf (
        "あ" to "a", "い" to "i", "う" to "u", "え" to "e", "お" to "o",
        "か" to "ka", "き" to "ki", "く" to "ku", "け" to "ke", "こ" to "ko",
        "さ" to "sa", "し" to "shi", "す" to "su", "せ" to "se", "そ" to "so",
        "た" to "ta", "ち" to "chi", "つ" to "tsu", "て" to "te", "と" to "to",
        "な" to "na", "に" to "ni", "ぬ" to "nu", "ね" to "ne", "の" to "no",
        "は" to "ha", "ひ" to "hi", "ふ" to "fu", "へ" to "he", "ほ" to "ho",
        "ま" to "ma", "み" to "mi", "む" to "mu", "め" to "me", "も" to "mo",
        "や" to "ya", "ゆ" to "yu", "よ" to "yo",
        "ら" to "ra", "り" to "ri", "る" to "ru", "れ" to "re", "ろ" to "ro",
        "わ" to "wa", "を" to "wo",
        "ん" to "n"
    )

    val shuffledMap = remember { hiraganaToRomajiMap.entries.shuffled().iterator() }

    var currentPair by remember { mutableStateOf(shuffledMap.next()) }
    var userInput by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    val numCorrectAnswers = 46;
    var numCorrectAnswersCurrent by remember { mutableStateOf(46) };
    var isPracticeCompleted by remember { mutableStateOf(false) }

    fun checkAnswer() {
        if (userInput.trim().isEmpty()){
            return;
        }
        if (userInput.lowercase().trim() == currentPair.value) {
            feedback = "Correct!"
        } else {
            feedback = """Incorrect, the right answer is "${currentPair.value}""""
            --numCorrectAnswersCurrent;
        }

        if (shuffledMap.hasNext()) {
            currentPair = shuffledMap.next()
            userInput = ""
        } else {
            val answers = "answer";
            feedback = """All done!
                |${numCorrectAnswersCurrent} correct ${answers.pluralize(numCorrectAnswersCurrent)} out of ${numCorrectAnswers}.
            """.trimMargin()
            isPracticeCompleted = true;
        }
    }

    Column(modifier = Modifier.padding(16.dp))
    {
        Text("What is the Romaji for ${currentPair.key}?")

        TextField(
            value = userInput,
            onValueChange = { userInput = it},
            label = { Text("Enter Romaji")},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {checkAnswer()})
        )

        Button(onClick = {
            checkAnswer()
        }) {
            Text("Check")
        }
        Text(feedback)

        if (isPracticeCompleted) {
            Button(onClick = navigateToMainScreen) {
                Text("Return to main screen")
            }
        }
    }
} }

fun String.pluralize(count: Int): String {
    return if (count == 1) this else "${this}s"
}
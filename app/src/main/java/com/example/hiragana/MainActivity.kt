package com.example.hiragana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hiragana.ui.theme.HiraganaTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiraganaTheme {
                HiraganaApp()
            }
        }
    }
}

@Composable
fun HiraganaApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") { GreetingScreen(navController) }
        composable("choiceScreen") { ChoiceScreen(navController) }
        composable("stepByStepPractice") { StepByStepPracticeScreen(navController) }
        composable("fullPractice") { FullPracticeScreen(navController) }
    }
}

@Composable
fun GreetingScreen(navController: androidx.navigation.NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate("choiceScreen") }) {
            Text("Start Hiragana Practice")
        }
    }
}

@Composable
fun ChoiceScreen(navController: androidx.navigation.NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate("stepByStepPractice") }) {
            Text("Practice Step by Step")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("fullPractice") }) {
            Text("Practice All Hiragana")
        }
    }
}

@Composable
fun StepByStepPracticeScreen(navController: androidx.navigation.NavHostController) {
    StepByStepHiraganaPractice(navigateToMainScreen = {
        navController.navigate("mainScreen")
    }, true)
}

@Composable
fun FullPracticeScreen(navController: androidx.navigation.NavHostController) {
    RandomizedHiraganaToRomajiPractice(navigateToMainScreen = {
        navController.navigate("mainScreen")
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HiraganaTheme {
        HiraganaApp()
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
        var incorrectAnswers = remember { mutableMapOf<String, String>() }
        var isPracticeCompleted by remember { mutableStateOf(false) }

        fun checkAnswer() {
            if (userInput.trim().isEmpty()){
                return;
            }
            if (userInput.lowercase().trim() == currentPair.value) {
                feedback = "Correct!"
            } else {
                incorrectAnswers.put(currentPair.key, userInput.lowercase().trim())
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
            if(!isPracticeCompleted) {

                Text("What is the romaji for ${currentPair.key}?")

                TextField(
                    value = userInput,
                    onValueChange = { userInput = it},
                    label = { Text("Enter romaji")},
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {checkAnswer()})
                )

                Button(onClick = {
                    checkAnswer()
                }) {
                    Text("Check")
                }
                Text(feedback)
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item {
                        Text("Review incorrect answers", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Text(
                                "Hiragana character",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Incorrect answer",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Correct \nanswer",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    incorrectAnswers.forEach { (hiragana, userAnswer) ->
                        item {
                            Row {
                                Text("$hiragana", modifier = Modifier.weight(1f))
                                Text(userAnswer, modifier = Modifier.weight(1f))
                                val correctAnswer = hiraganaToRomajiMap[hiragana] ?: "N/A"
                                Text(correctAnswer, modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = navigateToMainScreen) {
                            Text("Return to main screen")
                        }
                    }
                }
            }
        }
    } }

fun String.pluralize(count: Int): String {
    return if (count == 1) this else "${this}s"
}

@Composable
fun StepByStepHiraganaPractice(navigateToMainScreen: () -> Unit, stepByStep: Boolean = true) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val hiraganaLevels = listOf(
            mapOf("あ" to "a", "い" to "i", "う" to "u", "え" to "e", "お" to "o", "ん" to "n"),
            mapOf("か" to "ka", "き" to "ki", "く" to "ku", "け" to "ke", "こ" to "ko"),
            mapOf("さ" to "sa", "し" to "shi", "す" to "su", "せ" to "se", "そ" to "so"),
            mapOf("た" to "ta", "ち" to "chi", "つ" to "tsu", "て" to "te", "と" to "to"),
            mapOf("な" to "na", "に" to "ni", "ぬ" to "nu", "ね" to "ne", "の" to "no"),
            mapOf("は" to "ha", "ひ" to "hi", "ふ" to "fu", "へ" to "he", "ほ" to "ho"),
            mapOf("ま" to "ma", "み" to "mi", "む" to "mu", "め" to "me", "も" to "mo"),
            mapOf("や" to "ya", "ゆ" to "yu", "よ" to "yo"),
            mapOf("ら" to "ra", "り" to "ri", "る" to "ru", "れ" to "re", "ろ" to "ro"),
            mapOf("わ" to "wa", "を" to "wo")
        )

        var currentLevelIndex by remember { mutableStateOf(0) }
        var hiraganaToRomajiMap by remember { mutableStateOf(hiraganaLevels[currentLevelIndex]) }
        val shuffledMap = remember(currentLevelIndex) { hiraganaToRomajiMap.entries.shuffled().iterator() }
        var currentPair by remember { mutableStateOf(shuffledMap.next()) }
        var userInput by remember { mutableStateOf("") }
        var feedback by remember { mutableStateOf("") }
        var isLevelCompleted by remember { mutableStateOf(false) }

        fun resetForNextLevel() {
            currentLevelIndex++
            if (currentLevelIndex < hiraganaLevels.size) {
                hiraganaToRomajiMap = hiraganaLevels[currentLevelIndex]
                val newShuffledMap = hiraganaToRomajiMap.entries.shuffled().iterator()
                if (newShuffledMap.hasNext()) {
                    currentPair = newShuffledMap.next()
                }
                isLevelCompleted = false
                feedback = ""
                userInput = ""
            } else {
                navigateToMainScreen()
            }
        }

        fun checkAnswer() {
            if (userInput.trim().isEmpty()) {
                feedback = "Please enter an answer."
                return
            }

            if (userInput.lowercase().trim() == currentPair.value) {
                feedback = "Correct!"
                if (!shuffledMap.hasNext()) {
                    isLevelCompleted = true
                    feedback = "Level completed!"
                } else {
                    currentPair = shuffledMap.next()
                    userInput = ""
                }
            } else {
                feedback = """Incorrect, the correct answer is "${currentPair.value}""""
                userInput = ""
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            if (!isLevelCompleted) {
                Text("What is the romaji for \"${currentPair.key}\"?")
                TextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Enter romaji") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { checkAnswer() })
                )
                Button(onClick = { checkAnswer() }) {
                    Text("Check")
                }
                Text(feedback)
            }

            if (isLevelCompleted) {
                if (currentLevelIndex + 1 < hiraganaLevels.size) {
                    Button(onClick = { resetForNextLevel() }) {
                        Text("Next Level")
                    }
                } else {
                    Button(onClick = navigateToMainScreen) {
                        Text("All levels completed! Return to main screen")
                    }
                }
            }
        }
    }
}


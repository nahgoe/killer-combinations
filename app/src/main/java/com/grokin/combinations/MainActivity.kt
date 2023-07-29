package com.grokin.combinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grokin.combinations.ui.theme.CombinationsTheme
import androidx.compose.runtime.*
import com.chargemap.compose.numberpicker.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CombinationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val res = combinations( 3, 14, 1)
                    val builder = StringBuilder()
                    for (c in res){
                        for (n in c){
                            builder.append(n)
                        }
                        builder.append(",")
                    }
                    Greeting(builder.toString())

                    Row {
                        SquaresPicker()
                        TotalPicker()
                    }
                }
            }
        }
    }

    private fun combinations(squares: Int, total: Int, start: Int): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        if (squares == 1) {
            if (total in (start + 1)..9) {
                val element = listOf(total)
                list.add(element)
            }
        } else {
            for (value in start until 9) {
                val remaining = total - value
                val c = combinations(squares - 1, remaining, value + 1)
                for (x in c) {
                    val element = mutableListOf(value)
                    element.addAll(x)
                    list.add(element)
                }
            }
        }
        return list
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CombinationsTheme {
        Greeting("Android")
    }
}

@Composable
private fun SquaresPicker() {
    var state by remember { mutableStateOf(0) }
    NumberPicker(
        value = state,
        range = 1..9,
        onValueChange = {
            state = it
        }
    )
}
@Composable
private fun TotalPicker() {
    var state by remember { mutableStateOf(0) }
    NumberPicker(
        value = state,
        range = 1..9,
        onValueChange = {
            state = it
        }
    )
}

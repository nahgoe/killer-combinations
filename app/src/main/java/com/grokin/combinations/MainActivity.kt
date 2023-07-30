package com.grokin.combinations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grokin.combinations.ui.theme.CombinationsTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chargemap.compose.numberpicker.*

class MainActivity : ComponentActivity() {
    class SquaresViewModel : ViewModel() {
        private val _squares = MutableLiveData(2)
        val squares: LiveData<Int> = _squares
        private val _total = MutableLiveData(3)
        val total: LiveData<Int> = _total
        private val _min = MutableLiveData(3)
        val min: LiveData<Int> = _min
        private val _max = MutableLiveData(16)
        val max: LiveData<Int> = _max

        fun onSquaresChange(newSquares: Int) {
            _squares.value = newSquares

            var min = 0
            var max = 0
            for (i in 1..newSquares) {
                min += i
                max += (10 - i)
            }
            _total.value?.let {
                if (min > it) {
                    _total.value = min
                }
                if (max < it) {
                    _total.value = max
                }
                _min.value = min
                _max.value = max
            }
        }

        fun onTotalChange(newTotal: Int) {
            _total.value = newTotal
        }
    }

    private val squaresViewModel = SquaresViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CombinationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val sq by squaresViewModel.squares.observeAsState(2)
                    val to by squaresViewModel.total.observeAsState(3)
                    val min by squaresViewModel.min.observeAsState(3)
                    val max by squaresViewModel.max.observeAsState(16)

                    Column {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                Modifier
                                    .padding(16.dp)
                            ) {
                                SquaresPicker(sq) { squaresViewModel.onSquaresChange(it) }
                            }
                            Column(
                                Modifier
                                    .padding(16.dp)
                            ) {
                                TotalPicker(to, min, max) { squaresViewModel.onTotalChange(it) }
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column {
                                val res = combinations(sq, to)
                                for (c in res) {
                                    Combination(c)
                                }

                            }
                        }
                    }
                }
            }
        }
    }


    private fun combinations(squares: Int, total: Int, start: Int = 1): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        if (squares == 1) {
            if (total in start..9) {
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

    @Composable
    private fun SquaresPicker(value: Int, onSquaresChange: (Int) -> Unit) {
        NumberPicker(
            value = value,
            range = 2..8,
            onValueChange = onSquaresChange
        )
    }

    @Composable
    private fun TotalPicker(value: Int, min: Int, max: Int, onTotalChange: (Int) -> Unit) {
        NumberPicker(
            value = value,
            range = min..max,
            onValueChange = onTotalChange
        )
    }

    @Composable
    private fun Combination(array: List<Int>) {
        val builder = StringBuilder()

        for (n in array) {
            builder.append(n)
        }
        Text(text = builder.toString())
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

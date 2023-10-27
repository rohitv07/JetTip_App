package com.project.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.jettip.components.InputField
import com.project.jettip.ui.theme.JetTipTheme
import com.project.jettip.util.calculateTotalPerPerson
import com.project.jettip.util.calculateTotalTip
import com.project.jettip.widgets.RoundIconButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                //TopHeader()
                MainContent()
            }
        }
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipTheme{
    Surface(
        color = MaterialTheme.colorScheme.onBackground) {
        content()
      }
    }
}


@Preview
@Composable
fun TopHeader(totalPerPerson:Double = 134.0 ){

    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .padding(12.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        color = Color(0xFFE9D7F7),
        )
    {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            val total = "%.2f".format(totalPerPerson)

            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
               // color = MaterialTheme.colorScheme.onBackground,
                //fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
               // color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center)
        }

    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){

    Column(modifier = Modifier.padding(all = 12.dp)) {
        BillForm(){billAmt ->

            Log.d("AMT", "MainContent: $billAmt")

        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
){

    val totalBillState = rememberSaveable { mutableStateOf("") }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty() }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember { mutableStateOf(0f) }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val splitByState = remember { mutableStateOf(1) }

    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember { mutableStateOf(0.0) }

    val totalPerPersonState = remember { mutableStateOf(0.0) }



    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        //color = MaterialTheme.colorScheme.onBackground,
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            InputField(valueState = totalBillState ,
                labelID = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if (!validState) return@KeyboardActions

                    //Todo - onValueChange
                    onValueChange(totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )

            if (validState) {
                Row(
                    modifier = Modifier
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ) {

                    Text(
                        text = "Split",
                        modifier = Modifier.align(
                            alignment = Alignment.CenterVertically
                        ),
                        // color =MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(120.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {

                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onCLick = {
                                // Log.d("ICON", "BillForm: Removed ")
                                splitByState.value =
                                    if (splitByState.value > 1) splitByState.value - 1 else 1

                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value, tipPercent = tipPercentage
                                    )
                            })

                        Text(
                            text = "${splitByState.value}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )


                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onCLick = {
                                //    Log.d("ICON", "BillForm: Added")
                                if (splitByState.value < range.last) {
                                    splitByState.value = splitByState.value + 1
                                }

                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value, tipPercent = tipPercentage
                                    )
                            })
                    }
                }


                //Tip Row
                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(200.dp))

                    Text(text = "$ ${tipAmountState.value}")
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = "$tipPercentage %")

                    Spacer(modifier = Modifier.height(15.dp))

                    //Slider
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal
                            //Log.d("Slider", "BillForm: $newVal")
                            tipAmountState.value =
                                calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercent = tipPercentage
                                )

                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value, tipPercent = tipPercentage
                                )
                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        //steps = 5
                    )

                }
            }
            else{
                Box{}
            }
        }
    }
}



















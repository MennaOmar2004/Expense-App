package com.example.expensetracker.ui.theme

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.R
import com.example.expensetracker.room.ExpenseEntity
import com.example.expensetracker.room.ExpenseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: ExpenseViewModel = viewModel()) {

    val allExpenses by viewModel.allExpenses.collectAsState(initial = emptyList())
    val sumOfExpense by viewModel.totalExpenses.collectAsState(initial = Double)
    val sheetState = rememberBottomSheetScaffoldState(bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))
    val coroutineScope = rememberCoroutineScope()
    val idValue by remember { mutableIntStateOf(0) }
    var thingText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }

@Composable
fun AnimatedButton(
    txt:String,
    onClick:()->Unit,
    modifier : Modifier=Modifier,
)
{
    var isPressed by remember { mutableStateOf(false) }

    val backGroundColor by animateColorAsState(
        targetValue = if (isPressed) colorResource(R.color.Light_teal) else Color.LightGray,
        animationSpec = tween(durationMillis = 200),
        label = ""
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) .9f else 1f ,
        animationSpec = tween(durationMillis = 200),
        label = ""
    )
    Button(
        onClick={
            isPressed =true
            onClick()
        },
        colors = ButtonDefaults.buttonColors(backGroundColor),
        modifier = modifier
            .scale(scale)
            .pointerInput(Unit){detectTapGestures(onPress = {isPressed = true
                tryAwaitRelease()
                isPressed = false})}
            .shadow(elevation = 3.dp, RoundedCornerShape(15.dp)),
        border = BorderStroke(width = 3.dp, color = Color.DarkGray.copy(alpha = .8f))
    )
    {
      Text(
          text =txt,
          fontSize = 20.sp,
          fontWeight = FontWeight.SemiBold,
          color = colorResource(R.color.black),
          fontStyle = FontStyle.Italic
      )
    }

    LaunchedEffect(isPressed) {
        if (isPressed){
        delay(200)
        isPressed = false
    }
    }

}

    Scaffold(
        containerColor = colorResource(R.color.teal_700),
        topBar = {
            TopAppBar(
                modifier = Modifier.height(250.dp),
                title = {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = if (sumOfExpense==null) "Total Expense 0.0 Eg" else "Total Expense $sumOfExpense Eg",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorResource(R.color.white),
                            fontStyle = FontStyle.Italic
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.teal_700))
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.size(height = 70.dp, width = Dp(Float.MAX_VALUE)),
                content = {
                    Row (
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    )
                    {
                        AnimatedButton(
                            txt = "clear",
                            onClick = {viewModel.clearAllExpenses()},
                            modifier = Modifier.weight(1f)

                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        AnimatedButton(
                            txt = "Add",
                            onClick = {coroutineScope.launch { sheetState.bottomSheetState.expand() }},
                            modifier = Modifier.weight(1f)

                        )
                    }
                },
                containerColor = colorResource(R.color.teal_700)
            )
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(color = Color.LightGray)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp, horizontal = 40.dp),
                    text = "Things ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.black),
                    fontStyle = FontStyle.Italic

                )
                Text(
                    modifier = Modifier.padding(vertical = 30.dp, horizontal = 40.dp),
                    text = "Price ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.black),
                    fontStyle = FontStyle.Italic
                )
            }
            LazyColumn()
            {
                items(allExpenses) {expense->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 25.dp, horizontal = 10.dp)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        )
                        {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    modifier = Modifier
                                        .padding(start = 5.dp)
                                        .size(25.dp),
                                    contentDescription = ""
                                )
                                Spacer(Modifier.padding(5.dp))
                                Column {
                                    Text(
                                        modifier = Modifier.padding(
                                            vertical = 15.dp,
                                            horizontal = 5.dp
                                        ),
                                        text = expense.thing,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorResource(R.color.black),
                                        fontStyle = FontStyle.Italic

                                    )
                                    Text(
                                        modifier = Modifier.padding(
                                            vertical = 10.dp,
                                            horizontal = 5.dp
                                        ),
                                        text = expense.date,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Red,
                                        fontStyle = FontStyle.Italic

                                    )
                                }
                            }
                            Text(
                                modifier = Modifier.padding(
                                    vertical = 20.dp,
                                    horizontal = 25.dp
                                ),
                                text = expense.price.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(R.color.Green),
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.shadow(elevation = 1.5.dp),
                        color = colorResource(R.color.black),
                        thickness = .5.dp
                    )
                }
            }
        }
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add Expense",
                        fontSize = 25.sp,
                        fontStyle = FontStyle.Italic,
                        color = colorResource(R.color.black),
                    )
                    OutlinedTextField(
                        value = thingText,
                        onValueChange = {expenseName->
                            thingText=expenseName
                        },
                        label = { Text(text = "Expense Name", fontSize = 15.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.DarkGray,
                            focusedLabelColor = Color.Black,
                        )
                    )
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = {price->
                            priceText = price
                        },
                        label = { Text(text = "Price", fontSize = 15.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.DarkGray,
                            focusedLabelColor = Color.Black,
                        )
                    )
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = {date->
                            dateText = date
                        },
                        label = { Text(text = "Date", fontSize = 15.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.DarkGray,
                            focusedLabelColor = Color.Black,
                        )
                    )
                    AnimatedButton(
                        txt = "Save Expense",
                        onClick = {
                            if (thingText.isNotEmpty() && priceText.isNotEmpty()&& dateText.isNotEmpty()){
                                viewModel.insertExpense(ExpenseEntity(id = idValue, thing = thingText, price = priceText.toInt(), date = dateText))
                                coroutineScope.launch { sheetState.bottomSheetState.hide() }
                            }
                        }
                    )
                    }
                },
            sheetPeekHeight = 0.dp // عشان يكون مخفي في البداية
        ){}
    }
}





@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowPreview(){
    HomeScreen()
}
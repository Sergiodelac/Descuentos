package edu.utvt.descuentos.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.utvt.descuentos.util.calculateTotalPerPerson
import edu.utvt.descuentos.util.calculateTotalTip

private val IconButtonSizeModifier = Modifier.size(40.dp)

@Composable
fun RoundIconButton(
    imageVector: ImageVector = Icons.Default.Done,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.Black
    ),
    elevation: CardElevation = CardDefaults.cardElevation(4.dp),
    onClick: () -> Unit = {}
){
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
            .then(IconButtonSizeModifier),
        shape = CircleShape,
        elevation = elevation,
        colors = colors,

    ) {
        Icon(
            imageVector = imageVector,
            null,
            modifier = Modifier
                .padding(top = 9.dp)
                .align(alignment = Alignment.CenterHorizontally)
             )

    }
}

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    message: String = "Default",
    color: Color = MaterialTheme.colorScheme.primary,
){
    Text(text = message, color = color, modifier = modifier)
}

@Composable
fun Header(amountPerPerson: Double = 0.0){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(20.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(10.dp))),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val total = "%.2f".format(amountPerPerson)
            
            Text(text = "Monto por Persona", style = MaterialTheme.typography.headlineSmall)
            Text(text =  "\$$total", style = MaterialTheme.typography.headlineLarge )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier.padding(10.dp),
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
){
    OutlinedTextField(
        modifier = modifier,
        value =  valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = "Money"
            )
        },
        singleLine = isSingleLine,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1 .. 100,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
){
    val totalBillState = remember {
        mutableStateOf("")
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyBoardController = LocalSoftwareKeyboardController.current

    val splitByState = remember {
        mutableStateOf(1)
    }

    var sliderPosition = remember {
        mutableStateOf(0f)
    }

    sliderPosition.value = Math.round(sliderPosition.value * 100f).toFloat() /100
    val tipPercentage = (sliderPosition.value * 100f).toInt()

    if (validState){
        tipAmountState.value =
            calculateTotalTip(
                totalBill = totalBillState.value.toDouble(),
                tipPercent = tipPercentage
            )
        totalPerPersonState.value =
            calculateTotalPerPerson(
                totalBill = totalBillState.value.toDouble(),
                splitBy = splitByState.value,
                tipPercent = tipPercentage
            )
    } else{
        totalPerPersonState.value = 0.0
        splitByState.value = 1
        sliderPosition.value = 0f
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        modifier = Modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(2.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                valueState = totalBillState,
                labelId = "Ingrese el Monto",
                onAction = KeyboardActions{
                    if (!validState){
                        onValChange(totalBillState.value.trim())
                        keyBoardController?.hide()
                        return@KeyboardActions
                    }
                })
            if (validState){
                Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "Entre",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(120.dp))

                    RoundIconButton(imageVector = Icons.Default.KeyboardArrowDown){
                        splitByState.value =
                            if (splitByState.value > 1) splitByState.value - 1 else 1
                    }

                    Text(text = "${splitByState.value}", modifier = Modifier.padding(10.dp))

                    RoundIconButton(imageVector = Icons.Default.KeyboardArrowUp){
                        if (splitByState.value < range.last){
                            splitByState.value = splitByState.value + 1
                        }
                    }

                }

                Row (
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Text(
                        text = "Propina",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(200.dp))

                    Text(
                        text = "$${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = "$tipPercentage%")
                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = sliderPosition.value,
                        onValueChange = { newVal ->
                            sliderPosition.value = newVal
                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 19
                    )

                }
            } else
                Box(){}
        }
    }
}
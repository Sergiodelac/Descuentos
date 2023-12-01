package edu.utvt.descuentos.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.utvt.descuentos.components.BillForm
import edu.utvt.descuentos.components.Header
import edu.utvt.descuentos.components.TitleBar

@Composable
private fun ContentDetails(){
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Header(amountPerPerson = totalPerPersonState.value)
        BillForm(totalPerPersonState = totalPerPersonState)

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipView(){

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar(message = "Aplicacion para el calculo de su factura") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        }
    ) {
        ContentDetails()
    }
}
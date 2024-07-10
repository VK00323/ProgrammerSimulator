package com.example.programmergame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun StatisticScreen(
    moneyRub: String,
    moneyBTC: String,
    moneyEnergy: String,
    moneyStealth: String,
    infoText: String,
    onProgramButtonClick: () -> Unit,
    onWorkButtonClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)
    ) {
        val (buttonWork,
            buttonProgram,
            textViewMoneyRub,
            textViewMoneyBTC,
            textViewMoneyEnergy,
            textViewInfo,
            imageViewEnergy,
            imageViewRub,
            imageViewBTC,
            imageViewStealth,
            textViewMoneyStealth,
            progressBar
        ) = createRefs()

        Button(
            onClick = onWorkButtonClick,
            modifier = Modifier
                .constrainAs(buttonWork) {
                    start.linkTo(buttonProgram.end)
                    end.linkTo(parent.end, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 10.dp)
        ) {
            Text(text = "Работать", fontSize = 11.sp)
        }

        Button(
            onClick = onProgramButtonClick,
            modifier = Modifier
                .constrainAs(buttonProgram) {
                    start.linkTo(parent.start)
                    end.linkTo(buttonWork.start, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
        ) {
            Text(text = "Программировать", fontSize = 11.sp)
        }

        Image(
            painter = painterResource(R.drawable.energy),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewEnergy) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 5.dp)
                }
        )

        Image(
            painter = painterResource(R.drawable.rub),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewRub) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 5.dp)
                }
        )

        Image(
            painter = painterResource(R.drawable.btc),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewBTC) {
                    end.linkTo(parent.end)
                    top.linkTo(imageViewRub.bottom, margin = 5.dp)
                }
        )

        Image(
            painter = painterResource(R.drawable.stealth),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewStealth) {
                    start.linkTo(parent.start)
                    top.linkTo(imageViewEnergy.bottom, margin = 5.dp)
                }
        )

        Text(
            text = moneyRub,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(textViewMoneyRub) {
                    end.linkTo(imageViewRub.start, margin = 18.dp)
                    top.linkTo(parent.top, margin = 5.dp)
                }
        )

        Text(
            text = moneyBTC,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(textViewMoneyBTC) {
                    end.linkTo(imageViewBTC.start, margin = 18.dp)
                    top.linkTo(textViewMoneyRub.bottom, margin = 5.dp)
                }
        )

        Text(
            text = moneyEnergy,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(textViewMoneyEnergy) {
                    start.linkTo(imageViewEnergy.end, margin = 18.dp)
                    top.linkTo(parent.top, margin = 5.dp)
                }
        )

        Text(
            text = infoText,
            modifier = Modifier
                .constrainAs(textViewInfo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    verticalBias = 0.3f
                }
        )

        // Replace with your image loading logic, such as Image from Coil or Glide
        // Here, just using empty Box to represent image views for simplicity
        Box(
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewEnergy) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 5.dp)
                }
        )

        Box(
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewRub) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 5.dp)
                }
        )

        Box(
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewBTC) {
                    end.linkTo(parent.end)
                    top.linkTo(imageViewRub.bottom, margin = 5.dp)
                }
        )

        Box(
            modifier = Modifier
                .size(30.dp)
                .constrainAs(imageViewStealth) {
                    start.linkTo(parent.start)
                    top.linkTo(imageViewEnergy.bottom, margin = 5.dp)
                }
        )

        Text(
            text = moneyStealth,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(textViewMoneyStealth) {
                    start.linkTo(imageViewStealth.end, margin = 18.dp)
                    top.linkTo(textViewMoneyEnergy.bottom, margin = 5.dp)
                }
        )

        LinearProgressIndicator(
            progress = 0.5f,
            modifier = Modifier
                .constrainAs(progressBar) {
                    start.linkTo(textViewMoneyEnergy.end, margin = 5.dp)
                    top.linkTo(parent.top, margin = 15.dp)
                    width = Dimension.value(100.dp)
                }
        )
    }
}

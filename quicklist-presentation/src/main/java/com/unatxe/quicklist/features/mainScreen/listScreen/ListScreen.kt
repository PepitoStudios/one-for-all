package com.unatxe.quicklist.features.mainScreen.listScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.unatxe.quicklist.navigation.NavigationDirections

import com.unatxe.quicklist.ui.theme.One4allTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(qListId: Int) {
    if (qListId == NavigationDirections.ListScreen.NO_VALUE){
        Text("ListScreen without id")
    }else{
        Text("ListScreen with id $qListId")
    }
}


@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    One4allTheme {
        ListScreen(0)
    }
}

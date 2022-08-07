package com.unatxe.quicklist.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.unatxe.quicklist.R

@OptIn(ExperimentalTextApi::class)
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
val fontName = GoogleFont("Cabin")

@OptIn(ExperimentalTextApi::class)
val fontFamilyCabin = FontFamily(
    Font(R.font.cabin_medium, weight = FontWeight.Medium),
    Font(R.font.cabin_bold, weight = FontWeight.Bold),
    Font(R.font.cabin_regular, weight = FontWeight.Normal)
)

val h3Medium = TextStyle(
    fontFamily = fontFamilyCabin,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp
)

val h3Regular = TextStyle(
    fontFamily = fontFamilyCabin,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp
)

val h5Bold = TextStyle(
    fontFamily = fontFamilyCabin,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.75.sp,
    fontSize = 18.sp
)

val bodyRegular = TextStyle(
    fontFamily = fontFamilyCabin,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)

val bodyLarge = TextStyle(
    fontFamily = fontFamilyCabin,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
)
// Set of Material typography styles to start with
/*val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 21.87.sp,

    ),

    H4 = TextStyle(
        fontFamily = fontFamilyCabin,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    /*labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
*/

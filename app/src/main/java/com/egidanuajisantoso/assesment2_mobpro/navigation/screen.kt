package com.egidanuajisantoso.assesment2_mobpro.navigation

// Screen.kt
sealed class Screen(val route: String) {
    object Home : Screen("main")
    object FormBaru : Screen("form_baru")
    object FormUbah : Screen("form_ubah/{id}") {
        fun createRoute(id: Int) = "form_ubah/$id"
    }
}
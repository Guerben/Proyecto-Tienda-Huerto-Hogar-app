package com.example.huertohogar_app.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // 1. IMPORTAR 'remember'
import androidx.lifecycle.viewmodel.compose.viewModel // 2. IMPORTAR 'viewModel'
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation // 3. IMPORTAR 'navigation' (para el grafo anidado)
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.huertohogar_app.auth.AuthViewModel // 4. IMPORTAR TU VIEWMODEL
import com.example.huertohogar_app.auth.LoginScreen
import com.example.huertohogar_app.auth.RegisterScreen
import com.example.huertohogar_app.cart.CartScreen
import com.example.huertohogar_app.products.ProductDetailScreen
import com.example.huertohogar_app.products.ProductListScreen

/**
 * Define las rutas de la aplicación.
 */
object Routes {
    // 5. RUTA PARA EL GRAFO DE AUTENTICACIÓN
    const val AUTH_GRAPH = "auth_graph"

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PRODUCTS_LIST = "products"
    const val CART = "cart"

    const val PRODUCT_DETAIL_ROUTE = "productdetail"
    const val PRODUCT_DETAIL_ARG = "productId"
    const val PRODUCT_DETAIL = "$PRODUCT_DETAIL_ROUTE/{$PRODUCT_DETAIL_ARG}"

    fun productDetail(productId: Long) = "$PRODUCT_DETAIL_ROUTE/$productId"
}

/**
 * Grafo de navegación principal de la aplicación.
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // 6. El NavHost AHORA EMPIEZA EN EL GRAFO DE AUTH
    NavHost(navController = navController, startDestination = Routes.AUTH_GRAPH) {

        // --- INICIO: GRAFO DE AUTENTICACIÓN ANIDADO ---
        // 7. Todas las pantallas de auth van aquí dentro
        navigation(
            startDestination = Routes.LOGIN,
            route = Routes.AUTH_GRAPH
        ) {
            /**
             * Pantalla de Login
             */
            composable(Routes.LOGIN) {
                // 8. Obtenemos el "dueño" del ViewModel (el grafo "AUTH_GRAPH")
                val authGraphEntry = remember(it) {
                    navController.getBackStackEntry(Routes.AUTH_GRAPH)
                }
                // 9. Pedimos el ViewModel compartido
                val sharedAuthViewModel: AuthViewModel = viewModel(authGraphEntry)

                LoginScreen(
                    // 10. Pasamos el ViewModel compartido a la pantalla
                    viewModel = sharedAuthViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.PRODUCTS_LIST) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true } // Limpia el grafo de auth
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    }
                )
            }

            /**
             * Pantalla de Registro
             */
            composable(Routes.REGISTER) {
                // 8. Obtenemos el "dueño" del ViewModel (el grafo "AUTH_GRAPH")
                val authGraphEntry = remember(it) {
                    navController.getBackStackEntry(Routes.AUTH_GRAPH)
                }
                // 9. Pedimos el MISMO ViewModel compartido
                val sharedAuthViewModel: AuthViewModel = viewModel(authGraphEntry)

                RegisterScreen(
                    // 10. Pasamos el ViewModel compartido a la pantalla
                    viewModel = sharedAuthViewModel,
                    onRegistrationSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }
        }
        // --- FIN: GRAFO DE AUTENTICACIÓN ---


        /**
         * Pantalla de Lista de Productos (Esta usa su propio ViewModel)
         */
        composable(Routes.PRODUCTS_LIST) {
            ProductListScreen(
                onOpenCart = {
                    navController.navigate(Routes.CART)
                },
                onProductSelected = { productId ->
                    navController.navigate(Routes.productDetail(productId))
                },
                onLogout = {
                    navController.navigate(Routes.AUTH_GRAPH){ // Regresa al grafo de auth
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        /**
         * Pantalla de Detalle de Producto
         */
        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument(Routes.PRODUCT_DETAIL_ARG) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong(Routes.PRODUCT_DETAIL_ARG)
            if (productId != null) {
                ProductDetailScreen(
                    productId = productId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }

        /**
         * Pantalla del Carrito
         */
        composable(Routes.CART) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
package com.example.programmergame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.programmergame.databinding.FragmentCoursesBinding
import com.example.programmergame.utils.showBottomSheetDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.parse.ParseUser
import com.parse.google.ParseGoogleUtils

class CoursesFragment : Fragment() {
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    private lateinit var coursesViewModel:  CoursesViewModel
    private var launcher: ActivityResultLauncher<Intent>? = null

    private fun getClient(): GoogleSignInClient {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.EMAIL))
            .requestIdToken("414952582173-8aa2iifkra3hcooujdmo1bccqvkp7dlh.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]

        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.text
        coursesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val currentUser = ParseUser.getCurrentUser()?.username?: "Пользователя нет"

        val isAuthenticatedUser = ParseUser.getCurrentUser()?.isAuthenticated
        Log.d("AAAAA", "Текущий пользователь $currentUser , Полизователь аутентифицированный? $isAuthenticatedUser")

        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Успешная авторизация
//                ParseGoogleUtils.onActivityResult(result.resultCode, result.data)
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("AAAAA", "Успешная авторизация id ${account}")
//                    Log.d("AAAAA", "Успешная авторизация id ${account.id}")
//                    Log.d("AAAAA", "Успешная авторизация id Token ${account.idToken}")
//                    Log.d("AAAAA", "Успешная авторизация email ${account.email}")

                    loginInBack4AppWithTutorialMethod(account)

                }
                catch (e:ApiException){
                    Log.d("AAAAA", "Api Exception $e")

                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // Авторизация отменена
                Log.e("AAAAA", "Авторизация отменена")

                // Проверяем, есть ли данные в Intent
                if (result.data != null) {
                    val additionalData = result.data?.extras
                    if (additionalData != null) {
                        // Пример: проверяем, есть ли ключ "error" в дополнительных данных
                        if (additionalData.containsKey("error")) {
                            val errorMessage = additionalData.getString("error")
                            // Обрабатываем ошибку
                            Log.e("AAAAA", "Ошибка при авторизации: $errorMessage")
                        }
                    }
                    // Проверяем код ошибки (если таковой есть)
                    val errorCode = result.data?.getIntExtra("error_code", -1)
                    if (errorCode != -1) {
                        // Обрабатываем ошибку по коду
                        when (errorCode) {
                            // Добавьте обработку других возможных кодов ошибок
                            // ...
                            else -> {
                                // Обработка для неизвестного кода ошибки
                                Log.e("AAAAA", "Неизвестный код ошибки: $errorCode")
                            }
                        }
                    }
                }
            }
        }

//        launcher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            ParseGoogleUtils.onActivityResult(result.resultCode, result.data)
////            Timber.d("GOOGLE ${result.resultCode}")
//        }



//        binding.googleBtn.setOnClickListener { loginWithGoogle() }
        binding.googleBtn.setOnClickListener { signInWithGoogle() }
        binding.facebookBtn?.setOnClickListener {
            ParseUser.logOut()
            val user = ParseUser.getCurrentUser()
            Log.e("AAAAA", "Пользователь после нажатия кнопки разлогина ${user}")
        }

        binding.loginbtn.setOnClickListener {
//            TODO пробую открыть с помощью экстеншена
            this.showBottomSheetDialog(R.layout.fragment_bottom_sheet_test)

//            findNavController().navigate(
//                R.id.action_navigation_courses_to_bottomSheetTest,
//                Bundle(),
//                navOption()
//            )
        }




        return root
    }
    private fun navOption() = NavOptions.Builder().setRestoreState(true).setLaunchSingleTop(true).build()

    private fun loginWithGoogle() {
        activity?.let {
            ParseGoogleUtils.logIn(it, launcher!!) { user, error ->
                it.runOnUiThread {
                    binding.signin.text = user.toString()
                }
            }
        }

    }

    private fun loginInBack4AppWithTutorialMethod(account: GoogleSignInAccount) {
        val authData = mutableMapOf<String, String>()
        authData["id"] = account.id!!
        authData["id_token"] = account.idToken!!
        ParseUser.logInWithInBackground("google", authData).continueWith {
                task->
            when {
                task.isCompleted -> {
                    val user = task.result
//                    .apply {
//                        email = account.email
//                        username = account.displayName
//                        put("imageUrl", account.photoUrl.toString())
//                        saveInBackground()
//                    }
                    user.deleteInBackground()

                    Log.d("AAAAA", "Залогинено в back4App, Пользователь: Имя ${user.get("username")} Новый пользователь:${user.isNew}.  ")

//                    onSignInCallbackResult(user, null)
//                    val user1 = ParseUser.getCurrentUser()
//                    Log.e("AAAAA", "CurrentUser ${user1.username} ${user1.email} ${user1.sessionToken} ${user1.isAuthenticated} ${user1.isNew} ")

                }
                task.isFaulted -> {
                    Log.e("AAAAA", "logInWithInBackground isFaulted ")
//                    onSignInCallbackResult(null, task.error)
                }
                else -> {
                    Log.e("AAAAA", "logInWithInBackground else ")
//                    onSignInCallbackResult(null, null)
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        signInClient.signOut()
        launcher?.launch(signInClient.signInIntent)
    }

//    private fun loginWithGoogle() {
//        activity?.let {
//            ParseGoogleUtils.logIn(it, launcher!!) { user, error ->
//                it.runOnUiThread {
//                    when {
//                        error != null -> {
//                            Log.e("AAAAA","An error occurred when signing up/logging in with  $error")
//
//                        }
//                        user == null -> {
//                            Log.d("AAAAA","Cancelled signup/login with google")
////                            publishNotLoading()
////                            publishEvent(SplashViewEvent.SignupLoginCancelled)
//                        }
//                        user.isNew -> {
//                            Log.d("AAAAA","Cancelled signup/login with google NEW")
////                            when (authType) {
////                                AuthType.FB,
////                                AuthType.GOOGLE -> signupNewUser(authType, user)
////                                else -> throw IllegalArgumentException("Unsupported social network authorization type: google")
////                            }
//                        }
//                        else -> {
////                            analyticsTracker.trackEvent(LoginSignupAnalyticsEvent(authType))
////                            proceedAfterSignupLogin(authType)
//                        }
//                    }
//
//
//                }
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}











////Это версия класса в которой используется Api SignIn похожая на версию Анвара
////Полностью рабочая
//class CoursesFragment : Fragment() {
//    companion object {
//        private const val AUTH_TYPE = "google"
//
////        "google"
//        private const val SERVER_CLIENT_ID =
//            "414952582173-8aa2iifkra3hcooujdmo1bccqvkp7dlh.apps.googleusercontent.com"
////        "414952582173-vttui74un9tlq6oak418fknnp68brlhj.apps.googleusercontent.com"
//
//
//        private const val AUTHORIZATION_TOKEN =
//            "ya29.a0AfB_byD6-Mxb47uV-36cyiend_NYTyZgTT7mPEComO40qKqM_olRCSoPaArRMT3_PYBBH2E4GjglBjkRDY0LHqoafZpwaTcpy9QnDcQV3Yl2XfhGvOevT3GDPF1GI5Zv6rgQIzrQC7d8uuVa3pmoIqVK8hfcbCYTbbTaaCgYKAdcSARASFQHGX2MiDqHIfHtFybn2BYhJPFNETA0171"
//    }
//
//    private var _binding: FragmentCoursesBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var credentialManager: CredentialManager
//    private lateinit var coursesViewModel:  CoursesViewModel
//
//    private var currentCallback: LogInCallback? = null
//
//    lateinit var googleIdTokenCredential: GoogleIdTokenCredential
//
//    val idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4YTYzYmM0NzY3Zjg1NTBhNTMyZGM2MzBjZjdlYjQ5ZmYzOTdlN2MiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MTQ5NTI1ODIxNzMtdnR0dWk3NHVuOXRscTZvYWs0MThma25ucDY4YnJsaGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MTQ5NTI1ODIxNzMtOGFhMmlpZmtyYTNoY29vdWpkbW8xYmNjcXZrcDdkbGguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ5MDE3NTgzNjk5NDI0ODMyMzAiLCJlbWFpbCI6Ijc5MjcyMzczMjk0dkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlZhZGltIEt1em5ldHNvdiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJLU1HRU9tc0sweDh5aUY3NWlGcnJ1VFB6cG16U1BuZnRFRWFrVmxIOHY9czk2LWMiLCJnaXZlbl9uYW1lIjoiVmFkaW0iLCJmYW1pbHlfbmFtZSI6Ikt1em5ldHNvdiIsImxvY2FsZSI6InJ1IiwiaWF0IjoxNzA1MzkyODU3LCJleHAiOjE3MDUzOTY0NTd9.EItCkbK5ZzSjHGfaBDrfGy8TV2yp-16FIUAV2j_jzjI8e2qI2vNL3R2WxKdb089ljrqY1rF7u1fuRnsHzw81_mMJtclOjv4WP9n1Q8Oncy9pSt0Stdg4scwpqmX1i7l79A42WVQtNJzt0y7ZMHaS-1BYWTvLaTuJ6zyBB4vBvirra9UI1JZr-scXh88QYTwamBJdg_dk4V5xbFr2swv2Pq20R_6i1J5loqOtnRyVdwUrI3a253QgrGYmWQpfJKaVvz4SdOOtZdhWM7b6ivlIvmRZ0qWeqiTWNTGVm__YWFPmqQBOnk5-zZsxGexoDJ-ja7v-UREXKl3U-spWhgrkcw"
//    private var launcher: ActivityResultLauncher<Intent>? = null
//    lateinit var googleSignInClient: GoogleSignInClient
//
////    lateinit var auth: FireBaseAuth
//
//    private fun getClient(): GoogleSignInClient {
//        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestScopes(Scope(Scopes.EMAIL))
////            Вот этот токен влияет, и если он не верный авторизация будет отменена
////            Причем работает именно последний автосгенерированный файрбейзом токен
//            .requestIdToken("414952582173-kgk6sbucpv6hmsa2fplq98rtljl5t55o.apps.googleusercontent.com")
//            .requestEmail()
//            .build()
//
//        return GoogleSignIn.getClient(requireActivity(), gso)
//    }
////    414952582173-kgk6sbucpv6hmsa2fplq98rtljl5t55o.apps.googleusercontent.com
////    414952582173-8aa2iifkra3hcooujdmo1bccqvkp7dlh.apps.googleusercontent.com
////Вот это андроид токен не работает
//    //    414952582173-vttui74un9tlq6oak418fknnp68brlhj.apps.googleusercontent.com
//// Канцелед приходил из -за неверного ключа?
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
//
//        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.text
//        coursesViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        val currentUser = ParseUser.getCurrentUser()
//        Log.d("AAAAA", "Текущий пользователь $currentUser")
////        ParseUser.logOut()
//
//        launcher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                // Успешная авторизация
////                ParseGoogleUtils.onActivityResult(result.resultCode, result.data)
//                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//                try {
//                    val account = task.getResult(ApiException::class.java)
//                    Log.d("AAAAA", "Успешная авторизация id ${account.id}")
//                    Log.d("AAAAA", "Успешная авторизация id Token ${account.idToken}")
//                    loginInBack4AppWithTotorialMethod(account.id, account.idToken)
//
//                }
//                catch (e:ApiException){
//                    Log.d("AAAAA", "Api Exception $e")
//
//                }
//            } else if (result.resultCode == Activity.RESULT_CANCELED) {
//                // Авторизация отменена
//                Log.e("AAAAA", "Авторизация отменена")
//
//                // Проверяем, есть ли данные в Intent
//                if (result.data != null) {
//                    val additionalData = result.data?.extras
//                    if (additionalData != null) {
//                        // Пример: проверяем, есть ли ключ "error" в дополнительных данных
//                        if (additionalData.containsKey("error")) {
//                            val errorMessage = additionalData.getString("error")
//                            // Обрабатываем ошибку
//                            Log.e("AAAAA", "Ошибка при авторизации: $errorMessage")
//                        }
//                    }
//                    // Проверяем код ошибки (если таковой есть)
//                    val errorCode = result.data?.getIntExtra("error_code", -1)
//                    if (errorCode != -1) {
//                        // Обрабатываем ошибку по коду
//                        when (errorCode) {
//                            // Добавьте обработку других возможных кодов ошибок
//                            // ...
//                            else -> {
//                                // Обработка для неизвестного кода ошибки
//                                Log.e("AAAAA", "Неизвестный код ошибки: $errorCode")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
////        binding.googleBtn.setOnClickListener { loginWithGoogle() }
//                binding.googleBtn.setOnClickListener { signInWithGoogle() }
//
//
//        return root
//    }
//
//    private fun loginInBack4AppWithTotorialMethod(id: String?, idToken: String?) {
//        val authData = mutableMapOf<String, String>()
//        authData["id"] = id!!
//        authData["id_token"] = idToken!!
//        ParseUser.logInWithInBackground("google", authData).continueWith {
//                task->
//            when {
//                task.isCompleted -> {
//                    val user = task.result
//                    Log.d("AAAAA", "isCompleted $user ")
//
//                    Log.d("AAAAA", "logInWithInBackground isCompleted ")
//
////                    onSignInCallbackResult(user, null)
//                    val user1 = ParseUser.getCurrentUser()
//                    Log.e("AAAAA", "CurrentUser ${user1.username} ${user1.email} ${user1.sessionToken} ${user1.isAuthenticated} ${user1.isNew} ")
//
//                }
//                task.isFaulted -> {
//                    Log.e("AAAAA", "logInWithInBackground isFaulted ")
////                    onSignInCallbackResult(null, task.error)
//                }
//                else -> {
//                    Log.e("AAAAA", "logInWithInBackground else ")
////                    onSignInCallbackResult(null, null)
//                }
//            }
//        }
//    }
//
//    private fun signInWithGoogle() {
//        val signInClient = getClient()
//        launcher?.launch(signInClient.signInIntent)
//    }
//
//    private fun loginWithGoogle() {
//        activity?.let {
//            ParseGoogleUtils.logIn(it, launcher!!) { user, error ->
//                it.runOnUiThread {
//                    when {
//                        error != null -> {
//                            Log.e("AAAAA","An error occurred when signing up/logging in with  $error")
//
//                        }
//                        user == null -> {
//                            Log.d("AAAAA","Cancelled signup/login with google")
////                            publishNotLoading()
////                            publishEvent(SplashViewEvent.SignupLoginCancelled)
//                        }
//                        user.isNew -> {
//                            Log.d("AAAAA","Cancelled signup/login with google NEW")
////                            when (authType) {
////                                AuthType.FB,
////                                AuthType.GOOGLE -> signupNewUser(authType, user)
////                                else -> throw IllegalArgumentException("Unsupported social network authorization type: google")
////                            }
//                        }
//                        else -> {
////                            analyticsTracker.trackEvent(LoginSignupAnalyticsEvent(authType))
////                            proceedAfterSignupLogin(authType)
//                        }
//                    }
//
//
//                }
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


//class CoursesFragment : Fragment() {
//    companion object {
//        private const val AUTH_TYPE = "google"
//
//        //        "google"
//        private const val SERVER_CLIENT_ID =
////            "414952582173-8aa2iifkra3hcooujdmo1bccqvkp7dlh.apps.googleusercontent.com"
//        "414952582173-kgk6sbucpv6hmsa2fplq98rtljl5t55o.apps.googleusercontent.com"
////        "414952582173-vttui74un9tlq6oak418fknnp68brlhj.apps.googleusercontent.com"
//
//
//        private const val AUTHORIZATION_TOKEN =
//            "ya29.a0AfB_byD6-Mxb47uV-36cyiend_NYTyZgTT7mPEComO40qKqM_olRCSoPaArRMT3_PYBBH2E4GjglBjkRDY0LHqoafZpwaTcpy9QnDcQV3Yl2XfhGvOevT3GDPF1GI5Zv6rgQIzrQC7d8uuVa3pmoIqVK8hfcbCYTbbTaaCgYKAdcSARASFQHGX2MiDqHIfHtFybn2BYhJPFNETA0171"
//    }
//
////    Web
//
////    414952582173-8aa2iifkra3hcooujdmo1bccqvkp7dlh.apps.googleusercontent.com
////Andr
////    414952582173-vttui74un9tlq6oak418fknnp68brlhj.apps.googleusercontent.com
//
//    private var _binding: FragmentCoursesBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var credentialManager: CredentialManager
//    private lateinit var coursesViewModel:  CoursesViewModel
//
//    private var currentCallback: LogInCallback? = null
//
//    lateinit var googleIdTokenCredential: GoogleIdTokenCredential
//
//    val idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4YTYzYmM0NzY3Zjg1NTBhNTMyZGM2MzBjZjdlYjQ5ZmYzOTdlN2MiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MTQ5NTI1ODIxNzMtdnR0dWk3NHVuOXRscTZvYWs0MThma25ucDY4YnJsaGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MTQ5NTI1ODIxNzMtOGFhMmlpZmtyYTNoY29vdWpkbW8xYmNjcXZrcDdkbGguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ5MDE3NTgzNjk5NDI0ODMyMzAiLCJlbWFpbCI6Ijc5MjcyMzczMjk0dkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlZhZGltIEt1em5ldHNvdiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJLU1HRU9tc0sweDh5aUY3NWlGcnJ1VFB6cG16U1BuZnRFRWFrVmxIOHY9czk2LWMiLCJnaXZlbl9uYW1lIjoiVmFkaW0iLCJmYW1pbHlfbmFtZSI6Ikt1em5ldHNvdiIsImxvY2FsZSI6InJ1IiwiaWF0IjoxNzA1MzkyODU3LCJleHAiOjE3MDUzOTY0NTd9.EItCkbK5ZzSjHGfaBDrfGy8TV2yp-16FIUAV2j_jzjI8e2qI2vNL3R2WxKdb089ljrqY1rF7u1fuRnsHzw81_mMJtclOjv4WP9n1Q8Oncy9pSt0Stdg4scwpqmX1i7l79A42WVQtNJzt0y7ZMHaS-1BYWTvLaTuJ6zyBB4vBvirra9UI1JZr-scXh88QYTwamBJdg_dk4V5xbFr2swv2Pq20R_6i1J5loqOtnRyVdwUrI3a253QgrGYmWQpfJKaVvz4SdOOtZdhWM7b6ivlIvmRZ0qWeqiTWNTGVm__YWFPmqQBOnk5-zZsxGexoDJ-ja7v-UREXKl3U-spWhgrkcw"
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
//
//        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.text
//        coursesViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//
//        credentialManager = CredentialManager.create(requireActivity())
//        initCredential()
////        ParseUser.getCurrentUser()
//        ParseUser.logOut()
//
//
////        val authData = hashMapOf<String, String>()
////        authData["access_token"] = AUTHORIZATION_TOKEN
////        authData["id"] = idToken
////        ParseUser.logInWithInBackground("google", authData)
//
//
//
//        return root
//    }
//
//    private fun initCredential(){
//        binding.googleBtn.setOnClickListener {
//            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(true)
//                .setServerClientId(SERVER_CLIENT_ID)
//                .build()
//
//            val request: GetCredentialRequest = GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build()
//
//            CoroutineScope(Dispatchers.Main).launch {
//                try {
//                    Log.e("AAAAA", "Вход")
//                    val result = credentialManager.getCredential(
//                        request = request,
//                        context = requireContext(),
//                    )
//                    Log.e("AAAAA", "Вход $result")
//
//
//                    handleSignIn(result)
//                } catch (e: GetCredentialException) {
//                    Log.e("AAAAA", e.message.toString())
//                }
//            }
//
//
//        }
//
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.e("AAAAA", "onActivityResult")
//        if(resultCode == Activity.RESULT_OK){
//            Log.e("AAAAA", "onActivityResultSuccess")
//        }
//        else{
//            Log.e("AAAAA", "onActivityResultError")
//        }
//    }
//
//    private fun handleSignIn(result: GetCredentialResponse) {
//        // Handle the successfully returned credential.
//        when (val credential = result.credential) {
//            is PublicKeyCredential -> {
//                // Share responseJson such as a GetCredentialResponse on your server to
//                // validate and authenticate
//                credential.authenticationResponseJson
//                Log.e("AAAAA", "PublicKeyCredential")
//            }
//
//            is PasswordCredential -> {
//                // Send ID and password to your server to validate and authenticate.
//                val username = credential.id
//                val password = credential.password
//                Log.e("AAAAA", "PasswordCredential")
//
//            }
//
//            is CustomCredential -> {
//                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                    try {
//
//                        // Use googleIdTokenCredential and extract id to validate and
//                        // authenticate on your server.
//                        val googleIdTokenCredential = GoogleIdTokenCredential
//                            .createFrom(credential.data)
////                        Вот сюда попадаем когда все ок
//                        Log.e("AAAAA", "googleIdTokenCredential $googleIdTokenCredential")
//                        this.googleIdTokenCredential = googleIdTokenCredential
//                        sendUserDataToBack(googleIdTokenCredential)
//
//                    } catch (e: GoogleIdTokenParsingException) {
//                        Log.e("AAAAA", "Received an invalid google id token response", e)
//                    }
//                } else {
//                    // Catch any unrecognized custom credential type here.
//                    Log.e("AAAAA", "Unexpected type of credential")
//                }
//            }
//
//            else -> {
//                // Catch any unrecognized credential type here.
//                Log.e("AAAAA", "Unexpected type of credential")
//            }
//        }
//    }
//
//    private fun getAuthData(account: GoogleIdTokenCredential): Map<String, String> {
//
//        val authData = mutableMapOf<String, String>()
//        authData["id"] = account.id
//        authData["id_token"] = account.idToken!!
////        account.id.let { authData["email"] = it }
////        account.profilePictureUri?.let { authData["photo_url"] = it.toString() }
//        return authData
//    }
//
//    private fun sendUserDataToBack(googleIdTokenCredential: GoogleIdTokenCredential) {
////        val authData = hashMapOf<String, String>()
////        authData["access_token"] =
////            "104901758369942483230"
////        "ya29.a0AfB_byBmmURgZTSX5OBhuFstNo9lOCWjG54FVnoh8i48sICKpxCctau1zfzlULc0XdT9f1bWPFvJIQhfOJmiy56eAIDLIejEhZNz1iq70ggv3tckgARNm4jytjR5wB8gVzFehSKKNe1CNc9QU3ys8LWFWx-_7R5111JyaCgYKAYwSARASFQHGX2MioaXrvarqZjj9Kyj3LwjayA0171"
////        79272373294v@gmail.com
//        //        С ответа гугла
////                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4YTYzYmM0NzY3Zjg1NTBhNTMyZGM2MzBjZjdlYjQ5ZmYzOTdlN2MiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MTQ5NTI1ODIxNzMtdnR0dWk3NHVuOXRscTZvYWs0MThma25ucDY4YnJsaGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MTQ5NTI1ODIxNzMtOGFhMmlpZmtyYTNoY29vdWpkbW8xYmNjcXZrcDdkbGguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ5MDE3NTgzNjk5NDI0ODMyMzAiLCJlbWFpbCI6Ijc5MjcyMzczMjk0dkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlZhZGltIEt1em5ldHNvdiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJLU1HRU9tc0sweDh5aUY3NWlGcnJ1VFB6cG16U1BuZnRFRWFrVmxIOHY9czk2LWMiLCJnaXZlbl9uYW1lIjoiVmFkaW0iLCJmYW1pbHlfbmFtZSI6Ikt1em5ldHNvdiIsImxvY2FsZSI6InJ1IiwiaWF0IjoxNzA1NDIzMDExLCJleHAiOjE3MDU0MjY2MTF9.WOuAj9sHA2DI-mbEgskEi3Hn8kjBwaiP9NwgSduE2v86zX_itLrjM9-X4Y3ytDhAcus-pYDk577Rm-mE4ClRxd2Lxo81uvqTmJe3SG0SZJLcJk4GUT1t9mDYBGCOr-YYdxZyDfEOeHFLL1uY-_5C3QvSeZ1y694Dryzb4lEvvxB1itbBrTAI_94xXYnM2gDlMrBEnee3yjOlzT8QoyIQRzwzl9GlVloGH7N-A92bfeCmlOtdiLOslLHCTw27q_3qjItIABQ-rMSJYmO3NQ1ecH2VXJeRQknz_H_0rIeZ0FuIKeRE1d4Pz3dF5BGK4DzuGey4u36sdqL97J09zoB6xA"
//
////        authData["id"] =
////                        "79272373294v@gmail.com"
////            "ya29.a0AfB_byBmmURgZTSX5OBhuFstNo9lOCWjG54FVnoh8i48sICKpxCctau1zfzlULc0XdT9f1bWPFvJIQhfOJmiy56eAIDLIejEhZNz1iq70ggv3tckgARNm4jytjR5wB8gVzFehSKKNe1CNc9QU3ys8LWFWx-_7R5111JyaCgYKAYwSARASFQHGX2MioaXrvarqZjj9Kyj3LwjayA0171"
//
////        С ответа гугла
////                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ4YTYzYmM0NzY3Zjg1NTBhNTMyZGM2MzBjZjdlYjQ5ZmYzOTdlN2MiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MTQ5NTI1ODIxNzMtdnR0dWk3NHVuOXRscTZvYWs0MThma25ucDY4YnJsaGouYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MTQ5NTI1ODIxNzMtOGFhMmlpZmtyYTNoY29vdWpkbW8xYmNjcXZrcDdkbGguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDQ5MDE3NTgzNjk5NDI0ODMyMzAiLCJlbWFpbCI6Ijc5MjcyMzczMjk0dkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlZhZGltIEt1em5ldHNvdiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJLU1HRU9tc0sweDh5aUY3NWlGcnJ1VFB6cG16U1BuZnRFRWFrVmxIOHY9czk2LWMiLCJnaXZlbl9uYW1lIjoiVmFkaW0iLCJmYW1pbHlfbmFtZSI6Ikt1em5ldHNvdiIsImxvY2FsZSI6InJ1IiwiaWF0IjoxNzA1NDIzMDExLCJleHAiOjE3MDU0MjY2MTF9.WOuAj9sHA2DI-mbEgskEi3Hn8kjBwaiP9NwgSduE2v86zX_itLrjM9-X4Y3ytDhAcus-pYDk577Rm-mE4ClRxd2Lxo81uvqTmJe3SG0SZJLcJk4GUT1t9mDYBGCOr-YYdxZyDfEOeHFLL1uY-_5C3QvSeZ1y694Dryzb4lEvvxB1itbBrTAI_94xXYnM2gDlMrBEnee3yjOlzT8QoyIQRzwzl9GlVloGH7N-A92bfeCmlOtdiLOslLHCTw27q_3qjItIABQ-rMSJYmO3NQ1ecH2VXJeRQknz_H_0rIeZ0FuIKeRE1d4Pz3dF5BGK4DzuGey4u36sdqL97J09zoB6xA"
////            "104901758369942483230"
////        authData["id_token"] = googleIdTokenCredential.idToken
////        authData["id"] = UUID.randomUUID().toString()
////        google
////        anonymous
//        ParseUser.logInWithInBackground("google", getAuthData(googleIdTokenCredential)).continueWith {
//                task->
//            when {
//                task.isCompleted -> {
//
//                    val user = task.result
//                    Log.e("AAAAA", "isCompleted $user ")
//
//                    Log.e("AAAAA", "logInWithInBackground isCompleted ")
//
//                    onSignInCallbackResult(user, null)
//                    val user1 = ParseUser.getCurrentUser()
//                    Log.e("AAAAA", "CurrentUser ${user1.username} ${user1.email} ${user1.sessionToken} ${user1.isAuthenticated} ${user1.isNew} ")
//
//                }
//                task.isFaulted -> {
//                    Log.e("AAAAA", "logInWithInBackground isFaulted ")
//                    onSignInCallbackResult(null, task.error)
//                }
//                else -> {
//                    Log.e("AAAAA", "logInWithInBackground else ")
//                    onSignInCallbackResult(null, null)
//                }
//            }
//        }
//    }
//
//    private fun onSignInCallbackResult(user: ParseUser?, exception: Exception?) {
//        val exceptionToThrow = when (exception) {
//            is ParseException -> {
//                Log.e("AAAAA", "onSignInCallbackResult $exception ")
//                exception
//
//            }
//            null -> {
//                Log.e("AAAAA", "onSignInCallbackResult null ")
////                Тут сохранить пользователя или найти если такого нет?
//
////                val person = ParseObject("Person")
////                person.put("user", googleIdTokenCredential.displayName?:"")
////                person.put("age", 17)
////                person.put("Family", googleIdTokenCredential.familyName?:"")
////                person.saveInBackground()
//                null
//            }
//            else -> {
//                val oo = ParseException(exception)
//                Log.e("AAAAA", "onSignInCallbackResult $oo")
//                ParseException(exception)
//
//            }
//        }
//        currentCallback?.done(user, exceptionToThrow)
//        Log.e("AAAAA", "onSignInCallbackResult $currentCallback ")
//
//    }
//
////            .onSuccess {
////                Log.e("AAAAA", "ParseUser Success")
////            }
//
//
////            .continueWith{ task ->
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
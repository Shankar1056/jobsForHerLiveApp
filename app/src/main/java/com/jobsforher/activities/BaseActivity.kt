package women.jobs.jobsforher.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
   // Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      window.statusBarColor = Color.WHITE
    }

  }

  fun openChromeTab(url: String)
  {
    var builder: CustomTabsIntent.Builder  = CustomTabsIntent.Builder()
    var customTabsIntent:CustomTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
  }


  private val handleAppCrash = Thread.UncaughtExceptionHandler { thread, ex ->
    Log.d("TAGG", "ERROR" + ex.toString())
    //send email here
    getErrorMessgae(ex)

  }

  private fun getErrorMessgae(e: Throwable) {
    var stackTrackElementArray = e.stackTrace
    var crashLog = e.toString() + "\n\n"
    crashLog += "--------- Stack trace ---------\n\n"
    for (i in stackTrackElementArray.indices) {
      crashLog += "    " + stackTrackElementArray[i].toString() + "\n"
    }
    crashLog += "-------------------------------\n\n"

    // If the exception was thrown in a background thread inside
    // AsyncTask, then the actual exception can be found with getCause
    crashLog += "--------- Cause ---------\n\n"
    val cause = e.cause
    if (cause != null) {
      crashLog += cause.toString() + "\n\n"
      stackTrackElementArray = cause.stackTrace
      for (i in stackTrackElementArray.indices) {
        crashLog += ("    " + stackTrackElementArray[i].toString()
                + "\n")
      }
    }
    crashLog += "-------------------------------\n\n"
    Log.d("TAGG","ERROR"+crashLog)
    sendmail(crashLog)
  }

  fun sendmail (data:String){
    /*ACTION_SEND action to launch an email client installed on your Android device.*/
    val mIntent = Intent(Intent.ACTION_SEND)
    /*To send an email you need to specify mailto: as URI using setData() method
    and data type will be to text/plain using setType() method*/
    mIntent.data = Uri.parse("mailto:")
    mIntent.type = "text/plain"
    // put recipient email in intent
    /* recipient is put as array because you may wanna send email to multiple emails
       so enter comma(,) separated emails, it will be stored in array*/
    val to = "snehapm13@gmail.com"
    val addressees = arrayOf(to)
    mIntent.putExtra(Intent.EXTRA_EMAIL, addressees)
    //put the Subject in the intent
    mIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash reports")
    //put the message in the intent
    mIntent.putExtra(Intent.EXTRA_TEXT, data)


    try {
      //start email intent
      startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
    }
    catch (e: Exception){
      //if any thing goes wrong for example no email client application or any exception
      //get and show exception message
      Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
    }
  }
}
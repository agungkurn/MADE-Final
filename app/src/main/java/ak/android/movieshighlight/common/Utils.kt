package ak.android.movieshighlight.common

import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast

/**
 * @return log for debug
 */
fun Any.log(logMsg: String) {
    Log.d(this::class.java.simpleName, logMsg)
}

/**
 * @return log for error
 */
fun Any.errLog(errorMsg: String) {
    Log.e(this::class.java.simpleName, errorMsg)
}

/**
 * @return toast and show
 */
fun Context.toast(msg: String) {
    Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
}

/**
 * @return show the view
 */
fun View.show() {
    visibility = VISIBLE
}

/**
 * @return hide the view
 */
fun View.hide() {
    visibility = GONE
}
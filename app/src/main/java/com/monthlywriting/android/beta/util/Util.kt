package com.monthlywriting.android.beta.util

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.monthlywriting.android.beta.alarm.AlarmReceiver
import com.monthlywriting.android.beta.alarm.AlarmReceiver.Companion.MONTHLY_NOTIFICATION
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentDate
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentMonth
import com.monthlywriting.android.beta.util.CurrentInfo.Companion.currentYear
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

fun Context.hideKeyboard(view: View?) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun getBitmapFromPath(filePath: String): Bitmap? {
    val f = File(filePath)
    val options = BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.ARGB_8888

    return BitmapFactory.decodeStream(FileInputStream(f), null, options)
}

fun checkPermission(activity: Activity, execute: () -> Unit) {
    if (ContextCompat.checkSelfPermission(activity.applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_DENIED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            10
        )
    } else {
        execute()
    }
}

fun saveBitmapInNewFile(context: Context, bitmap: Bitmap, fileName: String): String {
    val file = File(context.filesDir, fileName)
    val imgPath = file.absolutePath
    var outputStream: OutputStream? = null

    try {
        file.createNewFile()
        outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    } finally {
        outputStream?.close()
    }

    return imgPath
}

fun getGalleryLauncher(
    context: Context, fragment: Fragment, insertData: (String) -> Unit,
): ActivityResultLauncher<Intent> {

    return fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data!!.data
            if (uri != null) {
                val bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            context.contentResolver,
                            uri
                        )
                    )

                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }

                val bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true)

                //사진 저장 -> 사진 크기 조절할 것
                val filePath = saveBitmapInNewFile(context,
                    bitmap,
                    System.currentTimeMillis().toString())
                insertData(filePath)
            }
        }
    }
}

fun launchGalleryLauncher(galleryLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    intent.type = "image/*"
    galleryLauncher.launch(intent)
}

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

fun setNotification(context: Context, on: Boolean) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, MONTHLY_NOTIFICATION, intent, 0)

    when (on){
        true -> {
            val cal = Calendar.getInstance()
            cal.set(currentYear, currentMonth - 1, currentDate)
            val day = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 6

            val calendar: Calendar = Calendar.getInstance().apply {
                set(currentYear, currentMonth - 1, day, 22, 0, 0)
            }

            val triggerTime = calendar.timeInMillis

            alarmManager.set(AlarmManager.RTC, triggerTime, pendingIntent)
        }
        false -> {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}

fun setTextViewColor(context: Context, textView: TextView, @ColorRes colorResource:  Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        textView.setTextColor(ContextCompat.getColor(context, colorResource))
    } else {
        textView.setTextColor(context.resources.getColor(colorResource))
    }
}
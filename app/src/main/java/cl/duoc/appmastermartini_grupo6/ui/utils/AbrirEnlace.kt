package cl.duoc.appmastermartini_grupo6.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun AbrirEnlace (context: Context, url: String): Boolean{
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
        true
    } catch (e: ActivityNotFoundException){
        false
    }
}
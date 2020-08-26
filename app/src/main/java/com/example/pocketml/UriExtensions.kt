package com.example.pocketml

import android.net.Uri

fun Uri.isLocal(): Boolean = this.scheme == "content"

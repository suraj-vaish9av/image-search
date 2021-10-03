package com.kringleimagesearch.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.kringleimagesearch.R

@GlideModule
class MyGlideAppModule : AppGlideModule()
{
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 20 // 20mb
        val diskCacheSizeBytes = 1024 * 1024 * 40
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))
        builder.setDefaultRequestOptions(requestOptions())
    }

    private fun requestOptions(): RequestOptions {
        return RequestOptions()
            .signature(
                ObjectKey(
                    System.currentTimeMillis() / (24 * 60 * 60 * 1000)
                )
            )
            .placeholder(R.drawable.bg_placeholder)
            .error(R.drawable.bg_placeholder)
            .encodeFormat(Bitmap.CompressFormat.PNG)
            .encodeQuality(100)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .format(PREFER_ARGB_8888)
            .skipMemoryCache(false)
    }
}
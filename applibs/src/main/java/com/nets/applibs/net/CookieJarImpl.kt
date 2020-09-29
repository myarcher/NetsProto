package com.nets.applibs.net

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.*

class CookieJarImpl() : CookieJar {
    private val memoryCookies: MutableMap<String?, MutableList<Cookie>?> = HashMap()
    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val oldCookies: MutableList<Cookie>? = this.memoryCookies[url.host]
        val needRemove: MutableList<Cookie?> = ArrayList<Cookie?>()
        val var5: Iterator<*> = cookies.iterator()

        while (var5.hasNext()) {
            val newCookie = var5.next() as Cookie
            val var7: Iterator<*> = oldCookies!!.iterator()
            while (var7.hasNext()) {
                val oldCookie = var7.next() as Cookie
                if (newCookie.name == oldCookie.name) {
                    needRemove.add(oldCookie)
                }
            }
        }

        oldCookies!!.removeAll(needRemove)
        oldCookies.addAll(cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        var cookies: MutableList<Cookie>? = memoryCookies[url.host]
        if (cookies == null) {
            cookies = ArrayList<Cookie>()
            memoryCookies[url.host] = cookies
        }
        return cookies
    }

}

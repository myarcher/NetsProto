package com.nets.applibs.glides

import com.bumptech.glide.request.RequestOptions

class GlidOptions : RequestOptions {
    constructor(init: RequestOptions.() -> Unit) : super() {
        init()
    }
}
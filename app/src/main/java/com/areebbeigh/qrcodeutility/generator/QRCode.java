/**
 * Copyright 2016 Areeb Beigh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.areebbeigh.qrcodeutility.generator;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.areebbeigh.qrcodeutility.generator.schemes.QRCodeScheme;
import com.areebbeigh.qrcodeutility.zxingadditional.Contents;
import com.areebbeigh.qrcodeutility.zxingadditional.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;

import java.util.Map;

public class QRCode {
    public final static int DEFAULT_BG = 0xFFFFFFFF;
    public final static int DEFAULT_FG = 0xFF000000;
    public static int foreground = DEFAULT_FG;
    public static int background = DEFAULT_BG;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;

    private String str;

    public QRCode(QRCodeScheme codeObject) {
        this(codeObject.toString());
    }

    public QRCode(String string) {
        this.str = string;
    }

    @Override
    public String toString() {
        return "QRCode{" + "str='" + str + '\'' + '}';
    }

    public Bitmap getSimpleBitmap(@Nullable Map<EncodeHintType, Object> hints) throws WriterException {
        return getSimpleBitmap(DEFAULT_FG, hints);
    }

    public Bitmap getSimpleBitmap(int foregroundColor, @Nullable Map<EncodeHintType, Object> hints) throws WriterException {

        QRCodeEncoder qrCode = new QRCodeEncoder(str, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), HEIGHT); // HEIGHT AND WIDTH

        return qrCode.encodeAsBitmap(foregroundColor, hints);
    }
}

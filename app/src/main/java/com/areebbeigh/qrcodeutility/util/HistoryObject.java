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

package com.areebbeigh.qrcodeutility.util;

import android.graphics.Bitmap;

import com.areebbeigh.qrcodeutility.generator.QRCode;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

/**
 * An object representing an item in the history list
 */
public class HistoryObject {
    private String text;
    private String ecl;
    private int foreground;

    public HistoryObject(String text, String ecl) {
        this(text, ecl, QRCode.DEFAULT_FG);
    }

    public HistoryObject(String text, String ecl, int foreground) {
        this.text = text;
        this.ecl = ecl;
        this.foreground = foreground;
    }

    /**
     * Returns a {@link QRCode} object of the item
     */
    public QRCode getAsQRCodeObject() {
        return new QRCode(getText());
    }

    /**
     * Returns a bitmap of the QR Code
     */
    public Bitmap getBitmap() throws WriterException {
        String eclString = getEcl();
        ErrorCorrectionLevel ecl;

        switch (eclString) {
            case "M":
                ecl = ErrorCorrectionLevel.M;
                break;
            case "Q":
                ecl = ErrorCorrectionLevel.Q;
                break;
            case "H":
                ecl = ErrorCorrectionLevel.H;
                break;
            default:
                ecl = ErrorCorrectionLevel.L;
                break;
        }

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ecl);

        return getAsQRCodeObject().getSimpleBitmap(foreground, hints);
    }

    public String getEcl() {
        return ecl;
    }

    public String getText() {
        return text;
    }

    public int getForeground() {
        return foreground;
    }
}
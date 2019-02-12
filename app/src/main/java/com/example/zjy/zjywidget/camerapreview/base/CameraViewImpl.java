/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zjy.zjywidget.camerapreview.base;

import android.content.Context;
import android.view.View;

import java.util.Set;

public abstract class CameraViewImpl {

    protected final Callback mCallback;

    protected final PreviewImpl mPreview;

    public CameraViewImpl(Callback callback, PreviewImpl preview) {
        mCallback = callback;
        mPreview = preview;
    }

    public View getView() {
        return mPreview.getView();
    }

    /**
     * @return {@code true} if the implementation was able to start the camera session.
     */
    public abstract boolean start();

    public abstract void stop();

    public abstract boolean isCameraOpened();

    public abstract void setFacing(int facing);

    public abstract int getFacing();

    public abstract Set<AspectRatio> getSupportedAspectRatios();

    /**
     * @return {@code true} if the aspect ratio was changed.
     */
    public abstract boolean setAspectRatio(AspectRatio ratio);

    public abstract AspectRatio getAspectRatio();

    public abstract void setAutoFocus(boolean autoFocus);

    public abstract boolean getAutoFocus();

    public abstract void setFlash(int flash);

    public abstract int getFlash();

    public abstract void takePicture();

    public abstract void setDisplayOrientation(int displayOrientation);

    public interface Callback {

        void onCameraOpened();

        void onCameraClosed();

        void onPictureTaken(byte[] data);

        Context getContext();

    }

}

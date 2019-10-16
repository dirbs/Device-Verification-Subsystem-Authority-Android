/*
 * Copyright (c) 2018-2019 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment is required by displaying the trademark/log as per the details provided here: [https://www.qualcomm.com/documents/dirbs-logo-and-brand-guidelines]
 *  Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 *  This notice may not be removed or altered from any source distribution.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.dvsauthorized.mUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.RegistrationResponse;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An example persistence mechanism for an {@link AuthState} instance.
 * This stores the instance in a shared preferences file, and provides thread-safe access and
 * mutation.
 */
public class AuthStateManager {

    private static final AtomicReference<WeakReference<AuthStateManager>> INSTANCE_REF =
            new AtomicReference<>(new WeakReference<>((AuthStateManager) null));

    private static final String TAG = "AuthStateManager";
    private static final String STORE_NAME = "AuthState";
    private static final String KEY_STATE = "state";

    private final SharedPreferences mPrefs;
    private final ReentrantLock mPrefsLock;
    private final AtomicReference<AuthState> mCurrentAuthState;

    @AnyThread
    public static AuthStateManager getInstance(@NonNull Context context) {
        AuthStateManager manager = INSTANCE_REF.get().get();
        if (manager == null) {
            manager = new AuthStateManager(context.getApplicationContext());
            INSTANCE_REF.set(new WeakReference<>(manager));
        }
        return manager;
    }

    private AuthStateManager(Context context) {
        mPrefs = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        mPrefsLock = new ReentrantLock();
        mCurrentAuthState = new AtomicReference<>();
    }

    @AnyThread
    @NonNull
    public AuthState getCurrent() {
        if (mCurrentAuthState.get() != null) {
            return mCurrentAuthState.get();
        }

        AuthState state = readState();
        if (mCurrentAuthState.compareAndSet(null, state)) {
            return state;
        } else {
            return mCurrentAuthState.get();
        }
    }

    @AnyThread
    @NonNull
    public AuthState replace(@NonNull AuthState state) {
        writeState(state);
        mCurrentAuthState.set(state);
        return state;
    }

    @AnyThread
    @NonNull
    public AuthState updateAfterAuthorization(
            @Nullable AuthorizationResponse response,
            @Nullable AuthorizationException ex) {
        AuthState current = getCurrent();
        current.update(response, ex);
        return replace(current);
    }

    @AnyThread
    @NonNull
    public AuthState updateAfterTokenResponse(
            @Nullable TokenResponse response,
            @Nullable AuthorizationException ex) {
        AuthState current = getCurrent();
        current.update(response, ex);
        return replace(current);
    }

    @AnyThread
    @NonNull
    public AuthState updateAfterRegistration(
            RegistrationResponse response,
            AuthorizationException ex) {
        AuthState current = getCurrent();
        if (ex != null) {
            return current;
        }

        current.update(response);
        return replace(current);
    }

    @AnyThread
    @NonNull
    private AuthState readState() {
        mPrefsLock.lock();
        try {
            String currentState = mPrefs.getString(KEY_STATE, null);
            if (currentState == null) {
                return new AuthState();
            }
            try {
                return AuthState.jsonDeserialize(currentState);
            } catch (JSONException ex) {
                Log.w(TAG, "Failed to deserialize stored auth state - discarding");
                return new AuthState();
            }
        } finally {
            mPrefsLock.unlock();
        }
    }

    @AnyThread
    private void writeState(@Nullable AuthState state) {
        mPrefsLock.lock();
        try {
            SharedPreferences.Editor editor = mPrefs.edit();
            if (state == null) {
                editor.remove(KEY_STATE);
            } else {
                editor.putString(KEY_STATE, state.jsonSerializeString());
            }
            if (!editor.commit()) {
                throw new IllegalStateException("Failed to write state to shared prefs");
            }
        } finally {
            mPrefsLock.unlock();
        }
    }
}
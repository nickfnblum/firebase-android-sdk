// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.appdistribution;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appdistribution.internal.FirebaseAppDistributionProxy;

/**
 * The Firebase App Distribution API provides methods to update the app to the most recent
 * pre-release build.
 *
 * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in your
 * build, then all methods will be stubs and the {@link Task Tasks} and {@link UpdateTask
 * UpdateTasks} will fail with {@link FirebaseAppDistributionException.Status#NOT_IMPLEMENTED}.
 *
 * <p>By default, Firebase App Distribution is automatically initialized.
 *
 * <p>Call {@link #getInstance()} to get the singleton instance of {@link FirebaseAppDistribution}.
 */
public interface FirebaseAppDistribution {
  /**
   * Updates the app to the newest release, if one is available.
   *
   * <p>Returns the release information or {@code null} if no update is found. Performs the
   * following actions:
   *
   * <ol>
   *   <li>If tester is not signed in, presents the tester with a Google Sign-in UI.
   *   <li>Checks if a newer release is available. If so, presents the tester with a confirmation
   *       dialog to begin the download.
   *   <li>If the newest release is an APK, downloads the binary and starts an installation. If the
   *       newest release is an AAB, directs the tester to the Play app to complete the download and
   *       installation.
   * </ol>
   *
   * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in
   * your build, then this method returns a failed {@link Task} with {@link
   * FirebaseAppDistributionException.Status#NOT_IMPLEMENTED}.
   */
  @NonNull
  UpdateTask updateIfNewReleaseAvailable();

  /**
   * Returns {@code true} if the App Distribution tester is signed in.
   *
   * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in
   * your build, then this method always returns {@code false}.
   */
  boolean isTesterSignedIn();

  /**
   * Signs in the App Distribution tester. Presents the tester with a Google sign in UI.
   *
   * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in
   * your build, then this method returns a failed {@link Task} with {@link
   * FirebaseAppDistributionException.Status#NOT_IMPLEMENTED}.
   */
  @NonNull
  Task<Void> signInTester();

  /**
   * Signs out the App Distribution tester.
   *
   * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in
   * your build, then this method is a no-op.
   */
  void signOutTester();

  /**
   * Returns an {@link AppDistributionRelease} if an update is available for the current signed in
   * tester, or {@code null} otherwise.
   *
   * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in
   * your build, then this method returns a failed {@link Task} with {@link
   * FirebaseAppDistributionException.Status#NOT_IMPLEMENTED}.
   */
  @NonNull
  Task<AppDistributionRelease> checkForNewRelease();

  /**
   * Updates app to the {@link AppDistributionRelease} returned by {@link #checkForNewRelease}.
   *
   * <p>If the newest release is an APK, downloads the binary and starts an installation. If the
   * newest release is an AAB, directs the tester to the Play app to complete the download and
   * installation.
   *
   * <p>Fails the {@link Task} with {@link
   * FirebaseAppDistributionException.Status#UPDATE_NOT_AVAILABLE} if no new release is cached from
   * {@link #checkForNewRelease}.
   *
   * <p>If you don't include the {@code com.google.firebase:firebase-appdistribution} artifact in
   * your build, then this method returns a failed {@link UpdateTask} with {@link
   * FirebaseAppDistributionException.Status#NOT_IMPLEMENTED}.
   */
  @NonNull
  UpdateTask updateApp();

  /**
   * Takes a screenshot, and starts an activity to collect and submit feedback from the tester.
   *
   * <p>Performs the following actions:
   *
   * <ol>
   *   <li>Takes a screenshot of the current activity
   *   <li>If tester is not signed in, presents the tester with a Google Sign-in UI
   *   <li>Looks up the currently installed App Distribution release
   *   <li>Starts a full screen activity for the tester to compose and submit the feedback
   * </ol>
   */
  void collectAndSendFeedback();

  /** Gets the singleton {@link FirebaseAppDistribution} instance. */
  @NonNull
  static FirebaseAppDistribution getInstance() {
    return FirebaseApp.getInstance().get(FirebaseAppDistributionProxy.class);
  }
}

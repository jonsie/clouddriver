/*
 * Copyright 2017 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.aws.deploy.userdata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 * Implementations of this interface will provide user data to instances during the deployment
 * process
 */
public interface UserDataProvider {

  /**
   * Provide user data from the specified request.
   *
   * @param userDataRequest {@link UserDataRequest}
   * @return String
   */
  default String getUserData(UserDataRequest userDataRequest) {
    return "";
  }

  @Builder
  @JsonDeserialize(builder = UserDataRequest.UserDataRequestBuilder.class)
  @Value
  class UserDataRequest {
    String asgName;
    String launchSettingName;
    String region;
    String account;
    String environment;
    String accountType;
    Boolean launchTemplate;
    Boolean legacyUdf;
    boolean overrideDefaultUserData;
    String base64UserData;
    String iamRole;
    String imageId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserDataRequestBuilder {}
  }
}

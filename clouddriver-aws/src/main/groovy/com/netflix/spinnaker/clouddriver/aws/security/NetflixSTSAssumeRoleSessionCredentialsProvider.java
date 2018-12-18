/*
 * Copyright 2016 Netflix, Inc.
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

package com.netflix.spinnaker.clouddriver.aws.security;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NetflixSTSAssumeRoleSessionCredentialsProvider implements AWSSessionCredentialsProvider {
  @Value("${sts.regionOverride:#{null}}")
  String regionOverride;

  private final STSAssumeRoleSessionCredentialsProvider delegate;
  private final String accountId;

  public NetflixSTSAssumeRoleSessionCredentialsProvider(AWSCredentialsProvider longLivedCredentialsProvider,
                                                        String roleArn,
                                                        String roleSessionName,
                                                        String accountId) {
    this.accountId = accountId;
    AWSSecurityTokenService stsClient = createStsClient(longLivedCredentialsProvider);
    delegate = new STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, roleSessionName).withStsClient(stsClient).build();
  }

  public String getAccountId() {
    return accountId;
  }

  @Override
  public AWSSessionCredentials getCredentials() {
    return delegate.getCredentials();
  }

  @Override
  public void refresh() {
    delegate.refresh();
  }

  private AWSSecurityTokenService createStsClient(AWSCredentialsProvider longLivedCredentialsProvider) {
    AWSSecurityTokenServiceClientBuilder builder = AWSSecurityTokenServiceClient.builder();
    if (regionOverride != null) {
      return builder.withCredentials(longLivedCredentialsProvider).withRegion(regionOverride).build();
    } else {
      return builder.withCredentials(longLivedCredentialsProvider).build();
    }
  }
}

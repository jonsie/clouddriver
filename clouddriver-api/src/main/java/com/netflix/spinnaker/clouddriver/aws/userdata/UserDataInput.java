package com.netflix.spinnaker.clouddriver.aws.userdata;

import lombok.Builder;
import lombok.Value;


@Builder
@Value
public class UserDataInput {
  String asgName;
  String launchSettingName;
  String region;
  String account;
  String environment;
  String accountType;
  Boolean launchTemplate;
  Boolean legacyUdf;
  UserDataOverride userDataOverride;
  String base64UserData;
  String iamRole;
  String imageId;
}
